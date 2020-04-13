package com.study.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常的处理器类，处理异常的业务逻辑
 */
public class SysExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex) {
        // 获取到异常对象
        SysException e = null;
        if (ex instanceof SysException){
            e = (SysException) ex;
        }else{
            e = new SysException("系统正在维护");
        }
        // ModelAndView可以帮助我们跳转
        ModelAndView mv = new ModelAndView();
        mv.addObject("errorMsg", e.getMsg()); // 向ModelAndView存入一对键值对
        mv.setViewName("error"); // ModelAndView向视图解析器请求跳转到error页面
        return mv;
    }
}
