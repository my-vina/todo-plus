package com.foraixh.todo.plus.service.impl;

import com.foraixh.todo.plus.component.RedisDelayedQueue;
import com.foraixh.todo.plus.constant.MicrosoftGraphConstants;
import com.foraixh.todo.plus.service.TokenService;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.aad.msal4j.RefreshTokenParameters;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author myvina@qq.com
 * @date 2021/3/12  6:56
 * @usage
 */

@Service
public class TokenServiceImpl implements TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);

    private final Gson gson = new Gson();

    private final RedissonClient redissonClient;
    private final RedisDelayedQueue redisDelayedQueue;
    private final PublicClientApplication pca;

    @Value("${todo-plus.app.scopes}")
    private String[] scopes;

    public TokenServiceImpl(RedissonClient redissonClient, RedisDelayedQueue redisDelayedQueue,
                            PublicClientApplication pca) {
        this.redissonClient = redissonClient;
        this.redisDelayedQueue = redisDelayedQueue;
        this.pca = pca;
    }

    @Override
    public void tokenStorageScheduleRefresh(IAuthenticationResult result) {
        JsonObject authResult = gson.fromJson(gson.toJson(result), JsonObject.class);

        RMap<String, Object> tokenMap = redissonClient.getMap(result.account().username());
        tokenMap.expire(Instant.ofEpochSecond(authResult.get(MicrosoftGraphConstants.EXPIRES_ON).getAsLong()));
        tokenMap.put(MicrosoftGraphConstants.ACCESS_TOKEN, result.accessToken());
        tokenMap.put(MicrosoftGraphConstants.REFRESH_TOKEN, authResult.get(MicrosoftGraphConstants.REFRESH_TOKEN).getAsString());
        log.info("{}的认证token已存储到redis", result.account().username());

        long expiresOn = authResult.get(MicrosoftGraphConstants.EXPIRES_ON).getAsLong();
        long now = Instant.now().getEpochSecond();
        long expire = expiresOn - now;
        long delay = expire - 60;

        redisDelayedQueue.addQueue(
                authResult.get(MicrosoftGraphConstants.REFRESH_TOKEN).getAsString(),
                delay,
                TimeUnit.SECONDS,
                MicrosoftGraphConstants.TOKEN_REDIS_SCHEDULE_REFRESH_QUEUE
        );
        log.info("已设置{}的token定时刷新计划；下次执行时间：{}秒后", result.account().username(), delay);
    }

    @Override
    public void refreshToken(String refreshToken) {
        // TODO 刷新的时候设置锁，防止使用时被刷新
        pca.acquireToken(RefreshTokenParameters.builder(Sets.newHashSet(scopes), refreshToken).build())
                .exceptionally(e -> {
                    throw new RuntimeException("token：\"" + refreshToken + "\"刷新失败", e);
                })
                .thenAcceptAsync((IAuthenticationResult result) -> {
                    log.info("开始刷新{}的token", result.account().username());
                    this.tokenStorageScheduleRefresh(result);
                    log.info("{}的token刷新成功", result.account().username());
                });
    }
}
