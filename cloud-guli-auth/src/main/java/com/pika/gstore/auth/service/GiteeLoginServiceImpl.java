package com.pika.gstore.auth.service;

import com.pika.gstore.auth.feign.GiteeFeignService;
import com.pika.gstore.auth.vo.GiteeAccessTokenRepVo;
import com.pika.gstore.auth.vo.GiteeAccessTokenReqVo;
import com.pika.gstore.auth.vo.GiteeEmailVo;
import com.pika.gstore.common.constant.DomainConstant;
import com.pika.gstore.common.to.GiteeUserInfoTo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pi'ka'chu
 */
@Service
public class GiteeLoginServiceImpl implements Oauth2Service {
    @Resource
    private GiteeFeignService giteeFeignService;

    /**
     * 默认授权成功后的回调地址
     */
    public static final String DEFAULT_REDIRECT_URL = DomainConstant.AUTH_DOMAIN + "oauth/gitee/success";

    /**
     * gitee跳转授权 url前缀
     */
    public static final String GITEE_AUTH__BASE_URL = "https://gitee.com/oauth/authorize?client_id=";
    @Value("${auth.gitee.client-id}")
    public String giteeClientId;
    @Value("${auth.gitee.client-secret}")
    public String giteeClientSecret;


    @Override
    public String getAuthUrl() {
        return getGiteeAuthUrl(DEFAULT_REDIRECT_URL);
    }

    /**
     * Desc:
     * 授权码模式
     * - 应用通过 浏览器 或 Webview 将用户引导到码云三方认证页面上（ ET请求 ）
     * https://gitee.com/oauth/authorize?client_id={client_id}&redirect_uri={redirect_uri}&response_type=code
     *
     * @param redirectUri 回调地址
     */
    public String getGiteeAuthUrl(String redirectUri) {
        return GITEE_AUTH__BASE_URL + giteeClientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";
    }

    @Override
    public GiteeAccessTokenRepVo getAccessToken(String code) {
        return getAccessToken(code, DEFAULT_REDIRECT_URL);
    }

    public GiteeAccessTokenRepVo getAccessToken(String code, String redirectUrl) {
        GiteeAccessTokenReqVo reqVo = new GiteeAccessTokenReqVo();
        reqVo.setCode(code);
        reqVo.setClient_id(giteeClientId);
        reqVo.setRedirect_uri(redirectUrl);
        reqVo.setClient_secret(giteeClientSecret);
        return giteeFeignService.getAccessToken(reqVo);
    }

    @Override
    public GiteeUserInfoTo getUserInfo(String accessToken) {
        GiteeUserInfoTo userInfo = giteeFeignService.getUserInfo(accessToken);
        userInfo.setAccess_token(accessToken);
        if (StringUtils.isEmpty(userInfo.getEmail())) {
            try {
                String email = getAllEmail(accessToken).get(0).getEmail();
                userInfo.setEmail(email);
            } catch (Exception ignored) {
            }
        }
        return userInfo;
    }

    public List<GiteeEmailVo> getAllEmail(String accessToken) {
        return giteeFeignService.getAllEmail(accessToken);
    }

    public GiteeAccessTokenRepVo refreshAccessToken() {
        return giteeFeignService.refreshToken("refresh_token", "refresh_token");
    }
}
