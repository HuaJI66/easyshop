package com.pika.gstore.ware.config;

import com.pika.gstore.common.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/28 14:32
 */
@Configuration
@EnableRabbit
@Slf4j
public class WareMqConfig {
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory factory) {
        log.info("caching factory: {}", factory.getChannelCacheSize());
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);

        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            if (ack) {
                log.info("消息成功发送到exchange");
            } else {
                log.error("消息发送exchange失败:" + cause);
            }
        });

        /**
         * 当mandatory标志位设置为true时
         * 如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息
         * 那么broker会调用basic.return方法将消息返还给生产者
         * 当mandatory设置为false时，出现上述情况broker会直接将消息丢弃
         */
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey) -> {
            log.info("返回消息回调:{} 应答代码:{} 回复文本:{} 交换器:{} 路由键:{}", message, replyCode, replyText, exchange, routingKey);
        });
        //使用单独的发送连接，避免生产者由于各种原因阻塞而导致消费者同样阻塞
        rabbitTemplate.setUsePublisherConnection(true);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(MqConstant.STOCK_RELEASE_STOCK_QUEUE)
    public Queue queue1() {
        return QueueBuilder.durable(MqConstant.STOCK_RELEASE_STOCK_QUEUE).build();
    }

    @Bean(MqConstant.STOCK_DELAY_QUEUE)
    public Queue queue2() {
        return QueueBuilder.durable(MqConstant.STOCK_DELAY_QUEUE).deadLetterExchange(MqConstant.STOCK_EVENT_EXCHANGE).deadLetterRoutingKey(MqConstant.STOCK_RELEASE_KEY).ttl(MqConstant.STOCK_RELEASE_TTL).build();
    }

    @Bean(MqConstant.STOCK_EVENT_EXCHANGE)
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(MqConstant.STOCK_EVENT_EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding binding1(@Qualifier(MqConstant.STOCK_RELEASE_STOCK_QUEUE) Queue queue, @Qualifier(MqConstant.STOCK_EVENT_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(MqConstant.STOCK_RELEASE_X_KEY).noargs();
    }

    @Bean
    public Binding binding2(@Qualifier(MqConstant.STOCK_DELAY_QUEUE) Queue queue, @Qualifier(MqConstant.STOCK_EVENT_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(MqConstant.STOCK_LOCKED_KEY).noargs();
    }
}
