package com.pika.gstore.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;
import com.pika.gstore.coupon.dao.SmsSeckillSessionDao;
import com.pika.gstore.coupon.entity.SmsSeckillSessionEntity;
import com.pika.gstore.coupon.entity.SmsSeckillSkuRelationEntity;
import com.pika.gstore.coupon.service.SmsSeckillSessionService;
import com.pika.gstore.coupon.service.SmsSeckillSkuRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    public List<SmsSeckillSessionEntity> get3LDS() {
        LocalDate now = LocalDate.now();
        LocalDateTime startTime = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(now.plusDays(2), LocalTime.MAX);
        String start = startTime.format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
        String end = endTime.format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
        List<SmsSeckillSessionEntity> list = list(new LambdaQueryWrapper<SmsSeckillSessionEntity>().between(SmsSeckillSessionEntity::getStartTime, start, end));
        if (list != null && list.size() > 0) {
            list.forEach(item -> {
                List<SmsSeckillSkuRelationEntity> entities = seckillSkuRelationService.list(new LambdaQueryWrapper<SmsSeckillSkuRelationEntity>().eq(SmsSeckillSkuRelationEntity::getPromotionSessionId, item.getId()));
                item.setRelationEntities(entities);
            });
        }
        return list;
    }

}
