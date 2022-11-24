package com.pika.gstore.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.coupon.entity.SmsHomeSubjectSpuEntity;

import java.util.Map;

/**
 * 专题商品
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 21:05:13
 */
public interface SmsHomeSubjectSpuService extends IService<SmsHomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

