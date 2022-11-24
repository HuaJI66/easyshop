package com.pika.feign;

import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "cloud-guli-yhq")
public interface YhqService {
    @GetMapping("/yhq/test")
    String test();

    @RequestMapping("/coupon/smscoupon/list")
    R list(@RequestParam Map<String, Object> params);
}
