package com.pika.gstore.third.pay.controller;

import com.pika.gstore.common.utils.R;
import com.pika.gstore.third.pay.service.PayService;
import com.pika.gstore.third.pay.service.SimplePayFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author pi'ka'chu
 */
@Controller
public class PayWebController {
    @PostMapping(value = "/payOrder")
    @ResponseBody
    public R payOrder(@RequestBody Map<String, String> payVo,
                      @RequestParam("payType") Integer payType) {
        try {
            PayService payService = SimplePayFactory.getPayService(payType);
            String pay = (String) payService.doPay(payVo);
            return R.ok().setData(pay);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}
