package com.pika.gstore.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ESSearchMain9000 {

    public static void main(String[] args) {
        SpringApplication.run(ESSearchMain9000.class, args);
    }

}
