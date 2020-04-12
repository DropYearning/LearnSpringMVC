# LearnSpringMVC-day1-SpringMVC中常用注解
[SpringMVC教程IDEA版哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7qa?from=search&seid=12129021327363557187)

## 1 @RequestParam
- @RequestParam位置：加在方法的参数名称前{ElementType.PARAMETER}
- 作用：**把请求中指定名称的参数给控制器中的形参赋值。**
- 属性：
    - value：请求参数中的名称。
    - required：请求参数中是否必须提供此参数。默认值：true。表示必须提供，如果不提供将报错。
```java
@RequestMapping("/testRequestParam")
    // 将请求中名为name的变量值映射到username上, 且这个变量不必须
    public String testRequestParam(@RequestParam(name = "name" ) String username){
        System.out.println("testRequestParam方法执行了");
        System.out.println(username);
        return "success";
    }
```
## 2 @RequestBody
- 位置：加在方法的参数名称前{ElementType.PARAMETER}
- 作用：用于**获取请求体的整个内容**。直接使用得到是 key=value&key=value...结构的数据。get
    - 请求方式不适用。
- 属性：required：是否必须有请求体。默认值是:true。当取值为 true 时,get 请求方式会报错。如果取值为 false，get 请求得到是 null。
- 例如输入下面的表单：![tqU35q3](https://i.imgur.com/tqU35q3.png)
- 输出为：`username=123&age=123`
    ```java
    // 测试获取请求体的内容
        @RequestMapping("/testRequestBody")
        public String testRequestBody(@RequestBody String body){
            System.out.println("testRequestBody");
            System.out.println(body);
            return "success";
        }
    ```

## 3 @PathVariable
- 作用：用于绑定 url 中的**占位符**。
    - 例如：请求 url 中 /delete/{id}，这个{id}就是 url 占位符。url 支持占位符是 spring3.0 之后加入的。是 springmvc 支持 rest 风格 URL 的一个重要标志。
- 属性：
    - value：用于指定 url 中占位符名称。
    - required：是否必须提供占位符。
    ```java
    // 测试@PathVariable注解
        @RequestMapping("/testPathVariable/{sid}")
        public String testPathVariable(@PathVariable(name ="sid") String id){
            System.out.println("testRequestBody");
            System.out.println(id);
            return "success";
        }
    ```
    - 请求的url为：`<a href="/anno/proj3/testPathVariable/10">测试PathVariable</a>`


### 3.1RESTful风格
- REST（英文：Representational State Transfer，简称 REST）描述了一个架构样式的网络系统，
  比如 web 应用程序。
- 核心思想：使用HTTP协议里面四个操作方式来对应同一个请求路径的不同方法：GET 、POST 、PUT、DELETE。它们分别对应四种基本操作：GET 用来获取资源，POST 用来新建资源，PUT 用来更新资源，DELETE 用来删除资源。
- restful 的优点 ：它结构清晰、符合标准、易于理解、扩展方便，所以正得到越来越多网站的采用。
- 因为请求的路径相同，所有易于缓存
- ![jORhByv](https://i.imgur.com/jORhByv.png)

### 3.2 HiddenHttpMethodFilter过滤器
- 作用：由于浏览器 form 表单只支持 GET 与 POST 请求，而 DELETE、PUT 等 method 并不支持，Spring3.0 添
  加了一个过滤器，可以将浏览器请求改为指定的请求方式，发送给我们的控制器方法，使得支持 GET、POST、PUT
  与 DELETE 请求。
- 使用方法：
  - 第一步：在 web.xml 中配置该过滤器。
  - 第二步：请求方式必须使用 post 请求。
  - 第三步：按照要求提供_method 请求参数，该参数的取值就是我们需要的请求方式。
- 更推荐自行使用postman进行测试
   
## 4 @RequestHeader
- 作用：用于获取请求消息头。
- 属性：
  - value：指定获取某一个消息头
  - required：是否必须有此消息头
- 注：在实际开发中一般不怎么用。
    ```java
     // 测试@RequestHeader，获取并输出请求头
        @RequestMapping("/testRequestHeader")
        public String testRequestHeader(@RequestHeader(value = "accept") String header){
            System.out.println("testRequestBody");
            System.out.println(header);
            return "success";
        }
    ```

## 5 @CookieValue
- 作用：用于把指定 cookie 名称的值传入控制器方法参数
- 属性：
  - value：指定 cookie 的名称。
  - required：是否必须有此 cookie。

## 6 @ModelAttribute
> @ModelAttribute注解顾名思义就是在MVC中Model一级的属性。
>> Model（模型） - 模型代表一个存取数据的对象或 JAVA POJO。它也可以带有逻辑，在数据变化时更新控制器。

- 作用：它可以用于修饰方法和参数。
- 位置：方法或者参数上
    - 出现在方法上，**表示当前方法会在控制器的方法执行之前，先执行**。它可以修饰没有返回值的方法，也可
      以修饰有具体返回值的方法。
    - 出现在参数上，获取指定的数据给参数赋值。
- 属性：
  - value：用于获取数据的 key。key 可以是 POJO 的属性名称，也可以是 map 结构的 key。
- 应用场景：当表单提交数据不是完整的实体类数据时，保证没有提交数据的字段使用数据库对象原来的数据
    - 例如：
      我们在编辑一个用户时，用户有一个创建信息字段，该字段的值是不允许被修改的。在提交表单数据是肯定没有此字段的内容，一旦更新会把该字段内容置为 null，此时就可以使用此注解解决问题。
    ```java
         //ModelAttribute基于 POJO 属性的基本使用
         //该方法会比控制器方法先执行
         //只会替换到前端表单中没有填入的值！
        @ModelAttribute
        public User showUser(String uname){
            System.out.println("showUser方法执行了");
            // 通过用户名查询数据库（模拟）
            User user = new User();
            user.setUname(uname);
            user.setAge(20);
            user.setDate(new Date());
            return user;
        }
          // 测试@ModelAttribute
          @RequestMapping("/testModelAttribute")
          public String testModelAttribute(User user){
              System.out.println("testModelAttribute");
              System.out.println(user);
              return "success";
          }
    ```
    
### 6.1 ModelAttribute基于Map的应用场景示例 ：
```java
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
// 测试@ModelAttribute
@RequestMapping("/testModelAttribute")
public String testModelAttribute(@ModelAttribute("abc") User user){
    System.out.println("testModelAttribute");
    System.out.println(user);
    return "success";
}
```
- 从@ModelAttribute("abc")中可以取出在showUser()方法中存入的user对象
- **取出的user对象只会覆盖表单中没有填写的字段**

## 7 @SessionAttributes
- 位置：只能添加在类上
- 作用：用于多次执行控制器方法间的参数共享。
- 属性：
    - value：用于指定存入的属性名称
    - type：用于指定存入的数据类型。
- jsp 中的代码：
    ```jsp
    <!-- SessionAttribute 注解的使用 -->
    <a href="springmvc/testPut">存入 SessionAttribute</a>
    <hr/>
    <a href="springmvc/testGet">取出 SessionAttribute</a>
    <hr/>
    <a href="springmvc/testClean">清除 SessionAttribute</a>
    ```
- 控制器中的代码：
    ```java
    控制器中的代码：
    /**
    * SessionAttribute 注解的使用
    * @author 黑马程序员
    * @Company http://www.ithiema.com
    * @Version 1.0
    */
    @Controller("sessionAttributeController")
    @RequestMapping("/springmvc")
    @SessionAttributes(value ={"username","password"},types={Integer.class})
    public class SessionAttributeController {
    /**
    * 把数据存入 SessionAttribute
    * @param model
    * @return
    * Model 是 spring 提供的一个接口，该接口有一个实现类 ExtendedModelMap
    * 该类继承了 ModelMap，而 ModelMap 就是 LinkedHashMap 子类
    */
    @RequestMapping("/testPut")
    public String testPut(Model model){
     model.addAttribute("username", "泰斯特");
     model.addAttribute("password","123456");
     model.addAttribute("age", 31);
     //跳转之前将数据保存到 username、password 和 age 中，因为注解@SessionAttribute 中有
    这几个参数
     return "success";
     }
    
     @RequestMapping("/testGet")
     public String testGet(ModelMap model){
    
    System.out.println(model.get("username")+";"+model.get("password")+";"+model.get("a
    ge"));
     return "success";
     }
    
     @RequestMapping("/testClean")
     public String complete(SessionStatus sessionStatus){
     sessionStatus.setComplete();
     return "success";
     }
    }
    ```
- 


















