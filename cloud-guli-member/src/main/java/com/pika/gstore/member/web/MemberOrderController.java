package com.pika.gstore.member.web;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.pika.gstore.common.to.OrderTo;
import com.pika.gstore.common.utils.Constant;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.member.feign.OrderFeignService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/31 16:23
 */
@Controller
public class MemberOrderController {
    @Resource
    private OrderFeignService orderFeignService;

    @GetMapping("/memberOrder.html")
    public String memberOrder(@RequestParam(value = "pageNum", defaultValue = "1") String pageNum, Model model) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, pageNum);
        R r = orderFeignService.currUserOrderItemList(params);
        if (r.getCode() == 0) {
            PageUtils page = r.getData("page", new TypeReference<PageUtils>() {
            });
            model.addAttribute("pages", page);
        }
        return "orderList";
    }
}
