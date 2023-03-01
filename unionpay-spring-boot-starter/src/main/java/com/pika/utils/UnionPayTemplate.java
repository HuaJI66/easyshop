package com.pika.utils;

import cn.hutool.json.JSONUtil;
import com.pika.properties.SDKConfigProperties;
import com.pika.vo.UnionPayVo;
import com.unionpay.acp.sdk.AcpService;
import com.unionpay.acp.sdk.DemoBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 重要：联调测试时请仔细阅读注释！
 * <p>
 * 产品：跳转网关支付产品<br>
 * 功能：后台通知接收处理示例 <br>
 * 日期： 2015-09<br>
 * <p>
 * 版权： 中国银联<br>
 * 声明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障<br>
 * 该接口参考文档位置：open.unionpay.com帮助中心 下载  产品接口规范  《网关支付产品接口规范》，<br>
 * 《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表），
 * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
 * 调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
 * 测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
 * 2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
 * 交易说明：	前台类交易成功才会发送后台通知。后台类交易（有后台通知的接口）交易结束之后成功失败都会发通知。
 * 为保证安全，涉及资金类的交易，收到通知后请再发起查询接口确认交易成功。不涉及资金的交易可以以通知接口respCode=00判断成功。
 * 未收到通知时，查询接口调用时间点请参照此FAQ：https://open.unionpay.com/ajweb/help/faq/list?id=77&level=0&from=0
 *
 * @author pikachu
 * @since 2023/2/28 21:03
 */
@Slf4j
public class UnionPayTemplate {
    @Resource
    private SDKConfigProperties properties;

    /**
     * @param payVo {@link UnionPayVo}
     * @return {@link String} form表单,可直接跳转支付页面
     */
    public String doPay(LinkedHashMap<String, String> payVo) {
        Map<String, String> requestData = new HashMap<String, String>(payVo);
        //	1、获取参数
        /***商户接入参数***/
//        requestData.put("orderId", payVo.getOrderId());
//        requestData.put("txnTime", payVo.getTxnTime());
//        requestData.put("currencyCode", payVo.getCurrencyCode());
//        requestData.put("txnAmt", payVo.getTxnAmt());
//        requestData.put("reqReserved", payVo.getReqReserved());
//        requestData.put("riskRateInfo", payVo.getRiskRateInfo());
//        requestData.put("payTimeout", payVo.getPayTimeout());
//        requestData.put("txnType", payVo.getTxnType());
//        requestData.put("txnSubType", payVo.getTxnSubType());
//        requestData.put("bizType", payVo.getBizType());
//        requestData.put("channelType", payVo.getChannelType());

        // 2、封装报文
        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        requestData.put("merId", properties.getMerId());
        requestData.put("accessType", properties.getAccessType());
        requestData.put("version", properties.getVersion());
        requestData.put("encoding", properties.getEncoding());
        requestData.put("signMethod", properties.getSignMethod());


        //前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
        //如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
        //异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        requestData.put("frontUrl", properties.getFrontUrl());

        //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
        //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码
        //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
        //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
        requestData.put("backUrl", properties.getBackUrl());

        //
        //
        //       报文中特殊用法请查看 special_use_purchase.txt
        //
        //

        /**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
        //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        Map<String, String> submitFromData = AcpService.sign(requestData, properties.getEncoding());
        //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
        String requestFrontUrl = properties.getFrontTransUrl();
        //生成自动跳转的Html表单
        String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData, properties.getEncoding());
        //将生成的html写到浏览器中完成自动跳转打开银联支付页面；这里调用signData之后，将html写到浏览器跳转到银联页面之前均不能对html中的表单项的名称和值进行修改，如果修改会导致验签不通过
        log.info("html:{}", html);
        log.info("requestData:{}", JSONUtil.toJsonStr(requestData));
        return html;
    }

    /**
     * 获取请求参数中所有的信息
     * 当商户上送frontUrl或backUrl地址中带有参数信息的时候，
     * 这种方式会将url地址中的参数读到map中，会导多出来这些信息从而致验签失败，这个时候可以自行修改过滤掉url中的参数或者使用getAllRequestParamStream方法。
     */
    public Map<String, String> getAllRequestParam(
            final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                // 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                if (res.get(en) == null || "".equals(res.get(en))) {
                    // System.out.println("======为空的字段名===="+en);
                    res.remove(en);
                }
            }
        }
        return res;
    }

    /**
     * 获取请求参数中所有的信息。
     * 非struts可以改用此方法获取，好处是可以过滤掉request.getParameter方法过滤不掉的url中的参数。
     * struts可能对某些content-type会提前读取参数导致从inputstream读不到信息，所以可能用不了这个方法。理论应该可以调整struts配置使不影响，但请自己去研究。
     * 调用本方法之前不能调用req.getParameter("key");这种方法，否则会导致request取不到输入流。
     *
     * @param request
     * @return
     */
    public Map<String, String> getAllRequestParamStream(
            final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        try {
            String notifyStr = new String(IOUtils.toByteArray(request.getInputStream()), DemoBase.encoding);
            log.info("收到通知报文：" + notifyStr);
            String[] kvs = notifyStr.split("&");
            for (String kv : kvs) {
                String[] tmp = kv.split("=");
                if (tmp.length >= 2) {
                    String key = tmp[0];
                    String value = URLDecoder.decode(tmp[1], DemoBase.encoding);
                    res.put(key, value);
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.info("getAllRequestParamStream.UnsupportedEncodingException error: " + e.getClass() + ":" + e.getMessage());
        } catch (IOException e) {
            log.info("getAllRequestParamStream.IOException error: " + e.getClass() + ":" + e.getMessage());
        }
        return res;
    }

    /**
     * 验证签名(SHA-1摘要算法)<br>
     *
     * @param data     返回报文数据<br>
     * @param encoding 上送请求报文域encoding字段的值<br>
     * @return true 通过 false 未通过<br>
     */
    public boolean validate(Map<String, String> data, String encoding) {
        return AcpService.validate(data, encoding);
    }
}
