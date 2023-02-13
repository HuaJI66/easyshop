package com.pika.gstore.product.web;

import com.pika.gstore.product.service.SkuInfoService;
import com.pika.gstore.product.vo.SkuItemVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/8 8:21
 */
@Controller
public class ItemController {
    @Resource
    private SkuInfoService skuInfoService;
    @GetMapping("/{skuId}.html")
    public String itemInfo(Model model, @PathVariable Long skuId) {
        SkuItemVo skuItemVo = skuInfoService.itemInfo(skuId);
        model.addAttribute("item", skuItemVo);
//        System.out.println("skuItemVo = " + skuItemVo);
        return "item";
    }
}
