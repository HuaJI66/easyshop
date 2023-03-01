package com.pika.gstore.order.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/3/1 12:52
 */
@Data
@NoArgsConstructor
public class UnionPayVo {
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    /**
     * 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
     */
    private String orderId;
    /**
     * 订单发送时间，取系统时间，格式为yyyyMMddHHmmss，必须取当前时间，否则会报txnTime无效
     */
    private String txnTime;
    /**
     * 交易币种（境内商户一般是156 人民币）
     */
    private String currencyCode = "156";
    /**
     * 交易金额，单位分，不要带小数点(后二位为角分,如 999->9.99)
     */
    private String txnAmt;
    /**
     * 请求方保留域，如需使用请启用即可；
     * 透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,
     * 对本交易的交易状态查询交易、对账文件中均会原样返回，
     * 商户可以按需上传，长度为1-1024个字节。
     * 出现&={}[]符号时可能导致查询接口应答报文解析失败，
     * 建议尽量只传字母数字并使用|分割，
     * 或者可以最外层做一次base64编码(base64编码之后出现的等号不会导致解析失败可以不用管)。
     */
    private String reqReserved;
    /**
     * 商品名称
     */
    private String riskRateInfo;
    /**
     * 订单超时时间。(单位minutes)<br>
     * 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。
     * 此时间建议取支付时的北京时间加15分钟。
     * 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。
     */
    private long payTimeout;
    /**
     * 业务类型，B2C网关支付，手机wap支付
     */
    private String bizType = "000201";
    /**
     * 渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机
     */
    private String channelType = "07";
    /**
     * 交易类型 ，01：消费
     */
    private String txnType = "01";
    /**
     * 交易子类型， 01：自助消费
     */
    private String txnSubType = "01";

    /**
     * 获取订单时间
     */
    public String getTxnTime() {
        if (this.txnTime == null) {
            txnTime = dateFormat.format(LocalDateTime.now());
        }
        return this.txnTime;
    }

    /**
     * Desc: 设置交易商品名称
     *
     * @param productName
     * @return {@link String}
     */
    public String getRiskRateInfo() {
        return "{commodityName=" + riskRateInfo + "}";
    }

    public String getPayTimeout() {
        return dateFormat.format(LocalDateTime.now().plus(payTimeout, ChronoUnit.MINUTES));
    }

    public UnionPayVo(String orderId, String txnTime, String txnAmt, String riskRateInfo, long payTimeout) {
        this.orderId = orderId;
        this.txnTime = txnTime;
        this.txnAmt = txnAmt;
        this.riskRateInfo = riskRateInfo;
        this.payTimeout = payTimeout;
    }
}
