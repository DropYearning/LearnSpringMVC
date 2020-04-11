package com.study.servlet;
import javax.servlet.*;
import java.io.IOException;

/**
 * 实现javax.servlet.Servlet接口创建Servlet
 */
public class ServletDemo1 implements Servlet {

    // 手动覆盖Servlet生命周期的实例化方法
    public ServletDemo1() {
        System.out.println("ServletDemo1实例化");
    }

    // 初始化
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("ServletDemo1 init()");
    }

    // servlet可以使用这个方法得到任何启动信息
    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    // 服务方法
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("hello servlet");
    }

    // 允许servlet返回有关其自身的基本信息
    @Override
    public String getServletInfo() {
        return null;
    }

    // 销毁方法
    @Override
    public void destroy() {
        System.out.println("ServletDemo1 destroy()");
    }
}
