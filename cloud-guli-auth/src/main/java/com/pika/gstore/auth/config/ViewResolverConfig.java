package com.pika.gstore.auth.config;

import com.pika.gstore.common.constant.DomainConstant;
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
    public void myViewConfig( ThymeleafViewResolver thymeleafViewResolver) {
        if (thymeleafViewResolver != null) {
            Map<String, Object> map = new HashMap<>(8);
            map.put("AUTH_DOMAIN", DomainConstant.AUTH_DOMAIN);
            map.put("ORDER_DOMAIN", DomainConstant.ORDER_DOMAIN);
            map.put("ITEM_DOMAIN", DomainConstant.ITEM_DOMAIN);
            map.put("CART_DOMAIN", DomainConstant.CART_DOMAIN);
            map.put("MEMBER_DOMAIN", DomainConstant.MEMBER_DOMAIN);
            map.put("SECKILL_DOMAIN", DomainConstant.SECKILL_DOMAIN);
            map.put("MAIN_DOMAIN", DomainConstant.MAIN_DOMAIN);
            map.put("SEARCH_DOMAIN", DomainConstant.SEARCH_DOMAIN);
            map.put("REDIRECT_URL", DomainConstant.REDIRECT_URL);
            thymeleafViewResolver.setStaticVariables(map);
        }
    }
}
