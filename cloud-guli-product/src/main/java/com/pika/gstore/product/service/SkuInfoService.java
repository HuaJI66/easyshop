package com.pika.gstore.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.product.entity.SkuInfoEntity;
import com.pika.gstore.product.vo.SkuItemVo;

import java.util.Map;

/**
 * sku信息
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 19:35:09
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryByCondition(Map<String, Object> params);


    SkuItemVo itemInfo(Long skuId);
}

