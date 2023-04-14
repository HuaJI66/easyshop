package com.pika.gstore.common.config;

import com.pika.gstore.common.prooerties.DomainProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @author pikachu
 * @since 2023/4/13 12:55
 */
@EnableConfigurationProperties(DomainProperties.class)
@Configuration
public class DomainAutoConfiguration {
}
