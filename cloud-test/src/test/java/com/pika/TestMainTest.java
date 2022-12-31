package com.pika;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.IOException;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/11/23 15:51
 */
@SpringBootTest(classes = TestMain.class)
public class TestMainTest {
    @Test
    public void test1() throws IOException {
        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost("machine111", 9200)).build();

// Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

// And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);

        SearchResponse<User> search = client.search(s -> s
                        .index("products")
                        .query(q -> q
                                .matchAll(v->v)),
                User.class);

        for (Hit<User> hit: search.hits().hits()) {
            System.out.println("hit.source() = " + hit.source());
        }
    }
}
@Data
@NoArgsConstructor
@AllArgsConstructor
class User{
    private String name;
    private Integer age;
    private String gender;
}
