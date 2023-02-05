package com.pika.gstore.product.feign;

import com.pika.gstore.common.to.es.SkuEsModel;
import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author pi'ka'chu
 */
@FeignClient(value = "cloud-guli-search")
public interface EsFeignService {
    @PostMapping("search/save")
    R saveEs(@RequestBody List<SkuEsModel> skuEsModels);
}
