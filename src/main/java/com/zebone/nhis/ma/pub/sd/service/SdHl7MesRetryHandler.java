package com.zebone.nhis.ma.pub.sd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.ma.pub.service.SystemPubRealizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdDe;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.sd.service.SDPlatFormSendExHandler;

import ca.uhn.hl7v2.HL7Exception;



@Service
public class SdHl7MesRetryHandler {


	@Resource
	public SDPlatFormSendExHandler sDPlatFormSendExHandler;

	@Autowired
	private SystemPubRealizationService systemPubRealizationService;
	public Object invokeMethod(String methodName,Object...args){
		Object obj = null;
    	switch(methodName){
	    	case "sendDrugInfoToMachine":
	    		this.sendDrugInfoToMachine(args);
	    		break;
			case "querySendDrugAgainData":
				//查询重发包药机数据
				obj = systemPubRealizationService.querySendDrugAgainData(args);
				break;
			default :
				break;
    	}
    	return obj;
	}


	public void sendDrugInfoToMachine(Object...args){
		List<ExPdDe> exPdDes = new ArrayList<>();
		List<ExPdApplyDetail> exPdAppDetails =  new ArrayList<>();
		ExPdDe epd = new ExPdDe();
		Map<String, Object> codeDeMap = (Map<String, Object>)args[0];
		String codeDe = SDMsgUtils.getPropValueStr(codeDeMap, "codeDe");
		epd.setCodeDe(codeDe);
		exPdDes.add(epd);
		Map<String, Object> map = new HashMap<>();
		map.put("exPdAppDetails",exPdAppDetails);//请领明细
		map.put("exPdDes", exPdDes);//发药明细
		map.put("orc", "OK");//新增
		map.put("IsSendSDEx", true);//深大是否发送医嘱执行消息
		map.put("codeDe", codeDe);//发药单号
		//PlatFormSendUtils.sendScmIpDeDrug(map);
		try {
			sDPlatFormSendExHandler.sendScmIpDeDrug(map);
		} catch (HL7Exception e) {

		}
	}
}
