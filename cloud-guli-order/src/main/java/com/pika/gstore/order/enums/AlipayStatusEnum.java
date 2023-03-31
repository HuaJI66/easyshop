package com.pika.gstore.order.service.impl.enume;

import lombok.Getter;

/**
 * @author pi'ka'chu
 */
@Getter
public class AlipayStatusEnum {
    /**
     * 交易完成
     */
    public static final String TRADE_FINISHED = "TRADE_FINISHED";
    /**
     * 支付成功,触发通知
     */
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    /**
     * 交易创建
     */
    public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
    /**
     * 交易关闭
     */
    public static final String TRADE_CLOSED = "TRADE_CLOSED";

}
