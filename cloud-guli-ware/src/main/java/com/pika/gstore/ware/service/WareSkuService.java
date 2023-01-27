package com.pika.gstore.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.to.SkuHasStockVo;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.ware.entity.WareSkuEntity;
import com.pika.gstore.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-22 08:44:12
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    boolean lockStock(WareSkuLockVo wareSkuLockVo);
}

