package com.study.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServletContextDemo4 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        test1();
        test2();
        test3();
    }




    // 获取/WEB-INF下的a.properties配置文件
    private void test1() throws IOException {
        String path = this.getServletContext().getRealPath("/WEB-INF/a.properties");
        System.out.println(path);
        // 创建一个操作Properties文件的对象
        Properties prop = new Properties();
        prop.load(new FileInputStream(path));
        System.out.println(prop.getProperty("key"));
    }

    // 获取b.properties配置文件
    private void test2() throws IOException {
        String path = this.getServletContext().getRealPath("/WEB-INF/classes/b.properties");
        System.out.println(path);
        // 创建一个操作Properties文件的对象
        Properties prop = new Properties();
        prop.load(new FileInputStream(path));
        System.out.println(prop.getProperty("key"));
    }

    // 获取c.properties配置文件
    private void test3() throws IOException {
        String path = this.getServletContext().getRealPath("/WEB-INF/classes/com/study/servlet/c.properties");
        System.out.println(path);
        // 创建一个操作Properties文件的对象
        Properties prop = new Properties();
        prop.load(new FileInputStream(path));
        System.out.println(prop.getProperty("key"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
