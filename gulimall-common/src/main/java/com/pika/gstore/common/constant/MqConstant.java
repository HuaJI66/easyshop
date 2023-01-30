package com.pika.gstore.common.constant;

import lombok.Data;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/30 20:08
 */
@Data
public class MqConstant {

    public static final int STOCK_RELEASE_TTL = 60 * 1000;
    public static final String STOCK_RELEASE_STOCK_QUEUE = "stock.release.stock.queue";
    public static final String STOCK_DELAY_QUEUE = "stock.delay.queue";
    public static final String STOCK_EVENT_EXCHANGE = "stock-event-exchange";
    public static final String STOCK_LOCKED_KEY = "stock.locked";
    public static final String STOCK_RELEASE_KEY = "stock.release";
    public static final String STOCK_RELEASE_X_KEY = "stock.release.#";
    public static final int ORDER_RELEASE_TTL = 30 * 1000;
    public static final String ORDER_DELAY_QUEUE = "order.delay.queue";
    public static final String ORDER_EVENT_EXCHANGE = "order-event-exchange";
    public static final String ORDER_RELEASE_KEY = "order.release.order";
    public static final String ORDER_RELEASE_X_KEY = "order.release.order.#";
    public static final String ORDER_CREATE_KEY = "order.create.order";
    public static final String ORDER_RELEASE_ORDER_QUEUE = "order.release.order.queue";
}
