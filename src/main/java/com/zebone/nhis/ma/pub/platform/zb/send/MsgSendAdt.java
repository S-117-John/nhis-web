package com.zebone.nhis.ma.pub.platform.zb.send;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.model.v24.message.ADT_A02;
import ca.uhn.hl7v2.model.v24.message.ADT_A03;
import ca.uhn.hl7v2.model.v24.message.ADT_A09;
import ca.uhn.hl7v2.model.v24.message.ADT_A16;
import ca.uhn.hl7v2.model.v24.message.ADT_A21;
import ca.uhn.hl7v2.model.v24.message.ADT_A39;
import ca.uhn.hl7v2.model.v24.message.ADT_A54;
import ca.uhn.hl7v2.model.v24.segment.DG1;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NK1;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.PV2;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.zb.comm.Hl7MsgHander;
import com.zebone.nhis.ma.pub.platform.zb.dao.MsgRecMapper;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * Adt消息处理
 * @author chengjia
 *
 */
@Service
public class MsgSendAdt {
	
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");
	@Resource
	private	Hl7MsgHander msgHander;
	
	@Resource
	private	MsgRecMapper msgRecMapper;
	
	/**
	 * 发送Hl7消息
	 * @param triggerEvent
	 * @param paramMap 
	 * @throws HL7Exception
	 */
	public void sendAdtMsg(String triggerEvent,Map<String,Object> paramMap) throws HL7Exception{
		try {
			String msgId=MsgUtils.getMsgId();
			Message message=createAdtMsg(msgId,triggerEvent, paramMap);
			
			String msg = MsgUtils.getParser().encode(message);
			//发送消息
			msgHander.sendMsg(msgId, msg);
			//睡眠2分钟完成其他操作
			Thread.sleep(20000);
		} catch (Exception e) {
			loger.error("发送消息失败{},{}",triggerEvent,e.getMessage());
			throw new HL7Exception("发送消息失败！");
		}
	}
	
	/**
	 * 发送ADT消息
	 * @param msgId
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	private Message createAdtMsg(String msgId,String triggerEvent,Map<String,Object> paramMap){
		if(triggerEvent.equals("A01"))
		{
			if(null != paramMap.get("adtType") && paramMap.get("adtType").equals("新出生"))
				return a01_admitInf(msgId, paramMap);//婴儿登记
			else
				return a01_admit(msgId, paramMap);//入院登记
		}
		else if(triggerEvent.equals("A02"))
			return a02_deptChange(msgId, paramMap);//转科
		else if(triggerEvent.equals("A03"))
			return sendADTA03Msg(msgId, paramMap);//出院结算
		else if(triggerEvent.equals("A04"))
			return a04_opDeptIn(msgId, paramMap);//门诊挂号
		else if(triggerEvent.equals("A10"))
			return a10_ipDeptIn(msgId, paramMap);	//入科接收 √
		else if(triggerEvent.equals("A11"))
			return a11_cancelAdmitOrVisit(msgId, paramMap);//取消入院登记 | 婴儿取消登记 | 门诊退号
		else if(triggerEvent.equals("A16"))
			return a16_discharge(msgId, paramMap);//出院	
		else if(triggerEvent.equals("A25"))
			return a25_cancelDisCharge(msgId, paramMap);//取消出院		
		else if(triggerEvent.equals("A32"))
			return a32_cancelDeptIn(msgId, paramMap);//取消入科 √
		else if(triggerEvent.equals("A08"))
			return a08_editPatiInfo(msgId, paramMap);//修改患者信息
		else if(triggerEvent.equals("A42"))
			return a42_bedChange(msgId, paramMap);//包床|换床
		else if(triggerEvent.equals("A54"))
			return a54_changeAttendDoctor(msgId, paramMap);//修改主治医生
		return null;
	}

	/**
	 * ADT^A01 ADT_A01 入院登记
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a01_admit(String msgId, Map<String, Object> paramMap){
		
		ADT_A01 adt01 = new ADT_A01();
		try {
		MSH msh = adt01.getMSH();
		MsgUtils.createMSHMsg(msh, msgId, "ADT","A01");
		
		EVN evn=adt01.getEVN();
		createEVNMsg(paramMap, evn ,"A01");
		
		PID pid = adt01.getPID();
		//MsgUtils.createPIDMsg(pid, paramMap);
		
		PV1 pv1 = adt01.getPV1();
		//MsgUtils.createPV1Msg(pv1, paramMap);
		
		qryAndSetPID_PV1(pid, pv1, paramMap);
		
		NK1 nk1 = adt01.getNK1();
		createNK1Msg(nk1, paramMap);
		
		if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(paramMap,"pkPv"))){
			//诊断
			DG1 dg1=adt01.getDG1(0);
			Map<String,Object> diagMap = DataBaseHelper.queryForMap("SELECT * FROM PV_DIAG WHERE pk_pv = ?", MsgUtils.getPropValueStr(paramMap,"pkPv"));
			//类别
			if(diagMap != null){
				dg1.getDiagnosisType().setValue("01");//入院诊断
				dg1.getDiagnosisCodeDG1().getIdentifier().setValue(MsgUtils.getPropValueStr(diagMap,"codeIcd"));//诊断编码
				dg1.getDiagnosisDescription().setValue(MsgUtils.getPropValueStr(diagMap,"nameDiag"));//诊断名称
				//1、主要诊断 2、次要诊断
				if(diagMap.get("flagMaj")!=null&&diagMap.get("flagMaj").toString().equals("1")){
					dg1.getDiagnosisPriority().setValue("1");
				}else{
					dg1.getDiagnosisPriority().setValue("2");
				}
			}
			dg1.getDiagnosingClinician(0).getIDNumber().setValue(MsgUtils.getPropValueStr(paramMap,"codeEmpTre"));
			dg1.getDiagnosingClinician(0).getFamilyName().getSurname().setValue(MsgUtils.getPropValueStr(paramMap,"nameEmpTre"));
		}
		
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("A01{}",e.getMessage());
		}
		return adt01;
	}	
	
	/**
	 * ADT^A01 ADT_A01 婴儿 - 入院登记
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a01_admitInf(String msgId, Map<String, Object> paramMap){
		
		ADT_A01 adt01 = new ADT_A01();
		try {
			
		
		MSH msh = adt01.getMSH();
		MsgUtils.createMSHMsg(msh, msgId, "ADT","A01");
		
		EVN evn=adt01.getEVN();
		createEVNMsg(paramMap, evn,"A01");
		
		PID pid = adt01.getPID();
		PV1 pv1 = adt01.getPV1();
		Map<String, Object> patiMap = qryAndSetPID_PV1(pid, pv1, paramMap);
			
		PV2 pv2 = adt01.getPV2();
		pv2.getNewbornBabyIndicator().setValue("Y");//婴儿
		
		//联系人
		NK1 nk1=adt01.getNK1();
		createNK1Msg(nk1, patiMap);
		} catch (Exception e) {
			loger.error("婴A01{}",e.getMessage());
		}
		return adt01;
	}

	
	/**
	 * ADT^A02 ADT_A02转科
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a02_deptChange(String msgId, Map<String, Object> paramMap){
		
		ADT_A02 adt02=new ADT_A02();
		
		MSH msh = adt02.getMSH();
		MsgUtils.createMSHMsg(msh, msgId, "ADT", "A02");
		
		EVN evn=adt02.getEVN();
		createEVNMsg(paramMap, evn, "A02");
		
		PID pid = adt02.getPID();
		PV1 pv1 = adt02.getPV1();
		paramMap.put("EVNopeType", "A02");
		qryAndSetPID_PV1(pid, pv1, paramMap);
		
		return adt02;
	}
	
	/**
	 * 发送出院结算信息ADT^A03
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public Message sendADTA03Msg(String msgId, Map<String, Object> paramMap){

			ADT_A03 adt03 = new ADT_A03();
			//出院结算
			MSH msh = adt03.getMSH();
			MsgUtils.createMSHMsg(msh, msgId, "ADT","A03");
			
			EVN evn=adt03.getEVN();
			createEVNMsg(paramMap, evn, "A03");
			
			PID pid = adt03.getPID();
			PV1 pv1 = adt03.getPV1(); 
		
			qryAndSetPID_PV1(pid, pv1, paramMap);
			
			return adt03;
	}
	
	
	/**
	 * ADT^A04 ADT_A01门诊挂号
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a04_opDeptIn(String msgId, Map<String, Object> paramMap){
		
		ADT_A01 adt01=new ADT_A01();
		try {
			
		MSH msh = adt01.getMSH();
		MsgUtils.createMSHMsg(msh, msgId, "ADT", "A04");
		
		EVN evn=adt01.getEVN();
		createEVNMsg(paramMap, evn, "A04");
		
		PID pid = adt01.getPID();
		PV1 pv1 = adt01.getPV1();
		
		//1、查询门诊患者基本+就诊信息
		List<Map<String, Object>> pati = msgRecMapper.queryPatListOp(paramMap);
		paramMap.putAll(pati.get(0));
		//2、处理患者基本信息
		MsgUtils.createPIDMsg(pid, paramMap);
		//3、处理患者就诊信息
		MsgUtils.createPV1Msg(pv1, paramMap);
		
		//单独处理pv1-17[挂号医师]
		pv1.getAdmittingDoctor(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());;
		pv1.getAdmittingDoctor(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
		
		pv1.getAccountStatus().setValue("");
		NK1 nk1=adt01.getNK1();
		createNK1Msg(nk1, paramMap);
		} catch (Exception e) {
			loger.error("A04{}",e.getMessage());
		}
		return adt01;
	}

	/**
	 * ADT^A08 ADT_A01修改患者信息
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a08_editPatiInfo(String msgId, Map<String, Object> paramMap){

		ADT_A01 adt01=new ADT_A01();
		
		MSH msh = adt01.getMSH();
		MsgUtils.createMSHMsg(msh, msgId, "ADT", "A08");
		
		EVN evn=adt01.getEVN();
		createEVNMsg(paramMap, evn, "A08");
		
		PID pid = adt01.getPID();
		PV1 pv1 = adt01.getPV1();
		qryAndSetPID_PV1(pid, pv1, paramMap);
		
		//联系人
		NK1 nk1=adt01.getNK1();
		createNK1Msg(nk1, paramMap);		
		return adt01;
	}
	
	/**
	 * ADT^A10 ADT_A09 入科
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a10_ipDeptIn(String msgId, Map<String, Object> paramMap){
		
		ADT_A09 adt09 = new ADT_A09();
		
		MSH msh = adt09.getMSH();
		MsgUtils.createMSHMsg(msh, msgId, "ADT","A10");
		
		EVN evn=adt09.getEVN();
		createEVNMsg(paramMap, evn,"A10");
		
		PID pid = adt09.getPID();
		PV1 pv1 = adt09.getPV1();
		qryAndSetPID_PV1(pid, pv1, paramMap);
		
		return adt09;
	}
	
	/**
	 * ADT^A11 ADT_A09 取消入院 | 婴儿撤销登记 | 门诊退号
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a11_cancelAdmitOrVisit(String msgId, Map<String, Object> paramMap){
		
		ADT_A09 adt09 =new ADT_A09();
		
		MSH msh = adt09.getMSH();
		MsgUtils.createMSHMsg(msh, msgId, "ADT", "A11");
				
		EVN evn=adt09.getEVN();
		createEVNMsg(paramMap, evn, "A11");
		PID pid = adt09.getPID();
		PV1 pv1 = adt09.getPV1();
		paramMap.remove("pkPi");
		qryAndSetPID_PV1(pid, pv1, paramMap);
		
		return adt09;
	}

	/**
	 * ADT^A16  ADT_A16出院
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a16_discharge(String msgId, Map<String, Object> paramMap){
		
		ADT_A16 adt16 = new ADT_A16();
		
		MSH msh = adt16.getMSH();
		MsgUtils.createMSHMsg(msh, msgId, "ADT", "A16");
		
		EVN evn=adt16.getEVN();
		createEVNMsg(paramMap, evn, "A16");
		
		PID pid = adt16.getPID();
		PV1 pv1 = adt16.getPV1();
		qryAndSetPID_PV1(pid, pv1, paramMap);
		
		return adt16;
	}

	/**
	 * ADT^A25 ADT_21 取消出院
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a25_cancelDisCharge(String msgId, Map<String, Object> paramMap){
		
		ADT_A21 adt21 = new ADT_A21();

		MSH msh = adt21.getMSH();
		MsgUtils.createMSHMsg(msh, msgId, "ADT", "A25");
		
		EVN evn=adt21.getEVN();
		createEVNMsg(paramMap, evn, "A25");
		
		PID pid = adt21.getPID();
		PV1 pv1 = adt21.getPV1();
		qryAndSetPID_PV1(pid, pv1, paramMap);
		return adt21;
	}

	/**
	 * ADT^A32 ADT_A21 取消入科
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a32_cancelDeptIn(String msgId, Map<String, Object> paramMap){

		ADT_A21 adt21 = new ADT_A21();
		
		MSH msh = adt21.getMSH();
		MsgUtils.createMSHMsg(msh, msgId, "ADT", "A32");
		
		EVN evn=adt21.getEVN();
		createEVNMsg(paramMap, evn, "A32");
		
		PID pid = adt21.getPID();
		PV1 pv1 = adt21.getPV1();
		qryAndSetPID_PV1(pid, pv1, paramMap);

		return adt21;
	}

	/**
	 * ADT^A42 ADT_A39包床 | 换床 
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	@SuppressWarnings("unchecked")
	private Message a42_bedChange(String msgId, Map<String, Object> paramMap){
		
		ADT_A39 adt39 = new ADT_A39();
		try {
			
		MSH msh = adt39.getMSH();
		MsgUtils.createMSHMsg(msh, msgId, "ADT", "A42");
		
		EVN evn=adt39.getEVN();
		createEVNMsg(paramMap, evn, "A42");
		
		if(CommonUtils.isEmptyString(MsgUtils.getPropValueStr(paramMap,"bedStatus"))){
			//换床
			//1、查询患者_1基本信息
			Map<String, Object> pati1 = new HashMap<String, Object>();
			pati1.put("pkPv", MsgUtils.getPropValueStr(paramMap,"pkPv"));
			//添加床位信息，推送换床使用
			pati1.put("bednoDes", MsgUtils.getPropValueStr(paramMap,"bednoDes"));
			PID pid_1 = adt39.getPATIENT(0).getPID();
			PV1 pv1_1 = adt39.getPATIENT(0).getPV1();
			qryAndSetPID_PV1(pid_1, pv1_1, pati1);
			
			//2、如果和患者互换，则查询 & 组织患者_2基本信息
			if(!CommonUtils.isEmptyString(MsgUtils.getPropValueStr(paramMap,"pkPvDes"))){
				Map<String, Object> pati2 = new HashMap<String, Object>();
				pati2.put("pkPv", MsgUtils.getPropValueStr(paramMap,"pkPvDes"));
				//添加床位信息，推送换床使用
				pati1.put("bednoDes", MsgUtils.getPropValueStr(paramMap,"bednoDes"));
				PID pid_2 = adt39.getPATIENT(1).getPID();
				PV1 pv1_2 = adt39.getPATIENT(1).getPV1();
				qryAndSetPID_PV1(pid_2, pv1_2, pati2);
			}
		}
		else
		{
			//包床 | 退包床
			List<String> beds = (List<String>)paramMap.get("bednos");
			if(beds.size() < 1) return null;
			
			//1、查询患者基本信息
			Map<String,Object> pati = new HashMap<String, Object>();
			pati.put("pkPv", CommonUtils.getString(paramMap.get("pkPv")));
			pati = qryPID_PV1(pati);
			
			for(int i = 0 ; i < beds.size() ; i++){
				PID pid = adt39.getPATIENT(i).getPID();
				MsgUtils.createPIDMsg(pid, pati);
				PV1 pv1 = adt39.getPATIENT(i).getPV1();
				MsgUtils.createPV1Msg(pv1, pati);
				pv1.getBedStatus().setValue(MsgUtils.getPropValueStr(paramMap,"bedStatus"));
				pv1.getAssignedPatientLocation().getBed().setValue(beds.get(i).toString());
			}
		}

		} catch (Exception e) {
			// TODO: handle exception
			loger.error("pkPv{},A42{}",MsgUtils.getPropValueStr(paramMap,"pkPv"),e.getMessage());
		}
		return adt39;
	}
	
	/**
	 * ADT^A54 ADT_A54改变主治医生
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a54_changeAttendDoctor(String msgId, Map<String, Object> paramMap){
		
		ADT_A54 adt54 = new ADT_A54();
		
		MSH msh = adt54.getMSH();
		MsgUtils.createMSHMsg(msh, msgId, "ADT", "A54");

		EVN evn=adt54.getEVN();
		createEVNMsg(paramMap, evn, "A54");
		
		PID pid = adt54.getPID();
		PV1 pv1 = adt54.getPV1();
		qryAndSetPID_PV1(pid, pv1, paramMap);
		
		return adt54;
	}
	
	/**
	 * 查询并设置PID，PV1，返回患者基本信息
	 * @param pid
	 * @param pv1
	 * @param patMap
	 * @return
	 */
	public Map<String,Object> qryAndSetPID_PV1(PID pid,PV1 pv1,Map<String,Object> patMap){
		//1、查询患者基本信息
		patMap = qryPID_PV1(patMap);
		//2、处理患者基本信息
		MsgUtils.createPIDMsg(pid, patMap);
		//3、处理患者就诊信息
		MsgUtils.createPV1Msg(pv1, patMap);
		return patMap;
	}
	
	/**
	 * 查询患者基本信息PID，PV1
	 * @param pid
	 * @param pv1
	 * @param patMap
	 * @return
	 */
	public Map<String,Object> qryPID_PV1(Map<String,Object> patMap){
		//1、查询患者基本信息
		List<Map<String, Object>> pati = msgRecMapper.queryPatListIp(patMap);
		patMap.putAll(pati.get(0));
		return patMap;
	}
	
	
	/**
	 * 联系人相关信息
	 * @param nk1
	 * @param patiMap
	 * @throws DataTypeException
	 */
	private void createNK1Msg(NK1 nk1, Map<String, Object> patiMap){
		try {
			if(patiMap!=null){
			nk1.getNKName(0).getFamilyName().getSurname().setValue(MsgUtils.getPropValueStr(patiMap,"nameRel"));
			//关系
			nk1.getRelationship().getIdentifier().setValue(MsgUtils.getPropValueStr(patiMap,"dtRalation"));
			nk1.getRelationship().getText().setValue(MsgUtils.getPropValueStr(patiMap,"ralationName"));
			//地址
			/*String cc = MsgUtils.getPropValueStr(patiMap,"addrRel");
			nk1.getAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(cc);*/
			nk1.getAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(MsgUtils.getPropValueStr(patiMap,"addrCur"));
			nk1.getAddress(0).getZipOrPostalCode().setValue(MsgUtils.getPropValueStr(patiMap,"postcodeCur"));
			nk1.getAddress(0).getAddressType().setValue("H");
			nk1.getAddress(1).getStreetAddress().getStreetOrMailingAddress().setValue(MsgUtils.getPropValueStr(patiMap,"addrRegi"));
			nk1.getAddress(1).getZipOrPostalCode().setValue(MsgUtils.getPropValueStr(patiMap,"postcodeRegi"));
			nk1.getAddress(1).getAddressType().setValue("O");
			//电话
			nk1.getPhoneNumber(0).getAnyText().setValue(MsgUtils.getPropValueStr(patiMap,"telRel"));
			}
		} catch (Exception e) {
			loger.error("关系NK1{}",e.getMessage());
		}
	}	

	/**
	 * 创建EVN
	 * @param paramMap
	 * @param evn
	 * @throws DataTypeException
	 */
	private void createEVNMsg(Map<String, Object> paramMap, EVN evn , String opeType){
		try {
	    String stringDate = MsgUtils.PropDateSting(new Date());
		evn.getEventTypeCode().setValue(opeType);
		evn.getRecordedDateTime().getTimeOfAnEvent().setValue(stringDate);
		evn.getDateTimePlannedEvent().getTimeOfAnEvent().setValue(stringDate);
		evn.getEventReasonCode().setValue("01");
		evn.getOperatorID(0).getIDNumber().setValue(MsgUtils.getPropValueStr(paramMap,"codeEmp"));
		evn.getOperatorID(0).getFamilyName().getSurname().setValue(MsgUtils.getPropValueStr(paramMap,"nameEmp"));
		evn.getEventOccurred().getTimeOfAnEvent().setValue(stringDate);
		evn.getEventFacility().getNamespaceID().setValue("NHIS");
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("EVN事件{},{}",opeType,e.getMessage());
		}
	}

}
