package com.pika.gstore.auth.config;

import com.pika.gstore.common.filter.AuthFilter;
import com.pika.gstore.common.prooerties.DomainProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/14 0:16
 */
@Configuration
public class MyWebConfig {
    @Resource
    private DomainProperties domainProperties;

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter(domainProperties.getAuthServiceUrl(), domainProperties.getGatewayUrl(), domainProperties.getAuth());
    }
}
