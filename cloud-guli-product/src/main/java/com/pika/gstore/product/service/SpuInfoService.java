package com.pika.gstore.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.product.entity.SpuInfoEntity;
import com.pika.gstore.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 19:35:09
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo saveVo);

    PageUtils queryCondition(Map<String, Object> params);

    void up(Long spuId);
}

