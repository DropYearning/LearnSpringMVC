package com.study.controller;


import com.study.exception.SysException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("proj1")
public class UserController {

    @RequestMapping("/testException")
    public String testException() throws SysException{
        System.out.println("testException 方法执行了");
        try {
            // 模拟异常
            int e = 10/0;
        } catch (Exception ex) { // 捕获异常
            ex.printStackTrace();
            // 抛出自定义异常类
            throw new SysException("testException方法出现错误");
        }
        return "success";
    }
}
