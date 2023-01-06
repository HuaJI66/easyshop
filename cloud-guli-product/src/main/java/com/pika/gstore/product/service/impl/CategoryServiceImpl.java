package com.pika.gstore.product.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.to.es.CategoryVo;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;
import com.pika.gstore.product.dao.CategoryDao;
import com.pika.gstore.product.entity.AttrGroupEntity;
import com.pika.gstore.product.entity.CategoryBrandRelationEntity;
import com.pika.gstore.product.entity.CategoryEntity;
import com.pika.gstore.product.service.CategoryBrandRelationService;
import com.pika.gstore.product.service.CategoryService;
import com.pika.gstore.product.vo.Category2Vo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author pi'ka'chu
 */
@Service("categoryService")
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void removeMenus(Long[] catIds) {
        // TODO: 2022/11/25 删除菜单前需要检查是否被引用
        baseMapper.deleteBatchIds(Arrays.asList(catIds));
    }

    @Override
    public Long[] findCatelogIdPath(AttrGroupEntity attrGroup) {
        Long pCid = attrGroup.getCatelogId();
        CategoryEntity categoryEntity;
        ArrayList<Long> result = new ArrayList<>();
        do {
            result.add(pCid);
            categoryEntity = baseMapper.selectById(pCid);
            pCid = categoryEntity.getParentCid();
        } while (pCid != 0);
        Collections.reverse(result);
        return result.toArray(new Long[0]);
    }

    @Override
//    @Caching(evict = {
//            @CacheEvict(cacheNames = "category", key = "'getFirstLevel'"),
//            @CacheEvict(cacheNames = "category", key = "'getCatalogJson'"),
//    })
    //@CacheEvict(cacheNames = "category", key = "'getFirstLevel'")
    @CacheEvict(cacheNames = "category", allEntries = true)
    public void updateCascade(CategoryEntity category) {
        // 更新本表数据
        updateById(category);
        // 更新 CategoryBrandRelation 表数据
        LambdaUpdateWrapper<CategoryBrandRelationEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CategoryBrandRelationEntity::getCatelogName, category.getName())
                .eq(CategoryBrandRelationEntity::getCatelogId, category.getCatId());
        categoryBrandRelationService.update(wrapper);

        // TODO: 2022/11/28 更新其它冗余表数据
    }

    @Override
    @Cacheable(cacheNames = {"category"}, key = "#root.methodName", sync = true)
    public List<CategoryEntity> getFirstLevel() {
        log.info("getFirstLevel");
        return list(new LambdaUpdateWrapper<CategoryEntity>().eq(CategoryEntity::getParentCid, 0));
    }

    @Override
    @Cacheable(cacheNames = "category", key = "#root.methodName", sync = true)
    public Map<String, List<Category2Vo>> getCatalogJson() {
        return getDataFromDB();
    }

    @Override
    public CategoryVo getName(Long catId) {
        CategoryEntity category = getOne(new LambdaQueryWrapper<CategoryEntity>().select(CategoryEntity::getName).eq(CategoryEntity::getCatId, catId));
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

    private String getCatalogJsonDBRedisDistLock() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //需要再次查询缓存,只允许一个线程去查询数据库
        String catalogJson = ops.get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            String uuid = IdUtil.fastSimpleUUID();
            // 1.尝试加锁,设置过期时间,避免死锁
            Boolean result = ops.setIfAbsent("lock", uuid, Duration.ofSeconds(30));
            // 1.1获取锁成功
            if (Boolean.TRUE.equals(result)) {
                log.info(Thread.currentThread().getName() + "获取锁成功" + uuid);
                // 2.查询数据库,try-finally保证出现异常时也要释放锁
                try {
                    Map<String, List<Category2Vo>> dataFromDB = getDataFromDB();
                    // 3.设置缓存
                    catalogJson = JSONUtil.toJsonStr(dataFromDB);
                    ops.setIfAbsent("catalogJson", catalogJson, Duration.ofDays(1));
                } finally {
                    // 4.使用lua脚本释放锁,先获取锁,在删除锁,原子操作
                    String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                            "    return redis.call(\"del\",KEYS[1])\n" +
                            "else\n" +
                            "    return 0\n" +
                            "end";
                    // 1-删除成功,0-删除失败
                    Long execute = stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList("lock"), uuid);
                    if (execute != null && execute == 1) {
                        log.info(Thread.currentThread().getName() + "释放锁成功" + uuid);
                    } else {
                        log.info(Thread.currentThread().getName() + "释放锁失败" + uuid);
                    }
                }
            } else {
                // 1.2获取锁失败,自旋重试
                try {
                    Thread.sleep(300);
                    return getCatalogJsonDBRedisDistLock();
                } catch (InterruptedException ignored) {
                }
            }
        }
        return catalogJson;
    }


    private synchronized String getCatalogJsonDBLocalLock() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String catalogJson = ops.get("catalogJson");
        if (!StringUtils.isEmpty(catalogJson)) {
            return catalogJson;
        }

        log.info("开始查询数据库--------");
        Map<String, List<Category2Vo>> collect = getDataFromDB();
        String jsonStr = JSONUtil.toJsonStr(collect);

        //将结果写入缓存
        ops.set("catalogJson", jsonStr, Duration.ofDays(1));
        return jsonStr;
    }

    private Map<String, List<Category2Vo>> getDataFromDB() {
        log.info(Thread.currentThread().getName() + "开始查询数据库");
        List<CategoryEntity> categoryEntities = list(new LambdaQueryWrapper<CategoryEntity>()
                .select(CategoryEntity::getCatId, CategoryEntity::getParentCid, CategoryEntity::getName));
        return categoryEntities.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .collect(Collectors.toMap(key -> key.getCatId().toString(), first -> categoryEntities.stream().filter(i -> i.getParentCid().equals(first.getCatId()))
                        .map(second -> new Category2Vo(second.getCatId().toString(), second.getName(), first.getCatId().toString(),
                                categoryEntities.stream().filter(j -> j.getParentCid().equals(second.getCatId())).collect(Collectors.toList())
                                        .stream().map(third -> new Category2Vo.Category3Vo(third.getCatId().toString(), third.getName(), second.getCatId().toString()))
                                        .collect(Collectors.toList()))).collect(Collectors.toList())));
    }

    @Override
    public List<CategoryEntity> listTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        return entities != null ? entities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity.getCatId(), entities)))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    private List<CategoryEntity> getChildren(Long parentId, List<CategoryEntity> all) {
        return parentId != null ? all.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid().equals(parentId))
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity.getCatId(), all)))
                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .collect(Collectors.toList()) : new ArrayList<>();
    }
}
