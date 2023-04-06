package com.pika.gstore.auth.feign;

import com.pika.gstore.auth.config.TestFeignClientConfig;
import com.pika.gstore.auth.vo.GiteeAccessTokenRepVo;
import com.pika.gstore.auth.vo.GiteeAccessTokenReqVo;
import com.pika.gstore.auth.vo.GiteeEmailVo;
import com.pika.gstore.common.to.GiteeUserInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author pi'ka'chu
 */
@FeignClient(name = "https://gitee.com", url = "https://gitee.com", configuration = TestFeignClientConfig.class)
public interface GiteeFeignService {
    String ACCESS_TOKEN = "access_token";
    String GRANT_TYPE = "grant_type";
    String REFRESH_TOKEN = "refresh_token";
    String CODE = "code";
    String CLIENT_ID = "client_id";
    String REDIRECT_URI = "redirect_uri";
    String CLIENT_SECRET = "client_secret";

    /**
     * 获取授权用户的全部邮箱
     */
    @GetMapping("/api/v5/emails")
    List<GiteeEmailVo> getAllEmail(@RequestParam(ACCESS_TOKEN) String access_token);

    /**
     * Desc: 使用feign远程调用
     * 应用服务器 或 Webview 使用 access_token API 向 码云认证服务器发送post请求传入 用户授权码 以及 回调地址（ POST请求 ）
     * 注：请求过程建议将 client_secret 放在 Body 中传值，以保证数据安全。
     * https://gitee.com/oauth/token?grant_type=authorization_code&code={code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}
     *
     * @param
     * @return {@link String}
     */
    @PostMapping("/oauth/token")
    GiteeAccessTokenRepVo getAccessToken(@RequestBody GiteeAccessTokenReqVo giteeAccessTokenReqVo);


    /**
     * 码云认证服务器返回 access_token
     * 应用通过 access_token 访问 Open API 使用用户数据。
     * https://gitee.com/oauth/token?grant_type=refresh_token&refresh_token={refresh_token}
     */
    @PostMapping("/oauth/token")
    GiteeAccessTokenRepVo refreshToken(@RequestParam(name = GRANT_TYPE, defaultValue = REFRESH_TOKEN) String grant_type,
                                       @RequestParam(REFRESH_TOKEN) String refresh_token);

    @GetMapping("api/v5/user")
    GiteeUserInfoTo getUserInfo(@RequestParam(ACCESS_TOKEN) String access_token);
}
