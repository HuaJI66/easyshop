package com.pika.gstore.order.config;

import com.pika.gstore.order.properties.MyThreadPoolProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/9 10:21
 */
@Configuration
public class MyThreadConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(MyThreadPoolProperties poolProperties) {
        return new ThreadPoolExecutor(
                poolProperties.getCorePoolSize(),
                poolProperties.getMaximumPoolSize(),
                poolProperties.getKeepAliveTime(),
                poolProperties.getUnit(),
                new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
