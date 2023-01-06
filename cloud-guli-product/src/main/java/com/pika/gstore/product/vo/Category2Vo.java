package com.pika.gstore.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/31 22:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category2Vo {
    private String id;
    private String name;
    private String catalog1Id;
    private List<Category3Vo> catalog3List;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Category3Vo {
        private String id;
        private String name;
        private String catalog2Id;
    }
}
