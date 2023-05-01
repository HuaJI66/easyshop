package com.pika.gstore.third.pay.service.impl;

import cn.hutool.json.JSONUtil;
import com.pika.gstore.common.constant.MqConstant;
import com.pika.gstore.common.to.PaymentInfoTo;
import com.pika.gstore.third.pay.service.AbstractPayAdapter;
import com.pika.utils.UnionPayTemplate;
import com.unionpay.acp.sdk.SDKConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/3/1 12:44
 */
@Service
@Slf4j
public class WebUnionPayImpl extends AbstractPayAdapter {
    @Resource
    private UnionPayTemplate unionPayTemplate;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public Object doPay(Object... objects) {
        return unionPayTemplate.doPay((Map<String, String>) objects[0]);
    }

    @Override
    public Boolean afterPaidFrontNotify(Object... objects) {
        HttpServletRequest req = (HttpServletRequest) objects[0];
        log.info("BackRcvResponse接收后台通知开始");

        String encoding = req.getParameter(SDKConstants.param_encoding);
        // 获取银联通知服务器发送的后台通知参数
        Map<String, String> reqParam = unionPayTemplate.getAllRequestParam(req);
        log.info(JSONUtil.toJsonStr(reqParam));

        //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
        boolean result = unionPayTemplate.validate(reqParam, encoding);
        if (!result) {
            log.info("验证签名结果[失败].");
            //验签失败，需解决验签问题

        } else {
            log.info("验证签名结果[成功].");
            //【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态

            //判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
            //修改订单状态
            PaymentInfoTo paymentInfo = new PaymentInfoTo();
            paymentInfo.setAlipayTradeNo(reqParam.get("traceNo"));
            paymentInfo.setOrderSn(reqParam.get("orderId"));
            paymentInfo.setPaymentStatus(reqParam.get("respMsg"));
            paymentInfo.setCallbackTime(new Date());
            rabbitTemplate.convertAndSend(MqConstant.ORDER_EVENT_EXCHANGE, MqConstant.ORDER_UPDATE_KEY, paymentInfo);
        }
        log.info("BackRcvResponse接收后台通知结束");
        //返回给银联服务器http 200  状态码
        return result;
    }

    @Override
    public String afterPaidBackNotify(Object... objects) {
        return afterPaidFrontNotify(objects) ? "ok" : "error";
    }
}
