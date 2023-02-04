package com.pika.gstore.order.web;

import com.alipay.api.AlipayApiException;
import com.pika.gstore.order.config.AlipayTemplate;
import com.pika.gstore.order.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/31 13:46
 */
@Controller
public class PayWebController {
    @Resource
    private AlipayTemplate alipayTemplate;
    @Resource
    private OrderService orderService;

    @GetMapping(value = "/payOrder",produces = "text/html")
    @ResponseBody
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        return alipayTemplate.pay(orderService.getPayVo(orderSn));
    }
}
