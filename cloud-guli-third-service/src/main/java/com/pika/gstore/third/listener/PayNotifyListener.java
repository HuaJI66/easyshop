package com.pika.gstore.third.listener;

import com.pika.gstore.common.enums.PayType;
import com.pika.gstore.third.service.pay.PayFactory;
import com.pika.gstore.third.service.pay.PayService;
import com.pika.gstore.third.vo.PayAsyncVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/27 22:03
 */
@RestController
@Slf4j
public class PayNotifyListener {
    /**
     * 支付宝Web支付成功异步回调地址,需要应用可公网访问
     * 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，
     * 校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
     */
    @RequestMapping("/paid/notify")
    public String paidNotify(HttpServletRequest request, PayAsyncVo response) {
        log.info("收到支付宝支付成功通知:{}"+response.getOut_trade_no());
        PayService payService = PayFactory.getPayService(PayType.ALI_WEB.ordinal());
        return (String) payService.afterPaidNotify(request, response);
    }
}
