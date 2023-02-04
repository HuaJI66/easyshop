package com.pika.gstore.order.controller;

import com.pika.gstore.order.config.AlipayTemplate;
import com.pika.gstore.order.entity.OrderEntity;
import com.pika.gstore.order.entity.OrderReturnReasonEntity;
import com.rabbitmq.client.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/16 11:43
 */
@Controller
@Slf4j
@RabbitListeners(value = {@RabbitListener(queuesToDeclare = {@Queue(name = "test.queue1", durable = "true", autoDelete = "false")}),
        @RabbitListener(queues = {"test.queue1"})
})
public class TestController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @RabbitHandler
    public void getMsg1(Message message, OrderEntity order, Channel channel) {
        log.info(new String(message.getBody()));
        log.info(message.getMessageProperties().toString());
        log.info(order.toString());
        log.info(channel.toString());
    }

    @RabbitHandler
    public void getMsg2(Message message, OrderReturnReasonEntity order, Channel channel) {
        log.info(new String(message.getBody()));
        log.info(message.getMessageProperties().toString());
        log.info(order.toString());
        log.info(channel.toString());
    }

    @GetMapping("send/{num}")
    public void send(@PathVariable Integer num) {
        if (num % 2 == 0) {
            rabbitTemplate.convertAndSend("test.direct.exchange", "test#", new OrderEntity());
        } else {
            rabbitTemplate.convertAndSend("test.direct.exchange", "test#", new OrderReturnReasonEntity());
        }
    }
}
