package com.pika.gstore.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.constant.SeckillConstant;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;
import com.pika.gstore.coupon.dao.SmsSeckillSessionDao;
import com.pika.gstore.coupon.entity.SmsSeckillSessionEntity;
import com.pika.gstore.coupon.entity.SmsSeckillSkuRelationEntity;
import com.pika.gstore.coupon.service.SmsSeckillSessionService;
import com.pika.gstore.coupon.service.SmsSeckillSkuRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("smsSeckillSessionService")
public class SmsSeckillSessionServiceImpl extends ServiceImpl<SmsSeckillSessionDao, SmsSeckillSessionEntity> implements SmsSeckillSessionService {
    @Resource
    private SmsSeckillSkuRelationService seckillSkuRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SmsSeckillSessionEntity> page = this.page(
                new Query<SmsSeckillSessionEntity>().getPage(params),
                new QueryWrapper<SmsSeckillSessionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SmsSeckillSessionEntity> getFuture3DaySeckillSession() {
        List<SmsSeckillSessionEntity> list = baseMapper.getFuture3DaySeckillSession(SeckillConstant.UPLOAD_SESSION_FUTURE_DAY);
        if (list != null && list.size() > 0) {
            list.forEach(item -> {
                List<SmsSeckillSkuRelationEntity> entities = seckillSkuRelationService.list(new LambdaQueryWrapper<SmsSeckillSkuRelationEntity>().eq(SmsSeckillSkuRelationEntity::getPromotionSessionId, item.getId()));
                item.setRelationEntities(entities);
            });
        }
        return list;
    }

}
