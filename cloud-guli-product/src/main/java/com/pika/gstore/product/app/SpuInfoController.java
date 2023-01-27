package com.pika.gstore.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pika.gstore.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.pika.gstore.product.entity.SpuInfoEntity;
import com.pika.gstore.product.service.SpuInfoService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.R;


/**
 * spu信息
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 20:16:15
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    @GetMapping("/skuId/{id}")
    public R getSpuBySkuId(@PathVariable("id") Long skuId) {
        SpuInfoEntity spuInfo = spuInfoService.getSpuBySkuId(skuId);
        return R.ok().setData(spuInfo);
    }

    @PostMapping("{spuId}/up")
    public R spuUp(@PathVariable("spuId") Long spuId) {
        spuInfoService.up(spuId);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // list?t=1670156646351&status=0&key=50&brandId=3&catelogId=225&page=1&limit=10
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = spuInfoService.queryCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @Transactional
    public R save(@RequestBody SpuSaveVo saveVo) {
        spuInfoService.saveSpuInfo(saveVo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo) {
        spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:spuinfo:delete")
    public R delete(@RequestBody Long[] ids) {
        spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
