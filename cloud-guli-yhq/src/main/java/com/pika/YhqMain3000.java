package com.pika;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class YhqMain3000 {
    public static void main(String[] args) {
        SpringApplication.run(YhqMain3000.class, args);
    }
}