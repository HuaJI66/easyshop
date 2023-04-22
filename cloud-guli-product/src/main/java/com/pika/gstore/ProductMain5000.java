package com.pika.gstore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan(basePackages ={ "com.pika.gstore.product.dao"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.pika.gstore.product.feign")
public class ProductMain5000 {
    // TODO: 2023/3/27 使用 bloom 过滤器剔除非法请求
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ProductMain5000.class, args);
    }

}
