package com.pika.gstore.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pika.gstore.product.entity.SkuImagesEntity;
import com.pika.gstore.product.entity.SpuInfoDescEntity;
import com.pika.gstore.product.service.*;
import com.pika.gstore.product.vo.SkuItemSaleAttrVo;
import com.pika.gstore.product.vo.SkuItemVo;
import com.pika.gstore.product.vo.SpuItemAttrGroupVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;

import com.pika.gstore.product.dao.SkuInfoDao;
import com.pika.gstore.product.entity.SkuInfoEntity;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private AttrGroupService attrGroupService;
    @Resource
    private SkuSaleAttrValueService attrValueService;
    @Resource
    private ThreadPoolExecutor executor;

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

    @Override
    public SkuItemVo itemInfo(Long skuId) {
        SkuItemVo skuItemVo = new SkuItemVo();
        CompletableFuture<SkuInfoEntity> skuInfoFuture = CompletableFuture.supplyAsync(() ->{
            SkuInfoEntity info = getById(skuId);
            skuItemVo.setInfo(info);
            return info;
        } , executor);

        CompletableFuture<Void> saleAttrFuture = skuInfoFuture.thenAcceptAsync(res -> {
            List<SkuItemSaleAttrVo> saleAttrVos = attrValueService.getSaleAttrBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(saleAttrVos);
        },executor);

        CompletableFuture<Void> spuInfoDescFuture = skuInfoFuture.thenAcceptAsync(res -> {
            SpuInfoDescEntity spuInfoDesc = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setDesc(spuInfoDesc);
        }, executor);

        CompletableFuture<Void> spuItemAttrFuture = skuInfoFuture.thenAcceptAsync(res -> {
            List<SpuItemAttrGroupVo> groupVoList = attrGroupService.getWithSpuIdCatalogId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setGroupAttrs(groupVoList);
        }, executor);

        CompletableFuture<Void> skuImgFuture = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> images = skuImagesService.list(new LambdaQueryWrapper<SkuImagesEntity>()
                    .eq(SkuImagesEntity::getSkuId, skuId));
            skuItemVo.setImages(images);
        }, executor);

        //编排
        try {
            CompletableFuture.allOf(saleAttrFuture, spuItemAttrFuture, spuInfoDescFuture, skuImgFuture).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }

        return skuItemVo;
    }

}
