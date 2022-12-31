package com.pika.gstore.search.service.impl;

import cn.hutool.json.JSONUtil;
import com.pika.gstore.common.to.es.SkuEsModel;
import com.pika.gstore.search.constant.EsConstant;
import com.pika.gstore.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/30 15:49
 */
@Service
@Slf4j
public class ProductSaveServiceImpl implements ProductSaveService {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public boolean saveEs(List<SkuEsModel> skuEsModels) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String jsonStr = JSONUtil.toJsonStr(skuEsModel);
            indexRequest.source(jsonStr,XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        boolean result = response.hasFailures();
        if (result) {
            List<String> collect = Arrays.stream(response.getItems()).filter(BulkItemResponse::isFailed).map(BulkItemResponse::getId).collect(Collectors.toList());
            // TODO: 2022/12/30 批量保存失败处理
            log.error("批量保存失败的ids: {}", collect);
            Arrays.stream(response.getItems()).map(BulkItemResponse::getFailureMessage).forEach(log::error);
        }
        return result;
    }
}
