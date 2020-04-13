package com.study.servlet;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 不使用过滤器的Servlet演示
 */
public class ServletDemo1 extends HttpServlet {

    @Override
    public void destroy() {
        System.out.println("ServletDemo1 destroy");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("ServletDemo1 init");
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("ServletDemo1 service");
    }
}
