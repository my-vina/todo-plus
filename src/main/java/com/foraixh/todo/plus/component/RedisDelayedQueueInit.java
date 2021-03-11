package com.foraixh.todo.plus.component;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * @author myvina
 * @date 2021/03/11 15:07
 * @usage
 */
@Component
public class RedisDelayedQueueInit implements ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(RedisDelayedQueueInit.class);

    private final RedissonClient redissonClient;
    private final ExecutorService redisDelayedQueueConsumerPool;

    public RedisDelayedQueueInit(RedissonClient redissonClient,
                                 @Qualifier("redisDelayedQueueConsumerPool") ExecutorService redisDelayedQueueConsumerPool) {
        this.redissonClient = redissonClient;
        this.redisDelayedQueueConsumerPool = redisDelayedQueueConsumerPool;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(RedisDelayedQueueListener.class).forEach(this::listenerStart);
    }

    /**
     * 启动线程获取队列*
     *
     * @param queueName                 queueName
     * @param redisDelayedQueueListener 任务回调监听
     * @param <T>                       泛型
     */
    private <T> void listenerStart(String queueName, RedisDelayedQueueListener<T> redisDelayedQueueListener) {
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(queueName);

        redisDelayedQueueConsumerPool.execute(() -> {
            while (true) {
                T t = null;
                try {
                    t = blockingFairQueue.take();
                    log.info("监听队列线程{},获取到值:{}", queueName, t);
                    redisDelayedQueueListener.accept(t);
                } catch (InterruptedException e) {
                    log.error("处理错误", e);
                }
            }
        });
    }

}
