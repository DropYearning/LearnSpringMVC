# LearnSpringMVC-day2-SpringMVC-异常处理
[SpringMVC教程IDEA版哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7qa?from=search&seid=12129021327363557187)

## 1 异常处理
- 系统中异常包括两类：预期异常和运行时异常 RuntimeException，前者通过捕获异常从而获取异常信息，后者主要通过规范代码开发、测试通过手段减少运行时异常的发生。
- 系统的 dao、service、controller 出现的异常都通过 throws Exception **向上抛出，最后由SpringMVC前端控制器交由异常处理器进行异常处理**，如下图：
- ![bD0a1Qr](https://i.imgur.com/bD0a1Qr.png)
- ![yogeais](https://i.imgur.com/yogeais.png)
- 如果不处理异常，将会在浏览器页面中显示：
    - ![6wj3gsz](https://i.imgur.com/6wj3gsz.png)


## 2 实现SpringMVC异常处理的步骤
- ![GYCCSpz](https://i.imgur.com/GYCCSpz.png)

### 1、编写自定义异常类（用来输出提示信息）
    ```java
    /**
     * 自定义的异常类
     */
    public class SysException extends Exception {
        private String msg; // 提示信息
    
        public SysException(String msg) {
            this.msg = msg;
        }
    
        public String getMsg() {
            return msg;
        }
    
        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
    ```
### 2、编写异常处理器类(implement Exception)：
    ```java
    /**
     * 异常的处理器类，处理异常的业务逻辑
     */
    public class SysExceptionResolver implements HandlerExceptionResolver {
        @Override
        public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex) {
            // 获取到异常对象
            SysException e = null;
            if (ex instanceof SysException){
                e = (SysException) ex;
            }else{
                e = new SysException("系统正在维护");
            }
            // ModelAndView可以帮助我们跳转
            ModelAndView mv = new ModelAndView();
            mv.addObject("errorMsg", e.getMsg()); // 向ModelAndView存入一对键值对
            mv.setViewName("error"); // ModelAndView向视图解析器请求跳转到error页面
            return mv;
        }
    }
    ```
### 3、在SpringMVC.xml中配置异常处理器（跳转到提示页面）
    ```xml
    <!--配置异常处理器-->
        <bean id="sysExceptionResolver" class="com.study.exception.SysExceptionResolver"></bean>
    ```
### 4、编写显示异常的jsp页面
```jsp
<body>
    <h3>这是错误页面</h3>
    ${errorMsg}
</body>
```

### 5、测试
```java
@Controller
@RequestMapping("proj1")
public class UserController {

    @RequestMapping("/testException")
    public String testException() throws SysException{
        System.out.println("testException 方法执行了");
        try {
            // 模拟异常
            int e = 10/0;
        } catch (Exception ex) { // 捕获异常
            ex.printStackTrace();
            // 抛出自定义异常类
            throw new SysException("testException方法出现错误");
        }
        return "success";
    }
}
```

- 效果：![JObgzD6](https://i.imgur.com/JObgzD6.png)















