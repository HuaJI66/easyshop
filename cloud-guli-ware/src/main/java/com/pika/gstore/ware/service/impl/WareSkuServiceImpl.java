package com.pika.gstore.ware.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.ware.dao.WareSkuDao;
import com.pika.gstore.ware.entity.WareSkuEntity;
import com.pika.gstore.ware.feign.ProductSkuInfoService;
import com.pika.gstore.ware.service.WareSkuService;
import com.pika.gstore.ware.vo.SkuInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;


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

}
