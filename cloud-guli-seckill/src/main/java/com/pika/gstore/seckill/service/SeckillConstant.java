package com.pika.gstore.seckill.service;

import lombok.Data;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/2 8:18
 */
@Data
public class SeckillConstant {
    /**
     * date格式化
     */
    public static final String SECKILL_DATEFORMAT = "yyyy-MM-dd";
    /**
     * <p>key: 当前日期 + 开始时间-结束时间</p>
     * <p>value: 参与秒杀的场次id</p>
     * <p>ex: 2023-02-02:1675267680000-1677513600000 -> 1</p>
     */
    public static final String SESSION_CACHE_PREFIX = "seckill:sessions:";
    /**
     * key: {sessionId}
     * value: hash{skuId: {skuInfo}}
     */
    public static final String SESSION_SKUS_CACHE_PREFIX = "seckill:skus:";
    /**
     * 信息量
     * key: 商品随机码uuid
     * value: 秒杀数量
     */
    public static final String SECKILL_SEMAPHORE_PREFIX = "seckill:stock:";
    /**
     * 信号量
     * key: sessionId:userId:skuId
     * value: limit(sku每人限购数量)
     */
    public static final String SECKILL_SUCCESS = "seckill:";
    /**
     * 执行定时任务锁
     */
    public static final String SECKILL_UPLOAD_LOCK = "seckill:upload:lock";
    public static final long SECKILL_SESSION_EXPIRE = 5;
    public static final long SECKILL_SKU_EXPIRE = 5;
    public static final long SECKILL_SKU_STOCK_EXPIRE = 5;
    public static final Long SECKILL_WAIT_TIME = 200L;
}
