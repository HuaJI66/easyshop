package com.pika.gstore.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/29 22:41
 */
@Data
public class SkuEsModel {
    private Long skuId;
    private Long spuId;
    private Long brandId;
    private Long catalogId;
    private String skuImg;
    private String catalogName;
    private List<Attrs> attrs;
    private BigDecimal skuPrice;
    private Long saleCount;
    private Long hotScore;
    private String brandName;
    private String skuTitle;
    private String brandImg;
    private Boolean hasStock;

    @Data
    public static class Attrs {
        private String attrName;
        private String attrValue;
        private Long attrId;
    }
}
