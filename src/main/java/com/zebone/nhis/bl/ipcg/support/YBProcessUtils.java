package com.zebone.nhis.bl.ipcg.support;


import com.zebone.nhis.bl.pub.service.IYBSettleService;
import com.zebone.nhis.bl.pub.vo.SettleInfo;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.modules.core.spring.ServiceLocator;

public class YBProcessUtils {
	/**
	 * 处理医保服务
	 * @param settlevo
	 * @param stVo
	 */
	public static void dealYBSettleMethod(SettleInfo settlevo,BlSettle stVo){
		//获取医保处理service，对应配置文件yb.properties配置文件
		String processClass = ApplicationUtils.getPropertyValue("yb.processClass", "");
		//获取是否启用医保标志
		if ("true".equals(ApplicationUtils.getPropertyValue("yb.enable", ""))) {
			Object bean = ServiceLocator.getInstance().getBean(processClass);
			if (bean != null) {
				IYBSettleService handler = (IYBSettleService) bean;
				handler.dealYBSettleMethod(settlevo, stVo);
			}
		} 
	}
}
