package com.pika.gstore.member.interceptor;

import com.pika.gstore.common.constant.AuthConstant;
import com.pika.gstore.common.constant.DomainConstant;
import com.pika.gstore.common.to.MemberInfoTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;


/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/14 13:57
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    public static ThreadLocal<MemberInfoTo> threadLocal = new ThreadLocal<>();

    /**
     * 获取用户登录信息
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        log.info("拦截请求:"+request.getRequestURI());
        HttpSession session = request.getSession();
        MemberInfoTo loginUser = (MemberInfoTo) session.getAttribute(AuthConstant.SESSION_LOGIN_USER);
        //1. loginUser==>
        if (loginUser != null) {
            // 1.1已登录
            threadLocal.set(loginUser);
            return true;
        } else {
            // 1.2未登录
            String queryString = request.getQueryString();
            if (StringUtils.isEmpty(queryString)) {
                queryString = "";
            } else {
                queryString = "?" + queryString.trim();
            }
            String redirectUrl = "/login.html?redirect_url=" + DomainConstant. MEMBER_DOMAIN+ request.getRequestURI() + queryString;
            session.setAttribute("errors", Collections.singletonMap("msg", "请登录"));
            response.sendRedirect( DomainConstant.AUTH_DOMAIN + redirectUrl);
            return false;
        }
    }

    /**
     * 下发cookie: user-key ==> uuid
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 防止内存泄露
        threadLocal.remove();
    }
}
