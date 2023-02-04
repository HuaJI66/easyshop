package com.pika.gstore.member.feign;

import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author pi'ka'chu
 */
@FeignClient("cloud-guli-order")
public interface OrderFeignService {
    @PostMapping("/order/order/currUserOrderList")
    R currUserOrderItemList(@RequestBody Map<String, Object> params);
}
