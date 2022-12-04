package com.pika.gstore.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/4 14:00
 */
@Data
public class SkuReductionTo {
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private BigDecimal priceStatus;
    private List<MemberPrice> memberPrice;
}
