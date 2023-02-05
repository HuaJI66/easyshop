package com.pika.gstore.common.exception;

import lombok.Getter;

/**
 * 10: product
 *
 * @author pi'ka'chu
 */

@Getter
public enum BaseException {
    /**
     * 请求频繁
     */
    TOO_MANY_REQUESTS (10405,"服务器繁忙"),
    /**
     * 数据转换发生错误,请检查数据是否合法
     */
    CONVERT_ERROR(10500, "数据转换发生错误,请检查数据是否合法"),
    /**
     * 数据检验错误
     */
    INVALID_DATA(10400, "数据检验错误"),
    /**
     * 未知异常
     */
    UNKOWN_EXCEPTION(10404, "未知异常"),
    /**
     * 商品上架异常
     */
    PRODUCT_UP_EXCEPTION(10501, "商品上架异常"),
    USER_INFO_INVALID_EXCEPTION(2000403, "用户信息错误"),
    PRICE_COUNT_EXCEPTION(4000501, "价格计算错误"),
    ORDER_EMPTY_EXCEPTION(4000400, "购物项为空"),
    ORDER_DEL_TOKEN_EXCEPTION(4000502, "令牌校验或删除失败"),
    ORDER_COMPUTE_PRICE_EXCEPTION(4000503, "价格校验失败"),
    ORDER_NOT_EXISTS_EXCEPTION(4000504, "订单号不存在"),

    /**
     * 未携带手机号或验证码
     */
    MISS_ERROR(7000405, "未携带手机号或验证码"),
    /**
     * 60s内重复发送
     */
    REPEATED_SEND_ERROR(7000406, "验证码获取频率过高,请稍后再试"),
    /**
     * 其它错误: 验证码/手机/格式有误,不可到达的手机号
     */
    OTHER_ERROR(7000407, "其它错误"),
    USER_EXIST_ERROR(7000408, "其它错误"),
    PHONE_EXIST_ERROR(7000409, "其它错误"),
    LOGIN_INVALID_ERROR(7000410, "用户名或密码错误"),
    SEND_ERROR(7000500, "验证码发送失败"),
    WARE_STOCK_ERROR(8000501, "库存不足"), ;


    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误消息
     */
    private final String msg;

    BaseException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
