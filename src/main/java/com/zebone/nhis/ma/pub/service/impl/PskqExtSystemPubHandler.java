package com.zebone.nhis.ma.pub.service.impl;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.sd.service.*;
import com.zebone.nhis.ma.pub.service.IExtSystemService;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 坪山口腔外部系统对接服务实现（无事物）
 * @author zhangtao
 *
 */
@Service("PskqExtSystemPubHandler")
public class PskqExtSystemPubHandler implements IExtSystemService {

	@Resource
    private DrgBasicInfoHandler gzgyHandler;
	@Resource
	private DrgBusinessInfoHandler drgBusinessInfoHandler;
	@Resource
	private InsSzybCityHandler insSzybCityHandler;
	@Resource
	private EnoteHandler enoteHandler;
	@Resource
	private SdHighValueConsumHandler consumHandler;
	@Resource
	private SdHl7MesRetryHandler hl7MesRetry;
	@Resource
	private OpDrugDispenseMachineHandler dispenseMachineHandler;
	@Resource
	private EmrStSpHandler emrStSpHandler;
	@Resource
	private EhealthCodeHandler ehealthCodeHandler;

	@Override
	public Object processExtSystem(String systemName, String methodName,
			Object... args) {
		Object result = null;
		if(CommonUtils.isEmptyString(systemName)||CommonUtils.isEmptyString(methodName))
			throw new BusException("调用外部系统处理接口时，未传入系统名systemName或处理业务的方法名methodName");
		switch(systemName){
			case "InsSzybCity":
				result=insSzybCityHandler.invokeMethod(methodName, args);
				break;
			case "medicalInsurance":
				insSzybCityHandler.invokeMethod(methodName, args);
				break;
			case "EBillInfo"://电子票据
				result=enoteHandler.invokeMethod(methodName, args);
				break;
			case "EHealthCode"://电子健康码
				result=ehealthCodeHandler.invokeMethod(methodName, args);
			break;
			default:
				break;
		}
		return result;
	}

}
