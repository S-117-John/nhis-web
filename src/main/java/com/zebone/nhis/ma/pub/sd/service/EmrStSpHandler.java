package com.zebone.nhis.ma.pub.sd.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class EmrStSpHandler {
	
	@Resource
	private EmrStSpService emtStSpService;
	
	/**
	 * 业务处理方法转换器
	 * @param methodName
	 * @param args
	 */
    public Object invokeMethod(String methodName,Object...args){
    	Object result = null;
    	switch(methodName){
    	case "saveOrUpdateCharges":
    		emtStSpService.saveOrUpdateCharges(args);
    		break;
    	}
    	
    	return result;
    }
	
}
