package com.pika.gstore.search;


import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest(classes = ESSearchMain9000.class)
public class ESSearchMain9000Tests {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() {
        System.out.println("restHighLevelClient = " + restHighLevelClient);
    }

    @Test
    void test1() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        User user = new User("kls", 20, "男");
        indexRequest.id("2");
        indexRequest.source(JSONUtil.toJsonStr(user), XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println("indexResponse = " + indexResponse);
    }

    @Test
    public void test2() throws IOException {
        SearchRequest searchRequest = new SearchRequest("bank");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"))
                .aggregation(AggregationBuilders.terms("ageAgg").field("age"))
                .aggregation(AggregationBuilders.avg("balanceAvg").field("balance"));
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println("searchSourceBuilder = " + searchSourceBuilder);
        System.out.println("response = " + response);
        for (SearchHit hit : hits.getHits()) {
            String json = hit.getSourceAsString();
            AccountResult.UserAccount userAccount = JSONUtil.toBean(json, AccountResult.UserAccount.class);
            System.out.println("userAccount = " + userAccount);
        }
        Aggregations aggregations = response.getAggregations();
        Terms ageAgg = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAgg.getBuckets()) {
            System.out.println("bucket.getKeyAsString() = " + bucket.getKeyAsString());
            System.out.println("bucket.getDocCount() = " + bucket.getDocCount());
        }
        Avg balanceAvg = aggregations.get("balanceAvg");
        System.out.println("balanceAvg.getValue() = " + balanceAvg.getValue());
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class User {
    private String name;
    private Integer age;
    private String gender;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class AccountResult {
    private String _id;
    private UserAccount _source;
    private Object _score;
    private String _type;
    private String _index;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UserAccount {
        private String email;
        private Integer age;
        private String state;
        private String employer;
        private Integer balance;
        private String lastname;
        private String city;
        private String gender;
        private String address;
        private String firstname;
        private Integer account_number;
    }

}
