package com.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// 控制器类
@Controller
@RequestMapping(path = "proj1")
public class HelloController {
    // 配置请求的映射
    @RequestMapping(path="/hello")
    public String sayHello(){
        System.out.println("Hello SpringMVC！");
        return "success"; // 默认指明了返回到的jsp文件名字（返回到success.jsp）
    }

    @RequestMapping(path = "/testRequestMapping",method = {RequestMethod.GET, RequestMethod.POST})
        // http://localhost:8080/start/proj1/testRequestMapping?username=1
    public String testRequestMapping(){
        System.out.println("测试RequestMapping注解");
        return "success";
    }
}
