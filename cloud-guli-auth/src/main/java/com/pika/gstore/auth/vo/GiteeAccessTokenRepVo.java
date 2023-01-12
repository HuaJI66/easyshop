package com.pika.gstore.auth.vo;

import lombok.Data;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/11 21:00
 */
@Data
public class GiteeAccessTokenRepVo {
    private Integer expires_in;
    private String scope;
    private String refresh_token;
    private String access_token;
    private String token_type;
    private Integer created_at;
}
