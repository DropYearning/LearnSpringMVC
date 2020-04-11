package com.study.servlet;


import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 继承javax.servet.GenericServlet类创建Servlet
 */
public class ServletDemo2 extends GenericServlet {
    // 只需重写service方法
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("Hello ServletDemo2!");
    }
}
