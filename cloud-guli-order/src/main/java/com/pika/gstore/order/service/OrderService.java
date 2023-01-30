package com.pika.gstore.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.order.entity.OrderEntity;
import com.pika.gstore.order.vo.OrderConfirmVo;
import com.pika.gstore.order.vo.OrderSubmitRepVo;
import com.pika.gstore.order.vo.OrderSubmitVo;

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

    OrderEntity getOrderStatus(String orderSn);

    boolean closeOrder(OrderEntity order);
}

