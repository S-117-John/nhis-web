package com.zebone.nhis.ma.pub.support;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.service.IExt2HipSystemService;
import com.zebone.nhis.ma.pub.service.IExtSystemService;
import com.zebone.platform.modules.core.spring.ServiceLocator;

import java.util.Map;

/**
 * 外部系统对接服务
 * @author yangxue
 *
 */
public class ExtSystemProcessUtils {
	/**
	 * 处理外部系统对接业务入口
	 * @param systemName 处理的业务系统，例如：PACS，LIS 等
	 * @param methodName 调用具体处理业务类中的具体方法
	 * @param args 调用方法时需要传入的参数列表
	 * @return
	 */
	public static Object processExtMethod(String systemName,String methodName,Object...args){
		Object result = null;
		//获取是否启用自定义记费服务标志 对应配置文件ext.properties配置文件
		if ("true".equals(ApplicationUtils.getPropertyValue("ext.system.enable", ""))) {
			//获取自定义记费服务service，对应配置文件ext.properties配置文件
			String processClass = ApplicationUtils.getPropertyValue("ext.system.processClass", "ExtSystemPubHandler");
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				IExtSystemService handler = (IExtSystemService) bean;
				result = handler.processExtSystem(systemName,methodName,args);
			}
		}
		return result;
	}
	
	public static Object processExt2HipMethod(String methodName,Map<String,Object> paramMap){
		Object result = null;
		//获取是否启用自定义记费服务标志 对应配置文件ext.properties配置文件
		if ("true".equals(ApplicationUtils.getPropertyValue("ext.hipSystem.enable", ""))) {
			//获取自定义记费服务service，对应配置文件ext.properties配置文件
			String processClass = ApplicationUtils.getPropertyValue("ext.hipSystem.processClass", "");
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				IExt2HipSystemService handler = (IExt2HipSystemService) bean;
				result = handler.processExtSystem(methodName, paramMap);
			}
		}
		return result;
	}
	
}
