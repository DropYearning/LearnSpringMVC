package com.study.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@WebServlet("/demo8")
public class ServletContextForward extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 实现请求转发,将请求转发到com.study.servlet.ServletContextDemo4（/demo7）
        System.out.println("请求了ServletContextForward, 转发至ServletContextDemo4");
        ServletContext servletContext = this.getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/demo7");
        // 将请求向下传递
        requestDispatcher.forward(req, resp);
        System.out.println("转发完毕");
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
