package com.pika.gstore.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/4 13:27
 */
@Data
public class SpuBoundTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
