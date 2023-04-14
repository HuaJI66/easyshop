package com.pika.gstore.cart.config;

import com.pika.gstore.cart.interceptor.LoginInterceptor;
import com.pika.gstore.common.prooerties.DomainProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/14 14:07
 */
@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    @Resource
    private DomainProperties domainProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(domainProperties)).addPathPatterns("/**");
    }
}
