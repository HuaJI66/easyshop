package com.pika.gstore.member.vo;

import lombok.Data;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/10 17:19
 */
@Data
public class UserRegistryReqVo {
    private String username;
    private String password;
    private String phone;
}
