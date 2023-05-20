package com.pika.gstore.common.config;

import com.pika.gstore.common.prooerties.DomainProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * ConditionalOnProperty 匹配 domain.enable!=false,
 * <p>
 * 若配置matchIfMissing = true (不存在domain.enable时也匹配),只要 domain.enable!=false就行
 *
 * @author pikachu
 * @since 2023/4/13 12:55
 */
@ConditionalOnProperty(prefix = "domain", name = "enable")
@ConditionalOnMissingBean(DomainProperties.class)
@EnableConfigurationProperties(DomainProperties.class)
@Configuration
public class DomainAutoConfiguration {
}


