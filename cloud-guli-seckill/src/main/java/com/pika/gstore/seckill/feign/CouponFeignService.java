package com.pika.gstore.seckill.feign;

import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("cloud-guli-yhq")
public interface CouponFeignService {
    @GetMapping("/coupon/seckillsession/get3lds")
    R getFuture3DaySeckillSession();
}
