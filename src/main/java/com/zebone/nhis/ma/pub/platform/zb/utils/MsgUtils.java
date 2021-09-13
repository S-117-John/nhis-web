package com.zebone.nhis.ma.pub.platform.zb.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.message.ACK;
import ca.uhn.hl7v2.model.v24.message.OMP_O09;
import ca.uhn.hl7v2.model.v24.message.ORL_O22;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.FT1;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.zb.vo.MsgTreeNode;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * Hl7消息工具类
 * @author chengjia
 *
 */
@Service
public class MsgUtils {
	private static Logger loger = LoggerFactory.getLogger("nhis.nhis.lbHl7Log");
    private EncodingCharacters encChars=new EncodingCharacters('|', null);

	private static HapiContext context = null;
	private static Parser parser=null;
	
	private static PipeParser pipeParser=null;
	
	public static Parser getParser(){
		if(context==null) context = new DefaultHapiContext();
		if(parser==null) parser = context.getGenericParser();
		
		return parser;
	}
	
	public static Message getMessage(String msg){
		try {
			Message hapiMsg = getParser().parse(msg);
			
			return hapiMsg;
		} catch (HL7Exception e) {
			return null;
		}
	}

	public static MSH getMessageMsh(String msg){
		try {
			Message hapiMsg = getParser().parse(msg);
			Segment segment = (Segment) hapiMsg.get("MSH");
			MSH msh=(MSH)segment;
			
			return msh;
		} catch (HL7Exception e) {
			return null;
		}
	}
	
	
	public static void createMSHMsg(MSH msh,String msgId,String msgType,String triggerEvent){
		try {
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getDateTimeOfMessage().getTimeOfAnEvent().setValue(PropDateSting(new Date()));
			msh.getSendingApplication().getNamespaceID().setValue("NHIS");
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");//@todo
			msh.getMessageType().getMessageType().setValue(msgType);
			msh.getMessageType().getTriggerEvent().setValue(triggerEvent);
			msh.getMessageControlID().setValue(msgId);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			msh.getAcceptAcknowledgmentType().setValue("AL");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			msh.getCountryCode().setValue("CHN");
			msh.getCharacterSet(0).setValue("GBK");
		} catch (Exception e) {
			loger.error("创建MSH消息失败{}",e.getMessage());
		}
	}
	
	
	public static void createMSHMsgCn(MSH msh,String msgId,String msgType,String triggerEvent){
		try {
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getDateTimeOfMessage().getTimeOfAnEvent().setValue(PropDateSting(new Date()));
			msh.getMessageType().getMessageType().setValue(msgType);
			msh.getMessageType().getTriggerEvent().setValue(triggerEvent);
			msh.getMessageControlID().setValue(msgId);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			msh.getSendingApplication().getNamespaceID().setValue("NHIS");
			if (triggerEvent == "O21" && msgType == "OML") {
				msh.getReceivingApplication().getNamespaceID().setValue("LIS");//@todo
			}else {
				msh.getReceivingApplication().getNamespaceID().setValue("HIS-RIS");//@todo
			}
			msh.getAcceptAcknowledgmentType().setValue("AL");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			msh.getCountryCode().setValue("CHN");
			msh.getCharacterSet(0).setValue("GBK");
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("创建MSH消息失败{}",e.getMessage());
		}
	}
	
	/**
	 * 创建LB-MSH
	 *单条发送检查申请 
	 *
	 */
	public static void createMSHMsgCnLbPacs(MSH msh,String msgId,String msgType,String triggerEvent,String codeDept){
		try {
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getDateTimeOfMessage().getTimeOfAnEvent().setValue(PropDateSting(new Date()));
			msh.getMessageType().getMessageType().setValue(msgType);
			msh.getMessageType().getTriggerEvent().setValue(triggerEvent);
			msh.getMessageControlID().setValue(msgId);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			msh.getSendingApplication().getNamespaceID().setValue("NHIS");
			//根据执行科室判断检查发送消息头
			//放射科室>>>>影像科室
			if (("0309").equals(codeDept)) {
				msh.getReceivingApplication().getNamespaceID().setValue("LWRIS");
			}
			//超声科室
			else if(("0303").equals(codeDept)){
				msh.getReceivingApplication().getNamespaceID().setValue("LWUS");
			}
			//内镜科室
			else if(("0305").equals(codeDept)){
				msh.getReceivingApplication().getNamespaceID().setValue("LWEIS");
			}
			else {
				msh.getReceivingApplication().getNamespaceID().setValue("HIS-RIS");//@todo
			}
			msh.getAcceptAcknowledgmentType().setValue("AL");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			msh.getCountryCode().setValue("CHN");
			msh.getCharacterSet(0).setValue("GBK");
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("创建MSH消息失败{}",e.getMessage());
		}
	}
	
	/**
	 * EVN消息段
	 * @param evn
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public static void createEVNMsg(EVN evn,Map<String, Object> paramMap){
		try {
			//evn-1
			evn.getEventTypeCode().setValue(getPropValueStr(paramMap,"triggerEvent"));
			//evn-2
			evn.getRecordedDateTime().getTimeOfAnEvent().setValue(DateUtils.getDateTimeStr(new Date()));
			//evn-3
			evn.getDateTimePlannedEvent().getTimeOfAnEvent().setValue(PropDateSting(new Date()));
			//evn-4
			evn.getEventReasonCode().setValue("01");
			//evn-5.1操作员编码
			evn.getOperatorID(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
			//evn-5.2操作员名字
			evn.getOperatorID(0).getFamilyName().getSurname().setValue(UserContext.getUser().getNameEmp());
			//evn-7
			evn.getEventOccurred().getTimeOfAnEvent().setValue(PropDateSting(new Date()));
			//evn-8
			evn.getEventFacility().getNamespaceID().setValue("NHIS");
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("创建evn消息失败{}",e.getMessage());
		}
	}
	
	/**
	 * 创建PID
	 * @param pid
	 * @param patMap
	 */
	public static void createPIDMsg(PID pid,Map<String,Object> patMap){
		if(pid==null) return;
		try {
			//PID_2 ID
			pid.getPatientID().getID().setValue(getPropValueStr(patMap,"codePi"));
			//PID_3 卡号
			pid.getPatientIdentifierList(0).getID().setValue(getPropValueStr(patMap,"cardno"));
			pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("IDCard");
			//PID_3 住院号
			pid.getPatientIdentifierList(1).getID().setValue(getPropValueStr(patMap,"codeIp"));
			pid.getPatientIdentifierList(1).getIdentifierTypeCode().setValue("PatientNO");
			//PID_3 身份证号
			if("01".equals(getPropValueStr(patMap,"dtIdtype")))
				pid.getPatientIdentifierList(2).getID().setValue(getPropValueStr(patMap,"idNo"));
			else
				pid.getPatientIdentifierList(2).getID().setValue("");
			pid.getPatientIdentifierList(2).getIdentifierTypeCode().setValue("IdentifyNO");
			//PID_3 门诊号
			pid.getPatientIdentifierList(3).getID().setValue(getPropValueStr(patMap,"codeOp"));
			pid.getPatientIdentifierList(3).getIdentifierTypeCode().setValue("Outpatient");
			//PID_5 姓名
			pid.getPatientName(0).getFamilyName().getSurname().setValue(getPropValueStr(patMap,"namePi"));
			//PID_6 母亲姓名
			//pid.getMotherSMaidenName(0).getFamilyName().getSurname().setValue(getPropValueStr(patMap,"namePiMa"));
			//PID_7 生日
			if(null != getPropValueDateSting(patMap,"birthDate")){
				pid.getDateTimeOfBirth().getTimeOfAnEvent().setValue(getPropValueDateSting(patMap,"birthDate"));
			}
			//PID_8 性别 - 调用转换方法 getSex
			pid.getAdministrativeSex().setValue(getSex(getPropValueStr(patMap,"dtSex")));
			//PID_11 患者地址
			pid.getPatientAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(getPropValueStr(patMap,"addrCur"));
			pid.getPatientAddress(0).getZipOrPostalCode().setValue(getPropValueStr(patMap,"postcodeCur"));
			pid.getPatientAddress(0).getAddressType().setValue("H");
			pid.getPatientAddress(1).getStreetAddress().getStreetOrMailingAddress().setValue(getPropValueStr(patMap,"addrRegi"));
			pid.getPatientAddress(1).getZipOrPostalCode().setValue(getPropValueStr(patMap,"postcodeRegi"));
			pid.getPatientAddress(1).getAddressType().setValue("O");
			pid.getPatientAddress(2).getStreetAddress().getStreetOrMailingAddress().setValue(getPropValueStr(patMap,"addrRegi"));
			pid.getPatientAddress(2).getZipOrPostalCode().setValue(getPropValueStr(patMap,"postcodeRegi"));
			pid.getPatientAddress(2).getAddressType().setValue("N");
			
			//PID_13.9 家庭电话
			if(StringUtils.isNotBlank(getPropValueStr(patMap,"mobile"))){
				pid.getPhoneNumberHome(0).getAnyText().setValue(getPropValueStr(patMap,"mobile"));
			}else if(StringUtils.isNotBlank(getPropValueStr(patMap,"telRel"))){
				pid.getPhoneNumberHome(0).getAnyText().setValue(getPropValueStr(patMap,"telRel"));
			}
			//pid.getPhoneNumberHome(0).getPhoneNumber().setValue(getPropValueStr(patMap,"mobile"));
			//PID_14.9 工作电话
			pid.getPhoneNumberBusiness(0).getAnyText().setValue(getPropValueStr(patMap,"telRel"));
			//PID_16 婚姻 - 调用转换方法 getDtMarry
			pid.getMaritalStatus().getIdentifier().setValue(getDtMarry(getPropValueStr(patMap,"dtMarry")));
			pid.getMaritalStatus().getText().setValue(getDtMarryText(getPropValueStr(patMap,"dtMarry")));
			//PID_19 社会保险号
			pid.getSSNNumberPatient().setValue(getPropValueStr(patMap,"insurNo"));
			//PID_21 母亲的ID
			//pid.getMotherSIdentifier(0).getID().setValue(MsgUtils.getPropValueStr(patMap,"codePiMa"));
			//PID_22 民族
			pid.getEthnicGroup(0).getIdentifier().setValue(getPropValueStr(patMap,"dtNation"));
			pid.getEthnicGroup(0).getText().setValue(getPropValueStr(patMap,"nationName"));
			//PID_23 出生地
			pid.getBirthPlace().setValue(getPropValueStr(patMap,"addrBirth"));
			//PID_25 出生顺序
			//pid.getBirthOrder().setValue(getPropValueStr(patMap,"sortNo"));
			//PID_28.1 国别
			pid.getNationality().getIdentifier().setValue(getPropValueStr(patMap,"dtCountry"));
			pid.getNationality().getText().setValue(getPropValueStr(patMap,"countryName"));
			
			//PID_35 种类代码？？？ 籍贯？？
			pid.getSpeciesCode().getIdentifier().setValue(getPropValueStr(patMap,"addrOrigin"));
			pid.getSpeciesCode().getText().setValue("籍贯");
			
			//PID_36
			pid.getBreedCode().getIdentifier().setValue(MsgUtils.getPropValueStr(patMap,"insuCode"));
			pid.getBreedCode().getText().setValue(MsgUtils.getPropValueStr(patMap,"insuName"));
			//PID_38 合同单位 -- 同步单位字典？？？
			pid.getProductionClassCode().getIdentifier().setValue(MsgUtils.getPropValueStr(patMap,"insuCode"));
			pid.getProductionClassCode().getText().setValue(MsgUtils.getPropValueStr(patMap,"insuName"));
			
		} catch (Exception e) {
			loger.error("创建PID消息失败{}",e.getMessage());
		}
		
	}

	/**
	 * 创建PV1
	 * @param pv1
	 * @param patMap
	 * @throws DataTypeException 
	 */
	public static void createPV1Msg(PV1 pv1,Map<String,Object> patMap){
		if(pv1==null) return ;
		try {
			String euPvType = getPropValueStr(patMap,"euPvtype");
			if(euPvType != null && euPvType.equals("1"))//门诊
			{
				//【PV1-02】患者类别 - 门诊O
				pv1.getPatientClass().setValue("O");
				//【PV1-04】入院类型 - 门诊对应就诊服务类型
				pv1.getAdmissionType().setValue(getPvInType(getPropValueStr(patMap,"euSrvtype"), "", euPvType));
				//【PV1-13】再次入院标识 - 门诊次数
				pv1.getReAdmissionIndicator().setValue(getPropValueStr(patMap,"opTimes"));
				//【PV1-26】合同总量-是否允许调阅健康档案 1允许，0不允许 ？？？
				pv1.getContractAmount(0).setValue("0");
			}else
			if(euPvType != null && euPvType.equals("2"))//急诊
			{
				//【PV1-02】患者类别 - 门诊O
				pv1.getPatientClass().setValue("O");
				//【PV1-04】入院类型 - 门诊对应就诊服务类型
				pv1.getAdmissionType().setValue(getPvInType(getPropValueStr(patMap,"euSrvtype"), "", euPvType));
				//【PV1-13】再次入院标识 - 门诊次数
				pv1.getReAdmissionIndicator().setValue(getPropValueStr(patMap,"opTimes"));
				//【PV1-26】合同总量-是否允许调阅健康档案 1允许，0不允许 ？？？
				pv1.getContractAmount(0).setValue("0");
			}
			else if(euPvType != null)//住院
			{
				//【PV1-02】患者类别 - 住院I
				pv1.getPatientClass().setValue("I");
				//【PV1-3.1】患者当前位置-病区
				pv1.getAssignedPatientLocation().getPointOfCare().setValue(getPropValueStr(patMap,"codeDeptNs"));
				//【PV1-3.3】患者当前位置-床位
				if(null != getPropValueStr(patMap,"bednoDes") && !("").equals(getPropValueStr(patMap,"bednoDes"))){
					pv1.getAssignedPatientLocation().getBed().setValue(getPropValueStr(patMap,"bednoDes"));
				}else if(null != getPropValueStr(patMap,"bedNo") && !("").equals(getPropValueStr(patMap,"bedNo"))){
					pv1.getAssignedPatientLocation().getBed().setValue(getPropValueStr(patMap,"bedNo"));
				}else if(null != getPropValueStr(patMap,"codeIpBed")&& !("").equals(getPropValueStr(patMap,"codeIpBed"))){
					pv1.getAssignedPatientLocation().getBed().setValue(getPropValueStr(patMap,"codeIpBed"));
				}
				//【PV1-04】入院类型
				pv1.getAdmissionType().setValue(getPvInType(getPropValueStr(patMap,"dtLevelDise"), getPropValueStr(patMap,"flagInf"), euPvType));//通过方法统一处理
				//【PV1-13】再次入院标识 - 住院次数
				pv1.getReAdmissionIndicator().setValue(getPropValueStr(patMap,"ipTimes"));
				//【PV1-14】 入院来源
				pv1.getAdmitSource().setValue(""); //参考入院来源字典 - 未确定？？？
			}
			//【PV1-3.4】患者当前位置-机构 => 科室
			pv1.getAssignedPatientLocation().getFacility().getNamespaceID().setValue(getPropValueStr(patMap,"codeDept"));
			pv1.getAssignedPatientLocation().getLocationStatus().setValue(getPropValueStr(patMap,"nameDept"));
			//【PV1-7】 主管医生
			pv1.getAttendingDoctor(0).getIDNumber().setValue(getPropValueStr(patMap,"codeEmpPhy"));
			pv1.getAttendingDoctor(0).getFamilyName().getSurname().setValue(getPropValueStr(patMap,"nameEmpPhy"));
			//【PV1-16】 VIP标识符 - 1：是 0：否 - 未确定？？？
 			pv1.getVIPIndicator().setValue("0");
			//【PV1-17】 入院医生 = 收治医生
			pv1.getAdmittingDoctor(0).getIDNumber().setValue(getPropValueStr(patMap,"codeEmpTre"));
  			pv1.getAdmittingDoctor(0).getFamilyName().getSurname().setValue(getPropValueStr(patMap,"nameEmpTre"));
			//【PV1-18】 患者类别 - 参照患者结算类别 01自费,02医保,03公费
			pv1.getPatientType().setValue(getPatiType(getPropValueStr(patMap,"euHptype")));
			//【PV1-19】 就诊号码 his visit no
			pv1.getVisitNumber().getID().setValue(getPropValueStr(patMap,"codePv"));
			//【PV1-44】入院日期 - 入科（就诊时间)
			if(!CommonUtils.isEmptyString(getPropValueStr(patMap,"dateAdmit"))) {
				pv1.getAdmitDateTime().getTimeOfAnEvent().setValue(getPropValueDateSting(patMap,"dateAdmit"));
			}else{
				if(null != getPropValueDateSting(patMap,"dateBegin") && !("").equals(getPropValueDateSting(patMap,"dateBegin"))){
					pv1.getAdmitDateTime().getTimeOfAnEvent().setValue(getPropValueDateSting(patMap,"dateBegin"));
				}
			}
			//【PV1-45】出院日期effective_e
			if("1".equals(getPropValueStr(patMap,"flagCancel"))){
				//取消就诊时，退诊日期
				if(!CommonUtils.isEmptyString(getPropValueStr(patMap,"dateCancel"))){
					pv1.insertDischargeDateTime(0);
					pv1.getDischargeDateTime(0).getTimeOfAnEvent().setValue(getPropValueDateSting(patMap,"dateCancel"));
				}
			}else{
				if(("A02").equals(patMap.get("EVNopeType"))){
					 //lb-电子病历-转科传患者转科时出科时间。
					System.out.println(DateUtils.addDate(new Date(), -20, 5, "yyyyMMddHHmmss"));
					String pkPv=getPropValueStr(patMap,"pkPv");
					String pkDeptNs=getPropValueStr(patMap,"pkDeptNs");
					if(StringUtils.isNotEmpty(pkPv) && StringUtils.isNotEmpty(pkDeptNs)){
						//转科，上次病区记录出科时间
						String dateEnd = getPropValueStr(DataBaseHelper.queryForList("select TO_CHAR(DATE_END,'YYYYMMDDHH24MISS') DATE_END from pv_adt where pk_pv=? and DATE_END is NOT  null  ORDER BY DATE_END DESC",pkPv).get(0),"dateEnd");
					    if(StringUtils.isNotEmpty(dateEnd)){
					    	pv1.getDischargeDateTime(0).getTimeOfAnEvent().setValue(dateEnd);
					    }else{//获取时间为空时默认传递一个前20分钟时间
					    	pv1.getDischargeDateTime(0).getTimeOfAnEvent().setValue(DateUtils.addDate(new Date(), -20, 5, "yyyyMMddHHmmss"));
					    }
					}else{//获取时间为空时默认传递一个前20分钟时间
				    	pv1.getDischargeDateTime(0).getTimeOfAnEvent().setValue(DateUtils.addDate(new Date(), -20, 5, "yyyyMMddHHmmss"));
				    }
				}else //出院时，结束日期
					if(!CommonUtils.isEmptyString(getPropValueStr(patMap,"dateEnd"))){
						pv1.insertDischargeDateTime(0);
					    pv1.getDischargeDateTime(0).getTimeOfAnEvent().setValue(getPropValueDateSting(patMap,"dateEnd"));
				}
					
			}
		} catch (Exception e) {
			loger.error("创建PV消息失败{}",e.getMessage());
		}
		
	}
	
	/**
	 * 创建FT1消息
	 * @param ft1
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public static void createFT1Msg(FT1 ft1,Map<String, Object> paramMap){
		try {
			//ft1-1  //发生序号
			ft1.getSetIDFT1().setValue("1");
			//ft1-2  
			//预交金收据号
			ft1.getTransactionID().setValue(getPropValueStr(paramMap,"reptNo"));
			//ft1-4  收取时间（事务时间）
			ft1.getTransactionDate().getTimeOfAnEvent().setValue(PropDateSting(new Date()));
			//ft1-6	 //预交金 EU_DPTYPE 0 就诊结算；1 中途结算；2 结算冲账；3 欠费补缴；4 取消结算；9 住院预交金
			//"结算类别（请参照患者结算类别） 01：自费 02：医保 03：公费"
			if("9".equals(getPropValueStr(paramMap, "euDptype"))){
				//住院预交金为固定值
				ft1.getTransactionType().setValue("INPAY");
			}else{
				ft1.getTransactionType().setValue(getPropValueStr(paramMap, "amtType"));
			}
			//ft1-7
			ft1.getTransactionCode().getIdentifier().setValue(getPropValueStr(paramMap,"dtPaymode"));
			ft1.getTransactionCode().getText().setValue(getPropValueStr(paramMap,"dtPaymode"));
			//FT1-8：结算方式(1：门诊结算，2：住院中途结算，3：出院结算，4：门诊挂号)：EU_DPTYPE
			String dtSttype = getPropValueStr(paramMap, "dtSttype");
			switch (dtSttype){
				case "00":dtSttype="1";break;
				case "01":dtSttype="4";break;
				case "10":dtSttype="3";break;
				case "11":dtSttype="2";break;
				case "20":dtSttype="4";break;
				case "21":dtSttype="3";break;
			}
			//ft1-8
			ft1.getTransactionDescription().setValue(dtSttype);
			//ft1-11
			ft1.getTransactionAmountExtended().getPrice().getQuantity().setValue(getPropValueStr(paramMap,"amount"));
			//ft1-18  //患者类型 euPvtype  1 门诊，2 急诊，3 住院，4 体检，5 家床
			ft1.getPatientType().setValue(getPropValueStr(paramMap, "euPvtype"));
			//ft1-25	//预交金状态    euDirect  1收 -1退  平台：预交金状态 0:收取；1:作废;2:补打,3结算召回作废
			//ft1-26	//预交金状态
			String euDirect = getPropValueStr(paramMap,"euDirect");
			String flagCc = getPropValueStr(paramMap,"flagCc");
			if("1".equals(euDirect)){
				ft1.getProcedureCode().getIdentifier().setValue("0");
				ft1.getProcedureCodeModifier(0).getIdentifier().setValue("0");
			}else if("-1".equals(euDirect)){
				//判断是否结账 是结账为1
				if("1".equals(flagCc)){
					ft1.getProcedureCode().getIdentifier().setValue("3");
					ft1.getProcedureCodeModifier(0).getIdentifier().setValue("3");
				}else {
					ft1.getProcedureCode().getIdentifier().setValue("1");
					ft1.getProcedureCodeModifier(0).getIdentifier().setValue("1");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("创建预交金ft1消息失败{}",e.getMessage());
		}
	}
	
	public static void createMSAMsg(MSA msa,String msgId){
		try {
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgId);
			
		} catch (Exception e) {
			loger.error("创建MSA消息失败{}",e.getMessage());
		}
		
	}	
	
	@SuppressWarnings("unused")
	public static String[] getAckMsg(String msgId,Message message) throws HL7Exception{
		Segment segment = (Segment) message.get("MSH");
		if(segment==null){
			return null;
		}
		MSH msh;
		msh=(MSH)segment;
		MSA msa;
		if(msh==null){
			return null;
		}
		String[] ackMsgs=new String[2];
		ackMsgs[0]="";
		ackMsgs[1]="";
		String ackMsg="";
		//String errTxt="";
		String msgType=msh.getMessageType().getMessageType().getValue();
		String triggerEvent=msh.getMessageType().getTriggerEvent().getValue();
		String msgCntrlID=msh.getMessageControlID().getValue();
		
		loger.info("getAckMsg:"+msgType+"-"+triggerEvent);
		if(msgType==null) return null;
		ackMsgs[0]=msgType+"^"+triggerEvent;
		if(msgType.equals("ACK")){
			ACK ack = new ACK();
			
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
				
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);
			
			ackMsg = getParser().encode(ack);		
		}else if(msgType.equals("ADT")){
			ACK ack = new ACK();
			
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
				
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);

			
			ackMsg = getParser().encode(ack);
			
		}else if(msgType.equals("ORL")){
			//检验申请
			ORL_O22 ack = new ORL_O22();
			
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ORL", triggerEvent);
			
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
				
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);
			
			ackMsg = getParser().encode(ack);
		}else if(msgType.equals("ORL")){
			//检验申请收费
			ACK ack = new ACK();
			
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
				
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);
			
			ackMsg = getParser().encode(ack);		
		}else if(msgType.equals("ORP")){
			//门诊收费、退费
			ACK ack = new ACK();
			
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
				
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);

			
			ackMsg = getParser().encode(ack);

		}else{
			ACK ack = new ACK();
			
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
				
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);
			
			ackMsg = getParser().encode(ack);
		}
		
		//MSH
		ackMsgs[1]=ackMsg;
		
		return ackMsgs;
	}
		
	@SuppressWarnings("unused")
	public static String[] getAckMsg(String msgId,Message message,String result) throws HL7Exception{
		Segment segment = (Segment) message.get("MSH");
		if(segment==null){
			return null;
		}
		MSH msh;
		msh=(MSH)segment;
		MSA msa;
		if(msh==null){
			return null;
		}
		String[] ackMsgs=new String[2];
		ackMsgs[0]="";
		ackMsgs[1]="";
		String ackMsg="";
		//String errTxt="";
		String msgType=msh.getMessageType().getMessageType().getValue();
		String triggerEvent=msh.getMessageType().getTriggerEvent().getValue();
		String msgCntrlID=msh.getMessageControlID().getValue();
		
		loger.info("getAckMsg:"+msgType+"-"+triggerEvent);
		if(msgType==null) return null;
		ackMsgs[0]=msgType+"^"+triggerEvent;
		if(msgType.equals("ACK")){
			ACK ack = new ACK();
			
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
				
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);
			
			ackMsg = getParser().encode(ack);		
		}else if(msgType.equals("ADT")){
			ACK ack = new ACK();
			
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
				
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);

			ackMsg = getParser().encode(ack);
			
		}else if(msgType.equals("ORL")){
			//检验申请
			ORL_O22 ack = new ORL_O22();
			
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ORL", triggerEvent);
			
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
				
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);
			
			ackMsg = getParser().encode(ack);
		}else if(msgType.equals("ORL")){
			//检验申请收费
			ACK ack = new ACK();
			
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
				
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);
			
			ackMsg = getParser().encode(ack);		
		}else if(msgType.equals("ORP")){
			//门诊收费、退费
			ACK ack = new ACK();
			
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
				
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);

			
			ackMsg = getParser().encode(ack);
			
		}else if(msgType.equals("OMP")){
			//医嘱消息响应
			ACK ack = new ACK();
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
			
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);
			msa.getTextMessage().setValue(result);
			
			OMP_O09 omp=(OMP_O09)message;
			//获取医嘱状态
	       	String OrderStatus = omp.getORDER(0).getORC().getOrderStatus().getValue();
	       	//获取系统名称
	       	String send = omp.getMSH().getSendingApplication().getNamespaceID().getValue();
	       	//mas-4
	       	msa.getExpectedSequenceNumber().setValue(send);
	       	//mas-5
			msa.getDelayedAcknowledgmentType().setValue(OrderStatus);
		
			ackMsg = getParser().encode(ack);

		}else{
			ACK ack = new ACK();
			
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
				
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);
			msa.getTextMessage().setValue(result);
			ackMsg = getParser().encode(ack);
		}
		
		//MSH
		ackMsgs[1]=ackMsg;
		
		return ackMsgs;
	}
	
	/**
	 * 取文本内容
	 * @param map
	 * @return
	 */
	public static String getPropValueStr(Map<String, Object> map,String key) {
		String value="" ;
		if(map.containsKey(key)){
			Object obj=map.get(key);
			value=obj==null?"":obj.toString();
		}
		return value;
	}
	
	/**
	 * 取日期内容
	 * @param map
	 * @return
	 */
	public static Date getPropValueDates(Map<String, Object> map,String key) {
		Date value=null;
		boolean have=map.containsKey(key);
		if(have){
			Object obj=map.get(key);
			if(obj==null) return null;
			if(obj instanceof Date )
			{
				return (Date)obj;//如果传入的就是Date类型的数据
			}
			String dateStr=obj.toString();
		    value = DateUtils.strToDate(dateStr, "yyyyMMddHHmmss");	
		}
		return value;
	}
	/**
	 * 取日期内容
	 * yyyyMMddHHmmss
	 * @param map
	 * @return String
	 */
	public static String getPropValueDateSting(Map<String, Object> map,String key) {
		return DateUtils.formatDate(getPropValueDates(map, key), "yyyyMMddHHmmss");
	}
	
	/**时间转换
	 * yyyyMMddHHmmss
	 * @param date
	 * @return String
	 */
	public static String PropDateSting(Date date) {
		return DateUtils.formatDate(date, "yyyyMMddHHmmss");
	}
	
	public static String getMsgId() {
		String msgId=UUID.randomUUID().toString().replace("-", ""); 
		return msgId;
	}

	public static String getPk() {
		String pk=UUID.randomUUID().toString().replace("-", ""); 
		return pk;
	}
	
	/**
	 * 创建消息树
	 * @param message
	 * @return
	 */
	public List<MsgTreeNode> createMsgTree(String msg) {
		Message message;
		MsgTreeNode root=new MsgTreeNode("");
		try {
			if(pipeParser==null)  pipeParser=new PipeParser();
			message = pipeParser.parse(msg);
			String className=message.getClass().getName();
			DefaultMutableTreeNode top = new DefaultMutableTreeNode(className);
			
			addChildren(message, top,root);

			return root.getChildren();
			
		} catch (HL7Exception e) {
			return root.getChildren();
		}
		
		
		 
	 }
	
	   /**
     * Adds the children of the given group under the given tree node.
     */
    private void addChildren(Group messParent, MutableTreeNode treeParent,MsgTreeNode parentNode) {
        String[] childNames = messParent.getNames();
        int currChild = 0;
        for (int i = 0; i < childNames.length; i++) {
            try {
                Structure[] childReps = messParent.getAll(childNames[i]);
                for (int j = 0; j < childReps.length; j++) {
                    DefaultMutableTreeNode newNode = null;
                    MsgTreeNode newTreeNode=null;
                    if (childReps[j] instanceof Group) {
                        String groupName = childReps[j].getClass().getName();
                        groupName = groupName.substring(groupName.lastIndexOf('.') + 1, groupName.length());
                        newNode = new DefaultMutableTreeNode(groupName + " (rep " + j + ")");
                        newTreeNode=new MsgTreeNode(groupName + " (rep " + j + ")");
                        
                        addChildren((Group)childReps[j], newNode,newTreeNode);
                    } else if (childReps[j] instanceof Segment) {
                    	if(pipeParser==null)  pipeParser=new PipeParser();
                        newNode = new DefaultMutableTreeNode(PipeParser.encode((Segment)childReps[j], encChars));
                    	
                        newTreeNode=new MsgTreeNode(PipeParser.encode((Segment)childReps[j], encChars));
                        addChildren((Segment)childReps[j], newNode,newTreeNode);
                      
                    }
                    treeParent.insert(newNode, currChild++);

                    if(parentNode.getChildren()==null) parentNode.setChildren(new ArrayList<MsgTreeNode>());
                	parentNode.getChildren().add(newTreeNode);
                }
            } catch (HL7Exception e) {

            }
        }
    }
    
    /**
     * Add fields of a segment to the tree ...
     */
    private void addChildren(Segment messParent, MutableTreeNode treeParent,MsgTreeNode parentNode) {
        int n = messParent.numFields();
        int currChild = 0;
        for (int i = 1; i <= n; i++) {
            try {
                Type[] reps = messParent.getField(i);
                for (int j = 0; j < reps.length; j++) {
                	if(pipeParser==null)  pipeParser=new PipeParser();
                    String field = PipeParser.encode(reps[j], encChars);
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("Field " + i + " rep " + j + " (" + getLabel(reps[j]) + "): " + field);
                    MsgTreeNode newTreeNode=new MsgTreeNode("Field " + i + " rep " + j + " (" + getLabel(reps[j]) + "): " + field);
                    addChildren(reps[j], newNode,newTreeNode);
                    treeParent.insert(newNode, currChild++);
                    if(parentNode.getChildren()==null) parentNode.setChildren(new ArrayList<MsgTreeNode>());	                    
                    if(newTreeNode!=null) parentNode.getChildren().add(newTreeNode);
                }
            } catch (HL7Exception e) {
               
            }
        }
    }
    
    /**
     * Adds children to the tree.  If the Type is a Varies, the Varies data are 
     * added under a new node called "Varies".  If there are extra components, 
     * these are added under a new node called "ExtraComponents"
     */
    private void addChildren(Type messParent, MutableTreeNode treeParent,MsgTreeNode parentNode) {
        if (Varies.class.isAssignableFrom(messParent.getClass())) {
            Type data = ((Varies) messParent).getData();
            DefaultMutableTreeNode dataNode = new DefaultMutableTreeNode(getLabel(data));
            MsgTreeNode dataTreeNode=new MsgTreeNode(getLabel(data));
            treeParent.insert(dataNode, 0);
            if(parentNode.getChildren()==null) parentNode.setChildren(new ArrayList<MsgTreeNode>());
            parentNode.getChildren().add(dataTreeNode);
            addChildren(data, dataNode,dataTreeNode);
        } else {
            if (Composite.class.isAssignableFrom(messParent.getClass())) {
               addChildren((Composite)messParent, treeParent,parentNode);
            } else if (Primitive.class.isAssignableFrom(messParent.getClass())) {
                addChildren((Primitive)messParent, treeParent,parentNode);
            }
            
            if (messParent.getExtraComponents().numComponents() > 0) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("ExtraComponents");
                
                MsgTreeNode newTreeNode=new MsgTreeNode("ExtraComponents");
                treeParent.insert(newNode, treeParent.getChildCount());
                if(parentNode.getChildren()==null) parentNode.setChildren(new ArrayList<MsgTreeNode>());
                parentNode.getChildren().add(newTreeNode);
                for (int i = 0; i < messParent.getExtraComponents().numComponents(); i++) {
                    DefaultMutableTreeNode variesNode = new DefaultMutableTreeNode("Varies");
                    MsgTreeNode node = new MsgTreeNode("Varies");
                    newNode.insert(variesNode, i);
                    if(newTreeNode.getChildren()==null) newTreeNode.setChildren(new ArrayList<MsgTreeNode>());
                    newTreeNode.getChildren().add(node);
                    addChildren(messParent.getExtraComponents().getComponent(i), variesNode,node);
                }
            }
        }
    }
    
    /**

/**
 * Adds components of a composite to the tree ...
 */
private void addChildren(Composite messParent, MutableTreeNode treeParent,MsgTreeNode parentNode) {
    Type[] components = messParent.getComponents();
    for (int i = 0; i < components.length; i++) {
        DefaultMutableTreeNode newNode;
        MsgTreeNode newTreeNode;
        
        newNode = new DefaultMutableTreeNode(getLabel(components[i]));
        newTreeNode = new MsgTreeNode(getLabel(components[i]));
        
        addChildren(components[i], newNode,newTreeNode);
        treeParent.insert(newNode, i);
        if(parentNode.getChildren()==null) parentNode.setChildren(new ArrayList<MsgTreeNode>());
        parentNode.getChildren().add(newTreeNode);
    }
}	    
    /**
     * Adds single text value to tree as a leaf
     */
    private void addChildren(Primitive messParent, MutableTreeNode treeParent,MsgTreeNode parentNode) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(messParent.getValue());
        MsgTreeNode newTreeNode=new MsgTreeNode(messParent.getValue());
        
        treeParent.insert(newNode, 0);
        if(parentNode.getChildren()==null) parentNode.setChildren(new ArrayList<MsgTreeNode>());
        parentNode.getChildren().add(newTreeNode);

    }	 
    
    /**
     * Returns the unqualified class name as a label for tree nodes. 
     */
    private static String getLabel(Object o) {
        String name = o.getClass().getName();
        return name.substring(name.lastIndexOf('.')+1, name.length());
    }	      
    
    /**
     * 住院医嘱转换编码
     * @param dtMarry
     * @return
     */
    public static String getEuStatusOrdText(String dtMarry){
    	//平台：医嘱状态 ：1录入 2提交 3首次执行（确认） 4执行 5停止 6撤销
		//his：医嘱状态 ：0 开立；1 签署；2 核对；3 执行；4 停止；9 作废
    	String euStatus;
    	switch (dtMarry) {
		case "0":
			euStatus="1";
			break;
		case "1":
			euStatus="2";
			break;
		case "2":
			euStatus="3";
			break;
		case "3":
			euStatus="4";
			break;
		case "4":
			euStatus="5";
			break;
		case "9":
			euStatus="6";
			break;
		default:
			euStatus="1";
			break;
		}
    	
    	return euStatus;
    }
    
    /**
     * 转换婚姻编码
     * @param dtMarry
     * @return
     */
    public static String getDtMarry(String dtMarry){
    	//婚姻 S:未婚，M:已婚，D:离婚，R:再婚，A:分居，W:丧偶
    	//00：未婚， 01：已婚， 02：初婚， 03：再婚， 04：复婚， 05：丧偶， 06：离婚， 99：其他，
    	if("00".equals(dtMarry)){
    		return "S";
    	}else if("01".equals(dtMarry) || "02".equals(dtMarry) || "04".equals(dtMarry)){
    		return "M";
    	}else if("03".equals(dtMarry))
    		return "R";
    	else if("06".equals(dtMarry))
    		return "D";
    	else if("05".equals(dtMarry))
    		return "W";
    	return "";
    }
    
    /**
     * 转换婚姻编码
     * @param dtMarry
     * @return
     */
    public static String getDtMarryText(String dtMarry){
    	//婚姻 S:未婚，M:已婚，D:离婚，R:再婚，A:分居，W:丧偶
    	//00：未婚， 01：已婚， 02：初婚， 03：再婚， 04：复婚， 05：丧偶， 06：离婚， 99：其他，
    	if("00".equals(dtMarry)){
    		return "未婚";
    	}else if("01".equals(dtMarry) || "02".equals(dtMarry) || "04".equals(dtMarry)){
    		return "已婚";
    	}else if("03".equals(dtMarry))
    		return "再婚";
    	else if("06".equals(dtMarry))
    		return "离婚";
    	else if("05".equals(dtMarry))
    		return "丧偶";
    	return "";
    }
    
    /**
     * 转换性别
     * @param dtSex
     * @return
     */
    public static String getSex(String dtSex){
    	//性别  M: 男,F: 女,O: 其它,U: 不知道,A: 不明确的,N: 不适用
		//NHIS 02:男,03:女,04：未知
		if("02".equals(dtSex)){
			return "M";
		}else if("03".equals(dtSex)){
			return "F";
		}else if("04".equals(dtSex)){
			return "A";
		}else
			return "U";
    }
    
    /**
     * 转换患者类别
     * @param euHptype
     * @return
     */
	public static String getPatiType(String euHptype){
		//患者类别 - 01：自费,02：医保,03：公费
		// NHIS : 0 全自费，1 社会保险，2 商业保险，3 公费，4 单位医保，5优惠， 9 其它
		if("3".equals(euHptype))
			return "03";
		else if("1".equals(euHptype) || "2".equals(euHptype) || "4".equals(euHptype))
			return "02";
		else
			return "01";
	}	
	
	/**
     * PV1 - 4 入院类型
     * @param euHptype
     * @return
     */
	public static String getPvInType(String euIntype , String FlagInf , String euPvType){
		
		/* 住院登记的类型： 1 一般， 2 病重， 3 病危， 4 传染 ， N 婴儿
		   NHIS : 00稳定，01加重，02病重，03病危，flagInf=1 婴儿*/
		if("3".equals(euPvType)){
			if("1".equals(FlagInf)){
				return "N";
			}else{
				if("02".equals(euIntype))
					return "2";
				else if("03".equals(euIntype) )
					return "3";
				else
					return "1";
			}
		}
		/* 门诊登记的类型：1 普通门诊，2 副主任医师，3 主任医师，4 急诊诊查费，5 名专家号，6 特需门诊，7 血透号，8 体检号，9 免费号
		 NHIS : 0 普通；1 专家；9 急诊 */ 
		else if("1".equals(euPvType) || "2".equals(euPvType)){
			if("1".equals(euIntype))
				return "5";
			else if("9".equals(euIntype))
				return "4";
			else 
				return "1";
		}
		else
			return "1";
	}	
	
	/**
	 * 数组转换成HL7消息
	 * @param strs
	 * @return
	 */
	public static String arrayStrHl7Msg(String[] strs){
		if(strs==null||strs.length==0) return "";
		StringBuffer sb = new StringBuffer();
		int len=strs.length;
		for(int i = 0; i < len; i++){
			sb.append(strs[i]);
			if(i<len-1) sb.append("|");
		}
		return sb.toString();
	}	
	
	/**
	 * Object 转  Map
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> objectToMap(Object obj) throws Exception {    
        if(obj == null){    
            return null;    
        }   
  
        Map<String, Object> map = new HashMap<String, Object>();    
  
        Field[] declaredFields = obj.getClass().getDeclaredFields();    
        for (Field field : declaredFields) {    
            field.setAccessible(true);  
            map.put(field.getName(), field.get(obj));  
        }    
  
        return map;  
    }   
	
	
	public static User getDefaultUser(String codeEmp) {
		User user = new User();
		Map<String, Object> bdOuMap = DataBaseHelper
				.queryForMap(
						"SELECT * FROM bd_ou_employee emp JOIN bd_ou_empjob empjob  ON empjob.pk_emp = emp.pk_emp WHERE empjob.del_flag = '0' and emp.del_flag = '0' and emp.CODE_EMP = ?",
						codeEmp);
		if (MapUtils.isNotEmpty(bdOuMap)) {
			user.setPkOrg(getPropValueStr(bdOuMap, "pkOrg"));
			user.setNameEmp(getPropValueStr(bdOuMap, "nameEmp"));
			user.setPkOrg(getPropValueStr(bdOuMap, "pkOrg"));
			user.setNameEmp(getPropValueStr(bdOuMap, "nameEmp"));
			user.setPkEmp(getPropValueStr(bdOuMap, "pkEmp"));
			user.setPkDept(getPropValueStr(bdOuMap, "pkDept"));
		}
		return user;
	}
}
