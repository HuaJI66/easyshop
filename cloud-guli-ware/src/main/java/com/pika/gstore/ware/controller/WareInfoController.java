package com.pika.gstore.ware.controller;

import java.util.Arrays;
import java.util.Map;

import com.pika.gstore.ware.vo.FareVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pika.gstore.ware.entity.WareInfoEntity;
import com.pika.gstore.ware.service.WareInfoService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.R;


/**
 * 仓库信息
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-22 08:44:12
 */
@RestController
@RequestMapping("ware/wareinfo")
public class WareInfoController {
    @Autowired
    private WareInfoService wareInfoService;

    @GetMapping("/fare")
    public FareVo getFare(@RequestParam Long addrId) {
        return wareInfoService.getFare(addrId);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:wareinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = wareInfoService.queryByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:wareinfo:info")
    public R info(@PathVariable("id") Long id) {
        WareInfoEntity wareInfo = wareInfoService.getById(id);

        return R.ok().put("wareInfo", wareInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:wareinfo:save")
    public R save(@RequestBody WareInfoEntity wareInfo) {
        wareInfoService.save(wareInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:wareinfo:update")
    public R update(@RequestBody WareInfoEntity wareInfo) {
        wareInfoService.updateById(wareInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:wareinfo:delete")
    public R delete(@RequestBody Long[] ids) {
        wareInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
