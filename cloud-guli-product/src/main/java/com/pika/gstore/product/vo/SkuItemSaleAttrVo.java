package com.pika.gstore.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/8 18:21
 */ //6、秒杀商品的优惠信息
//    private SeckillSkuVo seckillSkuVo;
@Data
@ToString
public class SkuItemSaleAttrVo {

    private Long attrId;

    private String attrName;

    private List<AttrValueWithSkuIdVo> attrValues;
}
