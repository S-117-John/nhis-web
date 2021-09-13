package com.zebone.nhis.ma.pub.service.impl;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.sd.service.*;
import com.zebone.nhis.ma.pub.service.IExtSystemService;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 深大外部系统对接服务实现（无事物）
 * @author yangxue
 *
 */
@Service("SdExtSystemPubHandler")
public class SdExtSystemPubHandler implements IExtSystemService {

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
	@Autowired
	private FollowUpService followUpService;
	@Resource
	private CiInsuHandler ciInsuHandler;


	@Override
	public Object processExtSystem(String systemName, String methodName,
			Object... args) {
		Object result = null;
		if(CommonUtils.isEmptyString(systemName)||CommonUtils.isEmptyString(methodName))
			throw new BusException("调用外部系统处理接口时，未传入系统名systemName或处理业务的方法名methodName");
		switch(systemName){
			case "DrgBasicInfo":
				result=gzgyHandler.invokeMethod(methodName, args);
				break;
			case "DrgBusinessInfo":
				result=drgBusinessInfoHandler.invokeMethod(methodName, args);
				break;
			case "InsSzybCity":
				result=insSzybCityHandler.invokeMethod(methodName, args);
				break;
			case "medicalInsurance":
				result = insSzybCityHandler.invokeMethod(methodName, args);
				break;
			case "EBillInfo"://电子票据
				result=enoteHandler.invokeMethod(methodName, args);
				break;
			case "CONSUMABLE"://高值耗材
				result=consumHandler.invokeMethod(methodName, args);
				break;
			case "PackMachine"://包药机
				result=hl7MesRetry.invokeMethod(methodName, args);
				break;
			case "PackMachineOp"://发药机
				result=dispenseMachineHandler.invokeMethod(methodName, args);
				break;
			case "EmrStSp"://急诊
				result = emrStSpHandler.invokeMethod(methodName, args);
				break;
			case "FollowUp"://随访系统
				result=followUpService.invokeMethod(methodName, args);
				break;
			case "CiInsu"://商保
				result=ciInsuHandler.invokeMethod(methodName, args);
				break;
			default:
				break;
		}
		return result;
	}

}
