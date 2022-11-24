package com.pika.gstore.member.dao;

import com.pika.gstore.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 21:24:18
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
