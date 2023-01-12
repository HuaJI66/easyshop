package com.pika.gstore.search.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/28 10:01
 */
@Configuration
@Data
public class EsConfig {
    @Value(("${elasticsearch.host}"))
    private String esHost;
    @Value(("${elasticsearch.port}"))
    private Integer esPort;
    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization", "Bearer " + TOKEN);
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory
//                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(esHost, esPort, "http"));
        return new RestHighLevelClient(builder);
    }
}
