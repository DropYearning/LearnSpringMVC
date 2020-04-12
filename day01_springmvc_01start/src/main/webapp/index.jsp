<%--
  Created by IntelliJ IDEA.
  User: brightzh
  Date: 2020/4/10
  Time: 11:57 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>SpringMVC入门</title>
</head>
<body>
    <h3>入门程序</h3>
    <%--这里如果直接写hello默认会访问http:localhost:8080/hello--%>
    <a href="/start/proj1/hello">入门程序链接</a>
    <hr/>
    <a href="/start/proj1/testRequestMapping">RequestMapping注解</a>
    <hr/>
    <a href="/start/proj2/testparam?username=hehe&password=1234">请求参数绑定（方法参数）</a>

<%--    <hr/>--%>
<%--    <div>表单传递参数，封装到JavaBean</div>--%>
<%--    <form action="/start/proj2/saveaccount" method="post" >--%>
<%--        &lt;%&ndash;属性名称要与实体类的变量名一致&ndash;%&gt;--%>
<%--        账号：<input type="text" name="username"/><br/>--%>
<%--        密码：<input type="text" name="password"/><br/>--%>
<%--        金额：<input type="text" name="money"/><br/>--%>
<%--        用户姓名：<input type="text" name="user.uname"/><br/>--%>
<%--        用户年龄：<input type="text" name="user.age"/><br/>--%>
<%--        <input type="submit" value="提交"/><br/>--%>
<%--    </form>--%>

    <hr/>
    <div>请求参数绑定:表单传递参数，含有复杂类型（集合、Map）</div>
    <form action="/start/proj2/saveaccount" method="post" >
        <%--属性名称要与实体类的变量名一致--%>
        账号：<input type="text" name="username"/><br/>
        密码：<input type="text" name="password"/><br/>
        金额：<input type="text" name="money"/><br/>
            <%--封装到Account.list[0]位置--%>
        list用户姓名：<input type="text" name="list[0].uname"/><br/>
        list用户年龄：<input type="text" name="list[0].age"/><br/>
            <%--自定义类型转换--%>
        list用户生日：<input type="text" name="list[0].date"/><br/>
            <%--封装到Account.map['one']位置--%>
<%--        map用户姓名：<input type="text" name="map['one'].uname"/><br/>--%>
<%--        map用户年龄：<input type="text" name="map['one'].age"/><br/>--%>
        <input type="submit" value="提交"/><br/>
    </form>
    <hr/>
    <a href="/start/proj2/servlet">Servlet原生API</a>

</body>
</html>
