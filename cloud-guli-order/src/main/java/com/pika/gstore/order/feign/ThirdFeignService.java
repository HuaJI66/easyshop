package com.pika.gstore.order.feign;

import com.pika.gstore.common.utils.R;
import com.pika.gstore.order.vo.PayVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author pi'ka'chu
 */
@FeignClient("cloud-guli-third-service")
public interface ThirdFeignService {
    @PostMapping(value = "/payOrder")
    R payOrder(@RequestBody PayVo payVo, @RequestParam("payType") Integer payType);
}
