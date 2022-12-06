package com.pika.gstore;

import com.pika.gstore.ware.service.WareSkuService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class WareMain8000Tests {
    @Resource
    private WareSkuService wareSkuService;

    @Test
    void contextLoads() {
        wareSkuService.addStock(1L, 1L, 20);
    }

}
