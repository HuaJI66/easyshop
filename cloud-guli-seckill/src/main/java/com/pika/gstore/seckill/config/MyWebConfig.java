package com.pika.gstore.seckill.config;

import com.pika.gstore.common.prooerties.DomainProperties;
import com.pika.gstore.seckill.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/3 23:34
 */
@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    @Resource
    private DomainProperties domainProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(domainProperties)).addPathPatterns("/**")
                .excludePathPatterns("/seckill/sku/**", "/getCurrSkus**");
    }
}
