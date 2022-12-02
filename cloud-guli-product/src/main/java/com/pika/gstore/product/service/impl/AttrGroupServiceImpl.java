package com.pika.gstore.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pika.gstore.product.entity.AttrAttrgroupRelationEntity;
import com.pika.gstore.product.service.AttrAttrgroupRelationService;
import com.pika.gstore.product.vo.AttrGroupVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;

import com.pika.gstore.product.dao.AttrGroupDao;
import com.pika.gstore.product.entity.AttrGroupEntity;
import com.pika.gstore.product.service.AttrGroupService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Resource
    private AttrAttrgroupRelationService relationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        IPage<AttrGroupEntity> page;
        String key = (String) params.get("key");
        LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(attrWrapper -> attrWrapper
                    .like(AttrGroupEntity::getAttrGroupName, key)
                    .or()
                    .like(AttrGroupEntity::getAttrGroupId, key));
        }
        if (catelogId == 0) {
            page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
        } else {
            wrapper.eq(AttrGroupEntity::getCatelogId, catelogId);
            page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
        }
        return new PageUtils(page);
    }

    @Override
    public void relate(List<AttrGroupVo> attrGroupVos) {
        List<AttrAttrgroupRelationEntity> list = attrGroupVos.stream().map(attrGroupVo -> {
            AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(attrGroupVo, relation);
            return relation;
        }).collect(Collectors.toList());
        relationService.saveBatch(list);
    }

}
