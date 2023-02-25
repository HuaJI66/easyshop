package com.pika.gstore.cart.web;

import com.pika.gstore.cart.service.CartService;
import com.pika.gstore.cart.vo.CartItemVo;
import com.pika.gstore.cart.vo.CartVo;
import com.pika.gstore.common.constant.DomainConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/14 11:01
 */
@Controller
@Slf4j
public class CartController {
    @Resource
    private CartService cartService;

    @GetMapping("/{userId}/cart")
    @ResponseBody
    public List<CartItemVo> getCartByUserId(@PathVariable("userId") String userId) {
        return cartService.getCheckedCartByUserId(userId);
    }

    @ResponseBody
    @GetMapping("/curr/cart")
    public List<CartItemVo> getCurrUserCart() {
        return cartService.getCurrUserCheckedCart();
    }

    @GetMapping("/cart.html")
    public String cart(Model model) {
        CartVo cartVo = cartService.getCart();
        model.addAttribute("cartList", cartVo);
        return "cartList";
    }

    @GetMapping("addToCart")
    public String success(@RequestParam("skuId") Long skuId,
                          @RequestParam("num") Integer num,
                          RedirectAttributes redirectAttributes) {
        CartItemVo cartItemVo = cartService.addToCart(skuId, num);
        redirectAttributes.addAttribute("skuId", skuId);
        return "redirect:" + DomainConstant.CART_DOMAIN + "addToCartSuccess.html";
    }

    @GetMapping("addToCartSuccess.html")
    public String success(@RequestParam("skuId") Long skuId, Model model) {
        CartItemVo cartItemVo = cartService.getCartItem(skuId);
        model.addAttribute("cart_item", cartItemVo);
        return "success";
    }

    @GetMapping("cartItem/changeState")
    public String changeState(@RequestParam("skuId") Long skuId, @RequestParam("checkedState") Boolean checkedState) {
        cartService.changeState(skuId, checkedState);
        return "redirect:" + DomainConstant.CART_DOMAIN + "cart.html";
    }

    @GetMapping("cartItem/changeNum")
    public String changeNum(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num) {
        cartService.changeNum(skuId, num);
        return "redirect:" + DomainConstant.CART_DOMAIN + "cart.html";
    }

    @GetMapping("cartItem/delete")
    public String deleteCartItem(@RequestParam("skuId") Long skuId) {
        cartService.deleteCartItem(skuId);
        return "redirect:" + DomainConstant.CART_DOMAIN + "cart.html";
    }
}
