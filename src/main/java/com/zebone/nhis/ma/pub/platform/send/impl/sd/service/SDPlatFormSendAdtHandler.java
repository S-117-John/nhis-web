package com.zebone.nhis.ma.pub.platform.send.impl.sd.service;

import ca.uhn.hl7v2.HL7Exception;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendAdt;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 发送ADT领域消息
 * @author 杨雪
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDPlatFormSendAdtHandler {
	@Resource
	private SDMsgSendAdt  sDMsgSendAdt;

	public void sendBedChangeMsg(Map<String,Object> paramMap) throws HL7Exception{
	//2018-05-10 发送换床消息 pkPv,pkPvDes,codeEmp - 发送床位发生变化的患者
	   sDMsgSendAdt.sendAdtMsg("A42", paramMap);
	}
	public void sendBedPackMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-09 发送包床消息-codeEmp,pk_pv,bedStatus【B】,bednos【List<String>】
		paramMap.put("bedStatus", "B");
		sDMsgSendAdt.sendAdtMsg("A42", paramMap);
	}
	public void sendBedRtnPackMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-09 发送退包床消息 - codeEmp,pk_pv,bedStatus【C】,bednos【List<String>】
		paramMap.put("bedStatus", "C");
		sDMsgSendAdt.sendAdtMsg("A42", paramMap);

	}
	public void sendDeptInMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-07 发送入科消息

		if("入院".equals(SDMsgUtils.getPropValueStr(paramMap,"adtType"))){
			//入科接收
			paramMap.remove("dateEnd");
			sDMsgSendAdt.sendAdtMsg("A10", paramMap);

		}else if("门诊".equals(SDMsgUtils.getPropValueStr(paramMap,"adtType"))){
			//门诊到诊
			sendPvOpArriveMsg(paramMap);
		}else if("新出生".equals(SDMsgUtils.getPropValueStr(paramMap,"adtType"))){
			//婴儿登记
			sDMsgSendAdt.sendAdtMsg("A01", paramMap);

		}else if("转科".equals(SDMsgUtils.getPropValueStr(paramMap,"adtType"))){
			//转科
			sDMsgSendAdt.sendAdtMsg("A02", paramMap);
		}

	}
	public void sendCancelDeptInMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-07 发送取消入科消息
		if("1".equals(SDMsgUtils.getPropValueStr(paramMap,"flagInf"))){
			//婴儿撤销登记
			sDMsgSendAdt.sendAdtMsg("A11", paramMap);
		}else{
			//取消入科
			sDMsgSendAdt.sendAdtMsg("A32", paramMap);
		}
	}
	public void sendDoctorMainChangeMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-10发送修改主治医生 //修改主治医生
		sDMsgSendAdt.sendAdtMsg("A54", paramMap);
	}
	/**
	 * 修改患者信息A08
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendPiMasterMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-10 灵璧需求 - 发送修改患者消息
		sDMsgSendAdt.sendAdt08Msg("A08", paramMap);
	}
	/**
	 * 修改患者
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendPvInfoMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-09 发送修改患者信息
		if("_UPDATE".equals(SDMsgUtils.getPropValueStr(paramMap, "STATUS"))) {
			sDMsgSendAdt.sendAdtMsg("A08", paramMap);
		}
	}
	/**
	 * 出院
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendPvOutMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-08 发送出院消息
		sDMsgSendAdt.sendAdtMsg("A16", paramMap);

	}
	/**
	 * 发送入院登记信息
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendPvInMsg(Map<String,Object> paramMap) throws HL7Exception{
		if(paramMap.containsKey("pvEncounter")){
			paramMap.putAll((Map<String,Object>)paramMap.get("pvEncounter"));
			paramMap.remove("pvEncounter");
		}
		if(paramMap.containsKey("piMaster")){
			paramMap.putAll((Map<String,Object>)paramMap.get("piMaster"));
			paramMap.remove("piMaster");
		}
		sDMsgSendAdt.sendAdtMsg("A01", paramMap);
	}
	/**
	 * 取消出院
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendPvCancelOutMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-08 发送取消出院消息
		sDMsgSendAdt.sendAdtMsg("A25", paramMap);
	}
	/**
	 * 取消入院
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendPvCancelInMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-09 发送取消入院消息
		sDMsgSendAdt.sendAdtMsg("A11", paramMap);
	}


	//*****************************************住院门诊分割线********************************************************


	/**
	 * 发送门诊诊毕消息
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendFinishClinicMsg(Map<String,Object> paramMap)  throws HL7Exception{
		paramMap.put("ipOrOp", "O");
		sDMsgSendAdt.sendAdtMsg("A03", paramMap);
	}

	/**
	 * 发送门诊挂号信息
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendPvOpRegMsg(Map<String,Object> paramMap) throws HL7Exception{
		//2018-05-09 发送门诊挂号信息
		paramMap.put("ipOrOp", "O");
		sDMsgSendAdt.sendAdtMsg("A04", paramMap);
	}

	/**
	 * 发送门诊转住院消息
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendopToIpMsg(Map<String,Object> paramMap) throws HL7Exception{
		paramMap.put("ipOrOp", "O");
		sDMsgSendAdt.sendAdtMsg("A06", paramMap);
	}
	/**
	 * 发送门诊患者信息修改
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendUpPiInfoMsg(Map<String,Object> paramMap)  throws HL7Exception{
		paramMap.put("ipOrOp", "O");
		sDMsgSendAdt.sendAdtMsg("A08", paramMap);
	}


	/**
	 * 发送门诊到诊
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendPvOpArriveMsg(Map<String,Object> paramMap) throws HL7Exception{

		paramMap.put("ipOrOp", "O");
		sDMsgSendAdt.sendAdtMsg("A10", paramMap);
	}

	/**
	 * 发送门诊退号信息
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendPvOpRegCancelMsg(Map<String,Object> paramMap) throws HL7Exception{
		paramMap.put("ipOrOp", "O");
		sDMsgSendAdt.sendAdtMsg("A11", paramMap);
	}


	/**
	 * 发送门诊取消到诊消息
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendCancelClinicMsg(Map<String,Object> paramMap)  throws HL7Exception{
		paramMap.put("ipOrOp", "O");
		sDMsgSendAdt.sendAdtMsg("A32", paramMap);
	}





}
