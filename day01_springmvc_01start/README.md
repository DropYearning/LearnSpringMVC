# LearnSpringMVC-day1-三层架构和 SpringMVC
[SpringMVC教程IDEA版哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7qa?from=search&seid=12129021327363557187)



## 1 三层架构

我们的开发架构一般都是基于两种形式，一种是 **C/S 架构，也就是客户端/服务器**，另一种是 **B/S 架构，也就 是浏览器服务器**。**在 JavaEE 开发中，几乎全都是基于 B/S 架构的开发**。那么在 B/S 架构中，系统标准的三层架构 包括：表现层、业务层、持久层。三层架构在我们的实际开发中使用的非常多，所以我们课程中的案例也都是基于 三层架构设计的。

- ![sG6IZaZ](https://i.imgur.com/sG6IZaZ.png)
- **表现层：也就是我们常说的web层**。**它负责接收客户端请求，向客户端响应结果**，通常客户端使用http协议请求 web 层，web 需要接收 http 请求，完成 http 响应。
    - 表现层包括展示层和控制层：控制层负责接收请求，展示层负责结果的展示。
    - 表现层依赖业务层，接收到客户端请求一般会调用业务层进行业务处理，并将处理结果响应给客户端。
    - 表现层的设计一般都使用 MVC 模型。（MVC 是表现层的设计模型，和其他层没有关系）
- **业务层：也就是我们常说的 service 层。它负责业务逻辑处理**，和我们开发项目的需求息息相关。web 层依赖业务层，但是业务层不依赖 web 层。业务层在业务处理时可能会依赖持久层，如果要对数据持久化需要保证事务一致性。（也就是我们说的， 事务应该放到业务层来控制）
- **持久层：也就是我们是常说的 dao 层。负责数据持久化，包括数据层即数据库和数据访问层**，数据库是对数据进行持久化的载体，数据访问层是业务层和持久层交互的接口，业务层需要通过数据访问层将数据持久化到数据库 中。通俗的讲，持久层就是和数据库交互，对数据库表进行增删改查的.

## 2 MVC模型

**MVC 全名是 Model View Controller，是模型(model)－视图(view)－控制器(controller)的缩写**， 是一种用于设计创建 Web 应用程序表现层的模式。MVC 中每个部分各司其职：

- Model（模型）：通常指的就是我们的数据模型。作用一般情况下用于封装数据。**Java Bean.**
- View（视图）：通常指的就是我们的 jsp 或者 html。作用一般就是展示数据的。通常视图是依据模型数据创建的。**JSP**
- Controller（控制器）：是应用程序中处理用户交互的部分。作用一般就是处理程序逻辑的。**Servlet**

## 3 Spring MVC

![L0AP2ig](https://i.imgur.com/L0AP2ig.png)

**SpringMVC 是一种基于 Java 的实现 MVC 设计模型的请求驱动类型的轻量级 Web 框架，属于 Spring FrameWork 的后续产品，已经融合在 Spring Web Flow 里面**。

SpringMVC 已经成为目前最主流的 MVC 框架之一，并且随着 Spring3.0 的发布，全面超越 Struts2，成为最优秀的 MVC 框架。

它通过一套注解，让一个简单的 Java 类成为处理请求的控制器，而无须实现任何接口。同时它还支持 RESTful 编程风格的请求。

- **SpringMVC 的优势**:
    - 清晰的角色划分：前端控制器（DispatcherServlet） 请求到处理器映射（HandlerMapping） 处理器适配器（HandlerAdapter） 视图解析器（ViewResolver） 处理器或页面控制器（Controller） 验证器（ Validator） 命令对象（Command 请求参数绑定到的对象就叫命令对象） 表单对象（Form Object 提供给表单展示和提交到的对象就叫表单对象）。[模块化开发, 各个控制器形成类似流水线]
    - 分工明确，而且扩展点相当灵活，可以很容易扩展，虽然几乎不需要。
    - 和 Spring 其他框架无缝集成，是其它 Web 框架所不具备的。
    - ...
- **SpringMVC 和 Struts2 的对比分析**:
    - 共同点：
        -  它们都是*表现层框架，都是基于 MVC 模型编写*的。
        -  它们的*底层都离不开原始 ServletAPI*。 
        - 它们*处理请求的机制都是一个核心控制器*(SpringMVC的核心控制器是Servle, struts2的核心控制器是Filter)
    - 区别：
        - 入口不一样:Spring MVC 的入口是 Servlet, 而 Struts2 是 Filter 
        - *Spring MVC 是基于方法设计的，而 Struts2 是基于类*，Struts2 每次执行都会创建一个动作类。所 以 Spring MVC 会稍微比 Struts2 快些。
        - Spring MVC 使用更加简洁,同时还支持 JSR303, 处理 ajax 的请求更方便
        - Struts2 的 OGNL 表达式使页面的开发效率相比 Spring MVC 更高些，但执行效率并没有比 JSTL 提 升，尤其是 struts2 的表单标签，远没有 html 执行效率高。

## 4 SpringMVC入门案例

- 入门案例需求: ![9DHKPT1](https://i.imgur.com/9DHKPT1.png)
### 4.1 入门单例搭建步骤

- 1 导入POM依赖: 参见pom.xml
- 2 在web.xml中加入前置控制器DispatcherServlet, 并配置其在Tomcat启动时实例化，并且载入springmvc.xml配置文件：
    ```xml
      <!-- SpringMVC的前端控制器，实际上就是一个Servlet -->
      <servlet>
        <servlet-name>dispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
          <!--配置加载springmvc.xml配置文件-->
          <param-name>contextConfigLocation</param-name>
          <param-value>classpath:springmvc.xml</param-value>
        </init-param>
        <!--配置一启动服务器就创建dispatcherServlet，并且加载springmvc.xml-->
        <load-on-startup>1</load-on-startup>
      </servlet>
      <servlet-mapping>
        <servlet-name>dispatcherServlet</servlet-name>
        <url-pattern>/</url-pattern>
      </servlet-mapping>
    ```
- 3 添加SpringMVC的配置文件：在resource目录下创建springmvc.xml，加入下面的XML头。注意：1) 配置spring创建容器时要扫描注解的包; 2)配置视图解析器；3)配置spring开启注解mvc的支持
    ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:mvc="http://www.springframework.org/schema/mvc"
      xmlns:context="http://www.springframework.org/schema/context"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation=" http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
          http://www.springframework.org/schema/mvc
          http://www.springframework.org/schema/mvc/spring-mvc.xsd
          http://www.springframework.org/schema/context
          http://www.springframework.org/schema/context/spring-context.xsd">
      <!--开启注解包扫描-->
      <context:component-scan base-package="com.study"></context:component-scan>
  
      <!--配置视图的解析器, 负责跳转指定的页面-->
      <bean id="internalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver" >
          <!--配置前缀路径和后缀名-->
          <property name="prefix" value="/WEB-INF/pages/"></property>
          <property name="suffix" value=".jsp"></property>
      </bean>
  
      <!--开启SpringMVC框架的注解支持-->
      <mvc:annotation-driven/>
  
  </beans>
    ```
- 4 编写index.jsp，其中有一个超链接指向处理请求的结果页面
    ```jsp
    <body>
        <h3>入门程序</h3>
        <%--这里如果直接写hello默认会访问http:localhost:8080/hello--%>
        <a href="hello">入门程序链接</a>
    </body>
    ```
- 5 编写HelloController控制器类
    ```java
    @Controller
    public class HelloController {
        // 配置请求的映射
        @RequestMapping(path="/hello")
        public String sayHello(){
            System.out.println("Hello SpringMVC！");
            return "success"; // 默认指明了返回到的jsp文件名字（返回到success.jsp）
        }
    }
    ```
- 6 在WEB-INF目录下创建pages文件夹，编写success.jsp的成功页面
    ```jsp
    <body>
        <h3>入门案例成功</h3>
    </body>
    ```
## 5  SpringMVC执行流程原理
- ![U8hgcLx](https://i.imgur.com/U8hgcLx.png)
- ![38b3Oxo](https://i.imgur.com/38b3Oxo.png)
- ![uCbhFpC](https://i.imgur.com/uCbhFpC.png)
- ![mJJ74i1](https://i.imgur.com/mJJ74i1.png)
- SpringMVC底层是基于组件(xx器)的方式执行流程的
- 流程梳理：
    - 1、服务器启动，应用被加载。读取到web.xml中的配置springmvc.xml创建 spring容器并且初始化容器中的对象。
    - 2、浏览器发送请求，被 DispatcherServlet 捕获，该 Servlet 并不处理请求，而是把请求转发出去。转发的路径是根据请求 URL，匹配@RequestMapping 中的内容。
    - 3、匹配到了后，执行对应方法。该方法有一个返回值。
    - 4、根据方法的返回值，借助 InternalResourceViewResolver (视图的解析器)找到对应的结果视图。
    - 5、渲染结果视图，响应浏览器
    
## 6  入门案例中涉及的组件
用户请求到达**前端控制器，它就相当于mvc模式中的c，dispatcherServlet 是整个流程控制的中心**，由它调用其它组件处理用户的请求，dispatcherServlet的存在降低了组件之间的耦合性。
### 6.1 HandlerMapping：处理器映射器
- HandlerMapping 负责根据用户请求找到Handler（Controller）即处理器，SpringMVC 提供了不同的映射器实现不同的映射方式，例如：配置文件方式，实现接口方式，注解方式等。

### 6.2 Handler：处理器
- 它就是我们开发中要编写的具体业务控制器。由 DispatcherServlet 把用户请求转发到Handler。由Handler对具体的用户请求进行处理。
- 在本例中，即为HelloController

### 6.3 HandlerAdapter：处理器适配器
- 通过HandlerAdapter对处理器进行执行，这是适配器模式的应用，通过扩展适配器可以对更多类型的处理器进行执行。

### 6.4 View Resolver：视图解析器
- View Resolver 负责将处理结果生成 View 视图，View Resolver 首先根据逻辑视图名解析成物理视图名,即具体的页面地址，再生成 View 视图对象，最后对 View 进行渲染将处理结果通过页面展示给用户。

### 6.5 View：视图
- SpringMVC 框架提供了很多的 View 视图类型的支持，包括：jstlView、freemarkerView、pdfView等。我们最常用的视图就是 jsp。
- 一般情况下需要通过页面标签或页面模版技术将模型数据通过页面展示给用户，需要由程序员根据业务需求开发具体的页面

### 6.6 <mvc:annotation-driven>说明
- 在 SpringMVC 的各个组件中，**处理器映射器、处理器适配器、视图解析器称为SpringMVC的三大组件**。
- 使用 <mvc:annotation-driven> 自动加载 RequestMappingHandlerMapping（处理映射器）和RequestMappingHandlerAdapter（ 处 理 适 配 器 ） 
- **可用在 SpringMVC.xml 配置文件中使用<mvc:annotation-driven>替代注解处理器和适配器的配置**。
- 它就相当于在 xml 中配置了：
```xml
<!-- 上面的标签相当于 如下配置-->
<!-- Begin -->
<!-- HandlerMapping -->
<bean
class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerM
apping"></bean>
<bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"></bean>
<!-- HandlerAdapter -->
<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"></bean>
<bean class="org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter"></bean>
<bean
class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"></bean>
<!-- HadnlerExceptionResolvers -->
<bean
class="org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver"></bean>
<bean
class="org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver"></bean>
<bean
class="org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver"
></bean>
<!-- End -->
```

## 7 @RequestMapping注解
- 作用：用于建立请求 URL 和处理请求方法之间的对应关系。 
- 出现位置：
    - 类上：设置请求 URL 的第一级访问目录。此处不写的话，就相当于应用的根目录。写的话需要以/开头。它出现的目的是为了使我们的URL可以按照模块化管理
    - 方法上：请求 URL 的第二级访问目录。
- 属性：
    - value：用于指定请求的 URL。它和 path 属性的作用是一样的。
    - method：用于指定请求的方式。
    - params：用于指定限制请求参数的条件。它支持简单的表达式。要求请求参数的key和value必须和配置的一模一样
        - 例如：params = {"accountName"}，表示请求参数必须有 accountName；params = {"moeny!100"}，表示请求参数中 money 不能是 100;params = {"moeny=100"}，表示请求参数中 money 只能是 100

    — headers：用于指定限制请求消息头的条件，请求头中必须含有某些字段

## 8 请求参数的绑定
- 基本类型参数：包括基本类型和 String 类型
    - 要求我们的参数名称必须和控制器中方法的形参名称保持一致。(严格区分大小写)
- POJO(JavaBeans)类型参数：包括实体类，以及关联的实体类
    - 要求表单中参数名称和POJO类的属性名称保持一致。并且控制器方法的参数类型是POJO类型。
- 数组和集合类型参数：包括 List 结构和 Map 结构的集合（包括数组）
    - 如果是集合类型,有两种方式：
        - 第一种：要求集合类型的请求参数必须在 POJO 中。在表单中请求参数名称要和 POJO 中集合属性名称相同。给 List 集合中的元素赋值，使用下标。给 Map 集合中的元素赋值，使用键值对。
        - 第二种：**接收的请求参数是 json 格式数据。需要借助一个注解实现。**
- SpringMVC会自动进行一些数据的类型转换。对于不常用的数据类型转换需要自己指定。


### 8.1 在处理请求的方法中增加参数
- 直接在Controller类中处理映射的方法的参数中增加参数即可：
    ![XBD1BiZ](https://i.imgur.com/XBD1BiZ.png)

### 8.2 请求参数与实体类封装
- 创建封装请求参数的javabean, ** 并实现序列化的接口 **，具有get/set方法
- 直接在Controller类中处理映射的方法的参数中增加该类型的参数即可：
    ```java
    // 保存用户，把数据封装到javabean的实例中
    @RequestMapping("/saveaccount")
    public String testParam(Account account){
        System.out.println("saveaccount执行了");
        System.out.println(account);
        return "success";
    }
    ```
### 8.3 复杂情况：请求参数绑定别的类的引用
- 例如Account类中包含成员类型引用User：直接在表单中使用`user.age`即可
    ```xml
    <div>表单传递参数，封装到JavaBean</div>
    <form action="/start/proj2/saveaccount" method="post" >
        <%--属性名称要与实体类的变量名一致--%>
        账号：<input type="text" name="username"/><br/>
        密码：<input type="text" name="password"/><br/>
        金额：<input type="text" name="money"/><br/>
        用户姓名：<input type="text" name="user.uname"/><br/>
        用户年龄：<input type="text" name="user.age"/><br/>
        <input type="submit" value="提交"/><br/>
    </form>
    ```
    
    ```java
    // 保存用户，把数据封装到javabean的实例中
    @RequestMapping("/saveaccount")
    public String testParam(Account account){
        System.out.println("saveaccount执行了");
        System.out.println(account);
        System.out.println(account.getUser());
        return "success";
    }
    ```
### 8.4 解决中文乱码问题
- post请求中文乱码（Tomcat版本造成的问题）
- 可以在web.xml配置使用SpringMVC的过滤器：
    ```xml
         <!--配置解决中文乱码的过滤器-->
         <filter>
           <filter-name>characterEncodingFilter</filter-name>
           <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
           <!--过滤器的初始化参数-->
           <init-param>
             <param-name>encoding</param-name>
             <param-value>UTF-8</param-value>
           </init-param>
         </filter>
         <filter-mapping>
           <filter-name>characterEncodingFilter</filter-name>
           <url-pattern>/*</url-pattern>
         </filter-mapping>
   ```

### 8.5 请求参数绑定集合类型
- JavaBean:
    ```java
    public class Account implements Serializable {
        private String username;
        private String password;
        private Double money;
    
        // 集合的封装
        private List<User> list;
        private Map<String, User> map;
        ...
    }
    ```
- index.jsp:
    ```xml
    <div>表单传递参数，含有复杂类型（集合、Map）</div>
    <form action="/start/proj2/saveaccount" method="post" >
        <%--属性名称要与实体类的变量名一致--%>
        账号：<input type="text" name="username"/><br/>
        密码：<input type="text" name="password"/><br/>
        金额：<input type="text" name="money"/><br/>
            <%--封装到Account.list[0]位置--%>
        list用户姓名：<input type="text" name="list[0].uname"/><br/>
        list用户年龄：<input type="text" name="list[0].age"/><br/>
            <%--封装到Account.map['one']位置--%>
        map用户姓名：<input type="text" name="map['one'].uname"/><br/>
        map用户年龄：<input type="text" name="map['one'].age"/><br/>
        <input type="submit" value="提交"/><br/>
    </form>
    ```

### 8.6 自定义类型转换器
![uN8OTK](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2021/03/uN8OTK.png)
- 第一步：定义一个类，实现 Converter 接口，该接口有两个泛型。`public interface Converter<S, T> `: S表示Source, T表示Target
    - 实际开发中Spring中已有很多个类实现了Converter 接口，一般情况下我们直接在其中选择使用即可
    - ![wYzpEZ4](https://i.imgur.com/wYzpEZ4.png)
    - public interface Converter<S, T> : S表示Source, T表示Target
        ```java
        /**
         * 把字符串转换为日期
         */
        public class StringToDateConverter implements Converter<String, Date> {
        
            @Override
            public Date convert(String source) {
                if (source == null){
                    throw new RuntimeException("没有传入日期字符串");
                }
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                try {
                     return df.parse(source);
                } catch (ParseException e) {
                    throw new RuntimeException("输入的日期不是yyyy-MM-dd格式的");
                }
            }
        }
        ```
- 第二步：在 spring 配置文件springmvc.xml中配置类型转换器。创建一个`SpringFramework的ConversionServiceFactoryBean`实例交由Spring容器管理，并将自定义的类型转换器注入其中。
![6BWcwO](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2021/03/6BWcwO.png)

    ```xml
        <!--配置类型转换器工厂-->
        <bean id="conversionServiceFactoryBean" class="org.springframework.context.support.ConversionServiceFactoryBean">
            <!--给工厂注入一个新的类型转换器 -->
            <property name="converters" >
                <set>
                    <!-- 配置自定义类型转换器 -->
                    <bean class="com.study.utils.StringToDateConverter"></bean>
                </set>
            </property>
        </bean>
    ```
- 第三步：在 annotation-driven 标签中引用配置的类型转换服务 
    ```xml
    <!--开启SpringMVC框架的注解支持, -->
    <mvc:annotation-driven conversion-service="conversionServiceFactoryBean"/>
    ```
- 最后，运行部署生效


## 9 获取原生的Servlet API
SpringMVC 还支持使用原始 ServletAPI 对象作为传入控制器方法的参数。
- 直接在处理请求的方法参数中加入需要的HttpServletRequest和HttpServletResponse对象参数即可
    ```java
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
    ```









