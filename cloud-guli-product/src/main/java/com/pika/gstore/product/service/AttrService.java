package com.pika.gstore.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.product.entity.AttrEntity;
import com.pika.gstore.product.vo.AttrGroupVo;
import com.pika.gstore.product.vo.AttrRespVo;
import com.pika.gstore.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 19:35:09
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(AttrVo attr);

    PageUtils queryPage(Map<String, Object> params, Long cateLogId,Integer type);

    AttrRespVo getAttrVo(Long attrId);

    void updateVo(AttrVo attr);

    List<AttrEntity> getAttrRelation(Long attrgroupId);

    void deleteRelation(AttrGroupVo[] attrGroupVos);

    PageUtils getNoRelation(Map<String, Object> params, String attrgroupId);
}

