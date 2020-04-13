# Servlet - 监听器
## 1 servlet三大作用域：request，session，application
### 1.1 request域
- request是表示一个请求，只要发出一个请求就会创建一个request，它的作用域：仅在当前请求中有效。
- 用处：常用于服务器间同一请求不同页面之间的参数传递，常应用于表单的控件值传递。
- 方法：request.setAttribute(); request.getAttribute(); request.removeAttribute(); request.getParameter().


### 1.2 session域
- 服务器可以为每个用户浏览器创建一个会话对象（session对象），所以session中的数据可供当前会话中所有servlet共享。
- 会话：用户打开浏览器会话开始，直到关闭浏览器会话才会结束。一次会话期间只会创建一个session对象。 
    - 一次会话中可以有多次请求
- 用处：常用于web开发中的登陆验证界面（当用户登录成功后浏览器分配其一个session键值对）。
- 方法：session.setAttribute(); session.getAttribute(); session.removeAttribute();
- session是服务器端对象，保存在服务器端。并且服务器可以将创建session后产生的sessionid通过一个cookie返回给客户端，以便下次验证。（session底层依赖于cookie）

### 1.3 application（ServletContext）域
- 作用范围：所有的用户都可以取得此信息，**此信息在整个服务器上被保留**。
- Application属性范围值，只要设置一次，则所有的网页窗口都可以取得数据。
- **ServletContext在服务器启动时创建，在服务器关闭时销毁，一个JavaWeb应用只创建一个ServletContext**对象，所有的客户端在访问服务器时都共享同一个ServletContext对象;ServletContext对象一般用于在多个客户端间共享数据时使用;


## 2 监听器的概念
- ![KeG7NoA](https://i.imgur.com/KeG7NoA.jpg)
    - 在 Servlet技术中我们学习了 request、 session、application作用域对象,其主要作用是实现数据的在不同场景中的灵活流转。但是数据的具体流转过程我们是看不到的,比如作用域对象是什么时候创建和销毁的,数据是什么时候存取,改变和删除的。因为具体的流转过程看不到,所以也就**无法在指定的时机对数据和对象进行操作,比如session创建时候人数+1，session销毁的时候,在线人数-1。**
    - ![xpmmeV1](https://i.imgur.com/xpmmeV1.jpg)
- Servlet监听器是Servlet规范中定义的一种特殊类，**用于监听ServletContext、HttpSession和ServletRequest等域对象的创建与销毁事件，以及监听这些域对象中属性发生修改**的事件。
- 监听对象：
  - 1、ServletContext：application，整个应用只存在一个
  - 2、HttpSession：session，针对每一个对话
  - 3、ServletRequest：request，针对每一个客户请求
- 监听内容：**创建、销毁、属性改变**事件
- **监听作用**：可以在事件发生前、发生后进行一些处理，一般可以用来统计在线人数和在线用户、统计网站访问量、系统启动时初始化信息等。

## 3 监听器的使用
- 1、实现自定义监听器类
- 2、在web.xml中配置监听器
    ```xml
        <!--配置监听器-->
        <listener>
            <!--配置监听器不需要url,因为监听器监听所有域对象的生命周期变化-->
            <listener-class>com.study.listener.MyListener</listener-class>
        </listener>
    ```
- 3、启动执行监听器。

### 3.1 使用监听器监听Request
- 实现**ServletRequestListener接口（可以监听request的创建和销毁）**，重写requestDestroyed()和requestInitialized()方法
- 实现**ServletRequestAttributeListener接口（可以监听request域中属性的增删改）**，重写attributeAdded()，attributeRemoved()，attributeReplaced()
- `ServletRequestAttributeEvent`对象可以帮助获取**Request域中本次Servlet service()操作修改了的数据的名称和值**

```java
/**
 * request监听器类:可以监听request的创建和销毁、request域中属性的增删改、
 */
public class MyListener implements ServletRequestListener, ServletRequestAttributeListener {

    // request被销毁时执行
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        System.out.println("MyListener requestDestroyed");
    }

    // request被创建时执行
    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        System.out.println("MyListener requestInitialized");
    }

    @Override
    public void attributeAdded(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        // ServletRequestAttributeEvent对象可以帮助获取域中属性
        System.out.println("MyListener Request域中增加了一条数据：" +servletRequestAttributeEvent.getName() + ":" + servletRequestAttributeEvent.getValue());
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        System.out.println("MyListener Request域中移除了一条数据："  +servletRequestAttributeEvent.getName() + ":" + servletRequestAttributeEvent.getValue());
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        System.out.println("MyListener Request域中替换了一条数据：" +servletRequestAttributeEvent.getName() + ":" + servletRequestAttributeEvent.getValue());
    }
}
```

### 3.2 使用监听器监听Session
- 实现HttpSessionListener接口，重写sessionCreated和sessionDestroyed，实现对session创建和销毁的监听。
- 实现HttpSessionAttributeListener,重写attribute的增删改监听方法。
- `HttpSessionEvent`对象可以帮助获取**Session域中本次Servlet service()操作修改了的数据的名称和值**
    ```java
    /**
     * session监听器类:可以监听session的创建和销毁、session域中属性的增删改、
     */
    public class MyListener2 implements HttpSessionListener, HttpSessionAttributeListener {
    
        @Override
        public void sessionCreated(HttpSessionEvent httpSessionEvent) {
            System.out.println("MyListener2 sessionCreated");
        }
    
        @Override
        public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
            System.out.println("MyListener2 sessionDestroyed");
        }
    
    
        @Override
        public void attributeAdded(HttpSessionBindingEvent event) {
            System.out.println("session中增加了一条数据" + event.getName() + ":" + event.getValue());
        }
    
        @Override
        public void attributeRemoved(HttpSessionBindingEvent event) {
            System.out.println("session中删除了一条数据" + event.getName() + ":" + event.getValue());
        }
    
        @Override
        public void attributeReplaced(HttpSessionBindingEvent event) {
            System.out.println("session中修改了一条数据" + event.getName() + ":" + event.getValue());
        }
    }
    ```

### 3.3 使用监听器监听Application
- 实现ServletContextListener接口，重写contextInitialized和contextDestroyed方法
- 实现ServletRequestAttributeListener接口，重写attribute的增删改方法
```java
/**
 * application监听器类
 */
public class MyListener3 implements ServletContextListener, ServletRequestAttributeListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("MyListener3 contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("MyListener3 contextDestroyed");
    }

    @Override
    public void attributeAdded(ServletRequestAttributeEvent event) {
        System.out.println("session中增加了一条数据" + event.getName() + ":" + event.getValue());
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent event) {
        System.out.println("session中删除了一条数据" + event.getName() + ":" + event.getValue());
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent event) {
        System.out.println("session中更新了一条数据" + event.getName() + ":" + event.getValue());
    }
}
```

- 结果：
```
MyListener3 contextInitialized
MyFilter2 init
MyFilter1 init
[2020-04-13 06:46:45,647] Artifact LearnServlet03_filter:war exploded: Artifact is deployed successfully
[2020-04-13 06:46:45,647] Artifact LearnServlet03_filter:war exploded: Deploy took 596 milliseconds
ServletDemo1 init
MyFilter1 doFilter 执行了
ServletDemo1 service
MyFilter1 doFilter 执行结束
/Users/brightzh/opt/apache-tomcat-8.5.53/bin/catalina.sh stop
NOTE: Picked up JDK_JAVA_OPTIONS:  --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED
13-Apr-2020 18:46:52.742 信息 [main] org.apache.catalina.core.StandardServer.await A valid shutdown command was received via the shutdown port. Stopping the Server instance.
13-Apr-2020 18:46:52.742 信息 [main] org.apache.coyote.AbstractProtocol.pause Pausing ProtocolHandler ["http-nio-8080"]
13-Apr-2020 18:46:52.756 信息 [main] org.apache.catalina.core.StandardService.stopInternal 正在停止服务[Catalina]
13-Apr-2020 18:46:52.774 信息 [main] org.apache.coyote.AbstractProtocol.stop 正在停止ProtocolHandler ["http-nio-8080"]
ServletDemo1 destroy
MyFilter2 destroy
MyFilter1 destroy
MyListener3 contextDestroyed
13-Apr-2020 18:46:52.777 信息 [main] org.apache.coyote.AbstractProtocol.destroy 正在摧毁协议处理器 ["http-nio-8080"]
Disconnected from server
```
## 4 基于监听器实现网站在线人数的统计
- 在ApplicationContext设置count属性保存当前在线人数

## 5 Servlet监听器类族与方法一览
- ![WAgzZA1](https://i.imgur.com/WAgzZA1.png)
- Servlet API提供以下事件对象：
  1. javax.servlet.AsyncEvent - 在ServletRequest（通过调用ServletRequest#startAsync或ServletRequest#startAsync(ServletRequest,ServletResponse)）启动的异步操作已完成，超时或产生错误时触发的事件。
  2. javax.servlet.http.HttpSessionBindingEvent - 将此类型的事件发送到实现HttpSessionBindingListener的对象，当该对象从会话绑定或解除绑定时，或者发送到在web.xml中配置的HttpSessionAttributeListener，当绑定任何属性时，在会话中取消绑定或替换。会话通过对HttpSession.setAttribute的调用来绑定对象，并通过调用HttpSession.removeAttribute解除对象的绑定。当对象从会话中删除时，我们可以使用此事件进行清理活动。
  3. javax.servlet.http.HttpSessionEvent - 这是表示Web应用程序中会话更改的事件通知的类。
  4. javax.servlet.ServletContextAttributeEvent - 关于对Web应用程序的ServletContext的属性进行更改的通知的事件类。
  5. javax.servlet.ServletContextEvent - 这是关于Web应用程序的servlet上下文更改的通知的事件类。
  6. javax.servlet.ServletRequestEvent - 此类事件表示ServletRequest的生命周期事件。事件的源代码是这个Web应用程序的ServletContext。
  7. javax.servlet.ServletRequestAttributeEvent - 这是事件类，用于对应用程序中servlet请求的属性进行更改的通知。
- Servlet API提供了以下监听器接口：
  1. javax.servlet.AsyncListener - 如果在添加了侦听器的ServletRequest上启动的异步操作已完成，超时或导致错误，将会通知侦听器。
  2. javax.servlet.ServletContextListener - 用于接收关于ServletContext生命周期更改的通知事件的接口。
  3. javax.servlet.ServletContextAttributeListener - 接收关于ServletContext属性更改的通知事件的接口。
  4. javax.servlet.ServletRequestListener - 用于接收关于进入和超出Web应用程序范围的请求的通知事件的接口。
  5. javax.servlet.ServletRequestAttributeListener - 接收关于ServletRequest属性更改的通知事件的接口。
  6. javax.servlet.http.HttpSessionListener - 接收关于HttpSession生命周期更改的通知事件的接口。
  7. javax.servlet.http.HttpSessionBindingListener - 使对象从会话绑定到绑定或从其绑定时被通知。
  8. javax.servlet.http.HttpSessionAttributeListener - 用于接收关于HttpSession属性更改的通知事件的接口。
  9. javax.servlet.http.HttpSessionActivationListener - 绑定到会话的对象可能会侦听容器事件，通知他们会话将被钝化，该会话将被激活。需要在VM或持久化会话之间迁移会话的容器来通知绑定到实现HttpSessionActivationListener的会话的所有属性。

## 参考
- [servlet三大作用域：request，session，application - ！！天道酬勤 - 博客园](https://www.cnblogs.com/oycq9999/p/10272827.html)
- [Servlet监听器（Listener）实例 - EasonJim - 博客园](https://www.cnblogs.com/easonjim/p/7100750.html)
- [java基础篇---Servlet监听器 - 偶my耶 - 博客园](https://www.cnblogs.com/oumyye/p/4276028.html)