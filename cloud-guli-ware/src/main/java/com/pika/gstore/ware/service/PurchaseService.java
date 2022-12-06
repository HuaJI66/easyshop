package com.pika.gstore.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.ware.entity.PurchaseEntity;
import com.pika.gstore.ware.vo.MergeVo;
import com.pika.gstore.ware.vo.PurchaseDoneVo;

import java.util.Map;

/**
 * 采购信息
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-22 08:44:12
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryUnReceive(Map<String, Object> params);

    void merge(MergeVo mergeVo);

    void received(Long[] ids);

    void done(PurchaseDoneVo purchaseDoneVo);
}

