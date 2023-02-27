package com.pika.gstore.order.exception;

import com.pika.gstore.common.exception.BaseException;
import lombok.Getter;
import lombok.Setter;


public class OrderException extends RuntimeException {
    @Getter
    @Setter
    private int code;

    public OrderException() {
        super(BaseException.PRICE_COUNT_EXCEPTION.getMsg());
    }

    public OrderException(String message) {
        super(message);
    }

    public OrderException(int code, String message) {
        super(message);
        this.code = code;
    }
}
