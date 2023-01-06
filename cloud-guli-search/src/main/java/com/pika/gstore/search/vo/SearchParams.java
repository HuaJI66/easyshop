package com.pika.gstore.search.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/3 23:29
 */
@Data
public class SearchParams {
    /**
     * 页面传递过来的全文匹配关键字
     */
    private String keyword;

    /**
     * 品牌id,可以多选
     */
    private List<Long> brandId;

    /**
     * 三级分类id
     */
    private Long catalog3Id;

    /**
     * 排序条件：sort=price/salecount/hotscore_desc/asc
     */
    private String sort;

    /**
     * 是否显示有货: 0:没有,1:有
     */
    private Integer hasStock;

    /**
     * 价格区间查询 2000_5000,_5000,2000_
     */
    private String skuPrice;

    /**
     * 按照属性进行筛选,格式: attrs=attrId_attrValue1:attrValue2,如 attrs=1_红色:绿色
     */
    private List<String> attrs;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 原生的所有查询条件
     */
    private String _queryString;

}
