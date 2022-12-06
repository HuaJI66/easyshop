package com.pika.gstore.ware.controller;

import java.util.*;

import com.pika.gstore.ware.vo.MergeVo;
import com.pika.gstore.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.pika.gstore.ware.entity.PurchaseEntity;
import com.pika.gstore.ware.service.PurchaseService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.R;

import javax.validation.Valid;


/**
 * 采购信息
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-22 08:44:12
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * Desc:
     * 领取采购单
     *
     * @param ids
     * @return {@link R}
     */
    @PostMapping("/done")
    @Transactional
    public R received(@RequestBody @Valid PurchaseDoneVo purchaseDoneVo) {
        purchaseService.done(purchaseDoneVo);
        return R.ok();
    }

    @PostMapping("/received")
    @Transactional
    public R received(@RequestBody Long[] ids) {
        if (ids != null && ids.length > 0) {
            purchaseService.received(ids);
        }
        return R.ok();
    }

    /**
     * Desc:
     * 合并采购项
     *
     * @param mergeVo
     * @return {@link R}
     */
    @PostMapping("merge")
    @Transactional
    public R merge(@RequestBody MergeVo mergeVo) {
        purchaseService.merge(mergeVo);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/unreceive/list")
    public R unReceiveList(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryUnReceive(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
