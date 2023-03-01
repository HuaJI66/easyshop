package com.pika.gstore.third.service.pay.impl;

import com.pika.gstore.third.service.pay.PayService;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/3/1 12:43
 */
public class AbstractUnionPayAdapter implements PayService {
    @Override
    public Object beforePay(Object... objects) {
        throw new RuntimeException("Unsupported method");
    }

    @Override
    public Object doPay(Object... objects) {
        throw new RuntimeException("Unsupported method");
    }

    @Override
    public Object afterPaidBackNotify(Object... objects) {
        throw new RuntimeException("Unsupported method");
    }

    @Override
    public Object afterPaidFrontNotify(Object... objects) {
        throw new RuntimeException("Unsupported method");
    }

    @Override
    public Object checkPay(Object... objects) {
        throw new RuntimeException("Unsupported method");
    }

    @Override
    public Object refund(Object... objects) {
        throw new RuntimeException("Unsupported method");
    }
}
