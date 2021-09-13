package com.zebone.nhis.pro.zsba.mz.pub.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * 电子票据业务处理服务
 * @author Administrator
 *
 */
@Service
public class ZsbaEnoteHandler {
	
	@Resource
	private ZsbaEnoteService zsbaEnoteService;
	
	/**
	 * 业务处理方法转换器
	 * @param methodName
	 * @param args
	 */
    public Object invokeMethod(String methodName,Object...args){
    	Object result = null;
    	switch(methodName){
    	case "eBillOutpatient":
    		result = zsbaEnoteService.eBillOutpatient(args);
    		break;
    	case "billCancel":
    		result = zsbaEnoteService.billCancel(args);
    		break;
    	case "rePaperInv":
    		result = zsbaEnoteService.rePaperInv(args);
    		break;
    	case "invCanl":
    		result = zsbaEnoteService.invCanl(args);
    		break;
    	case "eBillRegistration":
    		result = zsbaEnoteService.eBillRegistration(args);
    		break;
    	case "eBillMzOutpatient":
    		result = zsbaEnoteService.eBillMzOutpatient(args);
    		break;
    	}
    	
    	return result;
    }
}
