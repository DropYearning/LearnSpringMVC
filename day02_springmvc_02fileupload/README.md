# LearnSpringMVC-day2-SpringMVC-文件上传
[SpringMVC教程IDEA版哔哩哔哩 (゜-゜)つロ 干杯~-bilibili](https://www.bilibili.com/video/BV1Sb411s7qa?from=search&seid=12129021327363557187)

## 1 传统方式上传文件（Commons-fileupload）
传统方式的文件上传，指的是我们上传的文件和访问的应用存在于同一台服务器上。并且上传完成之后，浏览器可能跳转。

### 1.1 文件上传的必要前提
- form 表单的 enctype 取值必须是：`multipart/form-data`
    - (默认值是:application/x-www-form-urlencoded)
- enctype:是表单请求正文的类型
-  method 属性取值必须是 Post
-  提供一个文件选择域<input type=”file” />

### 1.2 示例代码
```jsp
    <h3>文件上传演示</h3>
    <form action="upload1" method="post" enctype="multipart/form-data">
        选择上传文件:<input type="file" name="upload" /><br/>
        <input type="submit" value="上传"/>
    </form>
```
- 需要借助第三方包：Commons-fileupload 组件实现文件上传，需要导入该组件相应的支撑jar 包：Commons-fileupload和commons-io
```java
// 传统方式上传文件(借助:common-fileupload.jar)
    @RequestMapping("/upload1")
    public String fileUpload(HttpServletRequest request) throws Exception {
        System.out.println("文件上传...");
        //使用fileupload组件完成文件上传
        // 1 指定文件上传的位置
        String path = request.getSession().getServletContext().getRealPath("/uploads");
        // 2 判断该路径是否存在
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        // 3 解析Request对象，获取上传的文件项
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> fileItems = upload.parseRequest(request);
        // 4 遍历
        for (FileItem item:fileItems){
            // 进行判断，当前的item对象是否是上传文件项
            if (item.isFormField()){ // 若为true则为普通表单项目
            }else{ // 若为上传文件项
                // 获取到上传文件的名称
                String filename = item.getName();
                // 将文件的名称设置为唯一的uuid
                String uuid = UUID.randomUUID().toString().replace("-", "");
                filename = uuid + "_" + filename;
                item.write(new File(path, filename));
                // 删除临时文件
                item.delete();
            }
        }
        return "success";
    }
```



## 2 SpringMVC的文件上传（通过文件解析器）
- ![rQPAWC5](https://i.imgur.com/rQPAWC5.png)
- 1、在springmvc.xml中配置文件解析器，可以对上传的文件进行更多的设置：
    ```xml
    <!-- 配置文件上传解析器 -->
    <bean id="multipartResolver" <!-- id 的值是固定的-->
        class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <!-- 设置上传文件的最大尺寸为 5MB -->
        <property name="maxUploadSize">
            <value>5242880</value>
        </property>
    </bean>
    ```
  - 注意：文件上传的解析器id是固定的，不能起别的名称，否则无法实现请求参数的绑定。（不光是文件，其他字段也将无法绑定）
- 2、jsp:
    ```jsp
    <h3>SpringMVC文件上传演示</h3>
    <form action="upload2" method="post" enctype="multipart/form-data">
        选择上传文件:<input type="file" name="upload" /><br/>
        <input type="submit" value="上传"/>
    </form>
    ``` 
- 3、Controller：
    ```java
    // SpringMVC上传文件(借助文件解析器)
        @RequestMapping("/upload2")
                                                            // MultipartFile的变量名要与表单中的name字段一致
        public String fileUpload2(HttpServletRequest request, MultipartFile upload) throws Exception {
            System.out.println("SpringMVC文件上传...");
            String path = request.getSession().getServletContext().getRealPath("/uploads");
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            // 获取到上传文件的名称
            String filename = upload.getOriginalFilename();
            // 将文件的名称设置为唯一的uuid
            String uuid = UUID.randomUUID().toString().replace("-", "");
            filename = uuid + "_" + filename;
            upload.transferTo(new File(path, filename));
            return "success";
        }
    ```
- 超过限制文件大小的提示：
    ![pyJYyzL](https://i.imgur.com/pyJYyzL.png)
    
    
## 3 SpringMVC跨服务器方式的文件上传
- 分服务器的目的:在实际开发中，我们会有很多处理不同功能的服务器。例如：
    - ![FgpW4md](https://i.imgur.com/FgpW4md.png)
    - 应用服务器：负责部署我们的应用
    - 数据库服务器：运行我们的数据库
    - 缓存和消息服务器：负责处理大并发访问的缓存和消息
    - 文件服务器：负责存储用户上传文件的服务器。
- 我们希望上传的文件可以保存至专门的“文件服务器”，因此需要配置另一个文件服务器项目和另一个tomcat



## 参考资料
- [Spring MVC框架入门教程](http://c.biancheng.net/spring_mvc/)
- [前后端分离跨服务器文件上传-Java SpringMVC版 - web喵神 - 博客园](https://www.cnblogs.com/libo0125ok/p/7773898.html)
- [使用jersey组件向图片资源服务器上传图片报405 Method Not Allowed错误_Java_chenmual的博客-CSDN博客](https://blog.csdn.net/chenmual/article/details/90438172)























