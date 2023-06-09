package com.pika.gstore.auth.config;

import feign.Client;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/11 22:46
 */
@Configuration
public class FeignConfiguration {
    @Bean
    public Logger.Level level() {
        return Logger.Level.BASIC;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // 解决seata的xid未传递
//            String xid = RootContext.getXID();
//            template.header(RootContext.KEY_XID, xid);

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                String cookie = attributes.getRequest().getHeader("Cookie");
                template.header("Cookie", cookie);
            }
        };
    }

    @Bean
    public CachingSpringLoadBalancerFactory cachingFactory(SpringClientFactory clientFactory) {
        return new CachingSpringLoadBalancerFactory(clientFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory,
                              SpringClientFactory clientFactory) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("SSL");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        return new LoadBalancerFeignClient(new Client.Default(ctx.getSocketFactory(),
                (hostname, session) -> true),
                cachingFactory, clientFactory);
    }
}
