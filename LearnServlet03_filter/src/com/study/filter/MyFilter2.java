package com.study.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * 过滤器，对服务器接受的请求资源、服务器响应给浏览器的资源进行管理
 */
public class MyFilter2 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("MyFilter2 init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("MyFilter2 doFilter 执行了");
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("MyFilter2 doFilter 执行结束");
    }

    @Override
    public void destroy() {
        System.out.println("MyFilter2 destroy");
    }
}
