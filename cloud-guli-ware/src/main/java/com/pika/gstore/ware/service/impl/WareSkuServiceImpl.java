package com.pika.gstore.ware.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.to.SkuHasStockVo;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.ware.dao.WareSkuDao;
import com.pika.gstore.ware.entity.WareSkuEntity;
import com.pika.gstore.common.exception.NoStockException;
import com.pika.gstore.ware.feign.ProductSkuInfoService;
import com.pika.gstore.ware.service.WareSkuService;
import com.pika.gstore.ware.vo.OrderItemVo;
import com.pika.gstore.ware.vo.SkuInfoVo;
import com.pika.gstore.ware.vo.WareSkuLockVo;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author pi'ka'chu
 */
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Resource
    private ProductSkuInfoService SkuInfoFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");
        LambdaQueryWrapper<WareSkuEntity> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(skuId)) {
            wrapper.eq(WareSkuEntity::getSkuId, skuId);
        }
        if (!StringUtils.isEmpty(wareId)) {
            wrapper.eq(WareSkuEntity::getWareId, wareId);
        }
        IPage<WareSkuEntity> page = this.page(new Query<WareSkuEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        SkuInfoVo skuInfo = new SkuInfoVo();
        WareSkuEntity wareSku = new WareSkuEntity();
        wareSku.setWareId(wareId);
        wareSku.setSkuId(skuId);
        wareSku.setStock(skuNum);
        // TODO: 2022/12/6 try 防止事务回滚
        try {
            R r = SkuInfoFeignService.info(skuId);
            if (r.getCode() == 0) {
                Object info = r.get("skuInfo");
                log.warn("info: " + info);
                skuInfo = JSONUtil.parse(info).toBean(SkuInfoVo.class);
                log.warn(skuInfo.toString());
                wareSku.setSkuName(skuInfo.getSkuName());
            } else {
                log.error("远程查询sku信息失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.warn(wareSku.toString());
        LambdaUpdateWrapper<WareSkuEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setSql(skuNum > 0, "stock=stock+" + skuNum)
                .set(WareSkuEntity::getSkuName, skuInfo.getSkuName())
                .eq(WareSkuEntity::getWareId, wareId)
                .eq(WareSkuEntity::getSkuId, skuId);
        saveOrUpdate(wareSku, wrapper);
    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
        return skuIds.stream().map(skuId -> {
            Long count = baseMapper.getSkuStock(skuId);
            SkuHasStockVo stockVo = new SkuHasStockVo();
            stockVo.setSkuId(skuId);
            stockVo.setHasStock(count != null ? count > 0 : Boolean.FALSE);
            return stockVo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = NoStockException.class)
    public boolean lockStock(WareSkuLockVo wareSkuLockVo) {
        List<OrderItemVo> locks = wareSkuLockVo.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            stock.setSkuId(item.getSkuId());
            stock.setNum(item.getCount());
            List<Long> list = baseMapper.getSkuWareStock(item.getSkuId());
            stock.setWareIds(list);
            return stock;
        }).collect(Collectors.toList());

        for (SkuWareHasStock stock : collect) {
            boolean lockResult = false;
            Long skuId = stock.getSkuId();
            List<Long> wareIds = stock.getWareIds();
            if (wareIds==null||wareIds.size()==0) {
                throw new NoStockException(skuId);
            }
            for (Long wareId : wareIds) {
                Long row= baseMapper.lockStock(skuId,wareId,stock.getNum());
                if (row == 1) {
                    //当前仓库锁定成功
                    lockResult = true;
                    break;
                }
            }
            if (!lockResult) {
                throw new NoStockException(skuId);
            }
        }
        return true;
    }

    @Data
    class SkuWareHasStock {
        private Long skuId;
        private Integer num;
        private List<Long> wareIds;
    }

}
