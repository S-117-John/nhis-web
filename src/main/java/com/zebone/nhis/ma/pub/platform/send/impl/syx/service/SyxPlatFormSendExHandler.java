package com.zebone.nhis.ma.pub.platform.send.impl.syx.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.hl7v2.HL7Exception;

import com.zebone.nhis.base.pub.service.BdPubService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.dao.SyxPlatFormSendExMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.vo.OrderCheckMsgVo;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.vo.SysEsbmsg;
import com.zebone.nhis.ma.pub.platform.syx.support.HIPMessageServerUtils;
import com.zebone.nhis.ma.pub.platform.syx.support.MsgUtils;
import com.zebone.nhis.ma.pub.platform.syx.support.XmlProcessUtils;
import com.zebone.nhis.ma.pub.platform.syx.vo.AsOrganizationPartOf;
import com.zebone.nhis.ma.pub.platform.syx.vo.AsOtherIDs;
import com.zebone.nhis.ma.pub.platform.syx.vo.AssignedEntity;
import com.zebone.nhis.ma.pub.platform.syx.vo.AssignedPerson;
import com.zebone.nhis.ma.pub.platform.syx.vo.Author;
import com.zebone.nhis.ma.pub.platform.syx.vo.Component2;
import com.zebone.nhis.ma.pub.platform.syx.vo.ComponentOf1;
import com.zebone.nhis.ma.pub.platform.syx.vo.Consumable2;
import com.zebone.nhis.ma.pub.platform.syx.vo.ContainerPackagedProduct;
import com.zebone.nhis.ma.pub.platform.syx.vo.ControlActProcess;
import com.zebone.nhis.ma.pub.platform.syx.vo.Encounter;
import com.zebone.nhis.ma.pub.platform.syx.vo.Item;
import com.zebone.nhis.ma.pub.platform.syx.vo.Location;
import com.zebone.nhis.ma.pub.platform.syx.vo.ManufacturedProduct;
import com.zebone.nhis.ma.pub.platform.syx.vo.ManufacturedProduct1;
import com.zebone.nhis.ma.pub.platform.syx.vo.ObservationDx;
import com.zebone.nhis.ma.pub.platform.syx.vo.Patient;
import com.zebone.nhis.ma.pub.platform.syx.vo.PatientPerson;
import com.zebone.nhis.ma.pub.platform.syx.vo.Performer;
import com.zebone.nhis.ma.pub.platform.syx.vo.PertinentInformation;
import com.zebone.nhis.ma.pub.platform.syx.vo.PertinentInformation1;
import com.zebone.nhis.ma.pub.platform.syx.vo.PlacerGroup;
import com.zebone.nhis.ma.pub.platform.syx.vo.ProcedureRequest;
import com.zebone.nhis.ma.pub.platform.syx.vo.RepresentedOrganization;
import com.zebone.nhis.ma.pub.platform.syx.vo.Request;
import com.zebone.nhis.ma.pub.platform.syx.vo.ServiceDeliveryLocation;
import com.zebone.nhis.ma.pub.platform.syx.vo.ServiceProviderOrganization;
import com.zebone.nhis.ma.pub.platform.syx.vo.Subject;
import com.zebone.nhis.ma.pub.platform.syx.vo.SubjectOf3;
import com.zebone.nhis.ma.pub.platform.syx.vo.SubjectOf6;
import com.zebone.nhis.ma.pub.platform.syx.vo.SubstanceAdministrationRequest;
import com.zebone.nhis.ma.pub.platform.syx.vo.Verifier;
import com.zebone.nhis.ma.pub.platform.syx.vo.WholeOrganization;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.utils.JsonUtil;


/**
 * ??????EX????????????
 * @author yangxue
 *
 */
@Service
public class SyxPlatFormSendExHandler {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat  sd= new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat  sdm= new SimpleDateFormat("yyyyMMddHHmm");
	@Resource
	private SyxPlatFormSendExMapper syxPlatFormSendExMapper;
	@Autowired
	private BdPubService bdPubService;
	private HIPMessageServerUtils hipMessageServerUtils=new HIPMessageServerUtils();
	
	@Autowired
	private SyxPlatFormSendExService syxPlatFormSendExService;
	
	/**
	 * ???????????????????????????????????????????????????
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	public void sendExOrderCheck(Map<String,Object> paramMap) throws Exception{ 
		String headType="";
		String action="";
		Request req=new Request();
		try {
			//?????????vo???????????????????????????
			if(paramMap == null||paramMap.get("ordlistvo")==null) return;
			String param=JsonUtil.writeValueAsString(paramMap.get("ordlistvo"));
			List<OrderCheckMsgVo> listMap = JsonUtil.readValue(param, new TypeReference<List<OrderCheckMsgVo>>() {});
			//List<Callable<Map<String,Object>>> listCall = new ArrayList<Callable<Map<String,Object>>>();
			List<SysEsbmsg> vos4save = new ArrayList<SysEsbmsg>(); 
			for(OrderCheckMsgVo checkvo : listMap){
				Map<String,Object> paramOrder=new HashMap<String,Object>();
				String codeOrdtype=checkvo.getCodeOrdtype();
				String euStatusOrd=checkvo.getEuStatusOrd();
				paramOrder.put("pkCnord",checkvo.getPkCnord());
				String type = ApplicationUtils.getSysparam("PUB0001", false);
				//========================????????????====================
				if("04".equals(codeOrdtype)){//????????????
					
					if(type.indexOf("7")<0)//type ????????? ???7??? ?????????????????????
						continue;
					if("0".equals(euStatusOrd)){//??????
						headType="POOR_IN200901UV";
						action="OperationAppInfoAdd";
					}else{//??????
						headType="POOR_IN200902UV";
						action="OperationAppInfoUpdate";
					}
					req=createOpApplyMessage(headType,paramOrder);
					if(type.indexOf("7N")<0){// ???????????? ???????????????????????? ???"7N"??????????????????????????? ????????????
						hipMessageServerUtils.sendHIPMsg(req, action, "MCCI_IN000002UV01", false);//??????????????????
						continue;
					}
				}else {
					
				
				//========================????????????====================
				 //if("0103".equals(codeOrdtype)){//??????
					if(type.indexOf("3")<0)
						continue;
					if("0".equals(euStatusOrd)){//??????
						headType="POOR_IN200901UV";
						action="OrderInfoAdd";
					}else{
						headType="POOR_IN200902UV";
						action="OrderInfoUpdate";
					}
					req=createOrderMessage(headType,paramOrder);
				}
				//??????SysEsbmsg?????????
				SysEsbmsg sysEsbmsg = createSysEsbmsg(req, action, checkvo, headType,  paramMap);
				vos4save.add(sysEsbmsg);
				
				//========================????????????====================
				/*if("0101".equals(codeOrdtype)||"0102".equals(codeOrdtype)){//????????????
					if(type.indexOf("3")<0)
						continue;
					if("0".equals(euStatusOrd)){//??????
						headType="POOR_IN200901UV";
						action="OrderInfoAdd";
					}else{
						headType="POOR_IN200902UV";
						action="OrderInfoUpdate";
					}
					req=createOrderMessage(headType,paramOrder);
				}*/
//				hipMessageServerUtils.sendHIPMsg(req, action, "MCCI_IN000002UV01", false);
				//?????????????????????????????????
				//??????????????????,?????????????????????
//				Callable<Map<String,Object>> threadChkSend = new ThreadChkSend(req, action, "MCCI_IN000002UV01", false);
//				listCall.add(threadChkSend);
			}
			if (vos4save.size()>0) {
				//?????????????????????????????????????????????????????????????????????????????????
				syxPlatFormSendExService.save(vos4save);
			}

//			if (listCall == null || listCall.size()<=0 ) {
//				return;
//			}
//			MsgUtils.threadExecuteMsg(listCall);
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * ??????????????????????????????
	 * @param paramMap
	 */
	public void sendOrderFillerMsg(Map<String,Object> paramMap){
		try {
			//BdOuDept dept=(BdOuDept) paramMap.get("dept");			
			String TXt="";						
				TXt=convertOrderFillerToRequestXml( "POOR_IN200901UV.xsd");
				System.out.println(TXt);
				hipMessageServerUtils.sendHIPService("OrderFillerStatusInfoUpdate", TXt);							
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
  	
   private String convertOrderFillerToRequestXml(String xsd){
	    
		Request req = new Request(xsd);
		
		createOrderFillerMessage(req,"POOR_IN200901UV");		
		
		return XmlProcessUtils.toRequestXml(req,xsd.substring(0, xsd.indexOf(".")));
    }
	
	/**
	 * ??????????????????
	 * @param paramMap
	 * @return
	 */
	private List<Map<String,Object>> qryOrderInfo(Map<String,Object> paramMap){
		return syxPlatFormSendExMapper.qryOrderInfo(paramMap);
	}
	/**
	 * ???????????????????????????IP
	 * @return
	 */
	private String qryIpSend(String action){
		switch (action) {
		case "OperationAppInfoAdd":
			action="OpAppInfoAdd";
			break;
		case "OperationAppInfoUpdate":
			action="OpAppInfoUpdate";
			break;
		default:
			break;
		}
		return syxPlatFormSendExMapper.qryIpSend(action);
	}
	/**
	 * ????????????????????????????????????????????????
	 * @param req
	 * @param resultList
	 */
    private Request createOpApplyMessage(String action,Map<String,Object> paramMap){
    		Request req=new Request(action+".xsd");
    		List<Map<String,Object>> resultList=syxPlatFormSendExMapper.qryOpApplyInfo(paramMap);
			Map<String,Object> resMap=resultList.get(0);
			if(resultList==null || resultList.size()<=0)return null;
			MsgUtils.createPubReq(req,action);
			req.getReceiver().getDevice().getId().getItem().setExtension(MsgUtils.getPropValueStr(paramMap, "pkCnord"));
			//??????????????????
			req.getControlActProcess().setClassCode("CACT");
			req.getControlActProcess().setMoodCode("EVN");
			req.getControlActProcess().getSubject().setTypeCode("SUBJ");
			req.getControlActProcess().getSubject().setContextConductionInd("false");
			req.getControlActProcess().getSubject().setProcedureRequest(new ProcedureRequest());
			ProcedureRequest procedureRequest=req.getControlActProcess().getSubject().getProcedureRequest();
			procedureRequest.setClassCode("PROC");
			procedureRequest.setMoodCode("RQO");
			List<Item> itemOps=procedureRequest.getId().getItems();
			Item itemOp=new Item(); 
			itemOp.setExtension(MsgUtils.getPropValueStr(resMap, "codeApply"));//????????????
			itemOp.setRoot("2.16.156.10011.1.24");
			itemOps.add(itemOp);
			Item itemCn=new Item();
			itemCn.setExtension(MsgUtils.getPropValueStr(resMap, "ordsn"));//?????????id
			itemCn.setRoot("2.16.156.10011.1.28");
			itemOps.add(itemCn);
			procedureRequest.getCode();
			procedureRequest.getText().setValue(MsgUtils.getPropValueStr(resMap, "noteOrd"));//??????
			procedureRequest.getStatusCode();
			procedureRequest.getEffectiveTime().setXSI_TYPE("IVL_TS");
			procedureRequest.getEffectiveTime().getLow().setValue(sdm.format(MsgUtils.getPropValueDate(resMap, "dateApply")));//????????????
			procedureRequest.getEffectiveTime().setAny(null);
			procedureRequest.getStatusCode().setCode(MsgUtils.getPropValueStr(resMap, "euStatusOp")); 
			List<Item> items=req.getControlActProcess().getSubject().getProcedureRequest().getMethodCode().getItems();
			Item item1=new Item();
			item1.setCode(MsgUtils.getPropValueStr(resMap, "anaeCode"));//??????????????????
			item1.setCodeSystem("2.16.156.10011.2.3.1.159");
			item1.getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "anaeName"));//??????????????????
			items.add(item1);
			Item item2=new Item();
			item2.setCode(MsgUtils.getPropValueStr(resMap, "euOptype"));//??????????????????
			item2.setCodeSystem("2.16.156.10011.2.3.1.15");
			item2.getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "optypeName"));//??????????????????
			items.add(item2);
			procedureRequest.getMethodCode().setItems(items);
			//???????????????
			Author author=procedureRequest.getAuthor();
			author.setTypeCode("AUT");
			author.setContextControlCode("OP");//TODO op???????????????????????????OP:?????????
			author.getAssignedEntity().setClassCode("ASSIGNED");
			author.getAssignedEntity().getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "ordEmpCode"));//??????????????????
			author.getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
			author.getAssignedEntity().getAssignedPerson().setClassCode("PSN");
			author.getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
			author.getAssignedEntity().getAssignedPerson().setXSI_NIL("false");
			author.getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("LIST_EN");
			author.getAssignedEntity().getAssignedPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "ordEmpName"));//??????????????????
			RepresentedOrganization represent=author.getAssignedEntity().getRepresentedOrganization();
			represent.setClassCode("ORG");
			represent.setDeterminerCode("INSTANCE");
			represent.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "ordCodeDept"));//??????????????????
			represent.getId().getItem().setRoot("2.16.156.10011.1.26");
			represent.getName().setXSI_TYPE("LIST_EN");
			represent.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "ordNameDept"));//??????????????????
			//???????????????
			Verifier verifier=procedureRequest.getVerifier();
			verifier.setTypeCode("VRF");
			String dateFormatString = MsgUtils.dateFormatString("YYYYMMDD", MsgUtils.getPropValueDate(resMap, "dateChk"));
			verifier.getTime().setValue(dateFormatString);//TODO ?????????????????????????????????YYYYMMDD
			AssignedEntity ass=verifier.getAssignedEntity();
			ass.setClassCode("ASSIGNED");
			ass.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "chkEmpCode"));//??????????????????
			ass.getId().getItem().setRoot("2.16.156.10011.1.4");
			ass.getAssignedPerson().setClassCode("PSN");
			ass.getAssignedPerson().setDeterminerCode("INSTANCE");
			ass.getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
			ass.getAssignedPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "chkEmpName"));//??????????????????
			List<Component2> component2s=procedureRequest.getComponent2();
			//??????????????????
			Component2 com=new Component2();
			com.getProcedureRequest().setClassCode("PROC");
			com.getProcedureRequest().setMoodCode("RQO");
			com.getProcedureRequest().getCode().setCodeSystem("2.16.156.10011.2.3.3.12");
			com.getProcedureRequest().getCode().setCode(MsgUtils.getPropValueStr(resMap, "diagcode"));//????????????
			com.getProcedureRequest().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "diagname"));//????????????
			com.getProcedureRequest().getPriorityCode().setCode(MsgUtils.getPropValueStr(resMap, "opLevelCode"));//??????????????????
			com.getProcedureRequest().getPriorityCode().setCodeSystem("2.16.156.10011.2.3.1.258");
			com.getProcedureRequest().getPriorityCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "opLevelName"));//??????????????????
			Performer per=com.getProcedureRequest().getPerformer();
			per.setTypeCode("PRF");
			per.getTime().getLow().setValue(sdm.format(MsgUtils.getPropValueDate(resMap, "datePlan")));//??????????????????
			per.getAssignedEntity().setClassCode("ASSIGNED");
			per.getAssignedEntity().getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "opEmpCode"));//??????????????????
			per.getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
			per.getAssignedEntity().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "opEmpName"));//????????????
			AssignedPerson assi= per.getAssignedEntity().getAssignedPerson();
			assi.setClassCode("PSN");
			assi.setDeterminerCode("INSTANCE");
			assi.getName().setXSI_TYPE("LIST_EN");
			assi.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "opEmpName"));//??????????????????
			RepresentedOrganization reps=per.getAssignedEntity().getRepresentedOrganization();
			reps.setClassCode("ORG");
			reps.setDeterminerCode("INSTANCE");
			reps.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "opDeptCode"));//??????????????????
			
			reps.getId().getItem().setRoot("2.16.156.10011.1.26");
			reps.getName().setXSI_TYPE("LIST_EN");
			reps.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "opDeptName"));//??????????????????
			component2s.add(com);
			for (int i = 1; i <resultList.size(); i++) {//??????????????????
				Component2 component2=new Component2();
				component2.getProcedureRequest().setClassCode("PROC");
				component2.getProcedureRequest().setMoodCode("RQO");
				component2.getProcedureRequest().getCode().setCodeSystem("2.16.156.10011.2.3.3.12");
				component2.getProcedureRequest().getCode().setCode(MsgUtils.getPropValueStr(resMap, "subDiagcode"));//????????????
				component2.getProcedureRequest().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "subDiagname"));//????????????
				component2.getProcedureRequest().getPriorityCode().setCode(MsgUtils.getPropValueStr(resMap, "opLevelCode"));//??????????????????
				component2.getProcedureRequest().getPriorityCode().setCodeSystem("2.16.156.10011.2.3.1.258");
				component2.getProcedureRequest().getPriorityCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "opLevelName"));//??????????????????
				Performer performer=component2.getProcedureRequest().getPerformer();
				performer.setTypeCode("PRF");
				performer.getTime().getLow().setValue(sdf.format(MsgUtils.getPropValueDate(resMap, "datePlan")));//??????????????????
				performer.getAssignedEntity().setClassCode("ASSIGNED");
				performer.getAssignedEntity().getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "opEmpCode"));//??????????????????
				performer.getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
				performer.getAssignedEntity().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "opEmpName"));//????????????
				AssignedPerson assPer= performer.getAssignedEntity().getAssignedPerson();
				assPer.setClassCode("PSN");
				assPer.setDeterminerCode("INSTANCE");
				assPer.getName().setXSI_TYPE("LIST_EN");
				assPer.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "opEmpName"));//??????????????????
				RepresentedOrganization reporg=performer.getAssignedEntity().getRepresentedOrganization();
				reporg.setClassCode("ORG");
				reporg.setDeterminerCode("INSTANCE");
				reporg.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "opDeptCode"));//??????????????????
				reporg.getId().getItem().setRoot("2.16.156.10011.1.26");
				reporg.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "opDeptName"));//??????????????????
				component2s.add(component2);
			}
			//????????????
			SubjectOf6 subjectOf6=procedureRequest.getSubjectOf6();
			subjectOf6.setContextConductionInd("true");
			subjectOf6.getSeperatableInd().setValue("false");
			subjectOf6.getAnnotation().getText().setValue("????????????");// TODO ????????????????????????
			subjectOf6.getAnnotation().getStatusCode();//?????????
			subjectOf6.getAnnotation().getAuthor().setTypeCode("AUT");
			subjectOf6.getAnnotation().getAuthor().getAssignedEntity().setClassCode("ASSIGNED");
			ComponentOf1 componentOf1=procedureRequest.getComponentOf1();
			componentOf1.setContextConductionInd("false");
			componentOf1.setContextControlCode("OP");//TODO OP???????????????????????????
			componentOf1.setTypeCode("COMP");
			Encounter encounter=componentOf1.getEncounter();
			encounter.setClassCode("ENC");
			encounter.setMoodCode("EVN");
			List<Item> enItems=new ArrayList<Item>();
			Item enItem1=new Item();//????????????
			enItem1.setExtension(MsgUtils.getPropValueStr(resMap, "ipTimes"));
			enItem1.setRoot("2.16.156.10011.2.5.1.8");
			Item enItem2=new Item();//???????????????
			enItem2.setExtension(MsgUtils.getPropValueStr(resMap, "codePv"));
			enItem2.setRoot("2.16.156.10011.2.5.1.9");
			enItems.add(enItem1);
			enItems.add(enItem2);
			for (int i = 0; i < enItems.size(); i++) {
				encounter.getId().setItems(enItems);
			}
			encounter.getCode().setCodeSystem("2.16.156.10011.2.3.1.271");
			encounter.getCode().setCode(MsgUtils.getPropValueStr(resMap, "euPvtype"));//??????????????????
			encounter.getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "pvtypeName"));//??????????????????
			encounter.getStatusCode().setCode("active");
			String format = sdm.format(MsgUtils.getPropValueDate(resMap, "dateAdmit"));
			encounter.getEffectiveTime().getLow().setValue(format);//TODO <!--??????(??????)?????? -->
			encounter.getEffectiveTime().setXSI_TYPE("IVL_TS");
			Subject ensub=encounter.getSubject();
			ensub.setTypeCode("SBJ");
			
			List<Item> patItems=new ArrayList<Item>();
			Item patItem1=new Item();//???id
			patItem1.setRoot("2.16.156.10011.2.5.1.5");
			patItem1.setExtension(MsgUtils.getPropValueStr(resMap, ""));//TODO ????????????????????????
			Item patItem2=new Item();//??????id
			patItem2.setRoot("2.16.156.10011.2.5.1.4");
			//patItem2.setExtension(MsgUtils.getPropValueStr(resMap, "pkPi"));//pk_pi????????????
			patItem2.setExtension(MsgUtils.getPropValueStr(resMap, "codePi"));
			Item patItem3=new Item();//?????????
			patItem3.setRoot("2.16.156.10011.1.11");
			patItem3.setExtension(MsgUtils.getPropValueStr(resMap, "codeOp"));
			Item patItem4=new Item();//?????????
			patItem4.setRoot("2.16.156.10011.1.12");
			patItem4.setExtension(MsgUtils.getPropValueStr(resMap, "codeIp"));
			patItems.add(patItem1);
			patItems.add(patItem2);
			patItems.add(patItem3);
			patItems.add(patItem4);
			ensub.getPatient().setClassCode("PAT");
			ensub.getPatient().getId().setItems(patItems);
			PatientPerson patientPerson= ensub.getPatient().getPatientPerson();
			patientPerson.setClassCode("PSN");
			patientPerson.setDeterminerCode("INSTANCE");
			patientPerson.setXSI_NIL("false");
			List<Item> perItems=patientPerson.getId().getItems();
			Item perItem1=new Item();
			perItem1.setExtension(MsgUtils.getPropValueStr(resMap, "idNo"));//????????????
			perItem1.setRoot("2.16.156.10011.1.3");
			Item perItem2=new Item();
			perItem2.setExtension(MsgUtils.getPropValueStr(resMap, "insurNo"));//????????????
			perItem2.setRoot("2.16.156.10011.1.15");
			perItems.add(perItem1);
			perItems.add(perItem2);
			patientPerson.getName().setXSI_TYPE("DSET_EN");
			patientPerson.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "namePi"));//??????
			patientPerson.getTelecom().setXSI_TYPE("BAG_TEL");
			patientPerson.getTelecom().getItem().setValue(MsgUtils.getPropValueStr(resMap, "telNo"));//??????
			
			patientPerson.getAdministrativeGenderCode().setCode(MsgUtils.getPropValueStr(resMap, "sexCode"));//????????????
			patientPerson.getAdministrativeGenderCode().setCodeSystem("2.16.156.10011.2.3.3.4");
			patientPerson.getAdministrativeGenderCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "sexName"));//????????????
			Date birthDate=MsgUtils.getPropValueDate(resMap, "birthDate");
			patientPerson.getBirthTime().setValue(sd.format(birthDate));//????????????
			Integer birth=DateUtils.getYear(new Date())- DateUtils.getYear(birthDate);
			patientPerson.getBirthTime().getOriginalText().setValue(birth.toString());//??????
			patientPerson.getAddr().setXSI_TYPE("BAG_AD");
			patientPerson.getAddr().getItem().setUse("H");// TODO ?????????
			patientPerson.getAddr().getItem().getPart().setType("AL");//TODO ?????????
			patientPerson.getAddr().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "address")); 
			Location enLoca=encounter.getLocation();
			enLoca.setTypeCode("LOC");
			enLoca.getTime();
			enLoca.getServiceDeliveryLocation().setClassCode("SDLOC");
			enLoca.getServiceDeliveryLocation().setLocation(new Location());
			Location delLoca=enLoca.getServiceDeliveryLocation().getLocation();
			delLoca.setClassCode("PLC");
			delLoca.setDeterminerCode("INSTANCE");
			delLoca.getId().getItem().setValue(MsgUtils.getPropValueStr(resMap, "bedCode"));//TODO ????????????
			delLoca.getName().setXSI_TYPE("BAG_EN");
			delLoca.getName().getItem().setUse("IDE");
			delLoca.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "bedName"));//??????
			delLoca.getAsLocatedEntityPartOf().setClassCode("LOCE");
			delLoca.getAsLocatedEntityPartOf().setLocation(new Location());
			Location asLoca= delLoca.getAsLocatedEntityPartOf().getLocation();
			asLoca.setClassCode("PLC");
			asLoca.setDeterminerCode("INSTANCE");
			asLoca.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, ""));//????????????????????????
			asLoca.getName().setXSI_TYPE("BAG_EN");
			asLoca.getName().getItem().setUse("IDE");
			asLoca.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "houseno"));//?????????
			ServiceProviderOrganization organ= enLoca.getServiceProviderOrganization();
			organ.setClassCode("ORG");
			organ.setDeterminerCode("INSTANCE");
			organ.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "pvDeptCode"));//????????????
			organ.getName().setXSI_TYPE("BAG_EN");
			organ.getName().getItem().setUse("IDE");
			organ.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "pvDeptName"));//????????????
			organ.getAsOrganizationPartOf().setClassCode("PART");
			WholeOrganization whole= organ.getAsOrganizationPartOf().getWholeOrganization();
			whole.setClassCode("ORG");
			whole.setDeterminerCode("INSTANCE");
			whole.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "nsDeptCode"));//????????????
			whole.getName().setXSI_TYPE("BAG_EN");
			whole.getName().getItem().setUse("IDE");
			whole.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "nsDeptName"));//????????????
			PertinentInformation1 pertinent=encounter.getPertinentInformation1();
			pertinent.setTypeCode("PERT");
			pertinent.setXSI_NIL("false");
			pertinent.setObservationDx(new ObservationDx());
			ObservationDx obser= pertinent.getObservationDx();
			obser.setClassCode("OBS");
			obser.setMoodCode("EVN");
			obser.getCode().setCode("0101");//TODO ??????????????????
			obser.getCode().setCodeSystem("2.16.156.10011.2.5.1.10");
			obser.getCode().getDisplayName().setValue("????????????");//??????????????????
			obser.getStatusCode().setCode("active");
			obser.getValue().setCode(MsgUtils.getPropValueStr(resMap, "codeCd"));//????????????
			obser.getValue().setCodeSystem("2.16.156.10011.2.3.3.11");
			obser.getValue().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "nameCd"));//????????????
			return req;
    }

    /**
     * ??????????????????
     * @param req
     * @param action
     */
    private Request createOrderMessage(String action,Map<String,Object> paramMap){
    	List<Map<String,Object>> orderList=qryOrderInfo(paramMap);
    	if(orderList==null || orderList.size()<=0)return null;
    	String timeFormat = "yyyyMMddHHmmss";//????????????
    	Map<String,Object> ordMap=orderList.get(0);
    	Request req=new Request(action+".xsd");
    	MsgUtils.createPubReq(req, action);
    	ControlActProcess con=req.getControlActProcess();
    	con.setClassCode("CACT");
    	con.setMoodCode("EVN");
    	Subject sub=con.getSubject();
    	sub.setTypeCode("SUBJ");
    	sub.getPlacerGroup().setClassCode("GROUPER");
    	sub.getPlacerGroup().setMoodCode("RQO");
    	
    	//???????????????
    	Author aut=sub.getPlacerGroup().getAuthor();
    	aut.setTypeCode("AUT");
    	aut.setContextControlCode("OP");//TODO ???????????????/??????????????????
    	String dateEnterStr = MsgUtils.dateFormatString(timeFormat, MsgUtils.getPropValueDate(ordMap, "dateEnter"));//TODO ??????????????????
    	aut.getTime().setValue(dateEnterStr);//TODO ??????????????????
    	aut.getSignatureCode().setCode("S");
    	aut.getSignatureText().setValue(MsgUtils.getPropValueStr(ordMap, "nameEmpInput"));//TODO ??????????????????????????????name_emp_input ??????????????????
    	AssignedEntity entity=aut.getAssignedEntity();
    	entity.setClassCode("ASSIGNED");
    	entity.getId().getItem().setRoot("2.16.156.10011.1.4");
    	entity.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "inCodeEmp"));//TODO ??????????????????
    	entity.getAssignedPerson().setClassCode("PSN");
    	entity.getAssignedPerson().setDeterminerCode("INSTANCE");
    	entity.getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
    	entity.getAssignedPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "nameEmpInput"));//???????????????
    	RepresentedOrganization reporg=entity.getRepresentedOrganization();
    	reporg.setClassCode("ORG");
    	//MsgUtils
    	reporg.setDeterminerCode("INSTANCE");
    	reporg.getId().getItem().setRoot("2.16.156.10011.1.26");
    	reporg.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "inCodeDept"));//TODO ??????????????????
    	reporg.getName().setXSI_TYPE("BAG_EN");
    	reporg.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "inNameDept"));//TODO ??????????????????
    	
    	//???????????????
    	Verifier veri=sub.getPlacerGroup().getVerifier();
    	veri.setTypeCode("VRF");
    	veri.setContextControlCode("OP");//TODO ???????????????/??????????????????
    	String dateChk = MsgUtils.dateFormatString(timeFormat, MsgUtils.getPropValueDate(ordMap, "dateChk"));
    	veri.getTime().setValue(dateChk);//????????????
    	veri.getSignatureCode().setCode("S");
    	veri.getSignatureText().setValue(MsgUtils.getPropValueStr(ordMap, "codeEmpChk"));//TODO ?????????????????????
    	AssignedEntity veriEnt=veri.getAssignedEntity();
    	veriEnt.setClassCode("ASSIGNED");
    	veriEnt.getId().getItem().setRoot("2.16.156.10011.1.4");
    	veriEnt.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "codeEmpChk"));//TODO ??????????????????
    	veriEnt.getAssignedPerson().setClassCode("PSN");
    	veriEnt.getAssignedPerson().setDeterminerCode("INSTANCE");
    	veriEnt.getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
    	veriEnt.getAssignedPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "nameEmpChk"));//???????????????
    	
    	List<Component2> com2s=sub.getPlacerGroup().getComponent2s();//1..*
    	for (int i = 0; i < orderList.size(); i++) {
    		
    		Map<String, java.lang.Object> map = orderList.get(i);
    		
    		Component2 com2=new Component2();
    		if("POOR_IN200901UV".equals(action)){
    			com2.getSequenceNumber().setValue(MsgUtils.getPropValueStr(paramMap, "pkCnord"));//????????????
    		}else{
    			com2.getSequenceNumber().setValue(MsgUtils.getPropValueStr(ordMap, "ordsn"));//????????????
    		}
    		SubstanceAdministrationRequest subReq=com2.getSubstanceAdministrationRequest();
    		subReq.setClassCode("SBADM");
    		subReq.setModdCode("RQO");
    		subReq.getId().setRoot("2.16.156.10011.1.28");
    		subReq.getId().setExtension(MsgUtils.getPropValueStr(ordMap, "ordsn"));//??????id
    		subReq.getCode().setCode(MsgUtils.getPropValueStr(ordMap, "codeOrdtype"));//????????????????????????
    		subReq.getCode().setCodeSystem("2.16.156.10011.2.3.1.268");
    		subReq.getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(ordMap, "nameOrdtype"));
    		
    		//subReq.setOrdCode(MsgUtils.getPropValueStr(ordMap, "codeOrd"));
    		//subReq.setOrdName(MsgUtils.getPropValueStr(ordMap, "nameOrd"));
    		
    		subReq.getText();
    		//subReq.getStatusCode().setCode("active");
    		subReq.getStatusCode().setCode(MsgUtils.getPropValueStr(ordMap, "euStatusOrd"));
    		subReq.getEffectiveTime().setXSI_TYPE("QSC_TS");
    		subReq.getEffectiveTime().setValidTimeLow(sdf.format(MsgUtils.getPropValueDate(ordMap, "dateStart")));//????????????
        	String dateEnd = MsgUtils.dateFormatString(timeFormat, MsgUtils.getPropValueDate(ordMap, "dateStop"));//??????????????????
    		subReq.getEffectiveTime().setValidTimeHigh(dateEnd);//??????
    		
    		subReq.getEffectiveTime().getCode().setCode(MsgUtils.getPropValueStr(ordMap, "codeFreq"));//??????
    		subReq.getEffectiveTime().getCode().setCodeSystem("2.16.156.10011.2.5.1.13");
    		subReq.getEffectiveTime().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(ordMap, "freqName"));//????????????
    		
    		subReq.getRouteCode().setCode(MsgUtils.getPropValueStr(ordMap, "codeSupply"));// 
    		subReq.getRouteCode().setCodeSystem("2.16.156.10011.2.3.1.158");
    		subReq.getRouteCode().setCodeSystemName("?????????????????????");//?????????????????????
    		subReq.getRouteCode().getDisplayName().setValue(MsgUtils.getPropValueStr(ordMap, "nameSupply"));//??????
    		
    		subReq.getDoseQuantity().setValue(MsgUtils.getPropValueStr(ordMap, "dosage"));//????????????-??????
    		subReq.getDoseQuantity().setUnit(MsgUtils.getPropValueStr(ordMap, "nameDos"));//??????
    		
    		subReq.getDoseCheckQuantity().setXSI_TYPE("DSET_RTO");
    		subReq.getDoseCheckQuantity().getItem().getNumerator().setXSI_TYPE("PQ");
    		subReq.getDoseCheckQuantity().getItem().getNumerator().setUnit(MsgUtils.getPropValueStr(ordMap, "nameQuan"));//??????
    		subReq.getDoseCheckQuantity().getItem().getNumerator().setValue(MsgUtils.getPropValueStr(ordMap, "quan"));//???
    		subReq.getDoseCheckQuantity().getItem().getDenominator().setXSI_TYPE("PQ");
    		subReq.getDoseCheckQuantity().getItem().getDenominator().setUnit("d");//??????
    		subReq.getDoseCheckQuantity().getItem().getDenominator().setValue(MsgUtils.getPropValueStr(ordMap, "days"));//???
			
    		//???????????? 
    		subReq.getAdministrationUnitCode().setCode(MsgUtils.getPropValueStr(ordMap, "pddocCodeStd"));//???????????????????????????
    		subReq.getAdministrationUnitCode().setCodeSystem("2.16.156.10011.2.3.1.211");
    		subReq.getAdministrationUnitCode().setCodeSystemName("?????????????????????");
    		subReq.getAdministrationUnitCode().getDisplayName().setValue(MsgUtils.getPropValueStr(ordMap, "pddocNameStd"));//???????????????????????????
    		
    		//????????????
    		Consumable2 con2=subReq.getConsumable2();
    		con2.setTypeCode("CSM");
    		ManufacturedProduct1 man1=con2.getManufacturedProduct1();
    		man1.setClassCode("MANU");	
    		man1.getId().setExtension(MsgUtils.getPropValueStr(ordMap, "apprNo"));//TODO ???????????? ???????????????/?????????
    		ManufacturedProduct man=man1.getManufacturedProduct();
    		man.setClassCode("MMAT");
    		man.setDeterminerCode("KIND");
    		man.getCode().setCode(MsgUtils.getPropValueStr(ordMap, "codeOrd"));//??????
    		man.getCode().setCodeSystem("2.16.156.10011.2.5.1.14");
    		man.getName().setXSI_TYPE("BAG_EN");
    		man.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "nameOrd"));//??????
    		man.getAsContent().setClassCode("CONT");
    		man.getAsContent().getQuantity();
    		ContainerPackagedProduct pro= man.getAsContent().getContainerPackagedProduct();
    		pro.setClassCode("HOLD");
    		pro.setDeterminerCode("KIND");
    		pro.getCode();
    		pro.getFormCode();
    		//TODO ???????????? ??????????????????????????????????????????  
    		pro.getCapacityQuantity().setUnit(MsgUtils.getPropValueStr(ordMap, "nameDos"));
    		pro.getCapacityQuantity().setValue(MsgUtils.getPropValueStr(ordMap, "nameQuan"));
    		
    		//TODO ????????????????????????  ?????????????????????
    		SubjectOf3 sub3=man1.getSubjectOf3();
    		sub3.setTypeCode("SBJ");
    		sub3.getPolicy().setClassCode("POLICY");
    		sub3.getPolicy().setMoodCode("EVN");
    		//!-- ????????????????????????/???????????????????????? -->
    		sub3.getPolicy().getCode().setCode(MsgUtils.getPropValueStr(ordMap, "poldocCodeStd"));
    		sub3.getPolicy().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(ordMap, "poldocNameStd"));
    		
    		Location loca=subReq.getLocation();
    		loca.setTypeCode("LOC");
    		ServiceDeliveryLocation serloca= loca.getServiceDeliveryLocation();
    		serloca.setClassCode("DSDLOC");
    		Location sonlo=serloca.getLocation();
    		sonlo.setClassCode("PLC");
    		sonlo.setDeterminerCode("INSTANCE");
    		sonlo.getId().getItem().setRoot("2.16.156.10011.1.26");
    		sonlo.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "exCodeDept"));//??????????????????
    		sonlo.getName().setXSI_TYPE("BAG_EN");
    		sonlo.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "exNameDept"));//??????????????????
    		subReq.getOccurrenceOf().getParentRequestReference().setClassCode("GROUPER");
    		subReq.getOccurrenceOf().getParentRequestReference().getId().setExtension(MsgUtils.getPropValueStr(ordMap, "ordsnParent"));//?????????id
    		
    		//???????????????1 ????????????\2 ????????????\ 9 ??????
    		PertinentInformation pre=subReq.getPertinentInformation();
    		pre.setTypeCode("PERT");
    		pre.setContextConductionInd("false");
    		pre.getObservation().setClassCode("OBS");
    		pre.getObservation().setMoodCode("EVN");
    		pre.getObservation().getCode().setCode("DE06.00.286.00");//
    		pre.getObservation().getCode().setCodeSystem("2.16.156.10011.2.2.1");
    		pre.getObservation().getCode().setCodeSystemName("???????????????????????????");
    		pre.getObservation().getValue().setXSI_TYPE("CD");
    		String euAlways=MsgUtils.getPropValueStr(ordMap, "euAlways");
    		String euCode="";
    		String euName="";
    		if("0".equals(euAlways)){
    			euCode="0";
    			euName="????????????";
    		}else if("1".equals(euAlways)){
    			euCode="1";
    			euName="????????????";
    		}else{
    			euCode="9";
    			euName="??????";
    		}
    		pre.getObservation().getValue().setCode(euCode);//??????????????????
    		pre.getObservation().getValue().setCodeSystem("2.16.156.10011.2.3.2.58");//??????????????????
    		pre.getObservation().getValue().getDisplayName().setValue(euName);//??????????????????
    		
    		Component2 sonCom=subReq.getComponent2();
    		sonCom.getSupplyRequest().setClassCode("SPLY");
    		sonCom.getSupplyRequest().setMoodCode("RQO");
    		sonCom.getSupplyRequest().getId();
    		sonCom.getSupplyRequest().getCode();
    		sonCom.getSupplyRequest().getStatusCode().setCode("active");
    		sonCom.getSupplyRequest().getQuantity().setValue(MsgUtils.getPropValueStr(ordMap, "quanAp"));
    		sonCom.getSupplyRequest().getQuantity().setUnit(MsgUtils.getPropValueStr(ordMap, "nameUnitAp"));
    		sonCom.getSupplyRequest().getExpectedUseTime().setValidTimeLow(sdm.format(MsgUtils.getPropValueDate(ordMap, "dateStart")));//??????????????????
        	String dateFormatString = MsgUtils.dateFormatString(timeFormat, MsgUtils.getPropValueDate(ordMap, "dateEnd"));//??????????????????
    		sonCom.getSupplyRequest().getExpectedUseTime().setValidTimeHigh(dateFormatString);//??????????????????
    		
    		//??????????????????
    		SubjectOf6 sub6=subReq.getSubjectOf6();
    		sub6.setTypeCode("SUBJ");
    		sub6.setContextConductionInd("false");
    		sub6.getSeperatableInd().setValue("false");
    		sub6.getAnnotation().getText().setValue(MsgUtils.getPropValueStr(ordMap, "noteOrd"));
    		sub6.getAnnotation().getStatusCode().setCode("completed");
    		sub6.getAnnotation().getAuthor().getAssignedEntity().setClassCode("ASSIGNED");
    		com2s.add(com2);
		}
    	//????????????
    	ComponentOf1 comof=sub.getPlacerGroup().getComponentOf1();
    	comof.setContextConductionInd("false");
    	Encounter enc=comof.getEncounter();
    	enc.setClassCode("ENC");
    	enc.setMoodCode("EVN");
    	List<Item> itemList =enc.getId().getItems();
    	Item itemIpt=new Item();
    	itemIpt.setExtension(MsgUtils.getPropValueStr(ordMap, "ipTimes"));
    	itemIpt.setRoot("2.16.156.10011.2.5.1.8");
    	Item itemCodeP = new Item();
    	itemCodeP.setExtension(MsgUtils.getPropValueStr(ordMap, "codePv"));//???????????????
    	itemCodeP.setRoot("2.16.156.10011.2.5.1.9");
    	itemList.add(itemIpt);
    	itemList.add(itemCodeP);
//    	enc.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "ipTimes"));
//    	enc.getId().getItem().setRoot("2.16.156.10011.2.5.1.8");
//    	enc.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "codePv"));//???????????????
//    	enc.getId().getItem().setRoot("2.16.156.10011.2.5.1.9");
    	String euPvtype=MsgUtils.getPropValueStr(ordMap, "euPvType");
    	enc.getCode().setCode(euPvtype);
    	enc.getCode().setCodeSystem("2.16.156.10011.2.3.1.271");
    	String pvtypeName="";
    	if("1".equals(euPvtype)){
    		pvtypeName="??????";
    	}else if("2".equals(euPvtype)){
    		pvtypeName="??????";
    	}else if("3".equals(euPvtype)){
    		pvtypeName="??????";
    	}else if("4".equals(euPvtype)){
    		pvtypeName="??????";
    	}else if("5".endsWith(euPvtype)){
    		pvtypeName="????????????";
    	}
    	
    	enc.getCode().setCode(euPvtype);//??????????????????
    	enc.getCode().getDisplayName().setValue(pvtypeName);
    	
    	enc.getStatusCode().setCode("active");
    	Subject ensub=enc.getSubject();
    	ensub.setTypeCode("SBJ");
    	Patient pat=ensub.getPatient();
    	pat.setClassCode("PAT");
    	List<Item> items=pat.getId().getItems();
    	Item item1=new Item();
    	item1.setRoot("2.16.156.10011.2.5.1.5");
    	item1.setExtension(MsgUtils.getPropValueStr(ordMap, ""));//???id????????????
    	Item item2=new Item();
    	item2.setRoot("2.16.156.10011.2.5.1.4");
    	//item2.setExtension(MsgUtils.getPropValueStr(ordMap, "pkPi"));//????????????
    	item2.setExtension(MsgUtils.getPropValueStr(ordMap, "codePi"));
    	Item item3=new Item();
    	item3.setRoot("2.16.156.10011.1.10");
    	item3.setExtension(MsgUtils.getPropValueStr(ordMap, "codeOp"));//????????????
    	Item item4=new Item();
    	item4.setRoot("2.16.156.10011.1.12");
    	item4.setExtension(MsgUtils.getPropValueStr(ordMap, "codeIp"));//?????????
    	items.add(item1);
    	items.add(item2);
    	items.add(item3);
    	items.add(item4);
    	pat.getTelecom().setXSI_TYPE("BAG_TEL");
    	pat.getTelecom().getItem().setValue(MsgUtils.getPropValueStr(ordMap, "telNo"));//????????????
    	pat.getStatusCode().setCode("active");
    	PatientPerson per=pat.getPatientPerson();
    	per.setClassCode("PSN");
    	per.setDeterminerCode("INSTANCE");
    	per.getId().getItem().setRoot("2.16.156.10011.1.3");
    	per.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "idNo"));//????????????
    	per.getName().setXSI_TYPE("BAG_EN");
    	per.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "namePi"));
    	
    	//??????
    	
    	per.getAdministrativeGenderCode().setCode(MsgUtils.getPropValueStr(ordMap, "sexCodeStd"));
    	per.getAdministrativeGenderCode().setCodeSystem("2.16.156.10011.2.3.3.4");
    	per.getAdministrativeGenderCode().setCodeSystemName("????????????????????????GB/T 2261.1???");
    	per.getAdministrativeGenderCode().getDisplayName().setValue(MsgUtils.getPropValueStr(ordMap, "sexNameStd"));
    	Date bDate = MsgUtils.getPropValueDate(ordMap, "birthDate");
    	String format = sd.format(bDate);
        per.getBirthTime().setValue(format);//????????????
    	//AsOtherIDs as = per.getAsOtherID();
    	List<AsOtherIDs> asOtherIDsList = per.getAsOtherIDs();
    	AsOtherIDs as = new AsOtherIDs();
    	as.setClassCode("ROL");
    	List<Item> asItems=as.getId().getItems();
    	Item asitem1=new Item();
    	asitem1.setRoot("2.16.156.10011.1.2");
    	asitem1.setExtension(MsgUtils.getPropValueStr(ordMap, ""));//??????????????????(??????)
    	Item asitem2=new Item();
    	asitem2.setRoot("2.16.156.10011.1.19");
    	asitem2.setExtension(MsgUtils.getPropValueStr(ordMap, "piHicNo"));//????????????
    	asItems.add(asitem1);
    	asItems.add(asitem2);
    	as.getScopingOrganization().setClassCode("ORG");
    	as.getScopingOrganization().setDeterminerCode("INSTANCE");
    	as.getScopingOrganization().setXSI_NIL("true");
    	asOtherIDsList.add(as);
    	Location enloc=enc.getLocation();
    	enloc.setTypeCode("LOC");
    	enloc.getTime();
    	enloc.getServiceDeliveryLocation().setClassCode("SDLOC");
    	Location serloc=enloc.getServiceDeliveryLocation().getLocation();
    	serloc.setClassCode("PLC");
    	serloc.setDeterminerCode("INSTANCE");
    	serloc.getId().getItem().setRoot("2.16.156.10011.1.22");
    	serloc.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "bedCode"));//?????????
    	serloc.getName().setXSI_TYPE("BAG_EN");
    	serloc.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "bedName"));//????????????
    	serloc.getAsLocatedEntityPartOf().setClassCode("LOCE");
    	//?????????
    	Location asloc= serloc.getAsLocatedEntityPartOf().getLocation();
    	asloc.setClassCode("PLC");
    	asloc.setDeterminerCode("INSTANCE");
    	asloc.getId().getItem().setRoot("2.16.156.10011.1.21");
    	asloc.getId().getItem().setExtension("");//????????????
    	asloc.getName().setXSI_TYPE("BAG_EN");
    	asloc.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "bedHouseno"));//????????????
    	
    	//????????????
    	ServiceProviderOrganization org= enloc.getServiceDeliveryLocation().getServiceProviderOrganization();
    	org.setClassCode("org");
    	org.setDeterminerCode("INSTANCE");
    	org.getId().getItem().setRoot("2.16.156.10011.1.26");
    	org.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "pvCodeDept"));//????????????
    	org.getName().setXSI_TYPE("BAG_EN");
    	org.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "pvNameDept"));
    	
    	//??????
    	AsOrganizationPartOf asorg= org.getAsOrganizationPartOf();
    	asorg.setClassCode("PART");
    	WholeOrganization whoorg= asorg.getWholeOrganization();
    	whoorg.setClassCode("ORG");
    	whoorg.setDeterminerCode("INSTANCE");
    	whoorg.getId().getItem().setRoot("2.16.156.10011.1.27");
    	whoorg.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "nsCodeDept"));//????????????
    	whoorg.getName().setXSI_TYPE("BAG_EN");
    	whoorg.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "nsNameDept"));//????????????
    	return req;
    }
    
    /*
     * ????????????????????????????????????    
     */
    private void createOrderFillerMessage(Request req,String action) {
    	MsgUtils.createPubReq(req, action);
    	ControlActProcess con=req.getControlActProcess();
    	con.setClassCode("CACT");
    	con.setMoodCode("EVN");
    	Subject sub = con.getSubject();
    	sub.setTypeCode("SUBJ");
    	sub.setXSI_NIL("false");
    	sub.getPlacerGroup();
    	PlacerGroup pGroup = sub.getPlacerGroup();
        pGroup.getCode();
        pGroup.getStatusCode().setCode("active");
        pGroup.getTranscriber().setTypeCode("TRANS");
        pGroup.getTranscriber().getTime();
        pGroup.getTranscriber().getTime().getAny().setValue("20110101"); //????????????
        pGroup.getTranscriber().getAssignedEntity().setClassCode("ASSIGNED");
        pGroup.getTranscriber().getAssignedEntity().getId();
        pGroup.getTranscriber().getAssignedEntity().getId().getItem().setExtension("???????????????"); //???????????????
        pGroup.getTranscriber().getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
        pGroup.getTranscriber().getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
        pGroup.getTranscriber().getAssignedEntity().getAssignedPerson().setClassCode("PSN");
        pGroup.getTranscriber().getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
        pGroup.getTranscriber().getAssignedEntity().getAssignedPerson().getName().getItem().setUse("ABC");
        pGroup.getTranscriber().getAssignedEntity().getAssignedPerson().getName().getItem().getPart().setValue("???????????????"); //???????????????
        pGroup.getLocation().setTypeCode("LOC");
        pGroup.getLocation().setXSI_NIL("false");
        pGroup.getLocation().getTime();
        pGroup.getLocation().getServiceDeliveryLocation().setClassCode("SDLOC");
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().setDeterminerCode("INSTANCE");
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().setClassCode("ORG");
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId();
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId().getItem().setExtension("??????????????????");//?????????????????????
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId().getItem().setRoot("2.16.156.10011.1.26");        
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().setXSI_TYPE("BAG_EN");
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().getItem();
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().getItem().getPart().setValue("??????????????????");//??????????????????
    	
        pGroup.getComponent2();
        pGroup.getComponent2().getObservationRequest().setClassCode("OBS");
        pGroup.getComponent2().getObservationRequest().getId();
        pGroup.getComponent2().getObservationRequest().getId().getItem().setRoot("2.16.156.10011.2.5.1.31");
        pGroup.getComponent2().getObservationRequest().getId().getItem().setExtension("?????????");//?????????
        pGroup.getComponent2().getObservationRequest().getId().getItem().setExtension("????????????");//????????????
        pGroup.getComponent2().getObservationRequest().getId().getItem().setScope("BUSN");
        pGroup.getComponent2().getObservationRequest().getId().getItem().setRoot("2.16.156.10011.1.24");
        pGroup.getComponent2().getObservationRequest().getCode().setCode("??????????????????");//??????????????????
        pGroup.getComponent2().getObservationRequest().getCode().setCodeSystem("2.16.156.10011.2.3.1.268");       
        pGroup.getComponent2().getObservationRequest().getCode().getDisplayName().setValue("????????????");
        pGroup.getComponent2().getObservationRequest().getStatusCode();
        pGroup.getComponent2().getObservationRequest().getEffectiveTime().setXSI_TYPE("IVL_TS");
        pGroup.getComponent2().getObservationRequest().getSpecimen().setTypeCode("SPC");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().setClassCode("SPEC");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getId().setRoot("2.16.156.10011.1.14");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getId().setExtension("???????????????");//????????????
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getCode();
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().setTypeCode("SBJ");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().setContextControlCode("OP");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().setMoodCode("EVN");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().setClassCode("SPECCOLLECT");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getEffectiveTime().setXSI_TYPE("IVL_TS");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getEffectiveTime().getAny().setValue("20110101");
        
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().setTypeCode("PRF");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().setClassCode("ASSIGNED");
        
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getId();
        
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getId().getItem().setExtension("?????????Id");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().setClassCode("PSN");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().getName().getItem();
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().getName().getItem().getPart().setValue("???????????????");
        
        pGroup.getComponent2().getObservationRequest().getReason().setContextConductionInd("true");
        pGroup.getComponent2().getObservationRequest().getReason().getObservation().setMoodCode("EVN");
        pGroup.getComponent2().getObservationRequest().getReason().getObservation().setClassCode("OBS");
        pGroup.getComponent2().getObservationRequest().getReason().getObservation().getCode();
        pGroup.getComponent2().getObservationRequest().getReason().getObservation().getValue().setXSI_TYPE("ST");
        pGroup.getComponent2().getObservationRequest().getReason().getObservation().getValue().setValue("??????????????????");
        pGroup.getComponent2().getObservationRequest().getComponentOf1().setContextConductionInd("true");
        pGroup.getComponent2().getObservationRequest().getComponentOf1().getProcessStep().setClassCode("PROC");
        pGroup.getComponent2().getObservationRequest().getComponentOf1().getProcessStep().getCode().setCode("??????????????????");
        pGroup.getComponent2().getObservationRequest().getComponentOf1().getProcessStep().getCode().setCodeSystem("2.16.156.10011.2.5.1.32");
        pGroup.getComponent2().getObservationRequest().getComponentOf1().getProcessStep().getCode().getDisplayName().setValue("????????????????????????");
        
        pGroup.getComponentOf1().setContextConductionInd("false");
        pGroup.getComponentOf1().setXSI_NIL("false");
        pGroup.getComponentOf1().setTypeCode("COMP");
        pGroup.getComponentOf1().getEncounter().setClassCode("ENC");
        pGroup.getComponentOf1().getEncounter().setMoodCode("EVN");
        pGroup.getComponentOf1().getEncounter().getId();
        pGroup.getComponentOf1().getEncounter().getId().getItem().setExtension("????????????");
        pGroup.getComponentOf1().getEncounter().getId().getItem().setRoot("2.16.156.10011.2.5.1.8");
        
        pGroup.getComponentOf1().getEncounter().getStatusCode();
        
        pGroup.getComponentOf1().getEncounter().getStatusCode().setCode("Active");        
        pGroup.getComponentOf1().getSubject().setTypeCode("SBJ");
        
        pGroup.getComponentOf1().getSubject().getPatient().setClassCode("PAT");
        pGroup.getComponentOf1().getSubject().getPatient().getId();        
        Item item1 = new Item();
		item1.setRoot("2.16.156.10011.2.5.1.5");
		item1.setExtension("");// ???ID
		pGroup.getComponentOf1().getSubject().getPatient().getId().getItems().add(item1);
		Item item2 = new Item();
		item2.setRoot("2.16.156.10011.2.5.1.4");
		item2.setExtension("");// ??????ID
		pGroup.getComponentOf1().getSubject().getPatient().getId().getItems().add(item2);                                        
    }
    
    /**
     * ?????? SysEsbmsg ?????????
     * @param req ????????????
     * @param action ????????????
     * @param checkvo ??????????????????
     * @param headType ???????????????
     * @param paramMap  
     * @return
     */
   private SysEsbmsg createSysEsbmsg(Request req, String action,OrderCheckMsgVo checkvo, String headType, Map<String,Object> paramMap ) {
		String reqXml=XmlProcessUtils.toRequestXml(req,req.getReqHead());
		String content = hipMessageServerUtils.getSoapXML(action, reqXml);
		//System.out.println(content);
		String pkPv = checkvo.getPkPv();
		String pkOrg = checkvo.getPkOrg();
		String id = req.getId().getExtension();
		SysEsbmsg sysEsbmsg = new SysEsbmsg(); 
		sysEsbmsg.setPkEsbmsg(NHISUUID.getKeyId());//????????????
		sysEsbmsg.setPkOrg(pkOrg);//??????
		sysEsbmsg.setIdMsg(id);//??????id
		sysEsbmsg.setPkPv(pkPv);//????????????
		sysEsbmsg.setDtEsbmsgtype(headType);//????????????
		sysEsbmsg.setContentMsg(content);//????????????
		sysEsbmsg.setEuStatus("0");//???????????????0????????????1.????????????2.???????????????
		sysEsbmsg.setDateSend(new Date());//????????????
		sysEsbmsg.setDescError("");//??????????????????
		sysEsbmsg.setCntHandle(0);//????????????
		String ipSend = MsgUtils.getPropValueStr(paramMap, action);
		if(!StringUtils.isNotBlank(ipSend)){//
			ipSend = qryIpSend(action);//?????????bd_defdoc???????????????ip
			paramMap.put(action, ipSend);
		}
		sysEsbmsg.setIpSend(ipSend);//??????IP
		sysEsbmsg.setAddrSend("");//????????????
		sysEsbmsg.setNote("");//??????
		User user = UserContext.getUser();
		sysEsbmsg.setCreator(user.getPkEmp());//?????????
		sysEsbmsg.setCreateTime(new Date());//????????????
		sysEsbmsg.setDelFlag("0");//
		sysEsbmsg.setTs(new Date());//?????????
		return sysEsbmsg;
	}
}
