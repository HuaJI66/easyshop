package com.pika.gstore.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.product.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 19:35:09
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

