package com.pika.gstore.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.pika.gstore.common.constant.ProductConstant;
import com.pika.gstore.product.vo.AttrRespVo;
import com.pika.gstore.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pika.gstore.product.entity.AttrEntity;
import com.pika.gstore.product.service.AttrService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.R;


/**
 * 商品属性
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 20:16:14
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping(value = {"/list"})
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping(value = {"/{type}/list/{cateLogId}"})
    public R baseList(@RequestParam Map<String, Object> params,
                      @PathVariable("cateLogId") Long cateLogId,
                      @PathVariable("type") String typeStr) {
        Integer type = ProductConstant.AttrEnum.ATTR_TYPE_BASE.getDesc().equalsIgnoreCase(typeStr) ?
                ProductConstant.AttrEnum.ATTR_TYPE_BASE.getType()
                : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getType();
        PageUtils page = attrService.queryPage(params, cateLogId, type);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId) {
        AttrRespVo attrRespVo = attrService.getAttrVo(attrId);

        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @Transactional
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr) {
        attrService.save(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr) {

        attrService.updateVo(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
