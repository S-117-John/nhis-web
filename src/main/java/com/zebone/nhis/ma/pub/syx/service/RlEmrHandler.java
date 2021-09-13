package com.zebone.nhis.ma.pub.syx.service;

import java.util.Map;

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
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class RlEmrHandler {

	protected final static RequestConfig defaultRequestConfig = RequestConfig.custom()
	            .setConnectionRequestTimeout(100000).setConnectTimeout(100000)
	            .setSocketTimeout(100000).build();
	
	private String getTokenUrl = "";
	private String getLoginTokenUrl = "";
	private String requestid = "";
	private String requestip = "";
	private String systemcode = "";
	private String systempassword = "";
	
	public String getRlEmrUrl(String param,IUser user) throws Exception{
		Map map = JsonUtil.readValue(param, Map.class);
		if(map==null||map.get("codeIp")==null) return null;
		
		String codeIp=map.get("codeIp").toString();
		User u = (User) user;
		//1.取的平台的token串
		String accessToken = getAccessToken();
		String loginToken = getLoginToken(accessToken, u.getCodeEmp());

		String url=ApplicationUtils.getPropertyValue("emr.rul.url", "");
		if(StringUtils.isEmpty(url)) return "";
		url=url+"iPSeqNo="+codeIp+"&state=1&loginToken="+loginToken;
		
		return url;
	}	

	@SuppressWarnings("static-access")
	public String getAccessToken(){
		String accessToken = "";
		getTokenUrl = ApplicationUtils.getPropertyValue("emr.rl.getTokenUrl", "-1");
		requestid = ApplicationUtils.getPropertyValue("emr.rl.requestid", "-1");
		requestip = ApplicationUtils.getPropertyValue("emr.rl.requestip", "-1");
		systemcode = ApplicationUtils.getPropertyValue("emr.rl.systemcode", "-1");
		systempassword = ApplicationUtils.getPropertyValue("emr.rl.systempassword", "-1");
    	String bodyString = "<GETTOKENREQUEST><REQUESTID>"+requestid+"</REQUESTID><REQUESTIP>"+requestip+"</REQUESTIP><SYSTEMCODE>"+systemcode+"</SYSTEMCODE><SYSTEMPASSWORD>"+systempassword+"</SYSTEMPASSWORD></GETTOKENREQUEST>";
    	String tonkenXml = this.post(getTokenUrl, bodyString,ContentType.create("text/plain", "UTF-8"));  
    	accessToken = StringUtils.substringBetween(tonkenXml, "<ACCESSTOKEN>", "</ACCESSTOKEN>");
	    return accessToken;
	}
	
	//83836
	public String getLoginToken(String accessToken,String empCode){
		String loginToken = "";
		getLoginTokenUrl = ApplicationUtils.getPropertyValue("emr.rl.getLoginTokenUrl", "-1");
		requestid = ApplicationUtils.getPropertyValue("emr.rl.requestid", "-1");
		systemcode = ApplicationUtils.getPropertyValue("emr.rl.systemcode", "-1");
    	String bodyString = "<GETLOGINTOKENREQUEST><REQUESTID>"+requestid+"</REQUESTID><ACCESSTOKEN>"+accessToken+"</ACCESSTOKEN ><LOGINSYSTEMCODE>"+systemcode+"</LOGINSYSTEMCODE><LOGINUSER>"+empCode+"</LOGINUSER></GETLOGINTOKENREQUEST>";
    	String tonkenXml = this.post(getLoginTokenUrl, bodyString,ContentType.create("text/plain", "UTF-8"));  
    	loginToken = StringUtils.substringBetween(tonkenXml, "<LOGINTOKEN>", "</LOGINTOKEN>");
	    return loginToken;
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

}
