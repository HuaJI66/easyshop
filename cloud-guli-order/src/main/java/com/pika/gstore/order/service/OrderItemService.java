package com.pika.gstore.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.order.entity.OrderItemEntity;

import java.util.Map;

/**
 * 订单项信息
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 22:34:09
 */
public interface OrderItemService extends IService<OrderItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

