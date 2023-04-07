package com.pika.gstore.order.feign;

import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("cloud-guli-product")
public interface ProductFeignService {
    @GetMapping("/product/spuinfo/skuId/{id}")
    R getSpuBySkuId(@PathVariable("id") Long skuId);

    @RequestMapping("/product/skuinfo/info/{skuId}")
    R getSkuById(@PathVariable("skuId") Long skuId);

    @GetMapping("/product/skuinfo/getSaleAttrs/{skuId}")
    List<String> getSaleAttrs(@PathVariable("skuId") Long skuId);
}
