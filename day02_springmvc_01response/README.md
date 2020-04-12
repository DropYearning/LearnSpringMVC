# LearnSpringMVC-day2-SpringMVC-响应数据和结果视图
[SpringMVC教程IDEA版哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7qa?from=search&seid=12129021327363557187)

## 1 返回值分类
### 1.1 返回字符串
- 使用Model将数据存入Request, 使JSP页面可以从Request中取出值展示
    - Model模型对象的作用主要是保存数据，可以借助它们将数据带到前端
    ```java
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
    ```
- 在前端jsp页面中要设置：`isELIgnored="false"`
    - 注意，在jsp中要使用Map中相同的key获取值
   ```jsp
    <body>
        <h3>执行成功了</h3>
        ${user1.username}
        ${user1.password}
    </body>
    ```

### 1.2 返回值是void
- 之前的方法我们都返回一个"success"指向显示结果的jsp页面，那如果设置方法的返回值是void，程序会将其发送给哪一个jsp呢？——结果如下：
    - 页面显示HTTP Status 404 – 未找到/day02/WEB-INF/pages/proj1/testVoid.jsp
    - testVoid方法仍然会在控制台执行
- 在返回void的情况下，默认跳转@RequestMapping("/xxx")指向的xxx.jsp
- 方法一：使用Servlet的请求转发API可以指定void控制器方法跳转指定jsp:
    ```java
    @RequestMapping("/testVoid")
    public void testVoid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { // 使用Model对象存入Request, 使JSP页面可以从Request中取出值展示
        System.out.println("testVoid方法执行了");
        // 使用Servlet的请求转发API
        request.getRequestDispatcher("/WEB-INF/pages/success.jsp").forward(request, response);
        return;
    }
    ```
- 方法二：使用重定向(重定向相当于这个控制器又帮我们重新发出了一次请求，总计2次请求)

> WEB-INF是受保护的文件夹，Servlet API中的forward请求转发是服务器内部的请求，可以访问到WEB-INF下的页面。而请求转发相当于重新发出了一次请求，不能访问 WEB-INF目录下的jsp文件，因此我们将请求转发至webapps/response.jsp

```java
@RequestMapping("/testVoid")
    public void testVoid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { // 使用Model对象存入Request, 使JSP页面可以从Request中取出值展示
        System.out.println("testVoid方法执行了");
        // 采用重定向的方法
        System.out.println(request.getContextPath()); // 输出/day02，即我们在Tomcat下deploy的Application Context路径
        // WEB-INF是受保护的资源文件夹，因此重定向不能定向至WEB-INF内部的jsp文件
        response.sendRedirect(request.getContextPath()+"/response.jsp");
    }
}
```

- 方法三：直接用输出流对页面进行响应
```java
@RequestMapping("/testVoid")
    public void testVoid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { // 使用Model对象存入Request, 使JSP页面可以从Request中取出值展示
        System.out.println("testVoid方法执行了");
        // 直接输出流向页面响应

        // 解决中文乱码
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        response.getWriter().print("你好！");

    }
```
- ![aM27OVt](https://i.imgur.com/aM27OVt.png)

### 1.3 返回值是ModelAndView对象
- ModelAndView 是 SpringMVC 为我们提供的一个对象，该对象也可以用作控制器方法的返回值
- 1.1中直接返回String类型的方法底层也是使用ModelAndView对象完成的

## 2 转发或重定向
- 转发：`return "forward:/WEB-INF/pages/success.jsp";`
- 重定向：`return "redirect:/response.jsp";`

> 重定向关键字与之前讲的一样不能访问WEB-INF目录下的文件

## 3 ResponseBody响应JSON数据
- 需求：使用@ResponseBody注解实现将controller方法返回的对象转换为json响应给客户端。
- 环境准备：
    - 1、在springmvc.xml中配置前端控制器不去拦截静态资源
        ```xml
        <!-- 设置静态资源不过滤 -->
        <mvc:resources location="/css/" mapping="/css/**"/> <!-- 样式 -->
        <mvc:resources location="/images/" mapping="/images/**"/> <!-- 图片 -->
        <mvc:resources location="/js/" mapping="/js/**"/> <!-- javascript -->
        ```
    - 2、在jsp页面中引入jquery.js：`<script src="js/jquery.min.js" ></script>`
- 目标:jsp页面使用Ajax向服务器端发送json数据，服务器端将json数据封装到一个javabean对象中

> 只要前台传送给后端的数据的key与javaBean中的变量名一致，可以自动封装为实例。【需要引入jackson的jar包】
>> Jackson是一个简单基于Java应用库，Jackson可以轻松的将Java对象转换成json对象和xml文档，同样也可以将json、xml转换成Java对象。Jackson所依赖的jar包较少，简单易用并且性能也要相对高些，并且Jackson社区相对比较活跃，更新速度也比较快。

- jackson的Maven坐标如下：
    ```xml
    <!--引入jackson来转换Javabean与json/xml-->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.9.0</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>2.9.0</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-annotations</artifactId>
      <version>2.9.0</version>
    </dependency>
    ```
- 控制器代码如下：
    ```java
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
    ```
- jsp页面代码如下：
```jsp
<%--在jsp页面中引入jquery--%>
    <script src="js/jquery.min.js" ></script>

    <script>
        /*页面加载，绑定单击事件*/
        /*这里注意要在web.xml中配置前端控制器不要拦截静态资源，否则js无效*/
        $(function () {
            $("#btn").click(function () {
                /*发送ajax请求*/
                $.ajax({
                    // json，设置属性和值，将数据传至服务器
                    url:"proj1/testAjax",
                    contentType:"application/json;charset=UTF-8",
                    data:'{"username":"hehe", "password":"123", "age":30}',
                    dataType:"json",
                    type: "post",
                    success:function (data) { //提供处理函数
                        // data是服务器端响应的json数据，进行解析
                        alert(data);
                        alert(data.username);
                        alert(data.age);
                    }
                });
            });
        })
    </script>
```

## 参考资料
- [Spring MVC @ModelAttribute详解 - 知乎](https://zhuanlan.zhihu.com/p/22539683)
- [SpringMVC中的Model对象_Java_Yang_Hui_Liang的博客-CSDN博客](https://blog.csdn.net/Yang_Hui_Liang/article/details/87931555)
- [jquery中ajax请求的使用和四个步骤示例 | 爱国足de博客](https://www.caizhichao.cn/967.html)
- [【狂神说Java】一小时掌握Ajax_哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Kt411u7BV?from=search&seid=2546920281783366685)






















