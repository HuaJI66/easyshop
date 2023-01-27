package com.pika.gstore.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/26 10:33
 */
@Data
public class WareSkuLockVo {
    private String orderSn;
    private List<OrderItemVo> locks;
}
