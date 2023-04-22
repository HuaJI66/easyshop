package com.pika.gstore.common.config;

import com.pika.gstore.common.prooerties.DomainProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pi'ka'chu
 */
@Configuration
@Slf4j
@Order
@ConditionalOnClass(ThymeleafViewResolver.class)
@AutoConfigureAfter(value = {DomainAutoConfiguration.class, ThymeleafAutoConfiguration.class})
public class ViewResolverConfig implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ThymeleafViewResolver thymeleafViewResolver = context.getBean(ThymeleafViewResolver.class);
        DomainProperties domainProperties = context.getBean(DomainProperties.class);
        log.warn("配置 Thymeleaf 域名全局变量.....");
        Map<String, Object> map = new HashMap<>(8);
        map.put("AUTH_DOMAIN", domainProperties.getAuth());
        map.put("ORDER_DOMAIN", domainProperties.getOrder());
        map.put("ITEM_DOMAIN", domainProperties.getItem());
        map.put("CART_DOMAIN", domainProperties.getCart());
        map.put("MEMBER_DOMAIN", domainProperties.getMember());
        map.put("SECKILL_DOMAIN", domainProperties.getSeckill());
        map.put("MAIN_DOMAIN", domainProperties.getMain());
        map.put("SEARCH_DOMAIN", domainProperties.getSearch());
        map.put("WARE_DOMAIN", domainProperties.getWare());
        map.put("REDIRECT_URL", domainProperties.getRedirectUrl());
        thymeleafViewResolver.setStaticVariables(map);
    }
}
