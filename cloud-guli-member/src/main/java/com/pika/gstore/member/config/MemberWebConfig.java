package com.pika.gstore.member.config;

import com.pika.gstore.common.prooerties.DomainProperties;
import com.pika.gstore.member.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/14 0:16
 */
@Configuration
public class MemberWebConfig implements WebMvcConfigurer {
    @Resource
    private DomainProperties domainProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(domainProperties)).addPathPatterns("/**").excludePathPatterns("/member/**").excludePathPatterns("/img/**");
    }
}
