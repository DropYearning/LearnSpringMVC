package com.study.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    public void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
        //servletRequest.setAttribute("str", "监听器学习");
        //servletRequest.setAttribute("str2", "监听器学习2");
        HttpSession session = servletRequest.getSession();
        session.setAttribute("str", "session监听器");
        session.invalidate(); // 手动销毁session
        servletResponse.getWriter().write("this is listener study"); // 不通过jsp，直接响应
        ServletContext sc = this.getServletContext();
        sc.setAttribute("str", "application监听器");

        System.out.println("ServletDemo1 service");
    }
}
