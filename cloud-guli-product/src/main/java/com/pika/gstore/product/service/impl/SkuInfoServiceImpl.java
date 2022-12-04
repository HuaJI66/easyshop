package com.pika.gstore.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;

import com.pika.gstore.product.dao.SkuInfoDao;
import com.pika.gstore.product.entity.SkuInfoEntity;
import com.pika.gstore.product.service.SkuInfoService;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    // list?t=1670159328928&page=1&limit=10&key=50&catelogId=225&brandId=3&min=4000&max=6000
    public PageUtils queryByCondition(Map<String, Object> params) {
        LambdaQueryWrapper<SkuInfoEntity> wrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        String catelogId = (String) params.get("catelogId");
        String brandId = (String) params.get("brandId");
        String min = (String) params.get("min");
        String max = (String) params.get("max");

        if (!StringUtils.isEmpty(key)) {
            wrapper.and(w -> w.eq(SkuInfoEntity::getSkuId, key)
                    .or()
                    .like(SkuInfoEntity::getSkuName, key));
        }
        if (!StringUtils.isEmpty(catelogId) && Integer.parseInt(catelogId) > 0) {
            wrapper.eq(SkuInfoEntity::getCatalogId, catelogId);
        }
        if (!StringUtils.isEmpty(brandId) && Integer.parseInt(brandId) > 0) {
            wrapper.eq(SkuInfoEntity::getBrandId, brandId);
        }
        boolean hasMin = !StringUtils.isEmpty(min);
        boolean hasMax = !StringUtils.isEmpty(max);
        if (hasMin) {
            if (hasMax) {
                BigDecimal minB = new BigDecimal(min);
                BigDecimal maxB = new BigDecimal(max);
                if (!(minB.compareTo(BigDecimal.ZERO) == 0 && maxB.compareTo(BigDecimal.ZERO) == 0)) {
                    wrapper.between(SkuInfoEntity::getPrice, min, max);
                }
            } else {
                wrapper.ge(SkuInfoEntity::getPrice, min);
            }
        } else if (hasMax) {
            wrapper.le(SkuInfoEntity::getPrice, max);
        }

        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

}
