package com.pika.gstore.order.feign;

import com.pika.gstore.common.utils.R;
import com.pika.gstore.order.vo.FareVo;
import com.pika.gstore.order.vo.WareSkuLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("cloud-guli-ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasStock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);
    @GetMapping("/ware/wareinfo/fare")
    FareVo getFare(@RequestParam("addrId") Long addrId);

    @PostMapping("/ware/waresku/lock/order")
    R lockStock(@RequestBody WareSkuLockVo wareSkuLockVo);
}
