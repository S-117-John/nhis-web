package com.zebone.nhis.ma.pub.sd.service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.modules.exception.BusException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * DRG信息平台基础数据系统业务处理服务(无事物)
 * @author yangxue
 *
 */
@Service
public class DrgBasicInfoHandler {
	
	@Autowired
	private DrgBasicInfoService drgSysService;

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
    	case "HospitalInfo":
    		result = drgSysService.hospitalInfoUpload(args);
    		break;
    	case "HospitalInfoUpd":
    		result = drgSysService.hospitalInfoUpdUpload(args);
    		break;
    	case "DeptSearch":
    		result = drgSysService.deptSearchUpload(args);
    		break;
    	case "DeptMate":
    		result = drgSysService.deptMateUpload(args);
    		break;
    	case "DeptMateUpd":
    		result = drgSysService.deptMateUpdUpload(args);
    		break;
    	case "DoctorInfo":
    		result = drgSysService.doctorInfoUpload(args);
    		break;
    	}
    	return result;
    }
}
