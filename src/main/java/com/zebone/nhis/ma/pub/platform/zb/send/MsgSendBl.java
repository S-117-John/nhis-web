package com.zebone.nhis.ma.pub.platform.zb.send;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.*;
import ca.uhn.hl7v2.model.v24.segment.*;

import com.zebone.nhis.ma.pub.platform.zb.comm.Hl7MsgHander;
import com.zebone.nhis.ma.pub.platform.zb.dao.MsgRecBlMapper;
import com.zebone.nhis.ma.pub.platform.zb.model.ZFI;
import com.zebone.nhis.ma.pub.platform.zb.model.ZMR_ZH1;
import com.zebone.nhis.ma.pub.platform.zb.service.SysMsgService;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Adt消息处理
 * @author chengjia
 *
 */
@Service
public class MsgSendBl {
	
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");
	
	@Resource
	private	Hl7MsgHander msgHander;
	@Resource
	private MsgSendAdt msgSendAdt;
	@Resource
	private MsgRecBlMapper msgRecBlMapper;
	@Resource
	private SysMsgService msgService;
	
	/**
	 * 发送Hl7消息
	 * A03：出院结算；A13：取消出院结算；ZH1：病案首页费用
	 * @param triggerEvent
	 * @param paramMap 
	 * @throws HL7Exception
	 */
	public void sendBlMsgs(String triggerEvent,Map<String,Object> paramMap) throws HL7Exception{
		try {
			String msgId=MsgUtils.getMsgId();
			Message message=createBlMsg(msgId,triggerEvent, paramMap);
			String msg = MsgUtils.getParser().encode(message);
			//发送消息
			msgHander.sendMsg(msgId, msg);
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
	private Message createBlMsg(String msgId,String triggerEvent,Map<String,Object> paramMap){
		try {
			String nwDate = MsgUtils.PropDateSting(new Date());
		if("A03".equals(triggerEvent)){
			ADT_A03 adt03 = new ADT_A03();
			//出院结算
			MSH msh = adt03.getMSH();
			MsgUtils.createMSHMsg(msh, msgId, "ADT","A03");
			
			EVN evn=adt03.getEVN();
			//[1]A03	
			evn.getEventTypeCode().setValue("A03");
			//[2]记录日期/时间	
			evn.getRecordedDateTime().getTimeOfAnEvent().setValue(nwDate);
			//[3]计划事件的日期/时间 
			evn.getDateTimePlannedEvent().getTimeOfAnEvent().setValue(nwDate);
			//[4]"01: 患者请求 02: 医生/医疗从业者医嘱  03:人口普查管理"
			evn.getEventReasonCode().setValue("01");
			//[5]操作员编码^操作员名称 
			evn.getOperatorID(0).getIDNumber().setValue(MsgUtils.getPropValueStr(paramMap, "doCode"));
			evn.getOperatorID(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(paramMap, "doName"));
			//[6]事件发生时间/操作时间
			evn.getEventOccurred().getTimeOfAnEvent().setValue(nwDate);
			//[7]事件机构
			evn.getEventFacility().getHd1_NamespaceID().setValue("NHIS");
			
			PID pid = adt03.getPID();
			PV1 pv1 = adt03.getPV1(); 
		
			msgSendAdt.qryAndSetPID_PV1(pid, pv1, paramMap);
			
			return adt03;
		}else if("A13".equals(triggerEvent)){
			
			ADT_A01 adt01 = new ADT_A01();
			msgId=MsgUtils.getMsgId();
			//取消出院结算
			MSH msh = adt01.getMSH();
			MsgUtils.createMSHMsg(msh, msgId, "ADT","A13");
			
			EVN evn=adt01.getEVN();
			//[1]A13
			evn.getEventTypeCode().setValue("A13");
			//2 记录日期/时间 
			evn.getRecordedDateTime().getTimeOfAnEvent().setValue(nwDate);
			//3 计划事件的日期/时间 
			evn.getDateTimePlannedEvent().getTimeOfAnEvent().setValue(nwDate);
			//4"01: 患者请求 02: 医生/医疗从业者医嘱  03:人口普查管理"
			evn.getEventReasonCode().setValue("01");
			//5 操作员编码^操作员名称 
			evn.getOperatorID(0).getIDNumber().setValue(MsgUtils.getPropValueStr(paramMap, "doCode"));
			evn.getOperatorID(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(paramMap, "doName"));
			//6	事件发生时间/操作时间 
			evn.getEventOccurred().getTimeOfAnEvent().setValue(nwDate);
			//7	事件机构 
			evn.getEventFacility().getHd1_NamespaceID().setValue("NHIS");
			 
			PID pid = adt01.getPID();
			PV1 pv1 = adt01.getPV1();
			msgSendAdt.qryAndSetPID_PV1(pid, pv1, paramMap);
			
			return adt01;
		}else if("ZH1".equals(triggerEvent)){
			/**
			 * 创建ZMR^ZH1：病案首页费用
			 * @param paramMap{"doCode":"操作员编码","doName":"操作员名称","pkPi":"患者编码","pkPv":"就诊编码","totalAmount":"结算总金额","selfAmount":"自费金额"}
			 * @return
			 * @throws HL7Exception
			 */
			ZMR_ZH1 zmr=new ZMR_ZH1();
			MSH msh=zmr.getMSH();
			MsgUtils.createMSHMsg(msh, msgId, "ZMR", "ZH1");
			
			PID pid = zmr.getPID();
			PV1 pv1 = zmr.getPV1();
			msgSendAdt.qryAndSetPID_PV1(pid, pv1, paramMap);
			
			FT1 ft1=zmr.getFT1();
			//[2] 发票号码
			ft1.getTransactionID().setValue(MsgUtils.getPropValueStr(paramMap,"settleNo"));
			//[4] 事务日期
			ft1.getTransactionDate().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));
			//[6] "结算类别（请参照患者结算类别） 01：自费 02：医保 03：公费"
			ft1.getTransactionType().setValue(MsgUtils.getPropValueStr(paramMap, "amtType"));
			//[8] "结算方式 1：门诊结算 2：住院中途结算 3：出院结算"
			ft1.getTransactionDescription().setValue(MsgUtils.getPropValueStr(paramMap, "amtKind"));
			//[11] 结算总金额
			ft1.getTransactionAmountExtended().getPrice().getQuantity().setValue(MsgUtils.getPropValueStr(paramMap, "totalAmount"));
			//[15]Insurance Amount 	保险总金额
			ft1.getInsuranceAmount().getPrice().getMo1_Quantity().setValue(MsgUtils.getPropValueStr(paramMap, "amtInsu"));
			//[22]自费
			ft1.getUnitCost().getPrice().getQuantity().setValue(MsgUtils.getPropValueStr(paramMap, "selfAmount"));
			
			List<Map<String,Object>> amountList=msgRecBlMapper.queryZMRDataByPkPv(paramMap);
			int i=0;
			for (Map<String, Object> map : amountList) {
				//一对多
				ZFI zfi=zmr.getZFI(i);
				//费用编码
				zfi.getFybm().setValue(MsgUtils.getPropValueStr(map, "code"));
				//费用项目	
				zfi.getFyxm().setValue(MsgUtils.getPropValueStr(map, "name"));
				//费用金额	
				zfi.getFyje().setValue(MsgUtils.getPropValueStr(map, "amount"));
				i++;
			}
		
		return zmr;
		}else if("P03".equals(triggerEvent)){
			DFT_P03 dft = new DFT_P03();
			
			//创建msh消息
			MSH msh=dft.getMSH();
			MsgUtils.createMSHMsg(msh, msgId, "DFT", "P03");
			//创建EVN消息
			MsgUtils.createEVNMsg(dft.getEVN(), paramMap);
			
			//创建pid消息
			PID pid = dft.getPID();
			//创建pv1消息
			PV1 pv1 = dft.getPV1();
			msgSendAdt.qryAndSetPID_PV1(pid, pv1, paramMap);
			//创建ft1消息
			paramMap.put("triggerevent", "P03");
			MsgUtils.createFT1Msg(dft.getFINANCIAL().getFT1(), paramMap);
			return dft;
		}
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("组装{}错误{}",triggerEvent,e.getMessage());
		}
		return null;
	}	
	
	/**
	 * Lb-检验检查Hl7消息发送
	 * triggerEvent:消息类型;paramMap：推送数据;type：门诊：out、住院标识
	 * @param triggerEvent
	 * @param paramMap
	 * @param type
	 * @throws HL7Exception
	 */
	public void LbsendORLMsg(String triggerEvent,Map<String,Object> paramMap,String type) throws HL7Exception{
		Map<String,Object> patMap = new HashMap<>();
		try {
			if(type == "out" && type != null){	//门诊
				patMap = msgService.getPatMapOut(MsgUtils.getPropValueStr(paramMap, "pkPv"), patMap);
				String msgId=MsgUtils.getMsgId();
				Message message=sendORLMsgLb(msgId,triggerEvent,patMap,paramMap);
				String msg = MsgUtils.getParser().encode(message);
				//发送消息
				msgHander.sendMsg(msgId, msg);
			}else{				
				patMap = msgService.getPatMap(MsgUtils.getPropValueStr(paramMap, "pkPv"), patMap);
				String msgId=MsgUtils.getMsgId();
				Message message=sendORLMsgLb(msgId,triggerEvent,patMap,paramMap);
				String msg = MsgUtils.getParser().encode(message);
				//发送消息
				msgHander.sendMsg(msgId, msg);
			}
		} catch (Exception e) {
			loger.error("发送消息失败{},{}",triggerEvent,e.getMessage());
			throw new HL7Exception("发送消息失败！");
		}
		
	}
	
	/**
	 * Lb-发送Hl7消息（ORL）检验收费(护士)、（ORG）检查收费
	 * 
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public Message sendORLMsgLb(String msgId,String triggerEvent,Map<String,Object> patMap , Map<String, Object> map){
		try{
		if ("O22".equals(triggerEvent)) {
			
			// 检验申请确认(护士)
			ORL_O22 orl = new ORL_O22();
			MSH msh;
			MSA msa;
			PID pid;

			ORC orc;
			OBR obr;		
	
				msh = orl.getMSH();
				MsgUtils.createMSHMsg(msh, msgId.toString(), "ORL", "O22");
				
				msa=orl.getMSA();
				MsgUtils.createMSAMsg(msa, msgId);
				
				//PID
				pid = orl.getRESPONSE().getPATIENT().getPID();
				MsgUtils.createPIDMsg(pid, patMap);
				
                //记费/退费标志
				map.put("control", MsgUtils.getPropValueStr(map,"control"));
				//ORC
				orc = orl.getRESPONSE().getPATIENT().getGENERAL_ORDER().getORDER().getORC();
				getOrcForRisAndLis(orc, map);
				
				
				obr=orl.getRESPONSE().getPATIENT().getGENERAL_ORDER().getORDER().getOBSERVATION_REQUEST().getOBR();
				// 项目编号^医嘱名称"
				obr.getUniversalServiceIdentifier().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrd"));
				obr.getUniversalServiceIdentifier().getText().setValue(MsgUtils.getPropValueStr(map, "nameOrd"));
				//相关的临床信息 描述
				obr.getRelevantClinicalInfo().setValue(MsgUtils.getPropValueStr(map, "descOrd"));
				//样本类型
				obr.getSpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrdtype"));
				//费用信息  
				obr.getChargeToPractice().getDollarAmount().getMo1_Quantity().setValue(MsgUtils.getPropValueStr(map, "priceCg"));
				// 执行科室
				Map<String,Object> deptMap=msgService.getDeptInfoByPkDept(map.get("pkDeptExec")==null? "0306":map.get("pkDeptExec").toString());
				if(deptMap==null){
					obr.getDiagnosticServSectID().setValue("0306");
				}else{
					obr.getDiagnosticServSectID().setValue(MsgUtils.getPropValueStr(deptMap, "codeDept"));
				}
				
			return orl;
		}else if("O20".equals(triggerEvent)){
			// 检查申请确认(护士)
			ORG_O20 org = new ORG_O20();
			MSH msh;
			PID pid;
			MSA msa;
			ORC orc;
			OBR obr;
			
				msh = org.getMSH();
				MsgUtils.createMSHMsg(msh, msgId, "ORG", "O20");

				msa = org.getMSA();
				MsgUtils.createMSAMsg(msa, msgId);

				pid = org.getRESPONSE().getPATIENT().getPID();
				MsgUtils.createPIDMsg(pid, patMap);

				//ORC
				orc = org.getRESPONSE().getORDER(0).getORC();
				getOrcForRisAndLis(orc, map);
				
				obr = org.getRESPONSE().getORDER(0).getOBR();
				// 项目编号^医嘱码"
				obr.getUniversalServiceIdentifier().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrd"));
				obr.getUniversalServiceIdentifier().getText().setValue(MsgUtils.getPropValueStr(map, "nameOrd"));
				obr.getUniversalServiceIdentifier().getNameOfCodingSystem().setValue(MsgUtils.getPropValueStr(map, "codeOrdtype"));
				//相关的临床信息 描述
				obr.getRelevantClinicalInfo().setValue(MsgUtils.getPropValueStr(map, "descOrd"));

				obr.getChargeToPractice().getDollarAmount().getMo1_Quantity().setValue(MsgUtils.getPropValueStr(map, "priceCg"));
				if(null !=map.get("pkDeptExec") && !(map.get("pkDeptExec").equals(""))){
					// 执行科室
					Map<String,Object> deptMap=msgService.getDeptInfoByPkDept(map.get("pkDeptExec").toString());
					obr.getDiagnosticServSectID().setValue(MsgUtils.getPropValueStr(deptMap, "codeDept"));
				}
				
		   return org;
		}
	} catch (Exception e) {
		// TODO: handle exception
		loger.error("LbsendORLMsg方法O22组装错误{}",e.getMessage());
	}
		return null;
	}
	
	
	/**
	 * 发送Hl7消息（ORL）检验收费(护士)
	 * 
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendORLMsg(String triggerEvent, List<Map<String, Object>> listMap,String ipOpType){
		try{
		String msgId, msg = "";
		if (triggerEvent.equals("O22")) {
			int i;
			List<Map<String, Object>> resList = HL7SendBlOrder(listMap,ipOpType);
			int sun;
			if(("O").equals(ipOpType)){
				sun = 1;
			}else{
				sun = resList.size();
			}
			for (i = 0; i < sun; i++) {
			msgId = MsgUtils.getMsgId();
			// 检验申请确认(护士)
			ORL_O22 orl = new ORL_O22();
			MSH msh;
			MSA msa;
			PID pid;

			ORC orc;
			OBR obr;		
	
	
			if (resList != null && resList.size() > 0) {
				Map<String,Object> patMap=resList.get(0);
				msh = orl.getMSH();
				msgId = MsgUtils.getMsgId();
				MsgUtils.createMSHMsg(msh, msgId.toString(), "ORL", "O22");
				
				msa=orl.getMSA();
				MsgUtils.createMSAMsg(msa, msgId);
				
				Map<String,Object> param=new HashMap<String,Object>();
				param.put("pkPv", patMap.get("pkPv"));
				
				pid = orl.getRESPONSE().getPATIENT().getPID();
				MsgUtils.createPIDMsg(pid, msgSendAdt.qryPID_PV1(param));
			}
			
				Map<String, Object> map = resList.get(0);
				map.put("control", MsgUtils.getPropValueStr(listMap.get(0),"Control"));
				orc = orl.getRESPONSE().getPATIENT().getGENERAL_ORDER().getORDER().getORC();
				getOrcForRisAndLis(orc, map);
				obr=orl.getRESPONSE().getPATIENT().getGENERAL_ORDER().getORDER().getOBSERVATION_REQUEST().getOBR();
				// 项目编号^医嘱名称"
				obr.getUniversalServiceIdentifier().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrd"));
				obr.getUniversalServiceIdentifier().getText().setValue(MsgUtils.getPropValueStr(map, "nameOrd"));
				//相关的临床信息 描述
				obr.getRelevantClinicalInfo().setValue(MsgUtils.getPropValueStr(map, "descOrd"));
				//样本类型
				obr.getSpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrdtype"));
				//费用信息  
				obr.getChargeToPractice().getDollarAmount().getMo1_Quantity().setValue(MsgUtils.getPropValueStr(map, "priceCg"));
				// 执行科室
				Map<String,Object> deptMap=msgService.getDeptInfoByPkDept(map.get("pkDeptEx")==null? "0306":map.get("pkDeptEx").toString());
				if(deptMap==null){
					obr.getDiagnosticServSectID().setValue("0306");
				}else{
				obr.getDiagnosticServSectID().setValue(MsgUtils.getPropValueStr(deptMap, "codeDept"));}
					msg = MsgUtils.getParser().encode(orl);
					msgHander.sendMsg(msgId, msg);
					orl = null;
					map.clear();
			}
			resList = null;
		} 
		msgId = null;
		msg = null;
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("sendORLMsg方法O22组装错误{}",e.getMessage());
		}
	}
	

	/**
	 * 发送Hl7消息（ORG）检查收费
	 * 
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendOGRGMsg(String triggerEvent, List<Map<String, Object>> listMap,String ipOpType){
		try{
		if(triggerEvent.equals("O20")){
			// 检查申请确认(护士)
			String msgId = ""; 
			String msg = "";
			List<Map<String, Object>> ListMapOne = new ArrayList<>();
			for (int K = 0; K < listMap.size(); K++) {
			ListMapOne.add(listMap.get(K));
			List<Map<String, Object>> resList = HL7SendBlOrder(ListMapOne,ipOpType);
			BigDecimal amtAcc = BigDecimal.ZERO;
			if(0 != resList.size()){
				for (int i = 0; i < resList.size(); i++) {
					if(null != resList.get(i).get("amount")){
						Double pric = Double.parseDouble(resList.get(i).get("amount").toString());
						BigDecimal amt=BigDecimal.valueOf(pric);
						amtAcc=amtAcc.add(amt);	
					}	
				}
			}
			
			ORG_O20 org = new ORG_O20();
			MSH msh;
			PID pid;
			MSA msa;
			ORC orc;
			OBR obr;
			
			if (resList != null && resList.size() > 0) {
				Map<String,Object> patMap=resList.get(0);
				msh = org.getMSH();
				msgId = MsgUtils.getMsgId();

				msh = org.getMSH();
				MsgUtils.createMSHMsg(msh, msgId, "ORG", "O20");

				msa = org.getMSA();
				MsgUtils.createMSAMsg(msa, msgId);

				Map<String,Object> param=new HashMap<String,Object>();
				param.put("pkPv", patMap.get("pkPv"));
				
				pid = org.getRESPONSE().getPATIENT().getPID();
				MsgUtils.createPIDMsg(pid, msgSendAdt.qryPID_PV1(param));
			}
				Map<String, Object> map = resList.get(0);
				map.put("control", MsgUtils.getPropValueStr(listMap.get(0),"Control"));
				map.put("fenlei", MsgUtils.getPropValueStr(listMap.get(0),"fenlei"));
				map.put("ordsn", MsgUtils.getPropValueStr(listMap.get(0),"ordsn"));
				orc = org.getRESPONSE().getORDER(0).getORC();
				getOrcForRisAndLis(orc, map);
				
				obr = org.getRESPONSE().getORDER(0).getOBR();
				// 项目编号^医嘱码"
				obr.getUniversalServiceIdentifier().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrd"));
				obr.getUniversalServiceIdentifier().getText().setValue(MsgUtils.getPropValueStr(map, "descOrd"));
				obr.getUniversalServiceIdentifier().getNameOfCodingSystem().setValue(MsgUtils.getPropValueStr(listMap.get(0), "codeOrdtype"));
				//相关的临床信息 描述
				obr.getRelevantClinicalInfo().setValue(MsgUtils.getPropValueStr(map, "descOrd"));
				
				//样本类型"
				//obr.getSpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrdtype"));
				//费用信息 Charge to Practice +
				obr.getChargeToPractice().getDollarAmount().getMo1_Quantity().setValue(amtAcc.toString());
				if(null !=map.get("pkDeptEx") && !(map.get("pkDeptEx").equals(""))){
					// 执行科室
					Map<String,Object> deptMap=msgService.getDeptInfoByPkDept(map.get("pkDeptEx").toString());
					obr.getDiagnosticServSectID().setValue(MsgUtils.getPropValueStr(deptMap, "codeDept"));
				}
					msg = MsgUtils.getParser().encode(org);
					msgHander.sendMsg(msgId, msg);
					ListMapOne.clear();
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("sendOGRGMsg01方法O20组装错误{}",e.getMessage());
		}
	}
	

	
	/**
	 * 门诊处方收费
	 * @param triggerEvent 区分消息信息
	 * @param listMap 拼装的数据
	 * @param typeStatus 当前的状态【ADD,UPDATE,DEL】
	 * @throws HL7Exception 
	 */
	public void sendORPMsg(String triggerEvent,List<Map<String, Object>> listMap,String typeStatus){
		try{
		if("O10".equals(triggerEvent)){
			if(listMap!=null){
			int i;
			String msgId="",msg="";
			ORP_O10 orp=new ORP_O10();
			MSH msh;
			MSA msa;
			PID pid;
			
			ORC orc;
			NTE nte;
			
			int mode=0;
			int index =0;
			Map<String,Object> paramMap=new HashMap<>();
				msgId=MsgUtils.getMsgId();
				msh=orp.getMSH();
				MsgUtils.createMSHMsg(msh, msgId, "ORP", triggerEvent);
				msa=orp.getMSA();
				MsgUtils.createMSAMsg(msa, msgId);
				
				pid=orp.getRESPONSE().getPATIENT().getPID();
				
				//1、查询门诊患者基本+就诊信息
				paramMap = msgService.getPatMapOut(MsgUtils.getPropValueStr(listMap.get(0), "pkPv"), paramMap);
				//2、处理患者基本信息
				MsgUtils.createPIDMsg(pid, paramMap);
			
			List<String> presNos=new ArrayList<String>();
			for (i = 0; i < listMap.size(); i++) {
				Map<String, Object> map=listMap.get(i);
				if(!presNos.contains(MsgUtils.getPropValueStr(map, "presNo"))){
					orc=orp.getRESPONSE().getORDER(index).getORC();
					createORP_orc(typeStatus, orc, map);
					presNos.add(MsgUtils.getPropValueStr(map, "presNo"));
					
					int k=0;
					for (int j = 0; j < listMap.size(); j++) {
						Map<String, Object> mapNte=listMap.get(j);
						//if(!MsgUtils.getPropValueStr(mapNte, "presNo").equals(MsgUtils.getPropValueStr(map, "presNo")))continue;
						nte=orp.getRESPONSE().getORDER(index).getORDER_DETAIL().getNTE2(k++);
						//3	 价格
						nte.getComment(0).setValue(MsgUtils.getPropValueStr(mapNte, "amount"));
						//4   项目编码^项目名称	
						String codeItem=qryItemOrPdCodeByPk(MsgUtils.getPropValueStr(mapNte, "pkPd"),MsgUtils.getPropValueStr(mapNte, "pkItem"));
						nte.getCommentType().getIdentifier().setValue(codeItem);
						nte.getCommentType().getText().setValue(MsgUtils.getPropValueStr(mapNte, "nameCg"));
					}

				}
				if(mode!=0 && i%10==0){
					msg=MsgUtils.getParser().encode(orp);
					msgHander.sendMsg(msgId, msg);
					
					orp=new ORP_O10();
					msh=orp.getMSH();
					msa=orp.getMSA();
					MsgUtils.createMSHMsg(msh, msgId, "ORP", triggerEvent);
					MsgUtils.createMSAMsg(msa, msgId);
					
					pid=orp.getRESPONSE().getPATIENT().getPID();
					//2、处理患者基本信息
					MsgUtils.createPIDMsg(pid, paramMap);
					index=0;
					mode=0;
				}else{
					index++;
					mode=i%10;
				}
			}
			if (mode != 0 || listMap.size() == 1) {
				msg = MsgUtils.getParser().encode(orp);
				msgHander.sendMsg(msgId, msg);
			} 
		}}
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("sendORPMsg方法O10组装错误{}",e.getMessage());
		}
	}

	/**
	 * 处方收费Orc消息段
	 * @param typeStatus
	 * @param orp
	 * @param i
	 * @param map
	 * @throws DataTypeException
	 */
	private void createORP_orc(String typeStatus, ORC orc,
			Map<String, Object> map){
		try{
		//1	 申请控制 "OK：收费 CR：退费"
		orc.getOrderControl().setValue(typeStatus);
		//2	下单方申请单编号     发票流水号^^门诊流水号^O
		//orc.getPlacerOrderNumber().getEntityIdentifier().setValue("");
		String codePv=qryCodePvByPkPv(map.get("pkPv").toString());
		orc.getPlacerOrderNumber().getUniversalID().setValue(codePv);
		orc.getPlacerOrderNumber().getUniversalIDType().setValue("O");
		//3	发票号
		orc.getFillerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeInv"));
		//4	Placer Group Number	下单方申请单组编号  处方号
		orc.getPlacerGroupNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "presNo"));
		//5	0：手工流程收费标志；1：自助机或电脑录入标志
		orc.getOrderStatus().setValue("0");
		//9  事务日期/时间 
		orc.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));
		//10  输入者    - 收费人员ID^收费人员编码|
		orc.getEnteredBy(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
		orc.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
		//12  下单方提供者 
		Map<String,Object> ordUser=msgService.getUserCodeByPkUser(MsgUtils.getPropValueStr(map, "pkEmpApp"));
		if(null != ordUser && !("").equals(ordUser)){
			orc.getOrderingProvider(0).getIDNumber().setValue(MsgUtils.getPropValueStr(ordUser, "code"));
			orc.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(ordUser, "name"));
		}
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("处方收费Orc消息段组装错误{}",e.getMessage());
		}
	}
	
	/**
	 * 创建住院/门诊通用检查检验ORC消息段
	 * @param orc
	 * @param map
	 * @throws DataTypeException
	 */
	private void getOrcForRisAndLis(ORC orc, Map<String, Object> map){
		try{
		//[1] 申请控制  
		orc.getOrderControl().setValue(MsgUtils.getPropValueStr(map, "control"));
		String euPvtype="1".equals(map.get("euPvtype"))?"O":"I";
		///申请编码，检查发送医嘱号
		if(("ris").equals(MsgUtils.getPropValueStr(map, "fenlei"))){
		    orc.getPlacerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsn"));
		}else{
			orc.getPlacerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeApply"));
		}
		if(euPvtype.equals("O")){
			orc.getPlacerOrderNumber().getUniversalID().setValue(MsgUtils.getPropValueStr(map,"codePv" ));//门诊流水号
		}
		else{
			orc.getPlacerOrderNumber().getUniversalID().setValue(MsgUtils.getPropValueStr(map,"codePi" ));//住院流水号
		}
		orc.getPlacerOrderNumber().getUniversalIDType().setValue(euPvtype);//患者来源
		//[3]收费单号
		orc.getFillerOrderNumber().getEi1_EntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "pkCgip"));
		//[4] 医嘱号
		orc.getPlacerGroupNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrd"));
		//[5]申请单状态:0、项目内不收费；1、项目外单独收费
		orc.getOrderStatus().setValue("0");
		//[9]收费时间 
		if(null != MsgUtils.getPropValueDateSting(map, "dateHap")){
			orc.getDateTimeOfTransaction().getTs1_TimeOfAnEvent().setValue(MsgUtils.getPropValueDateSting(map, "dateHap"));
		}else{
			orc.getDateTimeOfTransaction().getTs1_TimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));
		}
		//[10]输入者
		orc.getEnteredBy(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
		orc.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
		//[12]下单方提供者
		if(null != map.get("pkEmpApp") && ("").equals(map.get("pkEmpApp"))){
			Map<String,Object> empMap=msgService.getUserCodeByPkUser(map.get("pkEmpApp").toString());
			orc.getOrderingProvider(0).getIDNumber().setValue(MsgUtils.getPropValueStr(empMap, "code"));
			orc.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(empMap, "name"));
		}
		//[17]录入科室//@todo换成科室编码或名称
		Map<String,Object> inDeptMap=msgService.getDeptInfoByPkDept(UserContext.getUser().getPkDept());
		if(inDeptMap.isEmpty()){
			orc.getEnteringOrganization().getIdentifier().setValue(MsgUtils.getPropValueStr(inDeptMap, "codeDept"));
			orc.getEnteringOrganization().getText().setValue(MsgUtils.getPropValueStr(inDeptMap, "nameDept"));
			//[21]患者科室//@todo换成科室编码或名称
			orc.getOrderingFacilityName(0).getOrganizationName().setValue(MsgUtils.getPropValueStr(inDeptMap, "codeDept"));
		}
		} catch (Exception e) {
			// TODO: handle exception
			loger.error("创建住院/门诊通用检查检验ORC消息段{}",e.getMessage());
		}
	}

	// 根据医嘱主键查询医嘱信息
	public List<Map<String, Object>> HL7SendBlOrder(List<Map<String, Object>> paramMap,String IpOpType) {
		if(IpOpType=="O"){
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : paramMap) {
				mapList.addAll(msgRecBlMapper.queryOpBlInfo(map));
			}
			HashSet h = new HashSet(mapList);   
			mapList.clear();   
			mapList.addAll(h);   
			return mapList;
		}else{
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : paramMap) {
				mapList=(msgRecBlMapper.queryIpBlInfo(map));
			}
			return mapList;
		}
	}
	
	/**
	 * 根据pkPv查询就诊编码（门诊流水号/住院流水号）
	 * @param pkPv
	 * @return
	 */
	private String  qryCodePvByPkPv(String pkPv){
		if(pkPv==null || "".equals(pkPv)) return "";
		String sql="select code_pv CODE from pv_encounter where del_flag='0' and pk_pv=?" ;
		Map<String,Object> resMap=DataBaseHelper.queryForMap(sql, pkPv);
		if(resMap!=null&& resMap.get("code")!=null){
			return resMap.get("code").toString();
		}else{
			return "";
		}
    }
	
	/**
	 * 根据药品主键或者项目主键查询相应编码
	 * @param pkPd
	 * @param pkItem
	 * @return
	 */
	private String qryItemOrPdCodeByPk(Object pkPd,Object pkItem){
		String code="";
		if(pkPd!=null && !"".equals(pkPd)){//药品
			String sql="select code from bd_pd where del_flag='0' and pk_pd=?";
			code=DataBaseHelper.queryForScalar(sql, String.class, pkPd);
		}else if(pkItem!=null && !"".equals(pkItem)){
			String sql=" select code  from bd_item where del_flag='0' and pk_item=?";
			code=DataBaseHelper.queryForScalar(sql, String.class, pkItem);
		}
		return code;
	}
}
