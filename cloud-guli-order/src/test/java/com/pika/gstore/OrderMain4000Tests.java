package com.pika.gstore;

import java.math.BigDecimal;
import java.util.Date;

import com.pika.gstore.order.entity.OrderEntity;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class OrderMain4000Tests {
    private final String routingKey = "test#";
    private String testDirectExchange = "test.direct.exchange";
    private String testQueue1 = "test.queue1";
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private AmqpAdmin amqpAdmin;

    @Test
    public void test5() {
        amqpAdmin.declareExchange(ExchangeBuilder.directExchange(testDirectExchange).build());
        amqpAdmin.declareQueue(QueueBuilder.durable(testQueue1).build());
        Exchange exchange = ExchangeBuilder.directExchange(testDirectExchange).build();
        Queue queue = QueueBuilder.durable(testQueue1).build();
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs());
        OrderEntity order = new OrderEntity();
        {
            order.setId(0L);
            order.setMemberId(0L);
            order.setOrderSn("");
            order.setCouponId(0L);
            order.setCreateTime(new Date());
            order.setMemberUsername("");
            order.setTotalAmount(new BigDecimal("0"));
            order.setPayAmount(new BigDecimal("0"));
            order.setFreightAmount(new BigDecimal("0"));
            order.setPromotionAmount(new BigDecimal("0"));
            order.setIntegrationAmount(new BigDecimal("0"));
            order.setCouponAmount(new BigDecimal("0"));
            order.setDiscountAmount(new BigDecimal("0"));
            order.setPayType(0);
            order.setSourceType(0);
            order.setStatus(0);
            order.setDeliveryCompany("");
            order.setDeliverySn("");
            order.setAutoConfirmDay(0);
            order.setIntegration(0);
            order.setGrowth(0);
            order.setBillType(0);
            order.setBillHeader("");
            order.setBillContent("");
            order.setBillReceiverPhone("");
            order.setBillReceiverEmail("");
            order.setReceiverName("");
            order.setReceiverPhone("");
            order.setReceiverPostCode("");
            order.setReceiverProvince("");
            order.setReceiverCity("");
            order.setReceiverRegion("");
            order.setReceiverDetailAddress("");
            order.setNote("");
            order.setConfirmStatus(0);
            order.setDeleteStatus(0);
            order.setUseIntegration(0);
            order.setPaymentTime(new Date());
            order.setDeliveryTime(new Date());
            order.setReceiveTime(new Date());
            order.setCommentTime(new Date());
            order.setModifyTime(new Date());
        }
        rabbitTemplate.convertAndSend(testDirectExchange, routingKey, order);
    }

    @Test
    void contextLoads() {
        amqpAdmin.declareExchange(ExchangeBuilder.directExchange(testDirectExchange).build());
    }

    @Test
    public void test2() {
        amqpAdmin.declareQueue(QueueBuilder.durable(testQueue1).build());
    }

    @Test
    public void test3() {
        Exchange exchange = ExchangeBuilder.directExchange(testDirectExchange).build();
        Queue queue = QueueBuilder.durable(testQueue1).build();
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs());
    }

    @Test
    public void test4() {
        OrderEntity order = new OrderEntity();
        order.setId(0L);
        order.setMemberId(0L);
        order.setOrderSn("");
        order.setCouponId(0L);
        order.setCreateTime(new Date());
        order.setMemberUsername("");
        order.setTotalAmount(new BigDecimal("0"));
        order.setPayAmount(new BigDecimal("0"));
        order.setFreightAmount(new BigDecimal("0"));
        order.setPromotionAmount(new BigDecimal("0"));
        order.setIntegrationAmount(new BigDecimal("0"));
        order.setCouponAmount(new BigDecimal("0"));
        order.setDiscountAmount(new BigDecimal("0"));
        order.setPayType(0);
        order.setSourceType(0);
        order.setStatus(0);
        order.setDeliveryCompany("");
        order.setDeliverySn("");
        order.setAutoConfirmDay(0);
        order.setIntegration(0);
        order.setGrowth(0);
        order.setBillType(0);
        order.setBillHeader("");
        order.setBillContent("");
        order.setBillReceiverPhone("");
        order.setBillReceiverEmail("");
        order.setReceiverName("");
        order.setReceiverPhone("");
        order.setReceiverPostCode("");
        order.setReceiverProvince("");
        order.setReceiverCity("");
        order.setReceiverRegion("");
        order.setReceiverDetailAddress("");
        order.setNote("");
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        order.setUseIntegration(0);
        order.setPaymentTime(new Date());
        order.setDeliveryTime(new Date());
        order.setReceiveTime(new Date());
        order.setCommentTime(new Date());
        order.setModifyTime(new Date());

        rabbitTemplate.convertAndSend(testDirectExchange, routingKey, order);
    }

}
