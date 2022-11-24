package com.pika.gstore.gataway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/11/23 17:08
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayMain6000 {
    public static void main(String[] args) {
        SpringApplication.run(GatewayMain6000.class, args);
    }

}
