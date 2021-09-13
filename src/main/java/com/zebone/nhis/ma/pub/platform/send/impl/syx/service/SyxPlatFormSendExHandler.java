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
 * 发送EX领域消息
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
	 * 发送新增，更新包含（手术）医嘱信息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	public void sendExOrderCheck(Map<String,Object> paramMap) throws Exception{ 
		String headType="";
		String action="";
		Request req=new Request();
		try {
			//获取传vo格式的医嘱核对参数
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
				//========================手术申请====================
				if("04".equals(codeOrdtype)){//手术申请
					
					if(type.indexOf("7")<0)//type 中存在 “7” 代表开启该功能
						continue;
					if("0".equals(euStatusOrd)){//新增
						headType="POOR_IN200901UV";
						action="OperationAppInfoAdd";
					}else{//作废
						headType="POOR_IN200902UV";
						action="OperationAppInfoUpdate";
					}
					req=createOpApplyMessage(headType,paramOrder);
					if(type.indexOf("7N")<0){// 控制手术 实时发送，或存表 ，"7N"代表不开启实时发送 开启存表
						hipMessageServerUtils.sendHIPMsg(req, action, "MCCI_IN000002UV01", false);//实时发送报文
						continue;
					}
				}else {
					
				
				//========================草药医嘱====================
				 //if("0103".equals(codeOrdtype)){//草药
					if(type.indexOf("3")<0)
						continue;
					if("0".equals(euStatusOrd)){//新增
						headType="POOR_IN200901UV";
						action="OrderInfoAdd";
					}else{
						headType="POOR_IN200902UV";
						action="OrderInfoUpdate";
					}
					req=createOrderMessage(headType,paramOrder);
				}
				//组装SysEsbmsg表信息
				SysEsbmsg sysEsbmsg = createSysEsbmsg(req, action, checkvo, headType,  paramMap);
				vos4save.add(sysEsbmsg);
				
				//========================西药成药====================
				/*if("0101".equals(codeOrdtype)||"0102".equals(codeOrdtype)){//西药成药
					if(type.indexOf("3")<0)
						continue;
					if("0".equals(euStatusOrd)){//新增
						headType="POOR_IN200901UV";
						action="OrderInfoAdd";
					}else{
						headType="POOR_IN200902UV";
						action="OrderInfoUpdate";
					}
					req=createOrderMessage(headType,paramOrder);
				}*/
//				hipMessageServerUtils.sendHIPMsg(req, action, "MCCI_IN000002UV01", false);
				//追加发送检查、检验消息
				//利用线程发送,提升发送的速度
//				Callable<Map<String,Object>> threadChkSend = new ThreadChkSend(req, action, "MCCI_IN000002UV01", false);
//				listCall.add(threadChkSend);
			}
			if (vos4save.size()>0) {
				//医嘱信息批量插入到数据库（包含医嘱，检验，检查，手术）
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
	 * 发送医嘱执行状态信息
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
	 * 查询医嘱信息
	 * @param paramMap
	 * @return
	 */
	private List<Map<String,Object>> qryOrderInfo(Map<String,Object> paramMap){
		return syxPlatFormSendExMapper.qryOrderInfo(paramMap);
	}
	/**
	 * 查询医嘱信息发送者IP
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
	 * 创建手术申请（新增和更新的消息）
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
			//手术申请主干
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
			itemOp.setExtension(MsgUtils.getPropValueStr(resMap, "codeApply"));//申请单号
			itemOp.setRoot("2.16.156.10011.1.24");
			itemOps.add(itemOp);
			Item itemCn=new Item();
			itemCn.setExtension(MsgUtils.getPropValueStr(resMap, "ordsn"));//医嘱号id
			itemCn.setRoot("2.16.156.10011.1.28");
			itemOps.add(itemCn);
			procedureRequest.getCode();
			procedureRequest.getText().setValue(MsgUtils.getPropValueStr(resMap, "noteOrd"));//描述
			procedureRequest.getStatusCode();
			procedureRequest.getEffectiveTime().setXSI_TYPE("IVL_TS");
			procedureRequest.getEffectiveTime().getLow().setValue(sdm.format(MsgUtils.getPropValueDate(resMap, "dateApply")));//申请时间
			procedureRequest.getEffectiveTime().setAny(null);
			procedureRequest.getStatusCode().setCode(MsgUtils.getPropValueStr(resMap, "euStatusOp")); 
			List<Item> items=req.getControlActProcess().getSubject().getProcedureRequest().getMethodCode().getItems();
			Item item1=new Item();
			item1.setCode(MsgUtils.getPropValueStr(resMap, "anaeCode"));//麻醉方式编码
			item1.setCodeSystem("2.16.156.10011.2.3.1.159");
			item1.getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "anaeName"));//麻醉方式名称
			items.add(item1);
			Item item2=new Item();
			item2.setCode(MsgUtils.getPropValueStr(resMap, "euOptype"));//手术性质编码
			item2.setCodeSystem("2.16.156.10011.2.3.1.15");
			item2.getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "optypeName"));//手术性质名称
			items.add(item2);
			procedureRequest.getMethodCode().setItems(items);
			//申请人信息
			Author author=procedureRequest.getAuthor();
			author.setTypeCode("AUT");
			author.setContextControlCode("OP");//TODO op指代的具体含义门诊OP:住院？
			author.getAssignedEntity().setClassCode("ASSIGNED");
			author.getAssignedEntity().getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "ordEmpCode"));//申请医师工号
			author.getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
			author.getAssignedEntity().getAssignedPerson().setClassCode("PSN");
			author.getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
			author.getAssignedEntity().getAssignedPerson().setXSI_NIL("false");
			author.getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("LIST_EN");
			author.getAssignedEntity().getAssignedPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "ordEmpName"));//申请医师名称
			RepresentedOrganization represent=author.getAssignedEntity().getRepresentedOrganization();
			represent.setClassCode("ORG");
			represent.setDeterminerCode("INSTANCE");
			represent.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "ordCodeDept"));//申请科室编码
			represent.getId().getItem().setRoot("2.16.156.10011.1.26");
			represent.getName().setXSI_TYPE("LIST_EN");
			represent.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "ordNameDept"));//申请科室名称
			//审核人信息
			Verifier verifier=procedureRequest.getVerifier();
			verifier.setTypeCode("VRF");
			String dateFormatString = MsgUtils.dateFormatString("YYYYMMDD", MsgUtils.getPropValueDate(resMap, "dateChk"));
			verifier.getTime().setValue(dateFormatString);//TODO 审核日期格式显示要处理YYYYMMDD
			AssignedEntity ass=verifier.getAssignedEntity();
			ass.setClassCode("ASSIGNED");
			ass.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "chkEmpCode"));//审核医师工号
			ass.getId().getItem().setRoot("2.16.156.10011.1.4");
			ass.getAssignedPerson().setClassCode("PSN");
			ass.getAssignedPerson().setDeterminerCode("INSTANCE");
			ass.getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
			ass.getAssignedPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "chkEmpName"));//审核医师姓名
			List<Component2> component2s=procedureRequest.getComponent2();
			//添加主要手术
			Component2 com=new Component2();
			com.getProcedureRequest().setClassCode("PROC");
			com.getProcedureRequest().setMoodCode("RQO");
			com.getProcedureRequest().getCode().setCodeSystem("2.16.156.10011.2.3.3.12");
			com.getProcedureRequest().getCode().setCode(MsgUtils.getPropValueStr(resMap, "diagcode"));//手术编码
			com.getProcedureRequest().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "diagname"));//手术名称
			com.getProcedureRequest().getPriorityCode().setCode(MsgUtils.getPropValueStr(resMap, "opLevelCode"));//手术等级编码
			com.getProcedureRequest().getPriorityCode().setCodeSystem("2.16.156.10011.2.3.1.258");
			com.getProcedureRequest().getPriorityCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "opLevelName"));//手术等级名称
			Performer per=com.getProcedureRequest().getPerformer();
			per.setTypeCode("PRF");
			per.getTime().getLow().setValue(sdm.format(MsgUtils.getPropValueDate(resMap, "datePlan")));//预定手术时间
			per.getAssignedEntity().setClassCode("ASSIGNED");
			per.getAssignedEntity().getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "opEmpCode"));//手术医师工号
			per.getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
			per.getAssignedEntity().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "opEmpName"));//手术医师
			AssignedPerson assi= per.getAssignedEntity().getAssignedPerson();
			assi.setClassCode("PSN");
			assi.setDeterminerCode("INSTANCE");
			assi.getName().setXSI_TYPE("LIST_EN");
			assi.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "opEmpName"));//手术医师姓名
			RepresentedOrganization reps=per.getAssignedEntity().getRepresentedOrganization();
			reps.setClassCode("ORG");
			reps.setDeterminerCode("INSTANCE");
			reps.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "opDeptCode"));//执行科室编码
			
			reps.getId().getItem().setRoot("2.16.156.10011.1.26");
			reps.getName().setXSI_TYPE("LIST_EN");
			reps.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "opDeptName"));//执行科室名称
			component2s.add(com);
			for (int i = 1; i <resultList.size(); i++) {//处理附加手术
				Component2 component2=new Component2();
				component2.getProcedureRequest().setClassCode("PROC");
				component2.getProcedureRequest().setMoodCode("RQO");
				component2.getProcedureRequest().getCode().setCodeSystem("2.16.156.10011.2.3.3.12");
				component2.getProcedureRequest().getCode().setCode(MsgUtils.getPropValueStr(resMap, "subDiagcode"));//手术编码
				component2.getProcedureRequest().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "subDiagname"));//手术名称
				component2.getProcedureRequest().getPriorityCode().setCode(MsgUtils.getPropValueStr(resMap, "opLevelCode"));//手术等级编码
				component2.getProcedureRequest().getPriorityCode().setCodeSystem("2.16.156.10011.2.3.1.258");
				component2.getProcedureRequest().getPriorityCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "opLevelName"));//手术等级名称
				Performer performer=component2.getProcedureRequest().getPerformer();
				performer.setTypeCode("PRF");
				performer.getTime().getLow().setValue(sdf.format(MsgUtils.getPropValueDate(resMap, "datePlan")));//预定手术时间
				performer.getAssignedEntity().setClassCode("ASSIGNED");
				performer.getAssignedEntity().getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "opEmpCode"));//手术医师工号
				performer.getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
				performer.getAssignedEntity().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "opEmpName"));//手术医师
				AssignedPerson assPer= performer.getAssignedEntity().getAssignedPerson();
				assPer.setClassCode("PSN");
				assPer.setDeterminerCode("INSTANCE");
				assPer.getName().setXSI_TYPE("LIST_EN");
				assPer.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "opEmpName"));//手术医师姓名
				RepresentedOrganization reporg=performer.getAssignedEntity().getRepresentedOrganization();
				reporg.setClassCode("ORG");
				reporg.setDeterminerCode("INSTANCE");
				reporg.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "opDeptCode"));//执行科室编码
				reporg.getId().getItem().setRoot("2.16.156.10011.1.26");
				reporg.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "opDeptName"));//执行科室名称
				component2s.add(component2);
			}
			//注意事项
			SubjectOf6 subjectOf6=procedureRequest.getSubjectOf6();
			subjectOf6.setContextConductionInd("true");
			subjectOf6.getSeperatableInd().setValue("false");
			subjectOf6.getAnnotation().getText().setValue("注意事项");// TODO 当前数据需要确认
			subjectOf6.getAnnotation().getStatusCode();//未确认
			subjectOf6.getAnnotation().getAuthor().setTypeCode("AUT");
			subjectOf6.getAnnotation().getAuthor().getAssignedEntity().setClassCode("ASSIGNED");
			ComponentOf1 componentOf1=procedureRequest.getComponentOf1();
			componentOf1.setContextConductionInd("false");
			componentOf1.setContextControlCode("OP");//TODO OP的具体含义需要确定
			componentOf1.setTypeCode("COMP");
			Encounter encounter=componentOf1.getEncounter();
			encounter.setClassCode("ENC");
			encounter.setMoodCode("EVN");
			List<Item> enItems=new ArrayList<Item>();
			Item enItem1=new Item();//就诊次数
			enItem1.setExtension(MsgUtils.getPropValueStr(resMap, "ipTimes"));
			enItem1.setRoot("2.16.156.10011.2.5.1.8");
			Item enItem2=new Item();//就诊流水号
			enItem2.setExtension(MsgUtils.getPropValueStr(resMap, "codePv"));
			enItem2.setRoot("2.16.156.10011.2.5.1.9");
			enItems.add(enItem1);
			enItems.add(enItem2);
			for (int i = 0; i < enItems.size(); i++) {
				encounter.getId().setItems(enItems);
			}
			encounter.getCode().setCodeSystem("2.16.156.10011.2.3.1.271");
			encounter.getCode().setCode(MsgUtils.getPropValueStr(resMap, "euPvtype"));//就诊类型编码
			encounter.getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "pvtypeName"));//就诊类别名称
			encounter.getStatusCode().setCode("active");
			String format = sdm.format(MsgUtils.getPropValueDate(resMap, "dateAdmit"));
			encounter.getEffectiveTime().getLow().setValue(format);//TODO <!--就诊(住院)日期 -->
			encounter.getEffectiveTime().setXSI_TYPE("IVL_TS");
			Subject ensub=encounter.getSubject();
			ensub.setTypeCode("SBJ");
			
			List<Item> patItems=new ArrayList<Item>();
			Item patItem1=new Item();//域id
			patItem1.setRoot("2.16.156.10011.2.5.1.5");
			patItem1.setExtension(MsgUtils.getPropValueStr(resMap, ""));//TODO 待处理（没有的）
			Item patItem2=new Item();//患者id
			patItem2.setRoot("2.16.156.10011.2.5.1.4");
			//patItem2.setExtension(MsgUtils.getPropValueStr(resMap, "pkPi"));//pk_pi患者主键
			patItem2.setExtension(MsgUtils.getPropValueStr(resMap, "codePi"));
			Item patItem3=new Item();//门诊号
			patItem3.setRoot("2.16.156.10011.1.11");
			patItem3.setExtension(MsgUtils.getPropValueStr(resMap, "codeOp"));
			Item patItem4=new Item();//住院号
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
			perItem1.setExtension(MsgUtils.getPropValueStr(resMap, "idNo"));//身份证号
			perItem1.setRoot("2.16.156.10011.1.3");
			Item perItem2=new Item();
			perItem2.setExtension(MsgUtils.getPropValueStr(resMap, "insurNo"));//医保卡号
			perItem2.setRoot("2.16.156.10011.1.15");
			perItems.add(perItem1);
			perItems.add(perItem2);
			patientPerson.getName().setXSI_TYPE("DSET_EN");
			patientPerson.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "namePi"));//姓名
			patientPerson.getTelecom().setXSI_TYPE("BAG_TEL");
			patientPerson.getTelecom().getItem().setValue(MsgUtils.getPropValueStr(resMap, "telNo"));//电话
			
			patientPerson.getAdministrativeGenderCode().setCode(MsgUtils.getPropValueStr(resMap, "sexCode"));//性别代码
			patientPerson.getAdministrativeGenderCode().setCodeSystem("2.16.156.10011.2.3.3.4");
			patientPerson.getAdministrativeGenderCode().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "sexName"));//性别名称
			Date birthDate=MsgUtils.getPropValueDate(resMap, "birthDate");
			patientPerson.getBirthTime().setValue(sd.format(birthDate));//出生日期
			Integer birth=DateUtils.getYear(new Date())- DateUtils.getYear(birthDate);
			patientPerson.getBirthTime().getOriginalText().setValue(birth.toString());//年龄
			patientPerson.getAddr().setXSI_TYPE("BAG_AD");
			patientPerson.getAddr().getItem().setUse("H");// TODO 待确定
			patientPerson.getAddr().getItem().getPart().setType("AL");//TODO 待确定
			patientPerson.getAddr().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "address")); 
			Location enLoca=encounter.getLocation();
			enLoca.setTypeCode("LOC");
			enLoca.getTime();
			enLoca.getServiceDeliveryLocation().setClassCode("SDLOC");
			enLoca.getServiceDeliveryLocation().setLocation(new Location());
			Location delLoca=enLoca.getServiceDeliveryLocation().getLocation();
			delLoca.setClassCode("PLC");
			delLoca.setDeterminerCode("INSTANCE");
			delLoca.getId().getItem().setValue(MsgUtils.getPropValueStr(resMap, "bedCode"));//TODO 病床编码
			delLoca.getName().setXSI_TYPE("BAG_EN");
			delLoca.getName().getItem().setUse("IDE");
			delLoca.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "bedName"));//床号
			delLoca.getAsLocatedEntityPartOf().setClassCode("LOCE");
			delLoca.getAsLocatedEntityPartOf().setLocation(new Location());
			Location asLoca= delLoca.getAsLocatedEntityPartOf().getLocation();
			asLoca.setClassCode("PLC");
			asLoca.setDeterminerCode("INSTANCE");
			asLoca.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, ""));//病房编码（没有）
			asLoca.getName().setXSI_TYPE("BAG_EN");
			asLoca.getName().getItem().setUse("IDE");
			asLoca.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "houseno"));//病房号
			ServiceProviderOrganization organ= enLoca.getServiceProviderOrganization();
			organ.setClassCode("ORG");
			organ.setDeterminerCode("INSTANCE");
			organ.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "pvDeptCode"));//科室编码
			organ.getName().setXSI_TYPE("BAG_EN");
			organ.getName().getItem().setUse("IDE");
			organ.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "pvDeptName"));//科室名称
			organ.getAsOrganizationPartOf().setClassCode("PART");
			WholeOrganization whole= organ.getAsOrganizationPartOf().getWholeOrganization();
			whole.setClassCode("ORG");
			whole.setDeterminerCode("INSTANCE");
			whole.getId().getItem().setExtension(MsgUtils.getPropValueStr(resMap, "nsDeptCode"));//病区编码
			whole.getName().setXSI_TYPE("BAG_EN");
			whole.getName().getItem().setUse("IDE");
			whole.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(resMap, "nsDeptName"));//病区名称
			PertinentInformation1 pertinent=encounter.getPertinentInformation1();
			pertinent.setTypeCode("PERT");
			pertinent.setXSI_NIL("false");
			pertinent.setObservationDx(new ObservationDx());
			ObservationDx obser= pertinent.getObservationDx();
			obser.setClassCode("OBS");
			obser.setMoodCode("EVN");
			obser.getCode().setCode("0101");//TODO 诊断类别编码
			obser.getCode().setCodeSystem("2.16.156.10011.2.5.1.10");
			obser.getCode().getDisplayName().setValue("术前诊断");//诊断类别名称
			obser.getStatusCode().setCode("active");
			obser.getValue().setCode(MsgUtils.getPropValueStr(resMap, "codeCd"));//疾病代码
			obser.getValue().setCodeSystem("2.16.156.10011.2.3.3.11");
			obser.getValue().getDisplayName().setValue(MsgUtils.getPropValueStr(resMap, "nameCd"));//疾病名称
			return req;
    }

    /**
     * 创建医嘱信息
     * @param req
     * @param action
     */
    private Request createOrderMessage(String action,Map<String,Object> paramMap){
    	List<Map<String,Object>> orderList=qryOrderInfo(paramMap);
    	if(orderList==null || orderList.size()<=0)return null;
    	String timeFormat = "yyyyMMddHHmmss";//时间类型
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
    	
    	//医嘱开立者
    	Author aut=sub.getPlacerGroup().getAuthor();
    	aut.setTypeCode("AUT");
    	aut.setContextControlCode("OP");//TODO 是否是门诊/住院的标识符
    	String dateEnterStr = MsgUtils.dateFormatString(timeFormat, MsgUtils.getPropValueDate(ordMap, "dateEnter"));//TODO 医嘱开立时间
    	aut.getTime().setValue(dateEnterStr);//TODO 医嘱开立时间
    	aut.getSignatureCode().setCode("S");
    	aut.getSignatureText().setValue(MsgUtils.getPropValueStr(ordMap, "nameEmpInput"));//TODO 医嘱开立者即录入者（name_emp_input 录入者名称）
    	AssignedEntity entity=aut.getAssignedEntity();
    	entity.setClassCode("ASSIGNED");
    	entity.getId().getItem().setRoot("2.16.156.10011.1.4");
    	entity.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "inCodeEmp"));//TODO 医务人员工号
    	entity.getAssignedPerson().setClassCode("PSN");
    	entity.getAssignedPerson().setDeterminerCode("INSTANCE");
    	entity.getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
    	entity.getAssignedPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "nameEmpInput"));//医嘱开立者
    	RepresentedOrganization reporg=entity.getRepresentedOrganization();
    	reporg.setClassCode("ORG");
    	//MsgUtils
    	reporg.setDeterminerCode("INSTANCE");
    	reporg.getId().getItem().setRoot("2.16.156.10011.1.26");
    	reporg.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "inCodeDept"));//TODO 开立科室编码
    	reporg.getName().setXSI_TYPE("BAG_EN");
    	reporg.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "inNameDept"));//TODO 开立科室名称
    	
    	//医嘱审核者
    	Verifier veri=sub.getPlacerGroup().getVerifier();
    	veri.setTypeCode("VRF");
    	veri.setContextControlCode("OP");//TODO 是否是门诊/住院的标识符
    	String dateChk = MsgUtils.dateFormatString(timeFormat, MsgUtils.getPropValueDate(ordMap, "dateChk"));
    	veri.getTime().setValue(dateChk);//审核时间
    	veri.getSignatureCode().setCode("S");
    	veri.getSignatureText().setValue(MsgUtils.getPropValueStr(ordMap, "codeEmpChk"));//TODO 医嘱审核者签名
    	AssignedEntity veriEnt=veri.getAssignedEntity();
    	veriEnt.setClassCode("ASSIGNED");
    	veriEnt.getId().getItem().setRoot("2.16.156.10011.1.4");
    	veriEnt.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "codeEmpChk"));//TODO 医务人员工号
    	veriEnt.getAssignedPerson().setClassCode("PSN");
    	veriEnt.getAssignedPerson().setDeterminerCode("INSTANCE");
    	veriEnt.getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
    	veriEnt.getAssignedPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "nameEmpChk"));//医嘱审核者
    	
    	List<Component2> com2s=sub.getPlacerGroup().getComponent2s();//1..*
    	for (int i = 0; i < orderList.size(); i++) {
    		
    		Map<String, java.lang.Object> map = orderList.get(i);
    		
    		Component2 com2=new Component2();
    		if("POOR_IN200901UV".equals(action)){
    			com2.getSequenceNumber().setValue(MsgUtils.getPropValueStr(paramMap, "pkCnord"));//医嘱序号
    		}else{
    			com2.getSequenceNumber().setValue(MsgUtils.getPropValueStr(ordMap, "ordsn"));//医嘱序号
    		}
    		SubstanceAdministrationRequest subReq=com2.getSubstanceAdministrationRequest();
    		subReq.setClassCode("SBADM");
    		subReq.setModdCode("RQO");
    		subReq.getId().setRoot("2.16.156.10011.1.28");
    		subReq.getId().setExtension(MsgUtils.getPropValueStr(ordMap, "ordsn"));//医嘱id
    		subReq.getCode().setCode(MsgUtils.getPropValueStr(ordMap, "codeOrdtype"));//医嘱项目类型代码
    		subReq.getCode().setCodeSystem("2.16.156.10011.2.3.1.268");
    		subReq.getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(ordMap, "nameOrdtype"));
    		
    		//subReq.setOrdCode(MsgUtils.getPropValueStr(ordMap, "codeOrd"));
    		//subReq.setOrdName(MsgUtils.getPropValueStr(ordMap, "nameOrd"));
    		
    		subReq.getText();
    		//subReq.getStatusCode().setCode("active");
    		subReq.getStatusCode().setCode(MsgUtils.getPropValueStr(ordMap, "euStatusOrd"));
    		subReq.getEffectiveTime().setXSI_TYPE("QSC_TS");
    		subReq.getEffectiveTime().setValidTimeLow(sdf.format(MsgUtils.getPropValueDate(ordMap, "dateStart")));//开始时间
        	String dateEnd = MsgUtils.dateFormatString(timeFormat, MsgUtils.getPropValueDate(ordMap, "dateStop"));//获取结束时间
    		subReq.getEffectiveTime().setValidTimeHigh(dateEnd);//结束
    		
    		subReq.getEffectiveTime().getCode().setCode(MsgUtils.getPropValueStr(ordMap, "codeFreq"));//频次
    		subReq.getEffectiveTime().getCode().setCodeSystem("2.16.156.10011.2.5.1.13");
    		subReq.getEffectiveTime().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(ordMap, "freqName"));//频次名称
    		
    		subReq.getRouteCode().setCode(MsgUtils.getPropValueStr(ordMap, "codeSupply"));// 
    		subReq.getRouteCode().setCodeSystem("2.16.156.10011.2.3.1.158");
    		subReq.getRouteCode().setCodeSystemName("用药途径代码表");//用药途径代码表
    		subReq.getRouteCode().getDisplayName().setValue(MsgUtils.getPropValueStr(ordMap, "nameSupply"));//名称
    		
    		subReq.getDoseQuantity().setValue(MsgUtils.getPropValueStr(ordMap, "dosage"));//用药剂量-单次
    		subReq.getDoseQuantity().setUnit(MsgUtils.getPropValueStr(ordMap, "nameDos"));//单位
    		
    		subReq.getDoseCheckQuantity().setXSI_TYPE("DSET_RTO");
    		subReq.getDoseCheckQuantity().getItem().getNumerator().setXSI_TYPE("PQ");
    		subReq.getDoseCheckQuantity().getItem().getNumerator().setUnit(MsgUtils.getPropValueStr(ordMap, "nameQuan"));//单位
    		subReq.getDoseCheckQuantity().getItem().getNumerator().setValue(MsgUtils.getPropValueStr(ordMap, "quan"));//值
    		subReq.getDoseCheckQuantity().getItem().getDenominator().setXSI_TYPE("PQ");
    		subReq.getDoseCheckQuantity().getItem().getDenominator().setUnit("d");//单位
    		subReq.getDoseCheckQuantity().getItem().getDenominator().setValue(MsgUtils.getPropValueStr(ordMap, "days"));//值
			
    		//药物剂型 
    		subReq.getAdministrationUnitCode().setCode(MsgUtils.getPropValueStr(ordMap, "pddocCodeStd"));//获取码表对应的编码
    		subReq.getAdministrationUnitCode().setCodeSystem("2.16.156.10011.2.3.1.211");
    		subReq.getAdministrationUnitCode().setCodeSystemName("药物剂型代码表");
    		subReq.getAdministrationUnitCode().getDisplayName().setValue(MsgUtils.getPropValueStr(ordMap, "pddocNameStd"));//获取码表对应的名称
    		
    		//药物信息
    		Consumable2 con2=subReq.getConsumable2();
    		con2.setTypeCode("CSM");
    		ManufacturedProduct1 man1=con2.getManufacturedProduct1();
    		man1.setClassCode("MANU");	
    		man1.getId().setExtension(MsgUtils.getPropValueStr(ordMap, "apprNo"));//TODO 包装序号 （批准文号/注册）
    		ManufacturedProduct man=man1.getManufacturedProduct();
    		man.setClassCode("MMAT");
    		man.setDeterminerCode("KIND");
    		man.getCode().setCode(MsgUtils.getPropValueStr(ordMap, "codeOrd"));//药码
    		man.getCode().setCodeSystem("2.16.156.10011.2.5.1.14");
    		man.getName().setXSI_TYPE("BAG_EN");
    		man.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "nameOrd"));//药名
    		man.getAsContent().setClassCode("CONT");
    		man.getAsContent().getQuantity();
    		ContainerPackagedProduct pro= man.getAsContent().getContainerPackagedProduct();
    		pro.setClassCode("HOLD");
    		pro.setDeterminerCode("KIND");
    		pro.getCode();
    		pro.getFormCode();
    		//TODO 药物规格 需要确定医疗含量还是基本单位  
    		pro.getCapacityQuantity().setUnit(MsgUtils.getPropValueStr(ordMap, "nameDos"));
    		pro.getCapacityQuantity().setValue(MsgUtils.getPropValueStr(ordMap, "nameQuan"));
    		
    		//TODO 药物所属医保信息  医保数据待确定
    		SubjectOf3 sub3=man1.getSubjectOf3();
    		sub3.setTypeCode("SBJ");
    		sub3.getPolicy().setClassCode("POLICY");
    		sub3.getPolicy().setMoodCode("EVN");
    		//!-- 药物医保类别编码/药物医保类别名称 -->
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
    		sonlo.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "exCodeDept"));//执行科室编码
    		sonlo.getName().setXSI_TYPE("BAG_EN");
    		sonlo.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "exNameDept"));//执行科室名称
    		subReq.getOccurrenceOf().getParentRequestReference().setClassCode("GROUPER");
    		subReq.getOccurrenceOf().getParentRequestReference().getId().setExtension(MsgUtils.getPropValueStr(ordMap, "ordsnParent"));//父医嘱id
    		
    		//医嘱类别（1 长期医嘱\2 临时医嘱\ 9 其他
    		PertinentInformation pre=subReq.getPertinentInformation();
    		pre.setTypeCode("PERT");
    		pre.setContextConductionInd("false");
    		pre.getObservation().setClassCode("OBS");
    		pre.getObservation().setMoodCode("EVN");
    		pre.getObservation().getCode().setCode("DE06.00.286.00");//
    		pre.getObservation().getCode().setCodeSystem("2.16.156.10011.2.2.1");
    		pre.getObservation().getCode().setCodeSystemName("卫生信息数据元目录");
    		pre.getObservation().getValue().setXSI_TYPE("CD");
    		String euAlways=MsgUtils.getPropValueStr(ordMap, "euAlways");
    		String euCode="";
    		String euName="";
    		if("0".equals(euAlways)){
    			euCode="0";
    			euName="长期医嘱";
    		}else if("1".equals(euAlways)){
    			euCode="1";
    			euName="临时医嘱";
    		}else{
    			euCode="9";
    			euName="其他";
    		}
    		pre.getObservation().getValue().setCode(euCode);//医嘱类别编码
    		pre.getObservation().getValue().setCodeSystem("2.16.156.10011.2.3.2.58");//医嘱类别名称
    		pre.getObservation().getValue().getDisplayName().setValue(euName);//医嘱类别名称
    		
    		Component2 sonCom=subReq.getComponent2();
    		sonCom.getSupplyRequest().setClassCode("SPLY");
    		sonCom.getSupplyRequest().setMoodCode("RQO");
    		sonCom.getSupplyRequest().getId();
    		sonCom.getSupplyRequest().getCode();
    		sonCom.getSupplyRequest().getStatusCode().setCode("active");
    		sonCom.getSupplyRequest().getQuantity().setValue(MsgUtils.getPropValueStr(ordMap, "quanAp"));
    		sonCom.getSupplyRequest().getQuantity().setUnit(MsgUtils.getPropValueStr(ordMap, "nameUnitAp"));
    		sonCom.getSupplyRequest().getExpectedUseTime().setValidTimeLow(sdm.format(MsgUtils.getPropValueDate(ordMap, "dateStart")));//医嘱开始时间
        	String dateFormatString = MsgUtils.dateFormatString(timeFormat, MsgUtils.getPropValueDate(ordMap, "dateEnd"));//获取结束时间
    		sonCom.getSupplyRequest().getExpectedUseTime().setValidTimeHigh(dateFormatString);//医嘱结束时间
    		
    		//医嘱备注信息
    		SubjectOf6 sub6=subReq.getSubjectOf6();
    		sub6.setTypeCode("SUBJ");
    		sub6.setContextConductionInd("false");
    		sub6.getSeperatableInd().setValue("false");
    		sub6.getAnnotation().getText().setValue(MsgUtils.getPropValueStr(ordMap, "noteOrd"));
    		sub6.getAnnotation().getStatusCode().setCode("completed");
    		sub6.getAnnotation().getAuthor().getAssignedEntity().setClassCode("ASSIGNED");
    		com2s.add(com2);
		}
    	//就医信息
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
    	itemCodeP.setExtension(MsgUtils.getPropValueStr(ordMap, "codePv"));//就诊流水号
    	itemCodeP.setRoot("2.16.156.10011.2.5.1.9");
    	itemList.add(itemIpt);
    	itemList.add(itemCodeP);
//    	enc.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "ipTimes"));
//    	enc.getId().getItem().setRoot("2.16.156.10011.2.5.1.8");
//    	enc.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "codePv"));//就诊流水号
//    	enc.getId().getItem().setRoot("2.16.156.10011.2.5.1.9");
    	String euPvtype=MsgUtils.getPropValueStr(ordMap, "euPvType");
    	enc.getCode().setCode(euPvtype);
    	enc.getCode().setCodeSystem("2.16.156.10011.2.3.1.271");
    	String pvtypeName="";
    	if("1".equals(euPvtype)){
    		pvtypeName="门诊";
    	}else if("2".equals(euPvtype)){
    		pvtypeName="急诊";
    	}else if("3".equals(euPvtype)){
    		pvtypeName="住院";
    	}else if("4".equals(euPvtype)){
    		pvtypeName="体检";
    	}else if("5".endsWith(euPvtype)){
    		pvtypeName="家庭病床";
    	}
    	
    	enc.getCode().setCode(euPvtype);//就诊类别编码
    	enc.getCode().getDisplayName().setValue(pvtypeName);
    	
    	enc.getStatusCode().setCode("active");
    	Subject ensub=enc.getSubject();
    	ensub.setTypeCode("SBJ");
    	Patient pat=ensub.getPatient();
    	pat.setClassCode("PAT");
    	List<Item> items=pat.getId().getItems();
    	Item item1=new Item();
    	item1.setRoot("2.16.156.10011.2.5.1.5");
    	item1.setExtension(MsgUtils.getPropValueStr(ordMap, ""));//域id设置为空
    	Item item2=new Item();
    	item2.setRoot("2.16.156.10011.2.5.1.4");
    	//item2.setExtension(MsgUtils.getPropValueStr(ordMap, "pkPi"));//患者主键
    	item2.setExtension(MsgUtils.getPropValueStr(ordMap, "codePi"));
    	Item item3=new Item();
    	item3.setRoot("2.16.156.10011.1.10");
    	item3.setExtension(MsgUtils.getPropValueStr(ordMap, "codeOp"));//门急诊号
    	Item item4=new Item();
    	item4.setRoot("2.16.156.10011.1.12");
    	item4.setExtension(MsgUtils.getPropValueStr(ordMap, "codeIp"));//住院号
    	items.add(item1);
    	items.add(item2);
    	items.add(item3);
    	items.add(item4);
    	pat.getTelecom().setXSI_TYPE("BAG_TEL");
    	pat.getTelecom().getItem().setValue(MsgUtils.getPropValueStr(ordMap, "telNo"));//联系电话
    	pat.getStatusCode().setCode("active");
    	PatientPerson per=pat.getPatientPerson();
    	per.setClassCode("PSN");
    	per.setDeterminerCode("INSTANCE");
    	per.getId().getItem().setRoot("2.16.156.10011.1.3");
    	per.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "idNo"));//身份证号
    	per.getName().setXSI_TYPE("BAG_EN");
    	per.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "namePi"));
    	
    	//性别
    	
    	per.getAdministrativeGenderCode().setCode(MsgUtils.getPropValueStr(ordMap, "sexCodeStd"));
    	per.getAdministrativeGenderCode().setCodeSystem("2.16.156.10011.2.3.3.4");
    	per.getAdministrativeGenderCode().setCodeSystemName("生理性别代码表（GB/T 2261.1）");
    	per.getAdministrativeGenderCode().getDisplayName().setValue(MsgUtils.getPropValueStr(ordMap, "sexNameStd"));
    	Date bDate = MsgUtils.getPropValueDate(ordMap, "birthDate");
    	String format = sd.format(bDate);
        per.getBirthTime().setValue(format);//出生日期
    	//AsOtherIDs as = per.getAsOtherID();
    	List<AsOtherIDs> asOtherIDsList = per.getAsOtherIDs();
    	AsOtherIDs as = new AsOtherIDs();
    	as.setClassCode("ROL");
    	List<Item> asItems=as.getId().getItems();
    	Item asitem1=new Item();
    	asitem1.setRoot("2.16.156.10011.1.2");
    	asitem1.setExtension(MsgUtils.getPropValueStr(ordMap, ""));//健康档案编码(没有)
    	Item asitem2=new Item();
    	asitem2.setRoot("2.16.156.10011.1.19");
    	asitem2.setExtension(MsgUtils.getPropValueStr(ordMap, "piHicNo"));//健康卡号
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
    	serloc.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "bedCode"));//病床号
    	serloc.getName().setXSI_TYPE("BAG_EN");
    	serloc.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "bedName"));//病床名称
    	serloc.getAsLocatedEntityPartOf().setClassCode("LOCE");
    	//病房号
    	Location asloc= serloc.getAsLocatedEntityPartOf().getLocation();
    	asloc.setClassCode("PLC");
    	asloc.setDeterminerCode("INSTANCE");
    	asloc.getId().getItem().setRoot("2.16.156.10011.1.21");
    	asloc.getId().getItem().setExtension("");//病房编码
    	asloc.getName().setXSI_TYPE("BAG_EN");
    	asloc.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "bedHouseno"));//所在房间
    	
    	//科室信息
    	ServiceProviderOrganization org= enloc.getServiceDeliveryLocation().getServiceProviderOrganization();
    	org.setClassCode("org");
    	org.setDeterminerCode("INSTANCE");
    	org.getId().getItem().setRoot("2.16.156.10011.1.26");
    	org.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "pvCodeDept"));//科室编码
    	org.getName().setXSI_TYPE("BAG_EN");
    	org.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "pvNameDept"));
    	
    	//病区
    	AsOrganizationPartOf asorg= org.getAsOrganizationPartOf();
    	asorg.setClassCode("PART");
    	WholeOrganization whoorg= asorg.getWholeOrganization();
    	whoorg.setClassCode("ORG");
    	whoorg.setDeterminerCode("INSTANCE");
    	whoorg.getId().getItem().setRoot("2.16.156.10011.1.27");
    	whoorg.getId().getItem().setExtension(MsgUtils.getPropValueStr(ordMap, "nsCodeDept"));//病区编码
    	whoorg.getName().setXSI_TYPE("BAG_EN");
    	whoorg.getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(ordMap, "nsNameDept"));//病区名称
    	return req;
    }
    
    /*
     * 创建医嘱执行状态更新消息    
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
        pGroup.getTranscriber().getTime().getAny().setValue("20110101"); //操作日期
        pGroup.getTranscriber().getAssignedEntity().setClassCode("ASSIGNED");
        pGroup.getTranscriber().getAssignedEntity().getId();
        pGroup.getTranscriber().getAssignedEntity().getId().getItem().setExtension("操作人编码"); //操作人编码
        pGroup.getTranscriber().getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
        pGroup.getTranscriber().getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
        pGroup.getTranscriber().getAssignedEntity().getAssignedPerson().setClassCode("PSN");
        pGroup.getTranscriber().getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
        pGroup.getTranscriber().getAssignedEntity().getAssignedPerson().getName().getItem().setUse("ABC");
        pGroup.getTranscriber().getAssignedEntity().getAssignedPerson().getName().getItem().getPart().setValue("操作人姓名"); //操作人姓名
        pGroup.getLocation().setTypeCode("LOC");
        pGroup.getLocation().setXSI_NIL("false");
        pGroup.getLocation().getTime();
        pGroup.getLocation().getServiceDeliveryLocation().setClassCode("SDLOC");
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().setDeterminerCode("INSTANCE");
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().setClassCode("ORG");
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId();
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId().getItem().setExtension("执行科室编码");//执行科室的编码
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId().getItem().setRoot("2.16.156.10011.1.26");        
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().setXSI_TYPE("BAG_EN");
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().getItem();
        pGroup.getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().getItem().getPart().setValue("执行科室名称");//科室执行名称
    	
        pGroup.getComponent2();
        pGroup.getComponent2().getObservationRequest().setClassCode("OBS");
        pGroup.getComponent2().getObservationRequest().getId();
        pGroup.getComponent2().getObservationRequest().getId().getItem().setRoot("2.16.156.10011.2.5.1.31");
        pGroup.getComponent2().getObservationRequest().getId().getItem().setExtension("医嘱号");//医嘱号
        pGroup.getComponent2().getObservationRequest().getId().getItem().setExtension("申请单号");//申请单号
        pGroup.getComponent2().getObservationRequest().getId().getItem().setScope("BUSN");
        pGroup.getComponent2().getObservationRequest().getId().getItem().setRoot("2.16.156.10011.1.24");
        pGroup.getComponent2().getObservationRequest().getCode().setCode("医嘱类别编码");//医嘱类别编码
        pGroup.getComponent2().getObservationRequest().getCode().setCodeSystem("2.16.156.10011.2.3.1.268");       
        pGroup.getComponent2().getObservationRequest().getCode().getDisplayName().setValue("片剂药品");
        pGroup.getComponent2().getObservationRequest().getStatusCode();
        pGroup.getComponent2().getObservationRequest().getEffectiveTime().setXSI_TYPE("IVL_TS");
        pGroup.getComponent2().getObservationRequest().getSpecimen().setTypeCode("SPC");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().setClassCode("SPEC");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getId().setRoot("2.16.156.10011.1.14");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getId().setExtension("标本条码号");//标本条码
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
        
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getId().getItem().setExtension("采集人Id");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().setClassCode("PSN");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().getName().getItem();
        pGroup.getComponent2().getObservationRequest().getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().getName().getItem().getPart().setValue("采集人姓名");
        
        pGroup.getComponent2().getObservationRequest().getReason().setContextConductionInd("true");
        pGroup.getComponent2().getObservationRequest().getReason().getObservation().setMoodCode("EVN");
        pGroup.getComponent2().getObservationRequest().getReason().getObservation().setClassCode("OBS");
        pGroup.getComponent2().getObservationRequest().getReason().getObservation().getCode();
        pGroup.getComponent2().getObservationRequest().getReason().getObservation().getValue().setXSI_TYPE("ST");
        pGroup.getComponent2().getObservationRequest().getReason().getObservation().getValue().setValue("医嘱撤消原因");
        pGroup.getComponent2().getObservationRequest().getComponentOf1().setContextConductionInd("true");
        pGroup.getComponent2().getObservationRequest().getComponentOf1().getProcessStep().setClassCode("PROC");
        pGroup.getComponent2().getObservationRequest().getComponentOf1().getProcessStep().getCode().setCode("医嘱执行状态");
        pGroup.getComponent2().getObservationRequest().getComponentOf1().getProcessStep().getCode().setCodeSystem("2.16.156.10011.2.5.1.32");
        pGroup.getComponent2().getObservationRequest().getComponentOf1().getProcessStep().getCode().getDisplayName().setValue("医嘱执行状态名称");
        
        pGroup.getComponentOf1().setContextConductionInd("false");
        pGroup.getComponentOf1().setXSI_NIL("false");
        pGroup.getComponentOf1().setTypeCode("COMP");
        pGroup.getComponentOf1().getEncounter().setClassCode("ENC");
        pGroup.getComponentOf1().getEncounter().setMoodCode("EVN");
        pGroup.getComponentOf1().getEncounter().getId();
        pGroup.getComponentOf1().getEncounter().getId().getItem().setExtension("就诊次数");
        pGroup.getComponentOf1().getEncounter().getId().getItem().setRoot("2.16.156.10011.2.5.1.8");
        
        pGroup.getComponentOf1().getEncounter().getStatusCode();
        
        pGroup.getComponentOf1().getEncounter().getStatusCode().setCode("Active");        
        pGroup.getComponentOf1().getSubject().setTypeCode("SBJ");
        
        pGroup.getComponentOf1().getSubject().getPatient().setClassCode("PAT");
        pGroup.getComponentOf1().getSubject().getPatient().getId();        
        Item item1 = new Item();
		item1.setRoot("2.16.156.10011.2.5.1.5");
		item1.setExtension("");// 域ID
		pGroup.getComponentOf1().getSubject().getPatient().getId().getItems().add(item1);
		Item item2 = new Item();
		item2.setRoot("2.16.156.10011.2.5.1.4");
		item2.setExtension("");// 患者ID
		pGroup.getComponentOf1().getSubject().getPatient().getId().getItems().add(item2);                                        
    }
    
    /**
     * 组装 SysEsbmsg 表数据
     * @param req 报文对象
     * @param action 服务编码
     * @param checkvo 发送医嘱对象
     * @param headType 头文件类型
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
		sysEsbmsg.setPkEsbmsg(NHISUUID.getKeyId());//消息主键
		sysEsbmsg.setPkOrg(pkOrg);//机构
		sysEsbmsg.setIdMsg(id);//消息id
		sysEsbmsg.setPkPv(pkPv);//患者主键
		sysEsbmsg.setDtEsbmsgtype(headType);//消息类型
		sysEsbmsg.setContentMsg(content);//消息内容
		sysEsbmsg.setEuStatus("0");//消息状态（0未处理，1.已处理，2.处理失败）
		sysEsbmsg.setDateSend(new Date());//发送时间
		sysEsbmsg.setDescError("");//操作失败描述
		sysEsbmsg.setCntHandle(0);//操作次数
		String ipSend = MsgUtils.getPropValueStr(paramMap, action);
		if(!StringUtils.isNotBlank(ipSend)){//
			ipSend = qryIpSend(action);//从码表bd_defdoc获取发送者ip
			paramMap.put(action, ipSend);
		}
		sysEsbmsg.setIpSend(ipSend);//发送IP
		sysEsbmsg.setAddrSend("");//发送地址
		sysEsbmsg.setNote("");//备注
		User user = UserContext.getUser();
		sysEsbmsg.setCreator(user.getPkEmp());//创建人
		sysEsbmsg.setCreateTime(new Date());//创建时间
		sysEsbmsg.setDelFlag("0");//
		sysEsbmsg.setTs(new Date());//时间戳
		return sysEsbmsg;
	}
}
