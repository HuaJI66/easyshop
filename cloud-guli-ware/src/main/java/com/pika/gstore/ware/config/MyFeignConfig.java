package com.pika.gstore.ware.config;

import feign.Logger;
import feign.RequestInterceptor;
import io.seata.core.context.RootContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/22 21:34
 */
@Configuration
public class MyFeignConfig {
    @Bean
    public Logger.Level level() {
        return Logger.Level.BASIC;
    }
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // 解决seata的xid未传递
            String xid = RootContext.getXID();
            if (StringUtils.isEmpty(xid)) {
                template.header(RootContext.KEY_XID, xid);
            }

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                String cookie = attributes.getRequest().getHeader("Cookie");
                template.header("Cookie", cookie);
            }
        };
    }
}

