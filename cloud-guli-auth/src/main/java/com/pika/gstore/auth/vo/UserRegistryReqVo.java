package com.pika.gstore.auth.vo;

import com.pika.gstore.common.constant.AuthConstant;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/10 10:25
 */
@Data
public class UserRegistryReqVo {
    @NotEmpty(message = "用户名不能为空")
    @Length(min = 6, max = 18, message = "用户名为6到18位")
    private String username;
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 18, message = "密码为6-18位")
    private String password;
    @NotEmpty(message = "验证码不能为空")
    @Pattern(
            regexp = AuthConstant.VALID_PHONE,
            message = "手机号码格式有误"
    )
    private String phone;
    @NotEmpty(message = "验证码不能为空")
    @Length(min = 4, max = 4, message = "请输入4位验证码")
    private String code;
}
