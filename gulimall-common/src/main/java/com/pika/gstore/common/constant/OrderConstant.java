package com.pika.gstore.common.constant;

import lombok.Data;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/25 12:20
 */
@Data
public class OrderConstant {
    public static final String ORDER_NUMBER_KEY = "order:user:";
    public static final Integer ORDER_NUMBER_KEY_EXPIRE = 30;
}
