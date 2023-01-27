package com.pika.gstore.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.member.entity.MemberReceiveAddressEntity;

import java.util.List;
import java.util.Map;

/**
 * 会员收货地址
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 21:24:18
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<MemberReceiveAddressEntity> getMemberReceiveAddress(Long id);
}

