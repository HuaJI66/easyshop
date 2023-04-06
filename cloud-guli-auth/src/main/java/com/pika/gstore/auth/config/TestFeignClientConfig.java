package com.pika.gstore.auth.config;

import feign.Client;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.context.annotation.Bean;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author pikachu
 * @since 2023/4/6 22:53
 */
public class TestFeignClientConfig {
    // 加载自定义Client
    @Bean
    @ConditionalOnBean(FeignConfiguration.class)
    public Client generateClient(FeignConfiguration ignoreHttpsSSLClient, CachingSpringLoadBalancerFactory cachingFactory,
                                 SpringClientFactory clientFactory) throws NoSuchAlgorithmException, KeyManagementException {

        return ignoreHttpsSSLClient.feignClient(cachingFactory, clientFactory);
    }
}
