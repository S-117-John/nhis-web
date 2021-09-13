package com.zebone.nhis.ma.pub.platform.sd.send;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.*;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.sd.common.SDHl7MsgHander;
import com.zebone.nhis.ma.pub.platform.sd.create.CreateOpMsg;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDOpMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 门诊发消息
 * @author maijiaxing
 *
 */
@Service
public class SDMsgSendOpAdt {

	@Resource
	private	SDHl7MsgHander sDHl7MsgHander;
	@Resource
	private SDQueryUtils sDQueryUtils;
	@Resource
	private CreateOpMsg createOpMsg;
	@Resource
	private SDOpMsgMapper sDOpMsgMapper;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	



	/**
	 * 发送ADT消息   门诊消息
	 * @param msgId
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public Message createAdtOpMsg(String msgId,String triggerEvent,Map<String,Object> paramMap) throws HL7Exception{
		if("A03".equals(triggerEvent)){
			//门诊诊毕
			return a03_opDiagnose(msgId, paramMap);
		}else if("A04".equals(triggerEvent)){
			//门诊挂号
			return a04_opDeptIn(msgId, paramMap);
		}else if("A06".equals(triggerEvent)){
			//门诊转住院
			return a06_opToIpInfo(msgId, paramMap);
		}else if("A08".equals(triggerEvent)){
			//修改门诊患者信息
			return a08_opEditPatiInfo(msgId, paramMap);
		}else if("A10".equals(triggerEvent)){
			//门诊到诊
			return a10_opDeptIn(msgId, paramMap);
		}else if("A11".equals(triggerEvent)){
			//门诊退号
			return a11_opCancelAdmitOrVisit(msgId, paramMap);
		}else if("A32".equals(triggerEvent)){
			//取消门诊到诊
			return a32_opCancelDeptIn(msgId, paramMap);
		}
		return null;
	}



	/**
	 * 发送门诊诊毕消息（深圳）
	 * @param paramMap
	 * @throws Exception 
	 */
	public void sendOpCompleteMsg(Map<String, Object> paramMap) throws Exception {
		String pKPv = paramMap.get("pkPv").toString();
		paramMap = sDQueryUtils.getPatMap(pKPv,null);
		paramMap.put("euPvtype", "1");
		String msgId=SDMsgUtils.getMsgId();
		//门诊诊毕
		ADT_A03 adt03 = new ADT_A03();
		MSH msh = adt03.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "ADT","A03");
		EVN evn=adt03.getEVN();
		evn.getRecordedDateTime().getTimeOfAnEvent().setValue(sdf.format(new Date()));
		//evn.getOperatorID(0).getIDNumber().setValue(paramMap.get("codeEmp").toString());
		PID pid = adt03.getPID();
		SDMsgUtils.createPIDMsg(pid, paramMap);
		PV1 pv1 = adt03.getPV1();
		SDMsgUtils.createPV1Msg(pv1, paramMap);
		String msg = SDMsgUtils.getParser().encode(adt03);
		//发送消息
		sDHl7MsgHander.sendMsg(msgId, msg);
	}




	/**
	 * 发送门诊诊毕消息A03
	 * @param msgId
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public Message a03_opDiagnose(String msgId, Map<String, Object> paramMap) throws DataTypeException {

		ADT_A03 adt03 = new ADT_A03();
		MSH msh = adt03.getMSH();
		EVN evn =adt03.getEVN();
		paramMap.put("msgid",msgId);
		paramMap.put("msgtype", "ADT");
		paramMap.put("triggerevent", "A03");
		createOpMsg.createMSHMsg(msh, paramMap);
		createOpMsg.createEVNMsg(evn,paramMap);

		PID pid = adt03.getPID();
		PV1 pv1 = adt03.getPV1();

		setSdOpPID_PV1(pid, pv1, paramMap);

		return adt03;
	}


	/**
	 * ADT^A04 ADT_A01门诊挂号
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a04_opDeptIn(String msgId, Map<String, Object> paramMap) throws DataTypeException{

		ADT_A01 adt04=new ADT_A01();
		MSH msh = adt04.getMSH();
		EVN evn =adt04.getEVN();
		paramMap.put("msgid",msgId);
		paramMap.put("msgtype", "ADT");
		paramMap.put("triggerevent", "A04");
		createOpMsg.createMSHMsg(msh, paramMap);
		createOpMsg.createEVNMsg(evn,paramMap);
		PID pid = adt04.getPID();
		PV1 pv1 = adt04.getPV1();
		paramMap = setSdOpPID_PV1(pid, pv1, paramMap);

		NK1 nk1=adt04.getNK1();
		//是否需要重新取paramMap
		createOpMsg.createNK1Msg(nk1, paramMap);
		//预约段 数据
		List<Map<String, Object>> ticketnoList =  sDOpMsgMapper.qryTimeSegmentSort(paramMap);
		PV2 pv2 = adt04.getPV2();
		//pv2-2  固定值
		pv2.getPv22_AccommodationCode().getCe1_Identifier().setValue("0");
		if(ticketnoList!=null && ticketnoList.size()>0){
			//pv1-3.9
			pv1.getAssignedPatientLocation().getLocationDescription().setValue(SDMsgUtils.getPropValueStr(ticketnoList.get(0),"ticketno"));
			// pv2-1  0：预约 1：当天
			String timePosition = SDMsgUtils.getPropValueStr(ticketnoList.get(0),"pkSchappt");
			pv2.getPriorPendingLocation().getPointOfCare().setValue("".equals(timePosition)?"1":"0");
			//pv2-7  0：现场 1：自助机 2：电话 3：微信  4：支付宝
			pv2.getVisitUserCode(0).setValue(SDMsgUtils.getPropValueStr(paramMap,"addrPosition"));
			//pv2-8 预约开始时间
			pv2.getExpectedAdmitDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(ticketnoList.get(0),"dateBe"));
			//pv2-9 预约结束时间
			pv2.getExpectedDischargeDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueStr(ticketnoList.get(0),"dateEn"));
			//如果当前时间大于预约结束时间 说明迟到 修改pv2-1=1 为当天号
//			String date = sdf.format(new Date());
//			if(date.compareTo(SDMsgUtils.getPropValueStr(ticketnoList.get(0),"dateBe"))>0){
//				//pv2-1
//				pv2.getPriorPendingLocation().getPointOfCare().setValue("1");
//				//挂号开始时间 改为当前时间
//				pv2.getExpectedAdmitDateTime().getTimeOfAnEvent().setValue(date);
//
//			};
		}
		return adt04;
	}


	private Message a06_opToIpInfo(String msgId, Map<String, Object> paramMap) throws HL7Exception{
		ADT_A06 adt06=new ADT_A06();
		MSH msh = adt06.getMSH();
		EVN evn = adt06.getEVN();
		paramMap.put("msgid",msgId);
		paramMap.put("msgtype", "ADT");
		paramMap.put("triggerevent", "A06");
		createOpMsg.createMSHMsg(msh, paramMap);
		createOpMsg.createEVNMsg(evn,paramMap);

		PID pid = adt06.getPID();
		PV1 pv1 = adt06.getPV1();
		setSdOpPID_PV1(pid, pv1, paramMap);
		Map<String,Object> deptMap = DataBaseHelper.queryForMap("SELECT code_dept FROM bd_ou_dept  where pk_dept = ?", SDMsgUtils.getPropValueStr(paramMap,"pkDeptIp"));
		pv1.getPendingLocation().getFacility().getUniversalID().setValue(SDMsgUtils.getPropValueStr(deptMap,"codeDept"));

		if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(paramMap,"pkPv"))){
			//诊断
			DG1 dg1=adt06.getDG1(0);
			Map<String,Object> diagMap = DataBaseHelper.queryForMap("SELECT * FROM PV_DIAG WHERE pk_pv = ?", SDMsgUtils.getPropValueStr(paramMap,"pkPv"));
			dg1.getDiagnosisCodingMethod().setValue("NW");
			dg1.getDiagnosisType().setValue("01");//入院诊断
			//类别
			if(diagMap != null){
				dg1.getDiagnosisCodeDG1().getIdentifier().setValue(SDMsgUtils.getPropValueStr(diagMap,"codeIcd"));//诊断编码
				dg1.getDiagnosisDescription().setValue(SDMsgUtils.getPropValueStr(diagMap,"nameDiag"));//诊断名称
				//1、主要诊断 2、次要诊断
				if(diagMap.get("flagMaj")!=null&&diagMap.get("flagMaj").toString().equals("1")){
					dg1.getDiagnosisPriority().setValue("1");
				}else{
					dg1.getDiagnosisPriority().setValue("2");
				}
				dg1.getAttestationDateTime().getTimeOfAnEvent().setValue(sdf.format(SDMsgUtils.getPropValueDate(diagMap,"dateDiag")));
			}
			dg1.getDiagnosingClinician(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpPhy"));

		}
		return adt06;

	}


	/**
	 * ADT^A08 ADT_A01修改患者信息
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	private Message a08_opEditPatiInfo(String msgId, Map<String, Object> paramMap) throws HL7Exception{

		ADT_A01 adt08=new ADT_A01();
		MSH msh = adt08.getMSH();
		EVN evn=adt08.getEVN();
		paramMap.put("msgid",msgId);
		paramMap.put("msgtype", "ADT");
		paramMap.put("triggerevent", "A08");
		createOpMsg.createMSHMsg(msh, paramMap);
		createOpMsg.createEVNMsg(evn,paramMap);

		PID pid = adt08.getPID();
		PV1 pv1 = adt08.getPV1();
		setSdOpPID_PV1(pid, pv1, paramMap);

		//联系人
		NK1 nk1=adt08.getNK1();
		createNK1Msg(nk1, paramMap);

		if("新出生".equals(paramMap.get("adtType"))){
			PV2 pv2 = adt08.getPV2();
			//婴儿
			pv2.getNewbornBabyIndicator().setValue("Y");
			createAdtObx(adt08, SDMsgUtils.getPropValueStr(paramMap, "codeIp"));
		}

		if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(paramMap,"pkPv"))){
			//诊断
			DG1 dg1=adt08.getDG1(0);
			Map<String,Object> diagMap = DataBaseHelper.queryForMap("SELECT * FROM PV_DIAG WHERE pk_pv = ?", SDMsgUtils.getPropValueStr(paramMap,"pkPv"));
			//类别
			if(diagMap != null){
				//入院诊断
				dg1.getDiagnosisType().setValue("01");
				//诊断编码
				dg1.getDiagnosisCodeDG1().getIdentifier().setValue(SDMsgUtils.getPropValueStr(diagMap,"codeIcd"));
				//诊断名称
				dg1.getDiagnosisDescription().setValue(SDMsgUtils.getPropValueStr(diagMap,"nameDiag"));
				//1、主要诊断 2、次要诊断
				if( "1".equals(SDMsgUtils.getPropValueStr(diagMap,"flagMaj"))){
					dg1.getDiagnosisPriority().setValue("1");
				}else{
					dg1.getDiagnosisPriority().setValue("2");
				}
			}
//			dg1.getAttestationDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueDate(paramMap,"dateBegin"));
			dg1.getDiagnosingClinician(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpTre"));
			dg1.getDiagnosingClinician(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(paramMap,"nameEmpTre"));
		}
		return adt08;
	}



	/**
	 * ADT^A10 ADT_A09     门诊患者到诊
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a10_opDeptIn(String msgId, Map<String, Object> paramMap) throws DataTypeException {

		ADT_A09 adt10 = new ADT_A09();

		MSH msh = adt10.getMSH();
		EVN evn=adt10.getEVN();
		paramMap.put("msgid",msgId);
		paramMap.put("msgtype", "ADT");
		paramMap.put("triggerevent", "A10");
		createOpMsg.createMSHMsg(msh, paramMap);
		createOpMsg.createEVNMsg(evn,paramMap);
		PID pid = adt10.getPID();
		PV1 pv1 = adt10.getPV1();
		setSdOpPID_PV1(pid, pv1, paramMap);
		return adt10;
	}

	/**
	 * ADT^A11 ADT_A09    取消挂号
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a11_opCancelAdmitOrVisit(String msgId, Map<String, Object> paramMap) throws DataTypeException{

		ADT_A09 adt11 =new ADT_A09();

		MSH msh = adt11.getMSH();
		EVN evn = adt11.getEVN();
		paramMap.put("msgid",msgId);
		paramMap.put("msgtype", "ADT");
		paramMap.put("triggerevent", "A11");
		createOpMsg.createMSHMsg(msh, paramMap);
		createOpMsg.createEVNMsg(evn,paramMap);

		PID pid = adt11.getPID();
		PV1 pv1 = adt11.getPV1();
		paramMap.remove("pkPi");
		setSdOpPID_PV1(pid, pv1, paramMap);
		//如果是婴儿
		if("新出生".equals(SDMsgUtils.getPropValueStr(paramMap,"adtType"))){
			PV2 pv2 = adt11.getPV2();
			//婴儿
			pv2.getNewbornBabyIndicator().setValue("Y");
		}

		return adt11;
	}



	/**
	 * ADT^A32 ADT_A21 取消门诊到诊
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a32_opCancelDeptIn(String msgId, Map<String, Object> paramMap) throws DataTypeException {

		ADT_A21 adt32 = new ADT_A21();

		MSH msh = adt32.getMSH();
		EVN evn = adt32.getEVN();
		paramMap.put("msgid",msgId);
		paramMap.put("msgtype", "ADT");
		paramMap.put("triggerevent", "A32");
		createOpMsg.createMSHMsg(msh, paramMap);
		createOpMsg.createEVNMsg(evn,paramMap);

		PID pid = adt32.getPID();
		PV1 pv1 = adt32.getPV1();
		setSdOpPID_PV1(pid, pv1, paramMap);

		return adt32;
	}


	/**
	 * 查询并设置PID，PV1，返回患者基本信息-----(深大门诊)
	 * @param pid
	 * @param pv1
	 * @param patMap
	 * @return
	 */
	public Map<String,Object> setSdOpPID_PV1(PID pid,PV1 pv1,Map<String,Object> patMap){
		//1、查询患者基本信息
		patMap = createOpMsg.qrySdOpPID_PV1(patMap);
		//2、处理患者基本信息
		createOpMsg.createPIDMsg(pid, patMap);
		//3、处理患者就诊信息
		createOpMsg.createPV1Msg(pv1, patMap);
		return patMap;
	}

	/**
	 * 联系人相关信息
	 * @param nk1
	 * @param patiMap
	 * @throws DataTypeException
	 */
	private void createNK1Msg(NK1 nk1, Map<String, Object> patiMap) throws DataTypeException{
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

	/**
	 * ADT_A01/ADT_
	 * @param adt01
	 * @throws HL7Exception
	 */
	private void createAdtObx(ADT_A01 adt01,String pkPvInf) throws HL7Exception{
		if(adt01==null || CommonUtils.isNull(pkPvInf)) {
			return;
		}
		String sqlInp = "select  weight, len, weeks_preg, def.name out_mode, res.name labres,  num_preg, num_product from pv_infant pv" +
				" left join bd_defdoc def on def.code_defdoclist = '060209' and def.code = pv.dt_out_mode" +
				" left join bd_defdoc res on res.code_defdoclist='140023' and res.code=pv.dt_labresult2" +
				" WHERE pv.code = ?";
		Map<String,Object> resMap=DataBaseHelper.queryForMap(sqlInp, pkPvInf);
		if(resMap!=null){
			int index=0;
			for (String key : resMap.keySet()) {
				if(resMap.get(key)!=null){
					OBX obx=adt01.getOBX(index);
					String sortNO=CommonUtils.getString(index+1);
					obx.getSetIDOBX().setValue(sortNO);
					obx.getValueType().setValue("TX");
					switch (key) {
					case "weight"://体重
						obx.getObservationIdentifier().getIdentifier().setValue("BW");
						obx.getObservationIdentifier().getText().setValue("新生儿出生体重");
						obx.getUnits().getIdentifier().setValue("g");
						break;
					case "len"://身高
						obx.getObservationIdentifier().getIdentifier().setValue("BH");
						obx.getObservationIdentifier().getText().setValue("新生儿出生身高");
						obx.getUnits().getIdentifier().setValue("cm");
						break;
					case "weeksPreg"://孕周
						obx.getObservationIdentifier().getIdentifier().setValue("PW");
						obx.getObservationIdentifier().getText().setValue("孕周");
						obx.getUnits().getIdentifier().setValue("周");
						break;
					case "outMode"://分娩方式
						obx.getObservationIdentifier().getIdentifier().setValue("DM");
						obx.getObservationIdentifier().getText().setValue("分娩方式");
						break;
					case "labres"://分娩结果
						obx.getObservationIdentifier().getIdentifier().setValue("DR");
						obx.getObservationIdentifier().getText().setValue("分娩结果");
						break;
					case "numPreg"://孕次
						obx.getObservationIdentifier().getIdentifier().setValue("GT");
						obx.getObservationIdentifier().getText().setValue("孕次");
						obx.getUnits().getIdentifier().setValue("次");
						break;
					case "numProduct"://产次
						obx.getObservationIdentifier().getIdentifier().setValue("PT");
						obx.getObservationIdentifier().getText().setValue("产次");
						obx.getUnits().getIdentifier().setValue("次");
						break;
					default:
						break;
					}
					obx.getObservationValue(0).parse(resMap.get(key).toString());
					index++;
				}
			}

		}
	}


}
