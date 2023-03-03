package com.pika.gstore.third.controller;

import com.pika.gstore.common.utils.R;
import com.pika.gstore.third.service.pay.SimplePayFactory;
import com.pika.gstore.third.service.pay.PayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Controller
public class PayWebController {
    @PostMapping(value = "/payOrder")
    @ResponseBody
    public R payOrder(@RequestBody HashMap<String, String> payVo,
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
