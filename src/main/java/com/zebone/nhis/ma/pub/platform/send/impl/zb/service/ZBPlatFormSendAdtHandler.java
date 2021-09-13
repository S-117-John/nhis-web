package com.zebone.nhis.ma.pub.platform.send.impl.zb.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.hl7v2.HL7Exception;

import com.zebone.nhis.ma.pub.platform.zb.send.MsgSendAdt;
import com.zebone.nhis.ma.pub.platform.zb.send.MsgSendBl;

/**
 * 发送ADT领域消息
 * @author 杨雪
 *
 */
@Service
public class ZBPlatFormSendAdtHandler {
	@Resource
	private MsgSendAdt  msgSendAdt;
	@Resource
	private MsgSendBl msgSendBl;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendBedChangeMsg(Map<String,Object> paramMap) throws HL7Exception{
	//2018-05-10 发送换床消息 pkPv,pkPvDes,codeEmp - 发送床位发生变化的患者
	   msgSendAdt.sendAdtMsg("A42", paramMap);//换床
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendBedPackMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-09 发送包床消息-codeEmp,pk_pv,bedStatus【B】,bednos【List<String>】
		paramMap.put("bedStatus", "B");
		msgSendAdt.sendAdtMsg("A42", paramMap);//包床
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendBedRtnPackMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-09 发送退包床消息 - codeEmp,pk_pv,bedStatus【C】,bednos【List<String>】
		paramMap.put("bedStatus", "C");
		msgSendAdt.sendAdtMsg("A42", paramMap);//退包床
			
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendDeptInMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-07 发送入科消息
		if(paramMap.containsKey("isSendMsg")){
			return;
		}
		if(paramMap.get("adtType")!=null&&paramMap.get("adtType").equals("入院")){
			paramMap.remove("dateEnd");
			msgSendAdt.sendAdtMsg("A10", paramMap);//入科接收
		}
		else if(paramMap.get("adtType")!=null&&paramMap.get("adtType").equals("新出生"))
			msgSendAdt.sendAdtMsg("A01", paramMap);//婴儿登记
		else if(paramMap.get("adtType")!=null&&paramMap.get("adtType").equals("转科"))
			msgSendAdt.sendAdtMsg("A02", paramMap);//转科
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendCancelDeptInMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-07 发送取消入科消息
		if(paramMap.get("flagInf")!=null&&paramMap.get("flagInf").equals("1"))
			msgSendAdt.sendAdtMsg("A11", paramMap);//婴儿撤销登记
		else
			msgSendAdt.sendAdtMsg("A32", paramMap);//取消入科
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendDoctorMainChangeMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-10发送修改主治医生
		msgSendAdt.sendAdtMsg("A54", paramMap);//修改主治医生
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendPiMasterMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-10 灵璧需求 - 发送修改患者消息
		msgSendAdt.sendAdtMsg("A08", paramMap);//发送修改患者信息
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendPvInfoMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-09 发送修改患者信息
		if(paramMap.get("STATUS").toString().equals("_UPDATE"))
			msgSendAdt.sendAdtMsg("A08", paramMap);//发送修改患者信息
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendPvOutMsg(Map<String,Object> paramMap) throws HL7Exception{
		Map<String,Object> paramMapS = new HashMap<String, Object>();
		paramMapS.putAll(paramMap);
		//2018-05-08 发送出院消息
		msgSendAdt.sendAdtMsg("A16", paramMap);//出院信息
		//2020-03-14 发送出院消息
		msgSendBl.sendBlMsgs("ZH1", paramMapS);//发送病案首页信息
	}
	/**
	 * 发送入院登记信息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendPvInMsg(Map<String,Object> paramMap) throws HL7Exception{
		if(paramMap.containsKey("isSendMsg")){
			return;
		}
		if(paramMap.get("pvEncounter") !=null){
			paramMap.putAll((Map)paramMap.get("pvEncounter"));
		}
		if((Map)paramMap.get("piMaster")!=null){
			paramMap.putAll((Map)paramMap.get("piMaster"));
		}
		
		paramMap.remove("piMaster");
		paramMap.remove("pvEncounter");
		msgSendAdt.sendAdtMsg("A01", paramMap);//入院登记
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendPvCancelOutMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-08 发送取消出院消息
		msgSendAdt.sendAdtMsg("A25", paramMap);//取消出院
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendPvCancelInMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-09 发送取消入院消息
		msgSendAdt.sendAdtMsg("A11", paramMap);//取消入院
	}
	/**
	 * 发送门诊挂号信息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendPvOpRegMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-09 发送门诊挂号信息
		msgSendAdt.sendAdtMsg("A04", paramMap);//门诊挂号
	}
	/**
	 * 发送门诊退号信息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendPvOpRegCancelMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-10 发送门诊退号信息
		msgSendAdt.sendAdtMsg("A11", paramMap);//门诊退号
	}
}
