package com.zebone.nhis.ma.pub.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.service.IExt2HipSystemService;
import com.zebone.nhis.ma.pub.syx.service.ExtSendHipService;
import com.zebone.platform.modules.exception.BusException;

@Service("syxExt2HipSystemPubHandler")
public class SyxExt2HipSystemPubHandler implements IExt2HipSystemService{
	
	@Autowired
	private ExtSendHipService extSendHipService;
	
	@Override
	public Object processExtSystem(String methodName,
			Map<String,Object> paramMap) {
		Object result = null;
		if(CommonUtils.isEmptyString(methodName))
			throw new BusException("调用外部系统处理接口时，未传入系统名systemName或处理业务的方法名methodName");
		
		return extSendHipService.invokeMethod(methodName, paramMap);
	}

}
