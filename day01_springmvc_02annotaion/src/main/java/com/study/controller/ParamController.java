package com.study.controller;

import com.study.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 请求参数绑定
 */
@Controller
@RequestMapping("/proj2")
public class ParamController {

    // 请求参数绑定
    @RequestMapping("/testparam")
    public String testParam(String username, String password){
        System.out.println("testParam执行了" + "username:" + username + ", password:" + password);
        return "success";
    }

    // 保存用户，把数据封装到javabean的实例中
    @RequestMapping("/saveaccount")
    public String testParam(Account account){
        System.out.println("saveaccount执行了");
        System.out.println(account);
        return "success";
    }

    // Servlet原生API
    @RequestMapping("/servlet")
    public String testServlet(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("testServlet执行了");
        System.out.println(req);
        HttpSession session = req.getSession();
        System.out.println(session);
        ServletContext servletContext = session.getServletContext();
        System.out.println(servletContext);
        System.out.println(resp);
        return "success";
    }
}
