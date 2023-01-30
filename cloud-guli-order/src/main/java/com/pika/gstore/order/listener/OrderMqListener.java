package com.pika.gstore.order.listener;

import cn.hutool.core.util.IdUtil;
import com.pika.gstore.common.constant.MqConstant;
import com.pika.gstore.order.entity.OrderEntity;
import com.pika.gstore.order.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/29 21:38
 */
@Controller
@Slf4j
public class OrderMqListener {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private OrderService orderService;

    @GetMapping("/test/createOrder")
    @ResponseBody
    public String create() {
        String uuid = IdUtil.fastSimpleUUID();
        OrderEntity order = new OrderEntity();
        order.setOrderSn(uuid);
        rabbitTemplate.convertAndSend(MqConstant.ORDER_EVENT_EXCHANGE,
                MqConstant.ORDER_CREATE_KEY, order);
        return uuid;
    }

    @RabbitListener(queues = MqConstant.ORDER_RELEASE_ORDER_QUEUE)
    public void releaseOrder(OrderEntity order, Message message, Channel channel) throws IOException {
        try {
            log.info("待取消order = " + order.getOrderSn());
            Boolean res = orderService.closeOrder(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
}
