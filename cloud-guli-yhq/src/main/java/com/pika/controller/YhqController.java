package com.pika.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/11/22 21:33
 */
@RestController
@RequestMapping("yhq")
public class YhqController {
    @GetMapping("test")
    public String test() {
        return "success";
    }
}
