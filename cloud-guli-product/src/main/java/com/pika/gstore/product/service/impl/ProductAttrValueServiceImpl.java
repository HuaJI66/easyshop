package com.pika.gstore.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;

import com.pika.gstore.product.dao.ProductAttrValueDao;
import com.pika.gstore.product.entity.ProductAttrValueEntity;
import com.pika.gstore.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ProductAttrValueEntity> listForSpu(String spuId) {
        LambdaQueryWrapper<ProductAttrValueEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductAttrValueEntity::getSpuId, spuId);
        return list(wrapper);
    }

    @Override
    public void updateBySpuId(String spuId, List<ProductAttrValueEntity> attrValueEntities) {
        LambdaQueryWrapper<ProductAttrValueEntity> wrapper = new LambdaQueryWrapper<>();

        List<ProductAttrValueEntity> collect = attrValueEntities.stream().peek(item -> {
            wrapper.or(w ->
                    w.eq(ProductAttrValueEntity::getSpuId, spuId)
                            .eq(ProductAttrValueEntity::getAttrId, item.getAttrId())
            );
            item.setSpuId(Long.valueOf(spuId));
        }).collect(Collectors.toList());
        remove(wrapper);
        saveBatch(collect);
    }

}
