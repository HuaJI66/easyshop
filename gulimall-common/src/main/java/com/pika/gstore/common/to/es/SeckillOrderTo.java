package com.pika.gstore.common.to.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/4 13:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillOrderTo {
    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 用户
     */
    private Long memberId;
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
    private Integer num;

}
