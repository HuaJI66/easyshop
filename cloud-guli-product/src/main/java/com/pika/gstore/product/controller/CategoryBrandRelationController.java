package com.pika.gstore.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.pika.gstore.product.entity.BrandEntity;
import com.pika.gstore.product.entity.CategoryEntity;
import com.pika.gstore.product.service.BrandService;
import com.pika.gstore.product.service.CategoryService;
import com.pika.gstore.product.vo.BrandVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.pika.gstore.product.entity.CategoryBrandRelationEntity;
import com.pika.gstore.product.service.CategoryBrandRelationService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.R;

import javax.annotation.Resource;


/**
 * 品牌分类关联
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 20:16:14
 */
@RestController
@RequestMapping("product/categorybrandrelation")
@Slf4j
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    @Resource
    private BrandService brandService;
    @Resource
    private CategoryService categoryService;

    // http://localhost:6000/admin/product/categorybrandrelation/brands/list?t=1669993563013&catId=225
    @GetMapping("brands/list")
    public R getBrandsList(@RequestParam(value = "catId") Long catId) {
        List<BrandEntity> vos = categoryBrandRelationService.getBrands(catId);
        List<BrandVo> collect = vos.stream().map(o -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(o.getBrandId());
            brandVo.setBrandName(o.getName());
            return brandVo;
        }).collect(Collectors.toList());
        return R.ok().put("data", collect);
    }


    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 请求样例:
     * http://127.0.0.1:6000/admin/product/categorybrandrelation/catelog/list?brandId=3
     */
    @RequestMapping("/catelog/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R CatelogList(@RequestParam(required = false) Long brandId) {
        LambdaQueryWrapper<CategoryBrandRelationEntity> wrapper = new LambdaQueryWrapper<>();
        List<CategoryBrandRelationEntity> list = categoryBrandRelationService
                .list(wrapper.eq(CategoryBrandRelationEntity::getBrandId, brandId));
        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.save(categoryBrandRelation);

        return R.ok();
    }

    @PostMapping("/save/detail")
    @Transactional
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R saveDetail(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        LambdaQueryWrapper<BrandEntity> brandWrapper = new LambdaQueryWrapper<>();
        brandWrapper.select(BrandEntity::getName).eq(BrandEntity::getBrandId, categoryBrandRelation.getBrandId());
        BrandEntity brand = brandService.getOne(brandWrapper);

        LambdaQueryWrapper<CategoryEntity> categoryWrapper = new LambdaQueryWrapper<>();
        categoryWrapper.select(CategoryEntity::getName).eq(CategoryEntity::getCatId, categoryBrandRelation.getCatelogId());
        CategoryEntity category = categoryService.getOne(categoryWrapper);

        categoryBrandRelation.setBrandName(brand.getName());
        categoryBrandRelation.setCatelogName(category.getName());

        categoryBrandRelationService.save(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
