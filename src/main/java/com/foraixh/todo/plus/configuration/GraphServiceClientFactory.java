package com.foraixh.todo.plus.configuration;

import com.foraixh.todo.plus.SimpleAuthProvider;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author myvina
 * @date 2021/02/23 16:46
 * @usage
 */
public class GraphServiceClientFactory {
    private static final Logger log = getLogger(GraphServiceClientFactory.class);


    private final LoadingCache<String, IGraphServiceClient> clientLoadingCache;

    public GraphServiceClientFactory() {
        this.clientLoadingCache = CacheBuilder.newBuilder()
                .initialCapacity(200)
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                // 设置并发级别为cpu核心数
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .recordStats()
                .build(new CacheLoader<String, IGraphServiceClient>() {
                    @Override
                    public IGraphServiceClient load(String token) throws Exception {
                        return createGraphServiceClient(token);
                    }
                });
    }

    public IGraphServiceClient getClient(String accessToken) {
        try {
            return clientLoadingCache.get(accessToken);
        } catch (ExecutionException e) {
            throw new RuntimeException("获取微软API访问客户端失败", e);
        }
    }

    private IGraphServiceClient createGraphServiceClient(String accessToken) {
        // Create the auth provider
        SimpleAuthProvider authProvider = new SimpleAuthProvider(accessToken);

        // Create default logger to only log errors
        DefaultLogger logger = new DefaultLogger();
        logger.setLoggingLevel(LoggerLevel.ERROR);

        // Build a Graph client
        return GraphServiceClient.builder()
                .authenticationProvider(authProvider)
                .logger(logger)
                .buildClient();
    }
}
