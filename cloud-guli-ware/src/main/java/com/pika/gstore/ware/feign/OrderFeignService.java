package com.pika.gstore.ware.feign;

import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/29 22:21
 */
@FeignClient("cloud-guli-order")
public interface OrderFeignService {
    @GetMapping("/order/order/status")
    R getOrderStatus(@RequestParam("orderSn") String orderSn);
}
