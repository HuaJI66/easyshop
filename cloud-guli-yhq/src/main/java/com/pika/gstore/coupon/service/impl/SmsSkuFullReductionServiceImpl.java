package com.pika.gstore.coupon.service.impl;

import com.pika.gstore.common.to.MemberPrice;
import com.pika.gstore.common.to.SkuReductionTo;
import com.pika.gstore.coupon.entity.SmsMemberPriceEntity;
import com.pika.gstore.coupon.entity.SmsSkuLadderEntity;
import com.pika.gstore.coupon.service.SmsSkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;

import com.pika.gstore.coupon.dao.SmsSkuFullReductionDao;
import com.pika.gstore.coupon.entity.SmsSkuFullReductionEntity;
import com.pika.gstore.coupon.service.SmsSkuFullReductionService;

import javax.annotation.Resource;


@Service("smsSkuFullReductionService")
public class SmsSkuFullReductionServiceImpl extends ServiceImpl<SmsSkuFullReductionDao, SmsSkuFullReductionEntity> implements SmsSkuFullReductionService {
    @Resource
    private SmsSkuLadderService ladderService;
    @Resource
    private SmsMemberPriceServiceImpl memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SmsSkuFullReductionEntity> page = this.page(
                new Query<SmsSkuFullReductionEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveReductionInfo(SkuReductionTo reduction) {
        //1. sku的优惠满减等信息
        // gulimall_sms->sms_sku_ladder
        SmsSkuLadderEntity ladder = new SmsSkuLadderEntity();
        ladder.setSkuId(reduction.getSkuId());
        ladder.setFullCount(reduction.getFullCount());
        ladder.setDiscount(reduction.getDiscount());
        ladder.setAddOther(reduction.getCountStatus());
        if (reduction.getFullCount() > 0) {
            ladderService.save(ladder);
        }

        //sms_sku_full_reduction
        SmsSkuFullReductionEntity fullReduction = new SmsSkuFullReductionEntity();
        BeanUtils.copyProperties(reduction, fullReduction);
        if (fullReduction.getFullPrice().compareTo(BigDecimal.ZERO) == 1) {
            save(fullReduction);
        }

        //sms_member_price
        List<MemberPrice> memberPrices = reduction.getMemberPrice();
        if (memberPrices != null && memberPrices.size() > 0) {
            List<SmsMemberPriceEntity> collect = memberPrices.stream()
                    .filter(price -> price.getPrice().compareTo(BigDecimal.ZERO) > 0)
                    .map(price -> {
                        SmsMemberPriceEntity memberPrice = new SmsMemberPriceEntity();
                        memberPrice.setSkuId(reduction.getSkuId());
                        memberPrice.setMemberLevelId(price.getId());
                        memberPrice.setMemberLevelName(price.getName());
                        memberPrice.setMemberPrice(price.getPrice());
                        memberPrice.setAddOther(1);
                        return memberPrice;
                    }).collect(Collectors.toList());
            memberPriceService.saveBatch(collect);
        }
    }

}
