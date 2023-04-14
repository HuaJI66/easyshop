package com.pika.gstore.order.controller;

import com.pika.gstore.common.exception.BaseException;
import com.pika.gstore.common.prooerties.DomainProperties;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.order.entity.OrderEntity;
import com.pika.gstore.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 订单
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 22:34:09
 */
@RestController
@RequestMapping("order/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Resource
    private DomainProperties domainProperties;

    @GetMapping("/status")
    public R getOrderStatus(@RequestParam("orderSn") String orderSn) {
        OrderEntity order = orderService.getOrderByOrderSn(orderSn);
        return order != null ? R.ok().setData(order) : R.error(BaseException.ORDER_NOT_EXISTS_EXCEPTION.getCode(), BaseException.ORDER_NOT_EXISTS_EXCEPTION.getMsg());
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
    @PostMapping("/currUserOrderList")
    public R currUserOrderItemList(@RequestBody Map<String, Object> params) {
        PageUtils page = orderService.currUserOrderItemList(params);

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

    @GetMapping("/del")
    public RedirectView delOrderByOrderSn(@RequestParam String orderSn, RedirectView redirectView) {
        try {
            boolean result = orderService.delOrderByOrderSn(orderSn);
            log.info("删除订单" + orderSn + " :" + result);
        } catch (Exception ignore) {
        }
        redirectView.setUrl(domainProperties.getMember() + "/memberOrder.html");
        return redirectView;
    }
}
