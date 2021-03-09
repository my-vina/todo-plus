package com.foraixh.todo.plus.configuration;

import com.google.common.collect.Sets;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.net.MalformedURLException;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author myvina
 * @date 2021/02/23 16:33
 * @usage 访问微软api所需bean 管理
 */
public class MicrosoftGraphConfiguration {
    private final static String AUTHORITY = "https://login.microsoftonline.com/common/";

    @Value("${todo-plus.app.id}")
    private String appId;
    @Value("${todo-plus.app.scopes}")
    private String[] scopes;

    @Bean
    public GraphServiceClientFactory graphServiceClientFactory() {
        return new GraphServiceClientFactory();
    }

    @Bean
    public PublicClientApplication microsoftClientApplication(ExecutorService microsoftAppThreadPool) throws MalformedURLException {
        // Build the MSAL application object with
        // app ID and authority
        return PublicClientApplication.builder(appId)
                    .authority(AUTHORITY)
                    .executorService(microsoftAppThreadPool)
                    .build();
    }

    @Bean
    public ExecutorService microsoftAppThreadPool() {
        return new ThreadPoolExecutor(10, 20,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new DefaultThreadFactory("todo-plus"));
    }

    /**
     * 默认的线程池工厂类；可自定义线程名称前缀
     */
    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory(String prefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = prefix + "-pool-" +
                    POOL_NUMBER.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
