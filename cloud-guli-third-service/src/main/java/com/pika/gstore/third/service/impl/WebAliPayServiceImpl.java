package com.pika.gstore.third.service.impl;

import com.alipay.api.AlipayApiException;
import com.pika.gstore.third.config.AlipayTemplate;
import com.pika.gstore.third.vo.PayVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/27 9:09
 */
@Service
public class WebAliPayServiceImpl extends AbstractAliPayServiceAdapter {
    @Resource
    private AlipayTemplate alipayTemplate;

    @Override
    public String doPay(Object obj) {
        try {
            return alipayTemplate.pay((PayVo) obj);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }
}
