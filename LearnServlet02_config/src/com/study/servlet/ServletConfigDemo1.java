package com.study.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletConfigDemo1 extends HttpServlet {

    private ServletConfig config;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.config = config; // 重载GenericServlet中的init(ServletConfig config)ServletConfig
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String encoding = config.getInitParameter("encoding");// 获得配置文件中的参数信息
        System.out.println(encoding);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
