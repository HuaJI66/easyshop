package com.pika.gstore.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pika.gstore.common.constant.WareConstant;
import com.pika.gstore.ware.entity.PurchaseDetailEntity;
import com.pika.gstore.ware.service.PurchaseDetailService;
import com.pika.gstore.ware.service.WareSkuService;
import com.pika.gstore.ware.vo.MergeVo;
import com.pika.gstore.ware.vo.PurchaseDoneItemVo;
import com.pika.gstore.ware.vo.PurchaseDoneVo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;

import com.pika.gstore.ware.dao.PurchaseDao;
import com.pika.gstore.ware.entity.PurchaseEntity;
import com.pika.gstore.ware.service.PurchaseService;

import javax.annotation.Resource;


/**
 * @author pi'ka'chu
 */
@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Resource
    private PurchaseDetailService purchaseDetailService;

    @Resource
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryUnReceive(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseEntity::getStatus, 0)
                .or()
                .eq(PurchaseEntity::getStatus, 1);
        IPage<PurchaseEntity> page = this.page(new Query<PurchaseEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public void merge(MergeVo mergeVo) {
        Long purchaseId;
        Long[] items = mergeVo.getItems();

        //只要有任意一项为非新建状态
        List<PurchaseDetailEntity> detailEntities = purchaseDetailService.listByIds(Arrays.asList(items));
        if (detailEntities.stream()
                .anyMatch(item -> item.getStatus() != WareConstant.PurchaseDetailEnum.CREATED.getCode())) {
            return;
        }

        if (mergeVo.getPurchaseId() == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseEnum.CREATED.getCode());
            save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        } else {
            purchaseId = mergeVo.getPurchaseId();
        }

        List<PurchaseDetailEntity> collect = detailEntities.stream().peek(item -> {
            item.setPurchaseId(purchaseId);
            item.setStatus(WareConstant.PurchaseDetailEnum.ASSIGNED.getCode());
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(mergeVo.getPurchaseId());
        purchaseEntity.setUpdateTime(new Date());
        updateById(purchaseEntity);
    }

    @Override
    public void received(Long[] ids) {
        // 1. 确认当前采购单的状态为新建或已分配状态
        List<PurchaseEntity> list = listByIds(Arrays.asList(ids));
        List<PurchaseEntity> collect = list.stream()
                .filter(item -> item.getStatus() == WareConstant.PurchaseEnum.CREATED.getCode() ||
                        item.getStatus() == WareConstant.PurchaseEnum.ASSIGNED.getCode())
                .peek(item -> {
                    item.setStatus(WareConstant.PurchaseEnum.GOT.getCode());
                    item.setUpdateTime(new Date());
                })
                .collect(Collectors.toList());
        // 2.改变采购单状态
        updateBatchById(collect);
        // 3.改变采购项状态
        LambdaUpdateWrapper<PurchaseDetailEntity> wrapper1 = new LambdaUpdateWrapper<>();
        List<Long> purchaseIds = collect.stream().map(PurchaseEntity::getId).collect(Collectors.toList());
        wrapper1.set(PurchaseDetailEntity::getStatus, WareConstant.PurchaseDetailEnum.BUYING.getCode())
                .in(PurchaseDetailEntity::getPurchaseId, purchaseIds);
        purchaseDetailService.update(wrapper1);
    }

    @Override
    public void done(PurchaseDoneVo purchaseDoneVo) {
        AtomicBoolean error = new AtomicBoolean(false);

        // 2. 改变采购项状态
        List<PurchaseDoneItemVo> items = purchaseDoneVo.getItems();
        List<PurchaseDetailEntity> collect = items.stream().map(item -> {
            PurchaseDetailEntity detail = new PurchaseDetailEntity();
            detail.setId(item.getItemId());
            int status = item.getStatus();
            if (status < WareConstant.PurchaseDetailEnum.FINISHED.getCode()) {
                detail.setStatus(status);
                PurchaseDetailEntity purchaseDetail = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(purchaseDetail.getSkuId(), purchaseDetail.getWareId(), purchaseDetail.getSkuNum());
            } else {
                error.set(true);
                detail.setStatus(WareConstant.PurchaseDetailEnum.ERROR.getCode());
            }
            return detail;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);

        // 1. 改变采购单状态
        Long purchaseId = purchaseDoneVo.getId();
        PurchaseEntity entity = new PurchaseEntity();
        entity.setId(purchaseId);
        entity.setUpdateTime(new Date());
        log.warn("是否有异常:    " + error.get());
        entity.setStatus(error.get() ? WareConstant.PurchaseEnum.ERROR.getCode() : WareConstant.PurchaseEnum.FINISHED.getCode());
        updateById(entity);

        // 3. 将成功采购的进行入库
    }

}
