<%--
  Created by IntelliJ IDEA.
  User: brightzh
  Date: 2020/4/14
  Time: 2:00 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>账户列表</title>
</head>
<body>
    <h3>查询结果</h3>
    <c:forEach items="${list}" var="account">
        ${account.name}
        ${account.money}
    </c:forEach>

</body>
</html>
