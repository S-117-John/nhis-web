package com.zebone.nhis.ma.pub.platform.send.impl.zb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.ma.pub.platform.send.PlatFormSendHandlerAdapter;
import org.springframework.stereotype.Service;

import com.zebone.nhis.ma.pub.platform.send.IPlatFormSendHandler;

import ca.uhn.hl7v2.HL7Exception;
/**
 * 振邦集成平台消息发送服务
 * @author yangxue
 *
 */
@Service("ZBPlatFormSendService")
public class ZBPlatFormSendPubHandler extends PlatFormSendHandlerAdapter {
	@Resource
	private ZBPlatFormSendAdtHandler zBPlatFormSendAdtHandler;
	@Resource
    private ZBPlatFormSendBdHandler zBPlatFormSendBdHandler;
	@Resource
    private ZBPlatFormSendBlHandler zBPlatFormSendBlHandler;
	@Resource
    private ZBPlatFormSendCnHandler zBPlatFormSendCnHandler;
	@Resource
    private ZBPlatFormSendExHandler zBPlatFormSendExHandler;


	@Override
	public void sendBedChange(Map<String, Object> paramMap)  {
		try{
			zBPlatFormSendAdtHandler.sendBedChangeMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBdDefDocMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBdHandler.sendBdDefDocMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBdTermDiagMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBdHandler.sendBdTermDiagMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBdTermFreqMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBdHandler.sendBdTermFreqMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBdSupplyMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBdHandler.sendBdSupplyMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBdResBedMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBdHandler.sendBdResBedMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBdOrdMsg(Map<String, Object> paramMap)  {
		try{
			zBPlatFormSendBdHandler.sendBdOrdMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBdItemMsg(Map<String, Object> paramMap)  {
		try{
			zBPlatFormSendBdHandler.sendBdItemMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBdItemSetMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBdHandler.sendBdItemSetMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBdOuDeptMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBdHandler.sendBdOuDeptMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBdOuEmpMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBdHandler.sendEmpMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBdOuUserMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBdHandler.sendUserMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBedChangeMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendAdtHandler.sendBedChangeMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBedPackMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendAdtHandler.sendBedPackMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBedRtnPackMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendAdtHandler.sendBedRtnPackMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendDeptInMsg(Map<String, Object> paramMap)  {
		try{
			zBPlatFormSendAdtHandler.sendDeptInMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendCancelDeptInMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendAdtHandler.sendCancelDeptInMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendDoctorChangeMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendAdtHandler.sendDoctorMainChangeMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendPiMasterMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendAdtHandler.sendPiMasterMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendPvInfoMsg(Map<String, Object> paramMap)  {
		try{
			zBPlatFormSendAdtHandler.sendPvInfoMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendPvInMsg(Map<String, Object> paramMap)  {
		try{
			zBPlatFormSendAdtHandler.sendPvInMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendPvCancelInMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendAdtHandler.sendPvCancelInMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendPvOutMsg(Map<String, Object> paramMap)  {
		try{
			zBPlatFormSendAdtHandler.sendPvOutMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendPvCancelOutMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendAdtHandler.sendPvCancelOutMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendPvOpRegMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendAdtHandler.sendPvOpRegMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendPvOpCancelRegMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendAdtHandler.sendPvOpRegCancelMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBlCancelSettleMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBlHandler.sendBlCancelSettleMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBlSettleMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBlHandler.sendBlSettleMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBlOpSettleMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBlHandler.sendBlOpSettleMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBlOpCancelSettleMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBlHandler.sendBlOpCancelSettleMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendBlMedApplyMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendBlHandler.sendBlMedApplyMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendCnDiagMsg(Map<String, Object> paramMap)  {
		try{
			zBPlatFormSendCnHandler.sendCnDiagMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendCnMedApplyMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendCnHandler.sendCnMedApply(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendCnPresOpMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendCnHandler.sendCnPresOpMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendExConfirmMsg(Map<String, Object> paramMap)
			 {
		try{
			zBPlatFormSendExHandler.sendExConfirmMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	@Override
	public void sendExOrderCheckMsg(Map<String, Object> paramMap){
		try{
			Map<String, Object> paramMaps = new HashMap<>();
			paramMaps.putAll(paramMap);
			zBPlatFormSendExHandler.sendExOrderCheck(paramMap);
			zBPlatFormSendExHandler.sendOpConfirmMsg(paramMaps);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}
   /**
    * 发送药品信息
    */
	@Override
	public void sendBdPdMsg(Map<String, Object> paramMap) {
		try{
			zBPlatFormSendBdHandler.sendBdPdMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}

	}

	@Override
	public void sendEmrMsg(Map<String, Object> paramMap) {
		//todo
	}

	@Override
	public void sendBdFactoryMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBdOuOrgMsg(Map<String, Object> paramMap) {

	}

	@Override
	public void sendMsgLisRis(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}
	@Override
	public void sendSchInfo(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendSchAppt(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Map<String,Object>> getEMRClinicalData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendScmIpDeDrug(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendPvOpNoMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public String sendCnOpCall(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

		return null;
	}

	/**
	 * 诊疗级别（深圳）
	 */
	@Override
	public void sendRegLevelMsg(Map<String, Object> paramMap) {
	}

	/**
	 * 单位合同（深圳）
	 */
	@Override
	public void sendPactMsg(Map<String, Object> paramMap) {
	}

	/**
	 * 预交金（深圳）
	 */
	@Override
	public void sendDepositMsg(Map<String, Object> paramMap) {
		try {
			zBPlatFormSendBlHandler.sendDepositMsg(paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
	}

	/**
	 * 手术申请（深圳）
	 */
	@Override
	public void sendOpApplyMsg(Map<String, Object> paramMap) {
		try {
			zBPlatFormSendExHandler.sendOpConfirmMsg(paramMap);
		} catch (Exception e) {

		}
	}

	/**
	 * 住院发票（深圳）
	 */
	@Override
	public void sendReceiptMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}


	/**
	 * 手术确认（深圳）
	 */
	@Override
	public void sendOpConfirmMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCallPayMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendQueryIpMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendQueryPiMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendBlWeiXinSQMZQ1Msg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBlWeiXinQBPZZLMsgDetails(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBlWeiXinQBPZZLMsgTheme(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}



	@Override
	public void sendTotalExpensesMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendFeedbackDepositMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendOpCompleteMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}



	@Override
	public void sendDepositStatusMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendAddExOrderOccMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendDelExOrderOccMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendUpDateExOrderOccMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBedCgMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}
	/**
	 * 发送费用短信提醒信息
	 * zsrm-门诊欠费短信提醒
	 * @param paramMap
	 */
	@Override
	public void sendOpFeeReminderMsg(Map<String, Object> paramMap) {

	}

	@Override
	public void sendAllExOrderOccMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendRelationshipMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
	}

	@Override
	public void sendOperaOrderCancelMsg(Map<String, Object> paramMap) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("control", "NW");//操作类型
			map.put("ordStatus", "6");//医嘱状态
			map.put("ordlist", paramMap.get("changeOrdList"));
			sendExOrderCheckMsg(map);
			//检验作废发送 ORL^O22消息
			map.put("Control", "CR");//操作类型
			map.put("type", "O");//操作类型
			map.put("dtlist", paramMap.get("changeOrdList"));
			sendBlMedApplyMsg(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendCancelSignPresMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendSignPresMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCancelHerbOrderMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendUpdateSignMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendSignHerbOrder2Msg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCancleLisApplyListMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCancleRisApplyListMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendSaveDeptBuAndBusesMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendHighValueConSumIp(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendHighValueConSumIpBack(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBdMaterMsg(Map<String, Object> paramMap) {

	}

	@Override
	public void sendOpArriveMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOpToIpMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendFinishClinicMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCancelClinicMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendUpPiInfoMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendConsultMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendConsultResponeMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCnOpAppMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOpO09Msg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendDeptChangeMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBdDeptUnitMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendShortMsgPhoneChk(Map<String, Object> paramMap) {

	}

	@Override
	public void sendDstributeCardMsg(Map<String, Object> paramMap) {
		try{
			zBPlatFormSendAdtHandler.sendPiMasterMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}


}
