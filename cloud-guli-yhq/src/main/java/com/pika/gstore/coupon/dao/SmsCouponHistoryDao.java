package com.pika.gstore.coupon.dao;

import com.pika.gstore.coupon.entity.SmsCouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 21:05:12
 */
@Mapper
public interface SmsCouponHistoryDao extends BaseMapper<SmsCouponHistoryEntity> {
	
}
