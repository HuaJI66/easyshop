package com.pika.gstore.order.vo;

import com.pika.gstore.common.exception.BaseException;
import com.pika.gstore.order.exception.OrderException;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/16 23:20
 */
@Data
public class OrderItemVo {
    private Long skuId;
    private String title;
    private String image;
    private List<String> skuAttr;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;
    private Boolean hasStock=Boolean.FALSE;
    private BigDecimal weight=BigDecimal.ZERO;

    public BigDecimal getTotalPrice() {
        BigDecimal multiply = price.multiply(BigDecimal.valueOf(count));
        if (multiply.compareTo(BigDecimal.ZERO) >= 0) {
            return multiply;
        }
        throw new OrderException(BaseException.PRICE_COUNT_EXCEPTION.getMsg());
    }
}
