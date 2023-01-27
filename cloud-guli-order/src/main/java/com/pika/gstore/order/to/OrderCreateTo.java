package com.pika.gstore.order.to;

import com.pika.gstore.order.entity.OrderEntity;
import com.pika.gstore.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/25 18:54
 */
@Data
public class OrderCreateTo {
    private OrderEntity order;
    private List<OrderItemEntity> items;
    private BigDecimal payPrice;
    private BigDecimal fare;
}
