package com.pika.gstore.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ESSearchMain9000 {

    public static void main(String[] args) {
        SpringApplication.run(ESSearchMain9000.class, args);
    }

}
