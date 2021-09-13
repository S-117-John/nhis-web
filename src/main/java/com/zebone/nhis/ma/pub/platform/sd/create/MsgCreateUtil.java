package com.zebone.nhis.ma.pub.platform.sd.create;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.*;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;




/**
 * 创建消息类：深圳项目新加的消息，统一创建管理
 * @author maijiaxing
 * @since 2019年12月17日
 */
@Component
public class MsgCreateUtil {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	@Resource
	private SDQueryUtils sDQueryUtils;

	/**
	 * MSH消息段
	 * @param msh
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	 public MSH createMSHMsg(MSH msh,Map<String, Object> paramMap) throws DataTypeException{
		//MSH-1
		msh.getFieldSeparator().setValue("|");
		//MSH-2
		msh.getEncodingCharacters().setValue("^~\\&");
		//MSH-3
		msh.getSendingApplication().getNamespaceID().setValue("NHIS");
		//MSH-4 发送服务器
		 try {
			 msh.getMsh4_SendingFacility().getHd1_NamespaceID().setValue(InetAddress.getLocalHost().getHostName());
		 } catch (UnknownHostException e) {
			 //获取主机名字失败
//			 loger.info("MSH消息段，获取主机名字失败");
		 }
		 //MSH-5 receive
		String receive = SDMsgUtils.getPropValueStr(paramMap,"receive");
		if(StringUtils.isBlank(receive)){
			receive = "EAI";
		}
		msh.getReceivingApplication().getNamespaceID().setValue(receive);
		//MSH-7
		msh.getDateTimeOfMessage().getTimeOfAnEvent().setValue(sdf.format(new Date()));
		//MSH-9
		msh.getMessageType().getMessageType().setValue(SDMsgUtils.getPropValueStr(paramMap,"msgtype"));
		msh.getMessageType().getTriggerEvent().setValue(SDMsgUtils.getPropValueStr(paramMap,"triggerevent"));
		//MSH-10
		msh.getMessageControlID().setValue(SDMsgUtils.getPropValueStr(paramMap,"msgid"));
		//MSH-11
		msh.getProcessingID().getProcessingID().setValue("P");
		//MSH-12
		msh.getVersionID().getVersionID().setValue("2.4");
		//MSH-15
		msh.getAcceptAcknowledgmentType().setValue("AL");
		//MSH-16
		msh.getApplicationAcknowledgmentType().setValue("AL");
		//MSH-17
		msh.getCountryCode().setValue("CHN");
		//MSH-18
		msh.getCharacterSet(0).setValue("GBK");
		return msh;
	}

	/**
	 * EVN消息段
	 * @param evn
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public EVN createEVNMsg(EVN evn,Map<String, Object> paramMap) throws DataTypeException{
		//evn-1
		evn.getEventTypeCode().setValue(SDMsgUtils.getPropValueStr(paramMap,"triggerEvent"));
		//evn-2
		evn.getRecordedDateTime().getTimeOfAnEvent().setValue(DateUtils.getDateTimeStr(new Date()));
		//evn-3
		evn.getDateTimePlannedEvent().getTimeOfAnEvent().setValue(sdf.format(new Date()));
		//evn-4
		evn.getEventReasonCode().setValue("01");
		//evn-5.1操作员编码
		evn.getOperatorID(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
		//evn-5.2操作员名字
		evn.getOperatorID(0).getFamilyName().getSurname().setValue(UserContext.getUser().getNameEmp());
		//evn-7
		evn.getEventOccurred().getTimeOfAnEvent().setValue(sdf.format(new Date()));
		//evn-8
		evn.getEventFacility().getNamespaceID().setValue("NHIS");
		return evn;
	}

	/**
	 * 创建PID消息
	 * @param pid
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public PID createPIDMsg(PID pid,Map<String, Object> paramMap) throws HL7Exception{
		//PID-1
		pid.getSetIDPID().setValue("1");
		//PID-2 ID
		pid.getPatientID().getID().setValue(SDMsgUtils.getPropValueStr(paramMap,"codePi"));
		//PID_3 住院号
		pid.getPatientIdentifierList(0).getID().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeIp"));
		pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("PatientNO");
		//PID_3 卡号 codePi
		//pid.getPatientIdentifierList(1).getID().setValue(getPropValueStr(patMap,"cardno"));
		pid.getPatientIdentifierList(1).getID().setValue(SDMsgUtils.getPropValueStr(paramMap,"codePi"));
		pid.getPatientIdentifierList(1).getIdentifierTypeCode().setValue("IDCard");
		//PID_3 身份证号
		if("01".equals(SDMsgUtils.getPropValueStr(paramMap,"dtIdtype"))){
			pid.getPatientIdentifierList(2).getID().setValue(SDMsgUtils.getPropValueStr(paramMap,"idNo"));
		}
		else{
			pid.getPatientIdentifierList(2).getID().setValue("");
		}
		pid.getPatientIdentifierList(2).getIdentifierTypeCode().setValue("IdentifyNO");
		//PID_3 门诊号
		pid.getPatientIdentifierList(3).getID().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeOp"));
		pid.getPatientIdentifierList(3).getIdentifierTypeCode().setValue("Outpatient");
		//PID_3 电脑号(医保相关 ：深圳项目添加)
		pid.getPatientIdentifierList(3).getID().setValue("");
		pid.getPatientIdentifierList(3).getIdentifierTypeCode().setValue("SBpcNO");
		//PID-5  姓名
		pid.getPatientName(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(paramMap,"namePi"));
		//PID-7 生日
		String birthDate = SDMsgUtils.getPropValueStr(paramMap, "birthDate");
		if("".equals(birthDate)){
			birthDate = SDMsgUtils.getPropValueStr(paramMap, "birthdate");
		}
		pid.getDateTimeOfBirth().getTimeOfAnEvent().setValue(birthDate);
		//PID_8 性别 - 调用转换方法 getSex
		pid.getAdministrativeSex().setValue(SDMsgUtils.getSex(SDMsgUtils.getPropValueStr(paramMap,"dtSex")));
		//PID_11 患者地址
		pid.getPatientAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(SDMsgUtils.getPropValueStr(paramMap,"addrCur"));
		//pid.getPatientAddress(0).getZipOrPostalCode().setValue(getPropValueStr(patMap,"addrCur"));
		pid.getPatientAddress(0).getAddressType().setValue("H");
		pid.getPatientAddress(1).getStreetAddress().getStreetOrMailingAddress().setValue(SDMsgUtils.getPropValueStr(paramMap,"postcodework"));
		//pid.getPatientAddress(1).getZipOrPostalCode().setValue(getPropValueStr(patMap,"addrRegiDt"));
		pid.getPatientAddress(1).getAddressType().setValue("O");
		pid.getPatientAddress(2).getStreetAddress().getStreetOrMailingAddress().setValue(SDMsgUtils.getPropValueStr(paramMap,"addrRegi"));
		//pid.getPatientAddress(2).getZipOrPostalCode().setValue(getPropValueStr(patMap,"postcodeRegi"));
		pid.getPatientAddress(2).getAddressType().setValue("N");
		//PID-12
		pid.getCountyCode().setValue("");
		//PID_13.9 家庭电话
		pid.getPhoneNumberHome(0).getAnyText().setValue(SDMsgUtils.getPropValueStr(paramMap,"mobile"));
		//pid.getPhoneNumberHome(0).getPhoneNumber().setValue(getPropValueStr(paramMap,"mobile"));
		//PID_14.9 工作电话
		pid.getPhoneNumberBusiness(0).getAnyText().setValue(SDMsgUtils.getPropValueStr(paramMap,"telWork"));
		//PID_16 婚姻 - 调用转换方法 getDtMarry
		Map<String, Object> queryMarryByCode = sDQueryUtils.queryMarryByCode(SDMsgUtils.getPropValueStr(paramMap,"dtMarry"));
		String dtMarry = SDMsgUtils.getPropValueStr(queryMarryByCode,"dtMarry");
		//pid.getMaritalStatus().getIdentifier().setValue(SDMsgUtils.getDtMarry(SDMsgUtils.getPropValueStr(paramMap,"dtMarry")));
		pid.getMaritalStatus().getIdentifier().setValue(dtMarry);
		pid.getMaritalStatus().getText().setValue(SDMsgUtils.getDtMarryText(SDMsgUtils.getPropValueStr(paramMap,"marryName")));
		//PID -18 文化程度
		pid.getPid18_PatientAccountNumber().getCx1_ID().setValue(SDMsgUtils.getPropValueStr(paramMap, "dtEdu"));
		//PID_19 社会保险号
		pid.getSSNNumberPatient().setValue(SDMsgUtils.getPropValueStr(paramMap,"insurNo"));
		if("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagInf"))){
			Map<String, Object> queryMaInfo = sDQueryUtils.queryMaInfo(SDMsgUtils.getPropValueStr(paramMap, "codeIp"));
			//PID_21 母亲的ID
			pid.getMotherSIdentifier(0).getID().setValue(SDMsgUtils.getPropValueStr(queryMaInfo,"codePv"));
		}
		//PID_22 民族
		//pid.getEthnicGroup(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap,"dtNation"));
		pid.getEthnicGroup(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap,"nationOldId"));
		pid.getEthnicGroup(0).getText().setValue(SDMsgUtils.getPropValueStr(paramMap,"nationName"));
		//PID_23 出生地
		pid.getBirthPlace().setValue(SDMsgUtils.getPropValueStr(paramMap,"addrBirth"));
		//PID_25 出生顺序
		pid.getBirthOrder().setValue(SDMsgUtils.getPropValueStr(paramMap,"sortNo"));
		//PID_28.1 国别
		//pid.getNationality().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap,"dtCountry"));
		pid.getNationality().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap,"countryOldId"));
		pid.getNationality().getText().setValue(SDMsgUtils.getPropValueStr(paramMap,"countryName"));
		//PID_35 种类代码？？？ 籍贯  addr_origin
		pid.getSpeciesCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap,"addrOrigin"));
		//PID_36
		String insuCode = SDMsgUtils.getPropValueStr(paramMap,"insuCode");
		if(insuCode==null){
			insuCode = "310";
		}
		pid.getBreedCode().getIdentifier().setValue(insuCode);
		pid.getBreedCode().getAlternateIdentifier().setValue("21");
		String euPvType = SDMsgUtils.getPropValueStr(paramMap,"euPvtype");
		if("1".equals(euPvType)){
			pid.getBreedCode().getAlternateText().setValue("普通门诊");
		}else {
			pid.getBreedCode().getAlternateText().setValue("普通住院");
		}
		pid.getBreedCode().getText().setValue(SDMsgUtils.getPropValueStr(paramMap,"insuName"));
		//PID_38 合同单位 -- 同步单位字典？？？ code_parent
		String codeParent = SDMsgUtils.getPropValueStr(paramMap,"codeParent");
		codeParent = "".equals(codeParent)?insuCode:codeParent;
		pid.getProductionClassCode().getIdentifier().setValue(codeParent);
		//pid.getProductionClassCode().getText().setValue(SDMsgUtils.getPropValueStr(paramMap,"insuName"));
		//PID 39 参保类型（新加：深圳项目）
		//1.把pid转换为字符串 2.添加39 3.还原为PID段，替换原来的pid
		String pidStr = pid.encode();
		pidStr = pidStr + "|"+insuCode;
		pid.parse(pidStr);
		return pid;
	}


	/**
	 * 创建PV1消息
	 * @param pv1
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public PV1 createPV1Msg(PV1 pv1,Map<String, Object> paramMap) throws HL7Exception{
		String euPvType = SDMsgUtils.getPropValueStr(paramMap,"euPvtype");
		if("1".equals(euPvType)){   //门诊
			//【PV1-02】患者类别 - 门诊O
			pv1.getPatientClass().setValue("O");
			//【PV1-04】入院类型 - 门诊对应就诊服务类型
			pv1.getAdmissionType().setValue(SDMsgUtils.getPvInType(SDMsgUtils.getPropValueStr(paramMap,"euSrvtype"), "", euPvType));
			//【PV1-13】再次入院标识 - 门诊次数
			pv1.getReAdmissionIndicator().setValue(SDMsgUtils.getPropValueStr(paramMap,"opTimes"));
			//【PV1-26】合同总量-是否允许调阅健康档案 1允许，0不允许 ？？？
			pv1.getContractAmount(0).setValue("0");
		}else if("2".equals(euPvType)){  //急诊
			//【PV1-02】患者类别 - 门诊O
			pv1.getPatientClass().setValue("O");
			//【PV1-04】入院类型 - 门诊对应就诊服务类型
			pv1.getAdmissionType().setValue(SDMsgUtils.getPvInType(SDMsgUtils.getPropValueStr(paramMap,"euSrvtype"), "", euPvType));
			//【PV1-13】再次入院标识 - 门诊次数
			pv1.getReAdmissionIndicator().setValue(SDMsgUtils.getPropValueStr(paramMap,"opTimes"));
			//【PV1-26】合同总量-是否允许调阅健康档案 1允许，0不允许 ？？？
			pv1.getContractAmount(0).setValue("0");
		}else if(euPvType != null){  //住院
			//【PV1-02】患者类别 - 住院I
			pv1.getPatientClass().setValue("I");
			//【PV1-3.1】患者当前位置-病区
			pv1.getAssignedPatientLocation().getPointOfCare().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeDeptNs"));
			//【PV1-3.3】患者当前位置-床位
			if(!("".equals(SDMsgUtils.getPropValueStr(paramMap,"bednoDes")))){
				pv1.getAssignedPatientLocation().getBed().setValue(SDMsgUtils.getPropValueStr(paramMap,"bednoDes"));
			}else if(!("".equals(SDMsgUtils.getPropValueStr(paramMap,"bedNo")))){
				pv1.getAssignedPatientLocation().getBed().setValue(SDMsgUtils.getPropValueStr(paramMap,"bedNo"));
			}else if(!("".equals(SDMsgUtils.getPropValueStr(paramMap,"codeIpBed")))){
				pv1.getAssignedPatientLocation().getBed().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeIpBed"));
			}
			//【PV1-04】入院类型  //通过方法统一处理
			pv1.getAdmissionType().setValue(SDMsgUtils.getPvInType(SDMsgUtils.getPropValueStr(paramMap,"dtLevelDise"), SDMsgUtils.getPropValueStr(paramMap,"flagInf"), euPvType));
			//【PV1-13】再次入院标识 - 住院次数
			pv1.getReAdmissionIndicator().setValue(SDMsgUtils.getPropValueStr(paramMap,"ipTimes"));
			//【PV1-14】 入院来源
			pv1.getAdmitSource().setValue(""); //参考入院来源字典 - 未确定？？？
		}
		//【PV1-1】
		pv1.getSetIDPV1().setValue("1");
		//【PV1-3.4】患者当前位置-机构 => 科室
		pv1.getAssignedPatientLocation().getFacility().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeDept"));
		pv1.getAssignedPatientLocation().getLocationStatus().setValue(SDMsgUtils.getPropValueStr(paramMap,"nameDept"));
		//【PV1-7】 主管医生
		pv1.getAttendingDoctor(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpPhy"));
		pv1.getAttendingDoctor(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(paramMap,"nameEmpPhy"));
		//【PV1-9】 主管护士 codeEmpNs
		pv1.getConsultingDoctor(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpNs"));
		//【PV1-16】 VIP标识符 - 1：是 0：否 - 未确定？？？
		pv1.getVIPIndicator().setValue("0");
		//【PV1-17】 入院医生 = 收治医生
		pv1.getAdmittingDoctor(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpTre"));
		pv1.getAdmittingDoctor(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(paramMap,"nameEmpTre"));
		//入院诊断
		Map<String, Object> diagPv = sDQueryUtils.getDiagPv(SDMsgUtils.getPropValueStr(paramMap,"pkPv"));
		pv1.getAdmittingDoctor(0).getGivenName().setValue(SDMsgUtils.getPropValueStr(diagPv,"nameDiag"));
		//【PV1-18】 患者类别 - 参照患者结算类别 01自费,02医保,03公费
		//pv1.getPatientType().setValue(SDMsgUtils.getPatiType(SDMsgUtils.getPropValueStr(paramMap,"euHptype")));
		String euHptype = !"0".equals(SDMsgUtils.getPropValueStr(paramMap,"euHptype"))?"02":"01";
		pv1.getPatientType().setValue(euHptype);
		//【PV1-19】 就诊号码 his visit no
		pv1.getVisitNumber().getID().setValue(SDMsgUtils.getPropValueStr(paramMap,"codePv"));
		//PV1 -36 住院状态 pv 表的 eu_status   0 登记，1 就诊，2 结束，3 结算，9 退诊
		pv1.getPv136_DischargeDisposition().setValue(SDMsgUtils.getPropValueStr(paramMap,"euStatusPv"));
		//【PV1-44】入院日期 - 入科（就诊时间)
		if(!CommonUtils.isEmptyString(SDMsgUtils.getPropValueStr(paramMap,"dateAdmit"))) {
			pv1.getAdmitDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap,"dateAdmit"));
		}else{
			pv1.getAdmitDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap,"dateBegin"));
		}
		//取消出院时候flagCancel为0  出院为1
		//【PV1-45】出院日期effective_e
		if("1".equals(SDMsgUtils.getPropValueStr(paramMap,"flagCancel"))){
			//取消就诊时;出院时间
			if(!CommonUtils.isEmptyString(SDMsgUtils.getPropValueStr(paramMap,"dateCancel"))){
				//pv1.insertDischargeDateTime(0);
				pv1.getDischargeDateTime(0).getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap,"dateCancel"));
			}
		}else{ //取消出院
			 //转科时传递时间日期
			if("A02".equals(SDMsgUtils.getPropValueStr(paramMap, "EVNopeType"))){
				//转科，转科时间 转科操作时，源科室记录写入日期 dateBeginAdt
				//查询转科日期 pv_adt
				Map<String, Object> queryAdt = sDQueryUtils.queryAdt(SDMsgUtils.getPropValueStr(paramMap, "pkPv"));
				pv1.getDischargeDateTime(0).getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(queryAdt,"dateBegin"));
			}else if(!CommonUtils.isEmptyString(SDMsgUtils.getPropValueStr(paramMap,"dateEnd"))){
				//出院时，结束日期
				//pv1.insertDischargeDateTime(0);
				pv1.getDischargeDateTime(0).getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap,"dateEnd"));
			}else {
				//取消出院时间 (系统当前时间)
				pv1.getDischargeDateTime(0).getTimeOfAnEvent().setValue(sdf.format(new Date()));
			}
		}
		//总金额
		Map<String, Object> getAcoumt = sDQueryUtils.getAcoumt(SDMsgUtils.getPropValueStr(paramMap,"pkPv"));
		pv1.getTotalCharges().setValue(SDMsgUtils.getPropValueStr(getAcoumt,"sum"));
		//管床护士 52 code_emp_ns
		pv1.getOtherHealthcareProvider(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpNs"));
		return pv1;
	}

	/**
	 * 创建ORC消息
	 * @param orc
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public ORC createORCMsg(ORC orc,Map<String, Object> paramMap) throws DataTypeException{
		//ORC[1]-1 申请控制   control
		orc.getOrderControl().setValue(SDMsgUtils.getPropValueStr(paramMap, "control"));
		//ORC[1]-2  处方明细编码(执行单号)pkExocc
		String orc2Str = SDMsgUtils.getPropValueStr(paramMap, "pkExocc");
		if(orc2Str==null || "".equals(orc2Str)){
			orc2Str = SDMsgUtils.getPropValueStr(paramMap, "codeApply");
		}
		orc.getPlacerOrderNumber().getEntityIdentifier().setValue(orc2Str);
		//ORC[1]-3.1 申请单号
		String code = SDMsgUtils.getPropValueStr(paramMap, "codeApply");
		String type = SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype");
		if("02".startsWith(type)){
			code = SDMsgUtils.getPropValueStr(paramMap, "codeApply")+"H"+SDMsgUtils.getPropValueStr(paramMap, "ordsn");
			orc.getPlacerOrderNumber().getEntityIdentifier().setValue(code);
		}
		orc.getFillerOrderNumber().getEntityIdentifier().setValue(code);
		///ORC[1]-3.3
		//orc.getFillerOrderNumber().getUniversalID().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeApply"));
		//ORC[1]-4 医嘱号ordsnParent4.1
		orc.getPlacerGroupNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "ordsn"));
		orc.getPlacerGroupNumber().getEi2_NamespaceID().setValue("");
		orc.getPlacerGroupNumber().getUniversalID().setValue("");
		orc.getPlacerGroupNumber().getUniversalIDType().setValue("");
		//ORC[1]-5  医嘱状态  euPvtype  	1门诊；2急诊；3住院 ；4体检；5家床 mec体检 fp家庭病床
		if ("3".equals(SDMsgUtils.getPropValueStr(paramMap, "euPvtype"))) {
			String euStatusOrd = "";
			//System.out.println(SDMsgUtils.getPropValueStr(paramMap, "ordStatus"));
			if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "ordStatus"))){
				euStatusOrd = SDMsgUtils.getPropValueStr(paramMap, "ordStatus");
			}else if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "ordstatus"))){
				euStatusOrd = SDMsgUtils.getPropValueStr(paramMap, "ordstatus");
			}else{
				euStatusOrd = SDMsgUtils.getPropValueStr(paramMap, "euStatusOrd");
				//0 开立；1 签署；2 核对；3 执行；4 停止；9 作废 (his)
				//		2签署，3核对，		  5停止，6 作废
				switch(SDMsgUtils.getPropValueStr(paramMap, "euStatusOrd")){
					case "1":euStatusOrd="2"; break;
					case "2":euStatusOrd="3"; break;
					case "4":euStatusOrd="5"; break;
					case "9":euStatusOrd="6"; break;
				}
			}
			orc.getOrderStatus().setValue(euStatusOrd);
		} else {
			orc.getOrderStatus().setValue("1");// 门诊：【0：手工单；1、电脑录入】
		}
		//ORC[1]-6
		//orc.getResponseFlag().setValue(SDMsgUtils.getPropValueStr(paramMap, "control"));
		//ORC[1]-7.1.1  剂量
		orc.getQuantityTiming(0).getQuantity().getQuantity().setValue(SDMsgUtils.getPropValueStr(paramMap, "dosage"));
		// ORC[1]-7.1.2 单位
		String unitName = sDQueryUtils.qryUnitByPK(SDMsgUtils.getPropValueStr(paramMap,"pkUnitDos"));
		orc.getQuantityTiming(0).getQuantity().getUnits().getIdentifier().setValue(unitName);
		//ORC[1]-7.2.1频率 codeFreq
		String codeFreq=SDMsgUtils.getPropValueStr(paramMap, "codeFreq");
		//orc.getQuantityTiming(0).getInterval().getRi1_RepeatPattern().setValue(SDMsgUtils.getPropValueStr(map, "codeFreq"));// 牝鹿
		orc.getQuantityTiming(0).getInterval().getRepeatPattern().setValue(codeFreq);
		//ORC[1]-7.2.2 频率单位名称
		String name = SDMsgUtils.getPropValueStr(sDQueryUtils.getNameFreq(codeFreq), "name");
		orc.getQuantityTiming(0).getInterval().getExplicitTimeInterval().setValue(name);
		////7.3 天数
		//orc.getQuantityTiming(0).getDuration().setValue(SDMsgUtils.getPropValueStr(paramMap, "days"));
		orc.getQuantityTiming(0).getOccurrenceDuration().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "days"));
		//7.6 优先权
		String flagEmer = SDMsgUtils.getPropValueStr(paramMap, "flagEmer");
		if ("1".equals(flagEmer)){
			flagEmer = "A";
		}
		orc.getQuantityTiming(0).getPriority().setValue(flagEmer);
		String cnt = sDQueryUtils.qryItemFreq(SDMsgUtils.getPropValueStr(paramMap,"codeFreq"));
		orc.getQuantityTiming(0).getCondition().setValue(cnt);// 次数
		//ORC[1]-8   parent
		String parent = SDMsgUtils.getPropValueStr(paramMap, "ordsnParent");
		String ordsn = SDMsgUtils.getPropValueStr(paramMap, "ordsn");
		if(null!=parent && parent.equals(ordsn)){
			orc.getParentOrder().getParentSPlacerOrderNumber().getEntityIdentifier().setValue("");
		}else{
			orc.getParentOrder().getParentSPlacerOrderNumber().getEntityIdentifier().setValue(parent);
		}
		//ORC[1]-9  停止时间  	//事务时间
		orc.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(sdf.format(new Date()));
		//ORC[1]-10  操作人员编码
		//判断是否作废
		String pkEmp = "";
		if(!"".equals(SDMsgUtils.getPropValueStr(paramMap,"pkEmpErase"))){
			pkEmp = SDMsgUtils.getPropValueStr(paramMap,"pkEmpErase");
		}else if(!"".equals(SDMsgUtils.getPropValueStr(paramMap,"pkEmpStop"))){
			pkEmp = SDMsgUtils.getPropValueStr(paramMap,"pkEmpStop");
		}else{
			pkEmp = SDMsgUtils.getPropValueStr(paramMap,"pkEmpOrd");
		}
		Map<String, Object> empMap = sDQueryUtils.getUserCodeByPkUser(pkEmp);
		orc.getEnteredBy(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(empMap,"code"));
		orc.getEnteredBy(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(empMap,"name"));
		//ORC[1]-11  护士 检验人员编码
		String pkEmpCheck = "";
		if(!"".equals(SDMsgUtils.getPropValueStr(paramMap,"pkEmpErase"))){
			pkEmpCheck = SDMsgUtils.getPropValueStr(paramMap,"pkEmpErase");
		}else if(!"".equals(SDMsgUtils.getPropValueStr(paramMap,"pkEmpStopChk"))){
			pkEmpCheck = SDMsgUtils.getPropValueStr(paramMap,"pkEmpStopChk");
		}else if(!"".equals(SDMsgUtils.getPropValueStr(paramMap,"pkEmpChk"))){
			pkEmpCheck = SDMsgUtils.getPropValueStr(paramMap,"pkEmpChk");
		}else{
			pkEmpCheck = SDMsgUtils.getPropValueStr(paramMap,"pkEmpOrd");
		}
		Map<String, Object> empChkMap = sDQueryUtils.getUserCodeByPkUser(pkEmpCheck);
		orc.getVerifiedBy(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(empChkMap,"code"));
		orc.getVerifiedBy(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(empChkMap,"name"));
		//ORC[1]-12  开立医生
		String codeEmpOrd = SDMsgUtils.getPropValueStr(sDQueryUtils.getUserCodeByPkUser(SDMsgUtils.getPropValueStr(paramMap, "pkEmpInput")), "code");
		orc.getOrderingProvider(0).getIDNumber().setValue(codeEmpOrd);
		orc.getOrderingProvider(0).getGivenName().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameEmpInput"));
		orc.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameEmpInput"));
		//ORC-13 护嘱标志
		//String flagDoctor = "0".equals(SDMsgUtils.getPropValueStr(paramMap, "flagDoctor"))?"1":"0";
		orc.getEntererSLocation().getPointOfCare().setValue(SDMsgUtils.getPropValueStr(paramMap, "flagDoctor"));
		//ORC[1]-15  开始时间
		if("ORM".equals(SDMsgUtils.getPropValueStr(paramMap, "msgtype")) && "O01".equals(SDMsgUtils.getPropValueStr(paramMap, "triggerevent"))){
			orc.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap,"datePlan"));
		}else{
			if(null != SDMsgUtils.getPropValueDate(paramMap,"c")){
				orc.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap,"dateStart"));
			}else{
				orc.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			}
		}
		//ORC[1]-17 // 录入科室
		Map<String, Object> deptMap = sDQueryUtils.queryDeptByPk(SDMsgUtils.getPropValueStr(paramMap,"pkDept"));
		if(deptMap != null){
			orc.getEnteringOrganization().getIdentifier().setValue(SDMsgUtils.getPropValueStr(deptMap, "codeDept"));
			orc.getEnteringOrganization().getText().setValue(SDMsgUtils.getPropValueStr(deptMap, "nameDept"));
		}
		//ORC[1]-18  区分首次末次
		if ("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagFirst"))) {
			orc.getEnteringDevice().getIdentifier().setValue("F");
			orc.getEnteringDevice().getText().setValue(SDMsgUtils.getPropValueStr(paramMap, "firstNum"));
		} else {
			orc.getEnteringDevice().getIdentifier().setValue("L");
			orc.getEnteringDevice().getText().setValue(SDMsgUtils.getPropValueStr(paramMap, "lastNum"));
		}
		//ORC[1]-21  患者科室
		orc.getOrderingFacilityName(0).getOrganizationName().setValue(SDMsgUtils.getPropValueStr(deptMap, "codeDept"));
		orc.getOrderingFacilityName(0).getOrganizationNameTypeCode().setValue(SDMsgUtils.getPropValueStr(deptMap, "nameDept"));
		//ORC20.3 取消执行传值  orc2Str  paramMap.put("typestatus", "DEL");
		if("DEL".equals(SDMsgUtils.getPropValueStr(paramMap, "typestatus"))){
			//orc.getAdvancedBeneficiaryNoticeCode().getNameOfCodingSystem().setValue(orc2Str);
			orc.getAdvancedBeneficiaryNoticeCode().getAlternateIdentifier().setValue(orc2Str);
		}

		//ORC[1]-23  医嘱类型  codeOrdtype
		//orc.getOrderingFacilityPhoneNumber(0).getEmailAddress().setValue(SDMsgUtils.getPropValueStr(paramMap, "euAlways"));
		orc.getOrderingFacilityPhoneNumber(0).getExtension().setValue(SDMsgUtils.getPropValueStr(paramMap, "euAlways"));
		//orc.getOrderingFacilityPhoneNumber(0).getAnyText().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype"));
		String codeOrdtype = SDMsgUtils.getPropValueStr(paramMap,"codeOrdtype");
		String ordType="";
		if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype"))){
			codeOrdtype = SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype").toString().substring(0, 2);
			switch (codeOrdtype){
				case "01":ordType = "3";break;
				case "02":ordType = "1";break;
				case "03":ordType = "1";break;
				case "04":ordType = "2";break;
				case "05":ordType = "2";break;
				case "06":ordType = "6";break;
				case "07":ordType = "5";break;
				case "08":ordType = "2";break;//嘱托
				case "13":ordType = "4";break;
				default : ordType = "9";
			}
		}
		orc.getOrderingFacilityPhoneNumber(0).getAnyText().setValue(ordType);
		//长期医嘱标志  eu_always 数据库0是长期 ；消息1是长期
		String enAlways = "0".equals(SDMsgUtils.getPropValueStr(paramMap, "euAlways")) ? "1" : "0";
		orc.getOrderingFacilityPhoneNumber(0).getExtension().setValue(enAlways);
		//ORC-24.1：医嘱开始时间：cn_order.date_start
		orc.getOrderingProviderAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(SDMsgUtils.getPropValueStr(paramMap, "dateStart"));
		//ORC-24.2：医嘱结束时间(停止医嘱后要传值)：cn_order.date_stop
		orc.getOrderingProviderAddress(0).getOtherDesignation().setValue(SDMsgUtils.getPropValueStr(paramMap, "dateStop"));
		//ORC-25.1(Identifier)：(s自备，b基数药)：cn_order.flag_self，cn_order.flag_base
		if("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagSelf"))){
			orc.getOrderStatusModifier().getIdentifier().setValue("s");
		}else if("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagBase"))){
			orc.getOrderStatusModifier().getIdentifier().setValue("b");
		}
		//ORC-25.2(Text)：(p预防，t治疗)cn_order.flag_prev， cn_order.flag_thera
		if("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagPrev"))){
			orc.getOrderStatusModifier().getText().setValue("p");
		}else if("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagThera"))){
			orc.getOrderStatusModifier().getText().setValue("t");
		}
		//ORC-25.3(NameOfCodingSystem)：(d出院带药)cn_order.flag_medout
		if("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagMedout"))){
			orc.getOrderStatusModifier().getNameOfCodingSystem().setValue("d");
		}
		//ORC-25.4(AlternateIdentifier)：(n嘱托)cn_order.flag_note
		if("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagNote"))){
			orc.getOrderStatusModifier().getAlternateIdentifier().setValue("n");
		}
		//ORC-25（旧）
		// "a,长短期标识：0、临时       1、长期 b.LB-医嘱类别：01:药品，02:检查，03:检验，04:手术，
		// 05:治疗，06：护理，07：卫材，08：嘱托，09：诊疗，10：公卫，11：患者管理，12：输血，13：饮食，14：陪护，99：其他"
//		if ("0".equals(SDMsgUtils.getPropValueStr(paramMap, "euAlways"))) {
//			orc.getOrderStatusModifier().getIdentifier().setValue("1");
//		} else {
//			orc.getOrderStatusModifier().getIdentifier().setValue("0");
//		}
//		if(codeOrdtype!=null && codeOrdtype.length()>2){
//			orc.getOrderStatusModifier().getText().setValue(codeOrdtype.substring(0, 2));
//		}else{
//			orc.getOrderStatusModifier().getText().setValue(codeOrdtype);
//		}
		return orc;
	}

	/**
	 * 创建FT1消息
	 * @param ft1
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public FT1 createFT1Msg(FT1 ft1,Map<String, Object> paramMap) throws DataTypeException{
		//ft1-1  //发生序号
		ft1.getSetIDFT1().setValue("1");
		//ft1-2
		if("PMI".equals(SDMsgUtils.getPropValueStr(paramMap,"triggerevent"))){
			//发票号（住院结算）
			ft1.getTransactionID().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeInv"));
			//预交金
			String amountTotal = SDMsgUtils.getPropValueStr(paramMap, "amountSt");
			amountTotal = "".equals(amountTotal)?"0":amountTotal;
			//15.3 保险总金额
			String amountInsu = SDMsgUtils.getPropValueStr(paramMap, "amountInsu");
			amountInsu = "".equals(amountInsu)?"0":amountInsu;
			//22.3自费金额  amountPi 未扣除预交金 amount扣除预交金
			String amountSelf = SDMsgUtils.getPropValueStr(paramMap, "amountPi");
			amountSelf = "".equals(amountSelf)?"0":amountSelf;
			//ft1-11.3	结算总金额  amountSt
			ft1.getTransactionAmountExtended().getFromValue().setValue(amountTotal);
			//15.3 保险总金额
			ft1.getInsuranceAmount().getFromValue().setValue(amountInsu);
			//22.1  自费
			//ft1.getUnitCost().getPrice().getQuantity().setValue(SDMsgUtils.getPropValueStr(paramMap, "selfAmount"));
			//22.3自费金额
			ft1.getUnitCost().getFromValue().setValue(amountSelf);

		}else {
			//预交金收据号
			ft1.getTransactionID().setValue(SDMsgUtils.getPropValueStr(paramMap,"reptNo"));
			//ft1-11.3	结算总金额  amountSt
			String amountTotal = SDMsgUtils.getPropValueStr(paramMap, "amount");
			amountTotal = "".equals(amountTotal)?"0":amountTotal;
			ft1.getTransactionAmountExtended().getFromValue().setValue(amountTotal);
		}
		//ft1-4 有问题		//收取时间（事务时间）
		ft1.getTransactionDate().getTimeOfAnEvent().setValue(sdf.format(new Date()));
		//ft1-6	 //预交金 EU_DPTYPE 0 就诊结算；1 中途结算；2 结算冲账；3 欠费补缴；4 取消结算；9 住院预交金
		//"结算类别（请参照患者结算类别） 01：自费 02：医保 03：公费"
		if("9".equals(SDMsgUtils.getPropValueStr(paramMap, "euDptype"))){
			//住院预交金为固定值
			ft1.getTransactionType().setValue("INPAY");
		}else{
			String euHptype = !"0".equals(SDMsgUtils.getPropValueStr(paramMap,"euHptype"))?"02":"01";
			ft1.getTransactionType().setValue(euHptype);
		}
		//ft1-7
		ft1.getTransactionCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap,"dtPaymode"));
		ft1.getTransactionCode().getText().setValue(SDMsgUtils.getPropValueStr(paramMap,"dtPaymode"));
		//FT1-8：结算方式(1：门诊结算，2：住院中途结算，3：出院结算，4：门诊挂号)：EU_DPTYPE
		String dtSttype = SDMsgUtils.getPropValueStr(paramMap, "dtSttype");
		switch (dtSttype){
			case "00":dtSttype="1";break;
			case "01":dtSttype="4";break;
			case "10":dtSttype="3";break;
			case "11":dtSttype="2";break;
			case "20":dtSttype="4";break;
			case "21":dtSttype="3";break;
		}
		ft1.getTransactionDescription().setValue(dtSttype);
		//ft1-18  //患者类型 euPvtype  1 门诊，2 急诊，3 住院，4 体检，5 家床
		ft1.getPatientType().setValue(SDMsgUtils.getPropValueStr(paramMap, "euPvtype"));
		//ft1-25	//预交金状态    euDirect  1收 -1退  平台：预交金状态 0:收取；1:作废;2:补打,3结算召回作废
		//ft1-26	//预交金状态
		String euDirect = SDMsgUtils.getPropValueStr(paramMap,"euDirect");
		String flagCc = SDMsgUtils.getPropValueStr(paramMap,"flagCc");
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
		return ft1;
	}

	/**
	 * 创建ODT消息
	 * @param odt
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public ODT createODTMsg(ODT odt,Map<String, Object> paramMap) throws DataTypeException{
		//ODT-1
		odt.getTrayType().getIdentifier().setValue("1");
		return odt;
	}

	/**
	 * 创建DG1消息
	 * @param dg1
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public DG1 createDG1Msg(DG1 dg1,Map<String, Object> paramMap) throws DataTypeException{
		String pkDiagPre = SDMsgUtils.getPropValueStr(paramMap,"pkDiagPre");
		Map<String, Object> queryDiag = sDQueryUtils.queryDiag(pkDiagPre);
		//DG1-3
		dg1.getDiagnosisCodeDG1().getIdentifier().setValue(SDMsgUtils.getPropValueStr(queryDiag,"codeCd"));
		//DG1-4 诊断名称
		String diag = "";
		//会诊诊断信息
		String msgtype = SDMsgUtils.getPropValueStr(paramMap, "msgtype");
		String triggerevent = SDMsgUtils.getPropValueStr(paramMap, "triggerevent");
		if(("REF".equals(msgtype) && "I12".equals(triggerevent)) || ("RRI".equals(msgtype) && "I12".equals(triggerevent))){
			//DG1-4：诊断信息：cn_consult_apply.diagname
			diag = SDMsgUtils.getPropValueStr(paramMap, "diagname");
		}else{
			//其他消息诊断信息
			diag = SDMsgUtils.getPropValueStr(paramMap,"descDiagPre");
			diag = "".equals(diag)?SDMsgUtils.getPropValueStr(queryDiag,"nameCd"):diag;
		}
		dg1.getDiagnosisDescription().setValue(diag);
		//DG1-6
		dg1.getDiagnosisType().setValue(SDMsgUtils.getPropValueStr(paramMap,"num"));
		return dg1;
	}
	/**
	 * 微信模块DG1
	 * @param dg1
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public DG1 createDG1MsgWechat(DG1 dg1,Map<String, Object> paramMap)throws DataTypeException{
		//序号
		dg1.getDg11_SetIDDG1().setValue(SDMsgUtils.getPropValueStr(paramMap,"sort"));
		//DG1-3 诊断编码
		dg1.getDg13_DiagnosisCodeDG1().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeIcd"));
		//DG1-4 诊断名称
		dg1.getDg14_DiagnosisDescription().setValue(SDMsgUtils.getPropValueStr(paramMap,"nameDiag"));
		//DG1-6
		dg1.getDiagnosisType().setValue(SDMsgUtils.getPropValueStr(paramMap,"dtDiagtype"));
		return dg1;
	}


	/**
	 * 拼接Z段数据
	 * @param strs 包含数据的String数组
	 * @return 返回Stirng
	 */
	public String createZMsgStr(String[] strs) {
		String zstr= "";
		for (int k = 0; k < strs.length; k++) {
			String tmp = strs[k] == null ? "" : strs[k];
			if (k == 0) {
				zstr = "|" + tmp;
			} else {
				zstr = zstr + "|" + tmp;
			}
		}
		return zstr;
	}

	/**
	 * 创建MFI消息
	 * @param mfi
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public MFI createMFIMsg(MFI mfi, Map<String, Object> paramMap) throws DataTypeException {
		//MFI1
		mfi.getMasterFileIdentifier().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap,"msgName"));//MsgName
		mfi.getMasterFileIdentifier().getText().setValue(SDMsgUtils.getPropValueStr(paramMap,"msgText"));//MsgText
		mfi.getMasterFileIdentifier().getNameOfCodingSystem().setValue("HIS");
		//MFI3
		mfi.getFileLevelEventCode().setValue("UPD");
		//MFI4
		mfi.getEnteredDateTime().getTimeOfAnEvent().setValue(sdf.format(new Date()));
		//MFI5
		mfi.getEffectiveDateTime().getTimeOfAnEvent().setValue(sdf.format(new Date()));
		//MFI6
		mfi.getResponseLevelCode().setValue("AL");

		return mfi;
	}

	/**
	 * 创建MFE消息
	 * @param mfe
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public MFE createMFEMsg(MFE mfe,ST key, Map<String, Object> paramMap) throws DataTypeException {
		//MFE1
		mfe.getRecordLevelEventCode().setValue(SDMsgUtils.getPropValueStr(paramMap, "rleCode"));
		//MFE4
		key.setValue(SDMsgUtils.getPropValueStr(paramMap, "codeDept"));
		mfe.getPrimaryKeyValueMFE(0).setData(key);
		mfe.getPrimaryKeyValueType(0).setValue("CE");
		return mfe;
	}

	/**
	 * 创建OBR消息
	 * @param obr
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public OBR createOBRMsg(OBR obr, Map<String, Object> paramMap) throws DataTypeException {
		//OBR-2：医嘱号：cn_order.odsn
		obr.getObr2_PlacerOrderNumber().getEi1_EntityIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "ordsn"));
		//OBR-3：申请单号：cn_order.code_apply
		obr.getFillerOrderNumber().getEi1_EntityIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeApply"));
		//OBR-4.1项目编号   cn_order.code_ord
		obr.getUniversalServiceIdentifier().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOrd"));
		//OBR-4.2 医嘱码 cn_order.name_ord
		obr.getUniversalServiceIdentifier().getText().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameOrd"));
		//OBR-4.3 医嘱分类
		Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select dt_ordcate from bd_ord bd where bd.code=?", SDMsgUtils.getPropValueStr(paramMap, "codeOrd"));
		obr.getUniversalServiceIdentifier().getNameOfCodingSystem().setValue(SDMsgUtils.getPropValueStr(queryForMap, "dtOrdcate"));
		//obr.getUniversalServiceIdentifier().getNameOfCodingSystem().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype"));
		//2. OBR-6：申请时间：cn_consult_apply.date_apply
		obr.getObr6_RequestedDateTime().getTs1_TimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap, "dateApply"));
		//OBR-13 病情描述：cn_consult_apply.ill_summary
		obr.getRelevantClinicalInfo().setValue(SDMsgUtils.getPropValueStr(paramMap, "illSummary"));
		//obr-15 样本类型 此字段暂时不需要
		obr.getSpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "bbname"));
		obr.getSpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "descBody"));
		//obr-16.1 申请医生编码
		obr.getObr16_OrderingProvider(0).getXcn1_IDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeEmpOrd"));
		//obr-16.2  申请医生姓名
		obr.getObr16_OrderingProvider(0).getXcn2_FamilyName().getFn1_Surname().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameEmpOrd"));
		//obr-16.3 申请科室编码
		obr.getObr16_OrderingProvider(0).getXcn3_GivenName().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeDept"));
		//obr-16.4  申请科室名字
		obr.getObr16_OrderingProvider(0).getXcn4_SecondAndFurtherGivenNamesOrInitialsThereof().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameDept"));
		//obr-17.1 申请科室编码
		//obr.getObr17_OrderCallbackPhoneNumber(0).get9999999X99999CAnyText().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeDept"));
		//obr-17.2  申请科室名字
		//obr.getObr17_OrderCallbackPhoneNumber(0).getXtn2_TelecommunicationUseCode().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameDept"));
		//OBR-18：体格检查：cn_consult_apply.objective
		obr.getPlacerField1().setValue(SDMsgUtils.getPropValueStr(paramMap, "objective"));
		//OBR-19：检验检查：cn_consult_apply.examine
		obr.getPlacerField2().setValue(SDMsgUtils.getPropValueStr(paramMap, "examine"));
		//OBR-23-1
		obr.getChargeToPractice().getDollarAmount().getQuantity().setValue("");
		obr.getChargeToPractice().getDollarAmount().getDenomination().setValue("");
		//OBR-23-2
		obr.getChargeToPractice().getChargeCode().getIdentifier().setValue("");
		obr.getChargeToPractice().getChargeCode().getText().setValue("");
		//OBR-24 描述@todo待确认
		//obr.getRelevantClinicalInfo().setValue(SDMsgUtils.getPropValueStr(map, "noteOrd"));
		//执行科室//@todo换成编码或名称
		Map<String,Object> deptMap=sDQueryUtils.queryDeptByPk(SDMsgUtils.getPropValueStr(paramMap, "pkDeptExec"));
		obr.getDiagnosticServSectID().setValue(SDMsgUtils.getPropValueStr(deptMap, "codeDept"));
		//OBR-31
		obr.getReasonForStudy(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "purpose"));

		return obr;
	}

	/**
	 * 创建RXO消息
	 * @param rxo
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public RXO createRXOMsg(RXO rxo, Map<String, Object> paramMap) throws DataTypeException{
		String triggerevent = SDMsgUtils.getPropValueStr(paramMap, "triggerevent");
		if("O09".equals(triggerevent)){
			//RXO-1.1 医嘱编码
			rxo.getRequestedGiveCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOrd"));
			//医嘱名字
			StringBuilder name = new StringBuilder();
			if(SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype").startsWith("0103")){
				//草药  / 草药数量
				String queryBdPd = sDQueryUtils.queryBdPd(SDMsgUtils.getPropValueStr(paramMap, "pkCnord"));
				String num = "".equals(SDMsgUtils.getPropValueStr(paramMap, "ords"))?"1":SDMsgUtils.getPropValueStr(paramMap, "ords");
				name.append("草药").append(num).append("付").append("(").append(queryBdPd).append(")");
			}else {
				name.append(SDMsgUtils.getPropValueStr(paramMap, "nameOrd"));
			}
			rxo.getRequestedGiveCode().getText().setValue(name.toString());
			//rxo.getRequestedGiveCode().getText().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameOrd"));
			//RXO-1.3 医嘱类型codeOrdtype
			rxo.getRequestedGiveCode().getNameOfCodingSystem().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype"));
			// 数量
			rxo.getRequestedGiveAmountMinimum().setValue(SDMsgUtils.getPropValueStr(paramMap, "quan"));
			// 单位
			String unitName = sDQueryUtils.qryUnitByPK(SDMsgUtils.getPropValueStr(paramMap,"pkUnit"));
			rxo.getRequestedGiveUnits().getIdentifier().setValue(unitName);
			//RXO-8 药房编码
			Map<String, Object> itemMap = sDQueryUtils.queryDeptByPk(SDMsgUtils.getPropValueStr(paramMap,"pkDeptExec"));
			if(itemMap!=null){
				rxo.getDeliverToLocation().getPointOfCare().setValue(SDMsgUtils.getPropValueStr(itemMap, "codeDept"));
			}
			// 草药付数
			rxo.getNumberOfRefills().setValue(SDMsgUtils.getPropValueStr(paramMap, "ords"));
			// 预防治疗标志 皮试
			rxo.getIndication(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "euSt"));// TODO

		}else {
			//O17 消息
			//rxo-1 医嘱编码^收费项目编码^医嘱名称
			rxo.getRequestedGiveCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOrd"));
			//rxo-1.2收费项目编码
			//rxo.getRequestedGiveCode().getText().setValue(SDMsgUtils.getPropValueStr(map, "pkItem"));
			rxo.getRequestedGiveCode().getText().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOrd"));
			//rxo-1.3医嘱类型 code_ordtype d：药品，n：非药品
			if("01".equals(SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype").substring(0,2))){
				rxo.getRequestedGiveCode().getNameOfCodingSystem().setValue("d");
			}else{
				rxo.getRequestedGiveCode().getNameOfCodingSystem().setValue("n");
			}
			// rxo-2 请求给予的最小量（数量）  quan
			String quan = SDMsgUtils.getPropValueStr(paramMap, "quan");
			String quanOcc = SDMsgUtils.getPropValueStr(paramMap, "quanOcc");
			if("".equals(quanOcc)){
				quan = quanOcc;
			}
			if("DEL".equals(SDMsgUtils.getPropValueStr(paramMap, "typestatus"))){
				//传负数
				quan = "-"+quan;
			}
			rxo.getRequestedGiveAmountMinimum().setValue(quan);
			// rxo-4 给予单位 identifier 数量单位
			String unitPdName = sDQueryUtils.qryUnitByPK(SDMsgUtils.getPropValueStr(paramMap,"pkUnit"));
			rxo.getRequestedGiveUnits().getIdentifier().setValue(unitPdName);
			//rxo-6 供应方的药物/治疗指导  医嘱描述
			rxo.getProviderSPharmacyTreatmentInstructions(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "descOrd"));
			// rxo-7 嘱托
			rxo.getProviderSAdministrationInstructions(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "flagNote"));
			//rxo-8 facility-niversalID 药房编码
			Map<String, Object> nivDepts = sDQueryUtils.queryDeptByPk(SDMsgUtils.getPropValueStr(paramMap,"pkDeptExec"));
			//rxo-8.1
			rxo.getDeliverToLocation().getPointOfCare().setValue(SDMsgUtils.getPropValueStr(nivDepts, "codeDept"));
			//rxo-8.2
			rxo.getDeliverToLocation().getRoom().setValue(SDMsgUtils.getPropValueStr(nivDepts, "nameDept"));
			//rxo-8.4.2 药房编码
			rxo.getDeliverToLocation().getFacility().getUniversalID().setValue(SDMsgUtils.getPropValueStr(nivDepts, "codeDept"));
			//rxo-13 草药付数Number Of Refills
			rxo.getNumberOfRefills().setValue(SDMsgUtils.getPropValueStr(paramMap, "ords"));
			//rxo-20 预防治疗标志
			rxo.getIndication(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "flagPrev"));
			/*rxo-9 // Allow Substitutions 允许替代 拆分标识：0可拆分；1不可拆分 */// TODO 拆分标志
			rxo.getAllowSubstitutions().setValue("1");
			//rxo-24 药品单类型^药品单号^A打印D取消
			rxo.getSupplementaryCode(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameDecate"));
			rxo.getSupplementaryCode(0).getText().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeDe"));
			String print = "";
			if ("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagPrt"))) {
				print = "A";
			} else {
				print = "D";
			}
			rxo.getSupplementaryCode(0).getNameOfCodingSystem().setValue(print);
		}

		return rxo;
	}

	/**
	 * 创建ZFP消息
	 * @param zfp
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public ZFP createZFPMsg(ZFP zfp, Map<String, Object> paramMap) throws DataTypeException {
		//ZFP-1	Fph	  发票号
		//1.1发票号
		zfp.getFph().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeInv"));
		//1.2纸质发票号
		zfp.getFph().getCe2_Text().setValue("");
		// 1.3待确定
		zfp.getFph().getCe3_NameOfCodingSystem().setValue("");
		//1.4 重打发票号
		zfp.getFph().getCe4_AlternateIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "oldCodeInv"));
		// 医保金额 amtInsu
		String amountInsu = SDMsgUtils.getPropValueStr(paramMap, "amountInsu");
		amountInsu = "".equals(amountInsu)?"0":amountInsu;
		// 个人缴费  amountSelf
		String amountPi = SDMsgUtils.getPropValueStr(paramMap, "amountPi");
		amountPi = "".equals(amountPi)?"0":amountPi;
		//总金额  amountTotal
		String amountSt = SDMsgUtils.getPropValueStr(paramMap, "amountSt");
		amountSt = "".equals(amountSt)?"0":amountSt;
		//取消阶段为负数
		if("3".equals(SDMsgUtils.getPropValueStr(paramMap, "fpbz"))){
			amountInsu = "0".equals(amountInsu)?amountInsu:"-"+amountInsu;
			amountPi = "0".equals(amountPi)?amountPi:"-"+amountPi;
			amountSt = "0".equals(amountSt)?amountSt:"-"+amountSt;
		}
		//ZFP-2	Ybje 医保金额 amtInsu
		zfp.getYbje().setValue(amountInsu);
		//ZFP-3	Grjf 个人缴费  amountSelf
		zfp.getGrjf().setValue(amountPi);
		//ZFP-4	Zffs 支付方式  amtKind
		zfp.getZffs().setValue(SDMsgUtils.getPropValueStr(paramMap, "amtKind"));
		//ZFP-5	Sr	  舍入金额
		zfp.getSr().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountPrep"));
		//ZFP-6	Zje	 总金额  amountTotal
		zfp.getZje().setValue(amountSt);
		//ZFP-7	sfybm 收费员编码  doCode
		zfp.getSfybm().setValue(UserContext.getUser().getCodeEmp());
		//ZFP-8	Sfy	 收费员姓名  doName
		zfp.getSfy().setValue(UserContext.getUser().getNameEmp());
		//ZFP-9	Jssj 结算时间
		zfp.getJssj().setValue(sdf.format(new Date()));
		//ZFP-10 Fpbz 发票标识  发票标识(1：正常，2：重打，3：退费)
		zfp.getFpbz().setValue(SDMsgUtils.getPropValueStr(paramMap, "fpbz"));
		//ZFP-11 Xfph
		zfp.getXfph().setValue("");
		return zfp;
	}

	/**
	 * 创建PMI消息
	 * @param pmi
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public PMI createPMIMsg(PMI pmi, Map<String, Object> paramMap) throws DataTypeException {
		//PMI-1  Fpxbm 发票项编码
		pmi.getFpxbm().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeBill"));
		//PMI-2 Fpx 发票项目
		pmi.getFpx().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameBill"));
		if("3".equals(SDMsgUtils.getPropValueStr(paramMap, "fpbz"))){
			//PMI-3 Je 单项金额
			String amount = SDMsgUtils.getPropValueStr(paramMap, "amount");
			amount = "0".equals(amount)?"":amount;
			amount = "".equals(amount)?"0":"-"+amount;
			pmi.getJe().setValue(amount);
		}else{
			//PMI-3 Je 单项金额
			pmi.getJe().setValue(SDMsgUtils.getPropValueStr(paramMap, "amount"));
		}
		return pmi;
	}

	/**
	 * 创建TXD消息
	 * @param txd
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public TXD createTXDMsg(TXD txd, Map<String, Object> paramMap) throws DataTypeException {
		//TXD-1	remType	提醒类型	20
		txd.getRemType().setValue(SDMsgUtils.getPropValueStr(paramMap, "type"));
		//TXD-2	orderID	订单ID	32
		txd.getOrderID().setValue("");
		//TXD-3	amount	金额	10
		txd.getAmount().setValue("");
		//TXD-4	level	加急标志	12
		txd.getLevel().setValue("");
		//TXD-5	warnMsg	提醒内容	100
		switch (SDMsgUtils.getPropValueStr(paramMap, "type")){
			case "ZYYJ":{
				//预交金：yjpaysum
				String prePay = SDMsgUtils.getPropValueStr(paramMap, "yjpaysum");
				//总费用：fytotal
				String total = SDMsgUtils.getPropValueStr(paramMap, "fytotal");
				StringBuffer content = new StringBuffer("深圳大学总医院温馨提醒：您的总费用为"+total+"元，押金为"+prePay+"元。");
				content.append(SDMsgUtils.getPropValueStr(paramMap, "tip"));
				txd.getWarnMsg().setValue(content.toString());
			};break;
			case "JCSJJY":{
				txd.getWarnMsg().setValue(SDMsgUtils.getPropValueStr(paramMap, "content"));
			}break;
			case "ZDYJS":{
				txd.getWarnMsg().setValue(SDMsgUtils.getPropValueStr(paramMap, "content"));
			}break;
			default:{

			}break;
		}
		//TXD-6	extension	扩展	200
		txd.getExtension().setValue("");
		//TXD-7	queue	队列	15
		txd.getQueue().setValue("");
		return txd;
	}

	/**
	 * 创建MSA消息
	 * @param msa
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public MSA createMSAMsg(MSA msa, Map<String, Object> paramMap) throws DataTypeException {
		//MSA-1 错误情况：AA：接收  AE：错误
		msa.getAcknowledgementCode().setValue(SDMsgUtils.getPropValueStr(paramMap, "situation"));
		//msa.getAcknowledgementCode().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//MSA-2 oldMsgId
		msa.getMessageControlID().setValue(SDMsgUtils.getPropValueStr(paramMap, "msgOldId"));
		//MSA-3 text
		msa.getTextMessage().setValue(SDMsgUtils.getPropValueStr(paramMap, "msaText"));
		//msa.getTextMessage().setValue("成功");
		//MSA-4
		//msa.getExpectedSequenceNumber().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		msa.getExpectedSequenceNumber().setValue("100");
		//MSA-5
		//msa.getDelayedAcknowledgmentType().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		msa.getDelayedAcknowledgmentType().setValue("F");
		////MSA-6
		msa.getMsa6_ErrorCondition().getCe2_Text().setValue(SDMsgUtils.getPropValueStr(paramMap, "result"));
		return msa;
	}

	/**
	 * 创建ZIV消息
	 * @param ziv
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public ZIV createZIVMsg(ZIV ziv, Map<String, Object> paramMap) throws DataTypeException {
		//1	Fph	发票号
		ziv.getFph().setValue(SDMsgUtils.getPropValueStr(paramMap, "reptNo"));
		//2	Id	处方单序号或检查单序号
		ziv.getId().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//3	Jsfs 结算方式
		ziv.getJsfs().getCe2_Text().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//4	Jzje 记账金额
		ziv.getJzje().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//5	Grjf 个人缴费
		ziv.getGrjf().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//6	Sr 舍入金额
		ziv.getSr().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//7	Sfy	收费员
		ziv.getSfy().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameEmpPay"));
		//8	Ghrq 记账日期
		ziv.getGhrq().setValue(SDMsgUtils.getPropValueStr(paramMap, "datePay"));
		//9	Fprq 发票日期
		ziv.getFprq().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//10	Yjze	押金总额
		ziv.getYjze().setValue(SDMsgUtils.getPropValueStr(paramMap, "amount"));
		//11	Yjkyye	押金可用余额
		ziv.getYjkyye().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//12	YjisLow	押金余额是否低
		ziv.getYjisLow().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		return ziv;
	}

	/**
	 * 创建rxa消息
	 * @param rxa
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public RXA createRXAMsg(RXA rxa, Map<String, Object> paramMap) throws DataTypeException {
		String typestatus = SDMsgUtils.getPropValueStr(paramMap, "typestatus");
		//rxa-3 开始执行的日期／时间 医嘱开始时间
		rxa.getDateTimeStartOfAdministration().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap,"dateStart"));
		//rxa-4 结束执行的日期／时间 医嘱结束时间
		if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(paramMap, "dateStop"))){
			rxa.getDateTimeEndOfAdministration().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap, "dateStop"));
		}
		//rxa-5 "长短期标志identifier ^医嘱类别^费用标志
		//rxa-5.1  长短期标志identifier  0长期 1临时
		String euAlways = "0".equals(SDMsgUtils.getPropValueStr(paramMap, "euAlways")) ? "1" : "0";
		rxa.getAdministeredCode().getIdentifier().setValue(euAlways);
		//rxa-5.2  医嘱类别  1、检查类 2、治疗类 3、药品类 4、饮食类 5、材料类 6、护理类
		String ordType = "";
		if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype"))){
			String codeOrdtype = SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype").substring(0, 2);
			switch (codeOrdtype){
				case "01":ordType = "3";break;
				case "02":ordType = "1";break;
				case "03":ordType = "1";break;
				case "04":ordType = "2";break;
				case "05":ordType = "2";break;
				case "06":ordType = "6";break;
				case "07":ordType = "5";break;
				case "13":ordType = "4";break;
				default:ordType = "9";
			}
		}
		rxa.getAdministeredCode().getText().setValue(ordType);
		//如果多个标志同时存在，值会被替换掉
		//rxa-5.3 1、自备 2、嘱托 3、基数药 4、分药 5、计费 a、出院带药
		String flagBl = "";
		if ("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagBl"))) {
			flagBl = "5";
		} else if ("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagNote"))) {
			flagBl = "2";
		} else if ("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagSelf"))) {
			flagBl = "1";
		} else if ("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagMedout"))){
			flagBl = "a";
		}
//		else if ("1".equals(SDMsgUtils.getPropValueStr(map, "flagBase"))) {
//			flagBl = "3";
//		}
		rxa.getAdministeredCode().getNameOfCodingSystem().setValue(flagBl);
		//rxa-10 执行者 执行护士
		rxa.getAdministeringProvider(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
		rxa.getAdministeringProvider(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
		//rxa-11 执行定位 执行科室
		rxa.getAdministeredAtLocation().getPointOfCare().setValue(SDMsgUtils.getPropValueStr(paramMap, "codedept"));
		rxa.getAdministeredAtLocation().getRoom().setValue(SDMsgUtils.getPropValueStr(paramMap, "namedept"));

		//rxa-18 Substance/Treatment Refusal Reason拒绝物品/治疗原因 未作原因
		rxa.getSubstanceTreatmentRefusalReason(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "noteOrd"));
		//rxa-20 Completion Status完成情况 录入状态1 上账状态2
		rxa.getCompletionStatus().setValue("2");
		// Action Code-RXA　RXA行动代码 A增加 D删除 U更新(撤销D）
		String status = "";
		switch (typestatus){
			case "ADD":status = "A";break;
			case "DEL":status = "D";break;
			case "UPDATE":status = "U";break;
			default:status = "";
		}
		rxa.getActionCodeRXA().setValue(status);
		//rxa-22 System Entry Date/Time系统录入的日期/时间 执行时间
		//rxa.getSystemEntryDateTime().getTimeOfAnEvent().setValue(sdf.format(new Date()));
		rxa.getSystemEntryDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap, "datePlan"));

		return rxa;
	}

	/**
	 * 创建RXR消息
	 * @param rxr
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public RXR createRXRMsg(RXR rxr, Map<String, Object> paramMap) throws DataTypeException {
		// 给药方式
		rxr.getRoute().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeSupply"));
		// 草药用法
		String supplyName = sDQueryUtils.qrySupplyCode(SDMsgUtils.getPropValueStr(paramMap,"codeSupply"));
		rxr.getAdministrationMethod().getIdentifier().setValue(supplyName);
		return rxr;
	}

	/**
	 * 创建NTE消息
	 * @param nte
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public NTE createNTEMsg(NTE nte, Map<String, Object> paramMap) throws DataTypeException {
		// NTE 1- SetID 说明
		nte.getSetIDNTE().setValue("");
		//NTE 2 -SourceOfComment
		nte.getSourceOfComment().setValue("");
		// NTE 3 -说明
		//nte.getComment(0).setValue(SDMsgUtils.getPropValueStr(map, "price"));
		nte.getComment(0).setValue(SDMsgUtils.getPropValueStr(paramMap, "priceCg"));
		//NTE4.1  NTE4.2护理等级和护理名称
		nte.getCommentType().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOrd"));
		nte.getCommentType().getText().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameOrd"));
		return nte;
	}
	
	/**
	 * 创建NTE消息
	 * @param nte
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public NTE createNTEMsgForOmpO09(NTE nte, Map<String, Object> paramMap) throws DataTypeException {
		// NTE 1- SetID 说明
		//nte.getSetIDNTE().setValue("");
		//NTE 2 -SourceOfComment
		//nte.getSourceOfComment().setValue("");
		// NTE 3 -说明
		nte.getComment(0).setValue(SDMsgUtils.getPropValueStr(paramMap, "noteOrd"));
		return nte;
	}
	
	/**
	 * 创建OBX消息
	 * @param obx
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public OBX createOBXMsg(OBX obx, Map<String, Object> paramMap) throws HL7Exception{
		//OBX-1：  (无法放字符串)
		obx.getObx1_SetIDOBX().setValue("");
		//OBX-2：值类型：TX
		obx.getObx2_ValueType().setValue("TX");
		//OBX-3.1(Identifier)：标识符：R / A
		obx.getObx3_ObservationIdentifier().getIdentifier().setValue("A");
		//OBX-3.2(Text)：标识符名称： 会诊情况记录 / 会诊意见建议con_reply
		obx.getObx3_ObservationIdentifier().getText().setValue(SDMsgUtils.getPropValueStr(paramMap, "conReply"));
		//OBX-4：会诊应答唯一号：cn_consult_response.pk_consrep
		obx.getObx4_ObservationSubId().setValue(SDMsgUtils.getPropValueStr(paramMap, "pkConsrep"));
		//OBX-5：内容：cn_consult_response.con_advice
		String obx5 = SDMsgUtils.getPropValueStr(paramMap, "conAdvice");
		//Message parse = parser.parse(obx5);
		//TX tx = new TX(parse);
		//obx.getObx5_ObservationValue(0).setData(tx);
		obx.getObx5_ObservationValue(0).parse(obx5);
		//OBX-11：应答状态(0：未应答，1：应答)
		obx.getObx11_ObservationResultStatus().setValue(SDMsgUtils.getPropValueStr(paramMap, "flagRep"));
		//OBX-14：会诊应答时间：cn_consult_response.date_rep
		obx.getObx14_DateTimeOfTheObservation().getTs1_TimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap, "dateRep"));
		//OBX-16.1(IDNumber)：会诊科室编码：cn_consult_response.pk_dept_rep的code
		obx.getObx16_ResponsibleObserver().getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeDeptRep"));
		//OBX-16.2.1(FamilyName.Surname)：会诊医生编码：cn_consult_response.pk_emp_rep的code
		obx.getObx16_ResponsibleObserver().getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeEmpRep"));
		return obx;
	}

	/**
	 * 创建zpo消息
	 * @param zpo
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public void createZPOMsg(ZPO zpo, Map<String, Object> paramMap) throws DataTypeException {
		//1. ZPO-1：住院就诊流水号code_pv
		zpo.getMzhm().setValue(SDMsgUtils.getPropValueStr(paramMap, "codePv"));
		//2. ZPO-2：发票号
		zpo.getFPh().setValue("");
		// ZPO-6：费用类别名称：bl_invoice_dt.name_bill
		zpo.getBrxm().setValue(SDMsgUtils.getPropValueStr(paramMap, "name"));
		//ZPO-8：金额：bl_invoice_dt.amount
		zpo.getJzje().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(paramMap, "amt"));
		//3. ZPO-9：总金额
		zpo.getGrjf().getQuantity().setValue(SDMsgUtils.getPropValueStr(paramMap, "acoumt"));

	}
	/**
	 * 创建ZRI消息
	 * @param zri
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public void createZRIMsg(ZRI zri, Map<String, Object> paramMap) throws DataTypeException {
		//ZRI-1 发票编码
		String codeInv = SDMsgUtils.getPropValueStr(paramMap, "reptNo");
		zri.getFphm().setValue(codeInv);
		//ZRI-1-8 Zjje	总计金额	SDMsgUtils.getPropValueStr(paramMap, "amount")
		String amount = SDMsgUtils.getPropValueStr(paramMap, "amount");
		zri.getZjje().getMo1_Quantity().setValue(amount);
	}

	/**
	 * 创建QPD消息
	 * @param qpd
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void createQPDMsg(QPD qpd, Map<String, Object> paramMap) throws HL7Exception {
		//QPD-1. QPD-1：交易流水号
		qpd.getMessageQueryName().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "bankNo"));
		//QPD-2. QPD-2 ：渠道
		qpd.getQueryTag().setValue(SDMsgUtils.getPropValueStr(paramMap, "dtPaymode"));
		//QPD-3. QPD-3.1(IDNumber)：住院就诊流水号code_pv
		//QPD-3.5(IdentifierTypeCode)：PatientSerialNO：住院就诊流
		String qpd3 = SDMsgUtils.getPropValueStr(paramMap, "codePv")+"^^^^PatientSerialNO";
		qpd.getField(3, 0).parse(qpd3);


	}

	/**
	 * 创建RF1消息
	 * @param rf1
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public RF1 createRF1Msg(RF1 rf1, Map<String, Object> paramMap) throws DataTypeException {
		//会诊申请 RF1 -1：转诊状态(签署：NW，取消签署：CA)
		rf1.getReferralStatus().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "control"));
		//RF1 -2：转诊优先级：cn_order.flag-emer
		rf1.getReferralPriority().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "flagEmer"));
		//RF1 -3：转诊类型：Consult
		rf1.getReferralType().getCe1_Identifier().setValue("Consult");
		//RF1-4.1：会诊级别编码：cn_consult_apply.dt_conlevel
		rf1.getReferralDisposition(0).getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "dtConlevel"));
		//RF1-4.2：会诊级别名称：cn_consult_apply.dt_conlevel对应name
		rf1.getReferralDisposition(0).getCe2_Text().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameConlevel"));
		//RF1-5：转诊类别(1：院内，2：院外)
		rf1.getRf15_ReferralCategory().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "euType"));
		//RF1-6
		//rf1.getOriginatingReferralIdentifier().getEi1_EntityIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "euType"));
		//RF1-9：会诊日期：cn_consult_apply.date_cons
		rf1.getProcessDate().getTs1_TimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap, "dateCons"));
		//RF1-10：会诊目的：cn_consult_apply.reason
		rf1.getReferralReason(0).getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "reason"));
		return rf1;
	}

	/**
	 * 创建prd消息
	 * @param prd
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public PRD createPRDMsg(PRD prd, Map<String, Object> paramMap) throws DataTypeException {
		//PRD-2.1：会诊医生编码：cn_consult_response.pk_emp_rep的code
		prd.getPrd2_ProviderName(0).getXpn1_FamilyName().getFn1_Surname().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeEmpRep"));
		//PRD-2.2：会诊医生姓名：cn_consult_response.pk_emp_rep的name
		prd.getPrd2_ProviderName(0).getXpn2_GivenName().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameEmpRep"));
		//PRD-4.4.1：会诊科室编码：cn_consult_response.pk_dept_rep的code
		prd.getPrd4_ProviderLocation().getPl4_Facility().getHd1_NamespaceID().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeDeptRep"));
		//PRD-4.4.2：会诊科室名称：cn_consult_response.pk_dept_rep的name
		prd.getPrd4_ProviderLocation().getPl4_Facility().getHd2_UniversalID().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameDeptRep"));
		//PRD-7：会诊应答唯一号：cn_consult_response.pk_consrep
		prd.getPrd7_ProviderIdentifiers(0).getPi1_IDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap, "pkConsrep"));
		return prd;
	}

	/**
	 * 门诊已缴费记录明细查询 RSP_ZDL
	 * @param zdl
	 * @param paramMap
	 * @throws DataTypeException 
	 */
	public ZDL createZDLMsg(ZDL zdl, Map<String, Object> paramMap) throws DataTypeException {
		//1	Drug	药品编码及名称
		//1.1	Ypbm	药品编码
		zdl.getDrug().getCe1_Identifier().setValue("");
		//1.2	Ypmc	药品名称
		zdl.getDrug().getCe2_Text().setValue("");
		//2	Gg	规格
		zdl.getGg().setValue("");
		//3	Dw	单位
		zdl.getDw().setValue("");
		//4	Sl	数量
		zdl.getSl().setValue("");
		//5	Dj	单价
		zdl.getDj().getMo1_Quantity().setValue("");
		//6	Hjje	合计金额
		zdl.getHjje().getMo1_Quantity().setValue("");
		//7	kdks	开单科室（编码）
		//8	cfys	开单医生（编码）

		return zdl;
	}
	/**
	 * 门诊已缴费记录明细查询 RSP_ZDL
	 * @param qak
	 * @param paramMap
	 * @throws DataTypeException 
	 */
	public QAK createQAKMsg(QAK qak, Map<String, Object> paramMap) throws DataTypeException {
		//1.查询标记符
		qak.getQak1_QueryTag().setValue("");
		//2.查询应答状态
		qak.getQak2_QueryResponseStatus().setValue("");
		//3.消息查询名称
		qak.getQak3_MessageQueryName().getCe1_Identifier().setValue("");
		//4.匹配的总记录数
		qak.getQak4_HitCountTotal().setValue("");
		//5.本次响应中的记录数
		qak.getQak5_ThisPayload().setValue("");
		//6.剩余记录数
		qak.getQak6_HitsRemaining().setValue("");
		return qak;
	}
	
}
