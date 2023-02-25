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
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Set<String> exclude = new HashSet<>();
        exclude.add(".*/login");
        exclude.add("^http://127.0.0.1:10000/.*");
        //网关
        exclude.add("http://127.0.0.1:5000/");
        HttpSession session = ((HttpServletRequest) request).getSession();
        StringBuffer requestURL = ((HttpServletRequest) request).getRequestURL();
        String url = requestURL.toString().trim();
        //登录请求直接放行
        if (urlPass(url, exclude)) {
            log.info("放行请求:{}", url);
            chain.doFilter(request, response);
        } else {
            log.warn("拦截请求:{}", url);
            //非登录请求 ==>是否已登录?
            if (session.getAttribute(AuthConstant.SESSION_LOGIN_USER) == null) {
                ((HttpServletResponse) response).sendRedirect(AuthConstant.AUTH_URL);
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
