package com.zebone.platform.framework.controller;

import com.alibaba.dubbo.rpc.RpcException;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.platform.Application;
import com.zebone.platform.common.service.UserService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.framework.support.ZBRedisUtils;
import com.zebone.platform.framework.support.holder.SecurityHolder;
import com.zebone.platform.framework.vo.SysPushResponse;
import com.zebone.platform.framework.vo.SysResponseData;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.exception.JonException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.modules.utils.RenderUtils;
import com.zebone.platform.web.service.WebService;
import com.zebone.platform.web.support.DataOption;
import com.zebone.platform.web.support.DataSupport;
import com.zebone.platform.web.support.ResponseJson;
import com.zebone.rpc.Rpc;
import com.zebone.rpc.SqlRpc;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;


/**
 * 系统级控制器，用于项目级系统扩展
 * @author think
 *
 */

@Controller
public class SysSupportController {
	
	static Logger logger = LogManager.getLogger(SysSupportController.class.getName());
	
	@Autowired
    UserService userService;
	
//	@Resource
//    WebController webController;
	
	@Autowired
	private WebService webService;
	
	private SqlRpc sqlRpc;
	
	String logEnable ;
	
	@Value("#{applicationProperties['framework.type']}")
    private String frameworkType;  //1:表示普通  2:表示DUbbo
	
   protected final static RequestConfig defaultRequestConfig = RequestConfig.custom()
	            .setConnectionRequestTimeout(10000).setConnectTimeout(10000)
	            .setSocketTimeout(10000).build();

	/**
	 * 中山二院 通过tonken的方式直接 访问交易号接口
	 * @param transCode
	 * @param param
	 * @param request
	 * @param response
	 */
	 
    @RequestMapping(value = "/static/proxy/handle")
    public void handle(@RequestParam(required = false) String transCode,
                       @RequestParam(required = false) String param, HttpServletRequest request, HttpServletResponse response) {
    	   	 
    	System.out.println(request.getSession().getId());
    	this.setOrigin(request,response);
    	ResponseJson rjson  = new ResponseJson();
  
    	Map<String,Object> paramMap = getCommParam(param,request);
    	
    	String tonken = paramMap.get("accessToken")==null?"":paramMap.get("accessToken").toString();//request.getParameter("accessToken");
    	if(StringUtils.isEmpty(tonken)){
    		rjson.setStatus(-3);	
    		rjson.setDesc("参数中缺少tonken");
    		renderXml(response, rjson);
    		 //返回请求的结果
           // RenderUtils.renderJson(response, rjson);	
            return;
    	}
    	//从集成平台获取tonken 并验证 
    	if(checkTonken(tonken)){  // 验证通过
    	
    		String userCode = paramMap.get("userCodeZR")==null?"":paramMap.get("userCodeZR").toString();//request.getParameter("userCode");   
    		String deptCode = paramMap.get("deptCodeZR")==null?"":paramMap.get("deptCodeZR").toString();//request.getParameter("deptCode");
    		
    		if(!StringUtils.isEmpty(userCode) && !StringUtils.isEmpty(deptCode) )
    		{
	    		IUser user = RedisUtils.getCacheObj("zsey:user:"+userCode+deptCode, User.class); //  根据情况构建user对象
	
	    		if(user == null){
	    			try{
	    				user = userService.findUserByIdMaster(userCode, deptCode);
	    			}catch(BusException e){
	    				   rjson.setStatus(-6);
	    	       		   rjson.setDesc(e.getMessage());
	    	       		  //返回请求的结果
	    	       		  renderXml(response, rjson);
	    	             // RenderUtils.renderJson(response, rjson);	
	    	              return;
	    			}
	    			RedisUtils.setCacheObj("zsey:user:"+userCode+deptCode, user, 7200);
	    		}
	    		
	    		 SecurityHolder.setLocalUser((User)user);
	    	     handlePub(transCode, getParam(paramMap), request, response);
	    	     SecurityHolder.clearLocalUser();

    	   }else{
    		   rjson.setStatus(-5);
       		   rjson.setDesc("参数中用户名和当前科室不能为空！");
       		  //返回请求的结果
       		  renderXml(response, rjson);
             // RenderUtils.renderJson(response, rjson);	
    	   }
    		
    	}else{//验证失败
    		rjson.setStatus(-4);
    		rjson.setDesc("接口访问失败：accessToken验证失败！");
    		//返回请求的结果
    		renderXml(response, rjson);
        	//RenderUtils.renderJson(response, rjson);	
    	}
    	
    }
    
	private void renderXml(HttpServletResponse res, ResponseJson rjson){
    	SysPushResponse push = new SysPushResponse();
    	SysResponseData resp = new SysResponseData();
    	String jsonStr = JsonUtil.writeValueAsString(rjson.getData());
    	resp.setRjson(jsonStr);
    	resp.setReturncode(rjson.getStatus()<0?"000601":"000000"); //000601 推送数据有误
    	resp.setReturnmsg(rjson.getDesc());
    	push.setResponseData(resp);
    	String xml = XmlUtil.beanToXml(push, SysPushResponse.class);
    	RenderUtils.renderXml(res, xml);
    }
    	
    
    private Map<String,Object> getCommParam(String param, HttpServletRequest request){
		if(param == null){
			BufferedReader br =null;
			try {
				br = request.getReader();  
				 
		        String line = null;  
	            StringBuilder sb = new StringBuilder();  
	            while ((line = br.readLine()) != null) {  
	                sb.append(line);  
	            }  
	            param = sb.toString();
	            param = param.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
	            param = param.replaceAll("\\+", "%2B");
	    		
	            param=URLDecoder.decode(param, "utf-8");
				if(param.startsWith("param=")){
					param = param.substring(6);
				}
				br.close();

			} catch (Exception e) { if(br != null)
					try {
						br.close();
					} catch (IOException e1) {
				}
			}
		}
		Map<String,Object> map = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>() {});
		return map;
    }
    public String getParam(Map<String,Object> map){
    	map.remove("userCodeZR");
 	    map.remove("deptCodeZR");
 	    map.remove("accessToken");
 	    String newParam = JsonUtil.writeValueAsString(map);
 	    return newParam;
    }
    /**
     * 验证tonken
     * 
     * 为了提供访问效率，其实不用每次都需要从集成平台获取tonken进行比对，只要票据的有效周期内比对一次即可
     * 
     */
    @SuppressWarnings("static-access")
	private boolean checkTonken(String Tonken){
    	
    	Object oTonken = RedisUtils.getCacheObj("zsey:accessToken:"+Tonken);
    	if(oTonken == null){
    		
    		String rpctonken = "";
    		//rpctonken 为从集成平台获取的tonken
    		//<1> REQUESTID:UUID <2> REQUESTIP:第三方系统所在服务器的IP地址 <3> SYSTEMCODE:第三方系统编码 <4> 第三方系统请求密码
    	//	String bodyString = "<GETTOKENREQUEST><REQUESTID>1000000</REQUESTID><REQUESTIP>172.18.41.145</REQUESTIP><SYSTEMCODE>NEW_HIS_SYSTEM</SYSTEMCODE><SYSTEMPASSWORD>0</SYSTEMPASSWORD></GETTOKENREQUEST>";
   		  //  rpctonken = this.post("http://192.168.8.190:7001/hip/hipService/getToken", bodyString,ContentType.create("text/plain", "UTF-8"));  
   		 //  String tonkenXml = this.post("http://172.18.41.126:9090/hip/hipService/getToken", bodyString,ContentType.create("text/plain", "UTF-8"));  
   		//   rpctonken = StringUtils.substringBetween(tonkenXml, "<ACCESSTOKEN>", "</ACCESSTOKEN>");
    		if(Tonken.equals(rpctonken)){
    			RedisUtils.setCacheObj("zsey:accessToken:"+Tonken, "1", 7200); // 和集成平台的票据失效时间一致  单位为秒
    			return true;
    		}else{
    			return true;
    		}
    	}
    	
    	return true;
    }

    
     /**
      * 允许跨域进行访问
      * @param response
      */
	private void setOrigin(HttpServletRequest request, HttpServletResponse response){
		
		 String url = request.getHeader("Origin");
	     if (!StringUtils.isEmpty(url)) {
	            String val = response.getHeader("Access-Control-Allow-Origin");
	            if (StringUtils.isEmpty(val)) {
	                response.addHeader("Access-Control-Allow-Origin", url);
	                response.addHeader("Access-Control-Allow-Credentials", "true");
	            }
	            return;
	     }
	
	    response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
   }
    public static String post(String url, String content, ContentType contentType) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
        	
            HttpPost httpPost = new HttpPost(url);
            HttpEntity requestEntity = new StringEntity(content, contentType);
            httpPost.setEntity(requestEntity);
            httpPost.setConfig(RequestConfig.copy(defaultRequestConfig).build());
            
            httpResponse = httpClient.execute(httpPost);
          
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity resEntity = httpResponse.getEntity();
                result = EntityUtils.toString(resEntity, "UTF-8");
                HttpClientUtils.closeQuietly(httpResponse);
              
            } else {
                throw new ClientProtocolException("Unexpected response status: " + httpResponse.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            HttpClientUtils.closeQuietly(httpResponse);
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (Exception e) {
                }
            }
        }
        return result;
    }
    
	public void handlePub(@RequestParam(required = false) String transCode,
                          @RequestParam(required = false) String param, HttpServletRequest request, HttpServletResponse response) {
			
		ResponseJson rjson  = new ResponseJson();
		long startTime = 0;
		DataOption actionoption  = null;
		IUser user = SecurityHolder.getContext().user;
		Map tranmap = null;
		String rpcStr = null;
		
		if(param == null){
			BufferedReader br =null;
			try {
				br = request.getReader();  
				 
		        String line = null;  
	            StringBuilder sb = new StringBuilder();  
	            while ((line = br.readLine()) != null) {  
	                sb.append(line);  
	            }  
	            param = URLDecoder.decode(sb.toString());
				if(param.startsWith("param=")){
					param = param.substring(6);
				}
				br.close();

			} catch (Exception e) { if(br != null)
					try {
						br.close();
					} catch (IOException e1) {
				}
			
			
			}
		}
		
		try{
			if (StringUtils.isBlank(transCode)) {// 交易码是如果为空，返回错误信息给客户端
				rjson.setStatus(-1);
				rjson.setDesc("交易码不正确");
				renderXml(response, rjson);
				//RenderUtils.renderJson(response, rjson);
			}else{
				
				UserContext.setUser(user);
				
				Map p = null;
				tranmap = webService.getTrancode(transCode);
				String type = tranmap.get("type").toString().toLowerCase();
			    actionoption = new DataOption();
			    startTime = System.currentTimeMillis();
			    
				if(frameworkType != null && frameworkType.equals("2") ){	//--dubbo部署

					actionoption.setIsdubbo(true);
					if(type.equals("rpc")){
						actionoption.setType("java");
						String adress = tranmap.get("address").toString();
						int len = adress.lastIndexOf(".");
						String rpcname = adress.substring(0,adress.indexOf("/"));
						actionoption.setSql(rpcname);
						actionoption.setJclass(adress.substring(adress.indexOf("/")+1,len));
						actionoption.setJmethod(adress.substring(len + 1,adress.length()));
						

						Rpc rcp = (Rpc) ServiceLocator.getInstance().getBean(rpcname.toUpperCase());
						String str =  rcp.handel(actionoption, param, user);
						if(str.startsWith("\"temp:data:")){
							String key = str.replaceAll("\"","");
							str = ZBRedisUtils.getCacheObj(key).toString();
							ZBRedisUtils.delCache(key);
						}
						rpcStr = str;
						//RenderUtils.renderJsonText(response, str);
						ResponseJson result = JsonUtil.readValue(str, ResponseJson.class);
						renderXml(response, result);

					}else if(type.equals("sql")||type.equals("call")){
						
						if(sqlRpc ==null){
							sqlRpc = (SqlRpc) ServiceLocator.getInstance().getBean("sqlRpc");
						}
						
						actionoption.setType(type);
						actionoption.setSql( tranmap.get("address").toString());
	
						String str =   sqlRpc.handel(actionoption, param, user);
						if(str.startsWith("\"temp:data:")){
							String key = str.replaceAll("\"","");
							str = ZBRedisUtils.getCacheObj(key).toString();
							ZBRedisUtils.delCache(key);
						}
						rpcStr = str;
						//RenderUtils.renderJsonText(response, str);
						ResponseJson result = JsonUtil.readValue(str, ResponseJson.class);
						renderXml(response, result);
						
					}else if(type.equals("java")){ 
						actionoption.setIsdubbo(false);
						actionoption.setType("java");
						String adress = tranmap.get("address").toString();
						int len = adress.lastIndexOf(".");
						actionoption.setSql("java:"+adress);
						actionoption.setJclass(adress.substring(0,len));
						actionoption.setJmethod(adress.substring(len + 1,adress.length()));
						rjson =(ResponseJson) DataSupport.doData(actionoption, p,param);
						//RenderUtils.renderJson(response, rjson);
						renderXml(response, rjson);
						
					}else{
						
						if(sqlRpc ==null){
							sqlRpc = (SqlRpc) ServiceLocator.getInstance().getBean("sqlRpc");
						}
						actionoption.setType(type);
						String[] ads = tranmap.get("address").toString().split("##");
						actionoption.setJclass(ads[0]);
						if(ads.length == 2){
							actionoption.setSql(ads[1]);
						}
						String str =   sqlRpc.handel(actionoption, param, user);
						if(str.startsWith("\"temp:data:")){
							String key = str.replaceAll("\"","");
							str = ZBRedisUtils.getCacheObj(key).toString();
							ZBRedisUtils.delCache(key);
						}
						rpcStr = str;
						//RenderUtils.renderJsonText(response, str);
						ResponseJson result = JsonUtil.readValue(str, ResponseJson.class);
						renderXml(response, result);
						
					}
					
					
					
				}else{
					if(type.equals("rpc")){ 
						actionoption.setType("java");
						String adress = tranmap.get("address").toString();
						int len = adress.lastIndexOf(".");
						String rpcname = adress.substring(0,adress.indexOf("/"));
						actionoption.setSql(rpcname);
						actionoption.setJclass(adress.substring(adress.indexOf("/")+1,len));
						actionoption.setJmethod(adress.substring(len + 1,adress.length()));
					}else if(type.equals("java")){ 
						
						actionoption.setType("java");
						String adress = tranmap.get("address").toString();
						actionoption.setSql("java:"+adress);
						int len = adress.lastIndexOf(".");
						actionoption.setJclass(adress.substring(0,len));
						actionoption.setJmethod(adress.substring(len + 1,adress.length()));
					}else if(type.equals("sql")||type.equals("call")){ 
						p = JsonUtil.readValue(param, Map.class);
						actionoption.setType(type);
						actionoption.setSql(tranmap.get("address").toString());
					}else{
						p = JsonUtil.readValue(param, Map.class);
						actionoption.setType(type);
						actionoption.setJclass(tranmap.get("address").toString());
						
						if(tranmap.get("condition") != null){
							actionoption.setSql(tranmap.get("condition").toString());
						}
						
					}
					rjson =(ResponseJson) DataSupport.doData(actionoption, p,param);
					renderXml(response, rjson);
					//RenderUtils.renderJson(response, rjson);
				}
			}
		}
		catch(JonException e){
			logger.error(e);
			rjson.setStatus(-60);
			rjson.setDesc("参数格式不符合规范！");
		    renderXml(response, rjson);
			//RenderUtils.renderJson(response, rjson);
		}catch(RpcException e){
			if(e.isTimeout()){
				rjson.setStatus(-50);
				rjson.setDesc("服务超时,请联系管理员或者刷新数据!");
			}else if(e.isNetwork()){
				rjson.setStatus(-102);
				rjson.setDesc("网络异常,请联系管理员!");
			}else if(e.isForbidded()){
				rjson.setStatus(-50);
				rjson.setDesc("服务禁止访问,请联系管理员!");
			}else{
				rjson.setStatus(-50);
				rjson.setDesc("Dubbo服务异常！");
				rjson.setErrorMessage(e.getMessage()+"   "+ DataSupport.getErrorMessage(e));
			}
			logger.error(e);
			renderXml(response, rjson);
			//RenderUtils.renderJson(response, rjson);
		}
		catch(Exception e){
			logger.error(e);
			rjson.setStatus(-62);
			rjson.setDesc("交易码处理失败！");
			rjson.setErrorMessage(e.getMessage()+"   "+ DataSupport.getErrorMessage(e));
		    renderXml(response, rjson);
			//RenderUtils.renderJson(response, rjson);
		}
		
		if(logEnable == null){
			logEnable = Application.getApplicationSettings().getProperty("log.enable", "true");
		}
		
		if(actionoption != null && logEnable.equals("true")){		
			long  exectime  = System.currentTimeMillis() - startTime;
			tranmap.put("rpcRetrun", rpcStr);
			TrancodeRun run = new  TrancodeRun(webService,rjson,(User)user,param,actionoption,exectime,tranmap);
			run.start();
		}
		
	}
	
	class TrancodeRun extends Thread{
		
		private WebService webService;
		private ResponseJson rjson;
		private User user;
		private String param;
		private long exectime;
		private DataOption actionoption;
		private Map tranMap;
		
		
		public TrancodeRun(WebService webService , ResponseJson rjson , User user, String param, DataOption actionoption, long exectime, Map transMap){
			this.webService = webService;
			this.rjson  = rjson;
			this.user = user;
			this.param = param;
			this.actionoption = actionoption;
			this.exectime = exectime;
			this.tranMap = transMap;
		}
		
		public void run() {  
			try{
				webService.saveTrancodeLog(tranMap, rjson, user, param, actionoption, exectime);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
