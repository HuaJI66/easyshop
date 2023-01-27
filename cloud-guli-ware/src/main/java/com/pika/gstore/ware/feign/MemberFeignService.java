package com.pika.gstore.ware.feign;

import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author pi'ka'chu
 */
@FeignClient("cloud-guli-member")
public interface MemberFeignService {
    @RequestMapping("/member/memberreceiveaddress/info/{id}")
    R info(@PathVariable("id") Long id);
}
