package com.pika.gstore.third.service.pay;

import com.pika.gstore.common.enums.PayType;
import com.pika.gstore.third.service.pay.impl.WebAliPayImpl;
import com.pika.gstore.third.service.pay.impl.WebUnionPayImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Desc:工厂模式,根据payType提供不同支付方式
 *
 * @author pikachu
 * @since 2023/2/27 12:23
 */
@Service
public class PayFactory {
    @Resource
    private WebAliPayImpl webAliPayService;
    @Resource
    private WebUnionPayImpl webUnionPay;
    private static final HashMap<Integer, PayService> PAY_MAP = new HashMap<>(PayType.values().length);

    @PostConstruct
    public void init() {
        PAY_MAP.put(PayType.ALI_WEB.ordinal(), webAliPayService);
        PAY_MAP.put(PayType.UNIONPAY_WEB.ordinal(), webUnionPay);
    }

    public static PayService getPayService(Integer payType) {
        return PAY_MAP.get(payType);
    }
}
