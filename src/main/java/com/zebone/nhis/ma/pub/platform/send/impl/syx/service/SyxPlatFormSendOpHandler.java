package com.zebone.nhis.ma.pub.platform.send.impl.syx.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.constant.EuPvtype;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.constant.EuSrvtype;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.dao.SyxPlatFormSendOpMapper;
import com.zebone.nhis.ma.pub.platform.syx.support.HIPMessageServerUtils;
import com.zebone.nhis.ma.pub.platform.syx.support.MsgUtils;
import com.zebone.nhis.ma.pub.platform.syx.support.XmlProcessUtils;
import com.zebone.nhis.ma.pub.platform.syx.vo.ControlActProcess;
import com.zebone.nhis.ma.pub.platform.syx.vo.DataEnterer;
import com.zebone.nhis.ma.pub.platform.syx.vo.EncounterEventVo;
import com.zebone.nhis.ma.pub.platform.syx.vo.Item;
import com.zebone.nhis.ma.pub.platform.syx.vo.OpCall;
import com.zebone.nhis.ma.pub.platform.syx.vo.OpCallReq;
import com.zebone.nhis.ma.pub.platform.syx.vo.OpCallReqSender;
import com.zebone.nhis.ma.pub.platform.syx.vo.OpCallRes;
import com.zebone.nhis.ma.pub.platform.syx.vo.Request;
import com.zebone.nhis.ma.pub.platform.syx.vo.ResourceSlot;
import com.zebone.nhis.ma.pub.platform.syx.vo.Response;
import com.zebone.nhis.ma.pub.platform.syx.vo.Schedule;
import com.zebone.nhis.ma.pub.platform.syx.vo.Subject;
import com.zebone.nhis.ma.pub.service.SysLogService;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
 /**
  * 门诊服务
  * @author Administrator
  *
  */
@Service
public class SyxPlatFormSendOpHandler {
	
	@Autowired
	private SyxPlatFormSendOpMapper syxPlatFormSendOpMapper;
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat  sd= new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat  sdm= new SimpleDateFormat("yyyyMMddHHmm");
	public static SimpleDateFormat  sdh= new SimpleDateFormat("yyyyMMddHH");
	
	private HIPMessageServerUtils hipMessageServerUtils = new HIPMessageServerUtils();
	
	/**
	 * 门诊挂号服务
	 * @param paramMap
	 */
	public void sendPvOpRegMsg(Map<String,Object> paramMap) {
		try {
			String isAdd = MsgUtils.getPropValueStr(paramMap, "isAdd");
			
			if (isAdd.equals("0")) {//新增
				
				String xsd = "PRPA_IN400001UV.xsd";
				pvOpRegMsg(xsd,"OutPatientInfoAdd",paramMap,true);
				
			}else if(isAdd.equals("1")){//更新
				
				String xsd = "PRPA_IN400002UV.xsd";
				pvOpRegMsg(xsd,"OutPatientInfoUpdate",paramMap,false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 *  号源排班信息发送服务
	 * @param paramMap
	 */
	public void sendPvOpNoMsg(Map<String,Object> paramMap) {		
		try {			
			String status=(String) paramMap.get("status");			
			String TXt="";
			switch (status) {
			case "add":				
				TXt=convertPvOpNoToRequestXml(paramMap, true, "PRSC_IN030101UV.xsd");
				System.out.println(TXt);
				hipMessageServerUtils.sendHIPService("SourceAndScheduleInfoAdd", TXt);
				break;
			case "update":			
				TXt=convertPvOpNoToRequestXml(paramMap,false, "PRSC_IN030102UV.xsd");
				System.out.println(TXt);
				hipMessageServerUtils.sendHIPService("SourceAndScheduleInfoUpdate", TXt);
				break;
			default:
				break;	
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}	
	
    private String convertPvOpNoToRequestXml(Map<String, Object> paramMap,boolean flagAdd,String xsd){
	    
		Request req = new Request(xsd);
		if(flagAdd){
			sourceAndScheduleInfo(req,paramMap,"PRPA_IN400003UV");
		}else{
			sourceAndScheduleInfo(req,paramMap,"PRPA_IN400002UV");
		}		
		
		return XmlProcessUtils.toRequestXml(req,xsd.substring(0, xsd.indexOf(".")));
    }	
	
	/**
	 * 门诊预约状态信息发送服务
	 * @param paramMap
	 */
	public void sendOpSchMsg(Map<String,Object> paramMap) {
		String isAdd = MsgUtils.getPropValueStr(paramMap, "isAdd");
		String action="";
		String xsd ="";
		if(isAdd.equals("0")) {  //新增操作
		    xsd = "PRPA_IN410001UV.xsd";
			action="OutPatientAppointStatusInfoAdd";
		}else if (isAdd.equals("1")) {  //更新操作
			xsd = "PRPA_IN410002UV.xsd";
			action="OutPatientAppointStatusInfoUpdate";
		}
		OpSchMsg(xsd, action, paramMap,isAdd);
	}
	
	private void pvOpRegMsg(String xsd,String action,Map<String, Object> paramMap,boolean isAdd){
		String msgContext="";
		String msgType="";
		String msgId="";
		Request req =new Request(xsd);
		OutPatientInfo(req,paramMap,isAdd,action);
		String requestXml = XmlProcessUtils.toRequestXml(req,xsd.substring(0, xsd.indexOf(".")));
		
		System.out.println(requestXml);
		
		String qryRes = hipMessageServerUtils.sendHIPService(action, requestXml);
		Response qryResObj = XmlProcessUtils.toResponseEntity(qryRes,"MCCI_IN000002UV01");
		if(qryResObj!=null&&qryResObj.getAcknowledgement()!=null&&"AA".equals(qryResObj.getAcceptAckCode().getCode())){
			
		}else {
			//消息发送失败
			String errText="";
			if(qryResObj!=null && qryResObj.getAcknowledgement()!=null){
			    errText=qryResObj.getAcknowledgement().getAcknowledgementDetail().getText().getValue();
				msgId=qryResObj.getAcknowledgement().getTargetMessage().getId().getExtension();
			}
			msgContext=hipMessageServerUtils.getSoapXML(action, requestXml);
			SysLogService.saveSysMsgRec("receive", msgContext, errText,msgType, msgId);
		}
	}
	
	/**
	 * 门诊预约处理发送业务
	 * @param xsd
	 * @param action
	 * @param paramMap
	 */
	private void OpSchMsg(String xsd,String action,Map<String, Object> paramMap,String isAdd) {
		String msgContent="";
		String msgType="";
		String msgId="";
		Request req =new Request(xsd);
		OutPatientAppointStatusInfo(req);
		String requestXml = XmlProcessUtils.toRequestXml(req,xsd.substring(0, xsd.indexOf(".")));
		
		System.out.println(requestXml);
		
		String qryRes = hipMessageServerUtils.sendHIPService(action, requestXml);
		Response qryResObj = XmlProcessUtils.toResponseEntity(qryRes,"MCCI_IN000002UV01");
		if(qryResObj!=null&&qryResObj.getAcknowledgement()!=null&&"AA".equals(qryResObj.getAcceptAckCode().getCode())){
			
		}else {
			//消息发送失败
			String errText="";
			if(qryResObj!=null && qryResObj.getAcknowledgement()!=null){
			    errText=qryResObj.getAcknowledgement().getAcknowledgementDetail().getText().getValue();
				msgId=qryResObj.getAcknowledgement().getTargetMessage().getId().getExtension();
			}
			msgContent=hipMessageServerUtils.getSoapXML(action, requestXml);
			SysLogService.saveSysMsgRec("receive", msgContent, errText,msgType, msgId);
		}
	}
		
	/**
	 * 门诊挂号信息发送服务xml文档内容
	 * @param req
	 * @param map
	 * @param isAdd
	 */
	private void OutPatientInfo(Request req,Map<String, Object> map,boolean isAdd,String action) {
		           
		String pkPv = MsgUtils.getPropValueStr(map, "pkPv");
		
		req.getId().setRoot("2.16.156.10011.2.5.1.1");
		if("OutPatientInfoAdd".equals(action)){
			req.getId().setExtension(pkPv);
		}else{
			req.getId().setExtension(NHISUUID.getKeyId());
		}
		req.getCreationTime().setValue(sdf.format(new Date()));
		req.getInteractionId().setRoot("2.16.156.10011.2.5.1.2");
		
		req.getInteractionId().setExtension("PRPA_IN400002UV");
		if(isAdd)
		req.getInteractionId().setExtension("PRPA_IN400001UV");
		req.getProcessingCode().setCode("P");
		req.getProcessingModeCode();
		req.getAcceptAckCode().setCode("AL");
		//if (isAdd)
		//req.getAcceptAckCode().setCode("AK");
		
		req.getReceiver().setTypeCode("RCV");
		req.getReceiver().getDevice().setClassCode("DEV");
		req.getReceiver().getDevice().setDeterminerCode("INSTANCE");
		req.getSender().setTypeCode("SND");
		req.getReceiver().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
		req.getReceiver().getDevice().getId().getItem().setExtension("");
		req.getSender().getDevice().setClassCode("DEV");
		req.getSender().getDevice().setDeterminerCode("INSTANCE");
		req.getSender().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
		req.getSender().getDevice().getId().getItem().setExtension(UserContext.getUser().getCodeEmp());
		req.getControlActProcess().setClassCode("STC");
		req.getControlActProcess().setMoodCode("EVN");
		req.getControlActProcess().getSubject().setTypeCode("SUBJ");
		//头
		
		Map<String, Object> pvEnt = syxPlatFormSendOpMapper.qryPvAll(pkPv);
		
		EncounterEventVo encounterEvent = req.getControlActProcess().getSubject().getEncounterEventVo();
		encounterEvent.setClassCode("ENC");
		encounterEvent.setMoodCode("EVN");
		encounterEvent.getId();
		Item item = new Item();
		item.setRoot("2.16.156.10011.1.11");
		item.setExtension(MsgUtils.getPropValueStr(pvEnt, "codeOp"));
		encounterEvent.getId().getItems().add(item);				
		Item itemNum = new Item();
		itemNum.setRoot("2.16.156.10011.2.5.1.8");
		itemNum.setExtension(MsgUtils.getPropValueStr(pvEnt, "opTimes"));
		encounterEvent.getId().getItems().add(itemNum);			
		Item itemNo = new Item();
		itemNo.setRoot("2.16.156.10011.2.5.1.9");
		itemNo.setExtension(MsgUtils.getPropValueStr(pvEnt, "codePv"));
		encounterEvent.getId().getItems().add(itemNo);			
		String euPvtype = MsgUtils.getPropValueStr(pvEnt, "euPvtype");
		String pvType = "";
		String pvTypeName = "";
		if(euPvtype.equals(EuPvtype.OUTPATIENT)){
			pvType = "1";
			pvTypeName = "门诊";
		}else if (euPvtype.equals(EuPvtype.EMERGENCY)) {
			pvType = "2";
			pvTypeName = "急诊";
		}else if (euPvtype.equals(EuPvtype.HOSPITALIZED)) {
			pvType = "3";
			pvTypeName = "住院";
		}else {
			pvType = "9";
			pvTypeName = "其它";
		}
		
		encounterEvent.getCode().setCode(pvType);
		encounterEvent.getCode().setCodeSystemName("患者类型代码表");
		encounterEvent.getCode().setCodeSystem("2.16.156.10011.2.3.1.271");
		encounterEvent.getCode().getDisplayName().setValue(pvTypeName);
		
		String dateBegin = MsgUtils.getPropValueStr(pvEnt, "dateBegin");
		
		String status = "status";
		if(!isAdd)
			status = "retired";
		encounterEvent.setStatusCode(status);
		//if(StringUtils.isNotBlank(dateClinic))
		//	dateClinic=dateClinic.replaceAll("-", "");
		//	if(dateClinic.length()>7){
		//		dateClinic.substring(0, 8);
		//	}
		encounterEvent.getEffectiveTime().getLow().setValue(dateBegin);
		encounterEvent.getReasonCode().getItem().getOriginalText().setValue("");//就诊原因，不明字段
		
		//医疗保险类型，枚举
		encounterEvent.getAdmissionReferralSourceCode().setCode(MsgUtils.getPropValueStr(pvEnt, "codeStd"));
		
		encounterEvent.getAdmissionReferralSourceCode().setCodeSystem("2.16.156.10011.2.3.1.248");
		encounterEvent.getAdmissionReferralSourceCode().setCodeSystemName("医疗保险类别代码");
		encounterEvent.getAdmissionReferralSourceCode().getDisplayName().setValue(MsgUtils.getPropValueStr(pvEnt, "nameStd"));
		
		encounterEvent.getSubject().setTypeCode("SBJ");
		encounterEvent.getSubject().getPatient().setClassCode("PAT");
		encounterEvent.getSubject().getPatient().getId().getItem().setRoot("2.16.156.10011.2.5.1.4");
		
		//encounterEvent.getSubject().getPatient().getId().getItem().setExtension(MsgUtils.getPropValueStr(pvEnt, "pkPi"));
		encounterEvent.getSubject().getPatient().getId().getItem().setExtension(MsgUtils.getPropValueStr(pvEnt, "codePi"));
		encounterEvent.getSubject().getPatient().getPatientPerson().getId().getItem().setRoot("2.16.156.10011.1.3");
		encounterEvent.getSubject().getPatient().getPatientPerson().getId().getItem().setExtension(MsgUtils.getPropValueStr(pvEnt, "idNo"));
		encounterEvent.getSubject().getPatient().getPatientPerson().getName().setXSI_TYPE("DSET_EN");
		encounterEvent.getSubject().getPatient().getPatientPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(pvEnt, "namePi"));
		
		encounterEvent.getAdmitter().setTypeCode("ADM");
		encounterEvent.getAdmitter().getAssignedPerson().setClassCode("ASSIGNED");
		encounterEvent.getAdmitter().getAssignedPerson().getId().getItem().setRoot("2.16.156.10011.1.4");
		String codeEmp = MsgUtils.getPropValueStr(pvEnt, "codeEmp");
		String nameEmp = MsgUtils.getPropValueStr(pvEnt, "nameEmp"); 
		if(!StringUtils.isNotBlank(codeEmp)){
			codeEmp = MsgUtils.getPropValueStr(pvEnt, "codeEmpPv");
			nameEmp = MsgUtils.getPropValueStr(pvEnt, "nameEmpPv");
		}
		encounterEvent.getAdmitter().getAssignedPerson().getId().getItem().setExtension(codeEmp);
		encounterEvent.getAdmitter().getAssignedPerson().getAssignedPerson().setClassCode("PSN");
		encounterEvent.getAdmitter().getAssignedPerson().getAssignedPerson().setDeterminerCode("INSTANCE");
		encounterEvent.getAdmitter().getAssignedPerson().getAssignedPerson().getName().setXSI_TYPE("DSET_EN");
		
		encounterEvent.getAdmitter().getAssignedPerson().getAssignedPerson().getName().getItem().getPart().setValue(nameEmp);
		
		encounterEvent.getLocation().setTypeCode("LOC");
		encounterEvent.getLocation().getStatusCode().setCode("active");
		encounterEvent.getLocation().getServiceDeliveryLocation().setClassCode("SDLOC");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation().setClassCode("PLC");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation().setDeterminerCode("INSTANCE");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation().getId().getItem().setRoot("2.16.156.10011.1.26");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation().getId().getItem().setExtension(MsgUtils.getPropValueStr(pvEnt, "codeDept"));
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation().getName().setXSI_TYPE("DSET_EN");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(pvEnt, "nameDept"));
		encounterEvent.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().setClassCode("ORG");
		encounterEvent.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().setDeterminerCode("INSTACE");
		encounterEvent.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId().getItem().setRoot("2.16.156.10011.1.5");
		encounterEvent.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId().getItem().setExtension(MsgUtils.getPropValueStr(pvEnt, "codeOrg"));
		
	}
	
	/**
	 * 号源排班信息发送服务xml文档内容
	 * @param req
	 * @param map
	 * @param isAdd
	 */
	private void sourceAndScheduleInfo(Request req, Map<String, Object> paramMap ,String action) {

		List<String> pkSchList = (List<String>) paramMap.get("pkSchList");
		List<Map<String,Object>> schPlan = new ArrayList<>();
		if(pkSchList.size()>0){			
			schPlan = syxPlatFormSendOpMapper.qrySchSchAll(pkSchList);
		}

		req.getId().setRoot("2.16.156.10011.2.5.1.1");
		req.getId().setExtension(NHISUUID.getKeyId());
		req.getCreationTime().setValue(sdf.format(new Date()));
		req.getInteractionId().setRoot("2.16.156.10011.2.5.1.2");
		req.getInteractionId().setExtension(action);
		req.getProcessingCode().setCode("P");
		req.getProcessingModeCode();
		req.getAcceptAckCode().setCode("AL");
		req.getReceiver().setTypeCode("RCV");
		req.getReceiver().getDevice().setClassCode("DEV");
		req.getReceiver().getDevice().setDeterminerCode("INSTANCE");
		req.getReceiver().getDevice().getId();
		req.getReceiver().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
		req.getReceiver().getDevice().getId().getItem().setExtension("192.168.8.234");//接收方待定
		req.getSender().setTypeCode("SND");
		req.getSender().getDevice().setClassCode("DEV");
		req.getSender().getDevice().setDeterminerCode("INSTANCE");
		req.getSender().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
		req.getSender().getDevice().getId().getItem().setExtension("HIS");
		
		ControlActProcess cap = req.getControlActProcess();
		cap.setClassCode("CACT");
		cap.setMoodCode("EVN");		
		
		DataEnterer dEnterer = cap.getDataEnterer();
		dEnterer.setTypeCode("ENT");
		 
		dEnterer.getAssignedPerson().setClassCode("ASSIGNED");
		dEnterer.getAssignedPerson().getId();
		dEnterer.getAssignedPerson().getId().getItem().setRoot("2.16.156.10011.1.4");		
		dEnterer.getAssignedPerson().getId().getItem().setExtension(UserContext.getUser().getCodeEmp());
		dEnterer.getAssignedPerson().getAssignedPerson().setClassCode("PSN");
		dEnterer.getAssignedPerson().getAssignedPerson().setDeterminerCode("INSTANCE");
		
		dEnterer.getAssignedPerson().getAssignedPerson().getName().setXSI_TYPE("DSET_EN");
		dEnterer.getAssignedPerson().getAssignedPerson().getName().getItem();
		dEnterer.getAssignedPerson().getAssignedPerson().getName().getItem().getPart().setValue(UserContext.getUser().getNameEmp());
		if(schPlan.size()>0) {			
			for (Map<String, Object> map : schPlan) {					
				Subject sub = cap.getSubject();
				sub.setTypeCode("SUBJ");
				Schedule sch = sub.getSchedule();
				sch.setClassCode("SCH");
				sch.setMoodCode("EVN");	
				ResourceSlot slot = sch.getResourceSlot();
				slot.setClassCode("SLOT");
				
				slot.setMoodCode("EVN");
				slot.getId();
				slot.getId().getItem().setRoot("2.16.156.10011.2.5.1.20");
				slot.getId().getItem().setExtension(MsgUtils.getPropValueStr(map, "pkSch"));
				
				slot.getProfession().setCode(MsgUtils.getPropValueStr(map, "codeDept"));
				slot.getProfession().setCodeSystem("2.16.156.10011.2.3.2.62");		
				slot.getProfession().setCodeSystemName("医疗卫生机构业务科室分类与代码表");
				slot.getProfession().getDisplayName().setValue(MsgUtils.getPropValueStr(map, "nameDept"));
				slot.getTotalNumber().setValue(MsgUtils.getPropValueStr(map, "cntTotal"));
				slot.getDeptId();
				slot.getDeptId().getItem().setRoot("2.16.156.10011.1.26");
				slot.getDeptId().getItem().setExtension(MsgUtils.getPropValueStr(map, "codeDept"));
				String euSrvtype = MsgUtils.getPropValueStr(map, "euSrvtype");
				String euSrvtypeValue = "";
				switch(euSrvtype){ 
				case "0":  euSrvtypeValue = EuSrvtype.PUBLIC; break;
				case "1":  euSrvtypeValue = EuSrvtype.PROFESSION; break;
				case "2":  euSrvtypeValue = EuSrvtype.SPECIALMEDICAL; break;
				case "9":  euSrvtypeValue = EuSrvtype.EMERGENCY; break;	       
				default:  break;
				}
				slot.getCode().setCode(MsgUtils.getPropValueStr(map, "euSrvtype"));
				slot.getCode().setCodeSystem("2.16.156.10011.2.5.1.21");
				slot.getCode().setCodeSystemName("资源级别代码");
				slot.getCode().getDisplayName().setValue(euSrvtypeValue);
				
				slot.getStatusCode();
				slot.getEffectiveTime();
				slot.getPriorityCode();
				
				slot.getDirectTarget().setTypeCode("DIR");
				slot.getDirectTarget().getIdentifiedEntity().setClassCode("CONS");
				slot.getDirectTarget().getIdentifiedEntity().getId();
				slot.getDirectTarget().getIdentifiedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
				slot.getDirectTarget().getIdentifiedEntity().getId().getItem().setExtension(MsgUtils.getPropValueStr(map, "pkEmp"));
				String jobName =  MsgUtils.getPropValueStr(map, "jobname");
				if(jobName.length()>0){					
					List<Map<String,Object>> defDoc = syxPlatFormSendOpMapper.qryDefDocAll(jobName);
					slot.getDirectTarget().getIdentifiedEntity().getCode().setCode(MsgUtils.getPropValueStr(defDoc.get(0), "codeStd"));
					slot.getDirectTarget().getIdentifiedEntity().getCode().setCodeSystem("2.16.156.10011.2.3.1.209");
					slot.getDirectTarget().getIdentifiedEntity().getCode().setCodeSystemName("专业技术职务类别代码表");					
					slot.getDirectTarget().getIdentifiedEntity().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(defDoc.get(0), "nameStd"));
				}else {
					slot.getDirectTarget().getIdentifiedEntity().getCode().setCode("");
					slot.getDirectTarget().getIdentifiedEntity().getCode().setCodeSystem("2.16.156.10011.2.3.1.209");
					slot.getDirectTarget().getIdentifiedEntity().getCode().setCodeSystemName("专业技术职务类别代码表");					
					slot.getDirectTarget().getIdentifiedEntity().getCode().getDisplayName().setValue("");
				}
				slot.getDirectTarget().getIdentifiedEntity().getPerson().setClassCode("PSN");
				slot.getDirectTarget().getIdentifiedEntity().getPerson().setDeterminerCode("INSTANCE");
				slot.getDirectTarget().getIdentifiedEntity().getPerson().getId();
				slot.getDirectTarget().getIdentifiedEntity().getPerson().getId().getItem().setRoot("2.16.156.10011.1.3");
				slot.getDirectTarget().getIdentifiedEntity().getPerson().getId().getItem().setExtension(MsgUtils.getPropValueStr(map, "idno"));
				slot.getDirectTarget().getIdentifiedEntity().getPerson().getName().setXSI_TYPE("DSET_EN");
				slot.getDirectTarget().getIdentifiedEntity().getPerson().getName().getItem();
				slot.getDirectTarget().getIdentifiedEntity().getPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(map, "nameEmp"));
				
				slot.getTimeFrame();
				slot.getTimeFrame().getCode().setCode("1");
				slot.getTimeFrame().getCode().setCodeSystem("2.16.156.10011.2.5.1.22");
				slot.getTimeFrame().getCode().setCodeSystemName("排班资源类型代码");
				
				slot.getTimeFrame().getCode().getDisplayName().setValue("自助机");
				slot.getTimeFrame().getTotalFrameNumber().setValue("20");
				slot.getTimeFrame().getEffectiveTime();
				slot.getTimeFrame().getEffectiveTime().getLow().setValue(MsgUtils.getPropValueStr(map, "beginTime"));
				slot.getTimeFrame().getEffectiveTime().getHigh().setValue(MsgUtils.getPropValueStr(map, "endTime"));
				String cntUsed = MsgUtils.getPropValueStr(map, "cntUsed");
				String cntTotal = MsgUtils.getPropValueStr(map, "cntTotal");
				int cnt = 0;
				if(cntTotal != "") {			
					cnt = Integer.parseInt(cntTotal)-Integer.parseInt(cntUsed);
					slot.getTimeFrame().getRemainNumber().setValue(cnt+"");
				}else {
					slot.getTimeFrame().getRemainNumber().setValue("");
				}
			}
		}else {
			Subject sub = cap.getSubject();
			sub.setTypeCode("SUBJ");
			Schedule sch = sub.getSchedule();
			sch.setClassCode("SCH");
			sch.setMoodCode("EVN");	
			ResourceSlot slot = sch.getResourceSlot();
			slot.setClassCode("SLOT");
			
			slot.setMoodCode("EVN");
			slot.getId();
			slot.getId().getItem().setRoot("2.16.156.10011.2.5.1.20");
			slot.getId().getItem().setExtension("");
			
			slot.getProfession().setCode("");
			slot.getProfession().setCodeSystem("2.16.156.10011.2.3.2.62");		
			slot.getProfession().setCodeSystemName("医疗卫生机构业务科室分类与代码表");
			slot.getProfession().getDisplayName().setValue("");
			slot.getTotalNumber().setValue("");
			slot.getDeptId();
			slot.getDeptId().getItem().setRoot("2.16.156.10011.1.26");
			slot.getDeptId().getItem().setExtension("");			
			slot.getCode().setCode("");
			slot.getCode().setCodeSystem("2.16.156.10011.2.5.1.21");
			slot.getCode().setCodeSystemName("资源级别代码");
			slot.getCode().getDisplayName().setValue("");
			
			slot.getStatusCode();
			slot.getEffectiveTime();
			slot.getPriorityCode();
			
			slot.getDirectTarget().setTypeCode("DIR");
			slot.getDirectTarget().getIdentifiedEntity().setClassCode("CONS");
			slot.getDirectTarget().getIdentifiedEntity().getId();
			slot.getDirectTarget().getIdentifiedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
			slot.getDirectTarget().getIdentifiedEntity().getId().getItem().setExtension("");
			slot.getDirectTarget().getIdentifiedEntity().getCode().setCode("");
			slot.getDirectTarget().getIdentifiedEntity().getCode().setCodeSystem("2.16.156.10011.2.3.1.209");
			slot.getDirectTarget().getIdentifiedEntity().getCode().setCodeSystemName("专业技术职务类别代码表");
			
			slot.getDirectTarget().getIdentifiedEntity().getCode().getDisplayName().setValue("");
			slot.getDirectTarget().getIdentifiedEntity().getPerson().setClassCode("PSN");
			slot.getDirectTarget().getIdentifiedEntity().getPerson().setDeterminerCode("INSTANCE");
			slot.getDirectTarget().getIdentifiedEntity().getPerson().getId();
			slot.getDirectTarget().getIdentifiedEntity().getPerson().getId().getItem().setRoot("2.16.156.10011.1.3");
			slot.getDirectTarget().getIdentifiedEntity().getPerson().getId().getItem().setExtension("");
			slot.getDirectTarget().getIdentifiedEntity().getPerson().getName().setXSI_TYPE("DSET_EN");
			slot.getDirectTarget().getIdentifiedEntity().getPerson().getName().getItem();
			slot.getDirectTarget().getIdentifiedEntity().getPerson().getName().getItem().getPart().setValue("");
			
			slot.getTimeFrame();
			slot.getTimeFrame().getCode().setCode("1");
			slot.getTimeFrame().getCode().setCodeSystem("2.16.156.10011.2.5.1.22");
			slot.getTimeFrame().getCode().setCodeSystemName("排班资源类型代码");
			
			slot.getTimeFrame().getCode().getDisplayName().setValue("自助机");
			slot.getTimeFrame().getTotalFrameNumber().setValue("20");
			slot.getTimeFrame().getEffectiveTime();
		
			slot.getTimeFrame().getEffectiveTime().getLow().setValue("");
			slot.getTimeFrame().getEffectiveTime().getHigh().setValue("");												
		    slot.getTimeFrame().getRemainNumber().setValue("");		
		}
				
	}
	
	/**
	 * 门诊预约状态信息发送服务xml文档内容
	 * 
	 */
	public void OutPatientAppointStatusInfo(Request req) {
		MsgUtils.createPubReq(req, req.getReqHead());
		
		req.getControlActProcess().setClassCode("INFO");
		req.getControlActProcess().setMoodCode("APT");
		
		req.getControlActProcess().getSubject().setTypeCode("SUBJ");
		req.getControlActProcess().getSubject().getEncounterAppointment();
		
		req.getControlActProcess().getSubject().getEncounterAppointment().getId();
		req.getControlActProcess().getSubject().getEncounterAppointment().getId().getItem().setRoot("2.16.156.10011.2.5.1.29");
		req.getControlActProcess().getSubject().getEncounterAppointment().getId().getItem().setExtension("22");          //预约Id
		
		req.getControlActProcess().getSubject().getEncounterAppointment().getCode();
		req.getControlActProcess().getSubject().getEncounterAppointment().getStatusCode().setCode("active");
		req.getControlActProcess().getSubject().getEncounterAppointment().getEffectiveTime();
		req.getControlActProcess().getSubject().getEncounterAppointment().getEffectiveTime().getLow().setValue("20170101120000");   //预约时间
		
		req.getControlActProcess().getSubject().getEncounterAppointment().getSubject();
		req.getControlActProcess().getSubject().getEncounterAppointment().getSubject().getPatient().setClassCode("PAT");
		req.getControlActProcess().getSubject().getEncounterAppointment().getSubject().getPatient().getId();
		
		req.getControlActProcess().getSubject().getEncounterAppointment().getSubject().getPatient().getId().getItem().setRoot("2.16.156.10011.2.5.1.4");
		req.getControlActProcess().getSubject().getEncounterAppointment().getSubject().getPatient().getId().getItem().setExtension("12345sdsdfsdfsdf");   //患者ID
		req.getControlActProcess().getSubject().getEncounterAppointment().getSubject().getPatient().getPatientPerson();
		
		req.getControlActProcess().getSubject().getEncounterAppointment().getSubject().getPatient().getPatientPerson().getId();
		req.getControlActProcess().getSubject().getEncounterAppointment().getSubject().getPatient().getPatientPerson().getId().getItem().setRoot("2.16.156.10011.1.3");
		
	    req.getControlActProcess().getSubject().getEncounterAppointment().getSubject().getPatient().getPatientPerson().getId().getItem().setExtension("120109197706015516"); //身份证号      
		req.getControlActProcess().getSubject().getEncounterAppointment().getSubject().getPatient().getPatientPerson().getName().setXSI_TYPE("LIST_EN");
		
		req.getControlActProcess().getSubject().getEncounterAppointment().getSubject().getPatient().getPatientPerson().getName().getItem();
		req.getControlActProcess().getSubject().getEncounterAppointment().getSubject().getPatient().getPatientPerson().getName().getItem().getPart().setValue("朱洋洋");   //姓名
		req.getControlActProcess().getSubject().getEncounterAppointment().getReusableDevice();
		req.getControlActProcess().getSubject().getEncounterAppointment().getReusableDevice().getTime();
		req.getControlActProcess().getSubject().getEncounterAppointment().getReusableDevice().getTime().getLow().setValue("20170101010101");  //系统预约日期时间
		
		req.getControlActProcess().getSubject().getEncounterAppointment().getReusableDevice().getAssignedDevice().setClassCode("ASSIGNED");
		req.getControlActProcess().getSubject().getEncounterAppointment().getReusableDevice().getAssignedDevice().getId();
		req.getControlActProcess().getSubject().getEncounterAppointment().getReusableDevice().getAssignedDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
		req.getControlActProcess().getSubject().getEncounterAppointment().getReusableDevice().getAssignedDevice().getId().getItem().setExtension("");  //系统id
		
		req.getControlActProcess().getSubject().getEncounterAppointment().getReusableDevice().getAssignedDevice().getCode().setCode("预约状态码");
		req.getControlActProcess().getSubject().getEncounterAppointment().getReusableDevice().getAssignedDevice().getCode().setCodeSystem("2.16.156.10011.2.5.1.30");
		req.getControlActProcess().getSubject().getEncounterAppointment().getReusableDevice().getAssignedDevice().getCode().setCodeSystemName("门诊预约状态代码表");
		req.getControlActProcess().getSubject().getEncounterAppointment().getReusableDevice().getAssignedDevice().getCode().getDisplayName().setValue("预约状态");
		
		req.getControlActProcess().getSubject().getEncounterAppointment().getPerformer().setTypeCode("PRF");
		req.getControlActProcess().getSubject().getEncounterAppointment().getPerformer().getTime();
		req.getControlActProcess().getSubject().getEncounterAppointment().getPerformer().getTime().getLow().setValue("20170101120000");  //医务人员预约日期时间
		
		req.getControlActProcess().getSubject().getEncounterAppointment().getPerformer().getAssignedPerson().setClassCode("ASSIGNED");	
		req.getControlActProcess().getSubject().getEncounterAppointment().getPerformer().getAssignedPerson().getId();
		req.getControlActProcess().getSubject().getEncounterAppointment().getPerformer().getAssignedPerson().getId().getItem().setRoot("2.16.156.10011.1.4");
		req.getControlActProcess().getSubject().getEncounterAppointment().getPerformer().getAssignedPerson().getId().getItem().setExtension("12");  //医务人员工号
		
		req.getControlActProcess().getSubject().getEncounterAppointment().getArrivedBy();
		req.getControlActProcess().getSubject().getEncounterAppointment().getArrivedBy().getTransportationIntent().setMoodCode("PRP");
		req.getControlActProcess().getSubject().getEncounterAppointment().getArrivedBy().getTransportationIntent().getId();
		req.getControlActProcess().getSubject().getEncounterAppointment().getArrivedBy().getTransportationIntent().getId().getItem().setRoot("2.16.156.10011.2.5.1.20");
		req.getControlActProcess().getSubject().getEncounterAppointment().getArrivedBy().getTransportationIntent().getId().getItem().setExtension("12");  //资源ID
		
		req.getControlActProcess().getSubject().getEncounterAppointment().getArrivedBy().getTransportationIntent().getCode();
		req.getControlActProcess().getSubject().getEncounterAppointment().getArrivedBy().getTransportationIntent().getEffectiveTime();
		req.getControlActProcess().getSubject().getEncounterAppointment().getArrivedBy().getTransportationIntent().getEffectiveTime().getLow().setValue("20170101090000");  //资源时段
		req.getControlActProcess().getSubject().getEncounterAppointment().getArrivedBy().getTransportationIntent().getEffectiveTime().getHigh().setValue("20170101120000");  //资源时段
		
	
	}

	/**
	 * 门诊推送叫号信息
	 * @param paramMap
	 */
	public String sendOpCallMsg(Map<String, Object> paramMap) {
		
		try{
			List<OpCall> list = syxPlatFormSendOpMapper.qryQueueList(paramMap);
			if(list == null || list.size() == 0){
				return "当前患者不在队列中，无法叫号！";
			}
			
			OpCallReqSender sender = new OpCallReqSender();
			sender.setSenderId("OPCalling");
			sender.setSystemName("门诊叫号系统");
			sender.setSenderId(CommonUtils.getString(paramMap.get("userId")));
			sender.setSendername(CommonUtils.getString(paramMap.get("userName")));
			
			OpCallReq req = new OpCallReq();
			req.setId("@GUID");
			req.setCreationTime(sdf.format(new Date()));
			req.setActionId("setQueueList");
			req.setActionName("推送叫号信息服务");
			req.setSender(sender);
			req.setItem(list);
			
			String xml = XmlProcessUtils.beanToXml(req, OpCallReq.class);
			System.out.println("门诊推送叫号信息服务发送请求xml:"+xml);
			
			String result = hipMessageServerUtils.sendHIPService("setQueueList", xml);
			
			System.out.println("门诊推送叫号信息服务响应xml:"+result);
			
			OpCallRes res = (OpCallRes)XmlProcessUtils.XmlToBean(result, OpCallRes.class);
			
			return res.getSubject().getErrorMsg();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
