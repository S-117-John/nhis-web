<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title>4xx - 其他错误</title>
</head>

<body>
	<h2>4xx -  其他错误：客户端传入数据错误等.</h2>
	<p><a href="<c:url value="/"/>">返回首页</a></p>
</body>
</html>
