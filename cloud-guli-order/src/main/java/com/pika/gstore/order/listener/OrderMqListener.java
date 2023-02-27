package com.pika.gstore.order.listener;

import com.pika.gstore.common.constant.MqConstant;
import com.pika.gstore.common.enums.OrderStatusEnum;
import com.pika.gstore.common.to.OrderTo;
import com.pika.gstore.common.to.PaymentInfoTo;
import com.pika.gstore.order.entity.PaymentInfoEntity;
import com.pika.gstore.order.service.OrderService;
import com.pika.gstore.order.service.PaymentInfoService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
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
@RabbitListener(queues = {MqConstant.ORDER_RELEASE_ORDER_QUEUE, MqConstant.ORDER_STATUS_QUEUE})
public class OrderMqListener {
    @Resource
    private OrderService orderService;
    @Resource
    private PaymentInfoService paymentInfoService;

    @RabbitHandler
    public void releaseOrder(OrderTo order, Message message, Channel channel) throws IOException {
        try {
//            log.info("待取消order = " + order.getOrderSn());
            // TODO: 2023/2/27 关单前再次查询支付状态
            Boolean res = orderService.closeOrder(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }

    @RabbitHandler
    public void updateOrder(PaymentInfoTo paymentInfoTo, Message message, Channel channel) throws IOException {
        try {
            String orderSn = paymentInfoTo.getOrderSn();
            log.info("收到待更新支付状态的订单:{}", orderSn);
            //1.保存交易流失水
            PaymentInfoEntity paymentInfoEntity = new PaymentInfoEntity();
            BeanUtils.copyProperties(paymentInfoTo, paymentInfoEntity);
            paymentInfoService.save(paymentInfoEntity);
            //2.更新订单状态
            orderService.updateOrderStatus(orderSn, OrderStatusEnum.PAID.getCode());
            log.info(orderSn + "订单完成");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
}
