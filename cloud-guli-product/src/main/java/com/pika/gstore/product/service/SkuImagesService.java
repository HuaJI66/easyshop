package com.pika.gstore.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.product.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku图片
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 19:35:09
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

