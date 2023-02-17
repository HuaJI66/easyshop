package com.pika.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/2/15 19:02
 */
public class TestWebFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
