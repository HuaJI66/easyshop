package com.pika.gstore.cart.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/14 12:19
 */
public class CartVo {
    @Getter
    @Setter
    private List<CartItemVo> cartItems = new ArrayList<>();
    /**
     * 商品数量
     */
    private Integer countNum = 0;
    /**
     * 商品类型数量
     */
    private Integer typeNum = 0;
    /**
     * 商品总价
     */
    private BigDecimal totalAmount = BigDecimal.ZERO;
    /**
     * 促销减免
     */
    private BigDecimal reduce = BigDecimal.ZERO;


    public Integer getCountNum() {
        for (CartItemVo cartItem : cartItems) {
            countNum += cartItem.getCount();
        }
        return countNum;
    }

    public Integer getTypeNum() {
        return cartItems.size();
    }

    public BigDecimal getTotalAmount() {
        if (cartItems != null && cartItems.size() > 0) {
            cartItems.stream().filter(CartItemVo::getChecked).forEach(i -> totalAmount = totalAmount.add(i.getTotalPrice()));
        }
        totalAmount = totalAmount.subtract(getReduce());
        //防止负数
        return totalAmount.compareTo(BigDecimal.ZERO) >= 0 ? totalAmount : BigDecimal.ZERO;
    }

    public BigDecimal getReduce() {
        return reduce;
    }
}
