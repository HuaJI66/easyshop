package com.pika.gstore.ware.dao;

import com.pika.gstore.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-22 08:44:12
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
