package com.pika.controller;

import com.pika.feign.YhqService;
import com.pika.gstore.common.utils.R;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/11/22 21:35
 */
@RestController
@RequestMapping("member")
@RefreshScope
public class MemberTestController {
    @Resource
    private YhqService yhqService;

    @GetMapping("test")
    public String test() {
        return yhqService.test();
    }

    @GetMapping("/list")
    public R list() {
        return yhqService.list(new HashMap<>());
    }

}
