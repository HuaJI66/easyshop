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
public class OrderConfirmVo {
    /**
     * 防止重复令牌
     */
    private String orderToken;
    /**
     * 收货地址
     */
    private List<MemberAddressVo> address;
    /**
     * 购物项
     */
    private List<OrderItemVo> items;
    /**
     * 优惠券信息
     */
    private Integer integration=0;
    /**
     * 订单总额
     */
    private BigDecimal total;
    /**
     * 应付价格
     */
    private BigDecimal payPrice;

    /**
     * 总件数
     */
    private Integer count;

    public Integer getCount() {
        return items.stream().map(OrderItemVo::getCount).reduce(0, Integer::sum);
    }

    public BigDecimal getTotal() {
        if (items != null && items.size() > 0) {
            BigDecimal reduce = items.stream().map(OrderItemVo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            return reduce.compareTo(BigDecimal.ZERO) >= 0 ? reduce : BigDecimal.ZERO;
        }
        throw new OrderException(BaseException.ORDER_EMPTY_EXCEPTION.getMsg());
    }

    public BigDecimal getPayPrice() {
        return getTotal().subtract(BigDecimal.valueOf(integration));
    }
}
