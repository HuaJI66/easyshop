package com.pika.gstore.auth.vo;

import com.pika.gstore.auth.config.GiteeLoginConfigUtil;
import lombok.Data;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/11 23:10
 */
@Data
public class GiteeAccessTokenReqVo {
    private String grant_type = "authorization_code";
    private String code;
    private String client_id;
    private String redirect_uri;
    private String client_secret;
}
