package com.pika.gstore.cart.feign;

import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author pi'ka'chu
 */
@FeignClient("cloud-guli-product")
public interface ProductFeignService {
    @RequestMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);

    @GetMapping("/product/skuinfo/getSaleAttrs/{skuId}")
     List<String> getSaleAttrs(@PathVariable("skuId") Long skuId);
}
