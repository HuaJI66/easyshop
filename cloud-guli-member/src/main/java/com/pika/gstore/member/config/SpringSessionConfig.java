package com.pika.gstore.member.config;

import com.pika.gstore.common.constant.AuthConstant;
import com.pika.gstore.common.prooerties.DomainProperties;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.annotation.Resource;

/**
 * @author pi'ka'chu
 */
@EnableRedisHttpSession
public class SpringSessionConfig {
    @Resource
    private DomainProperties domainProperties;

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName(AuthConstant.AUTH_COOKIE_NAME);
        //子域共享session
        serializer.setDomainName(domainProperties.getBase());
        return serializer;
    }

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return RedisSerializer.json();
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(RedisProperties properties) {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        RedisProperties.Lettuce lettuce = properties.getLettuce();
        RedisProperties.Pool pool = lettuce.getPool();
        genericObjectPoolConfig.setMaxIdle(pool.getMaxIdle());
        genericObjectPoolConfig.setMinIdle(pool.getMinIdle());
        genericObjectPoolConfig.setMaxTotal(pool.getMaxActive());
        genericObjectPoolConfig.setMaxWaitMillis(pool.getMaxWait().toMillis());
        genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(100);
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(0);
        String redisHost = properties.getHost();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(properties.getPort());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(properties.getPassword()));
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(properties.getTimeout())
                .shutdownTimeout(properties.getLettuce().getShutdownTimeout())
                .poolConfig(genericObjectPoolConfig)
                .build();

        return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);
    }
}
