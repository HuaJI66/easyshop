package com.pika.gstore.search.feign;

import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author pi'ka'chu
 */
@FeignClient("cloud-guli-product")
public interface ProductFeignService {
    @GetMapping("product/attr/info/{attrId}")
    R attrInfo(@PathVariable("attrId") Long attrId);

    @GetMapping("product/brand/infos")
    R info(@RequestParam("brandIds") List<Long> brandIds) ;
    @GetMapping("product/category/info/{catId}")
    R getCategoryById(@PathVariable("catId") Long catId) ;
}
