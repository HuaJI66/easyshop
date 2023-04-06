package com.pika.gstore.product.app;

import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.common.validator.AddGroup;
import com.pika.gstore.common.validator.UpdateGroup;
import com.pika.gstore.common.validator.UpdateStatusGroup;
import com.pika.gstore.product.entity.BrandEntity;
import com.pika.gstore.product.service.BrandService;
import com.pika.gstore.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 品牌
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 20:16:15
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params) {

        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId) {
        BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }
    @RequestMapping("/infos")
    public R info(@RequestParam List<Long> brandIds) {
        List<BrandVo> list = brandService.infos(brandIds);
        return R.ok().put("brands", list);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Validated(value = AddGroup.class) @RequestBody BrandEntity brand) {
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@Validated(value = UpdateGroup.class) @RequestBody BrandEntity brand) {
        brandService.updateCascade(brand);
        return R.ok();
    }
    @RequestMapping("/update/status")
    //@RequiresPermissions("product:brand:update")
    public R updateStatus(@Validated(value = UpdateStatusGroup.class) @RequestBody BrandEntity brand) {
        brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds) {
        brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
