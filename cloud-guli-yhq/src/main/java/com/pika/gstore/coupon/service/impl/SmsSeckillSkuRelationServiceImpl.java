package com.pika.gstore.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;

import com.pika.gstore.coupon.dao.SmsSeckillSkuRelationDao;
import com.pika.gstore.coupon.entity.SmsSeckillSkuRelationEntity;
import com.pika.gstore.coupon.service.SmsSeckillSkuRelationService;
import org.springframework.util.StringUtils;


@Service("smsSeckillSkuRelationService")
public class SmsSeckillSkuRelationServiceImpl extends ServiceImpl<SmsSeckillSkuRelationDao, SmsSeckillSkuRelationEntity> implements SmsSeckillSkuRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<SmsSeckillSkuRelationEntity> wrapper = new LambdaQueryWrapper<>();
        String promotionSessionId = (String) params.get("promotionSessionId");
        String key = (String) params.get("key");
        wrapper.eq(!StringUtils.isEmpty(promotionSessionId), SmsSeckillSkuRelationEntity::getPromotionSessionId, promotionSessionId);
        wrapper.eq(!StringUtils.isEmpty(key), SmsSeckillSkuRelationEntity::getId, key);
        IPage<SmsSeckillSkuRelationEntity> page = this.page(
                new Query<SmsSeckillSkuRelationEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}
