package com.pika.gstore.auth.config;

import com.pika.gstore.auth.feign.GiteeFeignService;
import com.pika.gstore.auth.vo.GiteeAccessTokenRepVo;
import com.pika.gstore.auth.vo.GiteeAccessTokenReqVo;
import com.pika.gstore.auth.vo.GiteeEmailVo;
import com.pika.gstore.common.to.GiteeUserInfoTo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/11 19:42
 */
@Configuration
@RefreshScope
@Data
public class GiteeLoginConfigUtil {
    @Resource
    private GiteeFeignService giteeFeignService;
    /**
     * 默认授权成功后的回调地址
     */
    public static final String DEFAULT_REDIRECT_URL = "http://auth.gulimall.com/oauth/gitee/success";

    /**
     * gitee跳转授权 url前缀
     */
    public static final String GITEE_AUTH__BASE_URL = "https://gitee.com/oauth/authorize?client_id=";
    @Value("${auth.gitee.client-id}")
    public String giteeClientId;
    @Value("${auth.gitee.client-secret}")
    public String giteeClientSecret;

    /**
     * Desc:
     * 授权码模式
     * - 应用通过 浏览器 或 Webview 将用户引导到码云三方认证页面上（ ET请求 ）
     * https://gitee.com/oauth/authorize?client_id={client_id}&redirect_uri={redirect_uri}&response_type=code
     *
     * @param redirect_uri 回调地址
     */
    public String getGiteeAuthUrl(String redirect_uri) {
        return GITEE_AUTH__BASE_URL + giteeClientId
                + "&redirect_uri=" + redirect_uri
                + "&response_type=code";
    }

    public String getGiteeAuthUrl() {
        return getGiteeAuthUrl(DEFAULT_REDIRECT_URL);
    }

    /**
     * Desc: 使用feign远程调用
     * 应用服务器 或 Webview 使用 access_token API 向 码云认证服务器发送post请求传入 用户授权码 以及 回调地址（ POST请求 ）
     * 注：请求过程建议将 client_secret 放在 Body 中传值，以保证数据安全。
     * https://gitee.com/oauth/token?grant_type=authorization_code&code={code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}
     *
     * @param
     * @return {@link String}
     */

    public GiteeAccessTokenRepVo getAccessToken(String code, String redirectUrl) {
        GiteeAccessTokenReqVo reqVo = new GiteeAccessTokenReqVo();
        reqVo.setCode(code);
        reqVo.setClient_id(giteeClientId);
        reqVo.setRedirect_uri(redirectUrl);
        reqVo.setClient_secret(giteeClientSecret);
        return giteeFeignService.getAccessToken(reqVo);
    }

    public GiteeAccessTokenRepVo getAccessToken(String code) {
        return getAccessToken(code, DEFAULT_REDIRECT_URL);
    }

    /**
     * 码云认证服务器返回 access_token
     * 应用通过 access_token 访问 Open API 使用用户数据。
     * https://gitee.com/oauth/token?grant_type=refresh_token&refresh_token={refresh_token}
     */
    public GiteeAccessTokenRepVo refreshToken(String refresh_token) {
        return giteeFeignService.refreshToken("refresh_token", refresh_token);
    }

    /**
     * 获取授权用户的全部邮箱
     */
    public List<GiteeEmailVo> getAllEmail(String accessToken) {
        return giteeFeignService.getAllEmail(accessToken);
    }
    public GiteeUserInfoTo getUserInfo(String access_token){
        return giteeFeignService.getUserInfo(access_token);
    }
}
