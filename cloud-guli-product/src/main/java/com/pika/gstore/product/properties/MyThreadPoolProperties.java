package com.pika.gstore.product.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/9 10:29
 */
@ConfigurationProperties(prefix = "gulimall.thread")
@Component
@Data
public class MyThreadPoolProperties {
    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Long keepAliveTime;
    private TimeUnit unit = TimeUnit.SECONDS;
}
