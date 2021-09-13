package com.zebone.nhis.pro.zsba.mz.pub.support;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.pub.service.IInvPrintService;
import com.zebone.nhis.bl.pub.vo.BlInvPrint;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.modules.core.spring.ServiceLocator;

/**
 * 发票打印服务（所有项目通过此公共服务设置发票打印内容）
 * @author yangxue
 */
public class ZsbaInvPrintProcessUtils {
	/**
	 * 处理打印门诊发票服务
	 * @param settlevo
	 * @param stVo
	 */
	public static BlInvPrint getIpInvPrintDataByPkSettle(String pkSettle){
		//获取发票打印处理service，对应配置文件inv.properties配置文件
		String processClass = ApplicationUtils.getPropertyValue("inv.processClass", "");
		//获取是否启用发票打印标志
		BlInvPrint printinfo = null;
		if ("true".equals(ApplicationUtils.getPropertyValue("inv.enable", ""))) {
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				IInvPrintService handler = (IInvPrintService) bean;
				printinfo = handler.getIpInvPrintDataByPkSettle(pkSettle);
				return printinfo;
			}else return null;
		}else	return null;
	}
	
	/**
	 * 处理保存门诊发票服务
	 * @param blSettle
	 * @param invoiceInfos
	 * @return
	 */
	public static Map<String,Object> saveOpInvInfo(BlSettle blSettle,List<InvoiceInfo> invoiceInfos){
		//获取发票打印处理service，对应配置文件inv.properties配置文件
		String processClass = ApplicationUtils.getPropertyValue("inv.processClass", "");
		//获取是否启用发票打印标志
		Map<String,Object> invInfo = null;
		if ("true".equals(ApplicationUtils.getPropertyValue("inv.enable", ""))) {
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				IInvPrintService handler = (IInvPrintService) bean;
				invInfo = handler.saveOpInvInfo(blSettle,invoiceInfos);
				return invInfo;
			}else return null;
		}else	return null;
	}
	
	/**
	 * 重打发票个性化服务
	 * @param param
	 * @return
	 */
	public static Map<String,Object> invRePrint(Map<String,Object> param){
		//获取发票打印处理service，对应配置文件inv.properties配置文件
		String processClass = ApplicationUtils.getPropertyValue("inv.processClass", "");
		//获取是否启用发票打印标志
		Map<String,Object> invInfo = null;
		if ("true".equals(ApplicationUtils.getPropertyValue("inv.enable", ""))) {
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				IInvPrintService handler = (IInvPrintService) bean;
				invInfo = handler.invRePrint(param);
				return invInfo;
			}else return null;
		}else	return null;
	}
}
