# LearnSpringMVC-day2-SpringMVC-拦截器
[SpringMVC教程IDEA版哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7qa?from=search&seid=12129021327363557187)

## 1 拦截器概念
- ![KU6DyZV](https://i.imgur.com/KU6DyZV.png)
- ![HSwONQ0](https://i.imgur.com/HSwONQ0.jpg)
- **拦截器**，在AOP（Aspect-Oriented Programming）中用于在某个方法或字段被访问之前，进行拦截然后在之前或之后加入某些操作。拦截是AOP的一种实现策略。
- Spring MVC 的处理器拦截器类似于 Servlet 开发中的过滤器 Filter，用于对处理器进行预处理和后处理。
    - 拦截器链（Interceptor Chain）就是将拦截器按一定的顺序联结成一条链。在访问被拦截的方法或字段时，拦截器链中的拦截器就会按其之前定义的顺序被调用。
- SpringMVC拦截器与Servlet过滤器的区别：
    - 过滤器是 servlet 规范中的一部分，任何 java web 工程都可以使用。
    - 拦截器是 SpringMVC 框架自己的，只有使用了 SpringMVC 框架的工程才能用。
    - 过滤器在 url-pattern 中配置了/*之后，可以对所有要访问的资源拦截。
    - **拦截器只会拦截访问的控制器方法，如果访问的是 jsp，html,css,image 或者js是不会进行拦截的**。
    - 可以认为拦截器能做的事情过滤器都能做，但过滤器能做的拦截器不一定能做
- **拦截器也是AOP思想的具体应用**

## 2 使用拦截器
- 1、 编写一个普通类实现 HandlerInterceptor 接口 ，可以选择性地实现其中的preHandle、postHandle、afterCompletion三个方法
    - preHandle：预处理，在controller方法执行前执行
    - postHandle：后处理方法，在controller方法执行后执行, 在跳转到success.jsp执行之前执行
    - afterCompletion：在success.jsp页面执行后，这个方法会执行
    - 若不放行，可以进行页面跳转： `request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);`
   ```java
    /**
     * 自定义拦截器1
     */
    public class MyInterceptor1  implements HandlerInterceptor {
        // 预处理，在controller方法执行前执行
        // return true表示放行，执行下一个拦截器，若没有就执行controller方法
        // return false表示不放行，可以利用req和resp
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            System.out.println("MyInterceptor1 preHandle");
            //request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return true;
        }
    
    
        // 后处理方法，在controller方法执行后执行, 在success.jsp执行之前执行
        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
            System.out.println("MyInterceptor1 postHandle");
            //request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
        }
    
    
        // 在success.jsp页面执行后，这个方法会执行
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            System.out.println("MyInterceptor1 afterCompletion");
        }
    }
    ```
- 2、在springmvc.xml中声明拦截器
    ```xml
    <!-- 配置拦截器 -->
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/**"/>
            <bean id="handlerInterceptorDemo1"
            class="com.itheima.web.interceptor.HandlerInterceptorDemo1"></bean>
        </mvc:interceptor>
    </mvc:interceptors>
    ```

- 3、执行结果
    ```
    [2020-04-13 10:40:16,738] Artifact day02_springmvc_05interceptor:war exploded: Deploy took 2,038 milliseconds
    (点击拦截器url)
    MyInterceptor1 preHandle
    testInterceptor 方法执行了
    MyInterceptor1 postHandle
    success.jsp执行了...
    MyInterceptor1 afterCompletion
    ```

## 3 拦截器的细节

### 3.1 拦截路径
- `<mvc:mapping path="/**"/>` 表示拦截所有
- `<mvc:mapping path="/proj1/*"/> `表示拦截http://localhost:8080/day02/proj1/* 【day02是AppContext】
    
### 3.2 拦截器的执行顺序
- 若在xml中配置了多个可以拦截相同url的拦截器，执行顺序按xml中的配置顺序
- ![b0SBFvm](https://i.imgur.com/b0SBFvm.png)


## 4 拦截器的应用（验证用户是否登录）

### 4.1 实现思路
- 1、有一个登录页面，需要写一个 controller 访问页面
- 2、登录页面有一提交表单的动作。需要在 controller 中处理。
    - 2.1、判断用户名密码是否正确
    - 2.2、如果正确，向 session 中写入用户信息
    - 2.3、返回登录成功。
- 3、拦截用户请求，判断用户是否登录
    - 3.1、如果用户已经登录。放行
    - 3.2、如果用户未登录，跳转到登录页面

### 4.2 控制器代码
```java
//登陆页面
@RequestMapping("/login")
public String login(Model model)throws Exception{
    return "login";
}
//登陆提交
//userid：用户账号，pwd：密码
@RequestMapping("/loginsubmit")
public String loginsubmit(HttpSession session,String userid,String pwd)throws
Exception{
    //向 session 记录用户身份信息
    session.setAttribute("activeUser", userid);
    return "redirect:/main.jsp";
}

//退出
@RequestMapping("/logout")
public String logout(HttpSession session)throws Exception{
    //session 过期
    session.invalidate();
    return "redirect:index.jsp";
}
```
### 4.3 拦截器代码
```java
public class LoginInterceptor implements HandlerInterceptor{
    @Override
    Public boolean preHandle(HttpServletRequest request,
    HttpServletResponse response, Object handler) throws Exception {
        //如果是登录页面则放行
        if(request.getRequestURI().indexOf("login.action")>=0){
        return true;
    }
    HttpSession session = request.getSession();
    //如果用户已登录也放行
    if(session.getAttribute("user")!=null){
        return true;
    }
    //用户没有登录挑战到登录页面
    request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request,response);
    return false;
    }
}
```

## 参考资料
- [第三十章：SpringMVC中的拦截器 - 知乎](https://zhuanlan.zhihu.com/p/43484367)
- [基于SpringMVC的拦截器（Interceptor）和过滤器（Filter）的区别与联系-zifangsky的博客备份-51CTO博客](https://blog.51cto.com/983836259/1880286)













