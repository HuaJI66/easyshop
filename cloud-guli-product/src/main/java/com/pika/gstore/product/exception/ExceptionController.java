package com.pika.gstore.product.exception;

import com.pika.gstore.common.exception.BaseException;
import com.pika.gstore.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/11/27 21:41
 */
@RestControllerAdvice(basePackages = "com.pika.gstore.product.controller")
@Slf4j
public class ExceptionController {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public R handle(MethodArgumentNotValidException e) {
        HashMap<String, String> map = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError ->
                map.put(fieldError.getField(), fieldError.getDefaultMessage()
                ));
        log.error(e.getMessage());
        return R.error(BaseException.INVALID_DATA.getCode(), BaseException.INVALID_DATA.getMsg()).put("error", map);
    }


    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public R test1(HttpMessageNotReadableException e) {
        log.error(e.getMessage());
        return R.error(BaseException.CONVERT_ERROR.getCode(), BaseException.CONVERT_ERROR.getMsg());
    }

    @ExceptionHandler(value = Exception.class)
    public R test2(Exception e) {
        String name = e.getClass().getName();
        log.error(name);
        log.error(e.getMessage());
        return R.error(BaseException.UNKOWN_EXCEPTION.getCode(), BaseException.UNKOWN_EXCEPTION.getMsg());
    }
}

