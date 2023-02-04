package com.pika.gstore.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayVo {
    /**
     * 支付宝流水号
     */
    private String trade_no;
    /**
     * 商户订单号 必填
     */
    private String out_trade_no;
    /**
     * 订单名称 必填
     */
    private String subject;
    /**
     * 付款金额 必填
     */
    private String total_amount;
    /**
     * 商品描述 可空
     */
    private String body;
}
