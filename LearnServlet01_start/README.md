# Servlet补充

## 1 Idea搭建Servlet程序
- 1、选择创建Web Application程序
- 2、在`web/WEB-INF`文件下创建classes和lib两个文件夹
    - **WEB-INF是Java的WEB应用的安全目录**。所谓安全就是客户端无法访问，只有服务端可以访问的目录。如果想在页面中直接访问其中的文件，必须通过web.xml文件对要访问的文件进行相应映射才能访问。
    - classes用来存放编译后输出的class文件
    - lib用于存放第三方jar包。
- 3、配置文件夹路径：
    - File -> Project Structure (快捷键：Ctrl + Shift + Alt + S) -> 选择Module    
    - 选择 Paths -> 选择”Use module compile output path” -> 将Output path和Test output path都选择刚刚创建的classes文件夹。
    - 接着选择Dependencies -> 将Module SDK选择为1.7 -> 点击右边的“+”号 -> 选择1 “Jars or Directories”-> 选择刚刚创建的lib文件夹
- 4、配置Tomcat容器
- 5、在Tomcat中部署并运行项目：Run -> Edit Configurations，进入”Run/Debug Configurations”窗口 -> 选择刚刚建立的Tomcat容器 -> 选择Deployment -> 点击右边的“+”号 -> 选择Artifact->选择web项目 -> Application context可以填“/hello”(其实也可以不填的~~) -> OK
- 6、编辑index.jsp文件：适当修改首页内容
- 7、启动tomcat

## 2 Servlet介绍
- servlet是运行在 Web 服务器中的小型Java程序（即：服务器端的小应用程序）。servlet 通常**通过 HTTP（超文本传输协议）接收和响应来自 Web 客户端的请求**。它是作为来自 Web 浏览器或其他 HTTP 客户端的请求和 HTTP 服务器上的数据库或应用程序之间的中间层。
![oxSbIp](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2021/03/oxSbIp.jpg)
- Servlet 执行以下主要任务：
    - 读取客户端（浏览器）发送的显式的数据。这包括网页上的 HTML 表单，或者也可以是来自 applet 或自定义的 HTTP 客户端程序的表单。
    - 读取客户端（浏览器）发送的隐式的 HTTP 请求数据。这包括 cookies、媒体类型和浏览器能理解的压缩格式等等。
    - 处理数据并生成结果。这个过程可能需要访问数据库，执行 RMI 或 CORBA 调用，调用 Web 服务，或者直接计算得出对应的响应。
    - 发送显式的数据（即文档）到客户端（浏览器）。该文档的格式可以是多种多样的，包括文本文件（HTML 或 XML）、二进制文件（GIF 图像）、Excel 等。
    - 发送隐式的 HTTP 响应到客户端（浏览器）。这包括告诉浏览器或其他客户端被返回的文档类型（例如 HTML），设置 cookies 和缓存参数，以及其他类似的任务。


### 2.1 编写一个servlet程序
- a、写一个java类，实现servlet接口，需要实现接口下面的五个方法【init(), destroy(), getServletInfo(), ServletConfig(), service(...)】.[可以把实现了servlet接口的类看作是一个servlet小程序]
    - 其实service()是服务方法，向模板页面发送数据
```java
/**
 * 实现javax.servlet.Servlet接口创建Servlet
 */
public class ServletDemo1 implements Servlet {

    // 手动覆盖Servlet生命周期的实例化方法
    public ServletDemo1() {
        System.out.println("ServletDemo1实例化");
    }

    // 初始化
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("ServletDemo1 init()");
    }

    // servlet可以使用这个方法得到任何启动信息
    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    // 服务方法
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("hello servlet");
    }

    // 允许servlet返回有关其自身的基本信息
    @Override
    public String getServletInfo() {
        return null;
    }

    // 销毁方法
    @Override
    public void destroy() {
        System.out.println("ServletDemo1 destroy()");
    }
}
```
    
- b、修改web.xml文件，给servlet提供一个可访问的URI地址
    ```xml
    <!--创建一个servlet实例-->
    <servlet>
        <servlet-name>servletDemo1</servlet-name>
        <servlet-class>com.study.servlet.ServletDemo1</servlet-class>
    </servlet>

    <!--给之前创建过的servlet实例提供一个可供客户端访问的url映射-->
    <servlet-mapping>
        <servlet-name>servletDemo1</servlet-name>
        <!--匹配模式 http:localhost:8080/myapp/demo1-->
        <url-pattern>/demo1</url-pattern>
    </servlet-mapping>
    ```
    - 映射细节1：支持配置多个映射路径到同一个servlet中：
        - ![SJmezkg](https://i.imgur.com/SJmezkg.png)
    - 映射细节2： 通配符*代表任意字符串
        - url-pattern: /*  【任意字符串都可以访问】
        - url-pattern：/action/* 【以/action开头的请求都可以访问】
        - url-pattern: *.do  【以*.do字符串的请求都可以访问 注：不要加/】
        - 匹配优先级：从高到低，绝对匹配-->  /开头匹配 --> 扩展名方式匹配

- c、部署应用到tomcat服务器
- d、通过浏览器访问测试  
    ![ftxaAP1](https://i.imgur.com/ftxaAP1.png)
- 解决：[IntelliJ IDEA 出现" java: 程序包javax.servlet不存在、 java: 程序包javax.servlet.annotation"等错误 - 缘琪梦 - 博客园](https://www.cnblogs.com/Yimi/p/11978315.html)

### 2.2 servlet执行过程
- ![8XXDGR3](https://i.imgur.com/8XXDGR3.png)

### 2.3 servlet的生命周期
- 四个阶段：实例化(new)-->初始化(init)-->服务(service)->销毁(destroy)
- 出生：（实例化-->初始化）**第一次访问Servlet就实例化了（默认情况下）**
- 活着：（服务）**应用(服务器不关闭，应用不卸载)活着，servlet就活着**
- 死亡：（销毁）应用卸载了servlet就销毁
- 生命周期方法是按以下顺序调用的：
    1. 构造servlet，然后使用init方法将其初始化。 
    2. 处理来自客户端的对service方法的所有调用。 
    3. 从服务中取出servlet，然后使用destroy方法销毁它，最后进行垃圾回收并终止它。
- 除了生命周期方法外，Servlet接口还提供下面两个方法：
    - getServletConfig()：servlet可以使用这个方法得到任何启动信息
    - getServletInfo()：允许servlet返回有关其自身的基本信息。比如作者、版本号、版权信息等
- **init()方法在开启tomcat，第一次通过url访问时被调用（仅一次）**
- **service()方法每次通过url访问时都会被调用**
- **destroy()在应用被卸载（tomcat首页中manager App中undeploy）或者关闭tomcat时被调用**

```
(启动tomcat)
...
(打开浏览器通过url访问servlet)
ServletDemo1实例化
ServletDemo1 init()
hello servlet
(手动停止Tomcat服务)
正在停止服务[Catalina]
...
ServletDemo1 destroy()
...
Disconnected from server
```

### 2.4 如何让servlet在开启服务器时就实例化
- 默认是在首次服务时实例化，如何配置使servlet在开启tomcat服务器时就初始化？
```xml
<servlet>
    <servlet-name>servletDemo1</servlet-name>
    <servlet-class>com.study.servlet.ServletDemo1</servlet-class>
    <!--设置开启服务器时就实例化，中间数字越小优先级越高-->
    <load-on-startup>2</load-on-startup>
</servlet>
```

1. `<load-on-startup>` 元素标记容器是否应该在web应用程序启动的时候就加载这个servlet，(实例化并调用其init()方法)。
2. 它的值必须是一个**整数**，表示servlet被加载的先后顺序。
3. 如果该元素的值为负数或者没有设置，则容器会当Servlet被请求时再加载。
4. 如果值为正整数或者0时，表示容器在应用启动时就加载并初始化这个servlet，值越小，servlet的优先级越高，就越先被加载。值相同时，容器就会自己选择顺序来加载。

### 2.5 代码实例
```java
public class ServletDemo1 implements Servlet {
    // 手动覆盖Servlet生命周期的实例化方法
    public ServletDemo1() {
        System.out.println("ServletDemo1实例化");
    }

    // 初始化
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("ServletDemo1 init()");
    }

    // servlet可以使用这个方法得到任何启动信息
    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    // 服务方法
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("hello servlet");
    }

    // 允许servlet返回有关其自身的基本信息
    @Override
    public String getServletInfo() {
        return null;
    }

    // 销毁方法
    @Override
    public void destroy() {
        System.out.println("ServletDemo1 destroy()");
    }
}

```

## 3 Servlet的三种创建方式
- Servlet(接口) --> GenericServlet(抽象类) --> HttpServlet(类) -->  我们的Servlet(继承HttpServlet)
- 可以直接在Idea中创建Servlet:![YK14YUg](https://i.imgur.com/YK14YUg.png)
### 3.1 实现javax.servlet.Servlet接口（参见：2.5）
- 参见2.5 ServletDemo1
### 3.2 继承javax.servet.GenericServlet类(适配器模式)
- GenericServlet类是所有Servlet类的祖先类
- 目前编写的Java Servlet类实现javax.servlet.Servlet接口。**但是Servlet接口中大部分情况下都是在编写service方法，其它方法可能很少用，每一次都把所有的方法全部实现，代码丑陋，可以使用适配器设计模式解决这个问题**。
- 提供一个**抽象类GenericServlet**【标准通用的Servlet】，该类作为Servlet接口的适配器，**以后编写Servlet类不再直接实现Servlet接口了，继承GenericServlet即可**。重点实现service方法。
- 参见com.study.servlet.ServletDemo2

####  3.2.1 GenericServlet类中提供的方法
![IiOHC7j](https://i.imgur.com/IiOHC7j.png)

#### 3.2.2 GenericServlet代码示例
```java
/**
 * 继承javax.servet.GenericServlet类创建Servlet
 */
public class ServletDemo2 extends GenericServlet {
    // 只需重写service方法
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("Hello ServletDemo2!");
    }
}
```

### 3.3 继承javax.servlet.http.HttpServlet类（模板方法设计模式）
- **实际开发中最常用的方法**
- 在模板模式（Template Pattern）中，一个抽象类公开定义了执行它的方法的方式/模板。它的子类可以按需要重写方法实现，但调用将以抽象类中定义的方式进行。这种类型的设计模式属于行为型模式。
- 在javax.servlet.http包中定义了采用HTTP通信协议的HttpServlet类.
- HttpServlet类继承自抽象类GenericServlet
- HTTP的请求方式包括DELETE,GET,OPTIONS,POST,PUT和TRACE, 在HttpServlet类中分别提供了相应的服务方法,它们是,doDelete(),doGet(),doOptions(),doPost(), doPut()和doTrace(). 
- 默认可以不重写任何方法，采用get请求，返回：HTTP Status 405，此URL不支持Http方法GET
- **建议不重写service()方法，而是使用模板中的doXXX类方法**。开发人员在编写HttpServlet时，通常应继承这个类，实现其中的模板方法, 而避免直接去实现Servlet接口的service()方法。
- HttpServlet类中的service方法仍然是最终调用模板自带的（或者我们在子类中实现的）doXXX类方法
    ![TlFmfty](https://i.imgur.com/TlFmfty.png)

> 由上述可知，如果我们在继承实现HttpServlet的时候重写了service()方法，相当于其中定义的所有doXXX类方法都作废了！因此我们要避免重写了service()方法！


#### 3.3.1 HttpServlet类中提供的方法
![fvJNoB4](https://i.imgur.com/fvJNoB4.png)

#### 3.3.2 HttpServlet代码示例
```java
/**
 * 继承javax.servlet.http.HttpServlet类（模板方法设计模式）
 */
public class ServletDemo3 extends HttpServlet {
    // 默认可以不重写任何方法，返回HTTP Status 405 – 方法不允许，但是Tomcat后台不报错


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ServletDemo3：hello doGet!");
        System.out.println(req.getRemoteAddr());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ServletDemo3：hello doPost!");
    }
}
```

## 4 Servlet的线程安全
- Servlet容器默认是采用**单实例多线程**(这是造成线程安全的主因)方式处理多个请求的
- **线程安全**：指多个线程在执行同一段代码的时候采用加锁机制，使每次的执行结果和单线程执行的结果都是一样的，不存在执行程序时出现意外结果。
- **线程不安全**：是指不提供加锁机制保护，有可能出现多个线程先后更改数据造成所得到的数据是脏数据。
- **有状态对象(Stateful Bean)，就是有实例变量的对象**，可以保存数据，是非线程安全的。在不同方法调用间不保留任何状态。
- **无状态对象(Stateless Bean)，就是没有实例变量的对象.** 不能保存数据，是不变类，是线程安全的。
- Servlet不是线程安全的。
    - 当Tomcat接收到Client的HTTP请求时，Tomcat从线程池中取出一个线程，之后找到该请求对应的Servlet对象并进行初始化，之后调用service()方法。要注意的是每一个Servlet对象再Tomcat容器中只有一个实例对象，**即是单例模式**。
    - ![w1CGQv1](https://i.imgur.com/w1CGQv1.jpg)
    - 上图中的Thread1和Thread2调用了同一个Servlet1，所以此时如果Servlet1中定义了实例变量或静态变量，那么可能会发生线程安全问题（因为所有的线程都可能使用这些变量）。
- 如何控制Servlet的线程安全性？
    - 避免使用实例变量
    - 避免使用非线程安全的集合
    - 在多个Servlet中对某个外部对象(例如文件)的修改是务必加锁（Synchronized，或者ReentrantLock），互斥访问。

### 4.1 实现SingleThreadModel接口
该接口指定了系统如何处理对同一个Servlet的调用。如果一个Servlet被这个接口指定，那么在这个Servlet中的service方法将不会有两个线程被同时执行，当然也就不存在线程安全的问题。**但是，如果一个Servlet实现了SingleThreadModel接口，Servlet引擎将为每个新的请求创建一个单独的Servlet实例，这将引起大量的系统开销**，在现在的Servlet开发中基本看不到SingleThreadModel的使用，这种方式了解即可，尽量避免使用。
```java
    public class MyServlet extends HttpServlet implements SingleThreadModel {
        
    }
```

### 4.2 同步对共享数据的操作
使用**synchronized**关键字能保证一次只有一个线程可以访问被保护的区段，可以通过同步块操作来保证Servlet的线程安全。**如果在程序中使用同步来保护要使用的共享的数据，也会使系统的性能大大下降**。这是因为被同步的代码块在同一时刻只能有一个线程执行它，使得其同时处理客户请求的吞吐量降低，而且很多客户处于阻塞状态。另外为保证主存内容和线程的工作内存中的数据的一致性，要频繁地刷新缓存,这也会大大地影响系统的性能。所以在实际的开发中也应避免或最小化Servlet 中的同步代码。
```java
    Public class MyServlet extends HttpServlet {
        synchronized (this){XXXX}
    }
```

### 4.3 避免使用实例变量
- 实例变量：定义在类中但在任何方法之外的变量。
- 线程安全问题很大部分是由实例变量造成的，只要在Servlet里面的任何方法里面都不使用实例变量（尽量使用局部变量），那么该Servlet就是线程安全的。
- **在Servlet中避免使用实例变量是保证Servlet线程安全的最佳选择。**
- Java 内存模型中，方法中的临时变量是在栈上分配空间，而且每个线程都有自己私有的栈空间，所以它们不会影响线程的安全。

> 总结：我们应该把Servlet设计成无状态（Stateless）的。

### 4.4 Servlet线程安全问题示例
- 先访问?message=helloA，1秒内再访问?message=helloB
    - ![87hxGzd](https://i.imgur.com/87hxGzd.png)
- 先访问?message=helloB，1秒内再访问?message=helloA
    - ![RaMcCyv](https://i.imgur.com/RaMcCyv.png)

```java
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    message = request.getParameter("message");
    System.out.println("修改实例变量message的值为" + message);
    PrintWriter printWriter = response.getWriter();
    try {
        System.out.println("Thread into sleeping");
        Thread.sleep(1000);
        System.out.println("Thread out of sleeping");
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    printWriter.write(message);
    System.out.println("读取实例变量message的值为" + message);
}
```

## 5 一个典型的servlet在Tomcat上运行的生命周期

  1. Tomcat receives a request from a client through one of its connectors.
  2. Tomcat maps this request to the appropriate Engine for processing.  These Engines are contained within other elements, such as Hosts and Servers, which limit the scope of Tomcat's search for the correct Engine.
  3. Once the request has been mapped to the appropriate servlet, Tomcat checks to see if that servlet class has been loaded.  If it has not, Tomcat compiles the servlet into Java bytecode, which is executable by the JVM, and creates an instance of the servlet.
  4. Tomcat initializes the servlet by calling its **init** method.  The servlet includes code that is able to read Tomcat configuration files and act accordingly, as well as declare any resources it might need, so that Tomcat can create them in an orderly, managed fashion.
  5. Once the servlet has been initialized, Tomcat can call the servlet's **service** method to process the request, which will be returned as a response.
  6. During the servlet's lifecycle, Tomcat and the servlet can communicate through the use of **listener classes**, which monitor the servlet for a variety of state changes.  Tomcat can retrieve and store these state changes in a variety of ways, and allow other servlets access to them, allowing state to be maintained and accessed by various components of a given context across the span of a single or multiple user sessions.  An example of this functionality in action is an e-commerce application that remembers what the user has added to their cart and is able to pass this data to a checkout process.
  7. Tomcat calls the servlet's **destroy** method to smoothly remove the servlet.  This action is triggered either by a state change that is being listened for, or by an external command delivered to Tomcat to undeploy the servlet's Context or shut down the server.

## 参考资料

- [Java Web 学习与总结（一）Servlet基础 - 真是啰嗦 - 博客园](https://www.cnblogs.com/qq965921539/p/10161340.html)
- [Servlet的多线程和线程安全 - 邴越 - 博客园](https://www.cnblogs.com/binyue/p/4513577.html)
- [深入理解Servlet线程安全问题_Java_LCore的专栏-CSDN博客](https://blog.csdn.net/lcore/article/details/8974590)

- [An introduction to Tomcat servlet interactions | MuleSoft](https://www.mulesoft.com/tcat/tomcat-servlet)

























