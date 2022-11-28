package com.pika.gstore.third;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/11/27 11:25
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ThirdServiceMain7000 {
    public static void main(String[] args) {
        SpringApplication.run(ThirdServiceMain7000.class, args);
    }
}
