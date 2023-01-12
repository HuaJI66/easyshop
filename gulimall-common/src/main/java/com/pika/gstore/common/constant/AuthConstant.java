package com.pika.gstore.common.constant;

import lombok.Data;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/9 22:43
 */
@Data
public class AuthConstant {
    /**
     * 验证码缓存前缀
     */
    public static final String SMS_CACHE_CAPTCHA_PREFIX = "sms:captcha:";
    /**
     * 验证码有效期
     */
    public static final int SMS_CACHE_CAPTCHA_EXPIRE = 60 * 5;
    /**
     * 手机号码校验规则
     */
    public static final String VALID_PHONE = "^1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$";
}
