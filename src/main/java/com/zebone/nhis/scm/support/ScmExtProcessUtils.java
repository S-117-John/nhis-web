package com.zebone.nhis.scm.support;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.scm.pub.service.IScmService;
import com.zebone.platform.modules.core.spring.ServiceLocator;

/**
 * 供应链对外接口服务
 * @author yangxue
 *
 */
public class ScmExtProcessUtils {
	/**
	 * 住院对外发药接口
	 * @param blCgPubParamVos
	 */
	public static Map<String,Object> processExtIpDe(List<ExPdApplyDetail> exPdAppDetails,String param){
		
		//获取是否启用自定义记费服务标志 对应配置文件cg.properties配置文件
		if ("true".equals(ApplicationUtils.getPropertyValue("scm.ipde.ext.enable", ""))) {
			//获取自定义记费服务service，对应配置文件cg.properties配置文件
			String processClass = ApplicationUtils.getPropertyValue("scm.ipde.ext.processClass", "");
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				IScmService handler = (IScmService) bean;
				handler.processExtIpDe(exPdAppDetails,param);
			}
		}
		
		return null;
	}
	
	/**
	 * 门诊对外发药接口
	 * @param blCgPubParamVos
	 */
	public static Map<String,Object> processExtOpDe(ExPresOcc exPresOcc,Map<String,Object> paramMap){
		
		//获取是否启用自定义记费服务标志 对应配置文件cg.properties配置文件
		if ("true".equals(ApplicationUtils.getPropertyValue("scm.ipde.ext.enable", ""))) {
			//获取自定义记费服务service，对应配置文件cg.properties配置文件
			String processClass = ApplicationUtils.getPropertyValue("scm.ipde.ext.processClass", "");
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				IScmService handler = (IScmService) bean;
				handler.processExOpDe(exPresOcc,paramMap);
			}
		}
		
		return null;
	}
	
	
}
