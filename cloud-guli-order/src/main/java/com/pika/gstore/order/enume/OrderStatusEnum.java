package com.pika.gstore.order.enume;


import lombok.Getter;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/25 18:59
 */
public enum OrderStatusEnum {
    CREATE_NEW(0, "待付款"),
    PAYED(1, "已支付"),
    SENDED(2, "已发货"),
    RECEIVED(3, "已收货"),
    CANCLED(4, "已取消"),
    SERVICING(5, "售后中"),
    SERVICED(6, "售后完成"),
    ;
    @Getter
    private Integer code;
    @Getter
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
