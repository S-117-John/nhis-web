package com.zebone.nhis.ma.pub.support;

import com.zebone.nhis.ma.pub.sd.vo.ReportInfo;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ReportUtil {

	/***
	 *  组装发送 检验、检查报告 xml
	 * @param str
	 * @return
	 */
	public static String reportXmlToString (String param) {
		Map<String, Object> qryMap = JsonUtil.readValue(param, Map.class);
		String reportType = (String) qryMap.get("reportType");//报告类型
		String orgCode = (String) qryMap.get("orgCode");//组织机构代码
		String idcardTypeCode = (String) qryMap.get("idcardTypeCode");//身份证件类别代码
		String idcard = (String) qryMap.get("idcard");//身份证号码
		String applySource = (String) qryMap.get("applySource");//请求来源
		String applyDeptCode = (String) qryMap.get("applyDeptCode");//调阅请求科室编码
		String applyDeptName = (String) qryMap.get("applyDeptName");//调阅请求科室名称
		String applyDoctorCode = (String) qryMap.get("applyDoctorCode");//调阅请求医生编码
		String applyDoctorName = (String) qryMap.get("applyDoctorName");//调阅请求医生名称
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<Document xmlns=\"http://www.casking.org/hisInfo\">");
		//安全认证信息 authorIDcode 每个单位会单进行独通知
		sb.append("<authorIDcode>"+"65a04a6254d3475aacbf038100aec4e4"+"</authorIDcode>");
		//填写推送方单位名称
		sb.append("<authorName>"+"深圳大学总医院"+"</authorName>");
		//主要参数
		sb.append("<requestParams>");
		//    <!---*报告类型：1、检验 2、检查-->
		sb.append("<reportType>"+reportType+"</reportType>");
		//<!---*组织机构代码-->
		sb.append("<orgCode>"+orgCode+"</orgCode>");
		//!---*身份证件类别代码 01:居民身份证;02:护照;03:港澳居民来往内地通行证;04:台湾居民来往大陆通行证-->
		sb.append("<idcardTypeCode>"+idcardTypeCode+"</idcardTypeCode>");
		//<!---*证件号码-->
		sb.append("<idcard>"+idcard+"</idcard>");
		// <!---*调阅请求来源 1:门急诊;2:住院-->
		sb.append("<applySource>"+applySource+"</applySource>");
		// <!---*调阅请求科室编码-->
		sb.append("<applyDeptCode>"+applyDeptCode+"</applyDeptCode>");
		//  <!---*调阅请求科室名称-->
		sb.append("<applyDeptName>"+applyDeptName+"</applyDeptName>");
		//  <!---*调阅请求医生编码-->
		sb.append("<applyDoctorCode>"+applyDoctorCode+"</applyDoctorCode>");
		//  <!---*调阅请求医生名称-->
		sb.append("<applyDoctorName>"+applyDoctorName+"</applyDoctorName>");
		sb.append("</requestParams>");
		sb.append("</Document>");
		return sb.toString();
	}
	
    /**
     * 组装发送消息报文
     * @param action
     * @param inputXml
     * @return
     */
	public static String getSoapXML(String action,String inputXml){
    	StringBuilder sbr = new StringBuilder("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.dataquery.ccmr.casking.com.cn/\"><soapenv:Header/><soapenv:Body><web:messageQueryServer>");
    	sbr.append("<action>");
    	sbr.append(action);
    	sbr.append("</action>");
    	sbr.append("<message><![CDATA[");
    	sbr.append(inputXml);
    	sbr.append("]]></message>");
    	sbr.append("</web:messageQueryServer></soapenv:Body></soapenv:Envelope>");
    	return sbr.toString();
    }
    
	/**
	 * 发送请求消息获取检验、检查报告
	 * @param action
	 * @param messageXml
	 * @return
	 */
    
	public static List<ReportInfo> sendHIPService(String action,String messageXml){
		String retStr = "";
		try {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		//请求连接
		HttpPost httpPost = new HttpPost("http://6.6.0.253:81/dataquery/ccmr/services/dataQueryWebService?wsdl");
        //  设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(700000)
               .setConnectionRequestTimeout(700000)
				.setConnectTimeout(700000).build();
		httpPost.setConfig(requestConfig);
		String reportXml = reportXmlToString(messageXml);
		String soapXML = getSoapXML(action,reportXml);
		//System.out.println("send>>>"+soapXML);
		StringEntity data = new StringEntity(soapXML,
				Charset.forName("UTF-8"));
		httpPost.setEntity(data);
		CloseableHttpResponse response;
		response = closeableHttpClient.execute(httpPost);
		HttpEntity httpEntity = response.getEntity();
		
		if (httpEntity != null) {
				// 打印响应内容
				retStr = EntityUtils.toString(httpEntity, "UTF-8").replace("&lt;", "<").replace("&gt;", ">");
				retStr = StringEscapeUtils.unescapeHtml4(retStr);
				//System.out.println("return>>>"+retStr);
			}
            response.close();
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error("exception in doPostSoap1_1", e);
		}
		String interceptionXml = interceptionXml(retStr,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>","</return>");
		List<ReportInfo> retrunReportInfo = retrunReportInfo(interceptionXml);
		return retrunReportInfo;
	}
	
	/**
	 * 截取字符串 截取为标准的xml格式文档
	 * @param xml 需要截取的xml
	 * @param beginString 开始位置
	 * @param endString 结束位置
	 * @return
	 */
	public static String interceptionXml(String xml, String beginString, String endString){
		try {
			int beginIndex = 0;
			int endIndex = 0;
			beginIndex = xml.indexOf(beginString);
			endIndex = xml.indexOf(endString);
			String substring = StringUtils.substring(xml,beginIndex,endIndex);
			return substring;
		}catch (Exception e){
			throw new RuntimeException();
		}
	}
	
	/**
	 * 组装报告信息
	 * @param xml
	 * @return
	 */
	public static List<ReportInfo> retrunReportInfo(String xml){
		 Document doc = null;
		 List<ReportInfo> reportList = new ArrayList<ReportInfo>();
		 try {
			doc = DocumentHelper.parseText(xml);
			Element rootElt = doc.getRootElement(); // 获取根节点
			String operationType = rootElt.element("returnMessage").element("operationType").getText();//获取节点值<!--*操作是否成功 1：成功 2：失败-->
			//查询成功存在外院报告，但是查询参数有误，返回失败信息
			if (operationType.equals("2")) {
				ReportInfo reportInfo = new ReportInfo();
				String errorCode = rootElt.element("returnMessage").element("errorCode").getText();
				String errorName = rootElt.element("returnMessage").element("errorName").getText();
				reportInfo.setOperationType(operationType);
				reportInfo.setErrorCode(errorCode);
				reportInfo.setErrorName(errorName);
				reportList.add(reportInfo);
				return reportList;
			}
			// 查询成功 返回报告信息 （会区分检验或者检查信息）
			String reportCount = rootElt.element("component").element("reportCount").getText();//获取节点 查询返回的报告列表数量
			Integer reportSize = Integer.valueOf(reportCount);
			if (operationType.equals("1")&& reportSize > 0) {
				Element element = rootElt.element("component").element("reportList").element("reportInfo");
				for (int i = 0; i < reportSize; i++) {
					ReportInfo reportInfo = new ReportInfo();
					String reportOrgName = element.element("reportOrgName").getText();//获取机构名称
					String reportDate = element.element("reportDate").getText();//获取时间
					String reportTitle = element.element("reportTitle").getText();//获取标题
					reportInfo.setOperationType(operationType);//
					reportInfo.setReportOrgName(reportOrgName);
					reportInfo.setReportDate(reportDate);
					reportInfo.setReportTitle(reportTitle);
					reportList.add(reportInfo);
				}
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return reportList;
	}
	
	/**
	 * 通过webservice 获取外院报告
	 * @param action 请求的方法
	 * @param messageXml 请求信息
	 * @return
	 */
	public static String getReportListInfo(String action,String messageXml){
		String retStr = "";
		try {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		//请求连接
		HttpPost httpPost = new HttpPost("http://6.6.0.253:81/dataquery/ccmr/services/dataQueryWebService?wsdl");
        //  设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(700000)
               .setConnectionRequestTimeout(700000)
				.setConnectTimeout(700000).build();
		httpPost.setConfig(requestConfig);
		String reportXml = reportXmlToString(messageXml);
		String soapXML = getSoapXML(action,reportXml);
		//System.out.println("send>>>"+soapXML);
		StringEntity data = new StringEntity(soapXML,
				Charset.forName("UTF-8"));
		httpPost.setEntity(data);
		CloseableHttpResponse response;
		response = closeableHttpClient.execute(httpPost);
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity != null) {
				// 打印响应内容
				retStr = EntityUtils.toString(httpEntity, "UTF-8").replace("&lt;", "<").replace("&gt;", ">");
				retStr = StringEscapeUtils.unescapeHtml4(retStr);
				//System.out.println("return>>>"+retStr);
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
	
	
	
	
}
