package com.pika.gstore.product.feign;

import com.pika.gstore.common.to.SkuReductionTo;
import com.pika.gstore.common.to.SpuBoundTo;
import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author pi'ka'chu
 */
@FeignClient(value = "cloud-guli-yhq")
public interface YhqFeignService {
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo reduction);
}
