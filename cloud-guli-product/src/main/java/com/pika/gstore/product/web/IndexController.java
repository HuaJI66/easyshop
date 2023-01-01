package com.pika.gstore.product.web;

import com.pika.gstore.product.entity.CategoryEntity;
import com.pika.gstore.product.service.CategoryService;
import com.pika.gstore.product.vo.Category2Vo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/31 21:22
 */
@Controller
@Slf4j
public class IndexController {
    @Resource
    private CategoryService categoryService;

    @GetMapping(value = {"index.html", "/"})
    public String index(Model model) {
        //查询所有一级分类
        List<CategoryEntity> categoryEntities = categoryService.getFirstLevel();
        model.addAttribute("first_category", categoryEntities);
        return "index";
    }

    @GetMapping("index/catalog.json")
    @ResponseBody
    public Map<String, List<Category2Vo>> getCatalogJson() {
        return categoryService.getCatalogJson();
    }
}
