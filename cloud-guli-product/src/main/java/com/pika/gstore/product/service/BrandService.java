package com.pika.gstore.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.product.entity.BrandEntity;
import com.pika.gstore.product.vo.BrandVo;

import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 19:35:09
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateCascade(BrandEntity brand);

    List<BrandVo> infos(List<Long> brandIds);
}

