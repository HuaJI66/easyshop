package com.pika.gstore.order.config;

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
 * @since 2023/1/15 23:54
 */
@Configuration
@EnableRabbit
public class OrderMqConfig {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 定制rabbitTemplate
     */
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

    @Bean(MqConstant.ORDER_DELAY_QUEUE)
    public Queue queue1() {
        return QueueBuilder.durable(MqConstant.ORDER_DELAY_QUEUE).deadLetterExchange(MqConstant.ORDER_EVENT_EXCHANGE).deadLetterRoutingKey(MqConstant.ORDER_RELEASE_KEY).ttl(MqConstant.ORDER_RELEASE_TTL).build();
    }

    @Bean(MqConstant.ORDER_RELEASE_ORDER_QUEUE)
    public Queue queue2() {
        return QueueBuilder.durable(MqConstant.ORDER_RELEASE_ORDER_QUEUE).build();
    }

    @Bean(MqConstant.ORDER_EVENT_EXCHANGE)
    public Exchange exchange1() {
        return ExchangeBuilder.topicExchange(MqConstant.ORDER_EVENT_EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding binding1(@Qualifier(MqConstant.ORDER_DELAY_QUEUE) Queue queue, @Qualifier(MqConstant.ORDER_EVENT_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(MqConstant.ORDER_CREATE_KEY).noargs();
    }

    @Bean
    public Binding binding2(@Qualifier(MqConstant.ORDER_RELEASE_ORDER_QUEUE) Queue queue, @Qualifier(MqConstant.ORDER_EVENT_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(MqConstant.ORDER_RELEASE_KEY).noargs();
    }

    /**
     * 绑定库存解锁队列
     */
    @Bean
    public Binding binding3(@Qualifier(MqConstant.ORDER_EVENT_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(QueueBuilder.durable(MqConstant.STOCK_RELEASE_STOCK_QUEUE).build()).to(exchange).with(MqConstant.ORDER_RELEASE_X_KEY).noargs();
    }
}
