package com.pika.gstore.product.feign;

import com.pika.gstore.common.utils.R;
import com.pika.gstore.product.feign.fallback.SeckillFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "cloud-guli-seckill",fallback = SeckillFeignServiceImpl.class)
public interface SeckillFeignService {
    @GetMapping("/seckill/sku/{skuId}")
    R getSkuSeckillInfo(@PathVariable("skuId") String skuId);
}
