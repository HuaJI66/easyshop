package com.pika.gstore.order.exception;

import com.pika.gstore.common.exception.BaseException;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/22 13:13
 */
public class OrderException extends RuntimeException {
    public OrderException() {
        super(BaseException.PRICE_COUNT_EXCEPTION.getMsg());
    }

    public OrderException(String message) {
        super(message);
    }
}
