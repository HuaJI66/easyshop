package com.pika.gstore.order.feign;

import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author pi'ka'chu
 */
@FeignClient("cloud-guli-third-service")
public interface ThirdFeignService {
    /**
     * 让SpringMVC自动将Object序列化为Json串，接收时封装为LinkListHashMap
     */
    @PostMapping(value = "/payOrder")
    R payOrder(@RequestBody Object payVo, @RequestParam("payType") Integer payType);
}
