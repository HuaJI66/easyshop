package com.pika.gstore.ware.vo;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/26 11:10
 */

import lombok.Data;

@Data
public class LockStockVo {
    private Long skuId;
    private Integer num;
    private Boolean locked;
}
