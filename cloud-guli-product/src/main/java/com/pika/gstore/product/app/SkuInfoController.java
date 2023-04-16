package com.pika.gstore.product.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.product.entity.SkuInfoEntity;
import com.pika.gstore.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * sku信息
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 20:16:14
 */
@RestController
@RequestMapping("product/skuinfo")
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("{skuId}/price")
    public BigDecimal getSkuPrice(@PathVariable("skuId") Long skuId) {
        return skuInfoService.getOne(new LambdaQueryWrapper<SkuInfoEntity>()
                .eq(SkuInfoEntity::getSkuId, skuId).last("limit 1")).getPrice();
    }

    @GetMapping("getSaleAttrs/{skuId}")
    public List<String> getSaleAttrs(@PathVariable("skuId") Long skuId) {
        return skuInfoService.getSaleAttrs(skuId);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = skuInfoService.queryByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId) {
        SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return skuInfo == null ? R.error("未找到该skuInfo:" + skuId) : R.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:skuinfo:save")
    public R save(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:skuinfo:update")
    public R update(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:skuinfo:delete")
    public R delete(@RequestBody Long[] skuIds) {
        skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

}
