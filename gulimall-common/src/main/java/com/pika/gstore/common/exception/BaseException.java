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
    ;
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
