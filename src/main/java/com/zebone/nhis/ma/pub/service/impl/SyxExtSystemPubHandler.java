package com.zebone.nhis.ma.pub.service.impl;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.handler.SystemPubRealizationHandler;
import com.zebone.nhis.ma.pub.service.IExtSystemService;
import com.zebone.nhis.ma.pub.syx.service.*;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
 * 中山二院外部系统对接服务实现（无事物）
 * @author yangxue
 *
 */
@Service("SyxExtSystemPubHandler")
public  class SyxExtSystemPubHandler implements IExtSystemService {
    @Resource 
	private  LisSystemHandler lisSystemHandler;
    @Resource 
	private  PacsSystemHandler pacsSystemHandler;
    @Resource
    private PivasSystemHandler pivasSystemHandler;
    @Resource
	private QryOldSystemPiToHisHandler qryOldSystemPiToHisHandler;
    @Resource
	private MrdSystemHandler mrdSystemHandler;
    @Resource
    private HighValueConsumHandler consumHandler;
    @Resource
    private SendDrugsToMachineHandler machineHandler;
    @Resource
    private MedicalInsuranceGzgyHandler gzgyHandler;
    @Resource
    private SystemPubRealizationHandler systemPubRealizationHandler;
    
    @Resource
    private SyxPaAppWebHandler appWebHandler;
    
	@Override
	public Object processExtSystem(String systemName, String methodName,Object...args) {
		Object result = null;
		if(CommonUtils.isEmptyString(systemName)||CommonUtils.isEmptyString(methodName))
			throw new BusException("调用外部系统处理接口时，未传入系统名systemName或处理业务的方法名methodName");
		switch(systemName){
			case "LIS":
				lisSystemHandler.invokeMethod(methodName, args);
				//调用lisSystemHandler的统一处理方法
				break;
			case "PACS":
				result = pacsSystemHandler.invokeMethod(methodName, args);
				break;
			case "PIVAS"://静配
				result = pivasSystemHandler.invokeMethod(methodName, args);
				break;
			case "PiInfo":
				result = qryOldSystemPiToHisHandler.invokeMethod(methodName, args);
				break;
			case "MRD"://广东省病案系统
				result = mrdSystemHandler.invokeMethod(methodName, args);
				break;
			case "CONSUMABLE"://高值耗材
				result=consumHandler.invokeMethod(methodName, args);
				break;
			case "PackMachine"://包药机
				machineHandler.invokeMethod(methodName, args);
				break;
			case "medicalInsurance"://广州公医
				gzgyHandler.invokeMethod(methodName, args);
				break;
			case "builtAssistOcc"://生成医技执行单
				systemPubRealizationHandler.invokeMethod(methodName,args);
				break;
			case "PAAPP"://平安App接口
				appWebHandler.invokeMethod(methodName, args);
				break;
				
			//对应多个系统，可继续追加
			default:
				break;
		}
		return result;
	}
   

}
