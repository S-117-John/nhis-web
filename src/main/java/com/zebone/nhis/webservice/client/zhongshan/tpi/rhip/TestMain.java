package com.zebone.nhis.webservice.client.zhongshan.tpi.rhip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class TestMain {
	//前置机adapter中system配置的IP地址
	static String urlStr = "http://192.168.0.75:8203/WebServiceEntry?wsdl";
	static String qNameStr = "WebServiceEntryService";
	static String baseURL = "http://ws.adapter.bsoft.com/";
	static String appId = "test_new";
	static String pwd = "test_new123";
	static String serviceName = "adapter.transportService";
	static String method = "transportDataByXmlContent";
	static StringArray params = new StringArray();
	
	public static void main(String[] args) {
		int i=0;
		switch (i) {
		case 0:
			//验证adapter
			getADAPTER();
			break;
//		case 1:
//			//验证App
//			getAPP();
//			break;
//		case 2:
//			//调用ehrview
//			getEhrView();
//			break;
//		case 3:
//			//得到mpiid
//			getMpiid();
//			break;
//		case 4://调用测试的ehrview接口
//			testMy();
//			break;
//		case 5://测试本地ehrview
//			break;
//		case 6://测试云平台
//			testYUN();
//			break;
		}
	}
	
	
	
	private static void getADAPTER(){
		String xml = getXMLStr();
		params.getItem().add(xml);
		System.out.println(xml);
		String result = getReSult(appId, pwd, serviceName, method, params);
		System.out.println("result---->"+result);
	}
	
	
	
	public static String getReSult(String appId, String pwd, String service, String method, StringArray params){
		String result = "";
		if(appId.isEmpty()||pwd.isEmpty()||service.isEmpty()||method.isEmpty()
				||params.toString().isEmpty()){
			result = "调用方法出错，请检查输入信息！";
			return result;
		}
		
		try {
			URL url = new URL(urlStr);
			QName qname = new QName(baseURL, qNameStr);
			Service webService = Service.create(url, qname);
			WebServiceEntry s = webService.getPort(WebServiceEntry.class);
			result = s.invoke(appId, pwd, service, method, params);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception_Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	private static String getXMLStr(){
        StringBuffer strBuffer = new StringBuffer();
		File file = new File("D:\\ii.txt");
		InputStream inputStream = null;
		InputStreamReader inputReader = null;
        BufferedReader bufferReader = null;
        try{
            inputStream = new FileInputStream(file);
            inputReader = new InputStreamReader(inputStream, "GBK");
            bufferReader = new BufferedReader(inputReader);
             
            // 读取一行
            String line = null;
                 
            while ((line = bufferReader.readLine()) != null){
                strBuffer.append(line);
            } 
        }catch (IOException e){
        	e.printStackTrace();
        }finally{
        	try {
				bufferReader.close();
	        	inputReader.close();
	        	inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		
		return strBuffer.toString();
	}

}
