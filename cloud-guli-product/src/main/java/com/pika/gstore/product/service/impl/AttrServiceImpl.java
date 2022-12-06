package com.pika.gstore.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.injector.methods.DeleteBatchByIds;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.constant.ProductConstant;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;
import com.pika.gstore.product.dao.AttrDao;
import com.pika.gstore.product.entity.*;
import com.pika.gstore.product.service.*;
import com.pika.gstore.product.vo.AttrGroupVo;
import com.pika.gstore.product.vo.AttrRespVo;
import com.pika.gstore.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author pi'ka'chu
 */
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Resource
    private AttrAttrgroupRelationService relationService;
    @Resource
    private AttrGroupService attrGroupService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private ProductAttrValueService productAttrValueService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void save(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        baseMapper.insert(attrEntity);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getType() && attr.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
            relation.setAttrId(attrEntity.getAttrId());
            relation.setAttrGroupId(attr.getAttrGroupId());

            relationService.save(relation);
        }
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long cateLogId, Integer type) {
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttrEntity::getAttrType, type);
        if (cateLogId != 0) {
            wrapper.eq(AttrEntity::getCatelogId, cateLogId);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(o -> o.like(AttrEntity::getAttrName, key)
                    .or()
                    .like(AttrEntity::getShowDesc, key)
                    .or()
                    .like(AttrEntity::getAttrId, key)
            )
            ;
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        List<AttrEntity> records = page.getRecords();
        ArrayList<AttrRespVo> respVos = new ArrayList<>(records.size());
        records.forEach(attrEntity -> {
            AttrRespVo respVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, respVo);
            LambdaQueryWrapper<CategoryEntity> wrapper3 = new LambdaQueryWrapper<>();
            wrapper3.select(CategoryEntity::getName)
                    .eq(CategoryEntity::getCatId, respVo.getCatelogId());
            CategoryEntity category = categoryService.getOne(wrapper3);
            Optional<String> categoryName = Optional.ofNullable(category.getName());
            categoryName.ifPresent(respVo::setCatelogName);

            if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getType()) {
                // 查询关系表
                LambdaQueryWrapper<AttrAttrgroupRelationEntity> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.select(AttrAttrgroupRelationEntity::getAttrGroupId)
                        .eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId());
                AttrAttrgroupRelationEntity relation = relationService.getOne(wrapper1);

                Optional<AttrAttrgroupRelationEntity> attrGroupId = Optional.ofNullable(relation);
                attrGroupId.ifPresent(relationEntity -> {
                    LambdaQueryWrapper<AttrGroupEntity> wrapper2 = new LambdaQueryWrapper<>();
                    wrapper2.select(AttrGroupEntity::getAttrGroupName)
                            .eq(AttrGroupEntity::getAttrGroupId, relationEntity.getAttrGroupId());
                    AttrGroupEntity group = attrGroupService.getOne(wrapper2);
                    respVo.setGroupName(group.getAttrGroupName());
                });
            }

            respVos.add(respVo);
        });
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrVo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attr = getById(attrId);
        BeanUtils.copyProperties(attr, attrRespVo);

        // 分组信息
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getType()) {
            LambdaQueryWrapper<AttrAttrgroupRelationEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(AttrAttrgroupRelationEntity::getAttrGroupId)
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attrRespVo.getAttrId());
            AttrAttrgroupRelationEntity relationEntity = relationService.getOne(wrapper);
            Optional<AttrAttrgroupRelationEntity> optional = Optional.ofNullable(relationEntity);
            optional.ifPresent(o -> {
                LambdaQueryWrapper<AttrGroupEntity> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.select(AttrGroupEntity::getAttrGroupName)
                        .eq(AttrGroupEntity::getAttrGroupId, o.getAttrGroupId());
                AttrGroupEntity attrGroup = attrGroupService.getOne(wrapper1);
                Optional<AttrGroupEntity> optional1 = Optional.ofNullable(attrGroup);
                optional1.ifPresent(p -> attrRespVo.setGroupName(p.getAttrGroupName()));
            });
        }


        // 分类信息
        AttrGroupEntity attrGroup = new AttrGroupEntity();
        attrGroup.setCatelogId(attrRespVo.getCatelogId());
        attrRespVo.setCatelogPath(categoryService.findCatelogIdPath(attrGroup));
        CategoryEntity category = categoryService.getById(attrRespVo.getCatelogId());
        Optional<CategoryEntity> optional2 = Optional.ofNullable(category);
        optional2.ifPresent(o -> attrRespVo.setCatelogName(o.getName()));
        return attrRespVo;
    }

    @Override
    public void updateVo(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        updateById(attrEntity);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getType()) {
            // 修改分组关联关系
            LambdaUpdateWrapper<AttrAttrgroupRelationEntity> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(AttrAttrgroupRelationEntity::getAttrGroupId, attr.getAttrGroupId())
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attr.getAttrId());
            int count = relationService.count(new LambdaUpdateWrapper<AttrAttrgroupRelationEntity>()
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attr.getAttrId()));
            if (count > 0) {
                relationService.update(wrapper);
            } else {
                AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
                if (attr.getAttrGroupId() != null && attr.getAttrId() != null) {
                    relation.setAttrId(attr.getAttrId());
                    relation.setAttrGroupId(attr.getAttrGroupId());
                    relationService.save(relation);
                }
            }
        }

    }

    @Override
    public List<AttrEntity> getAttrRelation(Long attrgroupId) {
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrgroupId);
        List<AttrAttrgroupRelationEntity> relationEntityList = relationService.list(wrapper);

        return relationEntityList.stream().map(relationEntity -> getById(relationEntity.getAttrId())).collect(Collectors.toList());
    }

    @Override
    public void deleteRelation(AttrGroupVo[] attrGroupVos) {
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> wrapper = new LambdaQueryWrapper<>();
        for (AttrGroupVo attrGroupVo : attrGroupVos) {
            wrapper.or(w -> w.eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupVo.getAttrGroupId())
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attrGroupVo.getAttrId()));
        }
        relationService.remove(wrapper);
    }

    /**
     * Desc: 获取属性分组没有关联的其他属性
     */
    @Override
    public PageUtils getNoRelation(Map<String, Object> params, String attrgroupId) {
        // 1.只能关联自身所属分类的属性
        AttrGroupEntity attrGroup = attrGroupService.getById(attrgroupId);
        // 2.只能关联未被其它分组关联的属性
        // 2.1 当前分类下的其它分组
        LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> wrapper1 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<AttrEntity> wrapper2 = new LambdaQueryWrapper<>();
        // 只查询当前分类的基本属性
        wrapper2.eq(AttrEntity::getAttrType, ProductConstant.AttrEnum.ATTR_TYPE_BASE.getType())
                .eq(AttrEntity::getCatelogId, attrGroup.getCatelogId());

        wrapper.eq(AttrGroupEntity::getCatelogId, attrGroup.getCatelogId());

        List<Long> groupIds = attrGroupService.list(wrapper).stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
        // 2.2  这些分组关联的属性
        if (groupIds.size() > 0) {
            wrapper1.in(true, AttrAttrgroupRelationEntity::getAttrGroupId, groupIds);
            List<Long> relatedIds = relationService.list(wrapper1).stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());

            if (relatedIds.size() > 0) {
                // 2.3  从当前分类的所有属性中移除被关联的属性
                wrapper2.notIn(true, AttrEntity::getAttrId, relatedIds);
            }
        }

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper2.and(w -> {
                w.eq(AttrEntity::getAttrId, key)
                        .or()
                        .like(AttrEntity::getAttrName, key);
            });
        }
        IPage<AttrEntity> page = page(new Query<AttrEntity>().getPage(params), wrapper2);
        return new PageUtils(page);
    }
}
