package com.pika.gstore.cart.interceptor;

import cn.hutool.core.util.IdUtil;
import com.pika.gstore.cart.to.UserInfoTo;
import com.pika.gstore.common.constant.AuthConstant;
import com.pika.gstore.common.constant.CartConstant;
import com.pika.gstore.common.to.MemberInfoTo;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/14 13:57
 */
public class LoginInterceptor implements HandlerInterceptor {
    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    /**
     * 获取用户登录信息
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loginUser = request.getSession().getAttribute(AuthConstant.SESSION_LOGIN_USER);
        UserInfoTo userInfoTo = new UserInfoTo();
        //1. loginUser==>
        if (loginUser != null) {
            // 1.1已登录
            userInfoTo.setUserId(((MemberInfoTo) loginUser).getId());
        } else {
            // 1.2未登录
            userInfoTo.setTempUser(true);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CartConstant.CART_COOKIE_NAME.equals(cookie.getName())) {
                    userInfoTo.setUserKey(cookie.getValue());
                    break;
                }
            }
        }
        if (userInfoTo.getUserKey() == null) {
            userInfoTo.setUserKey(IdUtil.fastSimpleUUID());
        }
        threadLocal.set(userInfoTo);
        return true;
    }

    /**
     * 下发cookie: user-key ==> uuid
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Cookie cookie = new Cookie(CartConstant.CART_COOKIE_NAME, threadLocal.get().getUserKey());
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            boolean hasUserKey = Arrays.stream(cookies).noneMatch(item -> item.getName().equals(CartConstant.CART_COOKIE_NAME));
            if (hasUserKey) {
                cookie.setDomain("gulimall.com");
                cookie.setMaxAge(CartConstant.CART_COOKIE_MAX_AGE);
                response.addCookie(cookie);
            }
        } else {
            response.addCookie(cookie);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 防止内存泄露
        threadLocal.remove();
    }
}
