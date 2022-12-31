package com.pika.gstore;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pika.gstore.common.to.SkuHasStockVo;
import com.pika.gstore.ware.dao.WareSkuDao;
import com.pika.gstore.ware.entity.WareSkuEntity;
import com.pika.gstore.ware.service.WareSkuService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
class WareMain8000Tests {
    @Resource
    private WareSkuService wareSkuService;
    @Resource
    private WareSkuDao wareSkuDao;

    @Test
    void contextLoads() {
        wareSkuService.addStock(1L, 1L, 20);
    }

    @Test
    public void test1(){
        Map<String, Object> map = wareSkuService.getMap(new QueryWrapper<WareSkuEntity>()
                .select("SUM(stock-stock_locked) as has_stock")
                .eq("sku_id", 1));
        System.out.println("map = " + map);
    }
    @Test
    public void test2(){
        long skuStock = wareSkuDao.getSkuStock(1L);
        System.out.println("skuStock = " + skuStock);
    }
    @Test
    public void test3(){
        List<SkuHasStockVo> skuHasStock = wareSkuService.getSkuHasStock(Arrays.asList(1L, 2L));
        skuHasStock.forEach(System.out::println);
    }

}
