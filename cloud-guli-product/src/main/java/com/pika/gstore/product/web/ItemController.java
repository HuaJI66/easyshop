package com.pika.gstore.product.web;

import com.pika.gstore.product.service.SkuInfoService;
import com.pika.gstore.product.vo.SkuItemVo;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ItemController {
    @Resource
    private SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String itemInfo(Model model, @PathVariable Long skuId) {
        long start = System.currentTimeMillis();
        SkuItemVo skuItemVo = skuInfoService.itemInfo(skuId);
        model.addAttribute("item", skuItemVo);
        log.info("获取商品信息耗时: {}ms", System.currentTimeMillis() - start);
        return "item";
    }
}
