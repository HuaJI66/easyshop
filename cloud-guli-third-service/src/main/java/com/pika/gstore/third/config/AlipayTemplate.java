package com.pika.gstore.third.config;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.pika.gstore.third.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author pi'ka'chu
 */
@ConfigurationProperties(prefix = "alipay")
@Configuration
@Data
public class AlipayTemplate {
    /**
     * 在支付宝创建的应用的id
     */
    private String appId;

    /**
     * 商户私钥，您的PKCS8格式RSA2私钥
     */
    private String merchantPrivateKey;
    /**
     * 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
     */
    private String alipayPublicKey;
    /**
     * 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     * 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
     */
    private String notifyUrl;
    /**
     * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     * 同步通知，支付成功，一般跳转到成功页
     */
    private String returnUrl;
    /**
     * 签名方式
     */
    private String signType = "RSA2";
    /**
     * 字符编码格式
     */
    private String charset = "utf-8";
    /**
     * 支付宝网关； https://openapi.alipaydev.com/gateway.do
     */
    private String gatewayUrl;
    /**
     * 支付超时关单
     */
    private String timeoutExpress = "3m";

    /**
     * 支付
     *
     * @param vo
     * @return 会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
     * @see PayVo
     */
    public String pay(LinkedHashMap<String, String> vo) throws AlipayApiException {
        System.out.println("app_id = " + appId);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, merchantPrivateKey, "json", charset, alipayPublicKey, signType);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);

        HashMap<String, String> map = new HashMap<>(vo);
        //商户订单号，商户网站订单系统中唯一订单号，必填
//        String outTradeNo = vo.getOut_trade_no();
        //付款金额，必填
//        String totalAmount = vo.getTotal_amount();
        //订单名称，必填
//        String subject = vo.getSubject() != null ? vo.getSubject() : "PIKACHU 商城订单";
        //商品描述，可空
//        String body = vo.getBody();
//        map.put("out_trade_no", outTradeNo);
//        map.put("total_amount", totalAmount);
//        map.put("subject", subject);
//        map.put("body", body);
        map.put("product_code", "FAST_INSTANT_TRADE_PAY");
        map.put("timeout_express", timeoutExpress);
        alipayRequest.setBizContent(JSON.toJSONString(map));
        return alipayClient.pageExecute(alipayRequest).getBody();
    }

    /**
     * 统一收单交易关闭接口
     */
    public String close(String outTradeNo, String tradeNo) throws AlipayApiException {
        //商户订单号和支付宝交易号不能同时为空。 trade_no、  out_trade_no如果同时存在优先取trade_no
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        AlipayClient client = new DefaultAlipayClient(gatewayUrl, appId, merchantPrivateKey, "json", charset, alipayPublicKey, signType);
        AlipayTradeCloseRequest alipay_request = new AlipayTradeCloseRequest();

        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);
        alipay_request.setBizModel(model);

        AlipayTradeCloseResponse alipay_response = client.execute(alipay_request);
        String body = alipay_response.getBody();
        System.out.println(body);
        return body;
    }
}
