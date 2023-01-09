package com.pika.gstore;

import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pika.gstore.common.to.SkuHasStockVo;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.product.dao.AttrGroupDao;
import com.pika.gstore.product.dao.CategoryDao;
import com.pika.gstore.product.entity.AttrEntity;
import com.pika.gstore.product.entity.BrandEntity;
import com.pika.gstore.product.entity.CategoryEntity;
import com.pika.gstore.product.feign.WareFeignService;
import com.pika.gstore.product.service.AttrService;
import com.pika.gstore.product.service.BrandService;
import com.pika.gstore.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest(classes = ProductMain5000.class)
@Slf4j
class ProductMain5000Tests {
    @Resource
    private BrandService brandService;
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private CategoryService categoryService;
    @Resource
    private AttrService attrService;
    @Resource
    private WareFeignService wareFeignService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private AttrGroupDao attrGroupDao;

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
    public void test3() {
        LambdaQueryWrapper<CategoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(CategoryEntity::getSort);
        categoryService.list(wrapper).forEach(System.out::println);
    }

    @Test
    public void test4() {
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<>();
        Collection<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        wrapper.in(AttrEntity::getAttrId, integers);
        attrService.list(wrapper).forEach(System.out::println);
    }


    @Test
    public void test5() {
        // download file to local
        R skuHasStock = wareFeignService.getSkuHasStock(Arrays.asList(1L, 2L));
        System.out.println("skuHasStock = " + skuHasStock);
        List<SkuHasStockVo> data = skuHasStock.getData(new TypeReference<List<SkuHasStockVo>>() {
        });
        System.out.println("data = " + data);
        for (SkuHasStockVo vo : data) {
            System.out.println("vo = " + vo);
        }
    }
    @Test
    public void test6(){
        stringRedisTemplate.opsForValue().set("hello", "world");
        String hello = stringRedisTemplate.opsForValue().get("hello");
        System.out.println("hello = " + hello);
    }

    @Test
    public void test7() {
        System.out.println("redissonClient = " + redissonClient);
    }
    @Test
    public void test8(){
        attrGroupDao.getWithSpuIdCatalogId(1L, 225L).forEach(System.out::println);
    }
}
