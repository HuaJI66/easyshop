package com.pika.gstore.order.vo;

import com.pika.gstore.order.entity.OrderEntity;
import lombok.Data;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/25 12:54
 */
@Data
public class OrderSubmitRepVo {
    private OrderEntity order;
    /**
     * 错误状态码 0:成功
     */
    private Integer code;
    private String msg;
}
