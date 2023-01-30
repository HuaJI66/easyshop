package com.pika.gstore.order.controller;

import java.util.Arrays;
import java.util.Map;

import com.pika.gstore.common.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pika.gstore.order.entity.OrderEntity;
import com.pika.gstore.order.service.OrderService;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.R;


/**
 * 订单
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 22:34:09
 */
@RestController
@RequestMapping("order/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/status")
    public R getOrderStatus(@RequestParam("orderSn") String orderSn) {
        OrderEntity order = orderService.getOrderStatus(orderSn);
        return order != null ? R.ok().setData(order) : R.error(BaseException.ORDER_NOT_EXISTS_EXCEPTION.getCode(),BaseException.ORDER_NOT_EXISTS_EXCEPTION.getMsg());
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("order:order:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("order:order:info")
    public R info(@PathVariable("id") Long id) {
        OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("order:order:save")
    public R save(@RequestBody OrderEntity order) {
        orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("order:order:update")
    public R update(@RequestBody OrderEntity order) {
        orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("order:order:delete")
    public R delete(@RequestBody Long[] ids) {
        orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
