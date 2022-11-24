package com.pika.gstore.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.coupon.entity.SmsSpuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 21:05:13
 */
public interface SmsSpuBoundsService extends IService<SmsSpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

