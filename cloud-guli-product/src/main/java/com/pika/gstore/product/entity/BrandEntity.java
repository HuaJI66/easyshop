package com.pika.gstore.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.pika.gstore.common.validator.AddGroup;
import com.pika.gstore.common.validator.ListValues;
import com.pika.gstore.common.validator.UpdateGroup;
import com.pika.gstore.common.validator.UpdateStatusGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 19:35:09
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @TableId
    @NotNull(groups = {UpdateGroup.class, UpdateStatusGroup.class},message = "更新操作需要指明id")
    @Null(groups = AddGroup.class,message = "新增时无需指明id")
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名不能为空!",groups = {UpdateGroup.class,AddGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @URL(message = "logo必须是合法的url地址",groups = {AddGroup.class, UpdateGroup.class})
    @NotBlank(message = "logo不能为空",groups = AddGroup.class)
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    @NotNull(groups = UpdateStatusGroup.class)
    @ListValues(values = {0,1},message = "输入的数据不在范围内",groups = {AddGroup.class, UpdateStatusGroup.class})
    private Integer showStatus;
    /**
     * 排序
     */
    @PositiveOrZero(message = "排序为大于等于0的整数", groups = {UpdateGroup.class, AddGroup.class})
    @NotNull(groups = AddGroup.class,message = "排序不能为空")
    private Integer sort;
    /**
     * 检索首字母
     */
    @NotEmpty(groups = AddGroup.class, message = "检索首字母不能为空")
    @Pattern(regexp = "^[a-zA-z]$", message = "检索首字母为a-z或A-Z",groups = {UpdateGroup.class, AddGroup.class})
    private String firstLetter;

}
