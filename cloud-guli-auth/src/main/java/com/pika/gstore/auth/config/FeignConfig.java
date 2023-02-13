package com.pika.gstore.auth.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        return Logger.Level.BASIC;
    }
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // 解决seata的xid未传递
//            String xid = RootContext.getXID();
//            template.header(RootContext.KEY_XID, xid);

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                String cookie = attributes.getRequest().getHeader("Cookie");
                template.header("Cookie", cookie);
            }
        };
    }
}
