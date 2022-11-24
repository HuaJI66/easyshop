package com.pika.gstore.member.feign;

import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "gstore-coupon")
@Component
public interface CouponService {
    @RequestMapping("/coupon/smscoupon/list")
    R memberList();
    @RequestMapping("/coupon/smscoupon/test")
    String test();
}
