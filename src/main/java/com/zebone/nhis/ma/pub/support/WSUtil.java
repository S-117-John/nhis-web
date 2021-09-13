package com.zebone.nhis.ma.pub.support;

import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;


public class WSUtil {
	private static int connectionTimeout = 10*1000;
	private static int receiveTimeout = 60*1000;;

	public static Object invoke(String servicePath,String methodName,Object...param) throws Exception {
		return invoke(connectionTimeout,receiveTimeout,servicePath,methodName,param);
	}

	public static Object invoke(int connectionTimeout,int receiveTimeout,String servicePath,String methodName,Object...param) throws Exception {
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		org.apache.cxf.endpoint.Client client=dcf.createClient(servicePath);
		HTTPConduit conduit = (HTTPConduit) client.getConduit();
		HTTPClientPolicy policy = new HTTPClientPolicy();
		policy.setConnectionTimeout(connectionTimeout);
		policy.setReceiveTimeout(receiveTimeout);
		conduit.setClient(policy);
		return client.invoke(methodName,param);
	}

	/**
	 * 调用示例
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception{
		String url = "http://localhost:8080/HISGL/static/webservice/SyxNHISWebService?wsdl";
		String param = "入参";
		try {
			WSUtil.invoke(url, "acceptUserAddrInfo",param);
		} catch (Exception e){
			e.printStackTrace();
		}

	}
}
