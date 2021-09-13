package com.zebone.nhis.ma.pub.platform.sd.create;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.message.SRR_S01;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.foxinmy.weixin4j.util.StringUtil;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDOpMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.*;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 针对深大【门诊】HL7消息拼接提供的公共方法,住院消息慎用
 *
 * @author FH
 *
 */

@Component
public class CreateOpMsg {

	private static Logger loger = LoggerFactory.getLogger("nhis.nhis.lbHl7Log");

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	@Resource
	private SDQueryUtils sDQueryUtils;
	@Resource
	private	SDOpMsgMapper sDOpMsgMapper;

	/**
	 * MSH消息段
	 * @param msh
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	 public MSH createMSHMsg(MSH msh,Map<String, Object> paramMap) throws DataTypeException {

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
			 loger.info("MSH消息段，获取主机名字失败");
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
	public EVN createEVNMsg(EVN evn,Map<String, Object> paramMap){
		try{
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
		} catch (Exception e) {
			loger.info("创建EVN消息失败"+e);
			e.printStackTrace();
		}
		return evn;
	}

	/**
	 * 创建PID消息
	 * @param pid
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public PID createPIDMsg(PID pid,Map<String, Object> paramMap){
		try{
			//PID-1
			pid.getSetIDPID().setValue("");
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
			pid.getPatientIdentifierList(2).getID().setValue(SDMsgUtils.getPropValueStr(paramMap,"idNo"));
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
			//PID_14.2 职业编码
			pid.getPhoneNumberBusiness(0).getTelecommunicationUseCode().setValue(SDMsgUtils.getPropValueStr(paramMap,"dtOccu"));;
			//PID_14.3 职业名称
			pid.getPhoneNumberBusiness(0).getTelecommunicationEquipmentType().setValue(SDMsgUtils.getPropValueStr(paramMap,"occuName"));
			//PID_14.9 工作电话
			pid.getPhoneNumberBusiness(0).getAnyText().setValue(SDMsgUtils.getPropValueStr(paramMap,"telWork"));
			//PID_16 婚姻 - 调用转换方法 getDtMarry
			//pid.getMaritalStatus().getIdentifier().setValue(SDMsgUtils.getDtMarry(SDMsgUtils.getPropValueStr(paramMap,"dtMarry")));
			//S未婚、M已婚、D离婚、R再婚、A分居、W丧偶
			String marryName = SDMsgUtils.getPropValueStr(paramMap,"marryName");
			String marryCode=SDMsgUtils.getDtMarryByName(marryName);
			pid.getMaritalStatus().getIdentifier().setValue(marryCode);
			pid.getMaritalStatus().getText().setValue(marryName);
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
			pid.getEthnicGroup(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap,"dtNation"));
			pid.getEthnicGroup(0).getText().setValue(SDMsgUtils.getPropValueStr(paramMap,"nationName"));
			//PID_23 出生地
			pid.getBirthPlace().setValue(SDMsgUtils.getPropValueStr(paramMap,"addrBirth"));
			//PID_25 出生顺序
			pid.getBirthOrder().setValue(SDMsgUtils.getPropValueStr(paramMap,"sortNo"));
			//PID_28.1 国别
			pid.getNationality().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap,"dtCountry"));
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
			if("2".equals(euPvType)){
				pid.getBreedCode().getAlternateText().setValue("急诊");
			}else {
				pid.getBreedCode().getAlternateText().setValue("普通门诊");
			}
			pid.getBreedCode().getText().setValue(SDMsgUtils.getPropValueStr(paramMap,"insuName"));
			//PID_38 合同单位 -- 同步单位字典？？？
			pid.getProductionClassCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap,"insuCode"));
			pid.getProductionClassCode().getText().setValue(SDMsgUtils.getPropValueStr(paramMap,"insuName"));
			//PID 39 参保类型（新加：深圳项目）
			//1.把pid转换为字符串 2.添加39 3.还原为PID段，替换原来的pid
			String pidStr = pid.encode();
			pidStr = pidStr + "|"+"参保类型";
			pid.parse(pidStr);
		} catch (Exception e) {
			loger.info("创建PID消息失败"+e);
			e.printStackTrace();
		}
		return pid;
	}


	/**
	 * 创建PV1消息
	 * @param pv1
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public PV1 createPV1Msg(PV1 pv1,Map<String, Object> paramMap){
		try{
		String euPvType = SDMsgUtils.getPropValueStr(paramMap,"euPvtype");
		if("1".equals(euPvType)){   //门诊
			//【PV1-02】患者类别 - 门诊O
			pv1.getPatientClass().setValue("O");
			//【PV1-04】入院类型 - 门诊对应就诊服务类型
			List<Map<String, Object>> list = DataBaseHelper.queryForList("select code from PV_OP op inner join sch_sch sch on op.PK_SCH=sch.PK_SCH inner join SCH_SRV srv on sch.PK_SCHSRV=srv.PK_SCHSRV where PK_PV=?", SDMsgUtils.getPropValueStr(paramMap, "pkPv"));
			if(list!=null && list.size()>0){
				pv1.getAdmissionType().setValue(SDMsgUtils.getPropValueStr(list.get(0),"code"));
			}
			//【PV1-13】再次入院标识 - 门诊次数
			pv1.getReAdmissionIndicator().setValue("1");
			//【PV1-17.1】 挂号医生编码
			pv1.getAdmittingDoctor(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpPvOp"));
			//【PV1-17.2】 挂号医生名称
			pv1.getAdmittingDoctor(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(paramMap,"nameEmpPvOp"));
			//【PV1-26】合同总量-是否允许调阅健康档案 1允许，0不允许 ？？？
			pv1.getContractAmount(0).setValue("0");
		}else if("2".equals(euPvType)){
			//【PV1-02】患者类别 - 门诊O
			pv1.getPatientClass().setValue("O");
			//【PV1-04】入院类型 - 门诊对应就诊服务类型
			List<Map<String, Object>> list = DataBaseHelper.queryForList("select code from PV_ER er inner join sch_sch sch on er.PK_SCH=sch.PK_SCH inner join SCH_SRV srv on sch.PK_SCHSRV=srv.PK_SCHSRV where PK_PV=?", SDMsgUtils.getPropValueStr(paramMap, "pkPv"));
			if(list!=null && list.size()>0){
				pv1.getAdmissionType().setValue(SDMsgUtils.getPropValueStr(list.get(0),"code"));
			}//【PV1-13】再次入院标识 - 门诊次数
			pv1.getReAdmissionIndicator().setValue("1");
			//【PV1-17.1】 挂号医生编码
			pv1.getAdmittingDoctor(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpPvEr"));
			//【PV1-17.2】 挂号医生名称
			pv1.getAdmittingDoctor(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(paramMap,"nameEmpPvEr"));
			//【PV1-26】合同总量-是否允许调阅健康档案 1允许，0不允许 ？？？
			pv1.getContractAmount(0).setValue("0");
		}else if("4".equals(euPvType)||"30".equals(SDMsgUtils.getPropValueStr(paramMap, "dtMedicaltype"))||SDMsgUtils.getPropValueStr(paramMap, "nameDept").contains("体检")){ //体检
			//【PV1-02】患者类别 - 体检 T
			pv1.getPatientClass().setValue("T");
			//【PV1-04】入院类型 - 门诊对应就诊服务类型
			List<Map<String, Object>> list = DataBaseHelper.queryForList("select code from PV_OP op inner join sch_sch sch on op.PK_SCH=sch.PK_SCH inner join SCH_SRV srv on sch.PK_SCHSRV=srv.PK_SCHSRV where PK_PV=?", SDMsgUtils.getPropValueStr(paramMap, "pkPv"));
			if(list!=null && list.size()>0){
				pv1.getAdmissionType().setValue(SDMsgUtils.getPropValueStr(list.get(0),"code"));
			}
			//【PV1-13】再次入院标识 - 门诊次数
			pv1.getReAdmissionIndicator().setValue("1");
			//【PV1-17.1】 挂号医生编码
			pv1.getAdmittingDoctor(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpPvOp"));
			//【PV1-17.2】 挂号医生名称
			pv1.getAdmittingDoctor(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(paramMap,"nameEmpPvOp"));
		}else if("3".equals(euPvType)){  //住院
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
			//pv1.getReAdmissionIndicator().setValue(SDMsgUtils.getPropValueStr(paramMap,"ipTimes"));
			//【PV1-17.1】 挂号医生编码
			pv1.getAdmittingDoctor(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpPvOp"));
			//【PV1-17.2】 挂号医生名称
			pv1.getAdmittingDoctor(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(paramMap,"nameEmpPvOp"));
			//【PV1-14】 入院来源
			pv1.getAdmitSource().setValue(""); //参考入院来源字典 - 未确定？？？
		}
//		if(StringUtils.isBlank(pv1.getAdmittingDoctor(0).getFamilyName().getSurname().getValue())){
//			//【PV1-17.1】 挂号医生编码
//			String codeEmp = SDMsgUtils.getPropValueStr(paramMap,"codeEmpPvOp");
//			codeEmp = StringUtils.isBlank(codeEmp)?SDMsgUtils.getPropValueStr(paramMap,"codeEmpPvEr"):codeEmp;
//			pv1.getAdmittingDoctor(0).getIDNumber().setValue(codeEmp);
//			//【PV1-17.2】 挂号医生名称
//			String nameEmp = SDMsgUtils.getPropValueStr(paramMap,"nameEmpPvOp");
//			nameEmp = StringUtils.isBlank(nameEmp)?SDMsgUtils.getPropValueStr(paramMap,"nameEmpPvEr"):nameEmp;
//			pv1.getAdmittingDoctor(0).getFamilyName().getSurname().setValue(nameEmp);
//		}
		//【PV1-1】
		pv1.getSetIDPV1().setValue("1");
		//【PV1-3.4】患者当前位置-机构 => 科室
		pv1.getAssignedPatientLocation().getFacility().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeDept"));
		pv1.getAssignedPatientLocation().getLocationStatus().setValue(SDMsgUtils.getPropValueStr(paramMap,"nameDept"));
		//【PV1-13】再次入院标识 - 门诊次数
		pv1.getReAdmissionIndicator().setValue("1");
		//【PV1-7】 主管医生
		pv1.getAttendingDoctor(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpPhy"));
		pv1.getAttendingDoctor(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(paramMap,"nameEmpPhy"));
		//【PV1-9】 主管护士 codeEmpNs
		pv1.getConsultingDoctor(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpNs"));
		//【PV1-16】 VIP标识符 - 1：是 0：否 - 未确定？？？
		pv1.getVIPIndicator().setValue("0");
		//【PV1-18】 患者类别 - 参照患者结算类别 01自费,02医保,03公费
		//pv1.getPatientType().setValue(SDMsgUtils.getPatiType(SDMsgUtils.getPropValueStr(paramMap,"euHptype")));
		String euHptype = !"0".equals(SDMsgUtils.getPropValueStr(paramMap,"euHptype"))?"02":"01";
		pv1.getPatientType().setValue(euHptype);
		//【PV1-19】 就诊号码 his visit no
		pv1.getVisitNumber().getID().setValue(SDMsgUtils.getPropValueStr(paramMap,"codePv"));
		//【PV1-44】挂号时间(到诊时间)
		pv1.getAdmitDateTime().getTimeOfAnEvent().setValue(DateUtils.getDateTimeStr(new Date()));
		//PV1-46 当前患者差额 -待缴费用
		pv1.getPv146_CurrentPatientBalance().setValue(SDMsgUtils.getPropValueStr(paramMap,"amountOp"));
		//【PV1-47】总费用
		//String amount = StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(paramMap,"amountOp"))?SDMsgUtils.getPropValueStr(paramMap,"amountOp"):SDMsgUtils.getPropValueStr(paramMap,"amountIp");
		pv1.getPv147_TotalCharges().setValue(SDMsgUtils.getPropValueStr(paramMap,"amount"));
		} catch (Exception e) {
			loger.info("创建PV1消息失败"+e);
			e.printStackTrace();
		}
		return pv1;
	}


	/**
	 * 联系人相关信息
	 * @param nk1
	 * @param patiMap
	 * @throws DataTypeException
	 */
	public NK1 createNK1Msg(NK1 nk1, Map<String, Object> patiMap){
		try {
			if(patiMap!=null){
				nk1.getNKName(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(patiMap,"nameRel"));
				//关系
				nk1.getRelationship().getIdentifier().setValue(SDMsgUtils.getPropValueStr(patiMap,"dtRalation"));
				nk1.getRelationship().getText().setValue(SDMsgUtils.getPropValueStr(patiMap,"ralationName"));
				//地址
				/*String cc = SDMsgUtils.getPropValueStr(patiMap,"addrRel");
				nk1.getAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(cc);*/
				nk1.getAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(SDMsgUtils.getPropValueStr(patiMap,"addrRel"));
				nk1.getAddress(0).getZipOrPostalCode().setValue(SDMsgUtils.getPropValueStr(patiMap,"postcodeCur"));
				nk1.getAddress(0).getAddressType().setValue("H");
				//电话
				nk1.getPhoneNumber(0).getAnyText().setValue(SDMsgUtils.getPropValueStr(patiMap,"telRel"));
				//nk1.getPhoneNumber(0).get9999999X99999CAnyText().setValue(SDMsgUtils.getPropValueStr(patiMap, "telRel"));
				//NK1-10：亲属工作单位 ：深圳项目
				nk1.getNextOfKinAssociatedPartiesJobTitle().setValue("");
				//NK1-11：亲属职业 ：深圳项目
				nk1.getNextOfKinAssociatedPartiesJobCodeClass().getJobCode().setValue("");
				nk1.getNextOfKinAssociatedPartiesJobCodeClass().getJobClass().setValue("");

			}
		} catch (Exception e) {
			loger.info("关系NK1"+e);
		}
		return nk1;
	}

	/**
	 * 创建ORC消息
	 * @param orc
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public ORC createORCMsg(ORC orc,Map<String, Object> paramMap) {
		try{
			//ORC-1 申请控制   control
			orc.getOrderControl().setValue(SDMsgUtils.getPropValueStr(paramMap, "control"));
			String codeApply = SDMsgUtils.getPropValueStr(paramMap, "codeApply");
			//检查需要申请单号拼医嘱号
			if(SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype").startsWith("02")&&!codeApply.startsWith("N")){
				codeApply = codeApply+"H"+SDMsgUtils.getPropValueStr(paramMap, "ordsn");
			}
			//ORC 2
			if("OMP".equals(SDMsgUtils.getPropValueStr(paramMap, "msgtype"))&&"O09".equals(SDMsgUtils.getPropValueStr(paramMap, "triggerevent"))){
				//OMP^O09 ORC 第二段只保留医嘱号
				orc.getPlacerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "ordsn"));
			}else{
				//ORC-2.1 申请单号
				orc.getPlacerOrderNumber().getEntityIdentifier().setValue(codeApply);
				//ORC-2.2 患者xx号
				orc.getPlacerOrderNumber().getUniversalID().setValue(SDMsgUtils.getPropValueStr(paramMap,"codePi"));//就诊流水号
				//ORC-2.3 体检
				if("4".equals(SDMsgUtils.getPropValueStr(paramMap, "euPvtype"))|| "30".equals(SDMsgUtils.getPropValueStr(paramMap, "dtMedicaltype"))){
					//ORC-2.1 申请单号 体检的申请单号需要截取调前五位的 PEIS_
					orc.getPlacerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeApply"));
					orc.getPlacerOrderNumber().getUniversalIDType().setValue("T");//体检
				}else{
					orc.getPlacerOrderNumber().getUniversalIDType().setValue("O");//门诊
				}
			}
			//ORC-3  处方明细编码(执行单号)pkExocc （目前为申请单号）
			orc.getFillerOrderNumber().getEntityIdentifier().setValue(codeApply);
			//ORC-4 医嘱号ordsnParent 处方号：若处方号为空（检查检验等）传医嘱号
			String presNo = SDMsgUtils.getPropValueStr(paramMap, "presNo");
			orc.getPlacerGroupNumber().getEntityIdentifier().setValue("".equals(presNo)?SDMsgUtils.getPropValueStr(paramMap, "ordsn"):presNo);
			//ORC-5  申请单状态  
			//医嘱状态  euPvtype  1:门诊；2急诊；3住院 ；4体检；5家床 mec体检 fp家庭病床 门诊0：团检(项目内，不收费) 1：个检(项目外，收费)
			orc.getOrderStatus().setValue("1");
			//ORC-6
			//orc.getResponseFlag().setValue(SDMsgUtils.getPropValueStr(paramMap, "control"));
			//ORC-7.1.1  剂量
			orc.getQuantityTiming(0).getQuantity().getQuantity().setValue(SDMsgUtils.getPropValueStr(paramMap, "dosage"));
			// ORC-7.1.2 单位
			String unitName = sDQueryUtils.qryUnitByPK(SDMsgUtils.getPropValueStr(paramMap,"pkUnitDos"));
			orc.getQuantityTiming(0).getQuantity().getUnits().getIdentifier().setValue(unitName);
			//ORC-7.2.1频率 codeFreq
			String codeFreq=SDMsgUtils.getPropValueStr(paramMap, "codeFreq");
			//orc.getQuantityTiming(0).getInterval().getRi1_RepeatPattern().setValue(SDMsgUtils.getPropValueStr(map, "codeFreq"));// 牝鹿
			orc.getQuantityTiming(0).getInterval().getRepeatPattern().setValue(codeFreq);
			//ORC-7.2.2 频率单位名称
			String name = SDMsgUtils.getPropValueStr(sDQueryUtils.getNameFreq(codeFreq), "name");
			orc.getQuantityTiming(0).getInterval().getExplicitTimeInterval().setValue(name);
			////7.3 天数
			orc.getQuantityTiming(0).getDuration().setValue(SDMsgUtils.getPropValueStr(paramMap, "days"));
			//7.6 优先权
			String flagEmer = SDMsgUtils.getPropValueStr(paramMap, "flagEmer");
			if ("1".equals(flagEmer)){
				flagEmer = "A";
			}
			orc.getQuantityTiming(0).getPriority().setValue(flagEmer);
			String cnt = sDQueryUtils.qryItemFreq(SDMsgUtils.getPropValueStr(paramMap,"codeFreq"));
			orc.getQuantityTiming(0).getCondition().setValue(cnt);// 次数
			//7.11 用药天数
			orc.getOrc7_QuantityTiming(0).getTq11_OccurrenceDuration().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "days"));
			//ORC-8   parent
			String parent = SDMsgUtils.getPropValueStr(paramMap, "ordsnParent");
			String ordsn = SDMsgUtils.getPropValueStr(paramMap, "ordsn");
			//if(null!=parent && parent.equals(ordsn)){
			if(StringUtils.isNotBlank(parent)){
				orc.getParentOrder().getParentSPlacerOrderNumber().getEntityIdentifier().setValue(parent);
			}else{
				orc.getParentOrder().getParentSPlacerOrderNumber().getEntityIdentifier().setValue(ordsn);
			}
			//ORC-9  停止时间  	//事务时间
			orc.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			//ORC-10  操作人员编码
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
			//ORC-11  护士 检验人员编码
			/*String pkEmpCheck = "";
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
			orc.getVerifiedBy(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(empChkMap,"name"));*/
			//ORC-12  开立医生
			String codeEmpInput = SDMsgUtils.getPropValueStr(paramMap, "codeEmpInput");
			if (StringUtils.isNotBlank(codeEmpInput)) {
				orc.getOrderingProvider(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeEmpInput"));
			}else{
				Map<String, Object> epMap = DataBaseHelper.queryForMap("Select code_emp from bd_ou_employee where name_emp = '"+SDMsgUtils.getPropValueStr(paramMap, "nameEmpInput")+"'  and del_flag = '0' ");
				orc.getOrderingProvider(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(epMap, "codeEmp"));
			}
			orc.getOrderingProvider(0).getGivenName().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameEmpInput"));
			orc.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameEmpInput"));
			//ORC-13 护嘱标志
			/*String flagDoctor = "0".equals(SDMsgUtils.getPropValueStr(paramMap, "flagDoctor"))?"1":"0";
			orc.getEntererSLocation().getPointOfCare().setValue(flagDoctor);*/
			//ORC-15  开始时间
			if("ORM".equals(SDMsgUtils.getPropValueStr(paramMap, "msgtype")) && "O01".equals(SDMsgUtils.getPropValueStr(paramMap, "triggerevent"))){
				orc.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap,"datePlan"));
			}else{
				if(null != SDMsgUtils.getPropValueDate(paramMap,"c")){
					orc.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(paramMap,"dateStart"));
				}else{
					orc.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(sdf.format(new Date()));
				}
			}
			//ORC-17 // 录入科室
			Map<String, Object> deptMap = sDQueryUtils.queryDeptByPk(SDMsgUtils.getPropValueStr(paramMap,"pkDept"));
			if(deptMap != null){
				orc.getEnteringOrganization().getIdentifier().setValue(SDMsgUtils.getPropValueStr(deptMap, "codeDept"));
				orc.getEnteringOrganization().getText().setValue(SDMsgUtils.getPropValueStr(deptMap, "nameDept"));
			}
			//ORC-18  区分首次末次
			/*if ("1".equals(SDMsgUtils.getPropValueStr(paramMap, "flagFirst"))) {
				orc.getEnteringDevice().getIdentifier().setValue("F");
				orc.getEnteringDevice().getText().setValue(SDMsgUtils.getPropValueStr(paramMap, "firstNum"));
			} else {
				orc.getEnteringDevice().getIdentifier().setValue("L");
				orc.getEnteringDevice().getText().setValue(SDMsgUtils.getPropValueStr(paramMap, "lastNum"));
			}*/
			//ORC-21  患者科室
			orc.getOrderingFacilityName(0).getOrganizationName().setValue(SDMsgUtils.getPropValueStr(deptMap, "codeDept"));
			orc.getOrderingFacilityName(0).getOrganizationNameTypeCode().setValue(SDMsgUtils.getPropValueStr(deptMap, "nameDept"));
			//ORC20.3 取消执行传值  orc2Str  paramMap.put("typestatus", "DEL");
			if("DEL".equals(SDMsgUtils.getPropValueStr(paramMap, "typestatus"))){
				//orc.getAdvancedBeneficiaryNoticeCode().getNameOfCodingSystem().setValue(orc2Str);
				//orc.getAdvancedBeneficiaryNoticeCode().getAlternateIdentifier().setValue(orc2Str);
			}

			//ORC-23  医嘱类型  codeOrdtype
			//orc.getOrderingFacilityPhoneNumber(0).getEmailAddress().setValue(SDMsgUtils.getPropValueStr(paramMap, "euAlways"));
			orc.getOrderingFacilityPhoneNumber(0).getExtension().setValue(SDMsgUtils.getPropValueStr(paramMap, "euAlways"));
			//orc.getOrderingFacilityPhoneNumber(0).getAnyText().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype"));
			String codeOrdtype = SDMsgUtils.getPropValueStr(paramMap,"codeOrdtype");
			String ordType="";
			if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype"))){
				codeOrdtype = SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype").toString().substring(0, 2);
				switch (codeOrdtype){
					case "01":{
						if("0103".equals(SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype")))ordType = "8";
						else ordType = "3";
					}break;
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
			/*//ORC-24.1：医嘱开始时间：cn_order.date_start
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
			//		}*/
			} catch (Exception e) {
			loger.info("创建ORC消息失败"+e);
			e.printStackTrace();
		}
		return orc;
	}

	/**
	 * 创建FT1消息
	 * @param ft1
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public FT1 createFT1Msg(FT1 ft1,Map<String, Object> paramMap){
		try{
			//ft1-1  //发生序号
			ft1.getSetIDFT1().setValue("1");
			//ft1-2
			if("PMI".equals(SDMsgUtils.getPropValueStr(paramMap,"triggerevent"))){
				//发票号（住院结算）
				ft1.getTransactionID().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeInv"));
				//预交金
				//String amountPrep = SDMsgUtils.getPropValueStr(paramMap, "amountPrep");
				//ft1-11.3	结算总金额  amountSt 未扣除预交金 amountTotal扣除预交金
				//String amountSt = SDMsgUtils.getPropValueStr(paramMap, "amountSt");
				//amountSt = "".equals(amountSt)?"0":amountSt;  发票金额：amountInv
				String amountTotal = SDMsgUtils.getPropValueStr(paramMap, "amountSt");
				amountTotal = "".equals(amountTotal)?"0":amountTotal;
				//15.3 保险总金额
				String amountInsu = SDMsgUtils.getPropValueStr(paramMap, "amountInsu");
				amountInsu = "".equals(amountInsu)?"0":amountInsu;
				//22.3自费金额  amountPi 未扣除预交金 amount扣除预交金
				//String amountPi = SDMsgUtils.getPropValueStr(paramMap, "amountPi");
				//amountPi = "".equals(amountPi)?"0":amountPi; 发票金额amountPiInv
				String amountSelf = SDMsgUtils.getPropValueStr(paramMap, "amountPi");
				amountSelf = "".equals(amountSelf)?"0":amountSelf;
				/**
				if("3".equals(SDMsgUtils.getPropValueStr(paramMap, "fpbz"))){
					//ft1-11.3	结算总金额  amountSt
					amountSt = "".equals(amountSt)?"0":"-"+amountSt;
					//15.3 保险总金额
					amountInsu = "".equals(amountInsu)?"0":"-"+amountInsu;
					//22.3自费金额
					amountPi = "".equals(amountPi)?"0":"-"+amountPi;
				}else{
					//ft1-11.3	结算总金额  amountSt
					amountSt = "".equals(amountSt)?"0":amountSt;
					//15.3 保险总金额
					amountInsu = "".equals(amountInsu)?"0":amountInsu;
					//22.3自费金额
					amountPi = "".equals(amountPi)?"0":amountPi;
				}
				*/
				//ft1-11.3	结算总金额  amountSt
				ft1.getTransactionAmountExtended().getFromValue().setValue(amountTotal);
				//11.4
				//ft1.getTransactionAmountExtended().getToValue().setValue(SDMsgUtils.getPropValueStr(paramMap, "totalAmount"));
				//15.1 Insurance Amount 	保险总金额
				//ft1.getInsuranceAmount().getPrice().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(paramMap, "amtInsu"));
				//15.3 保险总金额
				ft1.getInsuranceAmount().getFromValue().setValue(amountInsu);
				//22.1  自费
				//ft1.getUnitCost().getPrice().getQuantity().setValue(SDMsgUtils.getPropValueStr(paramMap, "selfAmount"));
				//22.3自费金额
				ft1.getUnitCost().getFromValue().setValue(amountSelf);
			}else {
				//预交金收据号
				ft1.getTransactionID().setValue(SDMsgUtils.getPropValueStr(paramMap,"reptNo"));
			}
			//ft1-4 有问题		//收取时间（事务时间）
			ft1.getTransactionDate().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			//ft1-6	 //预交金 EU_DPTYPE 0 就诊结算；1 中途结算；2 结算冲账；3 欠费补缴；4 取消结算；9 住院预交金
			//"结算类别（请参照患者结算类别） 01：自费 02：医保 03：公费"
			if("9".equals(SDMsgUtils.getPropValueStr(paramMap, "euDptype"))){
				//住院预交金为固定值
				ft1.getTransactionType().setValue("INPAY");
			}else{
				ft1.getTransactionType().setValue(SDMsgUtils.getPropValueStr(paramMap, "amtType"));
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
		} catch (Exception e) {
			loger.info("创建FT1消息失败"+e);
			e.printStackTrace();
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
	public DG1 createDG1Msg(DG1 dg1,Map<String, Object> paramMap){
		try{
		String pkDiagPre = SDMsgUtils.getPropValueStr(paramMap,"pkDiagPre");
		Map<String, Object> queryDiag = sDQueryUtils.queryDiag(pkDiagPre);
		//DG1-3
		dg1.getDiagnosisCodeDG1().getIdentifier().setValue(SDMsgUtils.getPropValueStr(queryDiag,"codeCd"));
		//DG1-4 诊断名称
		String diag = SDMsgUtils.getPropValueStr(paramMap,"descDiagPre");
		if("".equals(diag)){
			diag = SDMsgUtils.getPropValueStr(queryDiag,"nameCd");
		}
		dg1.getDiagnosisDescription().setValue(diag);
		//DG1-6
		dg1.getDiagnosisType().setValue(SDMsgUtils.getPropValueStr(paramMap,"num"));
		} catch (Exception e) {
			loger.info("创建DG1消息失败"+e);
			e.printStackTrace();
		}
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
	public MFI createMFIMsg(MFI mfi, Map<String, Object> paramMap) {
		try{
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
		} catch (Exception e) {
			loger.info("创建MFI消息失败"+e);
			e.printStackTrace();
		}
		return mfi;
	}

	/**
	 * 创建MFE消息
	 * @param mfe
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public MFE createMFEMsg(MFE mfe,ST key, Map<String, Object> paramMap) {
		try {
			//MFE1
			mfe.getRecordLevelEventCode().setValue(SDMsgUtils.getPropValueStr(paramMap, "rleCode"));
			//MFE4
			key.setValue(SDMsgUtils.getPropValueStr(paramMap, "codeDept"));
			mfe.getPrimaryKeyValueMFE(0).setData(key);
			mfe.getPrimaryKeyValueType(0).setValue("CE");
		} catch (Exception e) {
			loger.info("创建MFE消息失败"+e);
			e.printStackTrace();
		}
		return mfe;
	}

	/**
	 * 创建OBR消息
	 * @param obr
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public OBR createOBRMsg(OBR obr, Map<String, Object> paramMap) {
		try {
			//项目编号  codeApply
			obr.getUniversalServiceIdentifier().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOrd"));
			//医嘱码
			obr.getUniversalServiceIdentifier().getText().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameOrd"));
			//医嘱分类
			obr.getUniversalServiceIdentifier().getNameOfCodingSystem().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype"));
			//描述@todo待确认
			obr.getRelevantClinicalInfo().setValue(SDMsgUtils.getPropValueStr(paramMap, "bbname"));
			//描述@todo待确认
			//obr.getRelevantClinicalInfo().setValue(SDMsgUtils.getPropValueStr(map, "noteOrd"));
			//执行科室//@todo换成编码或名称
			Map<String,Object> deptMap=sDQueryUtils.queryDeptByPk(paramMap.get("pkDeptExec").toString());
			obr.getDiagnosticServSectID().setValue(SDMsgUtils.getPropValueStr(deptMap, "codeDept"));
			//obr-15 样本类型
			//TODO 此字段暂时不需要
			obr.getSpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "bbname"));
			obr.getSpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "descBody"));
			obr.getReasonForStudy(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "purpose"));
			//OBR-23-1
			obr.getChargeToPractice().getDollarAmount().getQuantity().setValue("");
			obr.getChargeToPractice().getDollarAmount().getDenomination().setValue("");
			//OBR-23-2
			obr.getChargeToPractice().getChargeCode().getIdentifier().setValue("");
			obr.getChargeToPractice().getChargeCode().getText().setValue("");
		} catch (Exception e) {
			loger.info("创建OBR消息失败"+e);
			e.printStackTrace();
		}
		return obr;
	}

	/**
	 * 创建RXO消息    目前只有深大的门诊医嘱再用次此方法
	 * @param rxo
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public RXO createRXOMsg(RXO rxo, Map<String, Object> paramMap) throws DataTypeException{
		try{
			String propValueStr = SDMsgUtils.getPropValueStr(paramMap, "triggerevent");
			//判断是否 医嘱项目编码是否为空
			if(StringUtils.isBlank(SDMsgUtils.getPropValueStr(paramMap, "codeOrd"))){
				//查询医嘱编码
				Map<String, Object> map = DataBaseHelper.queryForMap("select code from bd_ord,CN_ORDER where BD_ORD.PK_ORD=CN_ORDER.PK_ORD and PK_CNORD=?", SDMsgUtils.getPropValueStr(paramMap, "pkCnord"));
				paramMap.put("codeOrd",SDMsgUtils.getPropValueStr(map,"code"));
			}
			if("O09".equals(propValueStr)){
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
				//RXO-1.3 医嘱类型codeOrdtype
				if("01".equals(SDMsgUtils.getPropValueStr(paramMap, "codeOrdtype").substring(0,2))){
					rxo.getRequestedGiveCode().getNameOfCodingSystem().setValue("d");
				}else{
					rxo.getRequestedGiveCode().getNameOfCodingSystem().setValue("n");
				}
				//RXO-2 数量
				rxo.getRequestedGiveAmountMinimum().setValue(SDMsgUtils.getPropValueStr(paramMap, "quanCg"));
				//RXO-4 单位
				String unitName = sDQueryUtils.qryUnitByPK(SDMsgUtils.getPropValueStr(paramMap,"pkUnitCg"));//获取医嘱计费单位
				rxo.getRequestedGiveUnits().getIdentifier().setValue(unitName);
				//RXO-4 规格
				rxo.getRxo5_RequestedDosageForm().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(paramMap,"spec"));
				//RXO-8.4.2 药房编码
				Map<String, Object> itemMap = sDQueryUtils.queryDeptByPk(SDMsgUtils.getPropValueStr(paramMap,"pkDeptExec"));
				if(itemMap!=null){
					rxo.getDeliverToLocation().getFacility().getUniversalID().setValue(SDMsgUtils.getPropValueStr(itemMap, "codeDept"));
				}
				// 草药付数
				rxo.getNumberOfRefills().setValue(SDMsgUtils.getPropValueStr(paramMap, "ords"));
				// 预防治疗标志 皮试
				Integer flagSt = DataBaseHelper.queryForScalar("select count(1) from BD_PD b where FLAG_ST='1' and b.CODE=? ", Integer.class, new Object[]{SDMsgUtils.getPropValueStr(paramMap, "codeOrd")});
				rxo.getIndication(0).getIdentifier().setValue(flagSt==0?"":"Y");

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
				String unitPdName = sDQueryUtils.qryUnitByPK(SDMsgUtils.getPropValueStr(paramMap,"pkUnitPd"));
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
				/*rxo-9 // Allow Substitutions 允许替代 拆分标识：0可拆分；1不可拆分 */
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
		} catch (Exception e) {
			loger.info("创建RXO消息失败"+e);
			e.printStackTrace();
		}
		return rxo;
	}

	/**
	 * 创建ZFP消息
	 * @param zfp
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public ZFP createZFPMsg(ZFP zfp, Map<String, Object> paramMap){
		try {

			//ZFP-1	Fph	  发票号
			//1.1发票号
			zfp.getFph().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeInv"));
			//1.2纸质发票号
			zfp.getFph().getCe2_Text().setValue("");
			// 1.3待确定
			zfp.getFph().getCe3_NameOfCodingSystem().setValue("");
			//1.4 重打发票号
			zfp.getFph().getCe4_AlternateIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "oldCodeInv"));
			//ZFP-2	Ybje 医保金额 amtInsu
			String amountInsu = SDMsgUtils.getPropValueStr(paramMap, "amountInsu");
			amountInsu = "".equals(amountInsu)?"0":amountInsu;
			zfp.getYbje().setValue(amountInsu);
			//ZFP-3	Grjf 个人缴费  amountSelf
			String amountPi = SDMsgUtils.getPropValueStr(paramMap, "amountPi");
			amountPi = "".equals(amountPi)?"0":amountPi;
			zfp.getGrjf().setValue(amountPi);
			//ZFP-4	Zffs 支付方式  amtKind
			zfp.getZffs().setValue(SDMsgUtils.getPropValueStr(paramMap, "amtKind"));
			//ZFP-5	Sr	  舍入金额
			zfp.getSr().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountPrep"));
			//ZFP-6	Zje	 总金额  amountTotal
			String amountSt = SDMsgUtils.getPropValueStr(paramMap, "amountSt");
			amountSt = "".equals(amountSt)?"0":amountSt;
			zfp.getZje().setValue(amountSt);
			//ZFP-7	sfybm 收费员编码  doCode
			//zfp.getSfybm().setValue(SDMsgUtils.getPropValueStr(paramMap, "doCode"));
			zfp.getSfybm().setValue(UserContext.getUser().getCodeEmp());
			//ZFP-8	Sfy	 收费员姓名  doName
			//zfp.getSfy().setValue(SDMsgUtils.getPropValueStr(paramMap, "doName"));
			zfp.getSfy().setValue(UserContext.getUser().getNameEmp());
			//ZFP-9	Jssj 结算时间
			zfp.getJssj().setValue(sdf.format(new Date()));
			//ZFP-10 Fpbz 发票标识  发票标识(1：正常，2：重打，3：退费)
			zfp.getFpbz().setValue(SDMsgUtils.getPropValueStr(paramMap, "fpbz"));
			//ZFP-11 Xfph
			zfp.getXfph().setValue("");

		} catch (Exception e) {
			loger.info("创建ZFP消息失败"+e);
			e.printStackTrace();
		}
		return zfp;
	}

	/**
	 * 创建PMI消息
	 * @param pmi
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public PMI createPMIMsg(PMI pmi, Map<String, Object> paramMap){
		try{
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
		} catch (Exception e) {
			loger.info("创建PMI消息失败"+e);
			e.printStackTrace();
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
	public MSA createMSAMsg(MSA msa, Map<String, Object> paramMap)  {
		try{
			//MSA-1 错误情况：AA：接收  AE：错误
			String situation = SDMsgUtils.getPropValueStr(paramMap, "situation");
			msa.getAcknowledgementCode().setValue(StringUtils.isNotBlank(situation)?situation:"AA");
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
		} catch (Exception e) {
			loger.info("创建MSA消息失败"+e);
			e.printStackTrace();
		}
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
		ziv.getFph().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//2	Id	处方单序号或检查单序号
		ziv.getId().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//3	Jsfs	结算方式
		ziv.getJsfs().getCe2_Text().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//4	Jzje	记账金额
		ziv.getJzje().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//5	Grjf	个人缴费
		ziv.getGrjf().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//6	Sr	舍入金额
		ziv.getSr().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//7	Sfy	收费员
		ziv.getSfy().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//8	Ghrq	记账日期
		ziv.getGhrq().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//9	Fprq	发票日期
		ziv.getFprq().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//10	Yjze	押金总额
		ziv.getYjze().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
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
		// NTE-说明
		//nte.getComment(0).setValue(SDMsgUtils.getPropValueStr(map, "price"));
		nte.getComment(0).setValue(SDMsgUtils.getPropValueStr(paramMap, "priceCg"));
		//护理等级和护理名称
		nte.getCommentType().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOrd"));
		nte.getCommentType().getText().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameOrd"));

		return nte;
	}

	/**
	 * 创建zpo消息
	 * @param zpo
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public void createZPOMsg(ZPO zpo, Map<String, Object> paramMap) throws DataTypeException {
		//ZPO-1：门诊号code_op
		zpo.getMzhm().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOp"));
		// ZPO-2：发票号
		zpo.getFPh().setValue(SDMsgUtils.getPropValueStr(paramMap, "reptNo"));
		//ZPO-3	医保卡号
		//ZPO-4：门诊日期
		zpo.getMzrq().setValue(SDMsgUtils.getPropValueStr(paramMap, "datePay"));
		//ZPO-5处方单号
		// ZPO-6：姓名
		zpo.getBrxm().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameEmpPay"));
		//ZPO-7：收费类别
		zpo.getSflb().setValue(SDMsgUtils.getPropValueStr(paramMap, "euDptype"));
		//ZPO-8：记账金额
		zpo.getJzje().getMo1_Quantity().setValue("0");
		//ZPO-9：个人缴费
		zpo.getGrjf().getQuantity().setValue(SDMsgUtils.getPropValueStr(paramMap, "amount"));
		//ZPO-10 结算方式  0 微信，1 支付宝，2 银联，9 其他（59-平安APP）
		zpo.getJsfs().setValue(SDMsgUtils.getPropValueStr(paramMap, "nameInsu"));
		//ZPO-14 西药费  11
		zpo.getXyf().setValue(SDMsgUtils.getPropValueStr(paramMap, "11"));
		//ZPO-15 中成药 14
		zpo.getZcy().setValue(SDMsgUtils.getPropValueStr(paramMap, "14"));
		//ZPO-16  中草药 12
		zpo.getZcay().setValue(SDMsgUtils.getPropValueStr(paramMap, "12"));
		//ZPO-17 诊查费 09
		zpo.getZcf().setValue(SDMsgUtils.getPropValueStr(paramMap, "09"));
		//ZPO-18 检验费 22
		zpo.getJyf().setValue(SDMsgUtils.getPropValueStr(paramMap, "22"));
		//zpo-19 检查费 21
		zpo.getJcf().setValue(SDMsgUtils.getPropValueStr(paramMap, "21"));
		//zpo -22 手术费 33
		zpo.getSsf().setValue(SDMsgUtils.getPropValueStr(paramMap, "33"));
		//zpo -24 其他费  43
		zpo.getQtf().setValue(SDMsgUtils.getPropValueStr(paramMap, "43"));
		//zpo -33 总金额
		zpo.getZje().setValue(SDMsgUtils.getPropValueStr(paramMap, "amount"));
		//zpo-35 收费员
		zpo.getSfy().setValue("T00001");
		//zpo-42 就诊流水号
		zpo.getJzlsh().setValue(SDMsgUtils.getPropValueStr(paramMap, "codePv"));
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
	 * 查询患者基本信息PID，PV1
	 * @param patMap
	 * @return
	 */
	public Map<String,Object> qrySdOpPID_PV1(Map<String,Object> patMap){
		Map<String,Object> map = new HashMap<>();
		//1、查询患者基本信息
		List<Map<String, Object>> pati = sDOpMsgMapper.queryPatListOp(patMap);
		if(null != pati && pati.size()>0){
			map.putAll(pati.get(0));
		}
		return map;
	}

	/**
	 * 退费消息段ZPM
	 * @param zpm
	 * @param paramMap
	 * @throws DataTypeException
	 */
	public void createZPMMsg(ZPM zpm, Map<String, Object> paramMap) throws DataTypeException {
	//1	Mzhm	门诊号码	ST	O	50
	zpm.getMzhm().setValue(SDMsgUtils.getPropValueStr(paramMap, "codePv"));
	//2	Fph		发票号	ST	R	50
	zpm.getFph().setValue(SDMsgUtils.getPropValueStr(paramMap, "reptNo"));
	//3	YjsId	预结算ID	ST	R	75
	zpm.getYjsId().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
	//4	Ybkh	医保卡号	ST	O	50
	zpm.getYbkh().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
	//5	Mzrq	门诊日期	DT	O	8
	zpm.getMzrq().setValue(SDMsgUtils.getPropValueStr(paramMap, "dateBegin"));
	//6	OrderId	微信（第三方）订单号	ST	R	75
	zpm.getOrderId().setValue(SDMsgUtils.getPropValueStr(paramMap, "serialNo"));
	//7	Brxm	姓名	ST	O	50
	zpm.getBrxm().setValue(SDMsgUtils.getPropValueStr(paramMap, "namePi"));
	//8	Sflb	收费类别	ST	O	1		0代表门诊 1 代表急诊
	zpm.getSflb().setValue(SDMsgUtils.getPropValueStr(paramMap, "euPvtype"));
	//9	Ybje	医保金额	MO	O	12
	zpm.getYbje().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountInsu"));
	//10 Zfje	自费金额	MO	O	12
	zpm.getZfje().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountPi"));
	//11 Jsfs	结算方式	ST	O	50
	zpm.getJsfs().setValue(SDMsgUtils.getPropValueStr(paramMap, "dtSttype"));
	//12 sqdlx	申请单类型	ST	R	10		1.挂号退费    2、诊间支付退费  3、住院预交金退费-待定
	String applyType = paramMap.containsKey("CancelReg")?"1":"2";
	zpm.getsqdlx().setValue(applyType);
	//34 Zje	总金额	MO	O	12
	zpm.getZje().setValue(SDMsgUtils.getPropValueStr(paramMap, "amount"));
	//38 Paymethod	支付方式	e.g. WX
	zpm.getPaymethod().setValue(SDMsgUtils.getPropValueStr(paramMap, "dtPaymode"));
	//39 PayChannel	渠道		e.g.JY160
	zpm.getPayChannel().setValue(SDMsgUtils.getPropValueStr(paramMap, "dtPaymode"));
	}


	/**
	 * RAR^RAR(门诊待缴费记录查询结果)-add医保
	 * @param sendApp
	 * @param status
	 * @param text
	 * @return
	 * @throws DataTypeException
	 */
	public static String createRAR_RAR(String oldMsgId,String sendApp,String status,String text) throws HL7Exception {
		RAR_RAR rar = new RAR_RAR();
		SDMsgUtils.createMSHMsg(rar.getMSH(), SDMsgUtils.getMsgId(), "RAR", "RAR");
		rar.getMSH().getReceivingApplication().getNamespaceID().setValue(sendApp);
		rar.getMSA().getAcknowledgementCode().setValue(status);
		rar.getMSA().getMessageControlID().setValue(oldMsgId);
		rar.getMSA().getTextMessage().setValue(text);
		rar.getMSA().getExpectedSequenceNumber().setValue("100");
		rar.getMSA().getDelayedAcknowledgmentType().setValue("F");
		return SDMsgUtils.getParser().encode(rar);
	}
	
	
	/**
	 * SQR^ZQ1(反馈缴费记录/返回订单状结果）
	 * @param sendApp
	 * @param status
	 * @param text
	 * @return
	 * @throws DataTypeException
	 */
	public static String createSQR_ZQ1(String oldMsgId,String sendApp,String status,String text) throws HL7Exception {
		SQR_ZQ1 sqr_zq1 = new SQR_ZQ1();
		SDMsgUtils.createMSHMsg(sqr_zq1.getMSH(), SDMsgUtils.getMsgId(), "SQR", "ZQ1");
		sqr_zq1.getMSH().getReceivingApplication().getNamespaceID().setValue(sendApp);
		sqr_zq1.getMSA().getAcknowledgementCode().setValue(status);
		sqr_zq1.getMSA().getMessageControlID().setValue(oldMsgId);
		sqr_zq1.getMSA().getTextMessage().setValue(text);
		sqr_zq1.getMSA().getExpectedSequenceNumber().setValue("100");
		sqr_zq1.getMSA().getDelayedAcknowledgmentType().setValue("F");
		return SDMsgUtils.getParser().encode(sqr_zq1);
	}
	
	/**
	 * SRM^S01 消息响应：SRR^S01(患者建档响应) SRR^S01(预约挂号响应)-add新医保
	 * @param sendApp
	 * @param status
	 * @param text
	 * @return
	 * @throws DataTypeException
	 */
	public static String createSRR_S01(String oldMsgId,String sendApp,String status,String text) throws HL7Exception {
		SRR_S01 srr_s01 = new SRR_S01();
		SDMsgUtils.createMSHMsg(srr_s01.getMSH(), SDMsgUtils.getMsgId(), "SRR", "S01");
		srr_s01.getMSH().getReceivingApplication().getNamespaceID().setValue(sendApp);
		srr_s01.getMSA().getAcknowledgementCode().setValue(status);
		srr_s01.getMSA().getMessageControlID().setValue(oldMsgId);
		srr_s01.getMSA().getTextMessage().setValue(text);
		srr_s01.getMSA().getExpectedSequenceNumber().setValue("100");
		srr_s01.getMSA().getDelayedAcknowledgmentType().setValue("F");
		return SDMsgUtils.getParser().encode(srr_s01);
	}
}