package com.zebone.nhis.ma.pub.platform.sd.send;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.*;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.sd.common.SDHl7MsgHander;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.lang3.StringUtils;
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
public class SDMsgSendAdt {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");
	@Resource
	private	SDHl7MsgHander sDHl7MsgHander;
	@Resource
	private	SDMsgMapper sDMsgMapper;

	@Resource
	private	SDMsgSendOpAdt sDMsgSendOp;

	/**
	 * 发送Hl7消息
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendAdtMsg(String triggerEvent,Map<String,Object> paramMap) throws HL7Exception{
		try {
			String msgId=SDMsgUtils.getMsgId();
			String ipOrOp = SDMsgUtils.getPropValueStr(paramMap,"ipOrOp");
			Message message;
			if("O".equals(ipOrOp)) {
				//门诊
				message=sDMsgSendOp.createAdtOpMsg(msgId,triggerEvent, paramMap);
			} else {
				//住院
				message=createAdtIpMsg(msgId,triggerEvent, paramMap);
			}
			String msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.info(triggerEvent+"发送消息失败"+e);
			throw new HL7Exception("发送消息失败！");
		}
	}

	/**
	 * 发送ADT消息   住院消息
	 * @param msgId
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	private Message createAdtIpMsg(String msgId,String triggerEvent,Map<String,Object> paramMap) throws HL7Exception{
		if("A01".equals(triggerEvent)){
			if("新出生".equals(SDMsgUtils.getPropValueStr(paramMap,"adtType"))){
				//婴儿登记
				return a01_admitInf(msgId, paramMap);
			}
			else{
				//入院登记
				return a01_admit(msgId, paramMap);
			}
		}else if("A02".equals(triggerEvent)){
			//转科
			return a02_deptChange(msgId, paramMap);
		}else if("A03".equals(triggerEvent)){
			//出院结算
			return sendADTA03Msg(msgId, paramMap);
		}else if("A08".equals(triggerEvent)){
			//修改患者信息/修改婴儿信息
			return a08_editIpPatiInfo(msgId, paramMap);
		}else if("A10".equals(triggerEvent)){
			//入科接收 √
			return a10_ipDeptIn(msgId, paramMap);
		}else if("A11".equals(triggerEvent)){
			//取消入院登记 | 婴儿取消登记 | 门诊退号
			return a11_cancelAdmitOrVisit(msgId, paramMap);
		}else if("A16".equals(triggerEvent)){
			//出院
			return a16_discharge(msgId, paramMap);
		}else if("A25".equals(triggerEvent)){
			//取消出院
			return a25_cancelDisCharge(msgId, paramMap);
		}else if("A32".equals(triggerEvent)){
			//取消入科 √     取消门诊到诊
			return a32_cancelDeptIn(msgId, paramMap);
		}else if("A42".equals(triggerEvent)){
			//包床|换床
			return a42_bedChange(msgId, paramMap);
		}else if("A54".equals(triggerEvent)){
			//修改主治医生
			return a54_changeAttendDoctor(msgId, paramMap);
		}
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
		SDMsgUtils.createMSHMsg(msh, msgId, "ADT","A01");

		EVN evn=adt01.getEVN();
		createEVNMsg(paramMap, evn ,"A01");

		PID pid = adt01.getPID();
		//SDMsgUtils.createPIDMsg(pid, paramMap);

		PV1 pv1 = adt01.getPV1();
		//SDMsgUtils.createPV1Msg(pv1, paramMap);
		qryAndSetPID_PV1(pid, pv1, paramMap);

		NK1 nk1 = adt01.getNK1();
		createNK1Msg(nk1, paramMap);

		if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(paramMap,"pkPv"))){
			//诊断
			DG1 dg1=adt01.getDG1(0);
			Map<String,Object> diagMap = DataBaseHelper.queryForMap("SELECT * FROM PV_IP WHERE pk_pv = ?", SDMsgUtils.getPropValueStr(paramMap,"pkPv"));
			//类别
			if(diagMap != null){
				dg1.getDiagnosisType().setValue("01");//入院诊断
				dg1.getDiagnosisCodeDG1().getIdentifier().setValue(SDMsgUtils.getPropValueStr(diagMap,"codeDiag"));//诊断编码
				dg1.getDiagnosisDescription().setValue(SDMsgUtils.getPropValueStr(diagMap,"nameDiag"));//诊断名称
			}
			//dg1.getAttestationDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueDate(paramMap,"dateBegin"));
			dg1.getDiagnosingClinician(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpTre"));
			dg1.getDiagnosingClinician(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(paramMap,"nameEmpTre"));
		}

		} catch (Exception e) {
			// TODO: handle exception
			loger.info("A01"+e);
		}
		return adt01;
	}

	/**
	 * ADT^A01 ADT_A01 婴儿 - 入院登记
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	private Message a01_admitInf(String msgId, Map<String, Object> paramMap) throws HL7Exception{
		ADT_A01 adt01 = new ADT_A01();
		try 
		{
			if(paramMap != null && paramMap.containsKey("pkPv"))
			{
				paramMap.remove("pkPv");
			}
			MSH msh = adt01.getMSH();
			SDMsgUtils.createMSHMsg(msh, msgId, "ADT","A01");
	
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
			// TODO: handle exception
			loger.info("婴A01"+e);
		}
		createAdtObx(adt01,SDMsgUtils.getPropValueStr(paramMap, "codeIp"));

		return adt01;
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
		StringBuffer sqlInp=new StringBuffer();
		sqlInp.append("select  weight, len, weeks_preg, def.name out_mode, res.name labres,  num_preg, num_product from pv_infant pv");
		sqlInp.append(" left join bd_defdoc def on def.code_defdoclist = '060209' and def.code = pv.dt_out_mode");
		sqlInp.append(" left join bd_defdoc res on res.code_defdoclist='140023' and res.code=pv.dt_labresult2");
		sqlInp.append(" WHERE pv.code = ?");
		Map<String,Object> resMap=DataBaseHelper.queryForMap(sqlInp.toString(), pkPvInf);
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

	/**
	 * ADT^A02 ADT_A02转科
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a02_deptChange(String msgId, Map<String, Object> paramMap) throws DataTypeException{

		ADT_A02 adt02=new ADT_A02();

		MSH msh = adt02.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "ADT", "A02");

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
	 * @param msgId
	 * @param paramMap
	 * @throws DataTypeException 
	 * @throws HL7Exception
	 */
	public Message sendADTA03Msg(String msgId, Map<String, Object> paramMap) throws DataTypeException{

		ADT_A03 adt03 = new ADT_A03();
		//出院结算
		MSH msh = adt03.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "ADT","A03");
	
		EVN evn=adt03.getEVN();
		createEVNMsg(paramMap, evn, "A03");
	
		PID pid = adt03.getPID();
		PV1 pv1 = adt03.getPV1();
	
		qryAndSetPID_PV1(pid, pv1, paramMap);
	
		return adt03;
	}


	/**
	 * ADT^A08 ADT_A01修改患者信息
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	private Message a08_editIpPatiInfo(String msgId, Map<String, Object> paramMap) throws HL7Exception{

		ADT_A01 adt01=new ADT_A01();

		MSH msh = adt01.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "ADT", "A08");

		EVN evn=adt01.getEVN();
		createEVNMsg(paramMap, evn, "A08");

		PID pid = adt01.getPID();
		PV1 pv1 = adt01.getPV1();
		qryAndSetPID_PV1(pid, pv1, paramMap);

		//联系人
		NK1 nk1=adt01.getNK1();
		createNK1Msg(nk1, paramMap);

		if(null!=paramMap && ("新出生").equals(paramMap.get("adtType"))){
			PV2 pv2 = adt01.getPV2();
			pv2.getNewbornBabyIndicator().setValue("Y");//婴儿
			createAdtObx(adt01, SDMsgUtils.getPropValueStr(paramMap, "codeIp"));
		}

		if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(paramMap,"pkPv"))){
			//诊断
			DG1 dg1=adt01.getDG1(0);
			Map<String,Object> diagMap = DataBaseHelper.queryForMap("SELECT * FROM PV_IP WHERE pk_pv = ?", SDMsgUtils.getPropValueStr(paramMap,"pkPv"));
			//类别
			if(diagMap != null){
				dg1.getDiagnosisType().setValue("01");//入院诊断
				dg1.getDiagnosisCodeDG1().getIdentifier().setValue(SDMsgUtils.getPropValueStr(diagMap,"codeDiag"));//诊断编码
				dg1.getDiagnosisDescription().setValue(SDMsgUtils.getPropValueStr(diagMap,"nameDiag"));//诊断名称

			}
//			dg1.getAttestationDateTime().getTimeOfAnEvent().setValue(SDMsgUtils.getPropValueDate(paramMap,"dateBegin"));
			dg1.getDiagnosingClinician(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeEmpTre"));
			dg1.getDiagnosingClinician(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(paramMap,"nameEmpTre"));
		}
		return adt01;
	}


	/**
	 * 针对多条患者信息修改
	 * @param triggerEvent
	 * @param paramMap  (必传pkpi)
	 * @throws HL7Exception
	 */
	public void sendAdt08Msg(String triggerEvent,Map<String,Object> paramMap) throws HL7Exception{
		List<Map<String, Object>> patis = sDMsgMapper.queryPatList(paramMap);
		//判断是否发送一条消息
		int count = 0;
		for (Map<String, Object> map : patis) {
			//在院患者
			boolean in = ("1".equals(SDMsgUtils.getPropValueStr(map, "flagIn")) && "3".equals(SDMsgUtils.getPropValueStr(map, "euPvtype")));
			//门诊当天的有效就诊记录
			boolean out = (SDMsgUtils.getPropValueStr(map, "dateBegin").startsWith(day.format(new Date())) && !"3".equals(SDMsgUtils.getPropValueStr(map, "euPvtype")));
			if(in || out ){
				String msgId = SDMsgUtils.getMsgId();
				ADT_A01 adt01 = new ADT_A01();

				MSH msh = adt01.getMSH();
				SDMsgUtils.createMSHMsg(msh, msgId, "ADT", "A08");

				EVN evn = adt01.getEVN();
				createEVNMsg(paramMap, evn, "A08");

				PID pid = adt01.getPID();
				SDMsgUtils.createPIDMsg(pid, map);

				PV1 pv1 = adt01.getPV1();
				SDMsgUtils.createPV1Msg(pv1, map);

				NK1 nk1 = adt01.getNK1();
				createNK1Msg(nk1, map);
				//createNK1Msg(nk1, PiMap);
				//发送消息
				sDHl7MsgHander.sendMsg(msgId, SDMsgUtils.getParser().encode(adt01));
				
				count++;
			}
		}
		//默认发送一条消息
		if(count==0){
			String msgId = SDMsgUtils.getMsgId();
			ADT_A01 adt01 = new ADT_A01();

			MSH msh = adt01.getMSH();
			SDMsgUtils.createMSHMsg(msh, msgId, "ADT", "A08");

			EVN evn = adt01.getEVN();
			createEVNMsg(paramMap, evn, "A08");

			PID pid = adt01.getPID();
			SDMsgUtils.createPIDMsg(pid, patis.get(0));

			PV1 pv1 = adt01.getPV1();
			SDMsgUtils.createPV1Msg(pv1, patis.get(0));

			NK1 nk1 = adt01.getNK1();
			createNK1Msg(nk1, patis.get(0));
			//createNK1Msg(nk1, PiMap);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, SDMsgUtils.getParser().encode(adt01));
		}
	}

	/**
	 * ADT^A10 ADT_A09 入科
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a10_ipDeptIn(String msgId, Map<String, Object> paramMap) throws DataTypeException{

		ADT_A09 adt09 = new ADT_A09();

		MSH msh = adt09.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "ADT","A10");

		EVN evn=adt09.getEVN();
		createEVNMsg(paramMap, evn,"A10");

		PID pid = adt09.getPID();
		PV1 pv1 = adt09.getPV1();
		qryAndSetPID_PV1(pid, pv1, paramMap);

		return adt09;
	}

	/**
	 * ADT^A11 ADT_A09 取消入院 | 婴儿撤销登记 | 门诊退号  PvInfantService.deleteInfantByPk
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a11_cancelAdmitOrVisit(String msgId, Map<String, Object> paramMap) throws DataTypeException{

		ADT_A09 adt09 =new ADT_A09();

		MSH msh = adt09.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "ADT", "A11");

		EVN evn=adt09.getEVN();
		createEVNMsg(paramMap, evn, "A11");
		PID pid = adt09.getPID();
		PV1 pv1 = adt09.getPV1();
		//paramMap.remove("pkPi");
		qryAndSetPID_PV1(pid, pv1, paramMap);
		//如果是婴儿
		if(null != paramMap && null != paramMap.get("adtType") &&"新出生".equals(paramMap.get("adtType"))){
			PV2 pv2 = adt09.getPV2();
			pv2.getNewbornBabyIndicator().setValue("Y");//婴儿
		}

		return adt09;
	}

	/**
	 * ADT^A16  ADT_A16出院
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	private Message a16_discharge(String msgId, Map<String, Object> paramMap) throws DataTypeException{

		ADT_A16 adt16 = new ADT_A16();

		MSH msh = adt16.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "ADT", "A16");

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
	private Message a25_cancelDisCharge(String msgId, Map<String, Object> paramMap) throws DataTypeException{

		ADT_A21 adt21 = new ADT_A21();

		MSH msh = adt21.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "ADT", "A25");

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
	private Message a32_cancelDeptIn(String msgId, Map<String, Object> paramMap) throws DataTypeException{

		ADT_A21 adt21 = new ADT_A21();

		MSH msh = adt21.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "ADT", "A32");

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
	private Message a42_bedChange(String msgId, Map<String, Object> paramMap){

		ADT_A39 adt39 = new ADT_A39();
		try {

		MSH msh = adt39.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "ADT", "A42");

		EVN evn=adt39.getEVN();
		createEVNMsg(paramMap, evn, "A42");

		if(CommonUtils.isEmptyString(SDMsgUtils.getPropValueStr(paramMap,"bedStatus"))){
			//换床
			//1、查询患者_1基本信息
			Map<String, Object> pati = new HashMap<>(16);
			pati.put("pkPv", SDMsgUtils.getPropValueStr(paramMap,"pkPv"));
			//添加床位信息，推送换床使用
			pati.put("bednoDes", SDMsgUtils.getPropValueStr(paramMap,"bednoDes"));
			PID pid_1 = adt39.getPATIENT(0).getPID();
			PV1 pv1_1 = adt39.getPATIENT(0).getPV1();
			qryAndSetPID_PV1(pid_1, pv1_1, pati);
			//如果存在婴儿
			List<Map<String, Object>> infantList = sDMsgMapper.getInfantListByMother(paramMap);
			if(infantList!=null && infantList.size()>0){
				for(Map<String,Object> map:infantList){
					ADT_A39 adt_a42 = new ADT_A39();
					String id = SDMsgUtils.getMsgId();
					SDMsgUtils.createMSHMsg(adt_a42.getMSH(),id , "ADT", "A42");
					createEVNMsg(map, adt_a42.getEVN(), "A42");
					Map<String, Object> infant = new HashMap<>(16);
					infant.put("pkPv", SDMsgUtils.getPropValueStr(map,"pkPv"));
					infant.put("pkPi", SDMsgUtils.getPropValueStr(map,"pkPi"));
					qryAndSetPID_PV1(adt_a42.getPATIENT(0).getPID(), adt_a42.getPATIENT(0).getPV1(), infant);
					//发送消息
					sDHl7MsgHander.sendMsg(id, SDMsgUtils.getParser().encode(adt_a42));
				}
			}

			//2、如果和患者互换，则查询 & 组织患者_2基本信息(如果患者存在婴儿不允许互换)
			if(!CommonUtils.isEmptyString(SDMsgUtils.getPropValueStr(paramMap,"pkPvDes"))){
				Map<String, Object> pati2 = new HashMap<>(16);
				pati2.put("pkPv", SDMsgUtils.getPropValueStr(paramMap,"pkPvDes"));
				//添加床位信息，推送换床使用
				pati.put("bednoDes", SDMsgUtils.getPropValueStr(paramMap,"bednoDes"));
				PID pid_2 = adt39.getPATIENT(1).getPID();
				PV1 pv1_2 = adt39.getPATIENT(1).getPV1();
				qryAndSetPID_PV1(pid_2, pv1_2, pati2);
			}
		}else{
			//包床 | 退包床
			List<String> beds = (List<String>)paramMap.get("bednos");
			if(beds.size() < 1) {
				return null;
			}

			//1、查询患者基本信息
			Map<String,Object> pati = new HashMap<>(16);
			pati.put("pkPv", CommonUtils.getString(paramMap.get("pkPv")));
			pati = qryPID_PV1(pati);

			for(int i = 0 ; i < beds.size() ; i++){
				PID pid = adt39.getPATIENT(i).getPID();
				SDMsgUtils.createPIDMsg(pid, pati);
				PV1 pv1 = adt39.getPATIENT(i).getPV1();
				SDMsgUtils.createPV1Msg(pv1, pati);
				pv1.getBedStatus().setValue(SDMsgUtils.getPropValueStr(paramMap,"bedStatus"));
				pv1.getAssignedPatientLocation().getBed().setValue(beds.get(i));
			}
		}

		} catch (Exception e) {
			loger.info("pkPv"+SDMsgUtils.getPropValueStr(paramMap,"pkPv")+"A42"+e);
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
	private Message a54_changeAttendDoctor(String msgId, Map<String, Object> paramMap) throws DataTypeException{

		ADT_A54 adt54 = new ADT_A54();

		MSH msh = adt54.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "ADT", "A54");

		EVN evn=adt54.getEVN();
		createEVNMsg(paramMap, evn, "A54");

		PID pid = adt54.getPID();
		PV1 pv1 = adt54.getPV1();
		paramMap.put("triggerEvent", "A54");
		qryAndSetPID_PV1(pid, pv1, paramMap);

		return adt54;
	}




	//************************************调用公共方法分界线**************************************************************

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
		SDMsgUtils.createPIDMsg(pid, patMap);
		//3、处理患者就诊信息
		SDMsgUtils.createPV1Msg(pv1, patMap);
		return patMap;
	}


	/**
	 * 查询患者基本信息PID，PV1
	 * @param patMap
	 * @return
	 */
	public Map<String,Object> qryPID_PV1(Map<String,Object> patMap){
		//1、查询患者基本信息
		List<Map<String, Object>> pati = sDMsgMapper.queryPatList(patMap);
		if(null != pati && pati.size()>0){
			patMap.putAll(pati.get(0));
		}
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
	 * 创建EVN
	 * @param paramMap
	 * @param evn
	 * @throws DataTypeException
	 */
	private void createEVNMsg(Map<String, Object> paramMap, EVN evn , String opeType) throws DataTypeException{
		//1.事件类型代码
		evn.getEventTypeCode().setValue(opeType);
		//2.记录日期时间
		evn.getRecordedDateTime().getTimeOfAnEvent().setValue(DateUtils.getDateTimeStr(new Date()));
		//3.计划事件日期／时间
		evn.getDateTimePlannedEvent().getTimeOfAnEvent().setValue(sdf.format(new Date()));
		//4.事件原因代码
		evn.getEventReasonCode().setValue("01");
		//5.操作者ID
		evn.getOperatorID(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
		evn.getOperatorID(0).getFamilyName().getSurname().setValue(UserContext.getUser().getNameEmp());
		//6.已发生事件
		evn.getEventOccurred().getTimeOfAnEvent().setValue(sdf.format(new Date()));
		//7.事件机构
		evn.getEventFacility().getNamespaceID().setValue("NHIS");
	}

}
