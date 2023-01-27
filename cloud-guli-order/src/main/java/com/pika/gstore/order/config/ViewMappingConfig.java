package com.pika.gstore.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/16 16:29
 */
@Configuration
public class ViewMappingConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/detail.html").setViewName("detail");
        registry.addViewController("/list.html").setViewName("list");
        registry.addViewController("/confirm.html").setViewName("confirm");
        WebMvcConfigurer.super.addViewControllers(registry);
    }
}
