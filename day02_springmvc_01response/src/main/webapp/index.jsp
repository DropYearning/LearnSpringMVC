<%--
  Created by IntelliJ IDEA.
  User: brightzh
  Date: 2020/4/12
  Time: 6:15 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
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
</head>

<body>
    <a href="/day02/proj1/testString" >test字符串</a>
    <hr/>
    <a href="/day02/proj1/testVoid" >testVoid</a>
    <hr/>
    <a href="/day02/proj1/testModelAndView" >testModelAndView</a>
    <hr/>
    <a href="/day02/proj1/testForwardOrRedirect" >testForwardOrRedirect</a>
    <hr/>
    <button id="btn">发送Ajax请求</button>

</body>
</html>
