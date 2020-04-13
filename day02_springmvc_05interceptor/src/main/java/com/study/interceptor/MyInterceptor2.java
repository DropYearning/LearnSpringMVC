package com.study.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器1
 */
public class MyInterceptor2 implements HandlerInterceptor {

    // 预处理，在controller方法执行前执行
    // return true表示放行，执行下一个拦截器，若没有就执行controller方法
    // return false表示不放行，可以利用req和resp
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("MyInterceptor2 preHandle");
        //request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
        return true;
    }


    // 后处理方法，在controller方法执行后执行, 在success.jsp执行之前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("MyInterceptor2 postHandle");
        //request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
    }


    // 在success.jsp页面执行后，这个方法会执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("MyInterceptor2 afterCompletion");
    }
}
