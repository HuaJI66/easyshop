package com.pika.gstore.third.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.pika.gstore.common.constant.MqConstant;
import com.pika.gstore.common.to.PaymentInfoTo;
import com.pika.gstore.common.to.pay.PayAsyncVo;
import com.pika.gstore.third.pay.config.AlipayTemplate;
import com.pika.gstore.third.pay.enums.AlipayStatusEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/27 9:09
 */
@Service
public class WebAliPayImpl extends AbstractPayAdapter {
    @Resource
    private AlipayTemplate alipayTemplate;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public Object afterPaidBackNotify(Object... objects) {
        boolean result;
        try {
            HttpServletRequest request = (HttpServletRequest) objects[0];
            PayAsyncVo response = (PayAsyncVo) objects[1];
            String tradeStatus = response.getTrade_status();
            result = isSignVerified(request) &&
                    (AlipayStatusEnum.TRADE_FINISHED.equals(tradeStatus) ||
                            AlipayStatusEnum.TRADE_SUCCESS.equals(tradeStatus));
            if (result) {
                //修改订单状态
                PaymentInfoTo paymentInfo = new PaymentInfoTo();
                paymentInfo.setAlipayTradeNo(response.getTrade_no());
                paymentInfo.setOrderSn(response.getOut_trade_no());
                paymentInfo.setPaymentStatus(tradeStatus);
                paymentInfo.setCallbackTime(response.getNotify_time());
                rabbitTemplate.convertAndSend(MqConstant.ORDER_EVENT_EXCHANGE, MqConstant.ORDER_UPDATE_KEY, paymentInfo);
            }

        } catch (Exception e) {
            result = false;
        }
        return result ? "success" : "failure";
    }

    @Override
    public String doPay(Object... objects) {
        try {
            return alipayTemplate.pay((Map<String, String>) objects[0]);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Desc: 支付宝签名验证
     *
     * @param request {@link HttpServletRequest}
     * @return {@link boolean}
     */
    private boolean isSignVerified(HttpServletRequest request) throws AlipayApiException {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), "gbk");
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        return AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipayPublicKey(), alipayTemplate.getCharset(), alipayTemplate.getSignType());
    }
}
