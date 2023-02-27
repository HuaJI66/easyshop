package com.pika.gstore.third.controller;

import com.pika.gstore.common.utils.R;
import com.pika.gstore.third.service.PayFactory;
import com.pika.gstore.third.service.PayService;
import com.pika.gstore.third.vo.PayVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PayWebController {
    @GetMapping(value = "/payOrder", produces = "text/html")
    @ResponseBody
    public R payOrder(@RequestBody PayVo payVo,
                      @RequestParam("payType") Integer payType) {
        try {
            PayService payService = PayFactory.getPayService(payType);
            Object pay = payService.doPay(payVo);
            return R.ok().setData(pay);
        } catch (Exception e) {
            return R.error();
        }
    }
}
