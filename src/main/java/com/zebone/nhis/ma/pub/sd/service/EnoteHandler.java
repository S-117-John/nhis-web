package com.zebone.nhis.ma.pub.sd.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 电子票据业务处理服务
 * @author Administrator
 *
 */
@Service
public class EnoteHandler {
	
	@Resource
	private EnoteService enoteService;
	
	/**
	 * 业务处理方法转换器
	 * @param methodName
	 * @param args
	 */
    public Object invokeMethod(String methodName,Object...args){
    	Object result = null;
    	switch(methodName){
    	case "eBillOutpatient":
    		result = enoteService.eBillOutpatient(args);
    		break;
    	case "billCancel":
    		result = enoteService.billCancel(args);
    		break;
    	case "rePaperInv":
    		result = enoteService.rePaperInv(args);
    		break;
    	case "invCanl":
    		result = enoteService.invCanl(args);
    		break;
    	case "eBillRegistration":
    		result = enoteService.eBillRegistration(args);
    		break;
    	case "eBillMzOutpatient":
    		result = enoteService.eBillMzOutpatient(args);
    		break;
    	case "eBillAdvancePayment":
			result = enoteService.eBillAdvancePayment(args);
			break;
		case "eBillAdvanceOffPayment":
			result = enoteService.eBillAdvanceOffPayment(args);
			break;
    	}
    	
    	return result;
    }
}
