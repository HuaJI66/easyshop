package com.pika.gstore.product.feign;

import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("cloud-guli-seckill")
public interface SeckillFeignService {
    @GetMapping("/seckill/sku/{skuId}")
    R getSkuSeckillInfo(@PathVariable("skuId") String skuId);
}
