package com.pika.gstore.product.feign;

import com.pika.gstore.common.to.SkuHasStockVo;
import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author pi'ka'chu
 */
@FeignClient(value = "cloud-guli-ware")
public interface WareFeignService {
    @PostMapping("ware/waresku/hasStock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);
}
