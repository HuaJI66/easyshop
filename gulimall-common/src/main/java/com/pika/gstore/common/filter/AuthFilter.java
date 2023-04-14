package com.pika.gstore.common.filter;

import com.pika.gstore.common.constant.AuthConstant;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/13 22:24
 */
@Slf4j
public class AuthFilter implements Filter {
    private String authServiceUrl;
    private String gatewayUrl;
    private String authDomain;

    public AuthFilter(String authServiceUrl, String gatewayUrl, String authDomain) {
        this.authServiceUrl = authServiceUrl;
        this.gatewayUrl = gatewayUrl;
        this.authDomain = authDomain;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Set<String> exclude = new HashSet<>();
        exclude.add(".*/login");
        //认证服务
        exclude.add(authServiceUrl);
        //网关
        exclude.add(gatewayUrl);
        HttpSession session = ((HttpServletRequest) request).getSession();
        StringBuffer requestURL = ((HttpServletRequest) request).getRequestURL();
        String url = requestURL.toString().trim();
        //登录请求直接放行
        if (urlPass(url, exclude)) {
//            log.info("放行请求:{}", url);
            chain.doFilter(request, response);
        } else {
            log.warn("拦截请求:{}", url);
            //非登录请求 ==>是否已登录?
            if (session.getAttribute(AuthConstant.SESSION_LOGIN_USER) == null) {
                ((HttpServletResponse) response).sendRedirect(authDomain + "login.html");
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * 是否放行
     */
    private static boolean urlPass(String url, Set<String> set) {
        for (String s : set) {
            if (url.matches(s)) {
                return true;
            }
        }
        return false;
    }
}
