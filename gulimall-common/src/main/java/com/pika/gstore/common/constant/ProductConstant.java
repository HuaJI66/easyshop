package com.pika.gstore.common.constant;

import lombok.Getter;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/2 15:58
 */
public class ProductConstant {
    @Getter
    public enum AttrEnum {
        /**
         * 基本属性
         */
        ATTR_TYPE_BASE(1, "base"),
        /**
         * 销售属性
         */
        ATTR_TYPE_SALE(0, "sale"),
        ;
        private final int type;
        private final String desc;

        AttrEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }
    @Getter
    public enum StatusEnum {
        /**
         * 基本属性
         */
        CREATED(0, "新建"),
        UP(1, "上架"),
        /**
         * 销售属性
         */
        DOWN(2, "下架"),
        ;
        private final int type;
        private final String desc;

        StatusEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }
}
