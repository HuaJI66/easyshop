package com.pika.gstore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com.pika.gstore.product.dao")
@EnableDiscoveryClient
public class ProductMain5000 {

    public static void main(String[] args) {
        SpringApplication.run(ProductMain5000.class, args);
    }

}
