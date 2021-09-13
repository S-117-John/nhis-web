<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.LockedAccountException "%>
<%@ page import="org.apache.shiro.authc.DisabledAccountException "%>
<%@ page import="org.apache.shiro.authc.AuthenticationException "%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException "%>


<%@ taglib prefix="c" uri="c.tld" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/> 
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title></title>
		<script src="${ctx}/ui/jquery-1.11.2.min.js" type="text/javascript"></script>
		<style>
			* { margin:0; padding:0; list-style:none;border:0px}
			html { height:100%;width:100%}
			body { height:100%;width:100% }
			div { line-height:1.6;}
			a{color:#2F2F2F;text-decoration:none}
			a:hover{color:#ff6600;text-decoration:none}
			
			.dlbody{background:#044680;font-family:"微软雅黑";font-size:14px;}
			.login{width:556px;height:333px;position:absolute;left:50%;top:50%;margin-top:-166px;margin-left:-278px;background:#fff;border-radius: 5px;border-bottom:5px solid #8CBA51}
			.login_tit{height:35px}
			.fs_logo{background:url(${ctx}/ui/images/fs_logo.png);width:186px;height:52px;position:absolute;top:30px;right:40px}
			.login_c{padding:10px 0px 0px 0px}
			.login_v{padding:10px 20px;text-align:right;color:#999;font-family:"微软雅黑";font-size:14px;}
			.login_user{height:51px;width:435px;border:1px solid #E5EEF0;background:url(${ctx}/ui/images/login_inputbg.jpg);margin:15px auto;padding:0px 0px 0px 10px}
			.login_user span{line-height:50px;_line-height:30px;font-family:"微软雅黑";font-size:14px;margin:0px 10px 0px 0px;_position:relative;_top:-10px;background:url(${ctx}/ui/images/yhm_ico.jpg) no-repeat;padding-left:25px}
			.login_user input{border:0px;width:330px;font-size:14px;margin-top:-2px;background:#F4F8F9;vertical-align:middle;padding:12px 5px}
			
			input:-webkit-autofill {background-color: #E2E2E2!important;background-image: none;}
			.login_mm{height:51px;width:435px;border:1px solid #E5EEF0;background:url(${ctx}/ui/images/login_inputbg.jpg);margin:15px auto;padding:0px 0px 0px 10px}
			.login_mm span{line-height:50px;font-family:"微软雅黑";font-size:14px;margin:0px 10px 0px 0px;_position:relative;_top:-10px;background:url(${ctx}/ui/images/mm_ico.jpg) no-repeat;padding-left:25px}
			.login_mm input{border:0px;width:330px;font-size:14px;margin-top:-2px;background:#F4F8F9;vertical-align:middle;padding:12px 5px}
			.login_bq{color:#fff;position:absolute;bottom:110px;left:0px;;width:100%;text-align:center}
			.login_dl{text-align:;padding:10px 50px 0px 55px;position:relative}
			input.login_btn{color:#fff;font-weight:bold;font-size:18px;text-align:center;width:162px;height:45px;background:#0683B1;font-family:"微软雅黑";cursor:pointer;line-height:45px}
			input.login_btn:hover{background:#ff6600}
			
			.error-div {position: absolute;border: 1px solid #DD9420;padding: 5px 10px 5px;font-size: 12px;color: #ff6600;width: 300px;background-color: #FFFCCE;z-index: 100;cursor: default;top:70px;left:55px}
		</style>
		<script type="text/javascript">
		
	   function t(w) {
       		 if (w.parent && w.parent != w) {
			return t(w.parent);
        	}
        	return w;
    	      }
    	var _TopW = t(window);
    	if(_TopW != window){
    		_TopW.location.href= '${ctx}/main';
    	}
    
		     $(document).ready(function(){
     		     if (navigator.userAgent.toLowerCase().indexOf("chrome") >= 0) {
					  $(window).load(function(){
					    $('input:-webkit-autofill').each(function(){
					      var text = $(this).val();
					      var name = $(this).attr('name');
					      $(this).after(this.outerHTML).remove();
					      $('input[name=' + name + ']').val(text);
					    });
					  });
				 }

		    	 $("#subimg").click(function(){
		    		 on_check_login_form();
		    		 return false;
		    	 });
		     });
		     
		     $(document).keydown(function(e){
		    	 if(e.which == 13 ){
		    	     on_check_login_form();
		    	     return false;
		    	 }
		     });
             
		     function on_check_login_form(){
		    	 if($("#username").val() == ""){
		    		 alert("用户名不能为空");
		    		 $("#username").focus();
		    		 return false;
		    	 }
		    	 if($("#password").val() == ""){
		    		 alert("密码不能为空");
		    		 $("#password").focus();
		    		 return false;
		    	 }
		    	 $('#loginForm').submit();
		    	 return true;
		     }
		</script>
	</head>
	<body class="dlbody">
		<div class="fs_logo"></div>
		<div class="login"> 
		    
		    <div class="login_c">
		   		<div class="login_v">标准版</div>
		    	
		    	<form id="loginForm" action="${ctx}/login" method="post" autocomplete="off">
		 			<div class="login_user"><span>用户名：</span><input name="username" id="username" type="text" value="${username}"/></div>
		      		<div class="login_mm"><span>密　码：</span><input name="password" id="password" type="password" /></div>
				<!--如果是指纹登录，先取userid-->
				<input name="userid" id="userid" type="hidden" value=""/>
		       	</form>
				
				<div class="login_dl">
					<input class="login_btn" value="登 录" id="subimg" type="button">
			       	<%
					AuthenticationException error =  (AuthenticationException)  request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
					if(error != null) { 
					%>
						<div class="error-div tip">  
					<%
							if(error instanceof DisabledAccountException) {
								out.print("用户已被屏蔽，请登录其他用户！");
							}else if(error instanceof IncorrectCredentialsException) {
								out.print("密码输入错误！");
							}else {
								out.print(error.getMessage());
							}
					%>		
						</div>
					<%
					}
					%>
		 		</div>
		 		
		    </div>
	  </div>
	  
	</body>
</html>