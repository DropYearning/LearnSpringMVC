package com.study.controller;

import com.study.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Date;
import java.util.Map;

/**
 * 演示常用注解的控制类
 */
@Controller
@RequestMapping(path = "proj3")
@SessionAttributes(value = {"msg"}) // 把msg=美美存入到session域sessionScope中
public class AnnoController {

    @RequestMapping("/testRequestParam")
    public String testRequestParam(@RequestParam(name = "name" ,required = false) String username){
        System.out.println("testRequestParam方法执行了");
        System.out.println(username);
        return "success";
    }

    // 测试获取请求体的内容
    @RequestMapping("/testRequestBody")
    public String testRequestBody(@RequestBody String body){
        System.out.println("testRequestBody");
        System.out.println(body);
        return "success";
    }

    // 测试@PathVariable注解
    @RequestMapping("/testPathVariable/{sid}")
    public String testPathVariable(@PathVariable(name ="sid") String id){
        System.out.println("testRequestBody");
        System.out.println(id);
        return "success";
    }

    // 测试@RequestHeader，获取并输出请求头
    @RequestMapping("/testRequestHeader")
    public String testRequestHeader(@RequestHeader(value = "accept") String header){
        System.out.println("testRequestHeader");
        System.out.println(header);
        return "success";
    }

    // 测试@CookieValue
    @RequestMapping("/testCookieValue")
    public String testCookieValue(@CookieValue(value = "JSESSIONID") String cookieValue){
        System.out.println("testCookieValue");
        System.out.println(cookieValue);
        return "success";
    }

    // 测试@ModelAttribute
    @RequestMapping("/testModelAttribute")
    public String testModelAttribute(@ModelAttribute("abc") User user){
        System.out.println("testModelAttribute");
        System.out.println(user);
        return "success";
    }

     //ModelAttribute基于 POJO 属性的基本使用
     //该方法会比控制器方法先执行
     //只会替换到前端表单中没有填入的值！
    //@ModelAttribute
    //public User showUser(String uname){
    //    System.out.println("showUser方法执行了");
    //    // 通过用户名查询数据库（模拟）
    //    User user = new User();
    //    user.setUname(uname);
    //    user.setAge(20);
    //    user.setDate(new Date());
    //    return user;
    //}

    //基于 Map 的应用场景示例 1：ModelAttribute 修饰方法不带返回值
    @ModelAttribute
    public void showUser(String uname, Map<String, User> map){
        System.out.println("showUser方法执行了");
        // 通过用户名查询数据库（模拟）
        User user = new User();
        user.setUname(uname);
        user.setAge(20);
        user.setDate(new Date());
        map.put("abc", user);
    }

    // 测试@SessionAttribute
    @RequestMapping("/testSessionAttribute")
    public String testSessionAttribute(Model model){
        System.out.println("testSessionAttribute");
        // 底层会存储到request域中,以k:v的形式：{msg=美美}
        model.addAttribute("msg", "美美");
        return "request";
    }

    // 测试@SessionAttribute，取值
    @RequestMapping("/testGetSessionAttribute")
    public String testGetSessionAttribute(ModelMap modelMap){
        System.out.println("testGetSessionAttribute");
        String msg = (String) modelMap.get("msg");
        System.out.println(msg);
        return "request";
    }

    // 测试@SessionAttribute, 清除
    @RequestMapping("/testDeleteSessionAttribute")
    public String testDeleteSessionAttribute(SessionStatus status){
        System.out.println("testDeleteSessionAttribute");
        status.setComplete();
        return "request";
    }

}
