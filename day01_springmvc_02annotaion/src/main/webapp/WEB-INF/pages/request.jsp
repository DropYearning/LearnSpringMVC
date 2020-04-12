<%--
  Created by IntelliJ IDEA.
  User: brightzh
  Date: 2020/4/12
  Time: 4:25 下午
  To change this template use File | Settings | File Templates.
--%>
                                                            <%--设置不忽略EL表达式--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>测试SessionAttribute</title>
</head>
<body>
    <h3>方法调用成功</h3>

    ${requestScope.msg}
    <hr/>
    ${sessionScope}

</body>
</html>
