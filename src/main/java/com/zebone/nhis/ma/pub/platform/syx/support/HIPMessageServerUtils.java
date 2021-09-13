package com.zebone.nhis.ma.pub.platform.syx.support;

import java.nio.charset.Charset;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.syx.vo.HIPException;
import com.zebone.nhis.ma.pub.platform.syx.vo.Request;
import com.zebone.nhis.ma.pub.platform.syx.vo.Response;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;


public class HIPMessageServerUtils {
	
	private String httpPostAddr = ApplicationUtils.getPropertyValue("msg.address", "");
	
	private String httpPostAddrPrivate=ApplicationUtils.getPropertyValue("msg.address.syxPrivate", "");
	/**
	 * 发送集成平台消息
	 * @param action
	 * @param messageXml
	 * @return
	 */
	public String sendHIPService(String action,String messageXml){
    	String retStr = "";
    	try {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		//HttpPost httpPost = new HttpPost("http://192.168.8.234:9000/services/HIPMessageServerInterface");
		HttpPost httpPost = new HttpPost(this.httpPostAddr);
                //  设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(700000)
                .setConnectionRequestTimeout(700000)
				.setConnectTimeout(700000).build();
		httpPost.setConfig(requestConfig);

			httpPost.setHeader("Content-Type", "application/soap+xml;charset=UTF-8");
			//httpPost.setHeader("SOAPAction", soapAction);
			String soapXml = getSoapXML(action,messageXml);
			System.out.println(soapXml);
			StringEntity data = new StringEntity(soapXml,
					Charset.forName("UTF-8"));
			httpPost.setEntity(data);
			CloseableHttpResponse response = closeableHttpClient
					.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				// 打印响应内容
				retStr = EntityUtils.toString(httpEntity, "UTF-8");
				System.out.print(retStr);
			}
                response.close();
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error("exception in doPostSoap1_1", e);
		}
		return retStr;

    }
	
	/**
	 * 发送平台私有服务
	 * @param action
	 * @param messageXml
	 * @return
	 */
	public String sendPrivateHIPService(String action,String messageXml){
    	String retStr = "";
    	try {
			// 创建HttpClientBuilder
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			// HttpClient
			CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
			HttpPost httpPost = new HttpPost(this.httpPostAddrPrivate);
	        //设置请求和传输超时时间20s
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectionRequestTimeout(20000).setConnectTimeout(20000).build();
			httpPost.setConfig(requestConfig);
			httpPost.setHeader("Content-Type", "application/soap+xml;charset=UTF-8");
			String soapXml = getPrivateSoapXML(action,messageXml);
			StringEntity data = new StringEntity(soapXml, Charset.forName("UTF-8"));
			httpPost.setEntity(data);
			CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
		    HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				retStr = EntityUtils.toString(httpEntity, "UTF-8");
				retStr=getPrivateResponseXml(retStr);
			}
			// 释放资源
	       response.close();
		   closeableHttpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retStr;
    }
    
    /**
     * 组装发送消息报文
     * @param action
     * @param inputXml
     * @return
     */
    public  String getSoapXML(String action,String inputXml){
    	StringBuilder sbr = new StringBuilder("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:hl7-org:v3\"><soap:Header/><soap:Body><urn:HIPMessageServer>");
    	sbr.append("<action>");
    	sbr.append(action);
    	sbr.append("</action>");
    	sbr.append("<message><![CDATA[");
    	sbr.append(inputXml);
    	sbr.append("]]></message>");
    	sbr.append("</urn:HIPMessageServer></soap:Body></soap:Envelope>");
    	
		return sbr.toString();
    }
    
    /***
     * 组织私有发送请求报文
     * @param action
     * @param inputXml
     * @return
     */
    public  String getPrivateSoapXML(String action,String inputXml){
    	StringBuilder sbr = new StringBuilder("<?xml version=\"1.0\"?><soap:Envelope xmlns:urn=\"urn:hl7-org:v3\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\"><soap:Header/><soap:Body><urn:PrivateHIPMessageServer>");
    	sbr.append("<action>");
    	sbr.append(action);
    	sbr.append("</action>");
    	sbr.append("<message><![CDATA[");
    	inputXml=inputXml.substring(55,inputXml.length());
    	sbr.append(inputXml);
    	sbr.append("]]></message>");
    	sbr.append("</urn:PrivateHIPMessageServer> </soap:Body></soap:Envelope>");
		return sbr.toString();
    }
    
    /**
     * 将私有消息服务发送后的xml截取处理
     * @param outXml
     * @return
     */
    public String getPrivateResponseXml(String outXml){
    	//<?xml version="1.0" encoding="UTF-8"?><soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"><soapenv:Body>
    	int endLength=outXml.indexOf("</soapenv:Body></soapenv:Envelope>");
    	outXml=outXml.substring(126, endLength);
    	outXml=outXml.replace("&lt;", "<");
    	outXml=outXml.replace("&gt;", ">");
    	return outXml;
    }
    
    /**
     * 发送平台数据
     * @param request 请求对象
     * @param action 调用方法
     * @param resHead 响应头(根节点信息)
     * @param flagSave 是否保存发送信息
     */
    public void sendHIPMsg(Request request,String action,String resHead,boolean flagSave){
    	String msgContent="";
    	String errText="";
    	String msgType="";
    	String msgId="";
    	try {
			msgId=request.getId().getExtension();
			msgType=request.getReqHead();
			String reqXml=createSendxml(request, action);
			System.out.println(reqXml);
			msgContent=reqXml;
			String resxml=createConnection(reqXml);
			createResponeXml(resxml,resHead);
			if(flagSave){
				saveSysLogInfo("sender", msgContent, errText, msgType, msgId, "1");
			}
		} catch (HIPException e) {
			//记录失败数据并进行日志保存
			saveSysLogInfo(e.getSendType(),msgContent,e.getErrorText(),msgType,msgId,"0");
		}
    }
    
    /**
     * 组装请求xml
     * @param request
     * @param action
     * @return
     */
    private String createSendxml(Request request,String action){
    	String reqHead=request.getReqHead();
    	String reqXml=XmlProcessUtils.toRequestXml(request,reqHead);
    	reqXml=getSoapXML(action,reqXml);
    	return reqXml;
    }
    
    /**
     * 创建连接
     * @param action
     * @param messageXml
     * @return
     * @throws Exception
     */
    private String createConnection(String messageXml)throws HIPException{
		String retStr="";
		try {
			// 创建HttpClientBuilder
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			// HttpClient
			CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
			// HttpPost httpPost = new
			// HttpPost("http://192.168.8.234:9000/services/HIPMessageServerInterface");
			HttpPost httpPost = new HttpPost(this.httpPostAddr);
			// 设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(700000).setConnectionRequestTimeout(700000)
					.setConnectTimeout(700000).build();
			httpPost.setConfig(requestConfig);

			httpPost.setHeader("Content-Type", "application/soap+xml;charset=UTF-8");
			// httpPost.setHeader("SOAPAction", soapAction);
			StringEntity data = new StringEntity(messageXml, Charset.forName("UTF-8"));
			httpPost.setEntity(data);
			CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				// 打印响应内容
				retStr = EntityUtils.toString(httpEntity, "UTF-8");
				System.out.print(retStr);
			}
			response.close();
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e) {
			throw new HIPException("【"+e.getMessage()+"】","receive");
		} 
		return retStr;
    }
    
    /**
     * 创建响应对象
     * @param resxml
     * @param resHead
     * @return
     * @throws Exception
     */
    private Response createResponeXml(String resxml,String resHead)throws HIPException{
    	Response resp=null;
		try {
			resp = XmlProcessUtils.toResponseEntity(resxml, resHead);
		} catch (Exception e) {//因连接超时，导致节点无法匹配，触发的异常
			System.out.println(e.getMessage());
			throw new HIPException(resxml+"【"+e.getMessage()+"】","receive");
		}
    	if(resp!=null){
    		if("AE".equals(resp.getAcknowledgement().getTypeCode())){//消息发送失败时进行的处理
    			throw new HIPException(resxml,"receive");
    		}
    	}else{
    		throw new HIPException("未获取到响应数据","receive");
    	}
    	return resp;
    }

    /**
     * 保存平台消息日志数据
     * @param sendType 发送/接受
     * @param msgContent 请求数据
     * @param errText 错误信息
     * @param msgType 消息分类(各个业务分类【手术，检查，检验对应不同的消息头】)
     * @param msgId 消息id
     * @param msgStatus 0:失败;1:成功
     */
    private void saveSysLogInfo(String sendType,String msgContent,String errText,String msgType,String msgId,String msgStatus){
    	SysMsgRec rec = new SysMsgRec();
    	rec.setPkMsg(NHISUUID.getKeyId());
    	rec.setPkOrg(UserContext.getUser().getPkOrg());
 	   	rec.setMsgId(msgId);
 	   	rec.setMsgType(msgType);
 	   	rec.setTransType(sendType);
 	   	rec.setTransDate(new Date());
 	   	rec.setMsgContent(msgContent);
 	   	rec.setErrTxt(errText);
 	   	rec.setMsgStatus(msgStatus);//0:未发送，1:已发送
 	   	rec.setSysCode("HIS");
 	 	DataBaseHelper.insertBean(rec);
    }
    
    public String sendHIPServiceCusTomAdds(String action,String messageXml){
    	String retStr = "";
    	try {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		//HttpPost httpPost = new HttpPost("http://192.168.8.234:9000/services/HIPMessageServerInterface");
		HttpPost httpPost = new HttpPost("http://192.168.8.234:9004/services/MDMWebMessageServer?wsdl");
                //  设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(700000)
                .setConnectionRequestTimeout(700000)
				.setConnectTimeout(700000).build();
		httpPost.setConfig(requestConfig);

			httpPost.setHeader("Content-Type", "application/soap+xml;charset=UTF-8");
			//httpPost.setHeader("SOAPAction", soapAction);
			String soapXml = getSoapXMLByBd(action,messageXml);
			System.out.println(soapXml);
			StringEntity data = new StringEntity(soapXml,
					Charset.forName("UTF-8"));
			httpPost.setEntity(data);
			CloseableHttpResponse response = closeableHttpClient
					.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				// 打印响应内容
				retStr = EntityUtils.toString(httpEntity, "UTF-8");
				System.out.print(retStr);
			}
                response.close();
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error("exception in doPostSoap1_1", e);
		}
		return retStr;

    }
    
    /**
     * 组装发送消息报文
     * @param action
     * @param inputXml
     * @return
     */
    public  String getSoapXMLByBd(String action,String inputXml){
    	StringBuilder sbr = new StringBuilder("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:hl7-org:v3\"><soap:Header/><soap:Body><urn:MDMWebMessageServer>");
    	sbr.append("<action>");
    	sbr.append(action);
    	sbr.append("</action>");
    	sbr.append("<message><![CDATA[");
    	sbr.append(inputXml);
    	sbr.append("]]></message>");
    	sbr.append("</urn:MDMWebMessageServer></soap:Body></soap:Envelope>");
		return sbr.toString();
    }
    
}
