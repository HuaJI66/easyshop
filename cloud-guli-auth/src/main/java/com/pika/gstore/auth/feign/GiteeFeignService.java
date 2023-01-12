package com.pika.gstore.auth.feign;

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
@FeignClient(name = "https://gitee.com", url = "https://gitee.com")
public interface GiteeFeignService {
    @GetMapping("/api/v5/emails")
    List<GiteeEmailVo> getAllEmail(@RequestParam("access_token") String access_token);

    @PostMapping("/oauth/token")
    GiteeAccessTokenRepVo getAccessToken(@RequestBody GiteeAccessTokenReqVo giteeAccessTokenReqVo);

    @PostMapping("/oauth/token")
    GiteeAccessTokenRepVo refreshToken(@RequestParam(name = "grant_type", defaultValue = "refresh_token") String grant_type,
                                       @RequestParam("refresh_token") String refresh_token);

    @GetMapping("api/v5/user")
    GiteeUserInfoTo getUserInfo(@RequestParam("access_token") String access_token);
}
