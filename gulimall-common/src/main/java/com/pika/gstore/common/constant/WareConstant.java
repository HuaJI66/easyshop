package com.pika.gstore.common.constant;

import lombok.Getter;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/5 0:14
 */
public class WareConstant {
    @Getter
    public enum PurchaseEnum {
        /**
         * 基本属性
         */
        CREATED(0, "新建"),
        ASSIGNED(1, "已分配"),
        GOT(2, "已领取"),
        FINISHED(3, "已完成"),
        ERROR(4, "有异常"),
        ;
        private final int code;
        private final String desc;

        PurchaseEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    @Getter
    public enum PurchaseDetailEnum {
        /**
         * 基本属性
         */
        CREATED(0, "新建"),
        ASSIGNED(1, "已分配"),
        BUYING(2, "正在采购"),
        FINISHED(3, "已完成"),
        ERROR(4, "采购失败"),
        ;
        private final int code;
        private final String desc;

        PurchaseDetailEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }
}
