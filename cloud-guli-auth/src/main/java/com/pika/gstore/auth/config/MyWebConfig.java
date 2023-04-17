package com.pika.gstore.auth.config;

import com.pika.gstore.common.filter.AuthFilter;
import com.pika.gstore.common.prooerties.DomainProperties;
import org.springframework.beans.factory.annotation.Value;
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
    /**
     * 认证服务端口,用于登录过滤器放行，如: http://127.0.0.1:10000/
     */
    @Value("${auth.filter.authServiceUrl}")
    private String authServiceUrl;
    /**
     * 网关服务端口，用于登录过滤器放行,如: http://127.0.0.1:5000/
     */
    @Value("${auth.filter.gatewayUrl}")
    private String gatewayUrl;

    /**
     * 其它需要放行的url，多个用逗号分割，可用正则
     */
    @Value("${auth.filter.otherUrl}")
    private String othersUrl = "";

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter(authServiceUrl, gatewayUrl, othersUrl, domainProperties.getAuth());
    }
}
