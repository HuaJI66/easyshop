package com.pika.gstore.member.config;

import com.pika.gstore.member.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/14 0:16
 */
@Configuration
public class MemberWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/member/**").excludePathPatterns("/img/**");
    }
}
