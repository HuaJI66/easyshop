package com.pika.gstore.cart.service;

import com.pika.gstore.cart.vo.CartItemVo;
import com.pika.gstore.cart.vo.CartVo;

import java.util.List;

/**
 * @author pi'ka'chu
 */
public interface CartService {
    CartItemVo addToCart(Long skuId, Integer num);

    CartItemVo getCartItem(Long skuId);

    CartVo getCart();

    void clearCart(String cartId);

    void changeState(Long skuId, Boolean checkedState);


    void changeNum(Long skuId, Integer num);

    void deleteCartItem(Long skuId);

    List<CartItemVo> getCheckedCartByUserId(String userId);

    List<CartItemVo> getCurrUserCheckedCart();
}
