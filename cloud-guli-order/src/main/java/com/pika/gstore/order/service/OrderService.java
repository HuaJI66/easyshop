package com.pika.gstore.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.to.OrderTo;
import com.pika.gstore.common.to.es.SeckillOrderTo;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.order.entity.OrderEntity;
import com.pika.gstore.order.vo.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 22:34:09
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    OrderSubmitRepVo orderSubmit(OrderSubmitVo vo);

    OrderEntity getOrderByOrderSn(String orderSn);

    boolean closeOrder(OrderTo order);

    Object getPayVo(String orderSn, Integer payType);

    PageUtils currUserOrderItemList(Map<String, Object> params);

    void updateOrderStatus(String orderSn, Integer status);

    Boolean createSeckillOrder(SeckillOrderTo order);
}

