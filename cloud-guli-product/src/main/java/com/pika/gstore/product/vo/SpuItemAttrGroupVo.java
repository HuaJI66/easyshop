package com.pika.gstore.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/8 18:21
 */
@Data
@ToString
public class SpuItemAttrGroupVo {

    private String groupName;

    private List<Attr> attrs;

}
