package com.zebone.nhis.ma.pub.zsrm.handler;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.zsrm.service.ZsrmEhealthCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 电子健康码业务处理服务
 *
 */
@Service
public class ZsrmEhealthCodeHandler {
	
	private  static final String URL=ApplicationUtils.getPropertyValue("health.url", "");
	
	@Resource
	private ZsrmEhealthCodeService zsrmEhealthCodeService;
	
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
	    		result = zsrmEhealthCodeService.eHealthCodeEHC03(args);
	    		break;
	    		//获取患者电子健康码二维码
			case "eHealthCodeEHC1026":
				result = zsrmEhealthCodeService.eHealthCodeEHC1026(args);
				break;
				//查询持卡人档案信息
			case "eHealthCodeEHCA1006":
				result = zsrmEhealthCodeService.eHealthCodeEHCA1006(args);
				break;
	    	default:
				break;
    	}
    	
    	return result;
    }
}
