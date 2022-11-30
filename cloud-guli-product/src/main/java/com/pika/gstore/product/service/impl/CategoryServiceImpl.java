package com.pika.gstore.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;
import com.pika.gstore.product.dao.CategoryDao;
import com.pika.gstore.product.entity.AttrGroupEntity;
import com.pika.gstore.product.entity.CategoryBrandRelationEntity;
import com.pika.gstore.product.entity.CategoryEntity;
import com.pika.gstore.product.service.CategoryBrandRelationService;
import com.pika.gstore.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author pi'ka'chu
 */
@Service("categoryService")
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        return entities != null ? entities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity.getCatId(), entities)))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public void removeMenus(Long[] catIds) {
        // TODO: 2022/11/25 删除菜单前需要检查是否被引用
        baseMapper.deleteBatchIds(Arrays.asList(catIds));
    }

    @Override
    public Long[] findCatelogIdPath(AttrGroupEntity attrGroup) {
        Long pCid = attrGroup.getCatelogId();
        CategoryEntity categoryEntity;
        ArrayList<Long> result = new ArrayList<>();
        do {
            result.add(pCid);
            categoryEntity = baseMapper.selectById(pCid);
            pCid = categoryEntity.getParentCid();
        } while (pCid != 0);
        Collections.reverse(result);
        return result.toArray(new Long[0]);
    }

    @Override
    public void updateCascade(CategoryEntity category) {
        // 更新本表数据
        updateById(category);
        // 更新 CategoryBrandRelation 表数据
        LambdaUpdateWrapper<CategoryBrandRelationEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CategoryBrandRelationEntity::getCatelogName, category.getName())
                .eq(CategoryBrandRelationEntity::getCatelogId, category.getCatId());
        categoryBrandRelationService.update(wrapper);

        // TODO: 2022/11/28 更新其它冗余表数据
    }

    private List<CategoryEntity> getChildren(Long parentId, List<CategoryEntity> all) {
        return parentId != null ? all.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid().equals(parentId))
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity.getCatId(), all)))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList()) : new ArrayList<>();
    }
}
