package com.pika.gstore;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.mysql.cj.log.Log;
import com.pika.gstore.product.dao.CategoryDao;
import com.pika.gstore.product.entity.BrandEntity;
import com.pika.gstore.product.entity.CategoryEntity;
import com.pika.gstore.product.service.BrandService;
import com.pika.gstore.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.annotation.Resources;

@SpringBootTest(classes = ProductMain5000.class)
@Slf4j
class ProductMain5000Tests {
    @Resource
    private BrandService brandService;
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private CategoryService categoryService;

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("test");
        brandEntity.setLogo("test");
        brandEntity.setDescript("test");

        brandService.save(brandEntity);
        System.out.println("brandService.getById(1) = " + brandService.getById(1));
    }

    //获取根据get方法获取数据库中对应的列名查询,
    @Test
    public void test1() {
        LambdaQueryWrapper<CategoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryEntity::getParentCid, 0);
        categoryDao.selectList(wrapper).forEach(System.out::println);
    }

    @Test
    public void test2() {
        categoryService.listTree().forEach(System.out::println);
    }
    @Test
    public void test3(){
        LambdaQueryWrapper<CategoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(CategoryEntity::getSort);
        categoryService.list(wrapper).forEach(System.out::println);
    }
}
