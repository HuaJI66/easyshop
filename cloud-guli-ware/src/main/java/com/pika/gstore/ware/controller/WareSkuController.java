package com.pika.gstore.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.pika.gstore.common.exception.BaseException;
import com.pika.gstore.common.to.SkuHasStockVo;
import com.pika.gstore.common.exception.NoStockException;
import com.pika.gstore.ware.vo.WareSkuLockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pika.gstore.ware.entity.WareSkuEntity;
import com.pika.gstore.ware.service.WareSkuService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.R;


/**
 * 商品库存
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-22 08:44:12
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    @PostMapping("/lock/order")
    public R lockStock(@RequestBody WareSkuLockVo wareSkuLockVo) {
        try {
            boolean res = wareSkuService.lockStock(wareSkuLockVo);
            return R.ok();
        } catch (NoStockException e) {
            return R.error(BaseException.WARE_STOCK_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 查询sku是否有库存
     */
    @PostMapping("hasStock")
    public R getSkuHasStock(@RequestBody List<Long> skuIds) {
        List<SkuHasStockVo> skuHasStockVos = wareSkuService.getSkuHasStock(skuIds);
        return R.ok().setData(skuHasStockVos);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:waresku:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:waresku:info")
    public R info(@PathVariable("id") Long id) {
        WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:waresku:delete")
    public R delete(@RequestBody Long[] ids) {
        wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
