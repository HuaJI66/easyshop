package com.pika.gstore.search.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.json.JSONUtil;
import com.pika.gstore.common.to.es.CategoryVo;
import com.pika.gstore.common.to.es.SkuEsModel;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.search.constant.EsConstant;
import com.pika.gstore.search.feign.ProductFeignService;
import com.pika.gstore.search.service.MallSearchService;
import com.pika.gstore.search.vo.AttrRespVo;
import com.pika.gstore.search.vo.BrandVo;
import com.pika.gstore.search.vo.SearchParams;
import com.pika.gstore.search.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/3 23:28
 */
@Service
@Slf4j
public class MallSearchServiceImpl implements MallSearchService {
    private final String attrAgg = "attr_agg";
    private final String attrSubAttrIdAgg = "attr_sub_attrId_agg";
    private final String attrIdSubAttrNameAgg = "attrId_sub_attrName_agg";
    private final String attrIdSubValueAgg = "attrId_sub_value_agg";
    private final String brandAgg = "brand_agg";
    private final String brandSubNameAgg = "brand_sub_name_agg";
    private final String brandSubImgAgg = "brand_sub_img_agg";
    private final String catalogAgg = "catalog_agg";
    private final String catalogSubNamesAgg = "catalog_sub_names_agg";
    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private ProductFeignService productFeignService;

    @Override
    public SearchResult search(SearchParams searchParams) {
        SearchResult result;
        try {
            //1.构造检索请求
            SearchRequest request = builtSearchRequest(searchParams);
            //2. 处理返回结果
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            result = builtSearchResult(response, searchParams);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("result = " + result);
        return result;
    }

    private SearchResult builtSearchResult(SearchResponse response, SearchParams searchParams) {
        SearchResult result = new SearchResult();

        SearchHits hits = response.getHits();
        if (hits != null) {
            List<SkuEsModel> esModels = Arrays.stream(hits.getHits())
                    .map(item -> {
                        SkuEsModel skuEsModel = JSONUtil.toBean(item.getSourceAsString(), SkuEsModel.class);
                        HighlightField skuTitle = item.getHighlightFields().get("skuTitle");
                        if (skuTitle != null) {
                            skuEsModel.setSkuTitle(skuTitle.getFragments()[0].string());
                        }
                        return skuEsModel;
                    }).collect(Collectors.toList());
            result.setProduct(esModels);
        }

        Aggregations aggregations = response.getAggregations();
        List<SearchResult.BrandVo> brands = new ArrayList<>();
        ParsedLongTerms brandAggregation = aggregations.get(brandAgg);
        for (Terms.Bucket bucket : brandAggregation.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            brandVo.setBrandId(bucket.getKeyAsNumber().longValue());
            brandVo.setBrandName(((ParsedStringTerms) bucket.getAggregations().get(brandSubNameAgg)).getBuckets().get(0).getKeyAsString());
            brandVo.setBrandImg(((ParsedStringTerms) bucket.getAggregations().get(brandSubImgAgg)).getBuckets().get(0).getKeyAsString());
//            System.out.println("brandVo = " + brandVo);
            brands.add(brandVo);
        }
        result.setBrands(brands);

        List<SearchResult.AttrVo> attrs = new ArrayList<>();
        ParsedNested attrAggregation = aggregations.get(attrAgg);
        ParsedLongTerms attrSubAttrIdAggregation = attrAggregation.getAggregations().get(attrSubAttrIdAgg);
        for (Terms.Bucket bucket : attrSubAttrIdAggregation.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            attrVo.setAttrId(bucket.getKeyAsNumber().longValue());
            attrVo.setAttrName(((ParsedStringTerms) bucket.getAggregations().get(attrIdSubAttrNameAgg)).getBuckets().get(0).getKeyAsString());
            attrVo.setAttrValue(((ParsedStringTerms) bucket.getAggregations().get(attrIdSubValueAgg)).getBuckets()
                    .stream().map(MultiBucketsAggregation.Bucket::getKeyAsString).collect(Collectors.toList()));
//            System.out.println("attrVo = " + attrVo);
            attrs.add(attrVo);
        }
        result.setAttrs(attrs);

        List<SearchResult.CatalogVo> catalogs = new ArrayList<>();
        ParsedLongTerms catalogAggregation = aggregations.get(catalogAgg);
        for (Terms.Bucket bucket : catalogAggregation.getBuckets()) {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            catalogVo.setCatalogId(bucket.getKeyAsNumber().longValue());
            catalogVo.setCatalogName(((ParsedStringTerms) bucket.getAggregations().get(catalogSubNamesAgg)).getBuckets().get(0).getKeyAsString());
//            System.out.println("catalogVo = " + catalogVo);
            catalogs.add(catalogVo);
        }
        result.setCatalogs(catalogs);

        Integer pageNum = searchParams.getPageNum();
        result.setPageNum(pageNum);
        long total = 0;
        if (hits != null) {
            total = hits.getTotalHits().value;
        }
        result.setTotal(total);
        int totalPages = Math.toIntExact((total + EsConstant.PAGE_SIZE - 1) / EsConstant.PAGE_SIZE);
        result.setTotalPages(totalPages);
        int[] rainbow = PageUtil.rainbow(pageNum, totalPages, EsConstant.DISPLAY_COUNT);
        result.setPageNavs(rainbow);

        //设置面包屑导航
        List<SearchResult.NavVo> navs = result.getNavs();
        //属性导航
        List<String> attrsStr = searchParams.getAttrs();
        if (!StringUtils.isEmpty(attrsStr) && attrsStr.size() > 0) {
            List<Long> attrIds = result.getAttrIds();
            attrsStr.forEach(attr -> {
                String[] split = attr.split("_");
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                R r = productFeignService.attrInfo(Long.valueOf(split[0]));
                if (r.getCode() == 0) {
                    AttrRespVo attrRespVo = r.getData("attr", new TypeReference<AttrRespVo>() {
                    });
                    navVo.setNavName(attrRespVo.getAttrName());
                    navVo.setNavValue(split[1]);
                    String queryString = searchParams.get_queryString();
                    queryString = removeQueryParams(queryString, "attrs", attr);
                    navVo.setLink(queryString);
                    attrIds.add(Long.valueOf(split[0]));
                }
                navs.add(navVo);
            });
        }
        //品牌导航
        List<Long> brandIds = searchParams.getBrandId();
        if (brandIds != null && brandIds.size() > 0) {
            R r = productFeignService.info(brandIds);
            if (r != null && r.getCode() == 0) {
                List<BrandVo> brandVos = r.getData("brands", new TypeReference<List<BrandVo>>() {
                });
                if (brandVos != null && brandVos.size() > 0) {
                    SearchResult.NavVo navVo = new SearchResult.NavVo();
                    navVo.setNavName("品牌");
                    StringBuilder builder = new StringBuilder();
                    String queryString = searchParams.get_queryString();
                    for (BrandVo brandVo : brandVos) {
                        Long brandId = brandVo.getBrandId();
                        String brandName = brandVo.getBrandName();
                        navVo.setNavValue(brandName);
                        builder.append(removeQueryParams(queryString, "brandId", brandId + ""));
                    }
                    navVo.setLink(builder.toString());
                    navs.add(navVo);
                }
            }
        }
        // TODO: 2023/1/6 分类导航
        Long catalog3Id = searchParams.getCatalog3Id();
        if (catalog3Id != null) {
            SearchResult.NavVo navVo = new SearchResult.NavVo();
            navVo.setNavName("分类");
            R r = productFeignService.getCategoryById(catalog3Id);
            if (r.getCode()==0) {
                CategoryVo data = r.getData(new TypeReference<CategoryVo>() {
                });
                navVo.setNavValue(data.getName());
                navVo.setLink(removeQueryParams(searchParams.get_queryString(), "catalog3Id", catalog3Id + ""));
                navs.add(navVo);
            }

        }
        result.setNavs(navs);
        return result;
    }

    /**
     * Desc:删除queryString中键为replaceKey,值为replaceValue的url参数
     */
    private String removeQueryParams(String queryString, String removeKey, String removeValue) {
        String encodeQuery = URLEncodeUtil.encodeQuery(removeValue, StandardCharsets.UTF_8);
        queryString = queryString.replace("?" + removeKey + "=" + encodeQuery, "");
        queryString = queryString.replace("&" + removeKey + "=" + encodeQuery, "");
        queryString = queryString.replace(removeKey + "=" + encodeQuery, "");
        String joinStr = "";
        if (!queryString.startsWith("?")) {
            joinStr = "?";
        }
        return joinStr + queryString;
    }

    private SearchRequest builtSearchRequest(SearchParams searchParams) {
        //1. 准备检索请求
        SearchRequest request = new SearchRequest(EsConstant.PRODUCT_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //开启布尔查询操作
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //1.1 全文匹配
        String keyword = searchParams.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword + " " + PinyinUtil.getPinyin(keyword, "");
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", keyword));
        }
        //1.2 过滤:catalogId,hasStock,brandId,skuPrice,attrs
        //catalogId
        Long catalog3Id = searchParams.getCatalog3Id();
        if (catalog3Id != null) {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", catalog3Id));
        }
        //hasStock
        Integer hasStock = searchParams.getHasStock();
        if (hasStock != null) {
            boolQuery.filter(QueryBuilders.termQuery("hasStock", hasStock == 1));
        }
        //brandId
        List<Long> brandId = searchParams.getBrandId();
        if (brandId != null && brandId.size() > 0) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", brandId));
        }
        //skuPrice
        String skuPrice = searchParams.getSkuPrice();
        if (!(StringUtils.isEmpty(skuPrice) || "_".equals(skuPrice))) {
            String[] split = skuPrice.split("_");
            if (split.length == 2) {
                if (StringUtils.isEmpty(split[0])) {
                    boolQuery.filter(QueryBuilders.rangeQuery("skuPrice").gte(split[0]).lte(split[1]));
                } else {
                    boolQuery.filter(QueryBuilders.rangeQuery("skuPrice").gte(0).lte(split[1]));
                }
            } else if (skuPrice.endsWith("_")) {
                boolQuery.filter(QueryBuilders.rangeQuery("skuPrice").gte(split[0]));
            }
        }
        //attrs
        List<String> attrs = searchParams.getAttrs();
        if (attrs != null && attrs.size() > 0) {
            //每次循环都因再次生成nested对象
            for (String attr : attrs) {
                attr = attr.trim();
                BoolQueryBuilder query = QueryBuilders.boolQuery();
                String[] split = attr.split("_");
                if (split.length == 2) {
                    query.must(QueryBuilders.termQuery("attrs.attrId", split[0]));
                    query.must(QueryBuilders.termsQuery("attrs.attrValue", split[1].split(":")));
                    boolQuery.filter(QueryBuilders.nestedQuery("attrs", query, ScoreMode.None));
                }
            }
        }
        //设置query
        sourceBuilder.query(boolQuery);
        //1.3 聚合
        //添加多个子聚合
        sourceBuilder.aggregation(AggregationBuilders.terms(brandAgg).field("brandId").size(10)
                .subAggregation(AggregationBuilders.terms(brandSubNameAgg).field("brandName").size(1))
                .subAggregation(AggregationBuilders.terms(brandSubImgAgg).field("brandImg").size(1))
        );

        sourceBuilder.aggregation(AggregationBuilders.terms(catalogAgg).field("catalogId").size(10)
                .subAggregation(AggregationBuilders.terms(catalogSubNamesAgg).field("catalogName").size(1))
        );

        sourceBuilder.aggregation(AggregationBuilders.nested(attrAgg, "attrs")
                .subAggregation(AggregationBuilders.terms(attrSubAttrIdAgg).field("attrs.attrId").size(10)
                        .subAggregation(AggregationBuilders.terms(attrIdSubAttrNameAgg).field("attrs.attrName").size(1))
                        .subAggregation(AggregationBuilders.terms(attrIdSubValueAgg).field("attrs.attrValue").size(50))
                )
        );

        //1.4 排序
        String sort = searchParams.getSort();
        if (!StringUtils.isEmpty(sort)) {
            String[] split = sort.split("_");
            if (split.length == 2 && !StringUtils.isEmpty(split[0])) {
                sourceBuilder.sort(split[0], "asc".equalsIgnoreCase(split[1]) ? SortOrder.ASC : SortOrder.DESC);
            }
        }

        //1.5 分页
        sourceBuilder.from((searchParams.getPageNum() - 1) * EsConstant.PAGE_SIZE).size(EsConstant.PAGE_SIZE);

        //1.6 高亮
        if (!StringUtils.isEmpty(keyword)) {
            sourceBuilder.highlighter(SearchSourceBuilder.highlight()
                    .field("skuTitle")
                    .preTags("<b style='color:red'>")
                    .postTags("</b>")
            );
        }
        //设置source
        request.source(sourceBuilder);
        System.out.println("sourceBuilder = " + sourceBuilder);
        return request;
    }
}
