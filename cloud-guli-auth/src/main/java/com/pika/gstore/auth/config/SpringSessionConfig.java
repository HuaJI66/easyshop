package com.pika.gstore.auth.config;

import com.pika.gstore.common.constant.AuthConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author pi'ka'chu
 */
@EnableRedisHttpSession
public class SpringSessionConfig {
	@Bean
	public CookieSerializer cookieSerializer(){
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setCookieName(AuthConstant.AUTH_COOKIE_NAME);
		//子域共享session
		serializer.setDomainName("gulimall.com");
		return serializer;
	}
	@Bean
	public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
		return RedisSerializer.json();
	}
	@Bean
	public LettuceConnectionFactory connectionFactory() {
		return new LettuceConnectionFactory();
	}
}
