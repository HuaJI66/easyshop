package com.pika.gstore.third.service.impl;

import com.pika.gstore.third.service.PayService;

/**
 * Desc:接口适配器模式 (缺省适配器模式)
 * <p>
 * 适用场景: 当不需要全部实现接口提供的方法时，
 * 可先设计一个抽象类实现接口，并为该接口中每个方法提供一个默认实现（空方法），
 * 那么该抽象类的子类可有选择地覆盖父类的某些方法来实现需求。
 * </p>
 * @author pikachu
 * @since 2023/2/27 8:42
 */
public abstract class AbstractAliPayServiceAdapter implements PayService {
    @Override
    public Object beforePay(Object obj) {
        throw new RuntimeException("Unsupported method");
    }

    @Override
    public Object doPay(Object obj) {
        throw new RuntimeException("Unsupported method");
    }

    @Override
    public Object afterPaidNotify(Object obj) {
        throw new RuntimeException("Unsupported method");
    }

    @Override
    public Object checkPay(Object obj) {
        throw new RuntimeException("Unsupported method");
    }

    @Override
    public Object refund(Object obj) {
        throw new RuntimeException("Unsupported method");
    }
}
