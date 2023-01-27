package com.pika.gstore.order.feign;

import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("cloud-guli-product")
public interface ProductFeignService {
    @GetMapping("product/spuinfo/skuId/{id}")
     R getSpuBySkuId(@PathVariable("id") Long skuId);
}
