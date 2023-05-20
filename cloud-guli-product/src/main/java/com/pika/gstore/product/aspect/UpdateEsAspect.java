package com.pika.gstore.product.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * TODO: 2023/5/9 每次添加商品的时候都需要重新更新商品索引，观察者模式? AOP?
 *
 * @author pikachu
 * @since 2023/5/9 21:58
 */
@Aspect
@Component
@Slf4j
public class UpdateEsAspect {
    @Pointcut(
            "execution(* com.pika.gstore.product.service.*.update*(..))||" +
                    "execution(* com.pika.gstore.product.service.*.save*(..))||" +
                    "execution(* com.pika.gstore.product.service.*.delete*(..))||" +
                    "execution(* com.pika.gstore.product.service.*.remove*(..))"
    )
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();
        log.info("capture:{} , target: {} ", joinPoint.getSignature().getName(), joinPoint.getTarget());
        // TODO: 2023/5/9 更新 ES 索引
        return proceed;
    }
}
