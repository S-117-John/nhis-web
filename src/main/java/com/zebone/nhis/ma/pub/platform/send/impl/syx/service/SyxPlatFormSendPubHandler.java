package com.zebone.nhis.ma.pub.platform.send.impl.syx.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.ma.pub.platform.send.PlatFormSendHandlerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.send.IPlatFormSendHandler;
import com.zebone.nhis.ma.pub.platform.syx.support.MsgUtils;
/**
 * 振邦集成平台消息发送服务
 * @author yangxue
 *
 */
@Transactional(propagation=Propagation.REQUIRES_NEW)
@Service("SyxPlatFormSendService")
public class SyxPlatFormSendPubHandler extends PlatFormSendHandlerAdapter {
	@Resource
    private SyxPlatFormSendExHandler syxPlatFormSendExHandler;
	@Resource
	private SyxPlatFormSendBdHandler syxPlatFormSendBdHandler;
	@Resource
	private SyxPlatFormSendAdtHandler syxPlatFormSendAdtHandler;
	@Resource
	private SyxPlatFormSendOpHandler syxPlatFormSendOpHandler;
	@Resource
	private SyxPlatFormSendExamHandler syxPlatFormSendExamHandler;
	@Resource
	private SyxPlaFormSendScmHandler syxPlaFormSendScmHandler;
	@Resource
	private SyxPlaFormSendEmrHandler syxPlaFormSendEmrHandler;

	@Override
	public void sendBedChange(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBdDefDocMsg(Map<String, Object> paramMap) {
		try {
			syxPlatFormSendBdHandler.sendBdDefDocMsg(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendBdTermDiagMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBdTermFreqMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBdSupplyMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public void sendBdResBedMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBdOrdMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBdItemMsg(Map<String, Object> paramMap) {

	}

	@Override
	public void sendBdItemSetMsg(Map<String, Object> paramMap) {

	}

	@Override
	public void sendBdOuDeptMsg(Map<String, Object> paramMap) {
		try {
			String type = ApplicationUtils.getSysparam("PUB0001", false);
			if (StringUtils.isNotBlank(type)&&!"0".equals(type)&&type.indexOf("B")>-1)
			syxPlatFormSendBdHandler.sendDeptMsg(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void sendBdOuEmpMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBdOuUserMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBdPdMsg(Map<String, Object> paramMap) {
		try {
			syxPlaFormSendScmHandler.sendPdDictMessage(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void sendEmrMsg(Map<String, Object> paramMap) {
		//todo
		try {
			syxPlaFormSendEmrHandler.sendEmrPiMessage(paramMap);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void sendBdFactoryMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBdOuOrgMsg(Map<String, Object> paramMap) {

	}

	@Override
	public void sendBedChangeMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBedPackMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBedRtnPackMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}
	//转科
	@Override
	public void sendDeptInMsg(Map<String, Object> paramMap) {
		try {
			String type = ApplicationUtils.getSysparam("PUB0001", false);
			if (StringUtils.isNotBlank(type)&&!"0".equals(type)&&type.indexOf("2")>-1)
			syxPlatFormSendAdtHandler.sendPvAdtMsg(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//取消转科
	@Override
	public void sendCancelDeptInMsg(Map<String, Object> paramMap) {
		try {
			String type = ApplicationUtils.getSysparam("PUB0001", false);
			if (StringUtils.isNotBlank(type)&&!"0".equals(type)&&type.indexOf("2")>-1)
			syxPlatFormSendAdtHandler.sendPvAdtMsg(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendDoctorChangeMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendPiMasterMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendPvInfoMsg(Map<String, Object> paramMap)  {
		//syxPlatFormSendAdtHandler.sendPvInfoMsg(paramMap);
	}

	@Override
	public void sendPvInMsg(Map<String, Object> paramMap) {
		try {
			String type = ApplicationUtils.getSysparam("PUB0001", false);
			if (StringUtils.isNotBlank(type)&&!"0".equals(type)&&type.indexOf("1")>-1)
				syxPlatFormSendAdtHandler.sendPvInMsg(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendPvCancelInMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub


	}

	@Override
	/**
	 * 出院登记信息新增服务
	 */
	public void sendPvOutMsg(Map<String, Object> paramMap) {
		try {
			String type = ApplicationUtils.getSysparam("PUB0001", false);
			if (StringUtils.isNotBlank(type)&&!"0".equals(type)&&type.indexOf("8")>-1)
				syxPlatFormSendAdtHandler.sendPvOutMsg(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	/**
	 * 取消出院登记信息新增服务
	 */
	public void sendPvCancelOutMsg(Map<String, Object> paramMap) {
		try {
			String type = ApplicationUtils.getSysparam("PUB0001", false);
			if (StringUtils.isNotBlank(type)&&!"0".equals(type)&&type.indexOf("8")>-1)
				syxPlatFormSendAdtHandler.sendPvOutMsg(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendPvOpRegMsg(Map<String, Object> paramMap) {

		//syxPlatFormSendOpHandler.sendPvOpRegMsg(paramMap);

	}

	@Override
	public void sendPvOpCancelRegMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBlCancelSettleMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBlSettleMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBlOpSettleMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBlOpCancelSettleMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBlMedApplyMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCnDiagMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendCnMedApplyMsg(Map<String, Object> paramMap) {

		try {
			String type = ApplicationUtils.getSysparam("PUB0001", false);
			if (StringUtils.isNotBlank(type)&&!"0".equals(type))
				if((type.indexOf("5")>-1&&"lis".equals(MsgUtils.getPropValueStr(paramMap, "type")))||((type.indexOf("6")>-1||type.indexOf("A")>-1)&&"ris".equals(MsgUtils.getPropValueStr(paramMap, "type"))))
					syxPlatFormSendExamHandler.sendMsg(paramMap,true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendCnPresOpMsg(Map<String, Object> paramMap) {
		try {
			if("1".equals(paramMap.get("IsHrOpen"))){
				syxPlaFormSendScmHandler.sendHrPresInfo(paramMap);//发送华润处方上传信息，经过系统参数【EX0019】判断后进行发送【门诊结算签到，门诊药房签到】
			}
			if("1".equals(paramMap.get("IsPtOpen"))){
				//syxPlaFormSendScmHandler.sendPtPresInfo(paramMap);//发送平台处方上传信息，只收费结算后进行发送
			}
			if("1".equals(paramMap.get("IsFinishDrug"))){//是否完成发药，门诊发药完成后调用以下接口
				syxPlaFormSendScmHandler.sendHrPresEnd(paramMap);
			}
			if("1".equals(paramMap.get("IsReturnDrug"))){//门诊退费
				syxPlaFormSendScmHandler.sendHrReturnDrugCgInfo(paramMap);;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void sendExConfirmMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}
	/**
	 * 发送医嘱核对信息
	 */
	@Override
	public void sendExOrderCheckMsg(Map<String, Object> paramMap) {
		try {
			String type = ApplicationUtils.getSysparam("PUB0001", false);
			if (StringUtils.isNotBlank(type)&&!"0".equals(type)&&(type.indexOf("3")>-1 ||type.indexOf("7")>-1)){
				syxPlatFormSendExHandler.sendExOrderCheck(paramMap);
			}
		} catch (Exception e) {
			//记录发送消息失败日志
			e.printStackTrace();
		}
	}

	@Override
	public void sendMsgLisRis(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}
	@Override
	public void sendSchInfo(Map<String, Object> paramMap) {

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
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendScmIpDeDrug(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	/*
	 * 发送号源排班信息
	 */
	@Override
	public void sendPvOpNoMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		try {
			//syxPlatFormSendOpHandler.sendPvOpNoMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	/**
	 * 门诊推送叫号信息
	 */
	@Override
	public String sendCnOpCall(Map<String, Object> paramMap) {

		try{
			if(StringUtils.isNotBlank(CommonUtils.getString(paramMap.get("pkPv")))){
				return syxPlatFormSendOpHandler.sendOpCallMsg(paramMap);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void sendDepositMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOpApplyMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendRegLevelMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendPactMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}


	@Override
	public void sendReceiptMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}


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
		// TODO Auto-generated method stub

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

	}


}
