package com.pika.gstore.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.ware.entity.WareInfoEntity;
import com.pika.gstore.ware.vo.FareVo;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-22 08:44:12
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryByCondition(Map<String, Object> params);

    FareVo getFare(Long addrId);
}

