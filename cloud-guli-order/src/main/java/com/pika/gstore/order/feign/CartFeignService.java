package com.pika.gstore.order.feign;

import com.pika.gstore.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author pi'ka'chu
 */
@FeignClient("cloud-guli-cart")
public interface CartFeignService {
    @GetMapping("/{userId}/cart")
    List<OrderItemVo> getCartByUserId(@PathVariable("userId") String userId);

    @GetMapping("/curr/cart")
    List<OrderItemVo> getCurrUserCart();
}
