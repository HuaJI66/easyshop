package com.pika.gstore.common.to;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.Length;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/10 23:04
 */
@Data
public class UserLoginTo {
    /**
     * username/phone
     */
    @NotEmpty(message = "账号不能为空")
    private String account;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
