package com.study.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 继承javax.servlet.http.HttpServlet类（模板方法设计模式）
 */
public class ServletDemo3 extends HttpServlet {
    // 默认可以不重写任何方法，返回HTTP Status 405 – 方法不允许，但是Tomcat后台不报错


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ServletDemo3：hello doGet!");
        System.out.println(req.getRemoteAddr());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ServletDemo3：hello doPost!");
    }
}
