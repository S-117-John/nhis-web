package com.zebone.nhis.ma.pub.sd.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;

/**
 * 电子健康码业务处理服务
 * @author zhangtao
 *
 */
@Service
public class EhealthCodeHandler {
	
	private  static final String URL=ApplicationUtils.getPropertyValue("health.url", "");
	
	@Resource
	private EhealthCodeService ehealthCodeService;
	
	/**
	 * 业务处理方法转换器
	 * @param methodName
	 * @param args
	 */
    public Object invokeMethod(String methodName,Object...args){
    	Object result = null;
    	if(CommonUtils.isNull(URL)){
			return result;
		}
    	switch(methodName){
    		//电子健康码注册
	    	case "eHealthCodeEHC01":
	    		result = ehealthCodeService.eHealthCodeEHC01(args);
	    		break;
	    	//电子健康码查询
	    	case "eHealthCodeEHC03":
	    		result = ehealthCodeService.eHealthCodeEHC03(args);
	    		break;
	    	//电子健康码验证
	    	case "eHealthCodeEHC05":
	    		result = ehealthCodeService.eHealthCodeEHC05(args);
	    		break;
	    	//电子健康码发起退费通知	
	    	case "eHealthCodePM020":
	    		result = ehealthCodeService.eHealthCodePM020(args);
	    		break;
	    	default:
				break;
    	}
    	
    	return result;
    }
}
