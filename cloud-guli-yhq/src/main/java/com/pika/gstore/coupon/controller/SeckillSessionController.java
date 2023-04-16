package com.pika.gstore.coupon.controller;

import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.coupon.entity.SmsSeckillSessionEntity;
import com.pika.gstore.coupon.service.SmsSeckillSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 秒杀活动场次
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 21:05:12
 */
@RestController
@RequestMapping("coupon/seckillsession")
public class SeckillSessionController {
    @Autowired
    private SmsSeckillSessionService smsSeckillSessionService;

    /**
     * 过去未来3天秒杀商品
     */
    @GetMapping("get3lds")
    public R getFuture3DaySeckillSession() {
        List<SmsSeckillSessionEntity> list = smsSeckillSessionService.getFuture3DaySeckillSession();
        return R.ok().setData(list);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("coupon:smsseckillsession:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = smsSeckillSessionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("coupon:smsseckillsession:info")
    public R info(@PathVariable("id") Long id) {
        SmsSeckillSessionEntity smsSeckillSession = smsSeckillSessionService.getById(id);

        return R.ok().put("smsSeckillSession", smsSeckillSession);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("coupon:smsseckillsession:save")
    public R save(@RequestBody SmsSeckillSessionEntity smsSeckillSession) {
        smsSeckillSessionService.save(smsSeckillSession);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("coupon:smsseckillsession:update")
    public R update(@RequestBody SmsSeckillSessionEntity smsSeckillSession) {
        smsSeckillSessionService.updateById(smsSeckillSession);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("coupon:smsseckillsession:delete")
    public R delete(@RequestBody Long[] ids) {
        smsSeckillSessionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
