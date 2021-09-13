package com.zebone.nhis.ma.pub.service.impl;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.lb.service.LbHighValueConsumHandler;
import com.zebone.nhis.ma.pub.lb.service.LbXFReferralHandler;
import com.zebone.nhis.ma.pub.lb.service.MedicalInsuranceCostsHandler;
import com.zebone.nhis.ma.pub.lb.service.NHISLBGXInsurHandler;
import com.zebone.nhis.ma.pub.service.IExtSystemService;
import com.zebone.nhis.ma.pub.syx.service.MedicalInsuranceGzgyHandler;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 灵璧人民医院外部系统对接服务实现（无事物）
 * @author yangxue
 *
 */
@Service("LbExtSystemPubHandler")
public class LbExtSystemPubHandler implements IExtSystemService {
	
	@Resource
    private MedicalInsuranceGzgyHandler gzgyHandler;
	
	@Resource
	private LbHighValueConsumHandler lbHighValueConsumHandler;

	@Resource
	private MedicalInsuranceCostsHandler medicalInsuranceCostsHandler;

	@Resource
	private NHISLBGXInsurHandler nhislbgxInsurHandler;

	@Resource
	private LbXFReferralHandler lbXFReferralHandler;

	@Override
	public Object processExtSystem(String systemName, String methodName,
			Object... args) {
		Object result = null;
		if(CommonUtils.isEmptyString(systemName)||CommonUtils.isEmptyString(methodName))
			throw new BusException("调用外部系统处理接口时，未传入系统名systemName或处理业务的方法名methodName");
		switch(systemName){
			case "medicalInsurance":
				gzgyHandler.invokeMethod(methodName, args);
				break;
			case "CONSUMABLE"://高值耗材
				result=lbHighValueConsumHandler.invokeMethod(methodName, args);
				break;
			case "MedicalCostsAudit"://医保行为和医疗费用监管审核平台
				result=medicalInsuranceCostsHandler.invokeMethod(methodName, args);
				break;
			case "ArchInfo"://中公网中心服务
				result=nhislbgxInsurHandler.invokeMethod(methodName, args);
				break;
			case "XFReferral"://讯飞转诊
				result=lbXFReferralHandler.invokeMethod(methodName, args);
				break;
			//对应多个系统，可继续追加
			default:
				break;
		}
		return result;
	}

}
