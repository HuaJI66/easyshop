package com.pika.gstore.order.listener;

import com.alipay.api.AlipayApiException;
import com.pika.gstore.order.service.OrderService;
import com.pika.gstore.order.vo.PayAsyncVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/31 22:38
 */
@RestController
@Slf4j
public class OrderPayListener {
    @Resource
    private OrderService orderService;

    @RequestMapping("/paid/notify")
    public String paidNotify(PayAsyncVo response, HttpServletRequest request) throws AlipayApiException {
        return orderService.handlePaidNotify(request,response);
    }
}
