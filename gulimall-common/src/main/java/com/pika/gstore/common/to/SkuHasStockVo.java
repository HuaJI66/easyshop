package com.pika.gstore.common.to;

import lombok.Data;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/30 13:30
 */
@Data
public class SkuHasStockVo {
    private Long skuId;
    private Boolean hasStock;
}
