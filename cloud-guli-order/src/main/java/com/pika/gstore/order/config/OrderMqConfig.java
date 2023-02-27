package com.pika.gstore.order.config;

import com.pika.gstore.common.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
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
 * @since 2023/1/15 23:54
 */
@Configuration
@Slf4j
public class OrderMqConfig {
    /**
     * ConfirmCallback:消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，也就是只确认是否正确到达 Exchange 中
     * 消息发送成功的回调
     * 需要开启发送确认
     * publisher-confirms: true <p>
     * ReturnCallback:启动消息失败返回，比如消息发送到 Broker 后,路由不到队列时触发回调
     * 发生异常时的消息返回提醒
     * 需要开启发送失败退回
     * publisher-returns: true
     */
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory factory) {
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
        //json消息转换
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    /**
     * 设置消息转换,不可只设置rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
     * 因为还要对消息进行还原
     */
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
        return BindingBuilder.bind(queue).to(exchange).with(MqConstant.ORDER_RELEASE_X_KEY).noargs();
    }

    /**
     * 绑定库存解锁队列
     */
    @Bean
    public Binding binding3(@Qualifier(MqConstant.ORDER_EVENT_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(QueueBuilder.durable(MqConstant.STOCK_RELEASE_STOCK_QUEUE).build()).to(exchange).with(MqConstant.ORDER_RELEASE_X_KEY).noargs();
    }

    /**
     * 秒杀削峰队列
     */
    @Bean(MqConstant.ORDER_SECKILL_QUEUE)
    public Queue queue3() {
        return QueueBuilder.durable(MqConstant.ORDER_SECKILL_QUEUE).build();
    }

    @Bean
    public Binding binding4(@Qualifier(MqConstant.ORDER_SECKILL_QUEUE) Queue queue, @Qualifier(MqConstant.ORDER_EVENT_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(MqConstant.ORDER_SECKILL_KEY).noargs();
    }

    /**
     * 订单状态更新队列
     */
    @Bean(MqConstant.ORDER_STATUS_QUEUE)
    public Queue queue4() {
        return QueueBuilder.durable(MqConstant.ORDER_STATUS_QUEUE).build();
    }

    @Bean
    public Binding binding5(@Qualifier(MqConstant.ORDER_STATUS_QUEUE) Queue queue, @Qualifier(MqConstant.ORDER_EVENT_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(MqConstant.ORDER_UPDATE_KEY).noargs();
    }
}
