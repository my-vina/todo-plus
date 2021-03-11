package com.foraixh.todo.plus.component;

import com.foraixh.todo.plus.constant.MicrosoftGraphConstants;
import org.springframework.stereotype.Service;

/**
 * @author myvina
 * @date 2021/03/11 17:11
 * @usage
 */
@Service(MicrosoftGraphConstants.TOKEN_REDIS_SCHEDULE_REFRESH_QUEUE)
public class TokenRefreshRedisDelayedQueueListener implements RedisDelayedQueueListener<String> {
    @Override
    public void accept(String s) {
        System.out.println("token refresh " + s);
    }
}
