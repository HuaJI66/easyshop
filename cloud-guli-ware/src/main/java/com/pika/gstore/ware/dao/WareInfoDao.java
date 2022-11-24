package com.pika.gstore.ware.dao;

import com.pika.gstore.ware.entity.WareInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库信息
 * 
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-22 08:44:12
 */
@Mapper
public interface WareInfoDao extends BaseMapper<WareInfoEntity> {
	
}
