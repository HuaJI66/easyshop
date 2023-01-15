package com.pika.gstore.cart.to;

import lombok.Data;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/14 13:54
 */
@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;
    private boolean isTempUser=false;
}
