package com.zebone.nhis.ma.pub.platform.sd.send;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ADT_A05;
import ca.uhn.hl7v2.model.v24.message.OMG_O19;
import ca.uhn.hl7v2.model.v24.message.OML_O21;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.ma.pub.platform.sd.common.SDHl7MsgHander;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adt消息处理(灵璧复制版本)
 * @author chengjia
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDMsgSendCn {

	public SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");
	@Resource
	private	SDHl7MsgHander sDHl7MsgHander;
	@Resource
	private SDMsgSendAdt sDMsgSendAdt;
	@Resource
	private SDQueryUtils sDQueryUtils;
	@Resource
	private SDMsgSendOpCn sDMsgSendOpCn;

	/**
	 * 发送Hl7消息
	 * @param triggerEvent
	 * @param pkPv
	 * @param patMap
	 * @param listMap
	 * @throws HL7Exception
	 */
	public void sendOmlMsg(String triggerEvent,String pkPv,Map<String,Object> patMap,Map<String,Object> listMap,String type) throws HL7Exception{
		try {
			String msgId=SDMsgUtils.getMsgId();
			Map<String,Object> map;
			Message message;
			if("out".equals(type)){
				//门诊
				map = sDQueryUtils.getPatMapOut(pkPv, patMap);
				message = sDMsgSendOpCn.createOmlMsgOut(msgId,triggerEvent, map,listMap);
			}else{
				map = sDQueryUtils.getPatMap(pkPv, patMap);
				message = createOmlMsg(msgId,triggerEvent, map,listMap);
			}
			String msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);

		} catch (Exception e) {
			e.printStackTrace();
			loger.info("SDMsgSendCn.sendOmlMsg 发送消息失败!"+e);
		}
	}

	/**
	 * 发送Oml消息(住院消息)
	 * @param msgId
	 * @param triggerEvent
	 * @param map
	 * @param patMap
	 * @throws HL7Exception
	 */
	private Message createOmlMsg(String msgId,String triggerEvent,Map<String,Object> patMap,Map<String,Object> map){
		Map<String,Object> deptMap=sDQueryUtils.queryDeptByPk(SDMsgUtils.getPropValueStr(map, "pkDeptExec"));
		Map<String, Object> bdOrdMap = DataBaseHelper.queryForMap("select bd.dt_ordcate,bd.code,bd.NAME from bd_ord bd inner join CN_ORDER ord on bd.PK_ORD=ord.PK_ORD where ORDSN=?", SDMsgUtils.getPropValueStr(map, "ordsn"));
		if("O21".equals(triggerEvent)){
			//检验申请
			OML_O21 oml = new OML_O21();
			try{
				MSH msh = oml.getMSH();
				SDMsgUtils.createMSHMsgCn(msh, msgId, "OML", "O21");

				PID pid = oml.getPATIENT().getPID();

				PV1 pv1 = oml.getPATIENT().getPATIENT_VISIT().getPV1();
				Map<String,Object> param= new HashMap<>(4);
				param.put("pkPv", patMap.get("pkPv"));
				sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, param);

				//通用申请
				ORC orcOml;
				OBR obr;
				//BLG blg;
				DG1 dg1;
				orcOml = oml.getORDER_GENERAL().getORDER().getORC();
				String euPvType = SDMsgUtils.getPropValueStr(patMap,"euPvtype");
				if("3".equals(euPvType)){
					//住院
					createOrcinfo(orcOml,map);
				}else{
					//创建ORC通用消息段 (门急诊使用)
					createOpOrcinfo(orcOml,map);
				}
				obr = oml.getORDER_GENERAL().getORDER().getOBSERVATION_REQUEST().getOBR();
				//项目编号  codeApply
				obr.getUniversalServiceIdentifier().getIdentifier().setValue(SDMsgUtils.getPropValueStr(bdOrdMap, "code"));
				//医嘱码
				obr.getUniversalServiceIdentifier().getText().setValue(SDMsgUtils.getPropValueStr(bdOrdMap, "name"));
				//描述@todo待确认
				obr.getRelevantClinicalInfo().setValue(SDMsgUtils.getPropValueStr(map, "bbname"));
				//执行科室//@todo换成编码或名称
				//Map<String,Object> deptMap=msgService.getDeptInfoByPkDept(map.get("pkDeptExec").toString());
				obr.getDiagnosticServSectID().setValue(SDMsgUtils.getPropValueStr(deptMap, "codeDept"));
				//OBR -13  医嘱描述
				obr.getObr13_RelevantClinicalInfo().setValue(SDMsgUtils.getPropValueStr(map, "noteOrd"));
				//obr-15 样本类型
				obr.getSpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "dtSamptype"));
				obr.getSpecimenSource().getSpecimenSourceNameOrCode().getCe2_Text().setValue(SDMsgUtils.getPropValueStr(map, "nameSamp"));

				dg1=oml.getORDER_GENERAL().getORDER().getOBSERVATION_REQUEST().getDG1();
				//根据患者PKPV查询患者诊断
				String sql = "SELECT sort_no,dt_diagtype,name_diag,desc_diag,flag_maj," +
						" to_char(date_diag,'YYYYMMDDHH24MiSS') date_diag,NAME_EMP_DIAG NAME_EMP," +
						" (select CODE_EMP from BD_OU_EMPLOYEE where pk_emp_diag=PK_EMP) CODE_EMP " +
						" FROM PV_DIAG WHERE pk_pv = ? AND DEL_FLAG = '0' and FLAG_MAJ='1' ";
				Map<String,Object> pvDiagMap=DataBaseHelper.queryForMap(sql, SDMsgUtils.getPropValueStr(patMap, "pkPv"));
				//组装DG1诊断消息体
				if(null != pvDiagMap && pvDiagMap.size()>0){
					//SET ID – DG1  	顺序号
					dg1.getSetIDDG1().setValue(SDMsgUtils.getPropValueStr(pvDiagMap, "sortNo"));
					//Diagnosis Code - DG1 编码
					dg1.getDiagnosisCodeDG1().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(pvDiagMap, "dtDiagtype"));
					//Diagnosis Description	诊断描述	名称
					dg1.getDiagnosisDescription().setValue(SDMsgUtils.getPropValueStr(pvDiagMap, "nameDiag"));
					//Diagnosis Type	诊断类型	01入院 02出院 03 即时 04门诊
					String diagType="";
					String dtDiagType = SDMsgUtils.getPropValueStr(pvDiagMap, "dtDiagtype");
					if(dtDiagType.startsWith("00")){
						//门诊04
						diagType="04";
					}else if(dtDiagType.startsWith("01")){
						//入院 01
						diagType="01";
					}else if(dtDiagType.startsWith("12")){
						//出院 02
						diagType="02";
					}else if("即时".equals(dtDiagType)){
						//03即时
						diagType="03";
					}
					dg1.getDiagnosisType().setValue(diagType);

					//诊断优先级	1、主要诊断 2、次要诊断
					if("1".equals(SDMsgUtils.getPropValueStr(pvDiagMap, "flagMaj"))) {
						dg1.getDiagnosisPriority().setValue("1");
					} else {
						dg1.getDiagnosisPriority().setValue("2");
					}

					dg1.getDiagnosingClinician(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(pvDiagMap, "codeEmp"));
					dg1.getDiagnosingClinician(0).getFamilyName().getFn1_Surname().setValue(SDMsgUtils.getPropValueStr(pvDiagMap, "nameEmp"));
					dg1.getAttestationDateTime().getTs1_TimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(pvDiagMap, "dateDiag"));
				}

			}catch (Exception e) {
				e.printStackTrace();
				loger.info("O21组装消息失败"+e);
			}
			return oml;
		}else if ("O19".equals(triggerEvent)) {

			//检查申请
			OMG_O19 oml = new OMG_O19();
			try{
				MSH msh = oml.getMSH();
				SDMsgUtils.createMSHMsgCnLbPacs(msh, msgId, "OMG", "O19",SDMsgUtils.getPropValueStr(deptMap, "codeDept"));
				PID pid = oml.getPATIENT().getPID();
				PV1 pv1 = oml.getPATIENT().getPATIENT_VISIT().getPV1();
				Map<String,Object> param = new HashMap<>(16);
				param.put("pkPv", patMap.get("pkPv"));
				sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, param);
				//创建ORC消息
				ORC orcOml = oml.getORDER().getORC();
				String euPvType = SDMsgUtils.getPropValueStr(patMap,"euPvtype");
				if("3".equals(euPvType)){
					//通用
					map.put("fenlei", "ris");
					createOrcinfo(orcOml,map);
				}else{
					//门诊
					createOpOrcinfo(orcOml,map);
				}
				//创建OBR 消息
				OBR obr = oml.getORDER().getOBR();
				//项目编号
				obr.getUniversalServiceIdentifier().getIdentifier().setValue(SDMsgUtils.getPropValueStr(bdOrdMap, "code"));
				//医嘱码
				obr.getUniversalServiceIdentifier().getText().setValue(SDMsgUtils.getPropValueStr(bdOrdMap, "name"));
				//医嘱分类
				//OBR-4.3：申请单类型：cn_order.code_ord对应的bd_ord.dt_ordcate
				//obr.getUniversalServiceIdentifier().getNameOfCodingSystem().setValue(SDMsgUtils.getPropValueStr(map, "codeOrdtype"));
				obr.getUniversalServiceIdentifier().getNameOfCodingSystem().setValue(SDMsgUtils.getPropValueStr(bdOrdMap, "dtOrdcate"));
				//描述@todo待确认
				obr.getRelevantClinicalInfo().setValue(SDMsgUtils.getPropValueStr(map, "noteOrd"));
				//执行科室//@todo换成编码或名称
				String dept = SDMsgUtils.getPropValueStr(deptMap, "codeDept")+"#"+SDMsgUtils.getPropValueStr(deptMap, "nameDept");
				obr.getDiagnosticServSectID().setValue(dept);
				/*样本类型 */
				obr.getSpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "descBody"));
				obr.getReasonForStudy(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "purpose"));

				//创建OBX消息 identifier: SIGN:临床症状及体征 HISTORY:既往病史  RIS:检查结果 LIS:检验结果 DRUG:用药情况 REMARK:备注 DIAG:诊断  SITE：部位描述
				String [] obxStr={"SIGN","DIAG","SITE"};
				for (int j = 0; j < obxStr.length; j++) {
					OBX obx =oml.getORDER().getOBSERVATION(j).getOBX();
					obx.getValueType().setValue("ST");
					obx.getObservationIdentifier().getIdentifier().setValue(obxStr[j]);
					String obxNote="";
					switch (obxStr[j]) {
						case "SIGN":
							obxNote = SDMsgUtils.getPropValueStr(map, "noteDise");
							break;
						case "DIAG":
							obxNote = SDMsgUtils.getPropValueStr(map, "nameDiag");
							break;
						case "SITE":
							obxNote = SDMsgUtils.getPropValueStr(map, "nameOrd");
							break;
						default:break;
					}
					obx.getObservationValue(0).parse(obxNote);
				}

			}catch (Exception e) {
				e.printStackTrace();
				loger.info("O19组装消息失败"+e);
			}
			return oml;
		}else{
			return null;
		}
	}

	/**
	 * 创建ORC通用消息段（通用）
	 * @param orcOml
	 * @param map
	 */
	private void createOrcinfo(ORC orcOml,Map<String,Object> map) {
		try {
			//申请控制    NW新增，RU修改，CA撤销
			String control = SDMsgUtils.getPropValueStr(map, "control");
			if("OC".equals(control)){
				control = "CA";
			}
			orcOml.getOrderControl().setValue(control);
			//orc-2  申请编码 深圳都发申请单号H医嘱号
			String code = SDMsgUtils.getPropValueStr(map, "codeApply")+"H"+SDMsgUtils.getPropValueStr(map, "ordsn");
			if(("ris").equals(SDMsgUtils.getPropValueStr(map, "fenlei"))){
				orcOml.getPlacerOrderNumber().getEntityIdentifier().setValue(code);
			}else{
				orcOml.getPlacerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "codeApply"));
			}
			//医嘱号 ORC4.1
			orcOml.getPlacerGroupNumber().getEi1_EntityIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "ordsn"));
			//申请单状态 //0、项目内不收费；1、项目外单独收费
			orcOml.getOrderStatus().setValue("1");
			//优先权(紧急A)

			//优先级
			String empFlag=SDMsgUtils.getPropValueStr(map, "flagEmer");
			if("1".equals(empFlag)){
				empFlag = "A";
			}
			orcOml.getQuantityTiming(0).getPriority().setValue(empFlag);
			//系统时间
			orcOml.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			//输入者@todo看实际情况决定是否患者编码
			orcOml.getEnteredBy(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
			orcOml.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
			//医生编码
			orcOml.getOrderingProvider(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
			orcOml.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());

			//开始时间
			orcOml.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(sdf.format(SDMsgUtils.getPropValueDate(map, "dateStart")));
			//录入科室//@todo换成科室编码或名称
			Map<String,Object> inDeptMap=sDQueryUtils.queryDeptByPk(UserContext.getUser().getPkDept());
			if(inDeptMap!=null){
				orcOml.getEnteringOrganization().getIdentifier().setValue(SDMsgUtils.getPropValueStr(inDeptMap, "codeDept"));
				orcOml.getEnteringOrganization().getText().setValue(SDMsgUtils.getPropValueStr(inDeptMap, "nameDept"));
				orcOml.getOrderingFacilityName(0).getOrganizationName().setValue(SDMsgUtils.getPropValueStr(inDeptMap, "codeDept"));
			}

		} catch (DataTypeException e) {
			e.printStackTrace();
			loger.info("方法createOrcinfo：创建ORC消息失败！"+e.getMessage());
		}
	}


	/**
	 * 创建ORC通用消息段 (门急诊使用)
	 * @param orcOml
	 * @param map
	 */
	private void createOpOrcinfo(ORC orcOml,Map<String,Object> map) {
		try {
			//申请控制 NW新医嘱CA取消医嘱
			orcOml.getOrderControl().setValue(SDMsgUtils.getPropValueStr(map, "control"));
			//申请编码，检查发送医嘱号
			if(("ris").equals(SDMsgUtils.getPropValueStr(map, "fenlei"))){
				orcOml.getPlacerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "ordsn"));
			}else{
				orcOml.getPlacerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "codeApply"));
			}
			//医嘱号 4
			orcOml.getPlacerGroupNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "ordsnParent"));
			//申请单状态 //0、项目内不收费；1、项目外单独收费
			orcOml.getOrderStatus().setValue("1");
			//优先权(紧急A)
			//优先级
			String empFlag=SDMsgUtils.getPropValueStr(map, "flagEmer");
			if("1".equals(empFlag)){
				orcOml.getQuantityTiming(0).getPriority().setValue("A");
			}
			//系统时间
			orcOml.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			//输入者@todo看实际情况决定是否患者编码
			orcOml.getEnteredBy(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(map, "codeEmp"));
			orcOml.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(SDMsgUtils.getPropValueStr(map, "nameEmpOrd"));
			//医生编码
			orcOml.getOrderingProvider(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(map, "codeEmp"));
			orcOml.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(SDMsgUtils.getPropValueStr(map, "nameEmpOrd"));

			//开始时间
			orcOml.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(sdf.format(SDMsgUtils.getPropValueDate(map, "dateStart")));
			//录入科室//@todo换成科室编码或名称
			Map<String,Object> inDeptMap=sDQueryUtils.queryDeptByPk(SDMsgUtils.getPropValueStr(map, "pkDept"));
			if(inDeptMap!=null){
				orcOml.getEnteringOrganization().getIdentifier().setValue(SDMsgUtils.getPropValueStr(inDeptMap, "codeDept"));
				orcOml.getEnteringOrganization().getText().setValue(SDMsgUtils.getPropValueStr(inDeptMap, "nameDept"));
				orcOml.getOrderingFacilityName(0).getOrganizationName().setValue(SDMsgUtils.getPropValueStr(inDeptMap, "codeDept"));
			}

		} catch (DataTypeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送诊断信息(新方法)
	 * @param triggerEvent
	 * @param paramMap
	 */
	public void sendADT_A31(String triggerEvent,Map<String,Object> paramMap){
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = createA31(msgId,triggerEvent, paramMap);
			String msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (HL7Exception e) {
			e.printStackTrace();
			loger.info(" sendADT_A31 诊断消息错误:{}",e.getMessage());
		}
	}
	/**
	 * 创建诊断信息 (新方法)
	 * @param triggerEvent
	 * @param paramMap
	 * @param msgId
	 * @return
	 * @throws DataTypeException
	 */
	private Message createA31(String msgId,String triggerEvent,Map<String,Object> paramMap) throws DataTypeException{
		ADT_A05 a31=new ADT_A05();
		MSH msh=a31.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "ADT", "A31");
		EVN evn=a31.getEVN();
		//[2]系统时间Recorded Date/Time
		evn.getRecordedDateTime().getTimeOfAnEvent().setValue(sdf.format(new Date()));
		//[5]操作者
		evn.getOperatorID(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
		evn.getOperatorID(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
		PID pid=a31.getPID();
		PV1 pv1=a31.getPV1();
		sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, paramMap);
		//删除数据长度
		int oldSize = 0;
		//删除的集合
		List<Map<String,Object>> oldList = (List<Map<String,Object>>)paramMap.get("paramList");
		if(oldList!=null && oldList.size()>0){
			oldSize = oldList.size();
			//删除原有诊断消息
			paramMap.put("control", "CA");
			for(int i=0;i<oldSize;i++){
				createA31DG1(a31.getDG1(i),paramMap,oldList.get(i));
			}
		}
		//新增的集合
		List<Map<String,Object>> newList = (List<Map<String,Object>>)paramMap.get("pvDiagList");
		//判断门诊或者住院
		newList=newList==null?(List<Map<String,Object>>)paramMap.get("source"):newList;
		if(newList!=null && newList.size()>0){
			int newSize = newList.size();
			//新诊断消息
			paramMap.put("control", "NW");
			for(int i=0;i<newSize;i++){
				createA31DG1(a31.getDG1(oldSize+i),paramMap,newList.get(i));
			}
		}
		return a31;
	}

	/**
	 * 诊断消息；创建DG1消息
	 * @param dg1
	 * @param paramMap
	 * @param map
	 * @throws DataTypeException
	 */
	private void createA31DG1(DG1 dg1,Map<String,Object> paramMap,Map<String,Object> map) throws DataTypeException{
		//1 设置ID – DG1 	顺序号 诊断编码
		String dgCode = SDMsgUtils.getPropValueStr(paramMap, "codePv")+SDMsgUtils.getPropValueStr(map, "sortNo");
		dg1.getSetIDDG1().setValue(dgCode);
		//2	 诊断编码方法
		dg1.getDiagnosisCodingMethod().setValue(SDMsgUtils.getPropValueStr(paramMap, "control"));
		//3 诊断代码 – DG1	identifier 编码
		dg1.getDiagnosisCodeDG1().getIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "codeIcd"));
		//4	 诊断描述	名称
		dg1.getDiagnosisDescription().setValue(SDMsgUtils.getPropValueStr(map, "nameDiag"));
		//6	 诊断类型	01入院 02出院 03 即时 04门诊
		//8 父诊断编号
		String pkFather = SDMsgUtils.getPropValueStr(map, "pkFather");
		if(pkFather!=null && !"".equals(pkFather)){
			Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select sort_no from pv_diag d where d.pk_pvdiag=?", pkFather);
			String father = SDMsgUtils.getPropValueStr(paramMap, "codePv")+SDMsgUtils.getPropValueStr(queryForMap, "sortNo");
			dg1.getDiagnosticRelatedGroup().getIdentifier().setValue(father);
		}
		Map<String, Object> queryDiagtype = sDQueryUtils.queryDiagtype(SDMsgUtils.getPropValueStr(map, "dtDiagtype"));
		dg1.getDiagnosisType().setValue(SDMsgUtils.getPropValueStr(queryDiagtype, "oldId"));
		//15 诊断优先级	1、主要诊断 2、次要诊断
		String flagMaj="1".equals(SDMsgUtils.getPropValueStr(map, "flagMaj")) ? "1":"2";
		dg1.getDiagnosisPriority().setValue(flagMaj);
		//16临床诊断医师	IDNumber
		dg1.getDiagnosingClinician(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(sDQueryUtils.getUserCodeByPkUser(SDMsgUtils.getPropValueStr(map,"pkEmpDiag")),"code"));
		dg1.getDiagnosingClinician(0).getFamilyName().getFn1_Surname().setValue(SDMsgUtils.getPropValueStr(map,"nameEmpDiag"));
		//17入院病情
		dg1.getDg117_DiagnosisClassification().setValue(SDMsgUtils.getPropValueStr(map, "euAdmcon"));
		//19 证明日期/时间
		if(map.get("dateDiag") instanceof Date){
			dg1.getAttestationDateTime().getTimeOfAnEvent().setValue(sdf.format(map.get("dateDiag")));
		}else{
			dg1.getAttestationDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(map, "dateDiag"));
		}
	}


}
