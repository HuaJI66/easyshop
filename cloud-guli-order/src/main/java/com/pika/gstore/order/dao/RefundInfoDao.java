package com.pika.gstore.order.dao;

import com.pika.gstore.order.entity.RefundInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款信息
 * 
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 22:34:09
 */
@Mapper
public interface RefundInfoDao extends BaseMapper<RefundInfoEntity> {
	
}
