package com.pika.gstore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.pika.gstore.product.dao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.pika.gstore.product.feign")
public class ProductMain5000 {

    public static void main(String[] args) {
        SpringApplication.run(ProductMain5000.class, args);
    }

}
