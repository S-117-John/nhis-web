package com.zebone.nhis.ma.pub.platform.zb.receive;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.NM;
import ca.uhn.hl7v2.model.v24.datatype.TX;
import ca.uhn.hl7v2.model.v24.message.OMG_O19;
import ca.uhn.hl7v2.model.v24.message.OMP_O09;
import ca.uhn.hl7v2.model.v24.message.ORG_O20;
import ca.uhn.hl7v2.model.v24.message.ORL_O22;
import ca.uhn.hl7v2.model.v24.message.ORM_O01;
import ca.uhn.hl7v2.model.v24.message.ORR_O02;
import ca.uhn.hl7v2.model.v24.message.ORU_R01;
import ca.uhn.hl7v2.model.v24.message.OUL_R21;
import ca.uhn.hl7v2.model.v24.message.RAS_O17;
import ca.uhn.hl7v2.model.v24.message.SIU_S12;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.OBR;
import ca.uhn.hl7v2.model.v24.segment.OBX;
import ca.uhn.hl7v2.model.v24.segment.ORC;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.RXO;
import ca.uhn.hl7v2.model.v24.segment.RXR;
import ca.uhn.hl7v2.model.v24.segment.SCH;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;

import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExRisOcc;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.zb.service.SysMsgService;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.nhis.ma.pub.platform.zb.vo.BlPubParamVo;
import com.zebone.nhis.ma.pub.platform.zb.vo.OrderParam;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.web.support.ResponseJson;


/**
 * hl7??????
 * @author chengjia
 *
 */
@Service
public class MsgReceive {
	private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

	@Value("#{applicationProperties['msg.split.char']}")
	private String splitChar;

	@Resource
	private	SysMsgService msgService;	


	public String handleSysMsgRec(SysMsgRec msgRec) throws HL7Exception, ParseException{
		 int i,j;
		 Parser parser = new GenericParser();
		 String msg=msgRec.getMsgContent();
		Map<String,Object> patientMap;
		List<Map<String,Object>> patList;
		 Message hapiMsg=null;
		 try {
			 hapiMsg = parser.parse(msg);
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("parse error");
			return null;
		}
		 
		 if(hapiMsg==null){
			 log.info("hapiMsg is null");
			 return null;
		 }
		 
		 Segment segment = (Segment) hapiMsg.get("MSH");
		 MSH msh=(MSH)segment;
		 
		 String msgType = msh.getMessageType().encode();
		 
		 log.info("msgType:"+msgType);
		 
		 Date now = new Date();
		 
		 PID pid;
		 PV1 pv1;
		 ORC orc;
		 OBR obr;
		 OBX obx;
		 RXO rxo;
		 MSA msa;
		 RXR rxr;
		 
		 int len;
		 String patientId=null;
		 String patientNo=null;
		 String pbarCode=null;
		 String socialNo=null;
		 //
		 if(msgType.equals("SIU^S12")){
			 //????????????
			 log.info("SIU^S12");
			 SIU_S12 siu = (SIU_S12)hapiMsg;
			 pid =siu.getPATIENT().getPID();
			 pv1=siu.getPATIENT().getPV1();
			 List<CnOpApply> list=new ArrayList<CnOpApply>();
			 SCH sch=siu.getSCH();
			 CnOpApply rec = new CnOpApply();
			 String opDateStr=sch.getAppointmentTimingQuantity(0).getStartDateTime().getTimeOfAnEvent().getValue();
			 if(opDateStr!=null&&!opDateStr.equals("")){
				 try {
					 Date opDate=DateUtils.strToDate(opDateStr);
					//????????????
					 rec.setDateConfirm(opDate);
				} catch (Exception e) {
					// TODO: handle exception
					log.error("??????????????????");
					return null;
				}
			 }else{
				 log.info("opDateStr is null");
			 }
			 String empSn=sch.getEnteredByPerson(0).getIDNumber().getValue();
			 //@todo???????????????
			 
			 //??????????????????
			 String opId=sch.getPlacerOrderNumber(0).getEntityIdentifier().getValue();
			 if(opId==null||opId.equals("")) return null;
			 NTE nte=siu.getNTE();
			 //??????
			 String remark=nte.getComment(0).getValue();
			 Segment zopSegment =(Segment)siu.get("ZOP");
			 String opRooms  = zopSegment.getField(3, 0).encode();
			 log.info(opRooms);
			 if(splitChar==null||splitChar.equals("")) splitChar="#";
			 String[] strs=opRooms.split(splitChar);
			 if(strs.length>1){
				 //?????????@todo
				 //?????????@todo
			 }
			 
			 //opId
			 //??????
			 //?????????
			 list.add(rec);
			 //@todo??????
		 }else if(msgType.equals("ORR^O02")){
			 //?????????????????????????????????
			 ORR_O02 orr = (ORR_O02)hapiMsg;
			 msa=orr.getMSA();
			 pid=orr.getRESPONSE().getPATIENT().getPID();
			 len=orr.getRESPONSE().getORDERReps();
			 List<CnOpApply> list=new ArrayList<CnOpApply>();
			 String type="1";
			 for(i=0;i<len;i++){
				 orc = orr.getRESPONSE().getORDER(i).getORC();
				 obr = orr.getRESPONSE().getORDER(i).getOBR();
				 
				 String opId=orc.getPlacerOrderNumber().getEntityIdentifier().getValue();
				 String control=orc.getOrderControl().getValue();
				 String hisVisitNO=orc.getPlacerOrderNumber().getUniversalID().getValue();
				 String visitType=orc.getPlacerOrderNumber().getUniversalIDType().getValue();
				 if(opId==null||opId.equals("")) continue;
				 if(control==null||control.equals("")) continue;
				 //log.info(control+"-"+visitType+"-"+orderId);
				 String chargeStatus="";
				 String opStatus="0";
				 if(control.equals("OK")){
					 //??????
					 if(visitType!=null&&visitType.equals("I")){
						 chargeStatus="2";
					 }else{
						 chargeStatus="4";
					 }
				 }else if(control.equals("CR")){
					 //??????
					 chargeStatus="7";
				 }else if(control.equals("OR")){
					 //????????????
					 opStatus="2";
					 type="2";
				 }
				 CnOpApply op = new CnOpApply();
				 //@todo
				 
				 list.add(op);					 

			 }
			 if(list.size()>0){
				 //@todo update
			 }
		 }else if(msgType.equals("ORM^O01")){
			 //????????????
			 ORM_O01 orm = (ORM_O01)hapiMsg;
			 pid=orm.getPATIENT().getPID();
			 len=orm.getORDERReps();
			 List<CnOpApply> list=new ArrayList<CnOpApply>();
			 for(i=0;i<len;i++){
				 orc=orm.getORDER(i).getORC();
				 
				 String opId=orc.getPlacerOrderNumber().getEntityIdentifier().getValue();
				 String control=orc.getOrderControl().getValue();
				 String hisVisitNO=orc.getPlacerOrderNumber().getUniversalID().getValue();
				 String visitType=orc.getPlacerOrderNumber().getUniversalIDType().getValue();
				 if(opId==null||opId.equals("")) continue;
				 if(control==null||control.equals("")) continue;
				 //log.info(control+"-"+visitType+"-"+orderId);
				 String chargeStatus="";
				 String reqStatus="2";
				//????????????
				 if(control.equals("CA")){
					 CnOpApply op=new CnOpApply();
					 //@todo
					 
					 list.add(op);
				 }
			 }
			 if(list.size()>0){
				 //@todo??????
			 }
		 }else if(msgType.equals("ORL^O22")){
			//?????????????????????????????????????????????
			 ORL_O22 org = (ORL_O22)hapiMsg;
			 msa=org.getMSA();
			 
			 len=org.getRESPONSE().getPATIENT().getGENERAL_ORDERReps();
			 String orderIdStr="";
			 String pkEmp=null; 
			/*
			 * ?????????????????????????????????
			 */
			 for(i=0;i<len;i++){
				 int len2=org.getRESPONSE().getPATIENT().getGENERAL_ORDER(i).getORDERReps();
				 for(j=0;j<len2;j++){
					 orc = org.getRESPONSE().getPATIENT().getGENERAL_ORDER(i).getORDER(j).getORC();
					 String orderId=orc.getPlacerOrderNumber().getEntityIdentifier().getValue(); //??????????????????
					 String codeEmp=orc.getEnteredBy(0).getIDNumber().getValue();//????????????????????????
					 if(!StringUtils.isNotBlank(pkEmp)){
						 if(StringUtils.isNotBlank(codeEmp)){//??????lis??????????????????????????????????????????
							 Map<String, Object> bdOuMap = DataBaseHelper.queryForMap("SELECT PK_EMP FROM bd_ou_employee WHERE CODE_EMP= ?",codeEmp);
							 if(bdOuMap!=null){
								 pkEmp=MsgUtils.getPropValueStr(bdOuMap,"pkEmp");
							 } 
						 } 
					 }
					
					 obr = org.getRESPONSE().getPATIENT().getGENERAL_ORDER(i).getORDER(j).getOBSERVATION_REQUEST().getOBR();
					 String ordsn = obr.getPlacerOrderNumber().getEntityIdentifier().getValue();//???????????????
					 if(StringUtils.isBlank(ordsn)){
		        		 return "???????????????????????????"; 
		        	 }
					 Map<String, Object> ordMap = new HashMap<>();
					 ordMap.put("ordsn", ordsn);
					 //queryMap.put("codeApply", orderId);
					 List<Map<String,Object>> orderList=msgService.queryReqOrdList(ordMap);
					 
					 if(orderList.size()>0){
						 for(Map<String,Object> orderMap :orderList){
							 if(StringUtils.isNotBlank(pkEmp)){
								 orderMap.put("pkEmp",pkEmp);//??????????????????
							 }
							 if(("????????????").equals(obr.getPriority().getValue())){
								 msgService.updateLisRptList(orderMap);
							 }else if(("??????").equals(obr.getPriority().getValue())){
								 orderMap.put("liseustu","02");//??????????????????
								 msgService.updateLisRptList(orderMap);
							 }
						 }
						 
					 }
				 }
			 }
		 }else if(msgType.equals("OUL^R21")){
			//????????????????????????
			 OUL_R21 oul=(OUL_R21)hapiMsg;
			 
			 pid =oul.getPATIENT().getPID();
			 pv1=oul.getVISIT().getPV1();
			 log.info("his visit no:"+pv1.getVisitNumber().getID().getValue());
			 patientId=pid.getPatientID().getID().getValue();
			 if(StringUtils.isBlank(patientId)){
        		 return "patient_id is null"; 
        	 }
			 
			 //??????????????????
			 Map<String,Object> queryMap = new HashMap<String,Object>();
			 queryMap.put("codeIp", patientId);//@todo?????????
			 patList=msgService.queryPatList(queryMap);
			 if(patList==null||patList.size()==0){
				 log.info("???????????????????????????:"+patientId);
				 return null;
			 }else{
				 patientMap=patList.get(0);
			 }
			 //????????????
			 List<ExLabOcc> list=new ArrayList<ExLabOcc>();

			 String pkPv=patientMap.get("pkPv").toString();
			 if(pkPv==null||pkPv.equals("")) return null;
			 
			 len=oul.getORDER_OBSERVATIONReps();  //?????????????????????????????????????????????????????????PV1???????????????OBR
			 String orderIdStr="";
			 String orderIdStrs="";
			 for(i=0;i<len;i++){
				 obr=oul.getORDER_OBSERVATION(i).getOBR();
	
				 String orderId=obr.getPlacerOrderNumber().getEntityIdentifier().getValue(); //??????????????????
				 if(orderIdStr.indexOf(orderId)<0){
					 if(orderIdStr.equals("")){
						 orderIdStr="('"+orderId;
					 }else{
						 orderIdStr=orderIdStr+"','"+orderId;
					 }
				 }
			 }
			 if(!orderIdStr.equals("")) orderIdStr=orderIdStr+"')";
			 queryMap.put("codeApplys", orderIdStr);
			 String codeApplys=" and ord.ordsn in "+orderIdStr; 
			 queryMap.put("codeApplyStr", codeApplys);
			 List<Map<String,Object>> orderList=msgService.queryReqOrdList(queryMap);
			 if(orderList.size()>0){
				 for(Map<String,Object> map:orderList){
					 if(orderIdStrs.equals("")){
						 orderIdStrs="('"+map.get("codeApply");
					 }else{
						 orderIdStrs=orderIdStrs+"','"+map.get("codeApply");
					 }
				 }
			 }
			 if(!orderIdStrs.equals("")) orderIdStrs=orderIdStrs+"')";
			 
			 for(i=0;i<len;i++){
				 obr=oul.getORDER_OBSERVATION(i).getOBR();
				 
				// String clsCode=obr.getUniversalServiceIdentifier().getIdentifier().getValue(); //e.g.<???????????????ID(ST)>^<????????????????????????(ST)>^Ordinary
				// String clsName=obr.getUniversalServiceIdentifier().getText().getValue(); //e.g.<???????????????ID(ST)>^<????????????????????????(ST)>^Ordinary
				// String orderId=obr.getPlacerOrderNumber().getEntityIdentifier().getValue(); //??????????????????
				 String orderId = MsgUtils.getPropValueStr(orderList.get(i),"codeApply");//??????????????????
				 
				 int len2=oul.getORDER_OBSERVATION(i).getOBSERVATIONReps(); //????????????????????????????????????????????????????????????OBR???????????????OBX
				 for(j=0;j<len2;j++){
					 obx=oul.getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX();
					 String range=obx.getReferencesRange().getValue();
					 String range1="";
					 String range2="";
					 if(range!=null&&!range.equals("")){
						 String[] ranges=range.split("-");
						 range1="";
						 range2="";
						 if(ranges.length>1){
							 range1=ranges[0];
							 range2=ranges[1];
						 }
					 }
					 
					 TX content=null;
					 NM contents=null;
					 String valueType = obx.getValueType().getValue();
					 String codeIndex=obx.getObservationIdentifier().getIdentifier().getValue();
					 String nameIndex = obx.getObservationIdentifier().getText().getValue();
					 if(obx.getObservationValue().length>0){
						 Varies v = obx.getObservationValue()[0];
						 
						 try {
							 if(("NM").equals(valueType)){
								 contents =(NM)v.getData(); 
							 }else if(("TX").equals(valueType)){
								 content =(TX)v.getData(); 
							 }
							 //log.info(codeIndex+"---"+nameIndex+"---"+content.getValue());
						 } catch (Exception e) {
								e.getMessage();
								log.error("??????????????????!{}",e.getMessage());
								continue;
								
						 }
					 }
					 
					 //String dateStr=obx.getDateTimeOfTheObservation().getTimeOfAnEvent().getValue(); //OBX??????????????????/??????
					 ExLabOcc result = new ExLabOcc();
					 result.setPkLabocc(MsgUtils.getPk());
					 result.setPkOrg(MsgUtils.getPropValueStr(patientMap, "pkOrg"));
					 result.setPkPi(MsgUtils.getPropValueStr(patientMap, "pkPi"));
					 result.setPkPv(MsgUtils.getPropValueStr(patientMap,"pkPv"));
					 Map<String,Object> orderMap=getOrderMapByCodeApply(orderList,orderId);
					 if(orderMap!=null){
						 result.setPkCnord(MsgUtils.getPropValueStr(orderMap,"pkCnord"));
						 result.setCodeApply(MsgUtils.getPropValueStr(orderMap,"codeApply"));
						 result.setPkOrgOcc(MsgUtils.getPropValueStr(orderMap,"pkOrgExec"));
						 result.setPkDeptOcc(MsgUtils.getPropValueStr(orderMap,"pkDeptExec"));
						 result.setPkOrd(MsgUtils.getPropValueStr(orderMap, "pkOrd"));
					 }
					 try {
						 Date reportDate = DateUtils.strToDate(obr.getResultsRptStatusChngDateTime().getTimeOfAnEvent().getValue());//OBR-????????????/??????Chng-??????/??????
						 result.setDateRpt(reportDate);
						 result.setDateChk(reportDate);//@todo
					 } catch (Exception e) {
						log.error("reportDate parse error{}",e.getMessage());
						e.getMessage();
					 }
					 String pkEmp = obr.getPrincipalResultInterpreter().getOPName().getIDNumber().getValue();  //?????????????????????
					 String nameEmp = obr.getPrincipalResultInterpreter().getOPName().getFamilyName().getValue();
					 result.setPkEmpOcc(pkEmp);
					 result.setNameEmpOcc(nameEmp);
					 result.setDateOcc(new Date()); //??????????????????????????????
					 result.setFlagChk("1");
					 result.setPkEmpChk(pkEmp);
					 result.setNameEmpChk(nameEmp);
					 Integer sortNo=null;
					 String sortNoStr = obx.getSetIDOBX().getValue();
					 if(StringUtils.isNoneBlank(sortNoStr)) sortNo = Integer.valueOf(sortNoStr);					 result.setSortNo(sortNo);
					// result.setDateColl(dateColl); @todo ???????????????????????????
					 result.setCodeIndex(codeIndex);
					 result.setNameIndex(nameIndex);
					// result.setPkUnit(pkUnit); @todo ???????????????
					 CE units= obx.getUnits();
					 result.setUnit(units.getIdentifier().getValue());
					 if(null !=content){
						 result.setVal(content.getValue());
					 }else if(null != contents){
						 result.setVal(contents.getValue()); 
					 }
					 result.setValMax(range2);
					 result.setValMin(range1);
					 String mark = obx.getAbnormalFlags().getValue();
					 String euResult = StringUtils.isBlank(mark)?"":"N".equals(mark)?"0":("L".equals(mark)||"LL".equals(mark))?"-1":("H".equals(mark)||"HH".equals(mark))?"1":"";
					 result.setEuResult(euResult); //@todo ???????????????
					 result.setDelFlag("0");
					 //General service marks ?????????????????????  Ordinary??????????????? Describe???????????????  MIC???????????????
					 String gmarks=obr.getUniversalServiceIdentifier().getNameOfCodingSystem().getValue();
					 if("MIC".equals(gmarks)){ // @todo ??????????????????ex_lab_occ_bact???ex_lab_occ_bact_al
						// ExLabOccBact back = new ExLabOccBact();  //@todo ???????????????
						// ExLabOccBactAl backAll = new ExLabOccBactAl(); //@todo ???????????????
					 }
					 list.add(result);
				 }
			 }
			 if(list.size()>0){
				//@todo update
				 msgService.saveLisRptList(pkPv, orderIdStrs, list); 
			 }
		 }else if(msgType.equals("OMG^O19")){
			//????????????????????????
			 OMG_O19 omg = (OMG_O19)hapiMsg;
			 pid=omg.getPATIENT().getPID();
			 pv1=omg.getPATIENT().getPATIENT_VISIT().getPV1();
			 len=omg.getORDERReps();
			 List<CnRisApply> list=new ArrayList<CnRisApply>();
			 String type="";
			 for(i=0;i<len;i++){
				 orc=omg.getORDER(i).getORC();
				 obr=omg.getORDER(i).getOBR();
				 String orderId=orc.getPlacerOrderNumber().getEntityIdentifier().getValue();
				 String control=orc.getOrderControl().getValue();
				 String hisVisitNO=orc.getPlacerOrderNumber().getUniversalID().getValue();
				 String visitType=orc.getPlacerOrderNumber().getUniversalIDType().getValue();
				 if(orderId==null||orderId.equals("")) continue;
				 if(control==null||control.equals("")) continue;
				 //log.info(control+"-"+visitType+"-"+orderId);
				 String chargeStatus="";
				 String reqStatus="2";
				 CnRisApply order = new CnRisApply();
				 if(control.equals("OC")){
					//??????????????????
					 type="2";
					 chargeStatus="1";
					 reqStatus="4";
					 //@todo
				 }
				
				 //@todo setvalue
				
				 list.add(order);
			 }
			 if(list.size()>0){
				 //@todo update
			 }
		 }else if(msgType.equals("ORG^O20")){
			 //????????????????????????(????????????)
			 ORG_O20 org = (ORG_O20)hapiMsg;
			 msa=org.getMSA();
			 orc=org.getRESPONSE().getORDER(0).getORC();
			 String ordsns=orc.getPlacerOrderNumber().getEntityIdentifier().getValue(); //?????????????????????lb:???????????????
			 if(StringUtils.isBlank(ordsns)){
				 log.info("???????????? is null");
				 return "???????????? is null";
			 }
			 
			 len=org.getRESPONSE().getORDERReps();
			 String orderIdStr="";
			 //LB
			 String pkEmp=null; 
	
			 for(i=0;i<len;i++){
				 orc=org.getRESPONSE().getORDER(i).getORC();
				 String ordsn=orc.getPlacerOrderNumber().getEntityIdentifier().getValue(); //?????????????????????lb:???????????????
				 if(StringUtils.isBlank(ordsn)){
	        		 return "???????????????????????????"; 
	        	 }
				 String codeEmp=orc.getOrderingProvider(0).getIDNumber().getValue();//????????????????????????
				 if(!StringUtils.isNotBlank(pkEmp)){
					 if(StringUtils.isNotBlank(codeEmp)){//??????lis??????????????????????????????????????????
						 Map<String, Object> bdOuMap = DataBaseHelper.queryForMap("SELECT PK_EMP FROM bd_ou_employee WHERE CODE_EMP= ?",codeEmp);
						 if(bdOuMap!=null){
							 pkEmp=MsgUtils.getPropValueStr(bdOuMap,"pkEmp");
						 } 
					 } 
				 }
				 
				 Map<String, Object> ordMap = new HashMap<>();
				 ordMap.put("ordsn", ordsn);
				// queryMap.put("ordsn", ordsn);
				 List<Map<String,Object>> orderList=msgService.queryReqOrdList(ordMap);
				 
				 String control=orc.getOrderControl().getValue();
				 if(control==null||control.equals("")) continue;
				 
				 if(orderList.size()>0){
					 for(Map<String,Object> orderMap :orderList){
						 if(StringUtils.isNotBlank(pkEmp)){
							 orderMap.put("pkEmp",pkEmp);//??????????????????
						 }
						 if(("OR").equals(control)){//??????
							 msgService.updateRisRptList(orderMap);
						 }else if(("OC").equals(control)){
							 orderMap.put("riseustu","02");//????????????
							 msgService.updateRisRptList(orderMap);
						 }else if(("CR").equals(control)){
							 orderMap.put("riseustu","02");//????????????
							 msgService.updateRisRptList(orderMap);
						 }
					 }
					 
				 }
			 }	 
		 }else if(msgType.equals("ORU^R01")){
			 //????????????????????????
			 ORU_R01 oru=(ORU_R01)hapiMsg;
			 pid =oru.getPATIENT_RESULT().getPATIENT().getPID();
			 pv1=oru.getPATIENT_RESULT().getPATIENT().getVISIT().getPV1();
			 msh=oru.getMSH();
			 
			 patientId=pid.getPatientID().getID().getValue();
			 //pbarCode=pv1.getVisitNumber().getID().getValue();
			 if(StringUtils.isBlank(patientId)){
				 log.info("patient_id is null");
				 //patientId="000271629700";
				 return null;
			 }

			 Map<String,Object> queryMap = new HashMap<String,Object>();
			 if(StringUtils.isBlank(patientId)){
        		 return "????????????????????????"; 
        	 }
			 queryMap.put("codePi", patientId);//@todo?????????
			 //queryMap.put("codePv", pbarCode);
			 patList=msgService.queryPatList(queryMap);
			 if(patList==null||patList.size()==0){
				 log.info("???????????????????????????:{}",patientId);
				 return null;
			 }else{
				 patientMap=patList.get(0);
			 }
			 List<ExRisOcc> list=new ArrayList<ExRisOcc>();
			 String codeApplyStr="";
			 String codeApplyStrs="";
			 String pkPv=patientMap.get("pkPv").toString();
			 if(pkPv==null||pkPv.equals("")) return null;
			 
			 len=oru.getPATIENT_RESULT().getORDER_OBSERVATIONReps();
			 int k;
			 
			 for(i=0;i<len;i++){
				 orc = oru.getPATIENT_RESULT().getORDER_OBSERVATION(i).getORC();
				 obr = oru.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBR();
				 String codeApply=obr.getPlacerOrderNumber().getEntityIdentifier().getValue();
				 if(splitChar==null||splitChar.equals("")) splitChar="#";
				 String[] strs=codeApply.split(splitChar);
				 if(strs.length>1){
					 for(k=0;k<strs.length;k++){
						 String str=strs[k];
						 if(codeApplyStr.indexOf(str)<0){
							 if(codeApplyStr.equals("")){
								 codeApplyStr="('"+str;
							 }else{
								 codeApplyStr=codeApplyStr+"','"+str;
							 }
						 }
					 }
				 }else{
					 strs=new String[1];
					 strs[0]=codeApply;
					 if(codeApplyStr.indexOf(codeApply)<0){
						 if(codeApplyStr.equals("")){
							 codeApplyStr="('"+codeApply;
						 }else{
							 codeApplyStr=codeApplyStr+"','"+codeApply;
						 }
					 }
				 }
			 }
			 if(!codeApplyStr.equals("")) codeApplyStr=codeApplyStr+"')";
			 queryMap.put("codeApplys", codeApplyStr);
			 String codeApplys=" and ord.ordsn in "+codeApplyStr; 
			 queryMap.put("codeApplyStr", codeApplys);
			 List<Map<String,Object>> orderList=msgService.queryReqOrdList(queryMap);
			 if(orderList.size()>0){
				 for(Map<String,Object> map:orderList){
					 if(codeApplyStrs.equals("")){
						 codeApplyStrs="('"+map.get("codeApply");
					 }else{
						 codeApplyStrs=codeApplyStrs+"','"+map.get("codeApply");
					 }
				 }
			 }
			 if(!codeApplyStrs.equals("")) codeApplyStrs=codeApplyStrs+"')";
			 
			 for(i=0;i<len;i++){
				 orc = oru.getPATIENT_RESULT().getORDER_OBSERVATION(i).getORC();
				 obr = oru.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBR();
				 //String codeApply=obr.getPlacerOrderNumber().getEntityIdentifier().getValue();
				 String codeApply = MsgUtils.getPropValueStr(orderList.get(i),"codeApply");//??????????????????
				 
				int len2= oru.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATIONReps();
				 ExRisOcc report;
				 for(k=0;k<len2;k++){
					 report = new ExRisOcc();
					 report.setPkRisocc(MsgUtils.getPk());
					 report.setPkOrg(MsgUtils.getPropValueStr(patientMap,"pkOrg"));
					 report.setPkPi(MsgUtils.getPropValueStr(patientMap,"pkPi"));
					 report.setPkPv(MsgUtils.getPropValueStr(patientMap,"pkPv"));
					 Map<String,Object> orderMap=getOrderMapByCodeApply(orderList,codeApply);
					 if(orderMap!=null){
						 report.setPkCnord(MsgUtils.getPropValueStr(orderMap,"pkCnord"));
						 report.setCodeApply(MsgUtils.getPropValueStr(orderMap,"codeApply"));
						 report.setPkOrgOcc(MsgUtils.getPropValueStr(orderMap,"pkOrgExec"));
						 report.setPkDeptOcc(MsgUtils.getPropValueStr(orderMap,"pkDeptExec"));
						 report.setPkOrd(MsgUtils.getPropValueStr(orderMap, "pkOrd"));
					 }
					 //??????????????????
					 //??????????????????:??????????????????:???????????????????????????
					 report.setNote(msh.getSendingApplication().getNamespaceID().getValue()+":"+obr.getUniversalServiceIdentifier().getIdentifier().getValue()+":"+obr.getUniversalServiceIdentifier().getText().getValue());
					 try {
						 Date reportDate = DateUtils.strToDate(msh.getDateTimeOfMessage().getTimeOfAnEvent().getValue());
						 report.setDateRpt(reportDate);
						 report.setDateOcc(reportDate);//@todo
						 report.setDateChk(reportDate);//@todo
					 } catch (Exception e) {
						log.error("reportDate parse error");
						e.getMessage();
					 }	
					 //int lenObx=oru.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATIONReps();
					 //for(j=0;j<lenObx;j++){
						 obx = oru.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(k).getOBX();
						 log.info("obx:"+k+"-"+obx.getObservationIdentifier().getIdentifier().getValue()+"-"+obx.getObservationIdentifier().getText());
						 TX content=null;
						 if(obx.getObservationValue().length>0){ 
							 Varies v = obx.getObservationValue()[0];
							 try {
								 content =(TX)v.getData();
								 log.info(content.getValue());
							 } catch (Exception e) {
									e.printStackTrace();
									continue;
							 } 
						 }
						
						 
						 
						 String reportType=obx.getObservationIdentifier().getText().getValue();
						 if(reportType!=null&&!reportType.equals("")){
							 if(reportType.equals("????????????")||reportType.equals("????????????")){
								 if(null !=content){
									 report.setResultObj(content.getValue()); 
								 }
								 report.setFlagChk("1");
								 report.setPkEmpChk(obx.getResponsibleObserver().getIDNumber().getValue());//@todo??????pk???
								 report.setNameEmpChk(obx.getResponsibleObserver().getFamilyName().getSurname().getValue());
								 report.setEuResult(obx.getObservationResultStatus().getValue());//@todo?????????
								 report.setPkEmpOcc(obx.getResponsibleObserver().getIDNumber().getValue());//@todo
								 report.setNameEmpOcc(obx.getResponsibleObserver().getFamilyName().getSurname().getValue());//@todo 
							 }else if(reportType.equals("????????????")||reportType.equals("????????????")){
								 if(null !=content){
									 report.setResultSub(content.getValue());
								 }
								 report.setFlagChk("1");
								 report.setPkEmpChk(obx.getResponsibleObserver().getIDNumber().getValue());//@todo??????pk???
								 report.setNameEmpChk(obx.getResponsibleObserver().getFamilyName().getSurname().getValue());
								 report.setEuResult(obx.getObservationResultStatus().getValue());//@todo?????????
								 report.setPkEmpOcc(obx.getResponsibleObserver().getIDNumber().getValue());//@todo
								 report.setNameEmpOcc(obx.getResponsibleObserver().getFamilyName().getSurname().getValue());//@todo 
							 }else if(reportType.equals("????????????")){
							 }
						 }
					 //??????????????????????????????
					 report.setDateOcc(new Date());
					 list.add(report);
					 //}
			 }
			 }
			 
			 if(list.size()>0){
				 msgService.saveRisRptList(pkPv, codeApplyStrs, list);
			 }
		 }else if(msgType.equals("RAS^O17")){
			 String ControlID=msh.getMessageControlID().getValue();//??????id
			 //????????????????????????
			 RAS_O17 oul=(RAS_O17)hapiMsg;
			 pid =oul.getPATIENT().getPID();
			 pv1=oul.getPATIENT().getPATIENT_VISIT().getPV1();
			 //log.info("his visit no:"+pv1.getVisitNumber().getID().getValue());
			 //??????????????????
			 patientId=pid.getPatientID().getID().getValue();
			 String codeIp=pid.getPatientIdentifierList(1).getID().getValue();
			 //???????????????????????????
			 String codePv=pv1.getVisitNumber().getID().getValue();
			 if(StringUtils.isBlank(patientId) && StringUtils.isBlank(codeIp)){
				 log.info("patient_id is null");
				 return "????????????????????????????????????";
			 }
				 
			 //??????????????????
			 Map<String,Object> queryMap = new HashMap<String,Object>();
			 queryMap.put("codePi", patientId);
			 queryMap.put("codeIp", codeIp);
			 queryMap.put("codePv", codePv);
			 patList=msgService.queryPatList(queryMap);
			 if(patList==null||patList.size()==0){
				 log.info("???????????????????????????:{}",patientId);
				 return "???????????????????????????"+patientId;
			 }else{
				 patientMap=patList.get(0);
			 }
			 Map<String, Object> bdItemMaps = DataBaseHelper.queryForMap("select amount,pk_dept_ex,pk_cgip from bl_ip_dt where pk_ordexdt=? and pk_pi=? and pk_pv=?",new Object[]{ControlID,MsgUtils.getPropValueStr(patientMap,"pkPi"),MsgUtils.getPropValueStr(patientMap,"pkPv")});
			 if(MapUtils.isNotEmpty(bdItemMaps)){
				 return "?????????";
			 }
			 len=oul.getORDERReps();
			 List<BlPubParamVo> blParaList = new ArrayList<>();
			 User user =new User(); 
	         for (int k = 0; k < len; k++) {;
	        	 orc=oul.getORDER(k).getORC();
	        	 rxo = oul.getORDER(k).getORDER_DETAIL().getRXO();
	        	 String codeEmp=orc.getEnteredBy(0).getIDNumber().getValue();//????????????????????????
	        	 if(StringUtils.isNotBlank(codeEmp)){
	        		 user=MsgUtils.getDefaultUser(codeEmp);
	        		 if(StringUtils.isBlank(user.getPkOrg())){
	        			 return "?????????????????????";  
	        		 }
	    		     UserContext userContext = new UserContext();
	    			 userContext.setUser(user);
	        	 }else{
	        		 return "????????????????????????"; 
	        	 }
	        	 String bdCode=rxo.getRequestedGiveCode().getText().getValue();//??????????????????
	        	 String quan=rxo.getRequestedGiveAmountMinimum().getValue();//??????
	        	 String codeDept=orc.getEnteringOrganization().getIdentifier().getValue();//??????????????????
	        	 String pkDept;
	        	//????????????//@todo???????????????????????????
	 			Map<String,Object> inDeptMap=msgService.getDeptInfoByPkDepts(codeDept);
	 			if(MapUtils.isNotEmpty(inDeptMap)){
	 				pkDept=MsgUtils.getPropValueStr(inDeptMap,"pkDept");
	 			}else{
	 				pkDept=user.getPkDept();
	 			}
	        	 if(StringUtils.isBlank(bdCode)){
	        		 return "??????????????????????????????"; 
	        	 }
	        	 if(StringUtils.isBlank(quan)){
	        		 return "??????????????????"; 
	        	 }
	        	 Map<String, Object> bdItemMap = DataBaseHelper.queryForMap("select pk_item from bd_item where del_flag='0' and code=?",bdCode);
	        	 if(MapUtils.isEmpty(bdItemMap)){
	        		 List<Map<String, Object>> bdItemList = DataBaseHelper.queryForList("select item.pk_item from bd_item item left join BD_ORD_ITEM orite on orite.pk_item=item.pk_item left join bd_ord ord on ord.pk_ord=orite.pk_ord  where ord.code=?",bdCode);
		        	 if(bdItemList.size()>0){
		        		 for(Map<String, Object> map:bdItemList){
		        			 map.put("pkOrg", user.getPkOrg());//????????????
		        			 map.put("pkPi", MsgUtils.getPropValueStr(patientMap,"pkPi"));//????????????
		        			 map.put("pkPv", MsgUtils.getPropValueStr(patientMap,"pkPv"));//??????????????????
		        			 map.put("euPvtype", MsgUtils.getPropValueStr(patientMap,"euPvtype"));//????????????
		        			 map.put("pkDept", pkDept);//????????????
		        			 map.put("pkEmpInput", user.getPkEmp());//????????????
		        			 map.put("nameEmpInput", user.getNameEmp());//????????????
		        			 map.put("pkEmpCg", user.getPkEmp());
		        			 map.put("nameEmpCg", user.getNameEmp());
		        			 map.put("pkOrgEcex", user.getPkOrg());//????????????
		        			 map.put("pkDeptExec", pkDept);//????????????
		        			 map.put("quan", quan);//??????
		        			 map.put("nameItemset",ControlID);//????????????id??????????????????????????????????????????
				        	 blParaList.add(getCgVo(map));  
		        		 }
		        	 }else{
		        		 return "??????????????????????????????";
		        	 }
	        	 }else{
	        		 bdItemMap.put("pkOrg", user.getPkOrg());//????????????
		        	 bdItemMap.put("pkPi", MsgUtils.getPropValueStr(patientMap,"pkPi"));//????????????
		        	 bdItemMap.put("pkPv", MsgUtils.getPropValueStr(patientMap,"pkPv"));//??????????????????
		        	 bdItemMap.put("euPvtype", MsgUtils.getPropValueStr(patientMap,"euPvtype"));//????????????
		        	 bdItemMap.put("pkDept", pkDept);//????????????
		        	 bdItemMap.put("pkEmpInput", user.getPkEmp());//????????????
		        	 bdItemMap.put("nameEmpInput", user.getNameEmp());//????????????
		        	 bdItemMap.put("pkEmpCg", user.getPkEmp());
		        	 bdItemMap.put("nameEmpCg", user.getNameEmp());
		        	 bdItemMap.put("pkOrgEcex", user.getPkOrg());//????????????
		        	 bdItemMap.put("pkDeptExec", pkDept);//????????????
		        	 bdItemMap.put("quan", quan);//??????
		        	 bdItemMap.put("nameItemset",ControlID);//????????????id??????????????????????????????????????????
		        	 blParaList.add(getCgVo(bdItemMap)); 
	        	 }
			}
			//??????????????????
			ResponseJson  NowSjh =  ApplicationUtils.execService("BL", "IpCgPubService", "savePatiCgInfo",blParaList,user);
			if(NowSjh.getStatus()==0){
				return "OK";
			}else{
				return NowSjh.getDesc();
			}
		 } else if(msgType.equals("OMP^O09")){
			 
			 String ControlID=msh.getMessageControlID().getValue();//??????id
			 //????????????????????????
			 OMP_O09 omp=(OMP_O09)hapiMsg;
			 //?????????????????????????????????
			 pid =omp.getPATIENT().getPID();
			 //???????????????????????????
			 pv1=omp.getPATIENT().getPATIENT_VISIT().getPV1();
			 //log.info("his visit no:"+pv1.getVisitNumber().getID().getValue());
			 //??????????????????
			 patientId=pid.getPatientID().getID().getValue();
			 //?????????????????????
			 String codeIp=pid.getPatientIdentifierList(1).getID().getValue();
			 //???????????????????????????
			 String codePv=pv1.getVisitNumber().getID().getValue();
			 //????????????
			 String euPvtype=pv1.getPatientClass().getValue();
			 //??????????????????????????????
			 if(StringUtils.isBlank(patientId) && StringUtils.isBlank(codeIp)){
				 log.info("patient_id is null");
				 return "????????????????????????????????????";
			 }
				 
			 //??????????????????map????????????????????????
			 Map<String,Object> queryMap = new HashMap<String,Object>();
			 queryMap.put("codePi", patientId);
			 queryMap.put("codeIp", codeIp);
			 queryMap.put("codePv", codePv);
			 if("I".equals(euPvtype)){
				 queryMap.put("euStatus", "1"); 
			 }
			 //????????????????????????????????????
			 patList=msgService.queryPatList(queryMap);
			 if(patList==null||patList.size()==0){
				 log.info("???????????????????????????:{}",patientId);
				 return "???????????????????????????"+patientId;
			 }else{
				 patientMap=patList.get(0);
			 }
			 
			 if(!"3".equals(MsgUtils.getPropValueStr(patientMap,"euPvtype"))){
				 return "?????????????????????"; 
			 }
			 
			 /**
			  * ????????????
			  * ??????O09???????????????????????????????????????
			  * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
			  */
			 
			 User user =new User();
			 //??????????????????
			 OrderParam orderParam=new OrderParam();
			 List<CnOrder>  changeOrdList = new ArrayList<CnOrder>();
			 
			 len=omp.getORDERReps();
			 for (int k = 0; k < len; k++) {
				 orc=omp.getORDER(k).getORC();
	        	 rxo = omp.getORDER(k).getRXO();
	        	 rxr = omp.getORDER(k).getRXR();
	        	 //??????????????????
	        	 String ordsn=orc.getPlacerOrderNumber().getEntityIdentifier().getValue();
	        	 //??????????????????
	        	 String OrderStatus = orc.getOrderStatus().getValue();
	        	 //????????????
	        	 String DateTime = orc.getOrderEffectiveDateTime().getTimeOfAnEvent().getValue();
	        	 if(StringUtils.isBlank(DateTime)){
	        		 return "???????????????"; 
	        	 }
	        	 //???????????????
	        	 String codeEmp= orc.getEnteredBy(0).getIDNumber().getValue();
	        	 //???????????????F?????????L??????
	        	 String flagFirst = orc.getEnteringDevice().getIdentifier().getValue();
	        	 //????????????
	        	 String num = orc.getEnteringDevice().getText().getValue();
	        	 //???????????????0?????????       1?????????
	        	 String euAlways = orc.getOrderStatusModifier().getIdentifier().getValue();
	        	//????????????????????? ???1?????? 2?????? 3???????????????????????? 4?????? 5?????? 6??????
				//his??????????????? ???0 ?????????1 ?????????2 ?????????3 ?????????4 ?????????9 ??????
	        	 if("5".equals(OrderStatus) && "1".equals(euAlways)){
	        		 //????????????????????????
	        		 if(StringUtils.isNotBlank(codeEmp)){
		        		 user=MsgUtils.getDefaultUser(codeEmp);
		        		 if(StringUtils.isBlank(user.getPkOrg())){
		        			 return "?????????????????????";  
		        		 }
		    		     UserContext userContext = new UserContext();
		    			 userContext.setUser(user);
		        	 }else{
		        		 return "????????????????????????"; 
		        	 }
	        		 
	        		 CnOrder ord = DataBaseHelper.queryForBean("select * from cn_order where flag_stop='0' and flag_erase='0' and eu_status_ord='3'  and ordsn_parent=? and PK_PV=?",CnOrder.class,ordsn,MsgUtils.getPropValueStr(patientMap,"pkPv"));
	        		 if(ord!=null){
	        			//????????????
		        		 ord.setFlagStop("1");
		        		 //????????????
		        		 ord.setPkEmpStop(user.getPkEmp());
		        		 //??????????????????
		        		 ord.setNameEmpStop(user.getNameEmp());
		        		 //??????????????????
		        		 ord.setDateStop(DateUtils.strToDate(DateTime));
		        		 //??????????????????
		        		 if("L".equals(flagFirst)){
		        			 ord.setLastNum(Long.valueOf(num)); 
		        		 }else{
		        			 ord.setLastNum(Long.valueOf("0")); 
		        		 }
		        		 
		        		 changeOrdList.add(ord);  
	        		 }
	        		 
	        	 }else if("6".equals(OrderStatus) && "0".equals(euAlways)){
	        		 
	        	 }
			}
			 
			 //??????????????????
			if(changeOrdList.size()>0){
				orderParam.setChangeOrdList(changeOrdList);
	     		//1:??????????????????????????????1??????????????????
	  			orderParam.setFlagCancleStop("-2");
				
				//????????????
		 		ResponseJson  NowSjh =  ApplicationUtils.execService("CN", "CnOrderService", "stopOrder",orderParam,user);
		 		if(NowSjh.getStatus()==0){
		 			return "OK";
		 		}else{
		 			return NowSjh.getDesc();
		 		}
			}else{
				return "??????????????????????????????"; 
			}
			
		 }	 
		 return "";
	}
	

	private BlPubParamVo getCgVo(Map<String, Object> orderMap){
		
		BlPubParamVo cgParam = new BlPubParamVo();
		cgParam.setPkOrdexdt(MsgUtils.getPropValueStr(orderMap,"nameItemset"));//????????????id??????????????????????????????????????????
		cgParam.setPkOrg(MsgUtils.getPropValueStr(orderMap,"pkOrg"));
		cgParam.setPkItem(MsgUtils.getPropValueStr(orderMap,"pkItem"));//????????????
		cgParam.setPkCnord(MsgUtils.getPropValueStr(orderMap,"pkCnord"));//????????????
		cgParam.setPkPi(MsgUtils.getPropValueStr(orderMap,"pkPi"));//????????????
		cgParam.setPkPv(MsgUtils.getPropValueStr(orderMap,"pkPv"));//??????????????????
		cgParam.setEuPvType(MsgUtils.getPropValueStr(orderMap,"euPvtype"));//????????????
		cgParam.setFlagPd("0");
		cgParam.setFlagPv("0");
		cgParam.setCodeOrdtype(MsgUtils.getPropValueStr(orderMap,"codeOrdtype"));
		cgParam.setDateStart((Date)orderMap.get("dateStart"));
		cgParam.setInfantNo(MsgUtils.getPropValueStr(orderMap,"infantNo"));//????????????
		cgParam.setDateHap(new Date());
		cgParam.setPkOrgApp(cgParam.getPkOrg());
		cgParam.setPkDeptApp(MsgUtils.getPropValueStr(orderMap,"pkDept"));//????????????
		cgParam.setPkDeptNsApp(MsgUtils.getPropValueStr(orderMap,"pkDeptNs"));
		cgParam.setPkEmpApp(MsgUtils.getPropValueStr(orderMap,"pkEmpInput"));
		cgParam.setNameEmpApp(MsgUtils.getPropValueStr(orderMap,"nameEmpInput"));
		cgParam.setPkOrgEx(MsgUtils.getPropValueStr(orderMap,"pkOrgEcex"));
		cgParam.setPkDeptEx(MsgUtils.getPropValueStr(orderMap,"pkDeptExec"));
		cgParam.setFlagFit(MsgUtils.getPropValueStr(orderMap,"flagFit"));
	    cgParam.setPkEmpCg(MsgUtils.getPropValueStr(orderMap,"pkEmpCg")); 
		cgParam.setNameEmpCg(MsgUtils.getPropValueStr(orderMap,"nameEmpCg"));
		cgParam.setPkDeptCg(cgParam.getPkDeptEx());
		String price = "0.00" ;  //????????????
		cgParam.setPrice(Double.parseDouble(price));
		Double quan = 1.00;
        Double quanCg = 1.00; 
		String quanStr  = MsgUtils.getPropValueStr(orderMap,"quan");
		if(StringUtils.isNotBlank(quanStr)) quan = Double.valueOf(quanStr);
		String quanCgStr  = MsgUtils.getPropValueStr(orderMap,"quanCg");
		if(StringUtils.isNotBlank(quanCgStr)) quanCg = Double.valueOf(quanCgStr);
		cgParam.setQuanCg("3".equals(cgParam.getEuPvType())?quan:quanCg); 
		//User u = new User();
		//u.setPkOrg(cgParam.getPkOrg()); //????????????
		//u.setPkEmp("~"); //@todo ?????????
		//UserContext.setUser(u);
		return cgParam;
	}
	
	@Autowired
	private SqlSessionFactory sessionFactory;
	/**
	 * ????????????????????????
	 */
	private List<RefundVo> getRtnCgVo(String pkCnord){
		String cgsql = "select pk_org,pk_dept_ex, pk_org_ex,pk_cgip,quan from bl_ip_dt where pk_cnord = ? and del_flag='0' ";
		List<Map<String, Object>> cglist = DataBaseHelper.queryForList(cgsql, pkCnord);
		if(cglist==null||cglist.size()<=0) 
			return null;
	    List<RefundVo> rtnlist = new ArrayList<RefundVo>();
	    for(Map<String, Object> cgmap:cglist){
	 		RefundVo vo = new RefundVo();
			vo.setPkCgip(CommonUtils.getString(cgmap.get("pkCgip")));
			vo.setPkDept(CommonUtils.getString(cgmap.get("pkDeptEx"))); //@todo ????????????????????????????????????????????????????????????
			vo.setPkOrg(CommonUtils.getString(cgmap.get("pkOrgEx"))); ////@todo ???????????? ????????????????????????????????????????????????
			vo.setPkEmp("~"); //@todo ????????????????????????
			vo.setNameEmp("~"); //@todo ??????????????????????????????
			vo.setQuanRe(CommonUtils.getDouble(cgmap.get("quan")));
			rtnlist.add(vo);
	    }
		User u = new User();
		u.setPkOrg(CommonUtils.getString(cglist.get(0).get("pkOrg"))); //????????????
		u.setPkEmp("~"); //@todo ??????????????????
		u.setNameEmp("~"); //@todo ?????????
		u.setPkDept(CommonUtils.getString(cglist.get(0).get("pkDeptEx")));
		u.setPkOrg(CommonUtils.getString(cglist.get(0).get("pkOrgEx")));
		UserContext.setUser(u);
		return rtnlist;
   }
	/**
	 * ????????????????????????
	 */
	private List<BlOpDt> getRtnOpCgVo(String pkCnord){
		String cgsql = "select * from bl_op_dt where del_flag = '0' and  pk_cnord = ?  ";
		List<BlOpDt> cgBacklist = DataBaseHelper.queryForList(cgsql, BlOpDt.class, new Object[]{pkCnord});
		validBlopdtCreateRefundParam(cgBacklist);
        return cgBacklist;
   }
	private void validBlopdtCreateRefundParam(List<BlOpDt> blOpDts) {
		if (blOpDts == null || blOpDts.size() == 0) {
			throw new BusException("?????????????????????????????????????????????");
		}
		String pkSettle = blOpDts.get(0).getPkSettle();
		for (BlOpDt blOpDt : blOpDts) {
			blOpDt.setCanBack(blOpDt.getQuan());
			blOpDt.setQuanBack(blOpDt.getQuan());
			if (StringUtils.isNoneBlank(pkSettle)&&!pkSettle.equals(blOpDt.getPkSettle())) {
				throw new BusException("??????????????????????????????????????????????????????");
			}
		}
	}
	
	public Map<String,Object> getOrderMapByCodeApply(List<Map<String,Object>> listMap,String codeApply){
		if(listMap==null||listMap.size()==0) return null;
		for (Map<String, Object> map : listMap) {
			Object obj=map.get("codeApply");
			if(obj!=null&&obj.toString().equals(codeApply)) return map;
		}
		return null;
	}
	

}
