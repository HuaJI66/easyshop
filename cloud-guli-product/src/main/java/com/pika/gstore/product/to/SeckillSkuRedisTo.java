package com.pika.gstore.product.to;

import com.pika.gstore.common.to.SkuInfoTo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/3 21:16
 */
@Data
public class SeckillSkuRedisTo {
    /**
     * 活动id
     */
    private Long promotionId;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private BigDecimal seckillCount;
    /**
     * 每人限购数量
     */
    private BigDecimal seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;
    private SkuInfoTo skuInfoTo;
    private Long startTime;
    private Long endTime;
    /**
     * 随机码
     */
    private String uuid;
}
