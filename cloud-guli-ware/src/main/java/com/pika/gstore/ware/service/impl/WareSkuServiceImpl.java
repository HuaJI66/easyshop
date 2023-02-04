package com.pika.gstore.ware.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.constant.MqConstant;
import com.pika.gstore.common.enume.OrderStatusEnum;
import com.pika.gstore.common.exception.BaseException;
import com.pika.gstore.common.to.SkuHasStockVo;
import com.pika.gstore.common.to.StockLockedTo;
import com.pika.gstore.common.to.WareOrderTaskDetailTo;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.ware.dao.WareSkuDao;
import com.pika.gstore.ware.entity.WareOrderTaskDetailEntity;
import com.pika.gstore.ware.entity.WareOrderTaskEntity;
import com.pika.gstore.ware.entity.WareSkuEntity;
import com.pika.gstore.common.exception.NoStockException;
import com.pika.gstore.ware.feign.OrderFeignService;
import com.pika.gstore.ware.feign.ProductSkuInfoService;
import com.pika.gstore.ware.service.WareOrderTaskDetailService;
import com.pika.gstore.ware.service.WareOrderTaskService;
import com.pika.gstore.ware.service.WareSkuService;
import com.pika.gstore.ware.vo.OrderItemVo;
import com.pika.gstore.common.to.OrderTo;
import com.pika.gstore.ware.vo.SkuInfoVo;
import com.pika.gstore.ware.vo.WareSkuLockVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author pi'ka'chu
 */
@Service("wareSkuService")
@Slf4j
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Resource
    private ProductSkuInfoService SkuInfoFeignService;
    @Resource
    private WareOrderTaskService wareOrderTaskService;
    @Resource
    private WareOrderTaskDetailService wareOrderTaskDetailService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private OrderFeignService orderFeignService;

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
    @Transactional(rollbackFor = Exception.class)
    public boolean lockStock(WareSkuLockVo wareSkuLockVo) {
        //保存库存工作单
        WareOrderTaskEntity task = new WareOrderTaskEntity();
        task.setOrderSn(wareSkuLockVo.getOrderSn());
        task.setTaskStatus(1);
        wareOrderTaskService.save(task);

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
            if (wareIds == null || wareIds.size() == 0) {
                throw new NoStockException(skuId);
            }
            for (Long wareId : wareIds) {
                Long row = baseMapper.lockStock(skuId, wareId, stock.getNum());
                if (row == 1) {
                    WareOrderTaskDetailEntity detail =
                            new WareOrderTaskDetailEntity(null, skuId, null, stock.num, task.getId(), wareId, 1);
                    wareOrderTaskDetailService.save(detail);

                    //todo 当前仓库锁定成功.向mq发送message
                    StockLockedTo lockedTo = new StockLockedTo();
                    lockedTo.setTaskId(task.getId());
                    WareOrderTaskDetailTo detailTo = new WareOrderTaskDetailTo();
                    BeanUtils.copyProperties(detail, detailTo);
                    lockedTo.setDetail(detailTo);
                    rabbitTemplate.convertAndSend(MqConstant.STOCK_EVENT_EXCHANGE, MqConstant.STOCK_LOCKED_KEY, lockedTo);
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

    @Override
    public Long unlockStock(Long skuId, Long wareId, Integer skuNum, Long detailId, Long taskId) {
        Long res = baseMapper.unlockStock(skuId, wareId, skuNum, detailId);
        WareOrderTaskDetailEntity detail = new WareOrderTaskDetailEntity();
        detail.setId(detailId);
        detail.setLockStatus(2);
        wareOrderTaskDetailService.updateById(detail);
        return res;
    }


    @Data
    class SkuWareHasStock {
        private Long skuId;
        private Integer num;
        private List<Long> wareIds;
    }

    @Override
    public void releaseStock(StockLockedTo to) {
        WareOrderTaskDetailTo detail = to.getDetail();
        Long detailId = detail.getId();
        WareOrderTaskDetailEntity taskDetail = wareOrderTaskDetailService.getById(detailId);

        if (taskDetail != null && taskDetail.getLockStatus() == 1) {
            WareOrderTaskEntity task = wareOrderTaskService.getById(to.getTaskId());

            log.info("收到订单号:" + task.getOrderSn());

            R r = orderFeignService.getOrderStatus(task.getOrderSn());
            Assert.notNull(r, "远程查询订单状态失败");
            //订单存在且已取消
            if (r.getCode() == 0) {
                OrderTo vo = r.getData(new TypeReference<OrderTo>() {
                });
                if (vo != null && vo.getStatus().equals(OrderStatusEnum.CANCELED.getCode())) {
                    Long res = unlockStock(detail.getSkuId(), detail.getWareId(), detail.getSkuNum(), detailId, detail.getTaskId());
                    log.info("解锁库存:" + detail.getSkuId() + " 结果:" + res);
                }
            }
            //订单不存在(回滚)
            else if (r.getCode() == BaseException.ORDER_NOT_EXISTS_EXCEPTION.getCode()) {
                Long res = unlockStock(detail.getSkuId(), detail.getWareId(), detail.getSkuNum(), detailId, detail.getTaskId());
                log.info("解锁库存:" + detail.getSkuId() + " 结果:" + res);
            } else {
                throw new RuntimeException("远程调用查询失败");
            }
        }
    }

    @Override
    public void releaseStock(OrderTo order) {
        if (order.getStatus().equals(OrderStatusEnum.CANCELED.getCode())) {
            WareOrderTaskEntity task = wareOrderTaskService.getOrderTasksByOrderSn(order.getOrderSn());
            List<WareOrderTaskDetailEntity> list = wareOrderTaskDetailService.list(new LambdaQueryWrapper<WareOrderTaskDetailEntity>()
                    .eq(WareOrderTaskDetailEntity::getTaskId, task.getId())
                    .eq(WareOrderTaskDetailEntity::getLockStatus, 1)
            );
            if (list != null && list.size() > 0) {
                for (WareOrderTaskDetailEntity detail : list) {
                    Long res = unlockStock(detail.getSkuId(), detail.getWareId(), detail.getSkuNum(), detail.getId(), detail.getTaskId());
                    log.info("解锁库存:" + detail.getSkuId() + " 结果:" + res);
                }
            }
        }
    }
}
