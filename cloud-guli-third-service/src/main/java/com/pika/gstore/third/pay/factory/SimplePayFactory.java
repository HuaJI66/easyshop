package com.pika.gstore.third.pay.factory;

import com.pika.gstore.common.enums.PayType;
import com.pika.gstore.third.pay.service.PayService;
import com.pika.gstore.third.pay.service.impl.WebAliPayImpl;
import com.pika.gstore.third.pay.service.impl.WebUnionPayImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Desc:简单工厂模式,根据payType提供不同支付方式
 *
 * @author pikachu
 * @since 2023/2/27 12:23
 */
@Service
public class SimplePayFactory {
    @Resource
    private WebAliPayImpl webAliPayService;
    @Resource
    private WebUnionPayImpl webUnionPay;
    private static final PayService[] PAY_SERVICES = new PayService[PayType.values().length];

    /**
     * 属性注入完成后，为PAY_SERVICES赋值
     */
    @PostConstruct
    public void init() {
        PAY_SERVICES[PayType.ALI_WEB.ordinal()] = webAliPayService;
        PAY_SERVICES[PayType.UNIONPAY_WEB.ordinal()] = webUnionPay;
    }

    public static PayService getPayService(Integer payType) {
        return PAY_SERVICES[payType];
    }
}
