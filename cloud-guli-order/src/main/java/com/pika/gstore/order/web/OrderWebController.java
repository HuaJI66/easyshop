package com.pika.gstore.order.web;

import com.pika.gstore.common.prooerties.DomainProperties;
import com.pika.gstore.order.service.OrderService;
import com.pika.gstore.order.vo.OrderConfirmVo;
import com.pika.gstore.order.vo.OrderSubmitRepVo;
import com.pika.gstore.order.vo.OrderSubmitVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/16 21:38
 */
@Controller
public class OrderWebController {
    @Resource
    private OrderService orderService;
    @Resource
    private DomainProperties domainProperties;

    @PostMapping("orderSubmit")
    public String orderSubmit(OrderSubmitVo vo, RedirectAttributes attributes, Model model) {
        try {
            OrderSubmitRepVo repVo = orderService.orderSubmit(vo);
            if (repVo.getCode() == 0) {
                model.addAttribute("OrderSubmitRepVo", repVo);
                return "pay";
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("errors", Collections.singletonMap("msg", "订单创建失败: " + e.getMessage()));
        }
        return "redirect:" + domainProperties.getOrder() + "toTrade";
    }


    @GetMapping("toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo vo = orderService.confirmOrder();
        model.addAttribute("orderConfirm", vo);
        return "confirm";
    }
}
