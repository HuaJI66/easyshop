package com.pika.gstore.order.web;

import com.pika.gstore.common.constant.DomainConstant;
import com.pika.gstore.common.exception.BaseException;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.order.exception.OrderException;
import com.pika.gstore.order.feign.ThirdFeignService;
import com.pika.gstore.order.service.OrderService;
import com.pika.gstore.order.vo.PayVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/31 13:46
 */
@Controller
@Slf4j
public class PayWebController {
    @Resource
    private OrderService orderService;
    @Resource
    private ThirdFeignService thirdFeignService;

    @GetMapping(value = "/payOrder", produces = {MediaType.TEXT_HTML_VALUE})
    @ResponseBody
    public String payOrder(@RequestParam("orderSn") String orderSn, @RequestParam("payType") Integer payType,
                           HttpServletResponse response) throws IOException {
        log.warn("支付方式:{}", payType);
        try {
            Object payVo = orderService.getPayVo(orderSn,payType);
            R r = thirdFeignService.payOrder(payVo, payType);
            if (r.getCode() == 0) {
                return (String) r.getData();
            }
        } catch (OrderException orderException) {
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            if (orderException.getCode() == BaseException.ORDER_STATUS_CANCEL_EXCEPTION.getCode()) {
                response.sendRedirect(DomainConstant.ORDER_DOMAIN + "toTrade");
            } else if (orderException.getCode() == BaseException.ORDER_STATUS_PAID_EXCEPTION.getCode()) {
                response.sendRedirect(DomainConstant.MEMBER_DOMAIN + "memberOrder.html");
            }
        } catch (Exception ignored) {
        }
        return "error";
    }
}
