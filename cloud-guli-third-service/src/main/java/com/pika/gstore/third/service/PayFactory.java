package com.pika.gstore.third.service;

import com.pika.gstore.third.enums.PayType;
import com.pika.gstore.third.service.impl.WebAliPayServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/27 12:23
 */
@Service
public class PayFactory {
    @Resource
    private WebAliPayServiceImpl webAliPayService;
    private static final HashMap<Integer, PayService> PAY_MAP = new HashMap<>(PayType.values().length);

    @PostConstruct
    public void init() {
        PAY_MAP.put(PayType.ALI_WEB.ordinal(), webAliPayService);
    }

    public static PayService getPayService(Integer payType) {
        return PAY_MAP.get(payType);
    }
}
