package com.pika.gstore.coupon.dao;

import com.pika.gstore.coupon.entity.SmsSeckillSkuRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 秒杀活动商品关联
 * 
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 21:05:12
 */
@Mapper
public interface SmsSeckillSkuRelationDao extends BaseMapper<SmsSeckillSkuRelationEntity> {
	
}
