package com.foraixh.todo.plus.component;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author myvina
 * @date 2021/03/11 14:47
 * @usage redis延时队列
 */
@Component
public class RedisDelayedQueue {
    private static final Logger log = LoggerFactory.getLogger(RedisDelayedQueue.class);

    private final RedissonClient redissonClient;

    public RedisDelayedQueue(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 添加队列
     *
     * @param t        DTO传输类
     * @param delay    时间数量
     * @param timeUnit 时间单位
     * @param <T>      泛型
     */
    public <T> void addQueue(T t, long delay, TimeUnit timeUnit, String queueName) {
        log.info("添加队列元素{}, delay: {}, timeUnit: {}" + queueName, delay, timeUnit);
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(queueName);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.offer(t, delay, timeUnit);
    }


}
