package com.zebone.nhis.pi.pub.support;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.modules.core.spring.ServiceLocator;

public class ClientUtils {

	// 新增
	public static Object addEmpi(PiMaster piMaster) {
		Object response = null;
		String compareClass = ApplicationUtils.getPropertyValue("empi.processClass", "");
		if ("true".equals(ApplicationUtils.getPropertyValue("empi.enable", ""))) {
			Object bean = ServiceLocator.getInstance().getBean(compareClass);
			if (bean != null) {
				EmpiClientInterFace handler = (EmpiClientInterFace) bean;
				response =  handler.addEmpi(piMaster);
				return response;
			}else return null;
		} else return null;
	}

	// 更新
	public static Object updateEmpi(PiMaster piMaster) {
		Object response = null;
		String compareClass = ApplicationUtils.getPropertyValue("empi.processClass", "");
		if ("true".equals(ApplicationUtils.getPropertyValue("empi.enable", ""))) {
			Object bean = ServiceLocator.getInstance().getBean(compareClass);
			if (bean != null) {
				EmpiClientInterFace handler = (EmpiClientInterFace) bean;
				response = handler.updateEmpi(piMaster);
				return response;
			}else return null;
		} else return null;
	}

	// 查询
	public static Object queryEmpi(PiMaster piMaster) {
		String compareClass = ApplicationUtils.getPropertyValue("empi.processClass", "");
		if ("true".equals(ApplicationUtils.getPropertyValue("empi.enable", ""))) {
			Object bean = ServiceLocator.getInstance().getBean(compareClass);
			if (bean != null) {
				EmpiClientInterFace handler = (EmpiClientInterFace) bean; 
				return handler.queryEmpi(piMaster);
			}else return null;
		} else return null;
	}
	
	// 交叉查询
		public static Object queryRelateEmpi(String code, String idtype,String cardNo) {
			Object response = null;
			String compareClass = ApplicationUtils.getPropertyValue("empi.processClass", "");
			if ("true".equals(ApplicationUtils.getPropertyValue("empi.enable", ""))) {
				Object bean = ServiceLocator.getInstance().getBean(compareClass);
				if (bean != null) {
					EmpiClientInterFace handler = (EmpiClientInterFace) bean;
					response =  handler.queryRelateEmpi(code,idtype,cardNo);
					return response;
				}else return null;
			} else return null;
		}
		
		//合并
		public static Object combineEmpi(String piMaster) {
			Object response = null;
			String compareClass = ApplicationUtils.getPropertyValue("empi.processClass", "");
			if ("true".equals(ApplicationUtils.getPropertyValue("empi.enable", ""))) {
				Object bean = ServiceLocator.getInstance().getBean(compareClass);
				if (bean != null) {
					EmpiClientInterFace handler = (EmpiClientInterFace) bean;
					response = handler.combineEmpi(piMaster);
					return response;
				}else return null;
			} else return null;
		}
		
}
