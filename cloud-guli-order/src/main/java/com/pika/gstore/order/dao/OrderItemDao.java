package com.pika.gstore.order.dao;

import com.pika.gstore.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 22:34:09
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
