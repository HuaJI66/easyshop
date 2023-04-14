package com.pika.gstore.seckill.schedule;

import com.pika.gstore.seckill.service.SeckillConstant;
import com.pika.gstore.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/1 23:01
 */
@Component
@Slf4j
public class SeckillSchedule {
    @Resource
    private SeckillService seckillService;
    @Resource
    private RedissonClient redissonClient;

    /**
     * 每天晚上3点,,上架最近3天要秒杀的商品
     */
    @Async
    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadL3DProuct() {
        RLock lock = redissonClient.getLock(SeckillConstant.SECKILL_UPLOAD_LOCK);
        lock.lock(10, TimeUnit.MINUTES);
        try {
            log.info("执行上架任务");
            seckillService.uploadL3DProuct();
        } finally {
            lock.unlock();
        }
    }
}
