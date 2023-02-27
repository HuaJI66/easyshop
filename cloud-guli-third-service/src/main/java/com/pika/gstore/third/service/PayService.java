package com.pika.gstore.third.service;

/**
 * @author pi'ka'chu
 */
public interface PayService {
    /**
     * Desc:支付前
     *
     * @param obj {@link Object}
     * @return {@link Object}
     */
    Object beforePay(Object obj);

    /**
     * Desc:调用支付
     *
     * @param obj {@link Object}
     * @return {@link Object}   订单支付页面
     */
    Object doPay(Object obj);

    /**
     * Desc:支付成功后回调
     *
     * @param obj {@link Object}
     * @return {@link Object}
     */
    Object afterPaidNotify(Object obj);

    /**
     * Desc:检查支付状态
     *
     * @param obj {@link Object}
     * @return {@link Object}
     */
    Object checkPay(Object obj);

    /**
     * Desc: 退款
     *
     * @param obj {@link Object}
     * @return {@link Object}
     */
    Object refund(Object obj);
}
