package com.pika.gstore.product.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pika.gstore.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/3 9:56
 */
@Data
public class AttrGroupWithAttrVo {
    private static final long serialVersionUID = 1L;

    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<AttrEntity> attrs;
}
