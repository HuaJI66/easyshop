/**
 * Licensed Property to China UnionPay Co., Ltd.
 * <p>
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 * All Rights Reserved.
 * <p>
 * <p>
 * Modification History:
 * =============================================================================
 * Author         Date          Description
 * ------------ ---------- ---------------------------------------------------
 * xshu       2014-05-28       MPI基本参数工具类
 * =============================================================================
 */
package com.pika.config;

import com.pika.properties.SDKConfigProperties;
import com.pika.utils.UnionPayTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author pi'ka'chu
 * @ClassName UnionPayAutoConfiguration
 * @Description acpsdk配置文件acp_sdk.properties配置信息类
 * @date 2016-7-22 下午4:04:55
 * 声明：以下代码只是为了方便接入方测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
@Configuration
@EnableConfigurationProperties({SDKConfigProperties.class})
@ConditionalOnClass(value = {UnionPayTemplate.class})
@ConditionalOnProperty(prefix = "union-pay.acpsdk", name = "enable")
public class UnionPayAutoConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(UnionPayAutoConfiguration.class);
    public static SDKConfigProperties sdkConfigProperties;

    public UnionPayAutoConfiguration(SDKConfigProperties sdkConfigProperties) {
        UnionPayAutoConfiguration.sdkConfigProperties = sdkConfigProperties;
        logger.info("sdkConfigProperties: {}", sdkConfigProperties);
    }

    public static SDKConfigProperties getConfig() {
        return sdkConfigProperties;
    }

    @ConditionalOnMissingBean(UnionPayTemplate.class)
    @Bean
    public UnionPayTemplate unionPayTemplate() {
        return new UnionPayTemplate();
    }
}

