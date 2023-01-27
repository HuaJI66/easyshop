package com.pika.gstore.ware.service.impl;

import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.ware.feign.MemberFeignService;
import com.pika.gstore.ware.vo.FareVo;
import com.pika.gstore.ware.vo.MemberAddressVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;

import com.pika.gstore.ware.dao.WareInfoDao;
import com.pika.gstore.ware.entity.WareInfoEntity;
import com.pika.gstore.ware.service.WareInfoService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {
    @Resource
    private MemberFeignService memberFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                new QueryWrapper<WareInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryByCondition(Map<String, Object> params) {

        LambdaQueryWrapper<WareInfoEntity> wrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.eq(WareInfoEntity::getId, key)
                    .or()
                    .like(WareInfoEntity::getAddress, key)
                    .or()
                    .like(WareInfoEntity::getName, key)
                    .or()
                    .like(WareInfoEntity::getAreacode, key);
        }
        IPage<WareInfoEntity> page = this.page(new Query<WareInfoEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    public FareVo getFare(Long addrId) {
        R r = memberFeignService.info(addrId);
        MemberAddressVo vo = r.getData("memberReceiveAddress",new TypeReference<MemberAddressVo>() {
        });
        String phone = vo.getPhone();
        FareVo fareVo = new FareVo();
        fareVo.setMemberAddressVo(vo);
        fareVo.setFare(new BigDecimal(phone.substring(phone.length() - 1)));
        return fareVo;
    }
}
