package com.zebone.platform.framework.controller;

import com.zebone.nhis.common.module.scm.pub.BdStore;
import com.zebone.platform.common.service.UserService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.security.redis.RedisSessionDAO;
import com.zebone.platform.framework.security.shiro.MutiUsernamePasswordToken;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.framework.support.holder.SecurityHolder;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.LoadDataSource;
import com.zebone.platform.modules.dao.jdbc.entity.DataBaseEntity;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class FrameController {
	
	@Autowired
    UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		SecurityHolder.logout();
		return "login";
	}
	
	
	public void saveSession(Session s ){
		 Object dao = ServiceLocator.getInstance().getBean("sessionDAO");
		 if(dao != null && dao instanceof RedisSessionDAO){
			 SessionDAO sessiondao = (RedisSessionDAO) dao;
			 Session session = sessiondao.readSession(s.getId());
			 session.setAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY,  SecurityUtils.getSubject().getPrincipals());
			 sessiondao.update(session); 
		 }
		 
	} 
	
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/loginnet")
    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String param = request.getParameter("param");
		Map<String, String> mapParam = JsonUtil.readValue(param, HashMap.class);

		ResponseJson respJson = new ResponseJson();
		String respString = null;
		MutiUsernamePasswordToken token = null;
		User user = null;
		try {
	
			String username = mapParam.get("username") == null ? "" : mapParam.get("username");
			String password  = mapParam.get("password") == null ? "" : mapParam.get("password");
			//前台传入的密码base64方式解密
			Base64 base64 = new Base64();
			if(CommonUtils.isNotEmpty(password)){
				try {
					password = new String(base64.decode(password), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		    token = new MutiUsernamePasswordToken(username, password.toCharArray(), false,"");
			Map tempMap = new HashMap();
			String caid=null;
		    //CA证书号
		    if(mapParam.containsKey("caid")){
		    	caid=(String)mapParam.get("caid");
		    	if(!StringUtils.isBlank(caid)){
		    		IUser usr = userService.findUserByCa(request, response);
					if(usr != null){
						 tempMap.put("isCaLogin", "1");
						 token.setUsername(usr.getLoginName());
					}
		    	}
			}
		    //单点登陆
		    if(mapParam.containsKey("isSso")){
		    	String isSso=mapParam.get("isSso");
		    	if(!StringUtils.isBlank(isSso)&&"1".equals(isSso)){
		    		IUser usr = userService.finduserbycode(request, response);
					if(usr != null){
						tempMap.put("isCaLogin", "1");
						token.setUsername(usr.getLoginName());
					}
		    	}
			}

			token.setTokenMap(tempMap);
			//
			SecurityUtils.getSubject().getSession().setAttribute("SHIRO_USERNAME", username);
		    SecurityUtils.getSubject().login(token);
		    
		    user = (User) SecurityUtils.getSubject().getPrincipal();
		    user.setCaid(caid);	
		    DataBaseEntity dbe= LoadDataSource.getDataConfig("default");
		    String url=dbe.getUrl();
		    String datebase="";
		    String ip="";
		    int i;
		    String[] temp;
		    String str;
		    if(url!=null){
		    	try {
		    		if(url.indexOf("jdbc:oracle:thin")>=0){
			    		temp = url.split(":");
			    		if(temp!=null&&temp.length>=4){
			    			ip=temp[3];
			    			if(ip!=null&&ip.indexOf("@")>=0){
			    				ip=ip.replace("@", "");
			    			}
			    		}
			    		if(temp!=null&&temp.length>=6){
			    			datebase=temp[5];
			    		}
			    		datebase=datebase+" - "+dbe.getUsername();
			    	}else if(url.indexOf("jdbc:sqlserver")>=0){
			            temp = url.split(";");
			            for (i = 0; i < temp.length; i++) {
			                str = temp[i];
			                if(i==0){
			                	String[] dbs = str.split(":");
			                	if(dbs!=null&&dbs.length>=3){
			                		ip=dbs[2];
			                		if(ip!=null&&ip.indexOf("//")>=0){
			                			ip=ip.replace("//", "");
			                		}
			                	}
			                }else{
			                	String[] props=str.split("=");
			                	if(props!=null&&props.length>=2){
			                		if(props[0]!=null&&props[0].equals("database")){
			                			datebase=props[1];
			                			break;
			                		}
			                	}
			                }
			            }
			    	}
		    		user.setDbIp(ip);
		    		user.setDbName(datebase);
				} catch (Exception e) {
					datebase="NHIS";
				}
            }
		    userService.success(user, null);
		    this.saveSession(SecurityUtils.getSubject().getSession());
		    respJson.setData(user);
		}
		catch (AuthenticationException ae) {

			Map<String, String> descMap = userService.failure(token, ae);
		    
		    String message = ae.getClass().getSimpleName();
		
			if ("IncorrectCredentialsException".equals(message)) {
				respJson.setStatus(-90);
				respJson.setDesc(descMap.get("desc"));
			} else if ("UnknownAccountException".equals(message)) {
				respJson.setStatus(-91);
				respJson.setDesc("账号不存在"); 
			} else if ("LockedAccountException".equals(message)) {
				respJson.setStatus(-92);
				respJson.setDesc("账号被锁定");
			} else if ("CaptchaIncorrectException".equals(message)) {
				respJson.setStatus(-93);
				respJson.setDesc("验证码错误：");
			} else if ("DisabledAccountException".equals(message)) {
				respJson.setStatus(-95);
				respJson.setDesc("帐号被禁用");
			} else {
				respJson.setStatus(-94);
				//respJson.setDesc("异常：" + ae.getMessage());
				respJson.setDesc(ae.getMessage());
			}

		}
		catch (Exception e) {
		    respJson.setStatus(-94);
		    respJson.setDesc("异常：" + e.getMessage());
		}
	
		try {
		    respString = JsonUtil.writeValueAsString(respJson);
		    response.setContentType("application/json;charset=UTF-8");
		    if(user != null){
		    	user.setOpers(null);
		    	user.setDepts(null);
		    	this.saveSession(SecurityUtils.getSubject().getSession());
		    }
		    
		    response.getWriter().write(respString);
		} catch (IOException e) {
		    e.printStackTrace();
		}
    }

	

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login_fail(@RequestParam("username") String userName, Model model) {
		model.addAttribute("username", userName);
		return "login";
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main(HttpServletRequest request, ModelMap model) {
		Cookie[] cookies = request.getCookies();
		int length = cookies.length;
		String cookiestr = "";
		for(int i =0 ; i < length ; i++ ){
			Cookie cookie = cookies[i];
			cookiestr += cookie.getName()+"="+cookie.getValue()+"&";
			
		}
		model.put("cookiestr", cookiestr);
		return "main";
	}
	
	public  void render(HttpServletResponse response, String text,
                        String contentType) {
		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType(contentType);
			response.getWriter().write(text);
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "/sessionout")
	@ResponseBody
	public Map<String, Object> logout(HttpServletRequest request, final HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Subject subject = SecurityUtils.getSubject();
		User user = (User)subject.getPrincipal();
		if (subject != null && subject.getPrincipal() != null) {
			subject.logout();
			map.put("code", 1);
		}else{
			map.put("code", -1);
		}
		return map;
	}

	
	/**
	 * 设置默认科室
	 * @param param
	 * @param user
	 */
	@RequestMapping(value = "/set/default/dept" , method = RequestMethod.POST)
	@ResponseBody
	public ResponseJson setDefaultDept(HttpServletRequest request, HttpServletResponse response){
		
		ResponseJson respJson = new ResponseJson();
		String param = request.getParameter("param");
		Map<String, String> mapParam = JsonUtil.readValue(param, HashMap.class);
	
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		String pkdept = null;
		if(mapParam.get("pkDept") != null){
			pkdept = mapParam.get("pkDept").toString();
		}else{
			respJson.setStatus(-1);
			respJson.setDesc("部门主键不能为空！");
			return respJson;
		}
		
		String pkorg = null;
		if(mapParam.get("pkOrg") != null){
			pkorg = mapParam.get("pkOrg").toString();
			int len = pkorg.length();
			for(int i = 0 ; i < 32 - len ; i++){
				pkorg += " ";
			}
			
		}else{
			respJson.setStatus(-1);
			respJson.setDesc("机构主键不能为空！");
			return respJson;
		}	
		 user.setPkOrg(pkorg);
		 user.setPkDept(pkdept);
		
		//如果是仓库返回仓库信息
		List<BdStore> bddtores =  DataBaseHelper.queryForList("select * from bd_store where pk_dept = ? and  del_flag= '0' ", BdStore.class, pkdept);
		if(bddtores.size() > 0){
			user.setPkStore(bddtores.get(0).getPkStore());
		}else{
			user.setPkStore(null);
		}
		this.saveSession(SecurityUtils.getSubject().getSession());
		respJson.setData(bddtores);
		
		return respJson;
	}
	
	/**
	 * 设置默仓库
	 * @param param
	 * @param user
	 */
	@RequestMapping(value = "/set/default/store" , method = RequestMethod.POST)
	@ResponseBody
	public ResponseJson setDefaultStore(HttpServletRequest request, HttpServletResponse response){
		
		String param = request.getParameter("param");
		Map<String, String> mapParam = JsonUtil.readValue(param, HashMap.class);
		 ResponseJson respJson = new ResponseJson();
	
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		String pkStore = null;
		if(mapParam.get("pkStore") != null){
			pkStore = mapParam.get("pkStore").toString();
			user.setPkStore(pkStore);
		}else{
			respJson.setStatus(-1);
			respJson.setDesc("仓库主键不能为空！");
		}		
		this.saveSession(SecurityUtils.getSubject().getSession());
		return respJson;
	}
}