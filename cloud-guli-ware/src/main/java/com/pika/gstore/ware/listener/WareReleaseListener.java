package com.pika.gstore.ware.listener;

import com.pika.gstore.common.constant.MqConstant;
import com.pika.gstore.common.to.mq.StockLockedTo;
import com.pika.gstore.ware.service.WareSkuService;
import com.pika.gstore.common.to.mq.OrderTo;
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
@RabbitListener(queues = MqConstant.STOCK_RELEASE_STOCK_QUEUE)
@Service
@Slf4j
public class WareReleaseListener {
    @Resource
    private WareSkuService wareSkuService;

    /**
     * Desc: 处理库存服务发出的库存锁定消息,订单不存在/已取消时需要解锁库存
     */
    @RabbitHandler
    public void releaseStock(StockLockedTo to, Message message, Channel channel) throws IOException {
        try {
            log.warn("收到待解锁任务:" + to.getTaskId().toString());
            wareSkuService.releaseStock(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    /**
     * 防止订单创建缓慢,库存解锁消息先过期
     */
    @RabbitHandler
    public void releaseStock(OrderTo vo, Message message, Channel channel) throws IOException {
        try {
            log.warn("收到待解锁订单:" + vo.getOrderSn());
            wareSkuService.releaseStock(vo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
