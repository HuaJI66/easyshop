package com.pika.gstore.seckill.service;


import com.pika.gstore.seckill.to.SeckillSkuRedisTo;

import java.util.List;

/**
 * @author pi'ka'chu
 */
public interface SeckillService {
    void uploadFuture3DaySeckillSession();

    List<SeckillSkuRedisTo> getCurrSeckillSkus();

    List<SeckillSkuRedisTo> getSkuSeckillInfo(String skuId);

    String doSeckill(String skuId, String sessionId, String code, Integer num);
}
