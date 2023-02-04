package com.pika.gstore.seckill.to;

import com.pika.gstore.common.to.SkuInfoTo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/2 8:39
 */
@Data
public class SeckillSkuRedisTo implements Comparable<SeckillSkuRedisTo> {
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

    @Override
    public int compareTo(SeckillSkuRedisTo o) {
        long compare = this.startTime - o.getStartTime();
        if (compare > 0) {
            return 1;
        } else if (compare == 0) {
            return 0;
        } else {
            return -1;
        }
    }
}
