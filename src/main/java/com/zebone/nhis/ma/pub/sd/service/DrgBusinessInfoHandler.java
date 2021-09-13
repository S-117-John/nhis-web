package com.zebone.nhis.ma.pub.sd.service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.modules.exception.BusException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * DRG信息平台业务数据系统业务处理服务(无事物)
 * @author yangxue
 *
 */
@Service
public class DrgBusinessInfoHandler {
	
	@Autowired
	private DrgBusinessInfoService drgSysService;

	/**
	 * 业务处理方法转换器
	 * @param methodName
	 * @param args
	 */
    public Object invokeMethod(String methodName,Object...args){
    	if(StringUtils.isEmpty(ApplicationUtils.getPropertyValue("DRG.APP_ID", ""))){
    		throw new BusException("APP_ID为空请检查。");
    	}
    	Object result = null;
    	switch(methodName){
    	case "MinData":
    		result = drgSysService.minDataInfoUpload(args);
    		break;
    	case "MinDataOff":
    		result = drgSysService.minDataOffInfoUpdUpload(args);
    		break;
    	case "ServiceDetail":
    		result = drgSysService.serviceDetailUpload(args);
    		break;
    	case "Control":
    		result = drgSysService.controlUpload(args);
    		break;
    	case "GroupQuery":
    		result = drgSysService.groupQuery(args);
    		break;
    	case "GroupQueryTask":
    		result = drgSysService.groupQueryTask(args);
    		break;
    	}
    	
    	return result;
    }
}
