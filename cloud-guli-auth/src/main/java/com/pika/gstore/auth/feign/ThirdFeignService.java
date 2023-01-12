package com.pika.gstore.auth.feign;

import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author pi'ka'chu
 */
@FeignClient("cloud-guli-third-service")
public interface ThirdFeignService {
    /**
     * Desc: 异步发送
     * @return {@link R}
     */
    @GetMapping("sms/sendAsync")
    R sendAsync(@RequestParam("phone") String phone, @RequestParam("code") String code);

    /**
     * 立即发送
     */
    @GetMapping("sms/send")
    R send(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
