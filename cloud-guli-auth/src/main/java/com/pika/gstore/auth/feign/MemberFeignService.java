package com.pika.gstore.auth.feign;

import com.pika.gstore.auth.vo.UserRegistryReqVo;
import com.pika.gstore.common.to.GiteeUserInfoTo;
import com.pika.gstore.common.to.UserLoginTo;
import com.pika.gstore.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author pi'ka'chu
 */
@FeignClient("cloud-guli-member")
public interface MemberFeignService {
    @PostMapping("/member/registry/add")
    R userRegistry(@RequestBody UserRegistryReqVo vo);

    @PostMapping("/member/login/get")
    R getMember(@RequestBody UserLoginTo userLoginTo);

    @PostMapping("/member/oauth/gitee/login")
    R loginOrRegistry(GiteeUserInfoTo giteeUserInfoTo);
}
