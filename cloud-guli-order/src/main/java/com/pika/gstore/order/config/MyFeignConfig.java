package com.pika.gstore.order.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/22 21:34
 */
@Configuration
public class MyFeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                String cookie = requestAttributes.getRequest().getHeader("Cookie");
                template.header("Cookie", cookie);
            }
        };
    }
}
