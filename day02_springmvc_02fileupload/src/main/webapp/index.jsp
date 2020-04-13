<%--
  Created by IntelliJ IDEA.
  User: brightzh
  Date: 2020/4/13
  Time: 9:00 上午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
</head>
<body>
    <h3>传统文件上传演示</h3>
    <form action="upload1" method="post" enctype="multipart/form-data">
        选择上传文件:<input type="file" name="upload" /><br/>
        <input type="submit" value="上传"/>
    </form>
    <hr/>
    <h3>SpringMVC文件上传演示</h3>
    <form action="upload2" method="post" enctype="multipart/form-data">
        选择上传文件:<input type="file" name="upload" /><br/>
        <input type="submit" value="上传"/>
    </form>
    <hr/>
    <h3>SpringMVC跨服务器文件上传演示</h3>
    <form action="upload3" method="post" enctype="multipart/form-data">
        选择上传文件:<input type="file" name="upload" /><br/>
        <input type="submit" value="上传"/>
    </form>
</body>
</html>
