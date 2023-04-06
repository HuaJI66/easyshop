package com.pika.gstore.search;

import com.pika.gstore.search.constant.EsConstant;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.IntStream;

/**
 * @author pikachu
 * @since 2023/4/5 17:26
 */
public class ESTest {
    static RestHighLevelClient restHighLevelClient;

    @BeforeAll
    public static void init() {
        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
        restHighLevelClient = new RestHighLevelClient(builder);
    }

    @Test
    public void test0() throws IOException {
        SearchRequest searchRequest = new SearchRequest(EsConstant.PRODUCT_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("brandId", 3));
        searchRequest.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            String json = hit.getSourceAsString();
            System.out.println("json = " + json);
        }

    }

    @Test
    public void test1() throws IOException {
        IntStream.range(7, 15).forEach(item -> {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            UpdateByQueryRequest request = new UpdateByQueryRequest(EsConstant.PRODUCT_INDEX);
            boolQuery.must(QueryBuilders.matchQuery("skuId", item));
            String img = "https://gstore-piks.oss-cn-hangzhou.aliyuncs.com/2023/04/05/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE_20230405_192651.png";
            Script script = new Script(ScriptType.INLINE,
                    "painless",
                    "ctx._source.skuImg = '" + img + "'",
                    Collections.emptyMap());
            request.setScript(script);
            request.setQuery(boolQuery);
            BulkByScrollResponse response = null;
            try {
                response = restHighLevelClient.updateByQuery(request, RequestOptions.DEFAULT);
                System.out.println("response.getUpdated() = " + response.getUpdated());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Test
    public void test2() {
        IntStream.range(9, 14).forEach(System.out::println);
    }
}
