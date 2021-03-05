# LearnSpringMVC-day3-SSM框架整合例子
[SpringMVC教程IDEA版哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7qa?from=search&seid=12129021327363557187)
整合思路：使用Spring Framework去整合其他框架。先保证其他框架能够正常工作，再考虑整合。
- ![f37O9ra](https://i.imgur.com/f37O9ra.png)
## 配置文件梳理
- `/WEB-INF/web.xml`: Java Web项目中的web.xml文件是用来初始化工程配置信息的。我们主要**用它来配置Servlet、过滤器、监听器**
    - 由于SpringMVC中的前端控制器也是一个Servlet，因此一旦用到也需要在这个文件中配置
    - 前端控制器：实现了mvc设计模式的web框架，首先用户发出请求，请求到达SpringMVC的前端控制器（DispatcherServlet）,前端控制器根据用户的url请求处理器映射器查找匹配该url的handler，并返回一个执行链，前端控制器再请求处理器适配器调用相应的handler进行处理并返回给前端控制器一个modelAndView，前端控制器再请求视图解析器对返回的逻辑视图进行解析，最后前端控制器将返回的视图进行渲染并把数据装入到request域，返回给用户。
    - ![bsBvvft](https://i.imgur.com/bsBvvft.jpg)
- `/resources/springmvc.xml`：SpringMVC框架的配置文件，我们主要用**它来对SpringMVC进行配置，包括开启对带有@Contoller注解的包的扫描、配置视图解析器、配置所有<mvc:xxx>形式的项目**
- `/resrouces/applicationContext.xml`：Spring的核心配置文件，用于**配置IoC容器，以及设置扫描注解的包**。
- `SqlMapConfig.xml`：MyBatis的全局配置文件，可以在其中设置好MySQL的连接参数、Mybatis要扫描注解的包、事务

> springmvc.xml和applicationContext.xml这两个配置文件都需要在`/WEB-INF/web.xml`中引入。但是Mybatis的配置文件`SqlMapConfig.xml`是独立的，我们只需要在其中引入Mybatis的DOCTYPE，Mybatis就会自动识别并使用该配置文。但是如果我们希望Spring框架与Mybatis整合，我们需要把SqlMapConfig.xml配置文件中的内容配置到applicationContext.xml配置文件中去。

## 零、配置Maven依赖
```xml
  <properties>
    <spring.version>5.0.2.RELEASE</spring.version>
    <slf4j.version>1.6.6</slf4j.version>
    <log4j.version>1.2.12</log4j.version>
    <mysql.version>8.0.19</mysql.version>
    <mybatis.version>3.4.5</mybatis.version>
  </properties>

  <dependencies>

    <!-- spring -->
    <dependency>
      <!--AOP-->
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>1.6.8</version>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-aop</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-test</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-tx</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>${mysql.version}</version>
    </dependency>

    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <version>2.5</version>
      <scope>provided</scope>
    </dependency>

    <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>jsp-api</artifactId>
      <version>2.0</version>
      <scope>provided</scope>
    </dependency>

    <dependency>
      <groupId>jstl</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>

    <!-- log start -->
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>${log4j.version}</version>
    </dependency>

    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-api</artifactId>
      <version>${slf4j.version}</version>
    </dependency>

    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-log4j12</artifactId>
      <version>${slf4j.version}</version>
    </dependency>
    <!-- log end -->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>${mybatis.version}</version>
    </dependency>

    <!--Spring整合Mybatis-->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>1.3.0</version>
    </dependency>

    <dependency>
      <groupId>c3p0</groupId>
      <artifactId>c3p0</artifactId>
      <version>0.9.1.2</version>
      <type>jar</type>
      <scope>compile</scope>
    </dependency>
  </dependencies>
```

## 一、配置Spring框架
- 1、编写Dao接口IAccountDao（使用Mybatis框架只需写Dao接口即可，不用再写Dao实现类）
    - 持久层使用`@Repository("accountDao")`注解
- 2、编写Service层接口IAccountService，以及它的实现类impl.AccountService
    - 业务层使用`@Service("accountService")`注解
- 3、在/resources下编写Spring配置文件 applicationContext.xml如下：
    - 注意开启Spring注解扫描时只希望Spring管理service和dao的注解，忽略SpringMVC中的Controller注解
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:context="http://www.springframework.org/schema/context"
           xmlns:aop="http://www.springframework.org/schema/aop"
           xmlns:tx="http://www.springframework.org/schema/tx"
           xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/context
            http://www.springframework.org/schema/context/spring-context.xsd
            http://www.springframework.org/schema/aop
            http://www.springframework.org/schema/aop/spring-aop.xsd
            http://www.springframework.org/schema/tx
            http://www.springframework.org/schema/tx/spring-tx.xsd">
    
        <!--开启Spring注解扫描, 只希望Spring管理service和dao-->
        <context:component-scan base-package="com.study">
            <!--配置哪些注解不扫描-->
            <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
        </context:component-scan>
    </beans>
    ```
- 4、创建Junit测试目录src/test/java/com/study/test/TestSpring.java，在其中测试Spring框架时候运行成功（是否能从Spring核心容器中取到）
    ```java
    public class TestSpring {
    
        @Test
        public void test1(){
            // 加载Spring配置文件 -> 获取对象 -> 调用方法
            ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
            IAccountService accountService = (IAccountService) ac.getBean("accountService");
            accountService.findAll();
        }
    }
    ```
## 二、配置SpringMVC
- 1、在 web.xml 中配置前端核心控制器（DispatcherServlet）和中文编码过滤器
    ```xml
    <!DOCTYPE web-app PUBLIC
     "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
     "http://java.sun.com/dtd/web-app_2_3.dtd" >
    
    <web-app>
      <display-name>Archetype Created Web Application</display-name>
      <!--配置前端控制器-->
      <servlet>
        <servlet-name>dispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--加载springmvc.xml的配置文件-->
        <init-param>
          <param-name>contextConfigLocation</param-name>
          <param-value>classpath:springmvc.xml</param-value>
        </init-param>
        <!--一启动服务器就创建该servlet-->
        <load-on-startup>1</load-on-startup>
      </servlet>
      <servlet-mapping>
        <servlet-name>dispatcherServlet</servlet-name>
        <url-pattern>/</url-pattern>
      </servlet-mapping>
    
      <!--解决中文乱码的过滤器-->
      <filter>
        <filter-name>characterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
          <param-name>encoding</param-name>
          <param-value>UTF-8</param-value>
        </init-param>
      </filter>
      <filter-mapping>
        <filter-name>characterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
      </filter-mapping>
    
    </web-app>
    ```
- 2、在/resources目录下编写SpringMVC的配置文件springmvc.xml，在其中配置视图解析器、SpringMVC要扫描的包、开启SpringMVC注解支持
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:mvc="http://www.springframework.org/schema/mvc"
           xmlns:context="http://www.springframework.org/schema/context"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="
            http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">
        
        <!--开启注解扫描：只扫描MVC中的controller -->
        <context:component-scan base-package="com.study">
            <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
        </context:component-scan>
        
        <!--配置视图解析器-->
        <bean id="internalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
            <property name="prefix" value="/WEB-INF/pages/"></property>
            <property name="suffix" value=".jsp"></property>
        </bean>
        
        <!--配置过滤静态资源-->
        <mvc:resources location="/css/" mapping="/css/**" />
        <mvc:resources location="/images/" mapping="/images/**" />
        <mvc:resources location="/js/" mapping="/js/**" />
    
        <!--开启SpringMVC注解支持-->
        <mvc:annotation-driven />
    </beans>
    ```
- 3、编写 Controller 和 jsp 页面
    - @Controller("accountController")
    - @RequestMapping("/account")
    ```java
        @Controller
        @RequestMapping("/account")
        public class AccountController {
        
            @RequestMapping("/findAll")
            public  String findAll(){
                System.out.println("Controller中的查询所有账户的方法执行了");
                return "list";
            }
        }
    ```
- 4、启动Tomcat, 测试SpringMVC是否运行正常

## 三、整合 Spring 和 SpringMVC
- 目前Spring容器的配置文件applicationContext还没有被加载
    - ![qqIfNYj](https://i.imgur.com/qqIfNYj.png)
- 在`web.xml`中配置`org.springframework.web.context.ContextLoaderListener`监听器实现启动服务器时根据`applicationContext`配置文件来创建Spring容器:
    - 默认只加载WEF-INF目录下的applicationContext.xml文件。若SpringFramework的配置文件不在该目录下需要再在web.xml中添加一个全局的Spring配置文件路径参数。 
    ```xml
      <!--配置加载Spring配置文件用的监听器, 默认只加载WEF-INF目录下的applicationContext.xml文件-->
      <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
      </listener>
      <!--设Spring置配置文件的路径-->
      <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
      </context-param>
    ```

## 四、配置Mybatis
- 1、搭建Mybatis环境，编写配置文件SqlMapConfig.xml。在其中设置好MySQL的连接参数和Mybatis要扫描注解的包
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE configuration
            PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-config.dtd">
    <configuration>
        <!--配置MySQL连接-->
        <environments default="mysql">
            <environment id="mysql">
                <transactionManager type="JDBC"/>
                <dataSource type="POOLED">
                    <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                    <property name="url" value="jdbc:mysql:///ssm"/>
                    <property name="username" value="root"/>
                    <property name="password" value="12345678"/>
                </dataSource>
            </environment>
        </environments>
    
        <!--引入映射配置扫描的包-->
        <mappers>
            <package name="com.study.dao"/>
        </mappers>
    </configuration>
    ```
- 2、给持久层接口方法上添加Mybatis相关的SQL注解
    - 若用了注解开发，则不应该在resources目录下创建dao.xml文件。否则不管是否用到xml配置文件，Mybatis都会报错
        ```java
          /**
           * 账户持久层接口（使用Mybatis框架只需写接口即可，不用再写实现类）
           */
          public interface IAccountDao {
          
              @Select("select * from account")
              List<Account> findAll(); // 查询所有
          
          
              @Insert("insert into account (name, money) values (#{name}, #{money})")
              void saveAccount(Account account); // 保存账户信息
          }
        ```
    - 若自实现dao类来开发，则应该编写 AccountDao 映射配置文件
        ```xml
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE mapper
         PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
         "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
        <mapper namespace="com.itheima.dao.IAccountDao">
          <!-- 查询所有账户 -->
          <select id="findAll" resultType="com.itheima.domain.Account">
              select * from account
          </select>
          <!-- 新增账户 -->
            <insert id="save" parameterType="com.itheima.domain.Account">
              insert into account(name,money) values(#{name},#{money});
            </insert>
        </mapper>
        ```
- 3、编写测试方法，测试Mybatis是否配置正常
    ```java
    @Test
    public void testSave() throws IOException {
        // 1 加载Mybatis配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        // 2 创建SqlSessionFactory工厂对象
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        // 3 创建SqlSession
        SqlSession sqlSession = factory.openSession();
        // 4 获取dao接口的代理对象
        IAccountDao accountDao = sqlSession.getMapper(IAccountDao.class);
        // 5 执行持久层操作
        Account newAccount = new Account("李白", 2000.0);
        accountDao.saveAccount(newAccount);
        // 6 提交事务
        sqlSession.commit();
        // 7 释放资源
        sqlSession.close();
        in.close();
    }
    ```
## 五、整合Spring和Mybatis
- 我们之前单独学习Mybatis的框架的时候，只写到Dao接口那一层，并在Junit单元测试中每次都new一次SqlSessionFactory、SqlSession和IUserDao对象。而在实际的整合开发中，Mybatis框架帮我们从接口中创建出来的代理对象也是需要交给Spring容器管理的，下面我们就需要做相应配置，让Spring容器可以管理Mybatis创建出的Dao接口代理对象。
- 在单独使用Mybatis测试方法的时候，我们是提供工厂获取到SqlSession，再通过SqlSession获取到Dao接口的代理对象。此时我们的业务层AccountService中的方法仍然只是模拟运行的，**我们希望通过Sping框架，将Dao接口的代理实现对象注入到AccountService方法中，这样我们的AccountService就能真正起到数据库操作的作用**
- 1、把SqlMapConfig.xml配置文件中的内容配置到applicationContext.xml配置文件中：让Spring 接管 MyBatis 的 Session 工厂
    - **整合后我们不再需要单独的Mybatis配置文件SqlMapConfig.xml**
    ```xml
        <!--Spring整合Mybatis-->
            <!--1 配置出c3p0连接池-->
        <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
            <property name="driverClass" value="com.mysql.cj.jdbc.Driver"></property>
            <property name="jdbcUrl" value="jdbc:mysql:///ssm"></property>
            <property name="user" value="root"></property>
            <property name="password" value="12345678"></property>
         </bean>
            <!--2 配置SqlSession工厂-->
        <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
            <property name="dataSource" ref="dataSource"/>
        </bean>
            <!--3 配置AccountDao接口所在的包，让Spring容器存入这些类的代理对象-->
        <bean id="mapperScanner" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
            <property name="basePackage" value="com.study.dao"></property>
        </bean>
    ```
- 2、在Spring的业务册Service中注入(@Autowired)持久层的对象
    ```java
    @Service("accountService")
    public class AccountService implements IAccountService {  
        @Autowired
        private IAccountDao accountDao;
    
        public List<Account> findAll() {
            System.out.println("Service层 findAll()");
            return accountDao.findAll();
        }
    
        public void saveAccount(Account account) {
    
            System.out.println("Service层 saveAccount()");
            accountDao.saveAccount(account);
        }
    }
    ``` 
- 3、修改Controller接受业务层的查询结果：
    ```java
    @Controller
    @RequestMapping("/account")
    public class AccountController {
    
        @Autowired
        private AccountService accountService;
    
        @RequestMapping("/findAll")
        public  String findAll(Model model){
            System.out.println("Controller中的查询所有账户的方法执行了");
            // 调用业务层的方法
            List<Account> list = accountService.findAll();
            model.addAttribute("list", list);
            return "list";
        }
    }
    ```
- 4、修改jsp页面展示查询结果：
    ```jsp
    <body>
        <h3>查询结果</h3>
        <c:forEach items="${list}" var="account">
            ${account.name}
            ${account.money}
        </c:forEach>
    </body>
    ```
![oUYxlh](https://gitee.com/pxqp9W/testmarkdown/raw/master/imgs/2021/03/oUYxlh.png)

## 六、Spring整合Mybatis后的事务配置
- 1、在在Spring配置文件applicationContext.xml中配置**声明式事务管理**：
    ```xml
    <!--配置Spring框架的声明式事务管理-->
    <!--1、配置事务管理器-->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
    <!--2、配置事务的通知-->
    <tx:advice id="txAdvice" transaction-manager="txManager">
        <tx:attributes>
            <tx:method name="find*" read-only="true"/>
            <tx:method name="save*" isolation="DEFAULT"/>
        </tx:attributes>
    </tx:advice>
    <!--3、配置AOP增强-->
    <aop:config>
        <aop:advisor advice-ref="txAdvice" pointcut="execution(* com.study.service.impl.*.*(..))"/>
    </aop:config>
    ```
- 2、完善控制器中的save方法：
    ```java
    @RequestMapping("/save")
    /*借助Spring自动封装来自jsp页面表单的数据到实体类Account*/
    public void save(Account account, HttpServletResponse response, HttpServletRequest request) throws IOException {
        System.out.println("Controller中的保存账户的方法执行了");
        // 调用业务层的方法
        accountService.saveAccount(account);
        // 增加用户之后重定向查询结果页面显示结果
        response.sendRedirect(request.getContextPath() + "/account/findAll");
        return;
    }
    ```
- 3、编写jsp
    ```jsp
    <div>测试saveAccount(带事务管理)</div>
    <form action="/day03/account/save" method="post">
        姓名：<input type="text" name="name"/> <br/>
        金额：<input type="text" name="money"/> <br/>
        <input type="submit" value="保存用户">
    </form>
   ```

## 整合SSM框架中的配置文件说明
- `webapp/WEB-INF/web/xml`： 用于对JAVA WEB项目进行配置与Servlet相关的项。我们可能需要在其中配置：
    -  SringMVC的前端控制器`DispatcherServlet`，它本质上是一个Servlet，因此在这里配置
    -  用于解决中文乱码的过滤器`CharacterEncodingFilter`，属于Servlet容器中的过滤器，因此在此处配置
    -  因为本项目是Web项目与Spring项目的结合，因此在web.xml中还需要配置一个`ContextLoaderListener`，它本质上是一个监听器，用于加载Spring Framework的配置文件
    -  在<context-param>全局参数中配置Spring Framework的全局配置文件路径`contextConfigLocation`
    ```xml
    <!DOCTYPE web-app PUBLIC
     "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
     "http://java.sun.com/dtd/web-app_2_3.dtd" >

    <web-app>
      <display-name>Archetype Created Web Application</display-name>
      <!--配置加载Spring配置文件用的监听器, 默认只加载WEF-INF目录下的applicationContext.xml文件-->
      <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
      </listener>
      <!--设Spring置配置文件的路径-->
      <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
      </context-param>

      <!--配置前端控制器-->
      <servlet>
        <servlet-name>dispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--加载springmvc.xml的配置文件-->
        <init-param>
          <param-name>contextConfigLocation</param-name>
          <param-value>classpath:springmvc.xml</param-value>
        </init-param>
        <!--一启动服务器就创建该servlet-->
        <load-on-startup>1</load-on-startup>
      </servlet>
      <servlet-mapping>
        <servlet-name>dispatcherServlet</servlet-name>
        <url-pattern>/</url-pattern>
      </servlet-mapping>

      <!--解决中文乱码的过滤器-->
      <filter>
        <filter-name>characterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
          <param-name>encoding</param-name>
          <param-value>UTF-8</param-value>
        </init-param>
      </filter>
      <filter-mapping>
        <filter-name>characterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
      </filter-mapping>

    </web-app>
    ```
- `SqlMapConfig`(有的项目也叫`mybatis-config.xml`)： Mybatis的主配置文件。在其中配置<environments>与<mappers>
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE configuration
            PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-config.dtd">
        <!--本xml配置的内容只是为了单独使用Mybatis，
            若需要与Spring整合，需要在Spring的核心配置文件中进一步设置，
            并替代本文件
        -->
    <configuration>
        <!--配置MySQL连接-->
        <environments default="mysql">
            <environment id="mysql">
                <transactionManager type="JDBC"/>
                <dataSource type="POOLED">
                    <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                    <property name="url" value="jdbc:mysql:///ssm"/>
                    <property name="username" value="root"/>
                    <property name="password" value="12345678"/>
                </dataSource>
            </environment>
        </environments>

        <!--引入映射配置扫描的包-->
        <mappers>
            <package name="com.study.dao"/>
        </mappers>
    </configuration>
    ```
- `springmvc.xml`：Spring MVC的配置文件。我们需要在其中配置SpringMVC中的各种“器”：例如视图解析器、类型转换器等。
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:mvc="http://www.springframework.org/schema/mvc"
           xmlns:context="http://www.springframework.org/schema/context"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="
            http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

        <!--开启注解扫描：只扫描MVC中的controller -->
        <context:component-scan base-package="com.study">
            <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
        </context:component-scan>

        <!--配置视图解析器-->
        <bean id="internalResourceViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
            <property name="prefix" value="/WEB-INF/pages/"></property>
            <property name="suffix" value=".jsp"></property>
        </bean>

        <!--配置不过滤静态资源-->
        <mvc:resources location="/css/" mapping="/css/**" />
        <mvc:resources location="/images/" mapping="/images/**" />
        <mvc:resources location="/js/" mapping="/js/**" />

        <!--开启SpringMVC注解支持-->
        <mvc:annotation-driven />

    </beans>    
    ```
- `applicationContext.xml`： Spring Framework的核心配置文件。我们需要在其中配置需要交给Spring容器管理的Bean和Spring事务管理相关的事宜。
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:context="http://www.springframework.org/schema/context"
           xmlns:aop="http://www.springframework.org/schema/aop"
           xmlns:tx="http://www.springframework.org/schema/tx"
           xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/context
            http://www.springframework.org/schema/context/spring-context.xsd
            http://www.springframework.org/schema/aop
            http://www.springframework.org/schema/aop/spring-aop.xsd
            http://www.springframework.org/schema/tx
            http://www.springframework.org/schema/tx/spring-tx.xsd">

        <!--开启Spring注解扫描, 只希望Spring管理service和dao-->
        <context:component-scan base-package="com.study">
            <!--配置哪些注解不扫描-->
            <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
        </context:component-scan>

        <!--Spring整合Mybatis-->
            <!--1 配置出c3p0连接池-->
        <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
            <property name="driverClass" value="com.mysql.cj.jdbc.Driver"></property>
            <property name="jdbcUrl" value="jdbc:mysql:///ssm"></property>
            <property name="user" value="root"></property>
            <property name="password" value="12345678"></property>
         </bean>
            <!--2 配置SqlSession工厂-->
        <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
            <property name="dataSource" ref="dataSource"/>
        </bean>
            <!--3 配置AccountDao接口所在的包，让Spring容器存入这些类的代理对象-->
        <bean id="mapperScanner" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
            <property name="basePackage" value="com.study.dao"></property>
        </bean>

        <!--配置Spring框架的声明式事务管理-->
        <!--1、配置事务管理器-->
        <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
            <property name="dataSource" ref="dataSource"></property>
        </bean>
        <!--2、配置事务的通知-->
        <tx:advice id="txAdvice" transaction-manager="txManager">
            <tx:attributes>
                <tx:method name="find*" read-only="true"/>
                <tx:method name="save*" isolation="DEFAULT"/>
            </tx:attributes>
        </tx:advice>
        <!--3、配置AOP增强-->
        <aop:config>
            <aop:advisor advice-ref="txAdvice" pointcut="execution(* com.study.service.impl.*.*(..))"/>
        </aop:config>
    </beans>
    ```

## 参考资料
- [关于web.xml配置的那些事儿 - 个人文章 - SegmentFault 思否](https://segmentfault.com/a/1190000011404088)
- [applicationContext.xml详解_Java_NPPPNHHH的博客-CSDN博客](https://blog.csdn.net/heng_ji/article/details/7022171)
- [mybatis入门基础(三)----SqlMapConfig.xml全局配置文件解析 - 阿赫瓦里 - 博客园](https://www.cnblogs.com/selene/p/4607004.html)
- [springmvc.xml和applicationContext.xml配置的特点 - 努力中国 - 博客园](https://www.cnblogs.com/kaiwen1/p/6864458.html)








