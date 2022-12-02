package com.pika.gstore.product.vo;

import com.pika.gstore.product.entity.AttrEntity;
import lombok.Data;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/11/30 20:53
 */
@Data
public class AttrRespVo extends AttrEntity {
    /**
     * 所属分类
     */
    private String catelogName;
    /**
     * 所属分组
     */
    private String groupName;

    /**
     * 分组路径
     */
    private Long[] catelogPath;
}
