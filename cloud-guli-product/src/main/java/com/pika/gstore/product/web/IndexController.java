package com.pika.gstore.product.web;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.product.entity.CategoryEntity;
import com.pika.gstore.product.feign.SeckillFeignService;
import com.pika.gstore.product.service.CategoryService;
import com.pika.gstore.product.to.SeckillSkuRedisTo;
import com.pika.gstore.product.vo.Category2Vo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private SeckillFeignService seckillFeignService;

    @GetMapping(value = {"index.html", "/"})
    public String index(Model model) {
        //查询所有一级分类
        try {
            R r = seckillFeignService.getCurrSeckillSkus();
            if (r.getCode() == 0) {
                List<SeckillSkuRedisTo> skuRedisTos = r.getData(new TypeReference<List<SeckillSkuRedisTo>>() {
                });
                model.addAttribute("seckill_skus", skuRedisTos);
            }
        } catch (Exception e) {
            log.info("获取当前秒杀商品信息失败:" + e.getMessage());
        }
        List<CategoryEntity> categoryEntities = categoryService.getFirstLevel();
        model.addAttribute("first_category", categoryEntities);
        return "index";
    }

    @GetMapping("index/catalog.json")
    @ResponseBody
    public Map<String, List<Category2Vo>> getCatalogJson() {
        return categoryService.getCatalogJson();
    }


    @GetMapping("hello")
    @ResponseBody
    public String hello() {
        RLock lock = redissonClient.getLock("lock:hello");
        try {
            lock.lock();
            log.info(Thread.currentThread().getName() + "获取锁");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
            log.info(Thread.currentThread().getName() + "释放锁");
        }
        return "world";
    }

    /*
    读写锁测试
     */
    @ResponseBody
    @GetMapping("write")
    public String write() {
        RReadWriteLock rwLock = redissonClient.getReadWriteLock("rwLock");
        RLock writeLock = rwLock.writeLock();
        String uuid = "";
        try {
            writeLock.lock();
            log.info(Thread.currentThread().getName() + "获取写锁: " + uuid);
            uuid = IdUtil.fastSimpleUUID();
            stringRedisTemplate.opsForValue().set("rw", uuid, Duration.ofDays(1));
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
            log.info(Thread.currentThread().getName() + "释放写锁:" + uuid);
        }
        return uuid;
    }

    @ResponseBody
    @GetMapping("read")
    public String read() {
        RReadWriteLock rwLock = redissonClient.getReadWriteLock("rwLock");
        RLock readLock = rwLock.readLock();
        String uuid = "";
        try {
            readLock.lock();
            log.info(Thread.currentThread().getName() + "获取读锁: " + uuid);
            uuid = stringRedisTemplate.opsForValue().get("rw");
//            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            readLock.unlock();
            log.info(Thread.currentThread().getName() + "释放读锁:" + uuid);
        }
        return uuid;
    }

    /*
    信号量
     */
    @GetMapping("park")
    @ResponseBody
    public Boolean park() throws InterruptedException {
        RSemaphore semaphore = redissonClient.getSemaphore("park");
        // 获取一个信号，有效期只有2秒钟。
        return semaphore.tryAcquire(5, TimeUnit.SECONDS);
    }

    @GetMapping("pass")
    @ResponseBody
    public String pass() {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.release();
        return "pass";
    }

    /*
    闭锁
     */
    @ResponseBody
    @GetMapping("door")
    public String door() throws InterruptedException {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.trySetCount(3);
        door.await();
        return "happy";
    }

    @ResponseBody
    @GetMapping("go/{id}")
    public String go(@PathVariable String id) {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown();
        return id;
    }
}
