package com.pika.gstore.ware.config;

import com.pika.gstore.common.constant.MqConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/28 14:32
 */
@Configuration
@EnableRabbit
public class WareMqConfig {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initRabbitTemplate() {
        //消息抵达broker回调
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {

        });
        //消息没有抵达队列回调
        rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey) -> {

        });
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
