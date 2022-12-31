package com.pika.gstore.product.service.impl;

import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.constant.ProductConstant;
import com.pika.gstore.common.to.SkuHasStockVo;
import com.pika.gstore.common.to.SkuReductionTo;
import com.pika.gstore.common.to.SpuBoundTo;
import com.pika.gstore.common.to.es.SkuEsModel;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.product.dao.SpuInfoDao;
import com.pika.gstore.product.entity.*;
import com.pika.gstore.product.feign.EsFeignService;
import com.pika.gstore.product.feign.WareFeignService;
import com.pika.gstore.product.feign.YhqFeignService;
import com.pika.gstore.product.service.*;
import com.pika.gstore.product.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author pi'ka'chu
 */
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Resource
    private SpuInfoDescService infoDescService;
    @Resource
    private SpuImagesService imagesService;
    @Resource
    private ProductAttrValueService productAttrValueService;
    @Resource
    private AttrService attrService;
    @Resource
    private SkuInfoService skuInfoService;
    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    private YhqFeignService yhqFeignService;
    @Resource
    private BrandService brandService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private WareFeignService wareFeignService;
    @Resource
    private EsFeignService esFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    // TODO: 2022/12/4 待完善
    public void saveSpuInfo(SpuSaveVo saveVo) {
        //1. 保存spu基本信息  pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(saveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        save(spuInfoEntity);

        //2. 保存spu的描述图片 pms_spu_info_desc
        List<String> decript = saveVo.getDecript();
        SpuInfoDescEntity spuInfoDesc = new SpuInfoDescEntity();
        spuInfoDesc.setDecript(String.join(",", decript));
        spuInfoDesc.setSpuId(spuInfoEntity.getId());
        if (!StringUtils.isEmpty(spuInfoDesc.getDecript())) {
            infoDescService.save(spuInfoDesc);
        }

        //3. 保存spu的图片集  pms_spu_images
        List<String> images = saveVo.getImages();
        imagesService.saveImages(spuInfoEntity.getId(), images);

        //4. 保存spu的规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = saveVo.getBaseAttrs();
        if (baseAttrs != null && baseAttrs.size() > 0) {
            List<ProductAttrValueEntity> collect = baseAttrs.stream().map(baseAttr -> {
                ProductAttrValueEntity productAttrValue = new ProductAttrValueEntity();
                productAttrValue.setSpuId(spuInfoEntity.getId());
                productAttrValue.setAttrId(baseAttr.getAttrId());
                AttrEntity attr = attrService.getById(baseAttr.getAttrId());
                productAttrValue.setAttrName(attr.getAttrName());
                productAttrValue.setAttrValue(baseAttr.getAttrValues());
                productAttrValue.setQuickShow(baseAttr.getShowDesc());
                return productAttrValue;
            }).collect(Collectors.toList());
            productAttrValueService.saveBatch(collect);
        }


        //5. 保存spu的积分信息 gulimall_sms->sms_spu_bounds
        Bounds bounds = saveVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R r = yhqFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode() != 0) {
            log.error("远程保存spu的积分信息失败");
        }


        //5. 保存当前spu的所有sku信息
        List<Skus> skus = saveVo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach(item -> {
                // 查找默认图片
                String imgUrl = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        imgUrl = image.getImgUrl();
                    }
                }
                //5.1 sku的基本信息  pms_sku_info
                SkuInfoEntity skuInfo = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfo);
                skuInfo.setSpuId(spuInfoEntity.getId());
                skuInfo.setBrandId(spuInfoEntity.getBrandId());
                skuInfo.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfo.setSaleCount(0L);
                skuInfo.setSkuDefaultImg(imgUrl);
                skuInfoService.save(skuInfo);

                Long skuId = skuInfo.getSkuId();

                //5.2 sku的图片信息  pms_sku_images
                // TODO: 2022/12/4 没有图片路径不需要保存
                List<SkuImagesEntity> imagesEntities = item.getImages().stream()
                        .filter(img -> !StringUtils.isEmpty(img.getImgUrl()))
                        .map(img -> {
                            SkuImagesEntity skuImages = new SkuImagesEntity();
                            skuImages.setSkuId(skuId);
                            skuImages.setImgUrl(img.getImgUrl());
                            skuImages.setDefaultImg(img.getDefaultImg());
                            return skuImages;
                        }).collect(Collectors.toList());
                skuImagesService.saveBatch(imagesEntities);


                //5.3 sku的销售属性信息    pms_sku_sale_attr_value
                List<Attr> attrs = item.getAttr();
                if (attrs != null && attrs.size() > 0) {
                    List<SkuSaleAttrValueEntity> saleAttrValueEntities = attrs.stream().map(attr -> {
                        SkuSaleAttrValueEntity saleAttrValueEntity = new SkuSaleAttrValueEntity();
                        BeanUtils.copyProperties(attr, saleAttrValueEntity);
                        saleAttrValueEntity.setSkuId(skuId);
                        return saleAttrValueEntity;
                    }).collect(Collectors.toList());
                    skuSaleAttrValueService.saveBatch(saleAttrValueEntities);
                }

                //5.4 sku的优惠满减等信息   gulimall_sms->sms_sku_ladder,sms_sku_full_reduction,sms_sku_full_reduction
                SkuReductionTo reduction = new SkuReductionTo();
                reduction.setSkuId(skuId);
                BeanUtils.copyProperties(item, reduction);
                if (reduction.getFullCount() > 0 || reduction.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
                    R r1 = yhqFeignService.saveSkuReduction(reduction);
                    if (r1.getCode() != 0) {
                        log.error("远程保存sku的优惠满减等信息失败");
                    }
                }
            });
        }
    }

    @Override
    //product/spuinfo/list?t=1670156646351&status=0&key=50&brandId=3&catelogId=225&page=1&limit=10
    public PageUtils queryCondition(Map<String, Object> params) {
        LambdaQueryWrapper<SpuInfoEntity> wrapper = new LambdaQueryWrapper<>();
        String brandId = (String) params.get("brandId");
        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(w -> w.eq(SpuInfoEntity::getId, key)
                    .or()
                    .like(SpuInfoEntity::getSpuName, key));
        }
        if (!StringUtils.isEmpty(brandId) && Integer.parseInt(brandId) > 0) {
            wrapper.eq(SpuInfoEntity::getBrandId, brandId);
        }
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq(SpuInfoEntity::getPublishStatus, status);
        }
        if (!StringUtils.isEmpty(catelogId) && Integer.parseInt(catelogId) > 0) {
            wrapper.eq(SpuInfoEntity::getCatalogId, catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {
        List<SkuInfoEntity> skus = skuInfoService.list(new LambdaQueryWrapper<SkuInfoEntity>()
                .eq(SkuInfoEntity::getSpuId, spuId));

        // TODO: 2022/12/29 4.查询当前sku所有可检索属性
        List<ProductAttrValueEntity> productAttrValueEntities = productAttrValueService.listForSpu(spuId);
        List<Long> attrIds = productAttrValueEntities.stream().map(ProductAttrValueEntity::getAttrId).collect(Collectors.toList());

        HashSet<Long> attrSet = attrService.list(new LambdaQueryWrapper<AttrEntity>()
                        .select(AttrEntity::getAttrId).in(AttrEntity::getAttrId, attrIds).eq(AttrEntity::getSearchType, 1))
                .stream().map(AttrEntity::getAttrId).collect(Collectors.toCollection(HashSet::new));

        List<SkuEsModel.Attrs> attrsCollect = productAttrValueEntities.stream()
                .filter(productAttrValueEntity -> attrSet.contains(productAttrValueEntity.getAttrId()))
                .map(productAttrValueEntity -> {
                    SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
                    BeanUtils.copyProperties(productAttrValueEntity, attrs);
                    return attrs;
                }).collect(Collectors.toList());

        // TODO: 2022/12/29 1. 远程调用库存系统,查询是否有库存
        Map<Long, Boolean> map = null;
        try {
            R skuHasStock = wareFeignService.getSkuHasStock(skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList()));
            map = skuHasStock.getData(new TypeReference<List<SkuHasStockVo>>() {
            }).stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
        } catch (Exception e) {
            log.error("远程查询库存出现异常: {}", e.getCause());
        }


        Map<Long, Boolean> finalMap = map;
        List<SkuEsModel> collect = skus.stream().map(sku -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, skuEsModel);
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());
            skuEsModel.setSkuPrice(sku.getPrice());

            //设置是否有库存,默认有
            skuEsModel.setHasStock(finalMap == null ? Boolean.TRUE : finalMap.get(sku.getSkuId()));

            // TODO: 2022/12/29 2. 默认热度评分 0
            skuEsModel.setHotScore(0L);

            // TODO: 2022/12/29 查询品牌和分类的名字
            BrandEntity brand = brandService.getById(sku.getBrandId());
            skuEsModel.setBrandName(brand.getName());
            skuEsModel.setBrandImg(brand.getLogo());
            CategoryEntity category = categoryService.getById(sku.getCatalogId());
            skuEsModel.setCatalogName(skuEsModel.getCatalogName());
            //设置检索属性
            skuEsModel.setAttrs(attrsCollect);
            return skuEsModel;
        }).collect(Collectors.toList());

        // TODO: 2022/12/29 5. 将数据保存到es
        R r = esFeignService.saveEs(collect);
        if (r.getCode() == 0) {
            // TODO: 2022/12/30 6. 修改商品商家状态
            update(new LambdaUpdateWrapper<SpuInfoEntity>()
                    .set(SpuInfoEntity::getPublishStatus, ProductConstant.StatusEnum.UP.getType())
                    .set(SpuInfoEntity::getUpdateTime, new Date())
                    .eq(SpuInfoEntity::getId, spuId));
        } else {
            // TODO: 2022/12/30 重复调用问题 ,接口幂等性?重试机制
        }
    }

}
