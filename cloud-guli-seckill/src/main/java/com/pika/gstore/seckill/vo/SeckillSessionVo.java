package com.pika.gstore.seckill.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/2 0:10
 */
@Data
public class SeckillSessionVo {

    /**
     * id
     */
    private Long id;
    /**
     * 场次名称
     */
    private String name;
    /**
     * 每日开始时间
     */
    private Date startTime;
    /**
     * 每日结束时间
     */
    private Date endTime;
    /**
     * 启用状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    private List<SeckillSkuRelationVo> relationEntities;
}
