package com.pika.gstore.auth.web;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.net.URLEncodeUtil;
import com.pika.gstore.auth.feign.MemberFeignService;
import com.pika.gstore.auth.service.impl.GiteeLoginServiceImpl;
import com.pika.gstore.auth.vo.GiteeAccessTokenRepVo;
import com.pika.gstore.common.constant.AuthConstant;
import com.pika.gstore.common.prooerties.DomainProperties;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    @Resource
    private DomainProperties domainProperties;

    @GetMapping("login.html")
    public String preLogin(Model model, HttpSession session, HttpServletRequest request,
                           @RequestParam(value = "redirect_url", required = false) String redirectUrl) {
        String queryString = request.getQueryString();
        if ((!StringUtils.isEmpty(queryString)) && queryString.startsWith(domainProperties.getRedirectUrl())) {
            redirectUrl = queryString.trim().replace(domainProperties.getRedirectUrl() + "=", "");
        }
        URLEncodeUtil.encodeAll(queryString);
        //todo 校验cookie
        if (session.getAttribute(AuthConstant.SESSION_LOGIN_USER) == null) {
            model.addAttribute("gitee_url", giteeLoginService.getAuthUrl());
            model.addAttribute("redirect_url", StringUtils.isEmpty(redirectUrl) ? domainProperties.getMain() : redirectUrl);
            return "login";
        }
        return "redirect:" + redirectUrl;
    }

    @GetMapping("oauth/gitee/success")
    public String giteeAuth(@RequestParam(value = "redirect_url", required = false) String redirectUrl,
                            String code, String state, HttpSession session, RedirectAttributes redirectAttributes) {
        GiteeAccessTokenRepVo repVo = giteeLoginService.getAccessToken(code);
        GiteeUserInfoTo userInfo = giteeLoginService.getUserInfo(repVo.getAccess_token());

        try {
            R r = memberFeignService.loginOrRegistry(userInfo);
            if (r.getCode() == 0) {
                MemberInfoTo data = r.getData(new TypeReference<MemberInfoTo>() {
                });
                session.setAttribute(AuthConstant.SESSION_LOGIN_USER, data);
                return "redirect:" + (StringUtils.isEmpty(redirectUrl) ? domainProperties.getMain() : redirectUrl);
            } else {
                session.setAttribute("errors", Collections.singletonMap("msg", r.getMsg()));
                return "redirect:" + domainProperties.getAuth() + "login.html";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errors", Collections.singletonMap("msg", e.getMessage()));
            return "redirect:" + domainProperties.getAuth() + "login.html";
        }

    }

    @PostMapping("login")
    public String login(
            @RequestParam(value = "redirect_url", required = false) String redirectUrl,
            @Valid UserLoginTo userLoginTo,
            BindingResult result, RedirectAttributes redirectAttributes,
            HttpSession session, HttpServletResponse response) {
        log.warn("redirect_url:{}", redirectUrl);
        if (result.hasErrors()) {
            HashMap<String, String> map = new HashMap<>(result.getFieldErrorCount());
            map.put("msg", "账号和密码不能为空");
            redirectAttributes.addFlashAttribute("errors", map);
            return "redirect:" + domainProperties.getAuth() + "login.html";
        }
        R r = memberFeignService.getMember(userLoginTo);
        if (r.getCode() == 0) {
            MemberInfoTo user = r.getData(new TypeReference<MemberInfoTo>() {
            });
            session.setAttribute(AuthConstant.SESSION_LOGIN_USER, user);
            return "redirect:" + (StringUtils.isEmpty(redirectUrl) ? domainProperties.getMain() : redirectUrl);
        } else {
            redirectAttributes.addFlashAttribute("errors", Collections.singletonMap("msg", r.getMsg()));
            return "redirect:" + domainProperties.getAuth() + "login.html";
        }
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        Object loginUser = session.getAttribute(AuthConstant.SESSION_LOGIN_USER);
        if (!Objects.isNull(loginUser)) {
            session.removeAttribute(AuthConstant.SESSION_LOGIN_USER);
            session.invalidate();
        }
        return "redirect:" + domainProperties.getMain();
    }
}
