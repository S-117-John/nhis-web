package com.zebone.nhis.ma.pub.platform.sd.util;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.receive.SDMsgReceive;
import com.zebone.nhis.ma.pub.platform.sd.vo.MsgTreeNode;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

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
import ca.uhn.hl7v2.model.v24.message.ORL_O22;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;


/**
 * Hl7消息工具类(灵璧直接复制版)
 * @author chengjia
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDMsgUtils {
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat sdfl = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static EncodingCharacters encChars=new EncodingCharacters('|', null);

	private static HapiContext context = null;
	private static Parser parser=null;

	private static PipeParser pipeParser=null;
	@Autowired
	private SDMsgReceive sDMsgReceive;
	@Resource
	private SDQueryUtils sDQueryUtils;

	public static Parser getParser(){
		if(context==null) context = new DefaultHapiContext();
		if(parser==null) parser = context.getGenericParser();

		return parser;
	}

//	public static Message getMessage(String msg){
//		try {
//			Message hapiMsg = getParser().parse(msg);
//			return hapiMsg;
//		} catch (HL7Exception e) {
//
//			e.printStackTrace();
//			return null;
//		}
//	}

//	public static MSH getMessageMsh(String msg){
//		try {
//			Message hapiMsg = getParser().parse(msg);
//			Segment segment = (Segment) hapiMsg.get("MSH");
//			MSH msh=(MSH)segment;
//
//			return msh;
//		} catch (HL7Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}


	public static void createMSHMsg(MSH msh,String msgId,String msgType,String triggerEvent){
		try {
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getDateTimeOfMessage().getTimeOfAnEvent().setValue(sdf.format(new Date()));
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
			//MSH-4 发送服务器
			try {
				msh.getMsh4_SendingFacility().getHd1_NamespaceID().setValue(InetAddress.getLocalHost().getHostName());
			} catch (UnknownHostException e) {
				//获取主机名字失败
				loger.info("MSH消息段，获取主机名字失败");
			}
		} catch (Exception e) {
			loger.info("创建MSH消息失败"+e);
			e.printStackTrace();
		}
	}


	public static void createMSHMsgCn(MSH msh,String msgId,String msgType,String triggerEvent){
		try {
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getDateTimeOfMessage().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			msh.getMessageType().getMessageType().setValue(msgType);
			msh.getMessageType().getTriggerEvent().setValue(triggerEvent);
			msh.getMessageControlID().setValue(msgId);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			//MSH-4 发送服务器
			try {
				msh.getMsh4_SendingFacility().getHd1_NamespaceID().setValue(InetAddress.getLocalHost().getHostName());
			} catch (UnknownHostException e) {
				//获取主机名字失败
				loger.info("MSH消息段，获取主机名字失败");
			}
			msh.getSendingApplication().getNamespaceID().setValue("NHIS");
			if (triggerEvent == "O21" && msgType == "OML") {
				msh.getReceivingApplication().getNamespaceID().setValue("LIS");//@todo
			}else {
				msh.getReceivingApplication().getNamespaceID().setValue("EAI");//@todo
			}
			msh.getAcceptAcknowledgmentType().setValue("AL");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			msh.getCountryCode().setValue("CHN");
			msh.getCharacterSet(0).setValue("GBK");
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("创建MSH消息失败"+e);
			e.printStackTrace();
		}
	}

	/**
	 * 创建SD-MSH(引用灵璧科室信息)
	 *单条发送检查申请
	 *
	 */
	public static void createMSHMsgCnLbPacs(MSH msh,String msgId,String msgType,String triggerEvent,String codeDept){
		try {
			msh.getFieldSeparator().setValue("|");
			msh.getEncodingCharacters().setValue("^~\\&");
			msh.getDateTimeOfMessage().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			msh.getMessageType().getMessageType().setValue(msgType);
			msh.getMessageType().getTriggerEvent().setValue(triggerEvent);
			msh.getMessageControlID().setValue(msgId);
			msh.getProcessingID().getProcessingID().setValue("P");
			msh.getVersionID().getVersionID().setValue("2.4");
			//MSH-4 发送服务器
			try {
				msh.getMsh4_SendingFacility().getHd1_NamespaceID().setValue(InetAddress.getLocalHost().getHostName());
			} catch (UnknownHostException e) {
				//获取主机名字失败
				loger.info("MSH消息段，获取主机名字失败");
			}
			msh.getSendingApplication().getNamespaceID().setValue("NHIS");
			String receive = "RIS";
			//根据执行科室判断检查发送消息头
			if("".equals(codeDept) || codeDept == null){
				receive = "EAI";
			}else if (codeDept.startsWith("0309")) {
				//放射科室>>>>影像科室
				receive = "LWRIS";
			}else if(codeDept.startsWith("0303")){
				//超声科室
				receive = "LWUS";
			}else if(codeDept.startsWith("0305")){
				//内镜科室
				receive = "LWEIS";
			}
			msh.getReceivingApplication().getNamespaceID().setValue(receive);
			msh.getAcceptAcknowledgmentType().setValue("AL");
			msh.getApplicationAcknowledgmentType().setValue("AL");
			msh.getCountryCode().setValue("CHN");
			msh.getCharacterSet(0).setValue("GBK");
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("创建MSH消息失败"+e);
			e.printStackTrace();
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
			//PID_3 住院号
			pid.getPatientIdentifierList(0).getID().setValue(getPropValueStr(patMap,"codeIp"));
			pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("PatientNO");
			//PID_3 卡号 codePi
			//pid.getPatientIdentifierList(1).getID().setValue(getPropValueStr(patMap,"cardno"));
			pid.getPatientIdentifierList(1).getID().setValue(getPropValueStr(patMap,"codePi"));
			pid.getPatientIdentifierList(1).getIdentifierTypeCode().setValue("IDCard");
			//PID_3 身份证号
			pid.getPatientIdentifierList(2).getID().setValue(getPropValueStr(patMap,"idNo"));
			pid.getPatientIdentifierList(2).getIdentifierTypeCode().setValue("IdentifyNO");
			//PID_3 门诊号
			pid.getPatientIdentifierList(3).getID().setValue(getPropValueStr(patMap,"codeOp"));
			pid.getPatientIdentifierList(3).getIdentifierTypeCode().setValue("Outpatient");
			//PID_3 电脑号(医保相关 ：深圳项目添加)
			pid.getPatientIdentifierList(3).getID().setValue("");
			pid.getPatientIdentifierList(3).getIdentifierTypeCode().setValue("SBpcNO");
			//PID_5 姓名
			pid.getPatientName(0).getFamilyName().getSurname().setValue(getPropValueStr(patMap,"namePi"));
			if("1".equals(SDMsgUtils.getPropValueStr(patMap, "flagInf"))){
				String sql = "select pv.* from pv_infant inf ,PV_ENCOUNTER pv where inf.pk_pv=pv.pk_pv and inf.CODE =?";
				Map<String, Object> maInfo = DataBaseHelper.queryForMap(sql,SDMsgUtils.getPropValueStr(patMap,"codeIp"));
				//PID_6 母亲姓名
				pid.getMotherSMaidenName(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(maInfo,"namePi"));
				//PID_21 母亲的ID  (改为母亲的就诊流水号：code_pv_ma)
				pid.getMotherSIdentifier(0).getID().setValue(SDMsgUtils.getPropValueStr(maInfo,"codePv"));

			}
			//PID_7 生日
			pid.getDateTimeOfBirth().getTimeOfAnEvent().setValue(getPropValueStr(patMap,"birthDate"));
			//PID_8 性别 - 调用转换方法 getSex
			pid.getAdministrativeSex().setValue(getSex(getPropValueStr(patMap,"dtSex")));
			//PID_11 患者地址  0 现住址 1 工作地址 2户口地址
			pid.getPatientAddress(0).getStreetAddress().getStreetOrMailingAddress().setValue(getPropValueStr(patMap,"addrCur"));
			//pid.getPatientAddress(0).getZipOrPostalCode().setValue(getPropValueStr(patMap,"addrCur"));
			pid.getPatientAddress(0).getAddressType().setValue("H");
			pid.getPatientAddress(1).getStreetAddress().getStreetOrMailingAddress().setValue(getPropValueStr(patMap,"unitWork"));
			//pid.getPatientAddress(1).getZipOrPostalCode().setValue(getPropValueStr(patMap,"addrRegiDt"));
			pid.getPatientAddress(1).getAddressType().setValue("O");
			pid.getPatientAddress(2).getStreetAddress().getStreetOrMailingAddress().setValue(getPropValueStr(patMap,"addrRegi"));
			//pid.getPatientAddress(2).getZipOrPostalCode().setValue(getPropValueStr(patMap,"postcodeRegi"));
			pid.getPatientAddress(2).getAddressType().setValue("N");
			//PID_13.9 家庭电话
			pid.getPhoneNumberHome(0).getAnyText().setValue(getPropValueStr(patMap,"mobile"));
			//pid.getPhoneNumberHome(0).getPhoneNumber().setValue(getPropValueStr(patMap,"mobile"));
			//PID14.2 职业编码
			pid.getPhoneNumberBusiness(0).getTelecommunicationUseCode().setValue(getPropValueStr(patMap,"dtOccu"));
			//PID14.3：职业名称
			pid.getPhoneNumberBusiness(0).getTelecommunicationEquipmentType().setValue(getPropValueStr(patMap,"occuName"));
			//PID_14.9 工作电话
			pid.getPhoneNumberBusiness(0).getAnyText().setValue(getPropValueStr(patMap,"telWork"));
			//PID_16 婚姻 - 调用转换方法 getDtMarry
			String dtMarry = getPropValueStr(patMap,"dtMarry");
			if(dtMarry!=null && !"".equals("dtMarry")){
				String sql = "select t.old_id from bd_defdoc t where t.code_defdoclist='000006' and t.code=?";
				Map<String, Object> dtMarryMap = DataBaseHelper.queryForMap(sql, dtMarry);
				dtMarry = SDMsgUtils.getPropValueStr(dtMarryMap,"oldId");
			}
			//pid.getMaritalStatus().getIdentifier().setValue(SDMsgUtils.getDtMarry(SDMsgUtils.getPropValueStr(paramMap,"dtMarry")));
			pid.getMaritalStatus().getIdentifier().setValue(dtMarry);
			pid.getMaritalStatus().getText().setValue(getPropValueStr(patMap,"marryName"));
			//PID -18 文化程度
			pid.getPid18_PatientAccountNumber().getCx1_ID().setValue(SDMsgUtils.getPropValueStr(patMap, "dtEdu"));
			//PID_19 社会保险号
			pid.getSSNNumberPatient().setValue(getPropValueStr(patMap,"insurNo"));
			//PID_22 民族
			//pid.getEthnicGroup(0).getIdentifier().setValue(getPropValueStr(patMap,"dtNation"));
			pid.getEthnicGroup(0).getIdentifier().setValue(getPropValueStr(patMap,"nationOldId"));
			pid.getEthnicGroup(0).getText().setValue(getPropValueStr(patMap,"nationName"));
			//PID_23 出生地
			pid.getBirthPlace().setValue(getPropValueStr(patMap,"addrBirth"));
			//PID_25 出生顺序
			pid.getBirthOrder().setValue(getPropValueStr(patMap,"sortNo"));
			//PID_28.1 国别
			//pid.getNationality().getIdentifier().setValue(getPropValueStr(patMap,"dtCountry"));
			pid.getNationality().getIdentifier().setValue(getPropValueStr(patMap,"countryOldId"));
			pid.getNationality().getText().setValue(getPropValueStr(patMap,"countryName"));
			//PID_35 种类代码？？？ 籍贯  addr_birth
			pid.getSpeciesCode().getIdentifier().setValue(getPropValueStr(patMap,"addrOrigin"));
			//PID_36 品种代码 (医保相关类型 ：深圳项目添加)
			pid.getBreedCode().getIdentifier().setValue("310");
			pid.getBreedCode().getAlternateIdentifier().setValue("21");
			pid.getBreedCode().getAlternateText().setValue("普通住院");
			//PID_38 合同单位 -- 同步单位字典？？？
			String codeParent = SDMsgUtils.getPropValueStr(patMap,"codeParent");
			String insuCode = SDMsgUtils.getPropValueStr(patMap,"insuCode");
			codeParent = "".equals(codeParent)?insuCode:codeParent;
			pid.getProductionClassCode().getIdentifier().setValue(codeParent);
			//pid.getProductionClassCode().getText().setValue(SDMsgUtils.getPropValueStr(patMap,"insuName"));
			//PID 39 参保类型（新加：深圳项目）
			//1.把pid转换为字符串 2.添加39 3.还原为PID段，替换原来的pid
			String pidStr = pid.encode();
			pidStr = pidStr + "|"+insuCode;
			pid.parse(pidStr);
		} catch (Exception e) {
			loger.info("创建PID消息失败"+e);
			e.printStackTrace();
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
			}else if(euPvType != null && euPvType.equals("2"))//急诊
			{
				//【PV1-02】患者类别 - 门诊O
				pv1.getPatientClass().setValue("O");
				//【PV1-04】入院类型 - 门诊对应就诊服务类型
				pv1.getAdmissionType().setValue(getPvInType(getPropValueStr(patMap,"euSrvtype"), "", euPvType));
				//【PV1-13】再次入院标识 - 门诊次数
				pv1.getReAdmissionIndicator().setValue(getPropValueStr(patMap,"opTimes"));
				//【PV1-26】合同总量-是否允许调阅健康档案 1允许，0不允许 ？？？
				pv1.getContractAmount(0).setValue("0");
			}else if(euPvType != null)//住院
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
				}else if(patMap.containsKey("bedNum")){
					pv1.getAssignedPatientLocation().getBed().setValue(getPropValueStr(patMap,"bedNum"));
				}
				//【PV1-04】入院类型
				//pv1.getAdmissionType().setValue(getPvInType(getPropValueStr(patMap,"dtLevelDise"), getPropValueStr(patMap,"flagInf"), euPvType));//通过方法统一处理
				//pv_ip.dt_level_dise 入院病情
				pv1.getAdmissionType().setValue(getPropValueStr(patMap,"dtLevelDise"));
				//【PV1-13】再次入院标识 - 住院次数
				pv1.getReAdmissionIndicator().setValue(getPropValueStr(patMap,"ipTimes"));
				//【PV1-14】 入院来源  DT_INTYPE
				String sql = "select t.old_id from bd_defdoc t where t.code_defdoclist='000104' and t.code=?";
				Map<String, Object> intypeMap = DataBaseHelper.queryForMap(sql, getPropValueStr(patMap,"dtIntype"));
				pv1.getAdmitSource().setValue(getPropValueStr(intypeMap,"oldId")); //参考入院来源字典 - 未确定？？？
			}
			//【PV1-3.4】患者当前位置-机构 => 科室
			pv1.getAssignedPatientLocation().getFacility().getNamespaceID().setValue(getPropValueStr(patMap,"codeDept"));
			pv1.getAssignedPatientLocation().getLocationStatus().setValue(getPropValueStr(patMap,"nameDept"));
			//【PV1-7】 主管医生
			pv1.getAttendingDoctor(0).getIDNumber().setValue(getPropValueStr(patMap,"codeEmpPhy"));
			pv1.getAttendingDoctor(0).getFamilyName().getSurname().setValue(getPropValueStr(patMap,"nameEmpPhy"));
			////【PV1-9】 主管护士 codeEmpNs
			pv1.getConsultingDoctor(0).getIDNumber().setValue(getPropValueStr(patMap,"codeEmpNs"));
			//【PV1-16】 VIP标识符 - 1：是 0：否 - 未确定？？？
 			pv1.getVIPIndicator().setValue("0");
			//【PV1-17】 入院医生 = 收治医生
			pv1.getAdmittingDoctor(0).getIDNumber().setValue(getPropValueStr(patMap,"codeEmpTre"));
  			pv1.getAdmittingDoctor(0).getFamilyName().getSurname().setValue(getPropValueStr(patMap,"nameEmpTre"));
			//【PV1-18】 患者类别 - 参照患者结算类别 01自费,02医保,03公费
  			String euHptype = !"0".equals(getPropValueStr(patMap,"euHptype"))?"02":"01";
			pv1.getPatientType().setValue(euHptype);
			//【PV1-19】 就诊号码 his visit no
			pv1.getVisitNumber().getID().setValue(getPropValueStr(patMap,"codePv"));
			//【PV1-44】入院日期 - 入科前为登记时间，入科后为入科时间
			if("1".equals(getPropValueStr(patMap,"euPvtype"))){//门诊
				pv1.getAdmitDateTime().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			}else{
				if(!"".equals(getPropValueStr(patMap,"dateAdmit"))) {
					pv1.getAdmitDateTime().getTimeOfAnEvent().setValue(getPropValueStr(patMap,"dateAdmit"));
				}else{
					if(!"".equals(getPropValueStr(patMap,"dateBegin"))){
						pv1.getAdmitDateTime().getTimeOfAnEvent().setValue(getPropValueStr(patMap,"dateBegin"));
					}
				}
				//取消出院时候flagCancel为0  出院为1
				//【PV1-45】出院日期effective_e
				if("1".equals(getPropValueStr(patMap,"flagCancel"))){
					//取消就诊时;出院时间
					if(!"".equals(getPropValueStr(patMap,"dateCancel"))){
						pv1.getDischargeDateTime(0).getTimeOfAnEvent().setValue(getPropValueStr(patMap,"dateCancel"));
					}
				}else{ //取消出院
					 //转科时传递时间日期
					if(("A02").equals(patMap.get("EVNopeType"))){
						//转科，转科时间 转科操作时，源科室记录写入日期 dateBeginAdt
						StringBuilder sql = new StringBuilder();
						sql.append("select to_char(adt.DATE_BEGIN,'YYYYMMDDHH24MISS') date_Begin from PV_ADT adt ");
						sql.append("where adt.DATE_END is null and adt.PK_PV=?");
						Map<String, Object> queryAdt = DataBaseHelper.queryForMap(sql.toString(), SDMsgUtils.getPropValueStr(patMap, "pkPv"));
						pv1.getDischargeDateTime(0).getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(queryAdt,"dateBegin"));
					}else if(!"".equals(getPropValueStr(patMap,"dateEnd"))){
						//出院时，结束日期
						pv1.getDischargeDateTime(0).getTimeOfAnEvent().setValue(getPropValueStr(patMap,"dateEnd"));
					}else {
						//取消出院时间 (系统当前时间)
						pv1.getDischargeDateTime(0).getTimeOfAnEvent().setValue(sdf.format(new Date()));
					}
				}
			}
			//51 在院标志
			pv1.getPv151_VisitIndicator().setValue(SDMsgUtils.getPropValueStr(patMap, "flagIn"));
			//管床护士 52 code_emp_ns
			pv1.getOtherHealthcareProvider(0).getIDNumber().setValue(getPropValueStr(patMap,"codeEmpNs"));
			String pkPv = getPropValueStr(patMap,"pkPv");
			//1. PV1-52.2(FamilyName.Surname)：医疗小组：pv_encounter.pk_wg的code(select t.code_wg from org_dept_wg t where t.pk_wg=)
			if(!"".equals(getPropValueStr(patMap,"pkWg"))){
				Map<String, Object> pkWg = DataBaseHelper.queryForMap("select t.code_wg from org_dept_wg t where t.pk_wg=?",getPropValueStr(patMap,"pkWg") );
				pv1.getOtherHealthcareProvider(0).getFamilyName().getSurname().setValue(getPropValueStr(pkWg,"codeWg"));
			}

			if(pkPv!=null && !"".equals(pkPv)){
				String sql = "select to_char(WMSYS.WM_CONCAT((select emp.code_emp from bd_ou_employee emp where emp.pk_emp=s.pk_emp and s.dt_role='00'))) YS ,"
						+ "to_char(WMSYS.WM_CONCAT((select emp.code_emp from bd_ou_employee emp where emp.pk_emp=s.pk_emp and s.dt_role='0000'))) ZRYS ,"
						+"to_char(WMSYS.WM_CONCAT((select emp.code_emp from bd_ou_employee emp where emp.pk_emp=s.pk_emp and s.dt_role='0001'))) GCYS ,"
						+"to_char(WMSYS.WM_CONCAT((select emp.code_emp from bd_ou_employee emp where emp.pk_emp=s.pk_emp and s.dt_role='0002'))) ZYYS ,"
						+"to_char(WMSYS.WM_CONCAT((select emp.code_emp from bd_ou_employee emp where emp.pk_emp=s.pk_emp and s.dt_role='0003'))) JXYS ,"
						+"to_char(WMSYS.WM_CONCAT((select emp.code_emp from bd_ou_employee emp where emp.pk_emp=s.pk_emp and s.dt_role='0004'))) SXYS ,"
						+"to_char(WMSYS.WM_CONCAT((select emp.code_emp from bd_ou_employee emp where emp.pk_emp=s.pk_emp and s.dt_role='0005'))) ZKYS ,"
						+"to_char(WMSYS.WM_CONCAT((select emp.code_emp from bd_ou_employee emp where emp.pk_emp=s.pk_emp and s.dt_role='0006'))) SJYS ,"
						+"to_char(WMSYS.WM_CONCAT((select emp.code_emp from bd_ou_employee emp where emp.pk_emp=s.pk_emp and s.dt_role='01'))) HS ,"
						+"to_char(WMSYS.WM_CONCAT((select emp.code_emp from bd_ou_employee emp where emp.pk_emp=s.pk_emp and s.dt_role='0100'))) ZGHS ,"
						+"to_char(WMSYS.WM_CONCAT((select emp.code_emp from bd_ou_employee emp where emp.pk_emp=s.pk_emp and s.dt_role='0101'))) HSZ "
						+"from pv_staff s where s.DATE_END is null and s.pk_pv =?";
				Map<String, Object> queryForMap = DataBaseHelper.queryForMap(sql, pkPv);
				//PV1-52.3(GivenName)：上级医师：pv_staff.dt_role='0006'的code(select t.code_emp from bd_ou_employee t where t.pk_emp=pv_staff.pk_emp)
				pv1.getOtherHealthcareProvider(0).getGivenName().setValue(getPropValueStr(queryForMap,"sjys"));
				//PV1-52.4(MiddleInitialOrName)：主任医师：pv_staff.dt_role='0000'的code同上
				pv1.getOtherHealthcareProvider(0).getXcn4_SecondAndFurtherGivenNamesOrInitialsThereof().setValue(getPropValueStr(queryForMap,"zrys"));
				//PV1-52.5(Suffix)：质控医师：pv_staff.dt_role='0005'的code同上
				pv1.getOtherHealthcareProvider(0).getXcn5_SuffixEgJRorIII().setValue(getPropValueStr(queryForMap,"zkys"));
				//PV1-52.6(Prefix)：护士长：pv_staff.dt_role='0101'的code同上
				pv1.getOtherHealthcareProvider(0).getXcn6_PrefixEgDR().setValue(getPropValueStr(queryForMap,"hsz"));
			}

		} catch (Exception e) {
			loger.info("创建PV1消息失败"+e);
			e.printStackTrace();
		}

	}


	public static void createMSAMsg(MSA msa,String msgId){
		try {
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgId);

		} catch (Exception e) {
			loger.info("创建MSA消息失败"+e);
			e.printStackTrace();
		}

	}

	public String[] getAckMsg(String msgId,Message message,String logPrefix) throws HL7Exception, ParseException{
		Segment segment = (Segment) message.get("MSH");
		if(segment==null)  return null;
		MSH msh;
		msh=(MSH)segment;
		MSA msa;
		String[] ackMsgs=new String[2];
		ackMsgs[0]="";
		ackMsgs[1]="";
		String ackMsg="";
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
			msa.getTextMessage().setValue("成功");
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
			msa.getTextMessage().setValue("成功");
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
			msa.getTextMessage().setValue("成功");
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
			msa.getTextMessage().setValue("成功");
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
			msa.getTextMessage().setValue("成功");
			ackMsg = getParser().encode(ack);
		}else if("QRY".equals(msgType) && "P04".equals(triggerEvent)){
			//毒麻药柜计费录入消息
			ACK ack = new ACK();
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);
			msa.getTextMessage().setValue("处理成功");
			ackMsg = getParser().encode(ack);
		}else if(msgType.equals("QBP")||msgType.equals("DFT")||msgType.equals("SQM")||msgType.equals("SRM")||msgType.equals("ZHY")||msgType.equals("QRY")){
			//微信查询不返回默认消息（深大项目）
			ackMsg = sDMsgReceive.sendFeedbackMsg(message, logPrefix);
			if(ackMsg==null || "".equals(ackMsg)){
				//如果消息为空，返回默认消息
				ACK ack = new ACK();
				msh = ack.getMSH();
				createMSHMsg(msh, msgId, "ACK", triggerEvent);
				msh.getReceivingApplication().getNamespaceID().setValue("EAI");
				msa=ack.getMSA();
				//接受
				msa.getAcknowledgementCode().setValue("AR");
				msa.getMessageControlID().setValue(msgCntrlID);
				msa.getTextMessage().setValue("失败：生成反馈消息为空！");
				ackMsg = getParser().encode(ack);
			}
		}else{
			ACK ack = new ACK();
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			msh.getReceivingApplication().getNamespaceID().setValue("EAI");
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgCntrlID);
			msa.getTextMessage().setValue("成功");
			ackMsg = getParser().encode(ack);
		}
		//MSH
		ackMsgs[1]=ackMsg;
		return ackMsgs;
	}
	
	public String getUnDefinAE(String message,String error){
		String msgType ="unknown";
		String msgCtrId ="unknown";
		String triggerEvent = "unknown";
		try{
			message = message.replace("|", "@");
			message = message.replace("^", "#");
			String[] ary = message.split("@");
			msgType = ary[8];
			msgCtrId = ary[9];
			triggerEvent = msgType.split("#")[1];
		}catch(Exception e){
			loger.error("", e);
		}
		ACK ack = new ACK();
		createMSHMsgReturn(ack.getMSH(), "ACK", triggerEvent, "EAI", msgCtrId);
		MSA msa = ack.getMSA();
		try{
			msa.getAcknowledgementCode().setValue("AE");
			msa.getMessageControlID().setValue(msgCtrId);
			ack.getMSH().getMessageControlID().setValue(msgCtrId);
			if(error!=null){
				msa.getTextMessage().setValue("消息格式错误,无法解析,类型["+msgType+"],唯一标识["+msgCtrId+"],"+error);
			}else{
				msa.getTextMessage().setValue("消息格式错误,无法解析,类型["+msgType+"],唯一标识["+msgCtrId+"]");
			}
			return new PipeParser().encode(ack);
		}catch(Exception e){
			loger.error("", e);
		}
		return "unknown";	
	}
	
	private String createMSHMsgReturn(MSH msh,String msgType,String triggerEvent,String receiveApp,String msgId){
		//	String msgId = encMgr.findSysSnGenerator("msg_id").toString();
			try {
				msh.getFieldSeparator().setValue("|");
				msh.getEncodingCharacters().setValue("^~\\&");
				msh.getDateTimeOfMessage().getTimeOfAnEvent().setValue(sdf.format(new Date()));
				msh.getSendingApplication().getNamespaceID().setValue("NHIS");
				msh.getReceivingApplication().getNamespaceID().setValue(receiveApp);
				msh.getMessageType().getMessageType().setValue(msgType);
				msh.getMessageType().getTriggerEvent().setValue(triggerEvent);
			//	msgId ="CIS_"+msgId;
				msh.getMessageControlID().setValue(msgId);
				msh.getProcessingID().getProcessingID().setValue("P");
				msh.getVersionID().getVersionID().setValue("2.4");
				
			} catch (Exception e) {
				loger.error("创建PID消息失败", e);
				throw new RuntimeException(e);
			}
			return msgId;			
		}	

	/**
	 * 异常数据返回使用
	 * @param msgId 消息Id
	 * @param message 接收消息体
	 * @param errorTxt 异常信息
	 * @return
	 * @throws HL7Exception
	 */
	public String[] getAckMsgError(String msgId,Message message,String errorTxt) throws HL7Exception{
		Segment segment = (Segment) message.get("MSH");
		if(segment==null)  return null;
		MSH msh;
		msh=(MSH)segment;
		MSA msa;
		String[] ackMsgs=new String[2];
		ackMsgs[0]="";
		ackMsgs[1]="";
		String ackMsg="";
		String msgType=msh.getMessageType().getMessageType().getValue();
		String triggerEvent=msh.getMessageType().getTriggerEvent().getValue();
		String msgCntrlID=msh.getMessageControlID().getValue();
		String receive = msh.getMsh3_SendingApplication().getHd1_NamespaceID().getValue();
		loger.info("getAckMsg:"+msgType+"-"+triggerEvent);
		if(msgType==null) return null;
		ackMsgs[0]=msgType+"^"+triggerEvent;
		if(msgType.equals("ACK")){
			ACK ack = new ACK();

			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);

			msh.getReceivingApplication().getNamespaceID().setValue(receive);
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AE");
			msa.getMessageControlID().setValue(msgCntrlID);
			msa.getTextMessage().setValue(errorTxt);
			ackMsg = getParser().encode(ack);
		}else if(msgType.equals("ADT")){
			ACK ack = new ACK();
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			msh.getReceivingApplication().getNamespaceID().setValue(receive);
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AE");
			msa.getMessageControlID().setValue(msgCntrlID);
			msa.getTextMessage().setValue(errorTxt);
			ackMsg = getParser().encode(ack);
		}else if(msgType.equals("ORL")){
			//检验申请
			ORL_O22 ack = new ORL_O22();
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ORL", triggerEvent);
			msh.getReceivingApplication().getNamespaceID().setValue(receive);
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AE");
			msa.getMessageControlID().setValue(msgCntrlID);
			msa.getTextMessage().setValue(errorTxt);
			ackMsg = getParser().encode(ack);
		}else if(msgType.equals("ORL")){
			//检验申请收费
			ACK ack = new ACK();
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			msh.getReceivingApplication().getNamespaceID().setValue(receive);
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AE");
			msa.getMessageControlID().setValue(msgCntrlID);
			msa.getTextMessage().setValue(errorTxt);
			ackMsg = getParser().encode(ack);
		}else if(msgType.equals("ORP")){
			//门诊收费、退费
			ACK ack = new ACK();
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			msh.getReceivingApplication().getNamespaceID().setValue(receive);
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AE");
			msa.getMessageControlID().setValue(msgCntrlID);
			msa.getTextMessage().setValue(errorTxt);
			ackMsg = getParser().encode(ack);
		}else if(msgType.equals("SRM")){
			ACK ack = new ACK();
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "SRR", triggerEvent);
			msh.getReceivingApplication().getNamespaceID().setValue(receive);
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AE");
			msa.getMessageControlID().setValue(msgCntrlID);
			msa.getTextMessage().setValue(errorTxt);
			ackMsg = getParser().encode(ack);
		}else{
			ACK ack = new ACK();
			msh = ack.getMSH();
			createMSHMsg(msh, msgId, "ACK", triggerEvent);
			msh.getReceivingApplication().getNamespaceID().setValue(receive);
			msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AE");
			msa.getMessageControlID().setValue(msgCntrlID);
			msa.getTextMessage().setValue(errorTxt);
			ackMsg = getParser().encode(ack);
		}
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
		if(key==null||"".equals(key)||map==null||map.size()<=0){
			return "";
		}
		if(map.containsKey(key)){
			Object obj=map.get(key);
			value=obj==null?"":obj.toString().trim();
		}
		return value;
	}

	/**
	 * 取日期内容
	 * @param map
	 * @return
	 */
	public static Date getPropValueDate(Map<String, Object> map,String key) {
		Date value=null;
		boolean have=map.containsKey(key);
		if(have){
			Object obj=map.get(key);
			if(obj==null||obj.equals("")) return null;
			if(obj instanceof Date ){
				return (Date)obj;//如果传入的就是Date类型的数据
			}
			String dateStr=obj.toString();
			try {
				if(dateStr.indexOf("-")>=0){
					dateStr=dateStr.substring(0, 19);
					value = sdfl.parse(dateStr);
				}else{
					value = sdf.parse(dateStr);
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return value;
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
//	public static List<MsgTreeNode> createMsgTree(String msg) {
//		Message message;
//		MsgTreeNode root=new MsgTreeNode("");
//		try {
//			if(pipeParser==null)  pipeParser=new PipeParser();
//			message = pipeParser.parse(msg);
//			String className=message.getClass().getName();
//			DefaultMutableTreeNode top = new DefaultMutableTreeNode(className);
//			addChildren(message, top,root);
//			return root.getChildren();
//		} catch (HL7Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return root.getChildren();
//		}
//	 }

	   /**
     * Adds the children of the given group under the given tree node.
     */
//	private static void addChildren(Group messParent, MutableTreeNode treeParent,MsgTreeNode parentNode) {
//        String[] childNames = messParent.getNames();
//        int currChild = 0;
//        for (int i = 0; i < childNames.length; i++) {
//            try {
//                Structure[] childReps = messParent.getAll(childNames[i]);
//                for (int j = 0; j < childReps.length; j++) {
//                    DefaultMutableTreeNode newNode = null;
//                    MsgTreeNode newTreeNode=null;
//                    if (childReps[j] instanceof Group) {
//                        String groupName = childReps[j].getClass().getName();
//                        groupName = groupName.substring(groupName.lastIndexOf('.') + 1, groupName.length());
//                        newNode = new DefaultMutableTreeNode(groupName + " (rep " + j + ")");
//                        newTreeNode=new MsgTreeNode(groupName + " (rep " + j + ")");
//                        addChildren((Group)childReps[j], newNode,newTreeNode);
//                    } else if (childReps[j] instanceof Segment) {
//                    	if(pipeParser==null)  pipeParser=new PipeParser();
//                        newNode = new DefaultMutableTreeNode(PipeParser.encode((Segment)childReps[j], encChars));
//                        newTreeNode=new MsgTreeNode(PipeParser.encode((Segment)childReps[j], encChars));
//                        addChildren((Segment)childReps[j], newNode,newTreeNode);
//                    }
//                    treeParent.insert(newNode, currChild++);
//
//                    if(parentNode.getChildren()==null) parentNode.setChildren(new ArrayList<MsgTreeNode>());
//                	parentNode.getChildren().add(newTreeNode);
//                }
//            } catch (HL7Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * Add fields of a segment to the tree ...
     */
//    private static void addChildren(Segment messParent, MutableTreeNode treeParent,MsgTreeNode parentNode) {
//        int n = messParent.numFields();
//        int currChild = 0;
//        for (int i = 1; i <= n; i++) {
//            try {
//                Type[] reps = messParent.getField(i);
//                for (int j = 0; j < reps.length; j++) {
//                	if(pipeParser==null)  pipeParser=new PipeParser();
//                    String field = PipeParser.encode(reps[j], encChars);
//                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("Field " + i + " rep " + j + " (" + getLabel(reps[j]) + "): " + field);
//                    MsgTreeNode newTreeNode=new MsgTreeNode("Field " + i + " rep " + j + " (" + getLabel(reps[j]) + "): " + field);
//                    addChildren(reps[j], newNode,newTreeNode);
//                    treeParent.insert(newNode, currChild++);
//                    if(parentNode.getChildren()==null) parentNode.setChildren(new ArrayList<MsgTreeNode>());
//                    if(newTreeNode!=null) parentNode.getChildren().add(newTreeNode);
//                }
//            } catch (HL7Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * Adds children to the tree.  If the Type is a Varies, the Varies data are
     * added under a new node called "Varies".  If there are extra components,
     * these are added under a new node called "ExtraComponents"
     */
//    private static void addChildren(Type messParent, MutableTreeNode treeParent,MsgTreeNode parentNode) {
//        if (Varies.class.isAssignableFrom(messParent.getClass())) {
//            Type data = ((Varies) messParent).getData();
//            DefaultMutableTreeNode dataNode = new DefaultMutableTreeNode(getLabel(data));
//            MsgTreeNode dataTreeNode=new MsgTreeNode(getLabel(data));
//            treeParent.insert(dataNode, 0);
//            if(parentNode.getChildren()==null) parentNode.setChildren(new ArrayList<MsgTreeNode>());
//            parentNode.getChildren().add(dataTreeNode);
//            addChildren(data, dataNode,dataTreeNode);
//        } else {
//            if (Composite.class.isAssignableFrom(messParent.getClass())) {
//               addChildren((Composite)messParent, treeParent,parentNode);
//            } else if (Primitive.class.isAssignableFrom(messParent.getClass())) {
//                addChildren((Primitive)messParent, treeParent,parentNode);
//            }
//            if (messParent.getExtraComponents().numComponents() > 0) {
//                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("ExtraComponents");
//                MsgTreeNode newTreeNode=new MsgTreeNode("ExtraComponents");
//                treeParent.insert(newNode, treeParent.getChildCount());
//                if(parentNode.getChildren()==null) parentNode.setChildren(new ArrayList<MsgTreeNode>());
//                parentNode.getChildren().add(newTreeNode);
//                for (int i = 0; i < messParent.getExtraComponents().numComponents(); i++) {
//                    DefaultMutableTreeNode variesNode = new DefaultMutableTreeNode("Varies");
//                    MsgTreeNode node = new MsgTreeNode("Varies");
//                    newNode.insert(variesNode, i);
//                    if(newTreeNode.getChildren()==null) newTreeNode.setChildren(new ArrayList<MsgTreeNode>());
//                    newTreeNode.getChildren().add(node);
//                    addChildren(messParent.getExtraComponents().getComponent(i), variesNode,node);
//                }
//            }
//        }
//    }

    /**

/**
 * Adds components of a composite to the tree ...
 */
//private static void addChildren(Composite messParent, MutableTreeNode treeParent,MsgTreeNode parentNode) {
//    Type[] components = messParent.getComponents();
//    for (int i = 0; i < components.length; i++) {
//        DefaultMutableTreeNode newNode;
//        MsgTreeNode newTreeNode;
//        newNode = new DefaultMutableTreeNode(getLabel(components[i]));
//        newTreeNode = new MsgTreeNode(getLabel(components[i]));
//        addChildren(components[i], newNode,newTreeNode);
//        treeParent.insert(newNode, i);
//        if(parentNode.getChildren()==null) parentNode.setChildren(new ArrayList<MsgTreeNode>());
//        parentNode.getChildren().add(newTreeNode);
//    }
//}
    /**
     * Adds single text value to tree as a leaf
     */
//    private static void addChildren(Primitive messParent, MutableTreeNode treeParent,MsgTreeNode parentNode) {
//        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(messParent.getValue());
//        MsgTreeNode newTreeNode=new MsgTreeNode(messParent.getValue());
//        treeParent.insert(newNode, 0);
//        if(parentNode.getChildren()==null) parentNode.setChildren(new ArrayList<MsgTreeNode>());
//        parentNode.getChildren().add(newTreeNode);
//
//    }

    /**
     * Returns the unqualified class name as a label for tree nodes.
     */
//    private static String getLabel(Object o) {
//        String name = o.getClass().getName();
//        return name.substring(name.lastIndexOf('.')+1, name.length());
//    }

    /**
     * 转换婚姻编码
     * @param dtMarry
     * @return
     */
//    public static String getDtMarry(String dtMarry){
//    	//婚姻 S:未婚，M:已婚，D:离婚，R:再婚，A:分居，W:丧偶
//    	//00：未婚， 01：已婚， 02：初婚， 03：再婚， 04：复婚， 05：丧偶， 06：离婚， 99：其他，
//    	if("00".equals(dtMarry)){
//    		return "S";
//    	}else if("01".equals(dtMarry) || "02".equals(dtMarry) || "04".equals(dtMarry)){
//    		return "M";
//    	}else if("03".equals(dtMarry))
//    		return "R";
//    	else if("06".equals(dtMarry))
//    		return "D";
//    	else if("05".equals(dtMarry))
//    		return "W";
//    	return "";
//    }

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
     * 根据婚姻名称或者平台对应编码
     * @param marryName
     * @return
     */
    public static String getDtMarryByName(String marryName){
    	String marryCode = "";
		switch (marryName) {
		case "未婚":
			marryCode = "S";
			break;
		case "已婚":
			marryCode = "M";
			break;
		case "离婚":
			marryCode = "D";
			break;
		case "再婚":
			marryCode = "R";
			break;
		case "分居":
			marryCode = "A";
			break;
		case "丧偶":
			marryCode = "W";
			break;
		default:
			break;
		}
		return marryCode;
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
     * 转换性别
     * @param dtSex
     * @return
     */
    public static String getParseSex(String sexTy){
    	//性别  M: 男,F: 女,O: 其它,U: 不知道,A: 不明确的,N: 不适用
		//NHIS 02:男,03:女,04：未知
		if("M".equals(sexTy)){
			return "02";
		}else if("F".equals(sexTy)){
			return "03";
		}else if("A".equals(sexTy)){
			return "04";
		}else
			return "04";
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
 	 * 截取某两个字符间的字符串，以及按某个字符切割字符串
	 * @param beginStr 截取某两个字符间的字符串(第一个)
	 * @param endStr	截取某两个字符间的字符串(第二个)
	 * @param splitStr  按某个字符切割
	 * @param value
	 * @return
	 */
	public static Map<String,Object> getStrBySplit(String beginStr, String endStr, String splitStr, String value){
		Map<String,Object> map = new HashMap<String,Object>();
		if(value!=null&&!value.isEmpty()){
			if(beginStr!=null&&!beginStr.isEmpty()){
				value = value.substring(value.indexOf(beginStr)+1);
			}
			if(endStr!=null&&!endStr.isEmpty()){
				value = value.substring(0,value.indexOf(endStr));
			}
			if(splitStr!=null&&!splitStr.isEmpty()){
				String[] split = value.split(splitStr);
				for(int i=0;i<split.length;i++){
					map.put(String.valueOf(i+1), split[i]);
				}
			}
			//String substring = value.substring(value.indexOf(beginStr)+1, value.indexOf(endStr));
			//String[] split = substring.split(splitStr);
			return map;
		}else{
			return map;
		}
	}
//	/**
//	 * 获取机构信息
//	 * @return
//	 */
//	public static Map<String,Object> getPkOrg(){
//		String sql = "select * from bd_ou_org where  FLAG_ACTIVE='1' and DEL_FLAG='0' and PK_ORG ^='~'";
//		 Map<String, Object> bdOuOrg = DataBaseHelper.queryForMap(sql);
//		 if(bdOuOrg==null){
//			 bdOuOrg = (Map<String, Object>) new HashMap<String, Object>().put("pkOrg", "~");
//		 }
//		return bdOuOrg;
//	}

	/**
	 * 用于对象（Object）转换Map<K, V>
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> objectToMap(Object obj) throws Exception {
		if (obj == null) {
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


	/**
	 * Bean转map
	 * @param obj
	 * @return
	 */
	public static Object beanToMap(Object obj) {
		String stringBean = JsonUtil.writeValueAsString(obj);
		return JsonUtil.readValue(stringBean, Map.class);
	}

	/**
	 * List Bean 转 List Map
	 * @param list
	 * @return
	 */
	public static List<Map<String, Object>> lisBToLisMap(List<Object> list) {
		List<Map<String, Object>> listMap = new ArrayList<>();
		for(Object lis:list){
			String stringBean = JsonUtil.writeValueAsString(lis);
			listMap.add(JsonUtil.readValue(stringBean, Map.class));
		}

		return listMap;
	}



	/**
	 * 随机生成6位数字
	 */
//	public static String getSixPwd(){
//	    int newNum = (int)((Math.random()*9+1)*100000);
//	    return String.valueOf(newNum);
//	}
	/**
	 * 拼接查询条件方法
	 * @param object
	 * @param key
	 * @return
	 */
//	public static StringBuffer getWhereValue(Object object,String key) {
//		StringBuffer result = new StringBuffer();
//		if(object instanceof HashSet){
//			HashSet<Object> set = (HashSet<Object>) object;
//			Iterator<Object> it = set.iterator();
//			int i = 0;
//			int size = set.size();
//			while(it.hasNext()){
//				result.append("'").append(it.next()).append("'");
//				if(i < (size-1)){
//					result.append(",");
//				}
//				i++;
//			}
//		}else if(object instanceof List){
//			List<Object> list = (List<Object>) object;
//			int size = list.size();
//			for(int i=0;i<size;i++){
//				if(list.get(i) instanceof Map){
//					HashMap<String,Object> map = (HashMap<String,Object>) list.get(i);
//					result.append("'").append(SDMsgUtils.getPropValueStr(map, key)).append("'");
//				}else{
//					result.append("'").append(list.get(i)).append("'");
//				}
//				if(i < (size-1)){
//					result.append(",");
//				}
//			}
//		}else{
//		}
//		return result;
//	}

	/**
	 * 接收消息通用解析方法
	 * @param message
	 * @return
	 */
	public static Map<String,List<Map<String,Object>>> resolueMessage(String message){
		Map<String,List<Map<String,Object>>> resMap=new HashMap<String,List<Map<String,Object>>>();
		String [] splitMsg=message.split("\r");
		for (int i = 0; i < splitMsg.length; i++) {
			List<Map<String,Object>> list=new ArrayList<>();
			String [] splitforClass=splitMsg[i].split("\\|");
			if(resMap.containsKey(splitforClass[0])){
				list=resMap.get(splitforClass[0]);
			}

			Map<String,Object> splitval=new HashMap<String,Object>();
			for (int j = 0; j < splitforClass.length; j++) {
				splitval.put(String.valueOf(j), splitforClass[j]);
			}
			list.add(splitval);
			resMap.put(splitforClass[0], list);
		}
		return resMap;
	}


	/**
	 * 医保挂号类别互转
	 */
	public static String ghTypeTran(String type){
		String typeCode = "";
		if("2".equals(type)||"17".equals(type)){
			//2 主治
			typeCode = "2";
		}else if("3".equals(type)||"18".equals(type)){
			//3 主任
			typeCode = "3";
		}else if("4".equals(type)||"14".equals(type)||"19".equals(type)||"6".equals(type)||"30".equals(type)){
			//4 免收诊金
			typeCode = "4";
		}else if("5".equals(type)||"12".equals(type)||"27".equals(type)||"28".equals(type)){
			//5 专家号
			typeCode = "5";
		}else{
			//1 普通
			typeCode = "1";
		}
		return typeCode;
	}


}
