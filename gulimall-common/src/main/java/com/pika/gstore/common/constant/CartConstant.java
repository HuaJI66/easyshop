package com.pika.gstore.common.constant;

import lombok.Data;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/14 14:25
 */
@Data
public class CartConstant {
    public static final String CART_COOKIE_NAME = "user-key";
    public static final String CACHE_CART_PREFIX = "cache:cart:";
    public static final int CART_COOKIE_MAX_AGE = 60 * 60 * 24 * 30;
}
