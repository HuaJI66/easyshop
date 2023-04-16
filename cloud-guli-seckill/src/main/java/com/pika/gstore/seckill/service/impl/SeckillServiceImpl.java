package com.pika.gstore.seckill.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.DateFormatUtils;
import com.pika.gstore.common.constant.MqConstant;
import com.pika.gstore.common.constant.SeckillConstant;
import com.pika.gstore.common.to.MemberInfoTo;
import com.pika.gstore.common.to.SkuInfoTo;
import com.pika.gstore.common.to.es.SeckillOrderTo;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.seckill.feign.CouponFeignService;
import com.pika.gstore.seckill.feign.ProductFeignService;
import com.pika.gstore.seckill.interceptor.LoginInterceptor;
import com.pika.gstore.seckill.service.SeckillService;
import com.pika.gstore.seckill.to.SeckillSkuRedisTo;
import com.pika.gstore.seckill.vo.SeckillSessionVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/1 23:15
 */
@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {
    @Resource
    private CouponFeignService couponFeignService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ProductFeignService productFeignService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    // TODO: 2023/2/2 幂等性接口
    public void uploadFuture3DaySeckillSession() {
        R r = couponFeignService.getFuture3DaySeckillSession();
        if (r.getCode() == 0) {
            List<SeckillSessionVo> sessionVos = r.getData(new TypeReference<List<SeckillSessionVo>>() {
            });
            //1. 缓存活动信息
            saveSessionInfos(sessionVos);
            //2. 缓存活动关联信息
            saveSessionSkuInfos(sessionVos);
            // TODO: 2023/2/4 锁定/解锁库存
        }
    }

    @Override
    public List<SeckillSkuRedisTo> getCurrSeckillSkus() {
        ArrayList<SeckillSkuRedisTo> result = new ArrayList<>();
        long now = System.currentTimeMillis();
//        String patten = DateFormatUtils.format(new Date(), SeckillConstant.SECKILL_DATEFORMAT);
//        Set<String> keys = stringRedisTemplate.keys(SeckillConstant.SESSION_CACHE_PREFIX + patten + "*");
        Set<String> keys = stringRedisTemplate.keys(SeckillConstant.SESSION_CACHE_PREFIX + "*");
        log.warn("keys:{}", keys);
        if (keys != null && !keys.isEmpty()) {
            keys.forEach(key -> {
                //seckill:sessions:2023-02-03:1675354200000-1677513600000
                String[] split = key.substring(key.lastIndexOf(':') + 1).split("-");
                long start = Long.parseLong(split[0]);
                long end = Long.parseLong(split[1]);
                log.warn("now:{},start:{},end:{}", now, start, end);
                if (now >= start && now <= end) {
                    String sessionId = stringRedisTemplate.opsForValue().get(key);
                    List<Object> redisToStr = stringRedisTemplate.opsForHash().values(SeckillConstant.SESSION_SKUS_CACHE_PREFIX + sessionId);
                    for (Object item : redisToStr) {
                        SeckillSkuRedisTo seckillSkuRedisTo = JSONUtil.toBean((String) item, new TypeReference<SeckillSkuRedisTo>() {
                        }, false);
                        result.add(seckillSkuRedisTo);
                    }
                }
            });
        }
        return result;
    }

    /**
     * 获取当前sku的秒杀信息(秒杀预告,今天至未来7天内)
     */
    @Override
    public List<SeckillSkuRedisTo> getSkuSeckillInfo(String skuId) {
        //seckill:sessions:2023-02-03:1675354200000-1677513600000
        List<SeckillSkuRedisTo> collect = new ArrayList<>();
        LocalDate date = LocalDate.now();
        ArrayList<String> keys = new ArrayList<>(8);
        keys.add(SeckillConstant.SESSION_CACHE_PREFIX + date);
        ArrayList<String> sessionIds = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            keys.add(SeckillConstant.SESSION_CACHE_PREFIX + date.plusDays(i));
        }
        //1. 获取当前时间以后的会场
        for (String item : keys) {
            Set<String> set = stringRedisTemplate.keys(item + "*");
            if (set != null && set.size() > 0) {
                List<String> multiGet = stringRedisTemplate.opsForValue().multiGet(set);
                sessionIds.addAll(multiGet);
            }
        }
        //2. 若会场含有该秒杀商品则取出秒杀信息
        sessionIds.forEach(item -> {
            String o = (String) stringRedisTemplate.opsForHash().get(SeckillConstant.SESSION_SKUS_CACHE_PREFIX + item, skuId);
            if (!StringUtils.isEmpty(o)) {
                SeckillSkuRedisTo redisTo = JSONUtil.toBean(o, new TypeReference<SeckillSkuRedisTo>() {
                }, true);
                //处理商品随机码,只有在秒杀时间内返回
                long now = System.currentTimeMillis();
                if (redisTo.getStartTime() > now || redisTo.getEndTime() < now) {
                    redisTo.setUuid(null);
                }
                collect.add(redisTo);
            }
        });
        return collect;
    }


    private void saveSessionInfos(List<SeckillSessionVo> sessionVos) {
        sessionVos.forEach(item -> {
            Date startTime = item.getStartTime();
            long start = startTime.getTime();
            long end = item.getEndTime().getTime();
            String key = SeckillConstant.SESSION_CACHE_PREFIX + DateFormatUtils.format(startTime, SeckillConstant.SECKILL_DATEFORMAT) + ":" + start + "-" + end;
            stringRedisTemplate.opsForValue().setIfAbsent(key, item.getId().toString(), Duration.ofDays(SeckillConstant.SECKILL_SESSION_EXPIRE));
        });
    }

    private void saveSessionSkuInfos(List<SeckillSessionVo> sessionVos) {
        sessionVos.stream().filter(item -> item.getEndTime().getTime() > System.currentTimeMillis()).forEach(item -> {
            BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(SeckillConstant.SESSION_SKUS_CACHE_PREFIX + item.getId());
            ops.expire(SeckillConstant.SECKILL_SKU_EXPIRE, TimeUnit.DAYS);
            item.getRelationEntities().forEach(vo -> {
                String uuid = IdUtil.fastSimpleUUID();
                //当前场次无该商品
                if (!Boolean.TRUE.equals(ops.hasKey(vo.getSkuId().toString()))) {
                    SeckillSkuRedisTo redisTo = new SeckillSkuRedisTo();
                    //1. sku基本数据
                    R r = productFeignService.info(vo.getSkuId());
                    if (r.getCode() == 0) {
                        SkuInfoTo skuInfo = r.getData("skuInfo", new TypeReference<SkuInfoTo>() {
                        });
                        redisTo.setSkuInfoTo(skuInfo);
                    }
                    //2. sku秒杀信息
                    BeanUtils.copyProperties(vo, redisTo);

                    //3. 秒杀时间信息
                    redisTo.setStartTime(item.getStartTime().getTime());
                    redisTo.setEndTime(item.getEndTime().getTime());

                    //4. 随机码
                    redisTo.setUuid(uuid);
                    RSemaphore semaphore = redissonClient.getSemaphore(SeckillConstant.SECKILL_SEMAPHORE_PREFIX + uuid);
                    semaphore.trySetPermits(vo.getSeckillCount().intValue());
                    ops.put(vo.getSkuId().toString(), JSON.toJSONString(redisTo));
                }
            });

        });
    }

    @Override
    public String doSeckill(String skuId, String sessionId, String code, Integer num) {
        //获取当前用户
        MemberInfoTo memberInfoTo = LoginInterceptor.threadLocal.get();
        //1. 校验合法性
        BoundHashOperations<String, String, String> ops = stringRedisTemplate.boundHashOps(SeckillConstant.SESSION_SKUS_CACHE_PREFIX + sessionId);
        if (Boolean.TRUE.equals(ops.hasKey(skuId))) {
            String seckillSkuInfo = ops.get(skuId);
            SeckillSkuRedisTo redisTo = JSONUtil.toBean(seckillSkuInfo, new TypeReference<SeckillSkuRedisTo>() {
            }, true);
            //校验随机码,秒杀时间,限购数量
            long now = System.currentTimeMillis();
            Long startTime = redisTo.getStartTime();
            Long endTime = redisTo.getEndTime();
            if (redisTo.getUuid().equals(code) && now >= startTime && now <= endTime && num <= redisTo.getSeckillLimit().intValue()) {
                //检查已购买数量,当购买成功时要占位
                //2. 获取当前用户限购信号量
                RSemaphore semaphore = redissonClient.getSemaphore(SeckillConstant.SECKILL_SUCCESS + sessionId + ":" + memberInfoTo.getId() + ":" + skuId);
                semaphore.trySetPermits(redisTo.getSeckillLimit().intValue());
                semaphore.expire(Duration.ofMillis(endTime - startTime));
                boolean tryAcquire = false;
                try {
                    tryAcquire = semaphore.tryAcquire(num, SeckillConstant.SECKILL_WAIT_TIME, TimeUnit.MILLISECONDS);
                    if (tryAcquire) {
                        //3. 获取当前商品限购信号量
                        boolean acquire = redissonClient.getSemaphore(SeckillConstant.SECKILL_SEMAPHORE_PREFIX + code)
                                .tryAcquire(num, SeckillConstant.SECKILL_WAIT_TIME, TimeUnit.MILLISECONDS);
                        if (acquire) {
                            //4. 秒杀成功,生成订单,发送mq
                            String orderSn = IdUtil.fastSimpleUUID();
                            SeckillOrderTo seckillOrderTo = new SeckillOrderTo(orderSn, memberInfoTo.getId(),
                                    redisTo.getPromotionSessionId(), Long.parseLong(skuId), redisTo.getSeckillPrice(), num);
                            rabbitTemplate.convertAndSend(MqConstant.ORDER_EVENT_EXCHANGE, MqConstant.ORDER_SECKILL_KEY, seckillOrderTo);
                            return orderSn;
                        }
                    }
                } catch (InterruptedException ignored) {

                }

            }
        }
        return null;
    }
}
