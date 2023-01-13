package com.pika.gstore.auth.web;

import cn.hutool.core.lang.TypeReference;
import com.pika.gstore.auth.config.GiteeLoginConfigUtil;
import com.pika.gstore.auth.feign.MemberFeignService;
import com.pika.gstore.auth.vo.GiteeAccessTokenRepVo;
import com.pika.gstore.common.constant.AuthConstant;
import com.pika.gstore.common.constant.DomainConstant;
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
import javax.servlet.http.HttpSession;
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
    public String loginByGitee(Model model,HttpSession session) {
        if (session.getAttribute(AuthConstant.SESSION_LOGIN_USER) == null) {
            model.addAttribute("gitee_url", giteeLoginConfigUtil.getGiteeAuthUrl());
            return "login";
        }
        return "redirect:http://" + DomainConstant.MAIN_DOMAIN;
    }

    @GetMapping("oauth/gitee/success")
    public String giteeAuth(String code, String state, HttpSession session, RedirectAttributes redirectAttributes) {
        GiteeAccessTokenRepVo repVo = giteeLoginConfigUtil.getAccessToken(code);
        System.out.println("repVo = " + repVo);
        GiteeUserInfoTo userInfo = giteeLoginConfigUtil.getUserInfo(repVo.getAccess_token());
        userInfo.setAccess_token(repVo.getAccess_token());
        if (StringUtils.isEmpty(userInfo.getEmail())) {
            try {
                String email = giteeLoginConfigUtil.getAllEmail(repVo.getAccess_token()).get(0).getEmail();
                userInfo.setEmail(email);
            } catch (Exception ignored) {
            }
        }

        try {
            R r = memberFeignService.loginOrRegistry(userInfo);
            if (r.getCode() == 0) {
                MemberInfoTo data = r.getData(new TypeReference<MemberInfoTo>() {
                });
                session.setAttribute(AuthConstant.SESSION_LOGIN_USER, data);
                return "redirect:http://" + DomainConstant.MAIN_DOMAIN;
            } else {
                session.setAttribute("errors", Collections.singletonMap("msg", r.getMsg()));
                return "redirect:http://" + DomainConstant.AUTH_DOMAIN + "/login.html";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errors", Collections.singletonMap("msg", e.getMessage()));
            return "redirect:http://" + DomainConstant.AUTH_DOMAIN + "/login.html";
        }

    }

    @PostMapping("login")
    public String login(@Valid UserLoginTo userLoginTo,
                        BindingResult result, RedirectAttributes redirectAttributes,
                        HttpSession session) {
        if (result.hasErrors()) {
            HashMap<String, String> map = new HashMap<>(result.getFieldErrorCount());
            map.put("msg", "账号和密码不能为空");
            redirectAttributes.addFlashAttribute("errors", map);
            return "redirect:http://" + DomainConstant.AUTH_DOMAIN + "/login.html";
        }
        R r = memberFeignService.getMember(userLoginTo);
        if (r.getCode() == 0) {
            MemberInfoTo data = r.getData(new TypeReference<MemberInfoTo>() {
            });
            session.setAttribute(AuthConstant.SESSION_LOGIN_USER, data);
            return "redirect:http://" + DomainConstant.MAIN_DOMAIN;
        } else {
            redirectAttributes.addFlashAttribute("errors", Collections.singletonMap("msg", r.getMsg()));
            return "redirect:http://" + DomainConstant.AUTH_DOMAIN + "/login.html";
        }
    }


}
