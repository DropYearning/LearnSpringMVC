# Servlet补充

## 1 ServletConfig的使用1：获取Servlet配置信息
- 有三种方式可以获取ServletConfig中的信息
- ServletConfig中的初始信息要写入xml中
    ```xml
    <servlet>
        <servlet-name>servletConfigDemo1</servlet-name>
        <servlet-class>com.study.servlet.ServletConfigDemo1</servlet-class>
        <init-param>
            <!--配置servlet的参数配置信息-->
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
    </servlet>
    ```
### 1.1 方法一：重写GenericServlet中的init(config)方法获取ServletConfig
```java
public class ServletConfigDemo1 extends HttpServlet {

    private ServletConfig config; // 实例变量，表示读取到的servlet配置信息

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.config = config; // 重载GenericServlet中的init(ServletConfig config)ServletConfig
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String encoding = config.getInitParameter("encoding");// 获得配置文件中的参数信息
        System.out.println(encoding);
    }
}
```


### 1.2 方法二：调用父类GenericServlet中的getInitParameter()方法
- **这种方法最简单**
```java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String encoding = super.getInitParameter("encoding"); // 直接调用父类GenericServlet中的getInitParameter()方法
    System.out.println(encoding);
}
```

### 1.3 方法三：调用接口Servlet中.getServletConfig.getInitParameter()
```java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String encoding = this.getServletConfig().getInitParameter("encoding");
    System.out.println(encoding);
}
```

## 2 ServletConfig的使用2：获取ServletContext对象
### 2.1 ServletContext概述
- ServletContext: 代表的是整个应用。**一个应用只有一个ServletContext对象。单实例**。
- ServletContext是一个全局的储存信息的空间，**由多个Servlet共享**，**服务器开始就存在，服务器关闭才释放**。
- ServletContext和Cookie、Session的对比：
    - ![vlh6zg3](https://i.imgur.com/vlh6zg3.jpg)
    - **cookie**：是指存储在客户端上的信息，浏览器一旦关闭cookie就被清除。**每个用户都有独立的存储cookie的空间，互相独立**，只能自己访问自己的cookie
        - Cookie的生命周期：不设置的话默认就是一次会话，可以通过设置值来实现持久化
    - **session**：是指存储在服务器端的信息。每个用户都有独立的存储session的空间，互相独立，只能自己访问自己的session
        - 创建：第一次执行request.getSession()时创建；
        - 销毁：
          1. 服务器关闭的时候
          2. session过期/失效（默认30分钟就自动清除，从客户端不操作服务器端资源时开始计时），Tomcat在web.xml中配置
          3. 手动销毁,session.invalidate();
        - 作用范围：默认在一次会话中，任何资源共用一个session对象
    - **servletContext**：servletContext接口是Servlet中最大的一个接口，呈现了web应用的Servlet视图。ServletContext实例是通过 getServletContext()方法获得的，由于HttpServlet继承GenericServlet的关系，GenericServlet类和HttpServlet类同时具有该方法。所有用户都可以访问servletContext
        - 创建：服务器启动的时候会为每一个WEB应用创建一个单独的ServletContext对象
        - 销毁：服务器关闭的时候
- Servlet中的ServletContext是一个接口，其实现类如下：
    ![gJwS3rZ](https://i.imgur.com/gJwS3rZ.png)
- 
### 2.2 ServletContext作用1：作为域对象
- **域对象**：在一定范围内（当前应用），使多个Servlet共享数据。
    - **ServletContext内维护一个Map<String, Object>容器**，可以使用set/get/remove等方法操作
    - ![Y164Nzy](https://i.imgur.com/Y164Nzy.png)
```java
 // ServletContextDemo1向ServletContext写入
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    ServletContext applicationContext = this.getServletContext();
    applicationContext.setAttribute("name", "tom"); //在init时写入ServletContext
}
```
```java
// ServletContextDemo1从ServletContext读取
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    ServletContext applicationContext = this.getServletContext();
    String name = (String) applicationContext.getAttribute("name");
    System.out.println(name);
}
```

### 2.2 ServletContext作用2：获取配置信息
- 在XML中配置下面的全局信息：
```xml
<!--配置全局信息-->
<context-param>
    <param-name>encoding</param-name>
    <param-value>UTF-8</param-value>
</context-param>
```

### 2.3 ServletContext作用3：获取资源路径
- String  getRealPath(String path); //根据资源名称得到资源的绝对路径.
- ![63bV3a1](https://i.imgur.com/63bV3a1.png)
    - `String path = this.getServletContext().getRealPath("/WEB-INF/a.properties");`
    - `String path = this.getServletContext().getRealPath("/WEB-INF/classes/b.properties");`
    - `String path = this.getServletContext().getRealPath("/WEB-INF/classes/com/study/servlet/c.properties");`
        ```java
        Properties prop = new Properties();
        prop.load(new FileInputStream(path));
        System.out.println(prop.getProperty("key"));
        ```
### 2.4 ServletContext作用4：实现Servlet的请求转发
- ![d8bFiwF](https://i.imgur.com/d8bFiwF.png)
-  `RequestDispatcher  getRequestDispatcher(String path) ;` //参数表示要跳转到哪去
- ![GkNVAZ8](https://i.imgur.com/GkNVAZ8.png)
```java
@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 实现请求转发,将请求转发到com.study.servlet.ServletContextDemo4（/demo7）
        System.out.println("请求了ServletContextForward, 转发至ServletContextDemo4");
        ServletContext servletContext = this.getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/demo7");
        // 向/demo7传递req和resp
        requestDispatcher.forward(req, resp);
        System.out.println("转发完毕");
    }
```

## 3 使用注解配置Servlet
- 每创建一个Servlet我们就需要在web.xml中配置，但是如果我们的Servlet版本在3.0以上，就可以选择不创建web.xml，而使用注解来解决，十分简单方便
- 在Servlet类上使用 **@WebServlet("/Demo2")**,将该类的服务映射到/Demo2路径中
```xml
    <servlet>
        <servlet-name>servletContextForward</servlet-name>
        <servlet-class>com.study.servlet.ServletContextForward</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>servletContextForward</servlet-name>
        <url-pattern>/demo8</url-pattern>
    </servlet-mapping>
```
等同于：
```java
@WebServlet("/demo8")
public class ServletContextForward extends HttpServlet {...}
```
- WebServlet源码节选:
```java
  //WebServlet 源码节选
  @Target({ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface WebServlet {
      String name() default "";
      String[] value() default {};
      String[] urlPatterns() default {};
      int loadOnStartup() default -1;
```
    - 这个注解可以看到，@Target({ElementType.TYPE})作用范围为类上,@Retention(RetentionPolicy.RUNTIME)保留在运行期，name()方法反而在这里没有那么重要，因为在web.xml中，name主要起一个关联的作用，其中我们最重要的就是这个String[] urlPatterns() default {};配置一个地址，它的定义为一个数组，当然配置一个也是可以的，即urlPatterns = "/Demo2"而其中value所代表的最重要的值，其实也就代表这个地址，所以可以写为 Value = "/Demo2" ，而 Value又可以省略，所以可以写成 "/Demo2"

## 4 Servlet相关类的关系整理
- ![mChFg8I](https://i.imgur.com/mChFg8I.png)
- ![Wfong4x](https://i.imgur.com/Wfong4x.jpg)
- 

## 参考
- [ServletContext、request、Cookie、Session生命周期_Java_ruanwenjun_csdn的博客-CSDN博客](https://blog.csdn.net/ruanwenjun_csdn/article/details/78987566)
- [Servlet概述、实现、细节、获取资源、ServletConfig、ServletContext - 知乎](https://zhuanlan.zhihu.com/p/79679019)












