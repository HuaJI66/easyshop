package com.pika.gstore.auth.service;


/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/24 22:36
 */
public interface Oauth2Service {
    /**
     * 获取需要跳转登录的url
     */
    String getAuthUrl();

    /**
     * 获取授权令牌
     */
    Object getAccessToken(String code);


    /**
     * 获取用户信息
     */
    Object getUserInfo(String accessToken);
}
