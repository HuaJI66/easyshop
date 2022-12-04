package com.pika.gstore.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.pika.gstore.product.entity.AttrEntity;
import com.pika.gstore.product.service.AttrService;
import com.pika.gstore.product.service.CategoryService;
import com.pika.gstore.product.vo.AttrGroupVo;
import com.pika.gstore.product.vo.AttrGroupWithAttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pika.gstore.product.entity.AttrGroupEntity;
import com.pika.gstore.product.service.AttrGroupService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.R;

import javax.annotation.Resource;


/**
 * 属性分组
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 20:16:15
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private AttrService attrService;

    @GetMapping("/{catelogId}/withattr")
    public R getWithAttr(@PathVariable("catelogId") Long catelogId) {
        List<AttrGroupWithAttrVo> groupWithAttrVo= attrGroupService.getWithAttr(catelogId);
        return R.ok().put("data", groupWithAttrVo);
    }

    @PostMapping("/attr/relation")
    public R relate(@RequestBody List<AttrGroupVo> attrGroupVos) {
        attrGroupService.relate(attrGroupVos);
        return R.ok();
    }

    /**
     * Desc: 获取属性分组没有关联的其他属性
     */
    @GetMapping("{attrgroupId}/noattr/relation")
    public R getNoRelation(@RequestParam Map<String, Object> params,
                           @PathVariable("attrgroupId") String attrgroupId) {
        PageUtils pageUtils = attrService.getNoRelation(params, attrgroupId);
        return R.ok().put("page", pageUtils);
    }

    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupVo[] attrGroupVos) {
        attrService.deleteRelation(attrGroupVos);
        return R.ok();
    }

    //    /product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId) {
        List<AttrEntity> attrEntities = attrService.getAttrRelation(attrgroupId);
        return R.ok().put("data", attrEntities);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable Long catelogId) {
        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        // 进一步回显分类路径
        Long[] catelogIdPath = categoryService.findCatelogIdPath(attrGroup);
        attrGroup.setCatelogPath(catelogIdPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
