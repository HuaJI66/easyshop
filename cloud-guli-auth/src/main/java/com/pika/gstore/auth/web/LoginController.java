package com.pika.gstore.auth.web;

import cn.hutool.core.lang.TypeReference;
import com.pika.gstore.auth.config.GiteeLoginConfigUtil;
import com.pika.gstore.auth.feign.MemberFeignService;
import com.pika.gstore.auth.vo.GiteeAccessTokenRepVo;
import com.pika.gstore.common.to.GiteeUserInfoTo;
import com.pika.gstore.common.to.MemberInfoTo;
import com.pika.gstore.common.to.UserLoginTo;
import com.pika.gstore.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/11 19:09
 */
@Controller
@Slf4j
public class LoginController {
    @Resource
    private MemberFeignService memberFeignService;
    @Resource
    private GiteeLoginConfigUtil giteeLoginConfigUtil;

    @GetMapping("login.html")
    public String loginByGitee(Model model) {
        model.addAttribute("gitee_url", giteeLoginConfigUtil.getGiteeAuthUrl());
        return "login";
    }

    @GetMapping("oauth/gitee/success")
    public String giteeAuth(String code, String state,RedirectAttributes redirectAttributes) {
        GiteeAccessTokenRepVo repVo = giteeLoginConfigUtil.getAccessToken(code);
        System.out.println("repVo = " + repVo);
        GiteeUserInfoTo userInfo = giteeLoginConfigUtil.getUserInfo(repVo.getAccess_token());
        if (StringUtils.isEmpty(userInfo.getEmail())) {
            String email = giteeLoginConfigUtil.getAllEmail(repVo.getAccess_token()).get(0).getEmail();
            userInfo.setEmail(email);
        }

        log.warn("userInfo:{}", userInfo);
        R r = memberFeignService.loginOrRegistry(userInfo);
        if (r.getCode() == 0) {
            MemberInfoTo data = r.getData(new TypeReference<MemberInfoTo>() {
            });
            redirectAttributes.addFlashAttribute("curr_user", data);
            return "redirect:http://gulimall.com";
        } else {
            redirectAttributes.addFlashAttribute("errors", Collections.singletonMap("msg", r.getMsg()));
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }

    @PostMapping("login")
    public String login(@Valid UserLoginTo userLoginTo,
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            HashMap<String, String> map = new HashMap<>(result.getFieldErrorCount());
            map.put("msg", "账号和密码不能为空");
            redirectAttributes.addFlashAttribute("errors", map);
            return "redirect:http://auth.gulimall.com/login.html";
        }
        R r = memberFeignService.getMember(userLoginTo);
        if (r.getCode() == 0) {
            MemberInfoTo data = r.getData(new TypeReference<MemberInfoTo>() {
            });
            redirectAttributes.addFlashAttribute("curr_user", data);
            return "redirect:http://gulimall.com";
        } else {
            redirectAttributes.addFlashAttribute("errors", Collections.singletonMap("msg", r.getMsg()));
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }


}
