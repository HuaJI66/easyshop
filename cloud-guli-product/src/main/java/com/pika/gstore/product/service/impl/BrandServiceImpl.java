package com.pika.gstore.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pika.gstore.product.entity.CategoryBrandRelationEntity;
import com.pika.gstore.product.service.CategoryBrandRelationService;
import com.pika.gstore.product.vo.BrandVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;

import com.pika.gstore.product.dao.BrandDao;
import com.pika.gstore.product.entity.BrandEntity;
import com.pika.gstore.product.service.BrandService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        LambdaQueryWrapper<BrandEntity> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            wrapper.like(BrandEntity::getBrandId, key).or().like(BrandEntity::getName, key);
        }
        IPage<BrandEntity> page = this.page(new Query<BrandEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    public void updateCascade(BrandEntity brand) {
        updateById(brand);
        // 更新 CategoryBrandRelation 表数据
        LambdaUpdateWrapper<CategoryBrandRelationEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CategoryBrandRelationEntity::getBrandName, brand.getName())
                .eq(CategoryBrandRelationEntity::getBrandId, brand.getBrandId());
        categoryBrandRelationService.update(wrapper);

        // TODO: 2022/11/28 更新其它冗余表数据
    }

    @Override
    @Cacheable(cacheNames = "brand",key = "#root.methodName+':'+#root.args[0]")
    public List<BrandVo> infos(List<Long> brandIds) {
        return list(new LambdaQueryWrapper<BrandEntity>().select(BrandEntity::getBrandId, BrandEntity::getName)
                .in(BrandEntity::getBrandId, brandIds)).stream().map(item -> {
            BrandVo brandVo = new BrandVo();
            BeanUtils.copyProperties(item, brandVo);
            brandVo.setBrandName(item.getName());
            return brandVo;
        }).collect(Collectors.toList());
    }
}
