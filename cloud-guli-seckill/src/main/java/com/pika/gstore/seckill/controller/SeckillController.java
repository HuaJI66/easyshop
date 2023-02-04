package com.pika.gstore.seckill.controller;

import com.pika.gstore.common.utils.R;
import com.pika.gstore.seckill.service.SeckillService;
import com.pika.gstore.seckill.to.SeckillSkuRedisTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/3 0:04
 */
@RestController
public class SeckillController {
    @Resource
    private SeckillService seckillService;

    @GetMapping("getCurrSkus")
    public R getCurrSeckillSkus() {
        List<SeckillSkuRedisTo> redisToList = seckillService.getCurrSeckillSkus();
        return R.ok().setData(redisToList);
    }

    @GetMapping("/seckill/sku/{skuId}")
    public R getSkuSeckillInfo(@PathVariable String skuId) {
        List<SeckillSkuRedisTo> to = seckillService.getSkuSeckillInfo(skuId);
        return R.ok().setData(to.stream().sorted(SeckillSkuRedisTo::compareTo));
    }

    /**
     * /doSeckill?skuId=7&sessionId=4&code=a3aaed5c459843a4827a09b1eed67038&num=1
     */
    @GetMapping("doSeckill")
    public ModelAndView doSeckill(@RequestParam("skuId") String skuId, @RequestParam("sessionId") String sessionId,
                                  @RequestParam("code") String code, @RequestParam("num") Integer num) {
        String orderSn= seckillService.doSeckill(skuId, sessionId, code, num);
        ModelAndView modelAndView = new ModelAndView("success");
        modelAndView.addObject("orderSn", orderSn);
        return modelAndView;
    }
}
