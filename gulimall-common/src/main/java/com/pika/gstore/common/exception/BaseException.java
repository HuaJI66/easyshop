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
    /*
    商品上架异常
     */
    PRODUCT_UP_EXCEPTION(10501, "商品上架异常"),
    /**
     * 错误码
     */
    ;
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
