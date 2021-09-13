package com.zebone.nhis.ma.pub.platform.zb.send;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ADT_A05;
import ca.uhn.hl7v2.model.v24.message.OMG_O19;
import ca.uhn.hl7v2.model.v24.message.OML_O21;
import ca.uhn.hl7v2.model.v24.message.OMP_O09;
import ca.uhn.hl7v2.model.v24.message.QBP_Q21;
import ca.uhn.hl7v2.model.v24.segment.DG1;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.FT1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.OBR;
import ca.uhn.hl7v2.model.v24.segment.OBX;
import ca.uhn.hl7v2.model.v24.segment.ORC;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.QPD;
import ca.uhn.hl7v2.model.v24.segment.RXO;
import ca.uhn.hl7v2.model.v24.segment.RXR;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.zb.comm.Hl7MsgHander;
import com.zebone.nhis.ma.pub.platform.zb.dao.MsgRecEXMapper;
import com.zebone.nhis.ma.pub.platform.zb.service.SysMsgService;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * Adt消息处理
 * @author chengjia
 *
 */
@Service
public class MsgSendCn {
	
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");
	@Resource
	private	Hl7MsgHander msgHander;
	
	@Resource
	private	SysMsgService msgService;
	
	@Resource
	private MsgSendAdt msgSendAdt;
	
	@Resource
	private MsgRecEXMapper msgRecEXMapper;
	
	//数组转字符串的接受变量
	private String zstr;
	public int splitNum = Integer.parseInt(ApplicationUtils.getPropertyValue(
			"msg.split.num", "10"));

	//@Resource
	//private OpApplyService opApplyService;
	/**
	 * 发送Hl7消息
	 * @param triggerEvent
	 * @param pkPv
	 * @param patMap 
	 * @param listMap
	 * @throws HL7Exception
	 */
	public void sendOmlMsg(String triggerEvent,String pkPv,Map<String,Object> listMap,String type){
		Map<String,Object> patMap = new HashMap<>();
		try {
			if(type == "out" && type != null){	//门诊
				patMap = msgService.getPatMapOut(pkPv, patMap);
				String msgId=MsgUtils.getMsgId();
				Message message=createOmlMsg(msgId,triggerEvent, patMap,listMap);
				
				String msg = MsgUtils.getParser().encode(message);
				//发送消息
				msgHander.sendMsg(msgId, msg);
			}else{				
				patMap = msgService.getPatMap(pkPv, patMap);
				String msgId=MsgUtils.getMsgId();
				Message message=createOmlMsg(msgId,triggerEvent, patMap,listMap);
				
				String msg = MsgUtils.getParser().encode(message);
				//发送消息
				msgHander.sendMsg(msgId, msg);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			loger.error("发送消息失败{},{},{}",type,triggerEvent,e.getMessage());
		}
	}
	
	/**
	 * 发送Oml消息
	 * @param msgId
	 * @param triggerEvent
	 * @param list
	 * @param paramMap
	 * @throws HL7Exception
	 */
	private Message createOmlMsg(String msgId,String triggerEvent,Map<String,Object> patMap,Map<String,Object> map){
		Map<String,Object> deptMap=null;
		if(map.containsKey("pkDeptExec")){
			if(CommonUtils.isNotNull(map.get("pkDeptExec"))){
				deptMap=msgService.getDeptInfoByPkDept(map.get("pkDeptExec").toString());
			}
		}
		if(triggerEvent.equals("O21")){
			//检验申请
			OML_O21 oml = new OML_O21();
			try{
			MSH msh = oml.getMSH();
			MsgUtils.createMSHMsgCn(msh, msgId, "OML", "O21");
			
			PID pid = oml.getPATIENT().getPID();
			//MsgUtils.createPIDMsg(pid,patMap);
			PV1 pv1 = oml.getPATIENT().getPATIENT_VISIT().getPV1();
			
			//处理患者基本信息
			MsgUtils.createPIDMsg(pid, patMap);
			//处理患者就诊信息
			MsgUtils.createPV1Msg(pv1, patMap);

			//通用申请
			ORC orcOml;
			OBR obr;
			//BLG blg;
			DG1 dg1;
				
				orcOml = oml.getORDER_GENERAL().getORDER().getORC();
				String euPvType = MsgUtils.getPropValueStr(patMap,"euPvtype");
				if(euPvType != null && !euPvType.equals("3")){
					createOpOrcinfo(orcOml,map);//门诊，急诊
				}else{
					createOrcinfo(orcOml,map);//通用
				}
				obr = oml.getORDER_GENERAL().getORDER().getOBSERVATION_REQUEST().getOBR();
				//项目编号
				obr.getUniversalServiceIdentifier().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrd"));
				//医嘱码
				obr.getUniversalServiceIdentifier().getText().setValue(MsgUtils.getPropValueStr(map, "nameOrd"));
				//描述@todo待确认
				obr.getRelevantClinicalInfo().setValue(MsgUtils.getPropValueStr(map, "bbname"));
				//执行科室//@todo换成编码或名称
				//Map<String,Object> deptMap=msgService.getDeptInfoByPkDept(map.get("pkDeptExec").toString());
				if(deptMap!=null){
					obr.getDiagnosticServSectID().setValue(MsgUtils.getPropValueStr(deptMap, "codeDept")); 
				}else{
					obr.getDiagnosticServSectID().setValue("0306");
				}
				//样本类型 
				//TODO 此字段暂时不需要
				obr.getSpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "bbname"));
				
				dg1=oml.getORDER_GENERAL().getORDER().getOBSERVATION_REQUEST().getDG1();
				//根据患者PKPV查询患者诊断
				Map<String,Object> pvDiagMap=DataBaseHelper.queryForMap("SELECT * FROM PV_DIAG WHERE pk_pv = ? AND DEL_FLAG = '0'", MsgUtils.getPropValueStr(patMap, "pkPv"));
				//组装DG1诊断消息体
				if(null != pvDiagMap && !("").equals(pvDiagMap)){
					//SET ID – DG1  	顺序号
					dg1.getSetIDDG1().setValue(MsgUtils.getPropValueStr(pvDiagMap, "sortNo"));
					//Diagnosis Code - DG1 编码	
					dg1.getDiagnosisCodeDG1().getCe1_Identifier().setValue(MsgUtils.getPropValueStr(pvDiagMap, "dtDiagtype"));
					//Diagnosis Description	诊断描述	名称	
					dg1.getDiagnosisDescription().setValue(MsgUtils.getPropValueStr(pvDiagMap, "descDiag"));
					//Diagnosis Type	诊断类型	01入院 02出院 03 即时 04门诊
					String diagType="";
						String dtDiagType=pvDiagMap.get("dtDiagtype").toString().substring(0, 2);
						if(dtDiagType.equals("00")){//门诊04
							diagType="04";
						}else if(dtDiagType.equals("01")){//入院 01
							diagType="01";
						}else if(dtDiagType.equals("12")){//出院 02
							diagType="02";
						}else if(dtDiagType.equals("即时")){//03即时
							diagType="03";
						}
						dg1.getDiagnosisType().setValue(diagType);
					
					//诊断优先级	1、主要诊断 2、次要诊断
					if(MsgUtils.getPropValueStr(pvDiagMap, "flagMaj").equals("1"))
						dg1.getDiagnosisPriority().setValue("1");
					else
						dg1.getDiagnosisPriority().setValue("2");
					
					if(pvDiagMap.get("pkEmpDiag")!=null){
						//临床诊断医师	IDNumber
						String pkEmpDiag=pvDiagMap.get("pkEmpDiag")==null ? map.get("pkEmpOrd").toString():pvDiagMap.get("pkEmpDiag").toString();
						Map<String, Object> empDiag = msgService.getUserCodeByPkUser(pkEmpDiag);
						if(empDiag != null){
							dg1.getDiagnosingClinician(0).getIDNumber().setValue(MsgUtils.getPropValueStr(empDiag, "code"));
							dg1.getDiagnosingClinician(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(empDiag, "name"));				//证明日期/时间
						}
					}
					dg1.getAttestationDateTime().getTs1_TimeOfAnEvent().setValue(MsgUtils.getPropValueDateSting(map, "dateDiag"));
				}

			}catch (Exception e) {
					loger.error("O21组装消息失败{}",e.getMessage());
				}
			return oml;
		}else if (triggerEvent.equals("O19")) {
			
			//检查申请
			OMG_O19 oml = new OMG_O19();
			try{
				
			MSH msh = oml.getMSH();
			MsgUtils.createMSHMsgCnLbPacs(msh, msgId, "OMG", "O19",deptMap.get("codeDept").toString().substring(0, 4));
			PID pid = oml.getPATIENT().getPID();		
			PV1 pv1 = oml.getPATIENT().getPATIENT_VISIT().getPV1();
			//处理患者基本信息
			MsgUtils.createPIDMsg(pid, patMap);
			//处理患者就诊信息
			MsgUtils.createPV1Msg(pv1, patMap);
			//通用申请
			ORC orcOml;
			OBR obr;
			OBX obx ;
		
				orcOml = oml.getORDER().getORC();
				//申请控制
				String euPvType = MsgUtils.getPropValueStr(patMap,"euPvtype");
				if(euPvType != null && !euPvType.equals("3")){
					createOpOrcinfo(orcOml,map);//门诊
				}else{
					createOrcinfo(orcOml,map);//通用
				}
				
				obr = oml.getORDER().getOBR();
				//项目编号
				obr.getUniversalServiceIdentifier().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrd"));
				//医嘱码
				obr.getUniversalServiceIdentifier().getText().setValue(MsgUtils.getPropValueStr(map, "nameOrd"));
				//医嘱分类
				obr.getUniversalServiceIdentifier().getNameOfCodingSystem().setValue(MsgUtils.getPropValueStr(map, "codeOrdtype"));
				//描述@todo待确认
				obr.getRelevantClinicalInfo().setValue(MsgUtils.getPropValueStr(map, "noteOrd"));
				//执行科室//@todo换成编码或名称
				if(deptMap!=null){
					obr.getDiagnosticServSectID().setValue(MsgUtils.getPropValueStr(deptMap, "codeDept")); 	
				}
				/*样本类型 */ //TODO 此字段暂时不需要
				obr.getSpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "descBody"));
				obr.getReasonForStudy(0).getIdentifier().setValue(MsgUtils.getPropValueStr(map, "purpose"));
				/**
				 * identifier: SIGN:临床症状及体征 HISTORY:既往病史  RIS:检查结果 LIS:检验结果 DRUG:用药情况 REMARK:备注 DIAG:诊断  SITE：部位描述
				 */
				String [] obxStr={"SIGN","DIAG","SITE"};
				for (int j = 0; j < obxStr.length; j++) {
					obx=oml.getORDER().getOBSERVATION(j).getOBX();
					obx.getValueType().setValue("ST");
					obx.getObservationIdentifier().getIdentifier().setValue(obxStr[j]);
					String obxNote="";
					if(obxStr[j].equals("SIGN")) obxNote=MsgUtils.getPropValueStr(map, "noteDise");
					else if(obxStr[j].equals("DIAG"))obxNote=MsgUtils.getPropValueStr(map, "descDiag");
					else if(obxStr[j].equals("SITE"))obxNote=MsgUtils.getPropValueStr(map, "nameOrd");
					obx.getObservationValue(0).parse(obxNote);
				}
		
			}catch (Exception e) {
				loger.error("O19组装消息失败{}",e.getMessage());
			}
			
			return oml;
		}else{
			return null;
		}
	}	 
	/**
	 * 创建ORC通用消息段
	 * @param orcOml
	 * @param map
	 */
	private void createOrcinfo(ORC orcOml,Map<String,Object> map) {
		try {
			//申请控制 NW新医嘱CA取消医嘱
			orcOml.getOrderControl().setValue(MsgUtils.getPropValueStr(map, "control"));
			//申请编码，检查发送医嘱号
			if(("ris").equals(MsgUtils.getPropValueStr(map, "fenlei"))){
				orcOml.getPlacerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsn"));
				}else{
					orcOml.getPlacerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeApply"));
				}
			//医嘱号
			orcOml.getPlacerGroupNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsnParent"));
			//申请单状态
			orcOml.getOrderStatus().setValue("1");//0、项目内不收费；1、项目外单独收费
			//优先权(紧急A)
			//优先级
			String empFlag=MsgUtils.getPropValueStr(map, "flagEmer");
			if(empFlag!=null&&empFlag.equals("1")){
				orcOml.getQuantityTiming(0).getPriority().setValue("A");
			}
			//系统时间
			orcOml.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));
			//输入者@todo看实际情况决定是否患者编码
			orcOml.getEnteredBy(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
			orcOml.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
			//医生编码
			//Map<String, Object> empCode = msgService.getUserCodeByPkUser(MsgUtils.getPropValueStr(map,"pkEmpInput"));
			//orcOml.getOrderingProvider(0).getIDNumber().setValue(MsgUtils.getPropValueStr(empCode, "code"));
			//orcOml.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(empCode, "name"));
			
			orcOml.getOrderingProvider(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
			orcOml.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
			
			//开始时间
			orcOml.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(MsgUtils.getPropValueDateSting(map, "dateStart"));
			//录入科室//@todo换成科室编码或名称
			Map<String,Object> inDeptMap=msgService.getDeptInfoByPkDept(UserContext.getUser().getPkDept());
			if(inDeptMap!=null){
				orcOml.getEnteringOrganization().getIdentifier().setValue(MsgUtils.getPropValueStr(inDeptMap, "codeDept"));
				orcOml.getEnteringOrganization().getText().setValue(MsgUtils.getPropValueStr(inDeptMap, "nameDept"));
				orcOml.getOrderingFacilityName(0).getOrganizationName().setValue(MsgUtils.getPropValueStr(inDeptMap, "codeDept"));
			}

		} catch (DataTypeException e) {
			
		}
	}
	
	
	/**
	 * 创建ORC通用消息段
	 * @param orcOml
	 * @param map
	 */
	private void createOpOrcinfo(ORC orcOml,Map<String,Object> map) {
		try {
			//申请控制 NW新医嘱CA取消医嘱
			orcOml.getOrderControl().setValue(MsgUtils.getPropValueStr(map, "control"));
			//申请编码，检查发送医嘱号
			if(("ris").equals(MsgUtils.getPropValueStr(map, "fenlei"))){
				orcOml.getPlacerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsn"));
				}else{
					orcOml.getPlacerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeApply"));
				}
			//医嘱号
			orcOml.getPlacerGroupNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsnParent"));
			//申请单状态
			orcOml.getOrderStatus().setValue("1");//0、项目内不收费；1、项目外单独收费
			//优先权(紧急A)
			//优先级
			String empFlag=MsgUtils.getPropValueStr(map, "flagEmer");
			if(empFlag!=null&&empFlag.equals("1")){
				orcOml.getQuantityTiming(0).getPriority().setValue("A");
			}
			//系统时间
			orcOml.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));
			//输入者@todo看实际情况决定是否患者编码
			orcOml.getEnteredBy(0).getIDNumber().setValue(MsgUtils.getPropValueStr(map, "codeEmp"));
			orcOml.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(map, "nameEmpOrd"));
			//医生编码
			//Map<String, Object> empCode = msgService.getUserCodeByPkUser(MsgUtils.getPropValueStr(map,"pkEmpInput"));
			//orcOml.getOrderingProvider(0).getIDNumber().setValue(MsgUtils.getPropValueStr(empCode, "code"));
			//orcOml.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(empCode, "name"));
			
			orcOml.getOrderingProvider(0).getIDNumber().setValue(MsgUtils.getPropValueStr(map, "codeEmp"));
			orcOml.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(map, "nameEmpOrd"));
			
			//开始时间
			orcOml.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(MsgUtils.getPropValueDateSting(map, "dateStart"));
			//录入科室//@todo换成科室编码或名称
			Map<String,Object> inDeptMap=msgService.getDeptInfoByPkDept(MsgUtils.getPropValueStr(map, "pkDept"));
			if(inDeptMap!=null){
				orcOml.getEnteringOrganization().getIdentifier().setValue(MsgUtils.getPropValueStr(inDeptMap, "codeDept"));
				orcOml.getEnteringOrganization().getText().setValue(MsgUtils.getPropValueStr(inDeptMap, "nameDept"));
				orcOml.getOrderingFacilityName(0).getOrganizationName().setValue(MsgUtils.getPropValueStr(inDeptMap, "codeDept"));
			}
			
			//患者科室//@todo换成科室编码或名称
			/*Map<String,Object> ordDeptMap=msgService.getDeptInfoByPkDept(map.get("pkDept").toString());
			orcOml.getOrderingFacilityName(0).getOrganizationName().setValue(MsgUtils.getPropValueStr(ordDeptMap, "codeDept"));*/
		} catch (DataTypeException e) {
			
		}
	}
	
	/**
	 * 发送诊断信息
	 * @param triggerEvent
	 * @param paramList
	 */
	public void sendADT_A31(String triggerEvent,List<Map<String,Object>> paramList,String type,String pkPv){
		Map<String,Object> paramMap=new HashMap<>();
		try {
			if(type == "out" && type != null){	//门诊
				paramMap = msgService.getPatMapOut(pkPv, paramMap);
				String msgId=MsgUtils.getMsgId();
				Message message=createA31(triggerEvent, paramMap, paramList,msgId);
				
				String msg = MsgUtils.getParser().encode(message);
				//发送消息
				msgHander.sendMsg(msgId, msg);
			}else{				
				paramMap = msgService.getPatMap(pkPv, paramMap);
				String msgId=MsgUtils.getMsgId();
				Message message=createA31(triggerEvent, paramMap, paramList,msgId);
				
				String msg = MsgUtils.getParser().encode(message);
				//发送消息
				msgHander.sendMsg(msgId, msg);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			loger.error("发送消息失败{},{},{}",type,triggerEvent,e.getMessage());
		}
	}
	
	/*
	 * 发送Hl7消息（OMP^O09）门诊处方
	 * 
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	@SuppressWarnings("unchecked")
	public void sendOMPMsg(String triggerEvent,Map<String,Object> paramMap){
		try{
		if (triggerEvent.equals("O09")) {
			String msgId = "", msg = "";
			// 医嘱信息
			OMP_O09 omp = new OMP_O09();
			MSH msh;
			PID pid;
			PV1 pv1 = null;

			ORC orc;
			RXO rxo;
			RXR rxr;
			FT1 ft1;
			int mod = 0;
			int index=0;
			List<Map<String,Object>> listMap = null;
			if(paramMap.get("cnOrder") != null){
				String stringBean = JsonUtil.writeValueAsString(paramMap.get("cnOrder"));
				listMap=(List<Map<String,Object>>)JsonUtil.readValue(stringBean, List.class);
			}else if(paramMap.get("addingList") != null){
				listMap = (List<Map<String,Object>>)paramMap.get("addingList");
			}
			if (listMap != null && listMap.size() > 0) {
				Map<String, Object> patMap = new  HashMap<>();
				String pkPv=listMap.get(0).get("pkPv").toString();
				patMap = msgService.getPatMapOut(pkPv, patMap);
				
				msh = omp.getMSH();
				msgId = MsgUtils.getMsgId();
				MsgUtils.createMSHMsg(msh, msgId, "OMP", "O09");
				
				pid = omp.getPATIENT().getPID();
				pv1 = omp.getPATIENT().getPATIENT_VISIT().getPV1();
				//处理患者基本信息
				MsgUtils.createPIDMsg(pid, patMap);
				//处理患者就诊信息
				MsgUtils.createPV1Msg(pv1, patMap);
				
			for (int i = 0; i < listMap.size(); i++) { // list.size()
				Map<String, Object> map = listMap.get(i);
				orc = omp.getORDER(index).getORC();
				// 申请控制
				orc.getOrderControl().setValue("NW");
				//cnPres
				//List<Map<String,Object>> presMap=(List<Map<String, Object>>) paramMap.get("addingList");
				//String presNo=presMap.get(0).get("presNo").toString();
				//处方明细编码--医嘱号
				orc.getPlacerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsn"));
				//处方编码
				//orc.getPlacerGroupNumber().getEntityIdentifier().setValue(presNo);pkPres
				//查询处方号
				String presNo = MapUtils.getString(DataBaseHelper.queryForMap("select pres_no from cn_prescription where pk_pres = ?", new Object[]{MsgUtils.getPropValueStr(map, "pkPres")}),"presNo");
				orc.getPlacerGroupNumber().getEntityIdentifier().setValue(presNo);
				// 医嘱状态
				if ("O".equalsIgnoreCase(pv1.getPatientClass().getValue())) {
					orc.getOrderStatus().setValue("1");// 门诊：【0：手工单；1、电脑录入】
				} else {
					//平台：医嘱状态 ：1录入 2提交 3首次执行（确认） 4执行 5停止 6撤销
					//his：医嘱状态 ：0 开立；1 签署；2 核对；3 执行；4 停止；9 作废
					Map<String,Object> statusMap=DataBaseHelper.queryForMap("SELECT eu_status_ord FROM cn_order WHERE pk_cnord = ?", MsgUtils.getPropValueStr(map, "pkCnord"));
					orc.getOrderStatus().setValue(MsgUtils.getEuStatusOrdText(MsgUtils.getPropValueStr(statusMap, "euStatusOrd")));
				}
				//医嘱同组
				orc.getParentOrder().getParentSPlacerOrderNumber().getEntityIdentifier().setValue(!map.get("ordsnParent").equals(0) ? MsgUtils.getPropValueStr(map, "ordsnParent") : MsgUtils.getPropValueStr(map, "ordsn"));
				/*
				 * 剂量(quantity-quantity)&单位(quantity-units-identifier)^
				 * 频率(interval-explicitTimeInterval)^ 优先权-priority(紧急A)^
				 * 天数(occurrenceDuration-identifier)^ 次数(totalOccurences)
				 */
				orc.getQuantityTiming(0).getQuantity().getQuantity().setValue(MsgUtils.getPropValueStr(map, "dosage"));//剂量
				String unitName=qryUnitByPK(MsgUtils.getPropValueStr(map, "pkUnitDos"));
				orc.getQuantityTiming(0).getQuantity().getUnits().getIdentifier().setValue(unitName);//单位
				String cnt=qryItemFreq(MsgUtils.getPropValueStr(map, "codeFreq"));
				
				Map<String,Object> freqMap=DataBaseHelper.queryForMap("select name from bd_term_freq where del_flag = '0' and ltrim(rtrim(pk_org)) ='~'   and code =?", MsgUtils.getPropValueStr(map, "codeFreq"));
				if(freqMap!=null){
					orc.getQuantityTiming(0).getInterval().getRi1_RepeatPattern().setValue(MsgUtils.getPropValueStr(freqMap, "name"));//频率
				}
				if(MsgUtils.getPropValueStr(map, "flagEmer").trim().equals("1"))
					orc.getQuantityTiming(0).getDuration().setValue("A");//优先权
				orc.getQuantityTiming(0).getPriority().setValue(MsgUtils.getPropValueStr(map, "days"));//天数
				orc.getQuantityTiming(0).getCondition().setValue(cnt);//次数

				// timeOfAnEvent系统时间
				orc.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));
				String euPvType = MsgUtils.getPropValueStr(patMap,"euPvtype");
				if(euPvType != null && !euPvType.equals("3")){
					// 输入者 
					orc.getEnteredBy(0).getIDNumber().setValue(MsgUtils.getPropValueStr(map, "codeEmp"));
					orc.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(map, "nameEmpOrd"));
					// 医生编码
					orc.getOrderingProvider(0).getIDNumber().setValue(MsgUtils.getPropValueStr(map, "codeEmp"));
					orc.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(map, "nameEmpOrd"));
				}else{
					// 输入者 
					orc.getEnteredBy(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
					orc.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
					// 医生编码
					orc.getOrderingProvider(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
					orc.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
				}
				
				// 开始时间
				String dateStart=MsgUtils.getPropValueDateSting(map, "dateStart");
				if(dateStart !=null){
					orc.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(dateStart!=null?dateStart:MsgUtils.PropDateSting(new Date()));
				}
				Map<String,Object> deptMap=msgService.getDeptInfoByPkDept(MsgUtils.getPropValueStr(map, "pkDept")) ;
				if(deptMap!=null){
					// 录入科室
					orc.getEnteringOrganization().getIdentifier().setValue(MsgUtils.getPropValueStr(deptMap, "codeDept"));
					orc.getEnteringOrganization().getText().setValue(MsgUtils.getPropValueStr(deptMap, "nameDept"));
				}
				// 患者科室
				Map<String,Object> ipDeptMap=msgService.getDeptInfoByPkDept(UserContext.getUser().getPkDept()) ;
				if(ipDeptMap !=null){
					orc.getOrderingFacilityName(0).getOrganizationName().setValue(MsgUtils.getPropValueStr(ipDeptMap, "codeDept"));
					orc.getOrderingFacilityName(0).getOrganizationNameTypeCode().setValue(MsgUtils.getPropValueStr(ipDeptMap, "nameDept"));
				}
				rxo = omp.getORDER(index).getRXO();
				// 医嘱编码
				rxo.getRequestedGiveCode().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrd"));
				rxo.getRequestedGiveCode().getText().setValue(MsgUtils.getPropValueStr(map, "nameOrd"));
				if(MsgUtils.getPropValueStr(map, "flagDurg").trim().equals("0"))
					rxo.getRequestedGiveCode().getNameOfCodingSystem().setValue("n");
				else{
					rxo.getRequestedGiveCode().getNameOfCodingSystem().setValue("d");
				}
				// 数量
				rxo.getRequestedGiveAmountMinimum().setValue(MsgUtils.getPropValueStr(map, "quan"));
				// 单位
				rxo.getRequestedGiveUnits().getIdentifier().setValue(unitName); 
				// 药房编码*/  
				Map<String,Object> itemMap=msgService.getDeptInfoByPkDept(MsgUtils.getPropValueStr(map, "pkDeptExec")) ;
				itemMap.put("pkPv", MsgUtils.getPropValueStr(map, "pkPv"));
				if(null != itemMap){
					rxo.getDeliverToLocation().getPointOfCare().setValue(MsgUtils.getPropValueStr(itemMap, "codeDept"));
					rxo.getDeliverToLocation().getRoom().setValue(MsgUtils.getPropValueStr(itemMap, "nameDept"));
				}
				// 草药付数
				rxo.getNumberOfRefills().setValue(MsgUtils.getPropValueStr(map, "ords"));
				// 预防治疗标志 皮试
				rxo.getIndication(0).getIdentifier().setValue(MsgUtils.getPropValueStr(map, "euSt"));//TODO

				rxr = omp.getORDER(index).getRXR();
				// 给药方式
				rxr.getRoute().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeSupply"));
				// 草药用法
				String supplyName=qrySupplyCode(MsgUtils.getPropValueStr(map, "codeSupply"));
				rxr.getAdministrationMethod().getIdentifier().setValue(supplyName);

				ft1=omp.getORDER(index).getFT1();
				//单价
				ft1.getTransactionAmountUnit().getPrice().getQuantity().setValue(MsgUtils.getPropValueStr(map, "priceCg"));
				if (i != 0 && i % splitNum == 0) {
					msg = MsgUtils.getParser().encode(omp);
					msgHander.sendMsg(msgId, msg);
					
					msgId = MsgUtils.getMsgId();
					omp = new OMP_O09();

					msh = omp.getMSH();
					MsgUtils.createMSHMsg(msh, msgId, "OMP", "O09");
					
					pid = omp.getPATIENT().getPID();  
					pv1 = omp.getPATIENT().getPATIENT_VISIT().getPV1(); 
					
					msgSendAdt.qryAndSetPID_PV1(pid, pv1, itemMap);

					mod = 0;
					index=0;
				} else {
					mod = i % splitNum;
					index++;
				}
			}

			if (mod != 0 || listMap.size() == 1) {
				msg = MsgUtils.getParser().encode(omp);
				msgHander.sendMsg(msgId, msg);
			}
			
		}
		}
		}catch (Exception e) {
			loger.error("发送Hl7消息（OMP^O09）门诊处方{}",e.getMessage());
		}
	}

	
		/**
		 * 根据医嘱用法名称查询用法编码
		 * @param name
		 * @return
		 */
		private String qrySupplyCode(String name){
			if(name.length()<=0)return null;
			String sql="select name  from bd_supply where code=?";
			return DataBaseHelper.queryForScalar(sql, String.class, name);
		}
		
		
		/**
		 * 根据医嘱频次编码查询频数
		 * @param code
		 * @return
		 */
		private String qryItemFreq(String code){
			if(code.length()<=0)return null;
			String sql="select cnt from BD_TERM_FREQ where CODE=?";
			return DataBaseHelper.queryForScalar(sql, String.class, code);
		}
		/**
		 * 根据单位主键查询单位名称
		 * @param pkUnit
		 * @return
		 */
		private String qryUnitByPK(String pkUnit){
			if(pkUnit.length()<=0)return null;
			String sql="select name from bd_unit where pk_unit=?";
			return DataBaseHelper.queryForScalar(sql, String.class, pkUnit);
		}
	/**
	 * 创建诊断信息
	 * @param triggerEvent
	 * @param paramMap
	 * @param paramList
	 * @return
	 * @throws DataTypeException
	 */
	private Message createA31(String triggerEvent,Map<String,Object> paramMap,List<Map<String,Object>> paramList,String msgId){
		if("A31".equals(triggerEvent)){
			ADT_A05 a31=new ADT_A05();
			try{
			MSH msh=a31.getMSH();
			MsgUtils.createMSHMsg(msh, msgId, "ADT", "A31");
			
			EVN evn=a31.getEVN();
			//[2]系统时间Recorded Date/Time 
			evn.getRecordedDateTime().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));
			//[5]操作者
			evn.getOperatorID(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
			evn.getOperatorID(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
			
			PID pid=a31.getPID();
			PV1 pv1=a31.getPV1();
			//处理患者基本信息
			MsgUtils.createPIDMsg(pid, paramMap);
			//处理患者就诊信息
			MsgUtils.createPV1Msg(pv1, paramMap);

			DG1 dg1=null;
			if(paramList!=null&& paramList.size()>0){
				for (int i = 0; i < paramList.size(); i++) {
					Map<String,Object> map=paramList.get(i);
					dg1=a31.getDG1(i);
					//1 设置ID – DG1 	顺序号
					dg1.getSetIDDG1().setValue(MsgUtils.getPropValueStr(map, "sortNo"));
					//2	 诊断编码方法	
					dg1.getDiagnosisCodingMethod().setValue(MsgUtils.getPropValueStr(map, "NW"));
					//3 诊断代码 – DG1	identifier 编码
					dg1.getDiagnosisCodeDG1().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeIcd"));
					//4	 诊断描述	名称
					dg1.getDiagnosisDescription().setValue(MsgUtils.getPropValueStr(map, "descDiag"));
					//6	 诊断类型	01入院 02出院 03 即时 04门诊
					String type="";
					String dtDiagtype=MsgUtils.getPropValueStr(map, "dtDiagtype");
					if("0100".equals(dtDiagtype))//入院
						type="01";
					else if("0109".equals(dtDiagtype))
						type="02";
					else if("0000".equals(dtDiagtype))
						type="04";
					else
						type="03";
					dg1.getDiagnosisType().setValue(type);
					//15 诊断优先级	1、主要诊断 2、次要诊断
					String flagMaj="1".equals(MsgUtils.getPropValueStr(map, "flagMaj")) ? "1":"2";
					dg1.getDiagnosisPriority().setValue(flagMaj);
					//16临床诊断医师	IDNumber
					dg1.getDiagnosingClinician(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
					dg1.getDiagnosingClinician(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
					//19	证明日期/时间
					dg1.getAttestationDateTime().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));

				}
			}
			}catch (Exception e) {
				loger.error("创建诊断信息{},{}",triggerEvent,e.getMessage());
			}
			return a31;
		}
		return null;
	}

	/**
	 * 发送查询患者状态信息
	 * @param string
	 * @param paramMap
	 */
	public void sendPatientInfo(String string, Map<String, Object> paramMap) {
		try {
			  QBP_Q21 qbp = new QBP_Q21();
			  MSH msh=qbp.getMSH();		
			  String msgId=MsgUtils.getMsgId();
			  MsgUtils.createMSHMsg(msh, msgId, "QBP", "Q21");
			  msh.getReceivingApplication().getNamespaceID().setValue("YTZZJ");
			  QPD qpd = qbp.getQPD();
			  
			  String[] strs = new String[7];
			  strs[0] = "LCP_GetCPState";
			  strs[1] = "0";//纳入路径说明，第三方还没有确定
			  //患者标识   ID号^^^^ID类型
			  String a = (String) paramMap.get("codePi") + "^^^^" + "PatientNO" ;
			  strs[2] = a;
			  strs[3] = (String) paramMap.get("codeDept");
			  strs[4] = (String) paramMap.get("namePi");
			  strs[5] = (String) paramMap.get("codePv");
			  strs[6] = "1";
			  qpd.parse(makeZxxString(strs,zstr));
		      String msg = MsgUtils.getParser().encode(qbp);
			  //发送消息
			  msgHander.sendMsg(msgId, msg);
		} catch (Exception e) {
			
		}
		
	}
	
	/**
	 * 拼接数据
	 * 
	 * @param strs
	 *            包含数据的String数组
	 * @param zstr
	 *            拼接后存放的String
	 * @return 返回Stirng
	 */
	private String makeZxxString(String[] strs, String zstr) {

		zstr = "";
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
}
