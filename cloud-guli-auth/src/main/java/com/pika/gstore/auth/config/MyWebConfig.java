package com.pika.gstore.auth.config;

import com.pika.gstore.common.filter.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/14 0:16
 */
@Configuration
public class MyWebConfig {
    @Bean
    public AuthFilter authFilter(){
        return new AuthFilter();
    }
}
