# Servlet - 过滤器

## 1 过滤器简介
- ![6YaTNTc](https://i.imgur.com/6YaTNTc.jpg)
- Filter可认为是Servlet的一种“变种”，**它主要用于对用户请求(HttpServletRequest)进行预处理,也可以对服务器响应(HttpServletResponse)进行后处理**，是个典型的处理链。
- 通过Servlet技术我们可以灵活地进行请求的处理, 我们需要对这些服务器的资源进行统一的管理, 比如请求编码格式的统一设置、资源的统一分配等等
- 过滤器:在请求被Servlet处理之前经过统一的操作(一般不涉及业务处理, 只设计对数据的一些预操作), 从而对服务器资源进行管理
	- 对**服务器接受的请求资源**、**服务器响应给浏览器的资源**进行管理
	- 对Servlet提供了一层“防护”(比如可以过滤掉午夜服务器维护时间段内收到的请求)
- Servlet由服务器自动调用创建；**过滤器和Servlet一样是由服务器调用的**。我们只是声明其实现。
- 一个过滤器可以过滤很多个Servlet的请求
- 执行过程：Servlet的作用是针对浏览器发起的请求,进行请求的处理.浏览器发起请求到服务器,服务器接收到请求后,根据URI信息在web,xm1中找到对应的过滤器执行doFi1ter方法,该方法对此次请求进行处理后如果符合要求则放行,放行后如果还有符合要求的过滤则继续进行过滤,找到执行对应的serv1et进行请求处理。 servlet对请求处理完毕后,也就 service方法结束了。还需继续返回相应的doFi1ter方法继续执行。
- Filter与Servlet的区别：Filter不能直接向用户生成响应。完整的流程是：Filter对用户请求进行预处理，接着将请求交给Servlet进行处理并生成响应，最后Filter再对服务器响应进行后处理。




## 2 在Servlet中使用过滤器
- 1、创建一个实现了Filter的过滤器类， 重写其中的init(),doFilter(),destroy()方法
    - init()：服务器启动时立即执行，资源初始化
    - doFilter()：拦截请求的方法，在此方法中可以对资源实现管理
        - 放行，将处理任务传递给链中的下一个资源：`filterChain.doFilter(servletRequest, servletResponse);`
    - destroy()：服务器关闭时执行
- 2、在`web.xml`中配置过滤器
    ```xml
    <!--配置过滤器-->
    <filter>
        <filter-name>myFilter</filter-name>
        <filter-class>com.study.filter.MyFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>myFilter</filter-name>
        <!--/*表示会匹配所有url：路径型的和后缀型的url(包括/login,*.jsp,*.js和*.html等)-->
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    ```
## 3 过滤器效果演示
- 设置两个过滤器，Filter1拦截/*，Filter2拦截*.do

```xml
<!--配置过滤器-->
<filter>
	<filter-name>myFilter</filter-name>
	<filter-class>com.study.filter.MyFilter</filter-class>
</filter>
<filter-mapping>
	<filter-name>myFilter</filter-name>
	<!--/*表示会匹配所有url：路径型的和后缀型的url(包括/login,*.jsp,*.js和*.html等)-->
	<url-pattern>/*</url-pattern>
</filter-mapping>

<!--配置过滤器-->
<filter>
	<filter-name>myFilter2</filter-name>
	<filter-class>com.study.filter.MyFilter2</filter-class>
</filter>
	<filter-mapping>
	<filter-name>myFilter2</filter-name>
	<!--过滤.do结果的请求-->
	<url-pattern>*.do</url-pattern>
</filter-mapping>
```

- Filter1:
    ```java
    public class MyFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            System.out.println("MyFilter1 init");
        }
    
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {  
            System.out.println("MyFilter1 doFilter 执行了");
            // 放行
            //filterChain.doFilter(servletRequest, servletResponse);
            System.out.println("MyFilter1 doFilter 放行");
        }
    
        @Override
        public void destroy() {
            System.out.println("MyFilter1 destroy");
        }
    }
    ```
- Filter2:
    ```java
    public class MyFilter2 implements Filter {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            System.out.println("MyFilter2 init");
        }
    
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    
            System.out.println("MyFilter2 doFilter 执行了");
            // 放行
            //filterChain.doFilter(servletRequest, servletResponse);
            System.out.println("MyFilter2 doFilter 放行");
        }
    
        @Override
        public void destroy() {
            System.out.println("MyFilter2 destroy");
        }
    }
    ```
### 3.1 Filter1和Filter2都不放行
```
MyFilter2 init
MyFilter1 init
[2020-04-13 04:29:27,921] Artifact is deployed successfully
[2020-04-13 04:29:27,922] Deploy took 584 milliseconds
(点击http://localhost:8080/servlet/demo1.do)
ServletDemo1 init
MyFilter1 doFilter 执行了
MyFilter1 doFilter 执行结束
（END，整个请求处理的流程结束）
```
- Filter1的过滤范围大，Filter1先执行了，由于没有设置Filter1放行，因此直至Filter1执行结束，Servlet中的service()方法都没被调用，本次请求处理的流程结束
### 3.2 Filter1放行，Filter2不放行
```
MyFilter2 init
MyFilter1 init
[2020-04-13 04:29:27,921] Artifact is deployed successfully
[2020-04-13 04:29:27,922] Deploy took 584 milliseconds
(点击http://localhost:8080/servlet/demo1.do)
ServletDemo1 init
MyFilter1 doFilter 执行了
MyFilter2 doFilter 执行了
MyFilter2 doFilter 执行结束
MyFilter1 doFilter 执行结束
```
- Filter1在放行之后，服务器又去调用了Filter2，但是由于Filter2不放行，Filter2执行结束之后Servlet中的service()方法都没被调用，于是返回Filter1，Filter1执行完毕之后本次请求处理的流程结束

### 3.3 Filter1和2都放行
```
MyFilter2 init
MyFilter1 init
[2020-04-13 04:29:27,921] Artifact is deployed successfully
[2020-04-13 04:29:27,922] Deploy took 584 milliseconds
(点击http://localhost:8080/servlet/demo1.do)
ServletDemo1 init
MyFilter1 doFilter 执行了
MyFilter2 doFilter 执行了
ServletDemo1 service
MyFilter2 doFilter 执行结束
MyFilter1 doFilter 执行结束
```
- Filter1和2都放行之后，Servlet中的service()方法被调用，service()执行完毕之后response再经过一次Filter2和1,最后本次请求处理的流程结束

### 3.4 总结
- 无论过滤器是否放行，Servlet的对象实例都会在我们点击URL的时刻立即被服务器创建
- Servlet中的service()方法是否执行，要看之前的过滤器是否都放行了
- 过滤器的调用是栈式的

> 多个filter的顺序是由<filter-mapping>的先后决定的


## 4 Filter的生命周期
- Tomcat启动时：`MyFilter init`， 然后Artifact is deployed successfully
- 向URL发出请求时：先`ServletDemo1 init`， 再`MyFilter doFilter`
    - 每一次请求时都只调用方法doFilter()进行处理； 
- Tomcat关闭时：依次`ServletDemo1 destroy`，`MyFilter destroy` 最后 `Disconnected from server`
- 在默认条件下Filter的生命周期比Servlet长


## 5 Filter的应用
- Filter的作用点：
    - 在HttpServletRequest到达Servlet之前，拦截客户的HttpServletRequest。
    - 根据需要检查HttpServletRequest，也可以修改HttpServletRequest头和数据。
    - 在HttpServletResponse到达客户端之前，拦截HttpServletResponse。
    - 根据需要检查HttpServletResponse,也可以修改HttpServletResponse头和数据。
- Filter的应用场景：
    - 用户授权的Filter：Filter负责检查用户请求，根据请求过滤用户非法请求。
    - 日志Filter：详细记录某些特殊的用户请求。
    - 负责解码的Filter：包括对非标准编码的请求解码。
    - session管理：登陆成功的信息放入session后，其他服务直接从session中取登陆信息即可。当session失效时传统方式使用重定向重新登录。**可以用过滤器检查登陆状态，判断具体的业务逻辑是否可以执行（session是否失效）**。
    - Filter可拦截多个请求或响应；一个请求或响应也可被多个请求拦截。
    - 资源管理：图片加水印、关键词识别等
    
## 6 Filter应用实例:使用过滤器统一编码和检查session
```java
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("MyFilter1 doFilter 执行了");
        // 统一编码设置
        servletRequest.setCharacterEncoding("utf-8");
        servletResponse.setContentType("text/html;charset=utf-8");
        // 检查session
        HttpSession session = ((HttpServletRequest) servletRequest).getSession();
        if (session.getAttribute("user" )== null){
            ((HttpServletResponse) servletResponse).sendRedirect("/error.jsp");
        }else{
            // 放行
            filterChain.doFilter(servletRequest, servletResponse);
        }
        System.out.println("MyFilter1 doFilter 执行结束");
    }
```

## 7 拦截器和过滤器的区别

（1）过滤器：

- 依赖于servlet容器。在实现上基于函数回调，可以对几乎所有请求进行过滤，但是缺点是一个过滤器实例只能在容器初始化时调用一次。使用过滤器的目的是用来做一些过滤操作，获取我们想要获取的数据，比如：在过滤器中修改字符编码；在过滤器中修改HttpServletRequest的一些参数，包括：过滤低俗文字、危险字符等

- 关于过滤器的一些用法可以参考这些[文章](http://www.07net01.com/2015/07/860262.html)：
      - 继承HttpServletRequestWrapper以实现在Filter中修改HttpServletRequest的参数：https://www.zifangsky.cn/677.html

      - 在SpringMVC中使用过滤器（Filter）过滤容易引发XSS的危险字符：https://www.zifangsky.cn/683.html

（2）拦截器：

- 依赖于web框架，在SpringMVC中就是依赖于SpringMVC框架。在实现上基于[Java](http://www.07net01.com/tags-Java-0.html)的反射机制，属于面向切面[编程](http://www.07net01.com/)（AOP）的一种运用。由于拦截器是基于web框架的调用，因此可以使用Spring的依赖注入（DI）进行一些业务操作，同时一个拦截器实例在一个controller生命周期之内可以多次调用。但是缺点是只能对controller请求进行拦截，对其他的一些比如直接访问静态资源的请求则没办法进行拦截处理

- 关于过滤器的一些用法可以参考这些文章：
      - 在SpringMVC中使用拦截器（interceptor）拦截CSRF攻击（修）：https://www.zifangsky.cn/671.html

      - SpringMVC中使用Interceptor+[cookie](http://www.07net01.com/tags-cookie-0.html)实现在一定天数之内自动登录：https://www.zifangsky.cn/700.html

![eYuPMJv](https://i.imgur.com/eYuPMJv.jpg)
![95RKyj](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2021/03/95RKyj.jpg)
①拦截器是基于java的反射机制的，而过滤器是基于函数回调。
②**拦截器不依赖与servlet容器，过滤器依赖与servlet容器**。
③拦截器只能对action请求起作用，而过滤器则可以对几乎所有的请求起作用。
④拦截器可以访问action上下文、值栈里的对象，而过滤器不能访问。
⑤在action的生命周期中，拦截器可以多次被调用，而过滤器只能在容器初始化时被调用一次。

⑥拦截器可以获取IOC容器中的各个bean，而过滤器就不行，这点很重要，在拦截器里注入一个service，可以调用业务逻辑。

 

## url-pattern写法细节

-  `<url-pattern>/</url-pattern>`  会匹配到`/login`这样的路径型url，不会匹配到模式为`*.jsp`这样的后缀型url不会进入spring的DispatcherServlet类
- `<url-pattern>/*</url-pattern>` 会匹配所有url：路径型的和后缀型的url(包括/login,*.jsp,*.js和*.html等). 会匹配*.jsp，导致进入spring的DispatcherServlet类，然后去寻找controller，接着找不到对应的controller所以报错。
- `<url-pattern>*.do</url-pattern>` 会匹配到所有以`.do`结尾的的请求，一般可以用来进行模块的拦截。

## 参考
- [分清<url-pattern>/</url-pattern>与<url-pattern>/*</url-pattern>的不同 - xsDao - 博客园](https://www.cnblogs.com/XSdao/p/11448678.html)
- [拦截器和过滤器的区别 - THISISPAN - 博客园](https://www.cnblogs.com/panxuejun/p/7715917.html)
- [Servlet/Filter 的生命周期_Servlet,filter_绿竹痕的博客-CSDN博客](https://blog.csdn.net/liwenjie001/article/details/8920977)
- [过滤器中的chain.doFilter(request,response) - ooooevan - 博客园](https://www.cnblogs.com/ooooevan/p/5727798.html)
- [拦截器和过滤器的区别 - THISISPAN - 博客园](https://www.cnblogs.com/panxuejun/p/7715917.html)























