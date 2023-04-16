package com.pika.gstore.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pika.gstore.coupon.entity.SmsSeckillSessionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 秒杀活动场次
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 21:05:12
 */
@Mapper
public interface SmsSeckillSessionDao extends BaseMapper<SmsSeckillSessionEntity> {

    List<SmsSeckillSessionEntity> getFuture3DaySeckillSession(@Param("uploadSessionFutureDay") int uploadSessionFutureDay);
}
