package com.pika.gstore.order.listener;

import com.pika.gstore.common.constant.MqConstant;
import com.pika.gstore.common.to.OrderTo;
import com.pika.gstore.order.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/29 21:38
 */
@Service
@Slf4j
@RabbitListener(queues = MqConstant.ORDER_RELEASE_ORDER_QUEUE)
public class OrderMqListener {
    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void releaseOrder(OrderTo order, Message message, Channel channel) throws IOException {
        try {
            log.info("待取消order = " + order.getOrderSn());
            Boolean res = orderService.closeOrder(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
}
