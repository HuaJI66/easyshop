package com.pika.gstore.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/9 13:48
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AuthMain10000 {
    public static void main(String[] args) {
        SpringApplication.run(AuthMain10000.class, args);
    }
}
