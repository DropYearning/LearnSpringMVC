<%--
  Created by IntelliJ IDEA.
  User: brightzh
  Date: 2020/4/14
  Time: 1:58 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
</head>
<body>
    <a href="/day03/account/findAll">测试findAll</a>
    <hr/>
    <div>测试saveAccount(带事务管理)</div>
    <form action="/day03/account/save" method="post">
        姓名：<input type="text" name="name"/> <br/>
        金额：<input type="text" name="money"/> <br/>
        <input type="submit" value="保存用户">
    </form>

</body>
</html>
