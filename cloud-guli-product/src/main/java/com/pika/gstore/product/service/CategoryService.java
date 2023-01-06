package com.pika.gstore.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.to.es.CategoryVo;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.product.entity.AttrGroupEntity;
import com.pika.gstore.product.entity.CategoryEntity;
import com.pika.gstore.product.vo.Category2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 19:35:09
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listTree();

    void removeMenus(Long[] catIds);

    Long[] findCatelogIdPath(AttrGroupEntity attrGroup);

    void updateCascade(CategoryEntity category);

    List<CategoryEntity> getFirstLevel();

    Map<String, List<Category2Vo>> getCatalogJson();

    CategoryVo getName(Long catId);
}

