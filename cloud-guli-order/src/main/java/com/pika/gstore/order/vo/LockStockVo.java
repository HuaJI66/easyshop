package com.pika.gstore.order.vo;

import lombok.Data;

@Data
public class LockStockVo {
    private Long skuId;
    private Integer num;
    private Boolean locked;
}
