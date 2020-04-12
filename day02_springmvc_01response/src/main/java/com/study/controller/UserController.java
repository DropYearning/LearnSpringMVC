package com.study.controller;

import com.study.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/proj1")
public class UserController {

    @RequestMapping("/testString")
    public String testString(Model model){ // 使用Model对象存入Request, 使JSP页面可以从Request中取出值展示
        System.out.println("testString方法执行了");
        // 模拟从数据库中查询出用户对象
        User user = new User();
        user.setUsername("美美");
        user.setAge(20);
        user.setPassword("123456");
        //模型对象的作用主要是保存数据，可以借助它们将数据带到前端
        model.addAttribute("user1", user);
        return "success";
    }

    @RequestMapping("/testVoid")
    public void testVoid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { // 使用Model对象存入Request, 使JSP页面可以从Request中取出值展示
        System.out.println("testVoid方法执行了");
        //// 使用Servlet的请求转发API
        //request.getRequestDispatcher("/WEB-INF/pages/success.jsp").forward(request, response);
        //return;
        // 采用重定向的方法
        //System.out.println(request.getContextPath()); // 输出/day02，即我们在Tomcat下deploy的Application Context路径
        //// WEB-INF是受保护的资源文件夹，因此重定向不能定向至WEB-INF内部的jsp文件
        //response.sendRedirect(request.getContextPath()+"/response.jsp");

        // 直接输出流向页面响应
        // 解决中文乱码
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        response.getWriter().print("你好！");

    }

    @RequestMapping("/testModelAndView")
    public ModelAndView testModelAndView() {
        // 创建一个ModelAndView对象
        ModelAndView mv = new ModelAndView();
        System.out.println("testModelAndView方法执行了");
        // 模拟从数据库中查询出用户对象
        User user = new User();
        user.setUsername("花花");
        user.setAge(20);
        user.setPassword("123456");
        // 将user对象存储到mv中，mv底层也会将user存储在request域中
        mv.addObject("user", user);
        // 设置跳转至哪一个页面
        mv.setViewName("success"); // 让视图解析器找到success.jsp
        return mv;
    }

    // 使用关键字的方式来进行转发或者重定向
    @RequestMapping("/testForwardOrRedirect")
    public String testForwardOrRedirect(){
        System.out.println("testForwardOrRedirect 方法执行了");

       //// 请求的转发(不能再使用视图解析器，而应该写清楚全路径)
       // return "forward:/WEB-INF/pages/success.jsp";
        // 重定向
        return "redirect:/response.jsp";

    }

    // 模拟Ajax异步请求和响应请求
    @RequestMapping("/testAjax")
    // jackson jar包可以自动封装json数据为JavaBean，因此参数不用再写requestBody，而是User对象
    public @ResponseBody User testAjax(@RequestBody User user){
        System.out.println("testAjax 方法执行了");
        // 客户端(jsp)发送Ajax请求，传送到后台的是JSON格式数据，服务器利用jackson封装JSON为User对象
        System.out.println(user); //
        // 服务器端可以作出响应，例如模拟假装查询数据库
        user.setAge(50); // 假设后台查出真实年龄为50
        // 为了向客户端返回对象，该方法的返回值应该为User类型。
        // 使用@ResponseBody注解来将返回的User对象转换成JSON返回给客户端
        return user;
    }


}
