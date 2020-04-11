package com.study.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletContextDemo1 extends HttpServlet {

    // ServletContextDemo1向ServletContext写入
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext applicationContext = this.getServletContext();
        applicationContext.setAttribute("name", "tom"); //在init时写入ServletContext
        System.out.println(applicationContext.getClass().getName()); // 查看具体是哪一个类实现了ServletContext接口
        // org.apache.catalina.core.ApplicationContextFacade
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
