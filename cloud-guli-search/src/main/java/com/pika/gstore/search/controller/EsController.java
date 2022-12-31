package com.pika.gstore.search.controller;

import com.pika.gstore.common.constant.ProductConstant;
import com.pika.gstore.common.exception.BaseException;
import com.pika.gstore.common.to.es.SkuEsModel;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/12/30 15:43
 */
@RestController
@RequestMapping("search")
@Slf4j
public class EsController {
    @Resource
    ProductSaveService productSaveService;

    @PostMapping("save")
    public R saveEs(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean hasError = false;
        try {
            hasError = productSaveService.saveEs(skuEsModels);
        } catch (IOException e) {
            log.error("商品上架异常:{}", e.getMessage());
        }
        return hasError ? R.error(BaseException.PRODUCT_UP_EXCEPTION.getCode(), BaseException.PRODUCT_UP_EXCEPTION.getMsg()):R.ok() ;
    }
}
