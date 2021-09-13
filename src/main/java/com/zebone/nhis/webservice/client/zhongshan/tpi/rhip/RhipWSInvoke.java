package com.zebone.nhis.webservice.client.zhongshan.tpi.rhip;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.tpi.rhip.vo.IptAdmissionNote;
import com.zebone.platform.common.support.User;

/**
 * 区域平台WebService接口调用
 * @author chengjia
 *
 */
public class RhipWSInvoke {

//	public static void main(String[] args) throws java.lang.Exception {
//		// TODO Auto-generated method stub
//		/*代表 wsdl 的路径，这个数据会根实际而变*/
//		String address="http://192.168.0.75:8203/WebServiceEntry?wsdl";
//		String namespaceURL="http://ws.adapter.bsoft.com/";
//		String localPart="WebServiceEntryService";
//		URL url=new URL(address);
//		QName qname=new QName(namespaceURL,localPart);
//		Service wsService=Service.create(url,qname);
//		System.out.println(wsService);
//		String[] params={"1","2012-10-17 00:00:00"};
//		//String result=getResult(wsService,"test","test123","esb.serviceInvokeStat","getStatByHours",params);
//		//transportData
//		User user=new User();
//		IptAdmissionNote note=new IptAdmissionNote();
//		PvEncounter pv=new PvEncounter();
//		PiMaster pi=new PiMaster();
//		String xml="";//XmlGenUtils.create(user, pi, pv, note);
//		System.out.println("xml:"+xml);
//		String result=getResult(wsService,"test_new","test_new123","adapter.transportService","transportDataByXmlContent",params);
//		http://192.168.0.75:8203/WebServiceEntry?wsdl
//		System.out.println("result:"+result);
//	}
	
	public static String execute(String xml) throws java.lang.Exception {
		// TODO Auto-generated method stub
		/*代表 wsdl 的路径，这个数据会根实际而变*/
		String address=ApplicationUtils.getPropertyValue("rhip.url", "");//"http://192.168.0.75:8203/WebServiceEntry?wsdl";
		String namespaceURL=ApplicationUtils.getPropertyValue("rhip.namespaceURL", "");//"http://ws.adapter.bsoft.com/";rhip.namespace
		String localPart=ApplicationUtils.getPropertyValue("rhip.localPart", "");//"WebServiceEntryService";
		URL url=new URL(address);
		QName qname=new QName(namespaceURL,localPart);
		Service wsService=Service.create(url,qname);
		//System.out.println(xml);
		String[] params={xml};
		//String result=getResult(wsService,"test","test123","esb.serviceInvokeStat","getStatByHours",params);
		//transportData
		
		//String xml=XmlGenUtils.create(user, pi, pv, note);
		//System.out.println("xml:"+xml);
		String appId=ApplicationUtils.getPropertyValue("rhip.appId", "");//test_new
		String pwd=ApplicationUtils.getPropertyValue("rhip.pwd", "");//test_new123
		String service=ApplicationUtils.getPropertyValue("rhip.service", "");//adapter.transportService
		String method=ApplicationUtils.getPropertyValue("rhip.method", "");//"transportDataByXmlContent"
		String result=getResult(wsService,appId,pwd,service,method,params);
		//http://192.168.0.75:8203/WebServiceEntry?wsdl
		//System.out.println("result:"+result);
		
		return result;
	}
	
	public static String getResult(Service webService,String appId,String pwd,String service,String method,String[] params){
		String result="";
		
		if(appId.isEmpty()||pwd.isEmpty()||service.isEmpty()||method.isEmpty()||params.toString().isEmpty()){
			result="调用方法出错，请检查输入信息！";
			return result;
		}
		WebServiceEntry webServiceEntry=webService.getPort(WebServiceEntry.class);
		StringArray sArray=new StringArray();
		Collection c=new ArrayList<>();
		List list=Arrays.asList(params);
		
		//sArray=ollections.addAll(list);
		
		sArray.getItem().addAll(list);
		try {
			result=webServiceEntry.invoke(appId, pwd, service, method, sArray);
		} catch (Exception_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
