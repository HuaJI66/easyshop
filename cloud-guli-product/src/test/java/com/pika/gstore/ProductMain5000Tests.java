package com.pika.gstore;

import com.pika.gstore.product.entity.BrandEntity;
import com.pika.gstore.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = ProductMain5000.class)
class ProductMain5000Tests {
    @Resource
    private BrandService brandService;

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("test");
        brandEntity.setLogo("test");
        brandEntity.setDescript("test");

        brandService.save(brandEntity);
        System.out.println("brandService.getById(1) = " + brandService.getById(1));
    }

}
