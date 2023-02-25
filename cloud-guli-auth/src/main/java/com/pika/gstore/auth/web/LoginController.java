package com.pika.gstore.auth.web;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.net.URLEncodeUtil;
import com.pika.gstore.auth.feign.MemberFeignService;
import com.pika.gstore.auth.service.GiteeLoginServiceImpl;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

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
    private GiteeLoginServiceImpl giteeLoginService;

    @GetMapping("login.html")
    public String loginByGitee(Model model, HttpSession session, HttpServletRequest request,
                               @CookieValue(value = AuthConstant.AUTH_COOKIE_NAME, required = false) String cookie,
                               @RequestParam(value = "redirect_url", defaultValue = DomainConstant.MAIN_DOMAIN) String redirectUrl) {
        String queryString = request.getQueryString();
        if ((!StringUtils.isEmpty(queryString)) && queryString.startsWith(DomainConstant.REDIRECT_URL)) {
            redirectUrl = queryString.trim().replace(DomainConstant.REDIRECT_URL + "=", "");
        }
        URLEncodeUtil.encodeAll(queryString);
        //todo 校验cookie
        if (session.getAttribute(AuthConstant.SESSION_LOGIN_USER) == null) {
            model.addAttribute("gitee_url", giteeLoginService.getAuthUrl());
            model.addAttribute("redirect_url", redirectUrl);
            return "login";
        }
        return "redirect:" + redirectUrl;
    }

    @GetMapping("oauth/gitee/success")
    public String giteeAuth(@RequestParam(value = "redirect_url", defaultValue = DomainConstant.MAIN_DOMAIN) String redirectUrl,
                            String code, String state, HttpSession session, RedirectAttributes redirectAttributes) {
        GiteeAccessTokenRepVo repVo = giteeLoginService.getAccessToken(code);
        GiteeUserInfoTo userInfo = giteeLoginService.getUserInfo(repVo.getAccess_token());

        try {
            R r = memberFeignService.loginOrRegistry(userInfo);
            if (r.getCode() == 0) {
                MemberInfoTo data = r.getData(new TypeReference<MemberInfoTo>() {
                });
                session.setAttribute(AuthConstant.SESSION_LOGIN_USER, data);
                return "redirect:" + redirectUrl;
            } else {
                session.setAttribute("errors", Collections.singletonMap("msg", r.getMsg()));
                return "redirect:" + DomainConstant.AUTH_DOMAIN + "login.html";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errors", Collections.singletonMap("msg", e.getMessage()));
            return "redirect:" + DomainConstant.AUTH_DOMAIN + "login.html";
        }

    }

    @PostMapping("login")
    public String login(
            @RequestParam(value = "redirect_url", defaultValue = DomainConstant.MAIN_DOMAIN) String redirectUrl,
            @Valid UserLoginTo userLoginTo,
            BindingResult result, RedirectAttributes redirectAttributes,
            HttpSession session, HttpServletResponse response) {
        log.warn("redirect_url:{}", redirectUrl);
        if (result.hasErrors()) {
            HashMap<String, String> map = new HashMap<>(result.getFieldErrorCount());
            map.put("msg", "账号和密码不能为空");
            redirectAttributes.addFlashAttribute("errors", map);
            return "redirect:" + DomainConstant.AUTH_DOMAIN + "login.html";
        }
        R r = memberFeignService.getMember(userLoginTo);
        if (r.getCode() == 0) {
            MemberInfoTo user = r.getData(new TypeReference<MemberInfoTo>() {
            });
            session.setAttribute(AuthConstant.SESSION_LOGIN_USER, user);
            return "redirect:" + redirectUrl;
        } else {
            redirectAttributes.addFlashAttribute("errors", Collections.singletonMap("msg", r.getMsg()));
            return "redirect:" + DomainConstant.AUTH_DOMAIN + "login.html";
        }
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        Object loginUser = session.getAttribute(AuthConstant.SESSION_LOGIN_USER);
        if (!Objects.isNull(loginUser)) {
            session.removeAttribute(AuthConstant.SESSION_LOGIN_USER);
            session.invalidate();
        }
        return "redirect:" + DomainConstant.MAIN_DOMAIN;
    }
}
