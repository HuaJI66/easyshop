package com.pika.gstore.order.listener;

import com.pika.gstore.common.constant.MqConstant;
import com.pika.gstore.common.to.es.SeckillOrderTo;
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
 * @since 2023/2/4 13:58
 */
@Service
@Slf4j
@RabbitListener(queues = MqConstant.ORDER_SECKILL_QUEUE)
public class SeckillOrderListener {
    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void releaseOrder(SeckillOrderTo order, Message message, Channel channel) throws IOException {
        try {
            log.info("待生成的秒杀order = " + order.getOrderSn());
            Boolean res = orderService.createSeckillOrder(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
}
