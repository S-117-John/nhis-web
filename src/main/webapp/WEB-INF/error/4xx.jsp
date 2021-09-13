<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title>4xx - 客户端发送数据异常</title>
</head>
<%--Springboot默认的错误处理页面规则中，会从模板目录的error文件夹下面按照错误状态码匹配页面，优先完全匹配，之后在模糊匹配--%>
<body>
	<h2>4xx -  客户端发送数据异常等.</h2>
	<p><a href="<c:url value="/"/>">返回首页</a></p>
</body>
</html>
