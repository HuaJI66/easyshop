package com.pika.gstore.product.vo;


import com.pika.gstore.product.entity.SkuImagesEntity;
import com.pika.gstore.product.entity.SkuInfoEntity;
import com.pika.gstore.product.entity.SpuInfoDescEntity;
import com.pika.gstore.product.to.SeckillSkuRedisTo;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class SkuItemVo {

    //1、sku基本信息的获取  pms_sku_info
    private SkuInfoEntity info;

    private boolean hasStock = true;

    //2、sku的图片信息    pms_sku_images
    private List<SkuImagesEntity> images;

    //3、获取spu的销售属性组合
    private List<SkuItemSaleAttrVo> saleAttr;

    //4、获取spu的介绍
    private SpuInfoDescEntity desc;

    //5、获取spu的规格参数信息
    private List<SpuItemAttrGroupVo> groupAttrs;
    private List<SeckillSkuRedisTo> skuRedisTo;

}
