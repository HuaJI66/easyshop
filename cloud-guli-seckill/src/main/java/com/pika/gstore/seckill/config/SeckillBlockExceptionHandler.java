package com.pika.gstore.seckill.config;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.pika.gstore.common.exception.BaseException;
import com.pika.gstore.common.utils.R;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class SeckillBlockExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        R error = R.error(BaseException.TOO_MANY_REQUESTS.getCode(),BaseException.TOO_MANY_REQUESTS.getMsg());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().write(JSONUtil.toJsonStr(error));
    }
}
