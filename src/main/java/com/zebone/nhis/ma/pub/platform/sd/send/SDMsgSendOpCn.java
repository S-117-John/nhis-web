package com.zebone.nhis.ma.pub.platform.sd.send;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.OMG_O19;
import ca.uhn.hl7v2.model.v24.message.OML_O21;
import ca.uhn.hl7v2.model.v24.message.OMP_O09;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.sd.common.SDHl7MsgHander;
import com.zebone.nhis.ma.pub.platform.sd.create.CreateOpMsg;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("unchecked")
public class SDMsgSendOpCn {

	public int splitNum = Integer.parseInt(ApplicationUtils.getPropertyValue("msg.split.num", "10"));

	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");
	@Resource
	private	SDHl7MsgHander sDHl7MsgHander;
	@Resource
	private SDQueryUtils sDQueryUtils;
	@Resource
	private CreateOpMsg createOpMsg;
	@Resource
	private SDMsgSendCn sDMsgSendCn;



	//****************************门诊住院分割线*******************************************************************


	/**
	 * 发送门诊检查检验消息
	 * @param paramMap
	 * @throws Exception
	 */
	public  void sendCnOpAppMsg(Map<String, Object> paramMap) throws Exception{
		List<CnOrder> addOrdList =  (List<CnOrder>)paramMap.get("addOrdList");
		List<CnOrder> updateOrdList =  (List<CnOrder>)paramMap.get("updateOrdList");
		Map<String, Object> mapRis = new HashMap<>();
		mapRis.put("type","ris");

		List<Map<String,Object>> addMapList;
		StringBuilder addPkCnords= new StringBuilder();
		//发送检查检验【申请】医嘱消息
		if(addOrdList!=null&&addOrdList.size()>0){
			String sql="SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,co.eu_Always,app.NOTE_DISE,apply.dt_samptype,def.name name_samp, "
					+" dt.PK_SETTLE,co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.pk_unit_cg,co.quan_cg,pre.pres_no,co.code_freq,co.flag_emer, "
					+" co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,bd1.code_emp,co.name_emp_ord,bd2.code_emp code_emp_input,co.name_emp_input,co.note_ord "
					+" FROM cn_order co left join CN_RIS_APPLY app on co.pk_cnord=app.pk_cnord left join cn_lab_apply apply on apply.pk_cnord=co.pk_cnord left join BD_DEFDOC def on def.code=apply.dt_samptype and def.CODE_DEFDOCLIST='030200'"
					+" LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi INNER JOIN BL_OP_DT dt ON dt.pk_cnord = co.pk_cnord LEFT JOIN BD_OU_EMPLOYEE bd1 on BD1.pk_emp=co.pk_emp_ord "
					+" LEFT JOIN BD_OU_EMPLOYEE bd2 on BD2.pk_emp = co.pk_emp_input left join cn_prescription pre on pre.pk_pres = co.pk_pres where "
					+ " co.pk_cnord  in ( " ;
			mapRis.put("Control", "NW");
			int pkCnOrdSize = addOrdList.size();
			for (int i = 0; i < pkCnOrdSize; i++) {
				if(i==pkCnOrdSize-1){
					if(StringUtils.isNotBlank(addOrdList.get(i).getPkCnord())){
						addPkCnords.append("'").append(addOrdList.get(i).getPkCnord()).append("')");
					}
				}else{
					if(StringUtils.isNotBlank(addOrdList.get(i).getPkCnord())){
						addPkCnords.append("'").append(addOrdList.get(i).getPkCnord()).append("',");
					}
				}
			}
			addMapList=DataBaseHelper.queryForList(sql+addPkCnords);
			mapRis.put("pkPv", addOrdList.get(0).getPkPv());
			sendOpApplyMsgOk(addMapList,mapRis);
			mapRis.clear();
		}


		//发送检查检验【修改】申请消息
		StringBuilder upPkCnords= new StringBuilder();
		List<Map<String,Object>> upMapList;
		if(updateOrdList!=null&&updateOrdList.size()>0){
			String sql="SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,co.eu_Always,app.NOTE_DISE,apply.dt_samptype,def.name name_samp, "
					+" dt.PK_SETTLE,co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.pk_unit_cg,co.quan_cg,pre.pres_no,co.code_freq,co.flag_emer, "
					+" co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,bd1.code_emp,co.name_emp_ord,bd2.code_emp code_emp_input,co.name_emp_input "
					+" FROM cn_order co left join CN_RIS_APPLY app on co.pk_cnord=app.pk_cnord left join cn_lab_apply apply on apply.pk_cnord=co.pk_cnord"
					+" LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi INNER JOIN BL_OP_DT dt ON dt.pk_cnord = co.pk_cnord left join BD_DEFDOC def on def.code=apply.dt_samptype and def.CODE_DEFDOCLIST='030200'"
					+" LEFT JOIN BD_OU_EMPLOYEE bd1 on BD1.pk_emp=co.pk_emp_ord LEFT JOIN BD_OU_EMPLOYEE bd2 on BD2.pk_emp = co.pk_emp_input left join cn_prescription pre on pre.pk_pres = co.pk_pres where  "
					+ " co.pk_cnord  in ( " ;
			mapRis.put("Control", "RU");
			int pkCnOrdSize = updateOrdList.size();
			for (int i = 0; i < pkCnOrdSize; i++) {
				if(i==pkCnOrdSize-1){
					if(StringUtils.isNotBlank(updateOrdList.get(i).getPkCnord())){
						upPkCnords.append("'").append(updateOrdList.get(i).getPkCnord()).append("')");
					}
				}else{
					if(StringUtils.isNotBlank(updateOrdList.get(i).getPkCnord())){
						upPkCnords.append("'").append(updateOrdList.get(i).getPkCnord()).append("',");
					}
				}
			}
			upMapList=DataBaseHelper.queryForList(sql+upPkCnords);
			mapRis.put("pkPv", updateOrdList.get(0).getPkPv());
			sendOpApplyMsgOk(upMapList,mapRis);
			mapRis.clear();
		}

		//发送检查检验【取消】申请消息
		List<Map<String,Object>>  delMapList = (List<Map<String,Object>>)paramMap.get("delMapList");
		if(delMapList!=null&&delMapList.size()>0){
			mapRis.put("pkPv", SDMsgUtils.getPropValueStr(delMapList.get(0), "pkPv"));
			mapRis.put("Control", "CA");
			sendOpApplyMsgOk(delMapList,mapRis);
			mapRis.clear();
		}



	}

	/**
	 * 发送检查检验【新增、修改、删除】申请医嘱消息
	 * @param mapList
	 * @throws Exception
	 */
	public void sendOpApplyMsgOk(List<Map<String,Object>> mapList, Map<String, Object> mapRis) throws Exception{
		String control = SDMsgUtils.getPropValueStr(mapRis, "Control");
		for (Map<String, Object> map : mapList) {
			if("02".equals(map.get("codeOrdtype").toString().substring(0,2))){
				map.put("control", control);
				//map.put("fenlei", "ris");
				sDMsgSendCn.sendOmlMsg("O19", map.get("pkPv").toString(), null, map,"out");
			}
			if("03".equals(map.get("codeOrdtype").toString().substring(0, 2))){
				map.put("control", control);
				sDMsgSendCn.sendOmlMsg("O21",  map.get("pkPv").toString(), null, map, "out");//门诊检验申请
			}
		}
		Map<String,Object> cnOrdListMap = new HashMap<>();
		cnOrdListMap.put("addingList", mapList);
		cnOrdListMap.put("control", control);
		sendOMPMsg("O09", cnOrdListMap);
	}








	/**
	 * 发送Oml消息(门诊消息)
	 * @param msgId
	 * @param triggerEvent
	 * @param map
	 * @param patMap
	 * @throws HL7Exception
	 */
	public Message createOmlMsgOut(String msgId,String triggerEvent,Map<String,Object> patMap,Map<String,Object> map){
		Map<String,Object> deptMap=sDQueryUtils.queryDeptByPk(SDMsgUtils.getPropValueStr(map, "pkDeptExec"));
		Map<String, Object> bdOrdMap = DataBaseHelper.queryForMap("select bd.dt_ordcate,bd.code,bd.NAME from bd_ord bd inner join CN_ORDER ord on bd.PK_ORD=ord.PK_ORD where ORDSN=?", SDMsgUtils.getPropValueStr(map, "ordsn"));
		if("O21".equals(triggerEvent)){
			//检验申请
			OML_O21 oml = new OML_O21();
			try{
				MSH msh = oml.getMSH();
				PID pid = oml.getPATIENT().getPID();
				PV1 pv1 = oml.getPATIENT().getPATIENT_VISIT().getPV1();
				ORC orc = oml.getORDER_GENERAL().getORDER().getORC();
				Map<String, Object> pMap=createOpMsg.qrySdOpPID_PV1(map);
				map.put("msgid",msgId);
				map.put("msgtype", "OML");
				map.put("triggerevent", "O21");
				createOpMsg.createMSHMsg(msh, map);
				createOpMsg.createPIDMsg(pid, pMap);
				createOpMsg.createPV1Msg(pv1, pMap);
				createOpMsg.createORCMsg(orc, map);
				OBR obr;
				DG1 dg1;
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
				//obr-15 样本类型
				//TODO 此字段暂时不需要
				obr.getSpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "dtSamptype"));
				obr.getSpecimenSource().getSpecimenSourceNameOrCode().getCe2_Text().setValue(SDMsgUtils.getPropValueStr(map, "nameSamp"));

				dg1=oml.getORDER_GENERAL().getORDER().getOBSERVATION_REQUEST().getDG1();
				//根据患者PKPV查询患者诊断
				Map<String,Object> pvDiagMap=DataBaseHelper.queryForMap("SELECT * FROM PV_DIAG WHERE pk_pv = ? AND DEL_FLAG = '0'", SDMsgUtils.getPropValueStr(patMap, "pkPv"));
				//组装DG1诊断消息体
				if(null != pvDiagMap && pvDiagMap.size()>0){
					//SET ID – DG1  	顺序号
					dg1.getSetIDDG1().setValue(SDMsgUtils.getPropValueStr(pvDiagMap, "sortNo"));
					//Diagnosis Code - DG1 编码
					dg1.getDiagnosisCodeDG1().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(pvDiagMap, "dtDiagtype"));
					//Diagnosis Description	诊断描述	名称
					dg1.getDiagnosisDescription().setValue(SDMsgUtils.getPropValueStr(pvDiagMap, "descDiag"));
					//Diagnosis Type	诊断类型	01入院 02出院 03 即时 04门诊
					String diagType="";
					String dtDiagType=pvDiagMap.get("dtDiagtype").toString().substring(0, 2);
					switch (dtDiagType) {
						case "00":
							//门诊04
							diagType = "04";break;
						case "01":
							//入院 01
							diagType = "01";break;
						case "12":
							//出院 02
							diagType = "02";break;
						case "即时":
							//03即时
							diagType = "03";break;
						default:break;
					}
					dg1.getDiagnosisType().setValue(diagType);

					//诊断优先级	1、主要诊断 2、次要诊断
					if("1".equals(SDMsgUtils.getPropValueStr(pvDiagMap, "flagMaj"))) {
						dg1.getDiagnosisPriority().setValue("1");
					} else {
						dg1.getDiagnosisPriority().setValue("2");
					}
					if(pvDiagMap.get("pkEmpDiag")!=null){
						//临床诊断医师	IDNumber
						String pkEmpDiag=pvDiagMap.get("pkEmpDiag")==null ? map.get("pkEmpOrd").toString():pvDiagMap.get("pkEmpDiag").toString();
						Map<String, Object> empDiag = sDQueryUtils.getUserCodeByPkUser(pkEmpDiag);
						if(empDiag != null){
							dg1.getDiagnosingClinician(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(empDiag, "code"));
							dg1.getDiagnosingClinician(0).getFamilyName().getFn1_Surname().setValue(SDMsgUtils.getPropValueStr(empDiag, "name"));
						}
					}
					dg1.getAttestationDateTime().getTs1_TimeOfAnEvent().setValue(SDMsgUtils.getPropValueDate(map, "dateDiag"));
				}

			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loger.info("O21组装消息失败"+e);
			}
			return oml;
		}else if ("O19".equals(triggerEvent)) {

			//检查申请
			OMG_O19 oml = new OMG_O19();
			try{
				MSH msh = oml.getMSH();
				PID pid = oml.getPATIENT().getPID();
				PV1 pv1 = oml.getPATIENT().getPATIENT_VISIT().getPV1();
				ORC orc = oml.getORDER().getORC();
				Map<String, Object> pMap=createOpMsg.qrySdOpPID_PV1(map);
				SDMsgUtils.createMSHMsgCnLbPacs(msh, msgId, "OMG", "O19",SDMsgUtils.getPropValueStr(deptMap,"codeDept"));
				createOpMsg.createPIDMsg(pid, pMap);
				createOpMsg.createPV1Msg(pv1, pMap);
				createOpMsg.createORCMsg(orc, map);
				OBR obr = oml.getORDER().getOBR();
				OBX obx ;
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

				//identifier: SIGN:临床症状及体征 HISTORY:既往病史  RIS:检查结果 LIS:检验结果 DRUG:用药情况 REMARK:备注 DIAG:诊断  SITE：部位描述
				String sql = "select DESC_DIAGS,ROWNUM from CN_DIAG where PK_PV=? order by TS desc ";
				List<Map<String, Object>> diags = DataBaseHelper.queryForList(sql, SDMsgUtils.getPropValueStr(map, "pkPv"));
				if(diags!=null){
					map.put("descDiag", SDMsgUtils.getPropValueStr(diags.get(0), "descDiags"));
				}
				String [] obxStr={"SIGN","DIAG","SITE"};
				for (int j = 0; j < obxStr.length; j++) {
					obx=oml.getORDER().getOBSERVATION(j).getOBX();
					obx.getValueType().setValue("ST");
					obx.getObservationIdentifier().getIdentifier().setValue(obxStr[j]);
					String obxNote="";
					switch (obxStr[j]) {
						case "SIGN":
							obxNote = SDMsgUtils.getPropValueStr(map, "noteDise");break;
						case "DIAG":
							obxNote = SDMsgUtils.getPropValueStr(map, "descDiag");break;
						case "SITE":
							obxNote = SDMsgUtils.getPropValueStr(map, "nameOrd");break;
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
	 * 发送Hl7消息（OMP^O09）门诊处方
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendOMPMsg(String triggerEvent,Map<String,Object> paramMap){
		try{
			List<Map<String,Object>> listMap = null;
			if ("O09".equals(triggerEvent)) {
				if(paramMap.get("cnOrder") != null){
					String stringBean = JsonUtil.writeValueAsString(paramMap.get("cnOrder"));
					listMap=JsonUtil.readValue(stringBean, List.class);
				}else if(paramMap.get("addingList") != null){
					listMap = (List<Map<String, Object>>)paramMap.get("addingList");
				}

				if (listMap != null && listMap.size() > 0) {
					String msgId = SDMsgUtils.getMsgId();
					OMP_O09 omp = new OMP_O09();

					String msg;
					int mod = 0;
					int index=0;

					Map<String, Object> patinMap = listMap.get(0);
					patinMap.put("msgid",msgId);
					patinMap.put("msgtype", "OMP");
					patinMap.put("triggerevent", "O09");
					patinMap.put("control", SDMsgUtils.getPropValueStr(paramMap, "control"));
					Map<String, Object> pMap=createOpMsg.qrySdOpPID_PV1(patinMap);

					MSH msh = omp.getMSH();
					PID pid = omp.getPATIENT().getPID();
					PV1 pv1 = omp.getPATIENT().getPATIENT_VISIT().getPV1();

					createOpMsg.createMSHMsg(msh, patinMap);
					createOpMsg.createPIDMsg(pid, pMap);
					createOpMsg.createPV1Msg(pv1, pMap);

					for (int i = 0; i < listMap.size(); i++) {
						Map<String, Object> map = listMap.get(i);
						ORC orc = omp.getORDER(index).getORC();
						RXO rxo = omp.getORDER(index).getRXO();
						NTE nte = omp.getORDER(index).getNTE();
						RXR rxr = omp.getORDER(index).getRXR();
						FT1 ft1 = omp.getORDER(index).getFT1();
						map.put("triggerevent", "O09");
						map.put("msgtype", "OMP");
						map.put("control", SDMsgUtils.getPropValueStr(paramMap, "control"));
						createOpMsg.createORCMsg(orc, map);
						createOpMsg.createRXOMsg(rxo, map);

						//备注
						nte.getComment(0).setValue(SDMsgUtils.getPropValueStr(map, "noteOrd"));
						// 给药方式
						rxr.getRoute().getIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "codeSupply"));
						// 草药用法
						String supplyName=qrySupplyCode(SDMsgUtils.getPropValueStr(map, "codeSupply"));
						rxr.getAdministrationMethod().getIdentifier().setValue(supplyName);
						// FT1-12.3  费用金额
						ft1.getTransactionAmountUnit().getFromValue().setValue(SDMsgUtils.getPropValueStr(map, "priceCg"));
						if (i != 0 && i % splitNum == 0) {

							msg = SDMsgUtils.getParser().encode(omp);
							sDHl7MsgHander.sendMsg(msgId, msg);

							msgId = SDMsgUtils.getMsgId();
							omp = new OMP_O09();
							patinMap.put("msgid",msgId);
							patinMap.put("msgtype", "OMP");
							patinMap.put("triggerevent", "O09");
							patinMap.put("control", SDMsgUtils.getPropValueStr(paramMap, "control"));
							msh = omp.getMSH();
							pid = omp.getPATIENT().getPID();
							pv1 = omp.getPATIENT().getPATIENT_VISIT().getPV1();
							createOpMsg.createMSHMsg(msh, patinMap);
							createOpMsg.createPIDMsg(pid, pMap);
							createOpMsg.createPV1Msg(pv1, pMap);


							mod = 0;
							index=0;
						} else {
							mod = i % splitNum;
							index++;
						}
					}

					if (mod != 0 || listMap.size() == 1) {
						msg = SDMsgUtils.getParser().encode(omp);
						sDHl7MsgHander.sendMsg(msgId, msg);
					}

				}
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.info("发送Hl7消息（OMP^O09）门诊处方 出错:", e);
		}
	}

	/**
	 * 根据医嘱用法名称查询用法编码
	 * @param name
	 * @return
	 */
	private String qrySupplyCode(String name){
		if(name.length()<=0) {
			return null;
		}
		String sql="select name  from bd_supply where code=?";
		return DataBaseHelper.queryForScalar(sql, String.class, name);
	}

}
