package com.pika.gstore.auth.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/11 22:46
 */
@Configuration
public class FeignConfig {
    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }
}
