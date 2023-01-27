package com.pika.gstore.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/25 12:35
 */
@Data
public class OrderSubmitVo {
    private Long addrId;
    private Integer payType;
    private String orderToken;
    /**
     * 应付价格,验价,防止价格波动
     */
    private BigDecimal payPrice;
    private String note;
}
