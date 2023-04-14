package com.pika.gstore.member.config;

import com.pika.gstore.common.prooerties.DomainProperties;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pi'ka'chu
 */
@Configuration
public class ViewResolverConfig {
    @Resource
    private DomainProperties domainProperties;

    @Resource
    public void myViewConfig(ThymeleafViewResolver thymeleafViewResolver) {
        if (thymeleafViewResolver != null) {
            Map<String, Object> map = new HashMap<>(8);
            map.put("AUTH_DOMAIN", domainProperties.getAuth());
            map.put("ORDER_DOMAIN", domainProperties.getOrder());
            map.put("ITEM_DOMAIN", domainProperties.getItem());
            map.put("CART_DOMAIN", domainProperties.getCart());
            map.put("MEMBER_DOMAIN", domainProperties.getMember());
            map.put("SECKILL_DOMAIN", domainProperties.getSeckill());
            map.put("MAIN_DOMAIN", domainProperties.getMain());
            map.put("SEARCH_DOMAIN", domainProperties.getSearch());
            map.put("REDIRECT_URL", domainProperties.getRedirectUrl());
            thymeleafViewResolver.setStaticVariables(map);
        }
    }
}
