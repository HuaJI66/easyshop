package com.pika.gstore.third.pay.listener;

import com.pika.gstore.common.constant.DomainConstant;
import com.pika.gstore.common.enums.PayType;
import com.pika.gstore.common.to.pay.PayAsyncVo;
import com.pika.gstore.third.pay.service.PayService;
import com.pika.gstore.third.pay.service.SimplePayFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/27 22:03
 */
@Slf4j
@Controller
public class PayNotifyListener {
    /**
     * 支付宝Web支付成功异步回调地址,需要应用可公网访问
     * 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，
     * 校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
     */
    @RequestMapping("/paid/notify/backRcvResponse/alipay")
    @ResponseBody
    public String paidNotify(HttpServletRequest request, PayAsyncVo response) {
        log.info("收到支付宝支付成功通知:{}", response.getOut_trade_no());
        PayService payService = SimplePayFactory.getPayService(PayType.ALI_WEB.ordinal());
        return (String) payService.afterPaidBackNotify(request, response);
    }

    /**
     * Desc: 银联支付后台通知
     */
    @RequestMapping(value = {"/paid/notify/backRcvResponse/unionpay"})
    @ResponseBody
    public String backRcvResponse(HttpServletRequest request) {
        return (String) SimplePayFactory.getPayService(PayType.UNIONPAY_WEB.ordinal()).afterPaidBackNotify(request);
    }

    /**
     * Desc: 银联`支付前台通知(可跳转商户页面)
     */
    @RequestMapping(value = "/paid/notify/frontRcvResponse/unionpay")
    public String frontRcvResponse() {
        return "redirect:" + DomainConstant.MEMBER_DOMAIN + "memberOrder.html";
    }
}
