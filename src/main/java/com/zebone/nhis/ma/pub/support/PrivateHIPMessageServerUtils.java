package com.zebone.nhis.ma.pub.support;

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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.syx.vo.HIPException;
import com.zebone.nhis.ma.pub.syx.vo.Request;
import com.zebone.nhis.ma.pub.syx.vo.Response;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;


public class PrivateHIPMessageServerUtils {
	
	private String httpPostAddr = ApplicationUtils.getPropertyValue("msg.address", "");
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
		HttpPost httpPost = new HttpPost("http://192.168.8.234:9001/services/PrivateHIPMessageServer?wsdl");
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
     * 组装发送消息报文
     * @param action
     * @param inputXml
     * @return
     */
    public  String getSoapXML(String action,String inputXml){
    	StringBuilder sbr = new StringBuilder("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:hl7-org:v3\"><soap:Header/><soap:Body><urn:PrivateHIPMessageServer>");
    	sbr.append(" <action>");
    	sbr.append(action);
    	sbr.append("</action>");
    	sbr.append("<message><![CDATA[");
    	sbr.append(inputXml);
    	sbr.append("]]></message>");
    	sbr.append("</urn:PrivateHIPMessageServer></soap:Body></soap:Envelope>");
		return sbr.toString();
    }
    
    /**
     * 发送平台数据
     * @param request 请求对象
     * @param action 调用方法
     * @param resHead 响应头(根节点信息)
     * @param flagSave 是否保存发送信息
     */
    public Response sendHIPMsg(Request request,String action,boolean flagSave){
    	String msgContent="";
    	String errText="";
    	String msgType="";
    	String msgId="";
    	Response response = null;
    	try {
			msgId=request.getId();
			msgType=request.getActionId();
			String reqXml=createSendxml(request, action);
			System.out.println(reqXml);
			msgContent=reqXml;
			String resxml=createConnection(reqXml);
			System.err.println(resxml);
			response = createResponeXml(resxml);
			if(flagSave){
				//saveSysLogInfo("sender", msgContent, errText, msgType, msgId, "1");
			}
		} catch (HIPException e) {
			//记录失败数据并进行日志保存
			//saveSysLogInfo(e.getSendType(),msgContent,e.getErrorText(),msgType,msgId,"0");
		}
		return response;
    }
    
    /**
     * 组装请求xml
     * @param request
     * @param action
     * @return
     */
	private String createSendxml(Request request, String action) {
		String reqXml = toRequestXml(request);
		reqXml = getSoapXML(action, reqXml);
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
			HttpPost httpPost = new HttpPost("http://192.168.8.234:9001/services/PrivateHIPMessageServer?wsdl");
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
    private Response createResponeXml(String resxml)throws HIPException{
    	Response resp=null;
		try {
			resp = toResponseEntity(resxml);
		} catch (Exception e) {//因连接超时，导致节点无法匹配，触发的异常
			System.out.println(e.getMessage());
			throw new HIPException(resxml+"【"+e.getMessage()+"】","receive");
		}
    	if(resp!=null){
    		if(resp.getResult()!= null && "AE".equals(resp.getResult().getId())){//消息发送失败时进行的处理
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
    
    private Response toResponseEntity(String xmlInput){
		  XStream xs = new XStream();
		  xs.setMode(XStream.NO_REFERENCES);
		  xs.processAnnotations(new Class[]{Response.class});
		  String st="<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><soapenv:Body>";
		  String end="</soapenv:Body></soapenv:Envelope>";
		  xmlInput = xmlInput.substring(st.length(), xmlInput.length()-end.length());
		  return (Response)xs.fromXML(xmlInput);
	  }
    
	  /**
	   * Requset对象转换成request格式的xml
	   * @param r
	   * @return
	   */
	  private  String toRequestXml(Request r){
		  XStream xs = new XStream();
		  xs.setMode(XStream.NO_REFERENCES);
		  xs.processAnnotations(new Class[]{Request.class});
		  String str = xs.toXML(r);
		  return str;
	  }
	  
	  /**
	     * 发送平台数据(重载方法)
	     * @param request 请求对象
	     * @param action 调用方法
	     * @param resHead 响应头(根节点信息)
	     * @param flagSave 是否保存发送信息
	     * @param isNoNameCoder 是否对所有特殊字符都不转义 true:表示特殊字符都不转义 ，解决自动增加下划线的问题
	     */
	    public Response sendHIPMsg(Request request,String action,boolean flagSave, boolean isNoNameCoder ){
	    	if (isNoNameCoder == true) {
		    	String msgContent="";
		    	String errText="";
		    	String msgType="";
		    	String msgId="";
		    	Response response = null;
		    	try {
					msgId=request.getId();
					msgType=request.getActionId();
					String reqXml=toRequestXmlNoNameCoder(request,action);
					System.out.println(reqXml);
					msgContent=reqXml;
					String resxml=createConnection(reqXml);
					System.err.println(resxml);
					response = createResponeXml(resxml);
					if(flagSave){
						//saveSysLogInfo("sender", msgContent, errText, msgType, msgId, "1");
					}
				} catch (HIPException e) {
					//记录失败数据并进行日志保存
					//saveSysLogInfo(e.getSendType(),msgContent,e.getErrorText(),msgType,msgId,"0");
				}
				return response;
			}else {
				Response sendHIPMsg = sendHIPMsg(request,action,flagSave);
				return sendHIPMsg;
			}
	    	
	    }
	  /**
	   * Requset对象转换成request格式的xml
	   * 使用NoNameCoder(),对全部属性不修改，
	   * 避免对象转XML的时候，对象属性名带有下划线的转换成XML的时候会变成两个下划线
	   * wangwx
	   * @param r
	   * @param action请求id
	   * @return
	   */
	  private String toRequestXmlNoNameCoder(Request r, String action ){
		  XStream xs = new XStream(new Xpp3Driver(new NoNameCoder()));//使用NoNameCoder(),对全部属性不修改
		 // XStream xs = new XStream(new DomDriver(null,new XmlFriendlyNameCoder("_-", "_")));
		  xs.setMode(XStream.NO_REFERENCES);
		  xs.processAnnotations(new Class[]{Request.class});
		  String reqXml = xs.toXML(r);
		  reqXml=getSoapXML(action,reqXml);
		  return reqXml;
	  }
	  
}
