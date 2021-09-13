package com.zebone.nhis.ma.pub.platform.send.impl.syx.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.pub.service.BdPubService;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.constant.EuPvtype;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.constant.EuPvtypeValue;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.constant.IsAdd;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.dao.SyxPlatFormSendAdtMapper;
import com.zebone.nhis.ma.pub.platform.syx.support.HIPMessageServerUtils;
import com.zebone.nhis.ma.pub.platform.syx.support.MsgUtils;
import com.zebone.nhis.ma.pub.platform.syx.support.XmlProcessUtils;
import com.zebone.nhis.ma.pub.platform.syx.vo.AssignedPerson;
import com.zebone.nhis.ma.pub.platform.syx.vo.Author;
import com.zebone.nhis.ma.pub.platform.syx.vo.Code;
import com.zebone.nhis.ma.pub.platform.syx.vo.ControlActProcess;
import com.zebone.nhis.ma.pub.platform.syx.vo.DepartedBy;
import com.zebone.nhis.ma.pub.platform.syx.vo.Discharger;
import com.zebone.nhis.ma.pub.platform.syx.vo.EncounterEvent;
import com.zebone.nhis.ma.pub.platform.syx.vo.EncounterEventVo;
import com.zebone.nhis.ma.pub.platform.syx.vo.Id;
import com.zebone.nhis.ma.pub.platform.syx.vo.Item;
import com.zebone.nhis.ma.pub.platform.syx.vo.LocatedEntity;
import com.zebone.nhis.ma.pub.platform.syx.vo.LocatedEntityHasParts;
import com.zebone.nhis.ma.pub.platform.syx.vo.LocatedPlace;
import com.zebone.nhis.ma.pub.platform.syx.vo.Location;
import com.zebone.nhis.ma.pub.platform.syx.vo.Location1;
import com.zebone.nhis.ma.pub.platform.syx.vo.Location2;
import com.zebone.nhis.ma.pub.platform.syx.vo.ObservationDx;
import com.zebone.nhis.ma.pub.platform.syx.vo.Part;
import com.zebone.nhis.ma.pub.platform.syx.vo.Patient;
import com.zebone.nhis.ma.pub.platform.syx.vo.PatientPerson;
import com.zebone.nhis.ma.pub.platform.syx.vo.Reason;
import com.zebone.nhis.ma.pub.platform.syx.vo.Request;
import com.zebone.nhis.ma.pub.platform.syx.vo.Response;
import com.zebone.nhis.ma.pub.platform.syx.vo.ServiceDeliveryLocation;
import com.zebone.nhis.ma.pub.platform.syx.vo.Subject;
import com.zebone.nhis.ma.pub.service.SysLogService;
import com.zebone.nhis.pi.pub.dao.EmpiClientSyxMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;

/**
 * 发送ADT领域消息
 * 
 * @author 杨雪
 * 
 */
@Service
public class SyxPlatFormSendAdtHandler {

	// private static Configuration config = new Configuration();

	@Autowired
	private SyxPlatFormSendAdtMapper syxPlatFormSendAdtMapper;
	@Autowired
	private BdPubService bdPubService;
	@Autowired
	private EmpiClientSyxMapper empiClientSyxMapper;
	@Autowired
	private SyxPlatFormSaveTabService syxPlatFormSaveTabService;

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat sdm = new SimpleDateFormat("yyyyMMddHHmm");

	private HIPMessageServerUtils hipMessageServerUtils = new HIPMessageServerUtils();

	/***
	 * 入院就诊信息
	 * 
	 * @param paramMap
	 */
	public void sendPvInMsg(Map<String, Object> paramMap) {
		String headType = "";
		String action = "";
		Request req = null;
		try {
			String status = MsgUtils.getPropValueStr(paramMap, "STATUS");
			if (status.equals("_UPDATE")||MsgUtils.getPropValueStr(paramMap, "isAdd").equals(IsAdd.UPDATE)) {// 更新
//				String pkPi = MsgUtils.getPropValueStr(paramMap, "pkPi");
//				if(StringUtils.isNotBlank(pkPi)){
//					paramMap.put("pkPv", syxPlatFormSendAdtMapper.qryPkPv(pkPi));
//				}
				headType = "PRPA_IN400002UV";
				action = "InPatientInfoUpdate";
				req = new Request(headType + ".xsd");
				createPatientInfoAdd(req, paramMap, false,action);
			} else {// 注册
				headType = "PRPA_IN400001UV";
				action = "InPatientInfoAdd";
				req = new Request(headType + ".xsd");
				createPatientInfoAdd(req, paramMap, true,action);
			}
			paramMap.put("action", action);
			paramMap.put("headType", headType);
			syxPlatFormSaveTabService.saveData(req, paramMap);
			//hipMessageServerUtils.sendHIPMsg(req, action, "MCCI_IN000002UV01",
					//false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/*
	 *  发送出院登记信息
	 * 
	 */
	public void sendPvOutMsg(Map<String, Object> paramMap){
		try {			
			String status=(String) paramMap.get("status");
			String TXt="";
			Request req;
			switch (status) {
			case "add":
				//TXt=convertPvOutToRequestXml(paramMap, true, "PRPA_IN400003UV.xsd");
				//hipMessageServerUtils.sendHIPService("DischargeInfoAdd", TXt);
				req = new Request("PRPA_IN400003UV.xsd");
				req = createPvOutMsg(req,paramMap,"PRPA_IN400003UV");
				paramMap.put("action", "DischargeInfoAdd");
				paramMap.put("headType", "PRPA_IN400003UV");
				syxPlatFormSaveTabService.saveData(req, paramMap);
				break;
			case "update":			
//				TXt=convertPvOutToRequestXml(paramMap,false, "PRPA_IN400002UV.xsd");
//				hipMessageServerUtils.sendHIPService("DischargeInfoUpdate", TXt);
				req = new Request("PRPA_IN400002UV.xsd");
				req = createPvOutMsg(req,paramMap,"PRPA_IN400002UV");
				paramMap.put("action", "DischargeInfoUpdate");
				paramMap.put("headType", "PRPA_IN400002UV");
				syxPlatFormSaveTabService.saveData(req, paramMap);
				break;
			default:
				break;	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    private String convertPvOutToRequestXml(Map<String, Object> paramMap,boolean flagAdd,String xsd){
	    
		Request req = new Request(xsd);
		if(flagAdd){
			createPvOutMsg(req,paramMap,"PRPA_IN400003UV");
		}else{
			createPvOutMsg(req,paramMap,"PRPA_IN400002UV");
		}
		//公共属性设置
		//req.getId().setRoot("2.16.156.10011.2.5.1.1");
		//翻译字典类字段方法，BdPubService.getBdDefDocStd
		
		return XmlProcessUtils.toRequestXml(req,xsd.substring(0, xsd.indexOf(".")));
    }	

	/**
	 * 就诊卡信息
	 * 
	 * @param paramMap
	 */
	public void sendPvInfoMsg(Map<String, Object> paramMap) {
		try {
			String msgContext = "";
			String msgType = "";
			String msgId = "";

			String isAdd = MsgUtils.getPropValueStr(paramMap, "isAdd");
			if (isAdd.equals("0")) {
				Request req = new Request("PRPA_IN201311UV02.xsd");
				getEncounterCardInfo(req, paramMap, true);
				String requestXml = XmlProcessUtils.toRequestXml(req,
						"PRPA_IN201311UV02.xsd".substring(0,
								"PRPA_IN201311UV02.xsd".indexOf(".")));
				System.err.println(requestXml);
				String qryRes = hipMessageServerUtils.sendHIPService(
						"EncounterCardInfoAdd", requestXml);
				
				Response qryResObj = XmlProcessUtils.toResponseEntity(qryRes,
						"MCCI_IN000002UV01");
				if (qryResObj != null && qryResObj.getAcknowledgement() != null
						&& "AA".equals(qryResObj.getAcceptAckCode().getCode())) {

				} else {
					// 消息发送失败
					String errText = "";
					if (qryResObj != null
							&& qryResObj.getAcknowledgement() != null) {
						errText = qryResObj.getAcknowledgement()
								.getAcknowledgementDetail().getText()
								.getValue();
						msgId = qryResObj.getAcknowledgement()
								.getTargetMessage().getId().getExtension();
					}
					msgContext = hipMessageServerUtils.getSoapXML(
							"EncounterCardInfoAdd", requestXml);
					SysLogService.saveSysMsgRec("receive", msgContext, errText,
							msgType, msgId);
				}
			} else if (isAdd.equals("1")) {
				
				Request req = new Request("PRPA_IN201314UV02.xsd");
				getEncounterCardInfo(req, paramMap, false);
				String requestXml = XmlProcessUtils.toRequestXml(req,
						"PRPA_IN201314UV02.xsd".substring(0,
								"PRPA_IN201314UV02.xsd".indexOf(".")));
				System.err.println(requestXml);
				String qryRes = hipMessageServerUtils.sendHIPService(
						"EncounterCardInfoUpdate", requestXml);
				Response qryResObj = XmlProcessUtils.toResponseEntity(qryRes,
						"MCCI_IN000002UV01");
				if (qryResObj != null && qryResObj.getAcknowledgement() != null
						&& "AA".equals(qryResObj.getAcceptAckCode().getCode())) {

				} else {
					// 消息发送失败
					String errText = "";
					if (qryResObj != null
							&& qryResObj.getAcknowledgement() != null) {
						errText = qryResObj.getAcknowledgement()
								.getAcknowledgementDetail().getText()
								.getValue();
						msgId = qryResObj.getAcknowledgement()
								.getTargetMessage().getId().getExtension();
					}
					msgContext = hipMessageServerUtils.getSoapXML(
							"EncounterCardInfoUpdate", requestXml);
					SysLogService.saveSysMsgRec("receive", msgContext, errText,
							msgType, msgId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 入院就诊信息拼接xml实体
	 * @param req
	 * @param map
	 * @param isAdd
	 */
	private void createPatientInfoAdd(Request req, Map<String, Object> map,
			boolean isAdd,String action) {
		String pkPv = MsgUtils.getPropValueStr(map, "pkPv");
		Map<String, Object> pvEn = syxPlatFormSendAdtMapper.qryPvAll(pkPv);
		
		req.getId().setRoot("2.16.156.10011.2.5.1.1");
		/*if("InPatientInfoAdd".equals(action)){
			req.getId().setExtension(pkPv);
		}else{
			req.getId().setExtension(NHISUUID.getKeyId());
		}*/
		req.getId().setExtension(NHISUUID.getKeyId());
		req.getCreationTime().setValue(sdf.format(new Date()));
		req.getInteractionId().setRoot("2.16.156.10011.2.5.1.2");
		req.getInteractionId().setExtension("PRPA_IN400002UV");
		if(isAdd)
			req.getInteractionId().setExtension("PRPA_IN400001UV");
		req.getProcessingCode().setCode("P");
		req.getProcessingModeCode();
		req.getAcceptAckCode().setCode("AL");
		// if (isAdd)
		//req.getAcceptAckCode().setCode("AK");

		req.getReceiver().setTypeCode("RCV");
		req.getReceiver().getDevice().setClassCode("DEV");
		req.getReceiver().getDevice().setDeterminerCode("INSTANCE");
		req.getSender().setTypeCode("SND");
		req.getReceiver().getDevice().getId().getItem()
				.setRoot("2.16.156.10011.2.5.1.3");
		req.getReceiver().getDevice().getId().getItem().setExtension("192.168.8.234");
		req.getSender().getDevice().setClassCode("DEV");
		req.getSender().getDevice().setDeterminerCode("INSTANCE");
		req.getSender().getDevice().getId().getItem()
				.setRoot("2.16.156.10011.2.5.1.3");
		req.getSender().getDevice().getId().getItem()
				.setExtension("HIS");
		req.getControlActProcess().setClassCode("STC");
		req.getControlActProcess().setMoodCode("EVN");
		req.getControlActProcess().getSubject().setTypeCode("SUBJ");

		EncounterEventVo encounterEvent = req.getControlActProcess().getSubject()
				.getEncounterEventVo();
		encounterEvent.setClassCode("ENC");
		encounterEvent.setMoodCode("EVN");
		encounterEvent.getId();
		Item item = new Item();
		item.setRoot("2.16.156.10011.1.12");
		item.setExtension(MsgUtils.getPropValueStr(pvEn, "codeIp"));
		encounterEvent.getId().getItems().add(item);

		Item itemNum = new Item();
		itemNum.setRoot("2.16.156.10011.2.5.1.8");
		itemNum.setExtension(MsgUtils.getPropValueStr(pvEn, "ipTimes"));
		encounterEvent.getId().getItems().add(itemNum);

		Item itemNo = new Item();
		itemNo.setRoot("2.16.156.10011.2.5.1.9");
		itemNo.setExtension(MsgUtils.getPropValueStr(pvEn, "codePv"));
		encounterEvent.getId().getItems().add(itemNo);

		String euPvtype = MsgUtils.getPropValueStr(pvEn, "euPvtype");
		String pvType = "";
		String pvTypeName = "";
		if (euPvtype.equals(EuPvtype.OUTPATIENT)) {
			pvType = "1";
			pvTypeName = "门诊";
		} else if (euPvtype.equals(EuPvtype.EMERGENCY)) {
			pvType = "2";
			pvTypeName = "急诊";
		} else if (euPvtype.equals(EuPvtype.HOSPITALIZED)) {
			pvType = "3";
			pvTypeName = "住院";
		} else {
			pvType = "9";
			pvTypeName = "其它";
		}

		encounterEvent.getCode().setCode(pvType);
		encounterEvent.getCode().setCodeSystemName("患者类型代码表");
		encounterEvent.getCode().setCodeSystem("2.16.156.10011.2.3.1.271");
		encounterEvent.getCode().getDisplayName().setValue(pvTypeName);
		String status = "active";
		if(!isAdd)
			status = "update";
		if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(map, "status")))
			status = "cancel";
		encounterEvent.setStatusCode(status);
		String propValueStr = MsgUtils.getPropValueStr(pvEn, "dateBegin");
		propValueStr = propValueStr.replaceAll("[^0-9]", "");
		
		encounterEvent.getEffectiveTime().getLow().setValue(propValueStr.substring(0, 8));
		encounterEvent.getReasonCode().getItem().getOriginalText().setValue("");
		
		// 医疗保险类型，枚举
		encounterEvent.getAdmissionReferralSourceCode().setCode(MsgUtils.getPropValueStr(pvEn, "hpcode"));
		encounterEvent.getAdmissionReferralSourceCode().setCodeSystem(
				"2.16.156.10011.2.3.1.248");
		encounterEvent.getAdmissionReferralSourceCode().setCodeSystemName("医疗保险类别代码");
		encounterEvent.getAdmissionReferralSourceCode().getDisplayName().setValue(MsgUtils.getPropValueStr(pvEn, "hpname"));

		encounterEvent.getLengthOfStayQuantity().setUnit("次");
		String ipTimes = MsgUtils.getPropValueStr(pvEn, "ipTimes");
		if(!StringUtils.isNotBlank(ipTimes))
			ipTimes = "0";
		encounterEvent.getLengthOfStayQuantity().setValue(ipTimes);

		encounterEvent.getSubject().setTypeCode("SBJ");
		encounterEvent.getSubject().getPatient().setClassCode("PAT");
		encounterEvent.getSubject().getPatient().getId().getItem()
				.setRoot("2.16.156.10011.2.5.1.4");

		/*encounterEvent.getSubject().getPatient().getId().getItem()
				.setExtension(MsgUtils.getPropValueStr(pvEn, "pkPi"));*/
		encounterEvent.getSubject().getPatient().getId().getItem()
		.setExtension(MsgUtils.getPropValueStr(pvEn, "codePi"));
		encounterEvent.getSubject().getPatient().getPatientPerson().getId()
				.getItem().setRoot("2.16.156.10011.1.3");
		encounterEvent.getSubject().getPatient().getPatientPerson().getId()
				.getItem().setExtension(MsgUtils.getPropValueStr(pvEn, "idNo"));
		encounterEvent.getSubject().getPatient().getPatientPerson().getName()
				.setXSI_TYPE("DSET_EN");
		encounterEvent.getSubject().getPatient().getPatientPerson().getName()
				.getItem().getPart()
				.setValue(MsgUtils.getPropValueStr(pvEn, "namePi"));

		encounterEvent.getAdmitter().setTypeCode("ADM");
		encounterEvent.getAdmitter().getTime();
		encounterEvent.getAdmitter().getAssignedPerson()
				.setClassCode("ASSIGNED");
		
		encounterEvent.getAdmitter().getAssignedPerson().getId().getItem()
				.setRoot("2.16.156.10011.1.4");
		encounterEvent.getAdmitter().getAssignedPerson().getId().getItem()
				.setExtension(MsgUtils.getPropValueStr(pvEn, "codeEmp"));
		
		encounterEvent.getAdmitter().getAssignedPerson().getAssignedPerson()
				.setClassCode("PSN");
		encounterEvent.getAdmitter().getAssignedPerson().getAssignedPerson()
				.setDeterminerCode("INSTANCE");
		encounterEvent.getAdmitter().getAssignedPerson().getAssignedPerson()
				.getName().setXSI_TYPE("DSET_EN");
		encounterEvent.getAdmitter().getAssignedPerson().getAssignedPerson()
				.getName().getItem().getPart()
				.setValue(MsgUtils.getPropValueStr(pvEn, "nameEmpTre"));

		encounterEvent.getLocation().setTypeCode("LOC");
		encounterEvent.getLocation().getTime();
		
		encounterEvent.getLocation().getStatusCode().setCode("active");
		encounterEvent.getLocation().getServiceDeliveryLocation()
				.setClassCode("SDLOC");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.setClassCode("PLC");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.setDeterminerCode("INSTANCE");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getId().getItem().setRoot("2.16.156.10011.1.26");

		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getId().getItem()
				.setExtension(MsgUtils.getPropValueStr(pvEn, "codeDept"));
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getName().setXSI_TYPE("DSET_EN");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getName().getItem().getPart()
				.setValue(MsgUtils.getPropValueStr(pvEn, "nameDept"));

		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().setClassCode("LOCE");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.setClassCode("PLC");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.setDeterminerCode("INSTANCE");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace().getId().getItem()
				.setRoot("2.16.156.10011.1.27");

		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace().getId().getItem()
				.setExtension(MsgUtils.getPropValueStr(pvEn, "codeDeptNs"));
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace().getName()
				.setXSI_TYPE("DSET_EN");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace().getName()
				.getItem().getPart().setValue(MsgUtils.getPropValueStr(pvEn, "nameDeptNs"));
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().setClassCode("LOCE");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace()
				.setClassCode("PLC");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace()
				.setDeterminerCode("INSTANCE");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace().getId().getItem()
				.setRoot("2.16.156.10011.1.21");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace().getId().getItem()
				.setExtension(MsgUtils.getPropValueStr(pvEn, "houseno"));
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace().getName()
				.setXSI_TYPE("DSET_EN");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace().getName()
				.getItem().getPart().setValue(MsgUtils.getPropValueStr(pvEn, "houseno"));

		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().setClassCode("LOCE");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace()
				.setClassCode("PLC");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace()
				.setDeterminerCode("INSTANCE");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace().getId().getItem()
				.setRoot("2.16.156.10011.1.22");
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace().getId().getItem()
				.setExtension(MsgUtils.getPropValueStr(pvEn, "bedNo"));
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace()
				.getLocatedEntityHasParts().getLocatedPlace().getName()
				.setXSI_TYPE("DSET_EN");
		
		encounterEvent.getLocation().getServiceDeliveryLocation().getLocation()
		.getLocatedEntityHasParts().getLocatedPlace()
		.getLocatedEntityHasParts().getLocatedPlace()
		.getLocatedEntityHasParts().getLocatedPlace().getName()
		.getItem().getPart().setValue(MsgUtils.getPropValueStr(pvEn, "bedName"));
				
		encounterEvent.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization()
				.setClassCode("ORG");
		encounterEvent.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization()
				.setDeterminerCode("INSTACE");
		encounterEvent.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId()
				.getItem().setRoot("2.16.156.10011.1.5");
		encounterEvent.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId()
				.getItem()
				.setExtension(MsgUtils.getPropValueStr(pvEn, "codeOrg"));

		List<Map<String, Object>> diags = syxPlatFormSendAdtMapper
				.qryDiag(pkPv);
		
		List<Reason> reasons = encounterEvent.getReasons();
		if (diags != null && diags.size() > 0) {
			
			List<String> icds = new ArrayList<String>();
			for (Map<String, Object> diag : diags) {
				String codeIcd = MsgUtils.getPropValueStr(diag, "codeIcd");
				if(StringUtils.isNotBlank(codeIcd)){
					String[] splitCodeIcds = codeIcd.split(" ");
					if(splitCodeIcds !=null &&splitCodeIcds.length>0){
						for (String icd : splitCodeIcds) {
							if(StringUtils.isNotBlank(icd)){
								icds.add(icd);
							}
						}
					}
					
				}
			}
				
			List<Map<String,Object>> icdInfos = syxPlatFormSendAdtMapper.qryIcd(icds);
			if(icdInfos!=null && icdInfos.size()>0){
				for (Map<String, Object> icdInfo : icdInfos) {
					if("01".equals(MsgUtils.getPropValueStr(icdInfo, "code")))
						continue;
				Reason reason = new Reason();
				reason.setTypeCode("RSON");
				reason.getObservationDx().setClassCode("OBS");
				reason.getObservationDx().setMoodCode("EVN");
				reason.getObservationDx().getValue()
						.setCode(MsgUtils.getPropValueStr(icdInfo, "diagcode"));
				String codeSystem = "";
				String codeSystemName = "";
				switch (MsgUtils.getPropValueStr(icdInfo, "code")) {
				case "00":codeSystem = "2.16.156.10011.2.3.3.11";codeSystemName = "ICD-10";
					break;
				case "02":codeSystem = "2.16.156.10011.2.3.3.14";codeSystemName = "中医病证分类与代码表（GB/T 15657）";
				break;
				}
				reason.getObservationDx().getValue()
						.setCodeSystem(codeSystem);
				reason.getObservationDx()
						.getValue()
						.setCodeSystemName(
								codeSystemName);
				reason.getObservationDx().getValue().getDisplayName()
						.setValue(MsgUtils.getPropValueStr(icdInfo, "diagname"));
				reason.getObservationDx().getAuthor().setTypeCode("AUT");
				reason.getObservationDx().getAuthor().getAssignedEntity()
						.setClassCode("ASSIGNED");
				reason.getObservationDx().getAuthor().getAssignedEntity()
						.getId().getItem().setRoot("2.16.156.10011.1.4");
				reason.getObservationDx()
						.getAuthor()
						.getAssignedEntity()
						.getId()
						.getItem()
						.setExtension(UserContext.getUser().getCodeEmp());
				reasons.add(reason);
				}
			}
		}
		
		if(reasons.size()<1){
			Reason reason = new Reason();
			reason.setTypeCode("RSON");
			reason.getObservationDx().getCode();
			reason.getObservationDx().getStatusCode();
			reason.getObservationDx().setClassCode("OBS");
			reason.getObservationDx().setMoodCode("EVN");
			reason.getObservationDx().getValue()
					.setCode("");
			String codeSystem = "";
			String codeSystemName = "";
			
			reason.getObservationDx().getValue()
					.setCodeSystem(codeSystem);
			reason.getObservationDx()
					.getValue()
					.setCodeSystemName(
							codeSystemName);
			reason.getObservationDx().getValue().getDisplayName()
					.setValue("");
			reason.getObservationDx().getAuthor().setTypeCode("AUT");
			reason.getObservationDx().getAuthor().getAssignedEntity()
					.setClassCode("ASSIGNED");
			reason.getObservationDx().getAuthor().getAssignedEntity()
					.getId().getItem().setRoot("2.16.156.10011.1.4");
			reason.getObservationDx()
					.getAuthor()
					.getAssignedEntity()
					.getId()
					.getItem()
					.setExtension(UserContext.getUser().getCodeEmp());
			reasons.add(reason);
		}
	}

	/**
	 * 发送患者转科消息
	 * 
	 * @param paramMap
	 */
	public void sendPvAdtMsg(Map<String, Object> paramMap) {
		if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(paramMap, "sendMsg")))
			return;
		String headType = "";
		String action = "";
		Request req = new Request();
		boolean status = (boolean) paramMap.get("status");
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		try {
			if (paramMap.get("adtType") != null
					&& paramMap.get("adtType").equals("入院")) {// 入科
			} else if (paramMap.get("adtType") != null
					&& paramMap.get("adtType").equals("新出生")) {// 婴儿登记
			} else if (paramMap.get("adtType") != null
					&& paramMap.get("adtType").equals("转科")) {// 转科
				if(status) {
					action = "TransferInfoAdd";
					headType = "PRPA_IN302011UV";					
				}else {
					action = "TransferInfoUpdate";
					headType = "PRPA_IN302012UV";	
				}

				resList = (List<Map<String, Object>>) paramMap.get("resList");
			}
			req = createPvAdtMsg(headType, resList);
			if (resList == null || resList.size() <= 0)
				return;
//			hipMessageServerUtils.sendHIPMsg(req, action, "MCCI_IN000002UV01",
//					false);
			//改成存表模式
			paramMap.put("action", action);
			paramMap.put("headType", headType);
			syxPlatFormSaveTabService.saveData(req, paramMap);
		} catch (Exception e) {
		}
	}

	/**
	 * 创建转科信息
	 * 
	 * @param action
	 * @param paramMap
	 * @return
	 */
	private Request createPvAdtMsg(String action,
			List<Map<String, Object>> resList) {
		Request req = new Request(action + ".xsd");
		//req = MsgUtils.createPubReq(req, action);
		Map<String, Object> result = resList.get(0);
		
		req.getId().setRoot("2.16.156.10011.2.5.1.1");
		/*if("PRPA_IN302011UV".equals(action)){
			req.getId().setExtension(MsgUtils.getPropValueStr(result, "pkAdt"));
		}else{
			req.getId().setExtension(NHISUUID.getKeyId());		
		}*/
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

		ControlActProcess con = req.getControlActProcess();
		con.setClassCode("STC");
		con.setMoodCode("APT");
		List<Subject> subjects = con.getSubjects();
		for (Map<String, Object> map : resList) {
			Subject sub = new Subject();
			sub.setTypeCode("SUBJ");

			EncounterEvent ence = sub.getEncounterEvent();
			ence.setClassCode("ENC");
			ence.setMoodCode("EVN");

			ence.getId();
			Id id = ence.getId();
			List<Item> items = id.getItems();
			Item item1 = new Item();									
			item1.setRoot("2.16.156.10011.1.12");
		    item1.setExtension(MsgUtils.getPropValueStr(map, "codeIp"));// 住院号				    
			Item item2 = new Item();			
			item2.setRoot("2.16.156.10011.2.5.1.8");
			item2.setExtension(MsgUtils.getPropValueStr(map, "ipTimes"));// 就诊次数			
			Item item3 = new Item();			
			item3.setRoot("2.16.156.10011.2.5.1.9");
			item3.setExtension(MsgUtils.getPropValueStr(map, "codePv"));// 就诊流水号
			items.add(item1);
			items.add(item2);
			items.add(item3);

			Subject sub1 = ence.getSubject();
			sub1.setTypeCode("SBJ");
			Patient pat = sub1.getPatient();
			pat.setClassCode("PAT");
			pat.getId().getItem().setRoot("2.16.156.10011.2.5.1.4");
			/*pat.getId().getItem()
					.setExtension(MsgUtils.getPropValueStr(map, "pkPi"));*/		
			pat.getId().getItem()
			.setExtension(MsgUtils.getPropValueStr(map, "codePi"));
			//<!--转入科室信息-->
			Location1 loc = ence.getLocation1();
			loc.setTypeCode("DST");
			loc.getTime().setXSI_TYPE("IVL_TS");
			loc.getTime()
					.getLow()
					.setValue(
							sdm.format(MsgUtils.getPropValueDate(map,
									"dateBegin")));// 转入时间
			loc.getStatusCode();
			ServiceDeliveryLocation seloc = loc.getServiceDeliveryLocation();
			seloc.setClassCode("SDLOC");
			Location seloc1 = seloc.getLocation();
			seloc1.setClassCode("PLC");
			seloc1.setDeterminerCode("INSTANCE");
			seloc1.getId().getItem().setRoot("2.16.156.10011.1.26");////转入科室标识
			seloc1.getId().getItem()
					.setExtension(MsgUtils.getPropValueStr(map, "codeDept"));//转入科室名称编码
			seloc1.getName().setXSI_TYPE("DSET_EN");
			seloc1.getName().getItem().getPart()
					.setValue(MsgUtils.getPropValueStr(map, "nameDept"));//转入科室名称
			seloc1.getLocatedEntityHasParts().setClassCode("LOCE");
			LocatedPlace locp = seloc1.getLocatedEntityHasParts()
					.getLocatedPlace();
			locp.setClassCode("PLC");
			locp.setDeterminerCode("INSTANCE");
			locp.getId().setRoot("2.16.156.10011.1.27"); 
			locp.getId().setExtension(
					MsgUtils.getPropValueStr(map, "codeDeptNs"));//转入病房标识
			locp.getName().setXSI_TYPE("DSET_EN");
			locp.getName().getItem().getPart()
					.setValue(MsgUtils.getPropValueStr(map, "nameDeptNs"));//转入病区描述
			LocatedEntityHasParts locpa = locp.getLocatedEntityHasParts();
			locpa.setClassCode("LOCE");
			LocatedPlace loce = locpa.getLocatedPlace();
			loce.setClassCode("PLC");
			loce.setDeterminerCode("INSTANCE");
			loce.getId().getItem().setRoot("2.16.156.10011.1.21");
			loce.getId().getItem()
					.setExtension(MsgUtils.getPropValueStr(map, "putHouseno"));//转入病床标识
			loce.getName().setXSI_TYPE("DSET_EN");
			loce.getName().getItem().getPart()
					.setValue(MsgUtils.getPropValueStr(map, ""));//转入病房描述（穿空）

			LocatedEntityHasParts lopat = loce.getLocatedEntityHasParts();
			lopat.setClassCode("LOCE");
			LocatedPlace lpce = lopat.getLocatedPlace();
			lpce.setClassCode("PLC");
			lpce.setDeterminerCode("INSTANCE");
			lpce.getId().getItem().setRoot("2.16.156.10011.1.22");
			lpce.getId().getItem()
					.setExtension(MsgUtils.getPropValueStr(map, "putBedCode"));//转入病床名称
			lpce.getName().setXSI_TYPE("DSET_EN");
			lpce.getName().getItem().getPart()
					.setValue(MsgUtils.getPropValueStr(map, "putBedName"));//转入病床名称
			//   <!--转出科室信息-->
			Location2 loc2 = ence.getLocation2();
			loc2.setTypeCode("LOC");
			loc2.getTime().setXSI_TYPE("IVL_TS");
			loc2.getTime()
					.getLow()
					.setValue(
							sdm.format(MsgUtils
									.getPropValueDate(map, "dateEnd")));// 转出时间
			loc2.getStatusCode();
			ServiceDeliveryLocation seloc2 = loc2.getServiceDeliveryLocation();
			seloc2.setClassCode("SDLOC");
			Location seloc12 = seloc2.getLocation();
			seloc12.setClassCode("PLC");
			seloc12.setDeterminerCode("INSTANCE");
			seloc12.getId().getItem().setRoot("2.16.156.10011.1.26");
			seloc12.getId().getItem()
					.setExtension(MsgUtils.getPropValueStr(map, "outCodeDept"));
			seloc12.getName().setXSI_TYPE("DSET_EN");
			seloc12.getName().getItem().getPart()
					.setValue(MsgUtils.getPropValueStr(map, "outNameDept"));
			seloc12.getLocatedEntityHasParts().setClassCode("LOCE");
			LocatedPlace locp2 = seloc12.getLocatedEntityHasParts()
					.getLocatedPlace();
			locp2.setClassCode("PLC");
			locp2.setDeterminerCode("INSTANCE");
			locp2.getId().setRoot("2.16.156.10011.1.27");
			locp2.getId().setExtension(
					MsgUtils.getPropValueStr(map, "outNsCodeDept"));
			locp2.getName().setXSI_TYPE("DSET_EN");
			locp2.getName().getItem().getPart()
					.setValue(MsgUtils.getPropValueStr(map, "outNsNameDept"));
			LocatedEntityHasParts locpa2 = locp2.getLocatedEntityHasParts();
			locpa2.setClassCode("LOCE");
			LocatedPlace loce2 = locpa2.getLocatedPlace();
			loce2.setClassCode("PLC");
			loce2.setDeterminerCode("INSTANCE");
			loce2.getId().getItem().setRoot("2.16.156.10011.1.21");
			loce2.getId().getItem()
					.setExtension(MsgUtils.getPropValueStr(map, "outHouseno"));//转出病房标识
			loce2.getName().setXSI_TYPE("DSET_EN");
			loce2.getName().getItem().getPart()
					.setValue(MsgUtils.getPropValueStr(map, ""));//转出病房描述(不存在)

			LocatedEntityHasParts lopat2 = loce2.getLocatedEntityHasParts();
			lopat2.setClassCode("LOCE");
			LocatedPlace lpce2 = lopat2.getLocatedPlace();
			lpce2.setClassCode("PLC");
			lpce2.setDeterminerCode("INSTANCE");
			lpce2.getId().getItem().setRoot("2.16.156.10011.1.22");
			lpce2.getId().getItem()
					.setExtension(MsgUtils.getPropValueStr(map, "outBedCode"));//转出病床标识(病床编码）
			lpce2.getName().setXSI_TYPE("DSET_EN");
			lpce2.getName().getItem().getPart()
					.setValue(MsgUtils.getPropValueStr(map, "outBedName"));//转出病床描述（病床名称）
			subjects.add(sub);
		}

		return req;
	}
	
	/**
	 * 就诊卡信息拼接xml实体
	 * @param req
	 * @param map
	 * @param isAdd
	 */
	private void getEncounterCardInfo(Request req, Map<String, Object> map,
			boolean isAdd) {
		req.getId().setRoot("2.16.156.10011.2.5.1.1");
		req.getId().setExtension(NHISUUID.getKeyId());
		req.getCreationTime().setValue(sdf.format(new Date()));
		req.getInteractionId().setRoot("2.16.156.10011.2.5.1.2");
		req.getInteractionId().setExtension("PRPA_IN201314UV02");
		if(isAdd)
			req.getInteractionId().setExtension("PRPA_IN201311UV02");
		req.getProcessingCode().setCode("P");
		req.getAcceptAckCode().setCode("AL");
		// if (isAdd)
		//req.getAcceptAckCode().setCode("AK");

		req.getReceiver().setTypeCode("RCV");
		req.getReceiver().getDevice().setClassCode("DEV");
		req.getReceiver().getDevice().setDeterminerCode("INSTANCE");
		req.getSender().setTypeCode("SND");
		req.getReceiver().getDevice().getId().getItem()
				.setRoot("2.16.156.10011.2.5.1.3");
		req.getReceiver().getDevice().getId().getItem().setExtension("192.168.8.234");
		req.getSender().getDevice().setClassCode("DEV");
		req.getSender().getDevice().setDeterminerCode("INSTANCE");
		req.getSender().getDevice().getId().getItem()
				.setRoot("2.16.156.10011.2.5.1.3");
		req.getSender().getDevice().getId().getItem()
				.setExtension("HIS");
		req.getControlActProcess().setClassCode("CACT");
		req.getControlActProcess().setMoodCode("EVN");
		req.getControlActProcess().getSubject().setTypeCode("SUBJ");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.setClassCode("REG");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.setMoodCode("RQO");
		req.getControlActProcess().getSubject().getRegistrationRequest()
		.getStatusCode();
		
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().setTypeCode("SBJ");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().setClassCode("PAT");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getId().getItem()
				.setRoot("2.16.156.10011.2.5.1.6");
		List<Map<String, Object>> pis = syxPlatFormSendAdtMapper
				.qryPiInfo(MsgUtils.getPropValueStr(map, "pkPi"));
				
				Map<String, Object> piMap = new HashMap<String, Object>();
				if(pis!=null && pis.size()>0){
					piMap = pis.get(0);
				}
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getId().getItem()
				.setExtension(MsgUtils.getPropValueStr(map, "pkPi")+"^"+ MsgUtils.getPropValueStr(piMap,"cardNo"));
		String flagActive = "active";
		if(MsgUtils.getPropValueStr(piMap, "flagActive").equals("0"))
			flagActive = "diable";
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getStatusCode().setCode(flagActive);
		
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getEffectiveTime()
				.getLow().setValue(sdf.format(new Date()));
		
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getPatientPerson()
				.getId().getItem().setRoot("2.16.156.10011.1.3");
		
		
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getPatientPerson()
				.getId().getItem().setExtension(MsgUtils.getPropValueStr(piMap, "idNo"));
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getPatientPerson()
				.getName().setXSI_TYPE("DSET_EN");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getPatientPerson()
				.getName().getItem().getPart()
				.setValue(MsgUtils.getPropValueStr(piMap, "namePi"));
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getPatientPerson()
				.getTelecom().setXSI_TYPE("DSET_TEL");
		String telNo = MsgUtils.getPropValueStr(piMap, "telNo");
		if(!StringUtils.isNotBlank(telNo))
			telNo = MsgUtils.getPropValueStr(piMap, "mobile");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getPatientPerson()
				.getTelecom().getItem().setValue(telNo);
		BdDefdoc docSexType = new BdDefdoc();
		if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(piMap,"dtSex")))
			docSexType = bdPubService.getBdDefDocStd(
				MsgUtils.getPropValueStr(piMap,"dtSex"), "000000");

		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getPatientPerson()
				.getAdministrativeGenderCode().setCode(docSexType.getCodeStd());
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getPatientPerson()
				.getAdministrativeGenderCode()
				.setCodeSystem("2.16.156.10011.2.3.3.4");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getPatientPerson()
				.getAdministrativeGenderCode()
				.setCodeSystemName("生理性别代码表(GB/T 2261.1)");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getPatientPerson()
				.getAdministrativeGenderCode().getDisplayName()
				.setValue(docSexType.getNameStd());
		String birthTime = "";
		if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(piMap, "birthDate"))){
			birthTime = MsgUtils.getPropValueStr(piMap, "birthDate").replaceAll("-", "");
			if(StringUtils.isNotBlank(birthTime) &&birthTime.length()>=7){
				birthTime = birthTime.substring(0,7);
			}
		}
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getPatientPerson()
				.getBirthTime().setValue(birthTime);
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getPatientPerson()
				.getAddr().setXSI_TYPE("LIST_AD");
		List<Part> parts = req.getControlActProcess().getSubject()
				.getRegistrationRequest().getSubject1().getPatient()
				.getPatientPerson().getAddr().getItem()
				.getParts();
		
		Map<String, Object> addr = empiClientSyxMapper.getAddr(MsgUtils.getPropValueStr(piMap, "addrcodeCur"));
		
		Part part = new Part();
		part.setType("SAL");
		part.setValue(MsgUtils.getPropValueStr(piMap, "addrCur")+MsgUtils.getPropValueStr(piMap, "addrCurDt"));
    	//20
    	Part partPro = new Part();
    	partPro.setType("STA");
    	partPro.setValue(MsgUtils.getPropValueStr(addr, "prov"));
    	parts.add(partPro);
    	
    	Part partCity = new Part();
    	partCity.setType("CTY");
    	partCity.setValue(MsgUtils.getPropValueStr(addr, "city"));
    	parts.add(partCity);
    	
    	Part partDis = new Part();
    	partDis.setType("CNT");
    	partDis.setValue(MsgUtils.getPropValueStr(addr, "dist"));
    	parts.add(partDis);
    	
    	Part partVil = new Part();
    	partVil.setType("STB");
    	partVil.setValue(MsgUtils.getPropValueStr(addr, "nameDiv"));
    	parts.add(partVil);

		String addBnr = "";
		int subLen = (MsgUtils.getPropValueStr(piMap, "addrCurDt")).length();
		if (StringUtils.isNotBlank(MsgUtils.getPropValueStr(piMap, "addrCurDt"))
				&& (MsgUtils.getPropValueStr(piMap, "addrCurDt")).length() > 1
				&& (MsgUtils.getPropValueStr(piMap, "addrCurDt")).charAt((MsgUtils.getPropValueStr(piMap, "addrCurDt")).length() - 1) == '号') {
			Pattern p = Pattern.compile("[0-9]");
			for (int i = (MsgUtils.getPropValueStr(piMap, "addrCurDt")).length() - 1; i >= 1; i--) {
				Matcher m = p.matcher((MsgUtils.getPropValueStr(piMap, "addrCurDt"))
						.charAt(i) + "");
				Matcher m2 = p.matcher((MsgUtils.getPropValueStr(piMap, "addrCurDt"))
						.charAt(i - 1) + "");
				if (m.find() && !m2.find()) {
					subLen = i;
					break;
				}
			}

		}

		Part partHam = new Part();
		partHam.setType("STR");
		partHam.setValue((MsgUtils.getPropValueStr(piMap, "addrCurDt"))
				.substring(0, subLen));
		parts.add(partHam);

		Part partMark = new Part();
		partMark.setType("BNR");
		partMark.setValue((MsgUtils.getPropValueStr(piMap, "addrCurDt")).substring(subLen));
		parts.add(partMark);

		String postCode = "";
		if (piMap.get("postcodeCur") != null)
			postCode = (String) piMap.get("postcodeCur");
		Part partZip = new Part();
		partZip.setType("ZIP");
		partZip.setValue(postCode);
		parts.add(partZip);
		
		PatientPerson patientPerson = req.getControlActProcess().getSubject()
				.getRegistrationRequest().getSubject1().getPatient()
				.getPatientPerson();

		BdDefdoc docMarry =  new BdDefdoc();
		if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(piMap, "dtMarry")))
			docMarry=bdPubService.getBdDefDocStd(
					MsgUtils.getPropValueStr(piMap, "dtMarry"), "000006");
		// 婚姻状况
		patientPerson.getMaritalStatusCode().setCode(docMarry.getCodeStd());
		patientPerson.getMaritalStatusCode().setCodeSystem(
				"2.16.156.10011.2.3.3.5");
		patientPerson.getMaritalStatusCode().setCodeSystemName("婚姻状况代码表(GB/T 2261.2)");

		patientPerson.getMaritalStatusCode().getDisplayName()
				.setValue(docMarry.getNameStd());
		
		BdDefdoc docNat =  new BdDefdoc();
		if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(piMap, "dtNation")))
		// 查询民族
			docNat  = bdPubService.getBdDefDocStd(
					MsgUtils.getPropValueStr(piMap, "dtNation"), "000003");

		patientPerson.getEthnicGroupCode().getItem()
				.setCode(docNat.getCodeStd());

		patientPerson.getEthnicGroupCode().getItem()
				.setCodeSystem("2.16.156.10011.2.3.3.3");
		
		patientPerson.getEthnicGroupCode().getItem()
		.setCodeSystemName("民族类别代码表(GB 3304)");

		patientPerson.getEthnicGroupCode().getItem().getDisplayName()
				.setValue(docNat.getNameStd());

		// 查询职业分类
		BdDefdoc docOccu = new BdDefdoc();
		if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(piMap, "dtOccu")))
			docOccu = bdPubService.getBdDefDocStd(
						MsgUtils.getPropValueStr(piMap, "dtOccu"), "000010");
		// 40
		patientPerson.getAsEmployee().setClassCode("EMP");
		patientPerson.getAsEmployee().getOccupationCode()
				.setCode(docOccu.getCodeStd());

		patientPerson.getAsEmployee().getOccupationCode()
				.setCodeSystem("2.16.156.10011.2.3.3.7");
		
		patientPerson.getAsEmployee().getOccupationCode()
		.setCodeSystemName("职业类别代码表(GB/T 6565)");

		patientPerson.getAsEmployee().getOccupationCode().getDisplayName()
				.setValue(docOccu.getNameStd());

		// 工作单位
		patientPerson.getAsEmployee().getEmployerOrganization().getName()
				.getItem().getPart().setValue(MsgUtils.getPropValueStr(piMap, "unitWork"));
		patientPerson.getAsEmployee().getEmployerOrganization()
				.setClassCode("ORG");
		patientPerson.getAsEmployee().getEmployerOrganization()
				.setDeterminerCode("INSTANCE");
		patientPerson.getAsEmployee().getEmployerOrganization().getName()
				.setXSI_TYPE("DSET_EN");
		patientPerson.getAsEmployee().getEmployerOrganization().getName()
				.getItem().getPart().setValue(MsgUtils.getPropValueStr(piMap, "unitWork"));
		patientPerson.getAsEmployee().getEmployerOrganization()
				.getContactParty().setClassCode("CON");
		patientPerson.getAsEmployee().getEmployerOrganization()
				.getContactParty().getTelecom().setXSI_TYPE("BAG_TEL");
		patientPerson.getAsEmployee().getEmployerOrganization()
				.getContactParty().getTelecom().getItem().setUse("WP");
		String telWork = "";
		if (piMap.get("telWork") != null)
			telWork = (String) piMap.get("telWork");
		patientPerson.getAsEmployee().getEmployerOrganization()
				.getContactParty().getTelecom().getItem().setValue(telWork);
		
		BdDefdoc docRel = new BdDefdoc();
		if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(piMap, "dtRalation")))
			docRel =bdPubService.getBdDefDocStd(
						MsgUtils.getPropValueStr(piMap, "dtRalation"), "000013");

		patientPerson.getPersonalRelationship().getCode()
				.setCode(docRel.getCodeStd());
		patientPerson.getPersonalRelationship().getCode()
				.setCodeSystem("2.16.156.10011.2.3.3.8");
		patientPerson.getPersonalRelationship().getCode()
				.setCodeSystemName("家庭关系代码表(GB/T 4761)");
		patientPerson.getPersonalRelationship().getCode().getDisplayName()
				.setValue(docRel.getNameStd());
		patientPerson.getPersonalRelationship().getTelecom()
				.setXSI_TYPE("BAG_TEL");
		patientPerson.getPersonalRelationship().getTelecom().getItem()
				.setUse("H");
		String telRel = "";
		if (piMap.get("telRel") != null)
			telRel = (String) piMap.get("telRel");
		patientPerson.getPersonalRelationship().getTelecom().getItem()
				.setValue(telRel);
		patientPerson.getPersonalRelationship().getRelationshipHolder1()
				.setClassCode("PSN");
		patientPerson.getPersonalRelationship().getRelationshipHolder1()
				.setDeterminerCode("INSTANCE");
		patientPerson.getPersonalRelationship().getRelationshipHolder1()
				.getName().setXSI_TYPE("DSET_EN");
		patientPerson.getPersonalRelationship().getRelationshipHolder1()
				.getName().getItem().getPart()
				.setValue(MsgUtils.getPropValueStr(piMap, "nameRel"));
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getProviderOrganization()
				.setClassCode("ORG");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getProviderOrganization()
				.setDeterminerCode("INSTANCE");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getProviderOrganization().getId()
				.getItem().setRoot("2.16.156.10011.1.5");

		Map<String, Object> bdOrg = syxPlatFormSendAdtMapper
				.qryOrg(MsgUtils.getPropValueStr(piMap, "pkOrg"));
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getProviderOrganization().getId()
				.getItem().setExtension(MsgUtils.getPropValueStr(bdOrg, "codeOrg"));
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getProviderOrganization().getName()
				.setXSI_TYPE("DSET_EN");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getProviderOrganization().getName()
				.getItem().getPart().setValue(MsgUtils.getPropValueStr(bdOrg, "nameOrg"));
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getProviderOrganization()
				.getContactParty().setClassCode("CON");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getCoveredPartyOf()
				.setTypeCode("COV");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getCoveredPartyOf()
				.getCoverageRecord().setClassCode("COV");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getCoveredPartyOf()
				.getCoverageRecord().setMoodCode("EVN");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getCoveredPartyOf()
				.getCoverageRecord().getBeneficiary().setTypeCode("BEN");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getCoveredPartyOf()
				.getCoverageRecord().getBeneficiary().getBeneficiary()
				.setClassCode("MBR");
		List<Map<String,Object>> mainHpInfo = syxPlatFormSendAdtMapper.getMainHpInfo(MsgUtils.getPropValueStr(map, "pkPi"));
		String hpCode = "";
		String hpName = "";
		if(mainHpInfo!=null && mainHpInfo.size()>0){
			hpCode = MsgUtils.getPropValueStr(mainHpInfo.get(0), "codeStd");
			hpName = MsgUtils.getPropValueStr(mainHpInfo.get(0), "nameStd");	
		}
		
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getCoveredPartyOf()
				.getCoverageRecord().getBeneficiary().getBeneficiary()
				.getCode().setCode(hpCode);
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getCoveredPartyOf()
				.getCoverageRecord().getBeneficiary().getBeneficiary()
				.getCode().setCodeSystem("2.16.156.10011.2.3.1.248");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getCoveredPartyOf()
				.getCoverageRecord().getBeneficiary().getBeneficiary()
				.getCode().setCodeSystemName("医疗保险类别代码（CV02.01.204）");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getSubject1().getPatient().getCoveredPartyOf()
				.getCoverageRecord().getBeneficiary().getBeneficiary()
				.getCode().getDisplayName().setValue(hpName);

		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getAuthor().setTypeCode("AUT");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getAuthor().getAssignedEntity().setClassCode("ASSIGNED");
		req.getControlActProcess().getSubject().getRegistrationRequest()
				.getAuthor().getAssignedEntity().getId().getItem()
				.setRoot("2.16.156.10011.1.4");
		Author author = req.getControlActProcess().getSubject()
				.getRegistrationRequest().getAuthor();
		Map<String, Object> creator = empiClientSyxMapper.getCreator(UserContext
				.getUser().getPkEmp());

		author.getAssignedEntity().getId().getItem()
				.setExtension(MsgUtils.getPropValueStr(creator, "codeEmp"));

		author.getAssignedEntity().getAssignedPerson().setClassCode("PSN");
		author.getAssignedEntity().getAssignedPerson()
				.setDeterminerCode("INSTANCE");
		author.getAssignedEntity().getAssignedPerson().getName()
				.setXSI_TYPE("DSET_EN");
		author.getAssignedEntity().getAssignedPerson().getName().getItem()
				.getPart()
				.setValue(MsgUtils.getPropValueStr(creator, "nameEmp"));

	}
	
	/**
	 * 创建出院登记信息
	 * 
	 * @param action
	 * @param paramMap
	 * @return
	 */
	private Request createPvOutMsg(Request req,Map<String, Object> map,String action) {
		//Request req = new Request(action + ".xsd");
		//req = MsgUtils.createPubReq(req, action);
		String pkPv = (String) map.get("pkPv");
		List<Map<String,Object>> deptList=syxPlatFormSendAdtMapper.qryPvOutAll(pkPv);
		Map<String,Object> deptMap=deptList.get(deptList.size()-1);
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

		ControlActProcess con = req.getControlActProcess();
		con.setClassCode("INFO");
		con.setMoodCode("PRP");

		Subject sub = con.getSubject();
		sub.setTypeCode("SUBJ");
		EncounterEvent enco = sub.getEncounterEvent();
		enco.setClassCode("ENC");
		enco.setMoodCode("EVN");
		Id id = enco.getId();
		List<Item> items = id.getItems();
		Item item1 = new Item();
		item1.setRoot("2.16.156.10011.1.12");
		
		item1.setExtension(MsgUtils.getPropValueStr(deptMap, "codeIp"));
		Item item2 = new Item();
		item2.setRoot("2.16.156.10011.2.5.1.8");
		item2.setExtension(MsgUtils.getPropValueStr(deptMap, "ipTimes"));
		Item item3 = new Item();
		item3.setRoot("2.16.156.10011.2.5.1.9");
		item3.setExtension(MsgUtils.getPropValueStr(deptMap, "codePv"));
		items.add(item1);
		items.add(item2);
		items.add(item3);

		Code code = enco.getCode();
		code.setCode(MsgUtils.getPropValueStr(deptMap, "euPvtype"));
		String euPvtype = MsgUtils.getPropValueStr(deptMap, "euPvtype");
		String euPvtypeValue = "";
		switch(euPvtype){ 
		    case "1":  euPvtypeValue = EuPvtypeValue.OUTPATIENT;break;
		    case "2":  euPvtypeValue = EuPvtypeValue.EMERGENCY;break;
		    case "3":  euPvtypeValue = EuPvtypeValue.INHOSPITAL;break;
		    case "4":  euPvtypeValue = EuPvtypeValue.EXAMINATION;break;
		    case "5":  euPvtypeValue = EuPvtypeValue.HOMESICKED;break;		    
		    default:  break;
		}
		
		code.setCodeSystem("2.16.156.10011.2.3.1.271");
		code.setCodeSystemName("患者类型代码表");
		code.getDisplayName().setValue(euPvtypeValue);

		enco.getStatusCode();
		String dateEnd = MsgUtils.getPropValueStr(deptMap, "dateEnd");		
		String dateBegin = MsgUtils.getPropValueStr(deptMap, "dateBegin");
		String EndDay = "";
		long day = 0;
		String day1 = "";
		if(dateEnd.length()!=0){			
			EndDay = dateEnd.substring(0,4)+'-'+dateEnd.substring(4,6)+'-'+dateEnd.substring(6,8);
		}
		String BeginDay = dateBegin.substring(0,4)+'-'+dateBegin.substring(4,6)+'-'+dateBegin.substring(6,8);
		if(dateEnd.length()!=0) {
			 day = getDaySub(BeginDay,EndDay);
			 day1 = String.valueOf(day);
		}
		enco.getEffectiveTime().getHigh().setValue(dateEnd);//出院时间
		enco.getLengthOfStayQuantity().setUnit("天");
		enco.getLengthOfStayQuantity().setValue(day1);

		Subject ensub = enco.getSubject();
		ensub.setTypeCode("SBJ");
		Patient pat = ensub.getPatient();
		pat.setClassCode("PAT");
		pat.getId().getItem().setRoot("2.16.156.10011.2.5.1.4");
		//pat.getId().getItem().setExtension(MsgUtils.getPropValueStr(deptMap, "pkPi"));
		pat.getId().getItem().setExtension(MsgUtils.getPropValueStr(deptMap, "codePi"));

		PatientPerson per = pat.getPatientPerson();
		per.getId().getItem().setRoot("2.16.156.10011.1.3");
		per.getId().getItem().setExtension(MsgUtils.getPropValueStr(deptMap, "idNo"));

		per.getName().setXSI_TYPE("DSET_EN");
		per.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(deptMap, "namePi"));

		Discharger dis = enco.getDischarger();
		dis.setTypeCode("DIS");
		dis.getTime();
		AssignedPerson ass = dis.getAssignedPerson();
		ass.setClassCode("ASSIGNED");
		ass.getId().getItem().setRoot("2.16.156.10011.1.4");
		ass.getId().getItem().setExtension(UserContext.getUser().getCodeEmp());
		AssignedPerson assp = ass.getAssignedPerson();
		assp.setClassCode("PSN");
		assp.setDeterminerCode("INSTANCE");
		assp.getName().setXSI_TYPE("DSET_EN");
		assp.getName().getItem().getPart().setValue(UserContext.getUser().getNameEmp());

		for (int i = 0; i < deptList.size(); i++) {//<!--诊断- 可重复-->
			Reason rea = enco.getReason();
			rea.setTypeCode("RSON");
			ObservationDx obs = rea.getObservationDx();
			obs.setClassCode("OBS");
			obs.setMoodCode("EVN");
			obs.getCode();
			obs.getStatusCode();
			obs.getValue().setCode(MsgUtils.getPropValueStr(deptMap, "pkDiag"));
			obs.getValue().setCodeSystem("2.16.156.10011.2.3.3.11");
			obs.getValue().setCodeSystemName(MsgUtils.getPropValueStr(deptMap, "codeIcd"));
			obs.getValue().getDisplayName().setValue(MsgUtils.getPropValueStr(deptMap, "nameDiag"));
			Author aut = obs.getAuthor();
			aut.setTypeCode("AUT");
			aut.getAssignedEntity().setClassCode("ASSIGNED");
			aut.getAssignedEntity().getId().setRoot("2.16.156.10011.1.4");
			String pkEmpDiag = MsgUtils.getPropValueStr(deptMap, "pkEmpDiag");
			String codeEmp = syxPlatFormSendAdtMapper.qryCodeEmpByEmployee(pkEmpDiag);		
			aut.getAssignedEntity().getId().setExtension(codeEmp);
		}

		DepartedBy dep = enco.getDepartedBy();
		if(action.equals("PRPA_IN400003UV")) {			
			dep.getTransportationEvent();
			dep.getTransportationEvent().getCode();
			Location deploc = dep.getTransportationEvent().getLocation();
			deploc.setTypeCode("LOC");
			LocatedEntity loen = deploc.getLocatedEntity();
			loen.setClassCode("LOCE");
			loen.getId();			
			Location loct = loen.getLocation();
			loct.setClassCode("PLC");
			loct.setDeterminerCode("INSTANCE");
			loct.getId().getItem().setRoot("2.16.156.10011.1.26");
			loct.getId().getItem().setExtension(MsgUtils.getPropValueStr(deptMap, "codeDept"));
			loct.getName().setXSI_TYPE("DSET_EN");
			loct.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(deptMap, "nameDept"));
			
			LocatedEntityHasParts part = loct.getLocatedEntityHasParts();
			part.setClassCode("LOCE");
			LocatedPlace place = part.getLocatedPlace();
			place.setClassCode("PLC");
			place.setDeterminerCode("INSTANCE");
			place.getId().getItem().setRoot("2.16.156.10011.1.27");
			place.getId().getItem().setExtension(MsgUtils.getPropValueStr(deptMap, "codeDeptNs"));
			place.getName().setXSI_TYPE("DSET_EN");
			place.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(deptMap, "nameDeptNs"));
		}else {
			dep.getTransportation();
			dep.getTransportation().setMoodCode("EVN");
			dep.getTransportation().getCode();
			Location deploc = dep.getTransportation().getLocation();
			deploc.setTypeCode("LOC");
			LocatedEntity loen = deploc.getLocatedEntity();
			loen.setClassCode("LOCE");
			loen.getId();
			
			Location loct = loen.getLocation();
			loct.setClassCode("PLC");
			loct.setDeterminerCode("INSTANCE");
			loct.getId().getItem().setRoot("2.16.156.10011.1.26");
			loct.getId().getItem().setExtension(MsgUtils.getPropValueStr(deptMap, "codeDept"));
			loct.getName().setXSI_TYPE("DSET_EN");
			loct.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(deptMap, "nameDept"));
			
			LocatedEntityHasParts part = loct.getLocatedEntityHasParts();
			part.setClassCode("LOCE");
			LocatedPlace place = part.getLocatedPlace();
			place.setClassCode("PLC");
			place.setDeterminerCode("INSTANCE");
			place.getId().getItem().setRoot("2.16.156.10011.1.27");
			place.getId().getItem().setExtension(MsgUtils.getPropValueStr(deptMap, "codeDeptNs"));
			place.getName().setXSI_TYPE("DSET_EN");
			place.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(deptMap, "nameDeptNs"));
		}
		return req;
		
	}
	
	/*
	 * 
	 *    时间相减得到天数
	 */
	public static long getDaySub(String beginDateStr,String endDateStr)
	     {
		     if(endDateStr.equals("0")) {
		    	 return 0;
		     }else {		    	 
		    	 long day=0;
		    	 java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");    
		    	 java.util.Date beginDate;
		    	 java.util.Date endDate;
		    	 try
		    	 {
		    		 beginDate = format.parse(beginDateStr);
		    		 endDate= format.parse(endDateStr);    
		    		 day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);    
		    		 //System.out.println("相隔的天数="+day);   
		    	 } catch (Exception e){	            
		    		 e.printStackTrace();
		    	 }   
		    	 return day;
		     }
	    }	

}
