package com.pika.gstore.common.prooerties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import javax.annotation.PostConstruct;

/**
 * @author pikachu
 * @since 2023/4/13 10:02
 */
@ConfigurationProperties(prefix = "domain")
@RefreshScope
@Data
@Slf4j
public class DomainProperties {
    private String base;
    private String main;
    private String auth;
    private String search;
    private String item;
    private String cart;
    private String order;
    private String member;
    private String seckill;
    /**
     * 认证服务端口,用于登录过滤器放行，如: http://127.0.0.1:10000/
     */
    private String authServiceUrl = "http://127.0.0.1:10000/";
    /**
     * 网关服务端口，用于登录过滤器放行,如: http://127.0.0.1:5000/
     */
    private String gatewayUrl = "http://127.0.0.1:5000/";
    private String redirectUrl = "redirect_url";

    @PostConstruct
    public void init() {
        log.warn("baseDomain:{}", base);
        log.warn("mainDomain:{}", main);
        log.warn("authDomain:{}", auth);
        log.warn("searchDomain:{}", search);
        log.warn("itemDomain:{}", item);
        log.warn("cartDomain:{}", cart);
        log.warn("orderDomain:{}", order);
        log.warn("memberDomain:{}", member);
        log.warn("seckillDomain:{}", seckill);
        log.warn("authServiceUrl:{}", authServiceUrl);
        log.warn("gatewayUrl:{}", gatewayUrl);
    }
}
