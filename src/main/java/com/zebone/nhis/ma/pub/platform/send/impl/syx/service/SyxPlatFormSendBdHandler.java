package com.zebone.nhis.ma.pub.platform.send.impl.syx.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.dao.SyxPlatFormSendBdMapper;
import com.zebone.nhis.ma.pub.platform.syx.support.HIPMessageServerUtils;
import com.zebone.nhis.ma.pub.platform.syx.support.MsgUtils;
import com.zebone.nhis.ma.pub.platform.syx.support.XmlProcessUtils;
import com.zebone.nhis.ma.pub.platform.syx.vo.AsAffiliate;
import com.zebone.nhis.ma.pub.platform.syx.vo.AssignedEntity;
import com.zebone.nhis.ma.pub.platform.syx.vo.AssignedPrincipalOrganization;
import com.zebone.nhis.ma.pub.platform.syx.vo.Author;
import com.zebone.nhis.ma.pub.platform.syx.vo.Request;
import com.zebone.nhis.ma.pub.platform.syx.vo.Response;
import com.zebone.nhis.ma.pub.platform.syx.vo.Subject;
import com.zebone.nhis.ma.pub.platform.syx.vo.Subject1;
import com.zebone.nhis.ma.pub.platform.syx.vo.ValueSetItems;
import com.zebone.nhis.ma.pub.service.SysLogService;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
/**
 * 发送BD领域消息
 * @author IBM
 *
 */
@Service
public class SyxPlatFormSendBdHandler {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat  sd= new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat  sdm= new SimpleDateFormat("yyyyMMddHHmm");

	
	@Resource 
	private SyxPlatFormSendBdMapper syxPlatFormSendBdMapper;
	
	@Resource
	private SyxPlatFormSaveTabService syxPlatFormSaveTabService;
	
	private HIPMessageServerUtils hipMessageServerUtils = new HIPMessageServerUtils();
	
	/**
	 * 发送科室数据信息
	 * @param paramMap
	 */
	public void sendDeptMsg(Map<String,Object> paramMap){
		try {
			BdOuDept dept=(BdOuDept) paramMap.get("dept");
			String status=(String) paramMap.get("STATUS");
			String TXt="";
			switch (status) {
			case "_ADD":
				TXt = convertDeptToRequestXml(dept, true, "PRPM_IN401030UV01.xsd");
				hipMessageServerUtils.sendHIPServiceCusTomAdds("OrganizationInfoRegister", TXt);
				break;
			case "_UPDATE":
				TXt = convertDeptToRequestXml(dept, false, "PRPM_IN403010UV01.xsd");
				hipMessageServerUtils.sendHIPServiceCusTomAdds("OrganizationInfoUpdate", TXt);
				break;
			case "_DE":
				convertDeptToRequestXml(dept, false, "PRPM_IN403010UV01.xsd");
					break;
			default://否则走查询服务
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private String convertDeptToRequestXml(BdOuDept dept,boolean flagAdd,String xsd){
	    
		Request req = new Request(xsd);
		if(flagAdd){
			createDeptMsg(req,dept,"PRPM_IN401030UV01");
		}else{
			createDeptMsg(req,dept,"PRPM_IN403010UV01");
		}
		//公共属性设置
		//req.getId().setRoot("2.16.156.10011.2.5.1.1");
		//翻译字典类字段方法，BdPubService.getBdDefDocStd
		
		return XmlProcessUtils.toRequestXml(req,xsd.substring(0, xsd.indexOf(".")));
    }
	
	/**
	 * 创建科室信息
	 * @param req
	 * @param paramList
	 */
	private void createDeptMsg(Request req,BdOuDept dept,String action){
		
		List<Map<String,Object>> deptList=syxPlatFormSendBdMapper.qryDeptInfo(dept.getPkDept());
		if(deptList==null ||deptList.size()<=0)return;
		Map<String,Object> deptMap=deptList.get(0);				
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
						
		req.getControlActProcess().setClassCode("CACT");
		req.getControlActProcess().setMoodCode("EVN");
		
		Subject subject=req.getControlActProcess().getSubject();
		subject.setTypeCode("SUBJ");
		subject.getRegistrationRequest().setClassCode("REG");
		subject.getRegistrationRequest().setMoodCode("RQO");
		Map<String,Object> flagActiveMap = deptList.get(deptList.size()-1);
		subject.getRegistrationRequest().getStatusCode().setCode(MsgUtils.getPropValueStr(flagActiveMap, "flagActive"));
		
		Subject1 sub1=subject.getRegistrationRequest().getSubject1();
		sub1.setTypeCode("SBJ");
		AssignedEntity assignedEntity=sub1.getAssignedEntity();
		assignedEntity.setClassCode("ASSIGNED");
		assignedEntity.getId().getItem().setRoot("2.16.156.10011.1.26");
		/*List<Part> parts =assignedEntity.getName().getItem().getParts();
		for (Map<String, Object> map : deptList) {
			Part part=new Part();
			part.setValue(MsgUtils.getPropValueStr(map, "deptTypeName"));
			parts.add(part);
		}*/		
		assignedEntity.getId().getItem().setExtension(MsgUtils.getPropValueStr(flagActiveMap, "pkDept"));//TODO 科室编码
		assignedEntity.getCode().setCode(MsgUtils.getPropValueStr(flagActiveMap, "deptTypeCode"));//TODO CV99.14.001 
		assignedEntity.getCode().setCodeSystem("2.16.156.10011.2.3.2.62");
		//assignedEntity.getCode().setCodeSystemName(dept.getNameDept());//TODO CV99.14.001 平台对应名称
		assignedEntity.getCode().setCodeSystemName("医疗卫生机构业务科室分类与代码表");//TODO CV99.14.001 平台对应名称
		//assignedEntity.getCode().getDisplayName().setValue(dept.getNameDept());//TODO 科室名称
		assignedEntity.getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(flagActiveMap, "deptTypeName"));//TODO 
		assignedEntity.getName().setXSI_TYPE("LIST_EN");
		//assignedEntity.getName().getItem().setParts(parts);//TODO 对应角色名称0..*
		assignedEntity.getName().getItem().getPart().setValue("");//TODO 对应角色名称0..*
		assignedEntity.getAddr().setXSI_TYPE("DSET_AD");
		assignedEntity.getAddr().getItem().getPart().setValue(MsgUtils.getPropValueStr(deptMap, "namePlace"));//TODO 工作地址
		assignedEntity.getTelecom().setXSI_TYPE("DSET_TEL");
		assignedEntity.getTelecom().getItem().setValue(MsgUtils.getPropValueStr(deptMap, "telnoDept"));//TODO 联系方式；电话
		assignedEntity.getStatusCode().setCode("active");
		assignedEntity.getEffectiveTime().getLow().setValue("");//TODO 开始时间 -暂时没有此数据
		assignedEntity.getEffectiveTime().getHigh().setValue("");//TODO 结束时间 -暂时没有此数据
		AssignedPrincipalOrganization organization=assignedEntity.getAssignedPrincipalOrganization();
		organization.setClassCode("ORG");
		organization.setDeterminerCode("INSTANCE");
		organization.getName().setXSI_TYPE("LIST_EN"); 
		String oldCode = MsgUtils.getPropValueStr(flagActiveMap, "oldCode");
		String nameArea = MsgUtils.getPropValueStr(flagActiveMap, "nameArea");
		organization.getName().getItem().getPart().setValue(dept.getNameDept()+"^"+dept.getCodeDept()+"^"+oldCode+"^"+nameArea);//TODO 科室名称
		AsAffiliate affiliate=organization.getAsAffiliate();
		affiliate.setClassCode("AFFL");
		affiliate.getCode();
		affiliate.getEffectiveTime();
		if(action.equals("PRPM_IN401030UV01")){			
			affiliate.getScoper2().setClassCode("ORG");
			affiliate.getScoper2().setDeterminerCode("INSTANCE");
			affiliate.getScoper2().getId();
			affiliate.getScoper2().getId().getItem().setRoot("2.16.156.10011.1.26");
			affiliate.getScoper2().getId().getItem().setExtension(MsgUtils.getPropValueStr(flagActiveMap, "fatPkDept"));//TODO 上级科室标识
			affiliate.getScoper2().getName().setXSI_TYPE("LIST_EN");
			affiliate.getScoper2().getName().getItem();
			affiliate.getScoper2().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(deptMap, "fatNameDept"));//TODO 上级科室名称 
		}else {
			affiliate.getAffiliatedPrincipalOrganization().setClassCode("ORG");
			affiliate.getAffiliatedPrincipalOrganization().setDeterminerCode("INSTANCE");
			affiliate.getAffiliatedPrincipalOrganization().getId();
			affiliate.getAffiliatedPrincipalOrganization().getId().getItem().setRoot("2.16.156.10011.1.26");
			affiliate.getAffiliatedPrincipalOrganization().getId().getItem().setExtension(MsgUtils.getPropValueStr(flagActiveMap, "fatPkDept"));//TODO 上级科室标识
			affiliate.getAffiliatedPrincipalOrganization().getName().setXSI_TYPE("LIST_EN");
			affiliate.getAffiliatedPrincipalOrganization().getName().getItem();
			affiliate.getAffiliatedPrincipalOrganization().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(deptMap, "fatNameDept"));//TODO 上级科室名称 
		}
		Author author=subject.getRegistrationRequest().getAuthor();
		author.setTypeCode("AUT");
		AssignedEntity auEntity=author.getAssignedEntity();
		auEntity.setClassCode("ASSIGNED");
		auEntity.getId().getItem().setRoot("2.16.156.10011.1.4");
		
		List<Map<String,Object>> empList=syxPlatFormSendBdMapper.qryEmpInfoByDept(UserContext.getUser().getPkEmp());
		Map<String,Object> empMap=empList.get(0);//此数据必须有数据
		//auEntity.getId().getItem().setExtension(MsgUtils.getPropValueStr(empMap, "codeEmp"));//TODO 申请者标识
		auEntity.getId().getItem().setExtension("725608");//TODO 申请者标识
		auEntity.getAssignedPerson().setClassCode("PSN");
		auEntity.getAssignedPerson().setDeterminerCode("INSTANCE");
		auEntity.getAssignedPerson().getName().setXSI_TYPE("LIST_EN");
		auEntity.getAssignedPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(empMap, "nameEmp"));//TODO 申请者名称 实例与文档描述不一致
		auEntity.getRepresentedOrganization().setClassCode("ORG");
		auEntity.getRepresentedOrganization().setDeterminerCode("INSTANCE");//新增属性
		auEntity.getRepresentedOrganization().getId().getItem().setRoot("2.16.156.10011.1.26");
		auEntity.getRepresentedOrganization().getId().getItem().setExtension(MsgUtils.getPropValueStr(empMap, "codeDept"));//申请人所在科室标识		
		auEntity.getRepresentedOrganization().getName().setXSI_TYPE("LIST_EN");
		auEntity.getRepresentedOrganization().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(empMap, "nameDept"));//申请人所在科室名称
		auEntity.getRepresentedOrganization().getContactParty().setClassCode("CON");
		auEntity.getRepresentedOrganization().getContactParty().getContactPerson().setClassCode("PSN");
		auEntity.getRepresentedOrganization().getContactParty().getContactPerson().setDeterminerCode("INSTANCE");
		auEntity.getRepresentedOrganization().getContactParty().getContactPerson().getName().setXSI_TYPE("LIST_EN");
		auEntity.getRepresentedOrganization().getContactParty().getContactPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(empMap, "nameEmp"));//联系人
	}

	/**
	 * 发送术语消息记录
	 * @param paramMap
	 */
	public void sendBdDefDocMsg(Map<String,Object> paramMap){
		String code = ApplicationUtils.getSysparam("PUB0001", false);
		if(!StringUtils.isNotBlank(code) || "0".equals(code) || code.indexOf("B")<0)
			return;
		try {
			String msgContext="";
			String msgType="";
			String msgId="";
			boolean isAdd = (boolean) paramMap.get("isAdd");
			if(isAdd){			
				Request req =new Request("PRVS_IN000001UV01.xsd");
				createBdReqSaveMessage(req,paramMap,true);
				
				String requestXml = XmlProcessUtils.toRequestXml(req,"PRVS_IN000001UV01.xsd".substring(0, "PRVS_IN000001UV01.xsd".indexOf(".")));
				System.out.println(requestXml);
				String res = hipMessageServerUtils.sendHIPServiceCusTomAdds("TerminologyRegister", requestXml);
				Response resObj = XmlProcessUtils.toResponseEntity(res,"MCCI_IN000002UV01");
				
				if(resObj!=null&&resObj.getAcknowledgement()!=null&&"AA".equals(resObj.getAcceptAckCode().getCode())){
					
				}else {
					//消息发送失败
					String errText="";
					if(resObj!=null && resObj.getAcknowledgement()!=null){
					    errText=resObj.getAcknowledgement().getAcknowledgementDetail().getText().getValue();
						msgId=resObj.getAcknowledgement().getTargetMessage().getId().getExtension();
					}
					msgContext=hipMessageServerUtils.getSoapXML("TerminologyRegister", requestXml);
					SysLogService.saveSysMsgRec("receive", msgContext, errText,msgType, msgId);
				}
				
			}else  {
				Request req =new Request("PRVS_IN000002UV01.xsd");
				createBdReqSaveMessage(req,paramMap,false);
				
				String requestXml = XmlProcessUtils.toRequestXml(req,"PRVS_IN000002UV01.xsd".substring(0, "PRVS_IN000002UV01.xsd".indexOf(".")));
				System.out.println(requestXml);
				String res = hipMessageServerUtils.sendHIPServiceCusTomAdds("TerminologyUpdate", requestXml);
				Response resObj = XmlProcessUtils.toResponseEntity(res,"MCCI_IN000002UV01");
				
				if(resObj!=null&&resObj.getAcknowledgement()!=null&&"AA".equals(resObj.getAcceptAckCode().getCode())){
					
				}else {
					//消息发送失败
					String errText="";
					if(resObj!=null && resObj.getAcknowledgement()!=null){
					    errText=resObj.getAcknowledgement().getAcknowledgementDetail().getText().getValue();
						msgId=resObj.getAcknowledgement().getTargetMessage().getId().getExtension();
					}
					msgContext=hipMessageServerUtils.getSoapXML("TerminologyUpdate", requestXml);
					SysLogService.saveSysMsgRec("receive", msgContext, errText,msgType, msgId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询人员信息
	 * @param pkEmp
	 * @return
	 */
	public static Map<String,Object> qryEmpinfo(String pkEmp){
		String sql="select old_code from bd_ou_employee where pk_emp=?";
		Map<String,Object> resMap=DataBaseHelper.queryForMap(sql, new Object[]{pkEmp});
		return resMap;
	}

	 
	/**
	 * 新增和更新字典
	 * @param req
	 * @param resultList
	 */
	private void createBdReqSaveMessage(Request req,Map<String,Object> defdocMap,boolean isAdd){
		req.getId().setRoot("2.16.156.10011.2.5.1.1");
		req.getId().setExtension(NHISUUID.getKeyId());
		req.getCreationTime().setValue(sdf.format(new Date()));
		req.getInteractionId().setRoot("2.16.156.10011.2.5.1.2");
		req.getInteractionId().setExtension("PRVS_IN000002UV01");
		if(isAdd)
			req.getInteractionId().setExtension("PRVS_IN000001UV01");
		req.getProcessingCode().setCode("P");
		req.getAcceptAckCode().setCode("AL");
		
		req.getReceiver().setTypeCode("RCV");
		req.getReceiver().getDevice().setClassCode("DEV");
		req.getReceiver().getDevice().setDeterminerCode("INSTANCE");
		req.getSender().setTypeCode("SND");
		req.getReceiver().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
		req.getReceiver().getDevice().getId().getItem().setExtension("EAI");
		req.getSender().getDevice().setClassCode("DEV");
		req.getSender().getDevice().setDeterminerCode("INSTANCE");
		req.getSender().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
		String codeEmp1=MsgUtils.getPropValueStr(qryEmpinfo(UserContext.getUser().getPkEmp()), "oldCode");
		req.getSender().getDevice().getId().getItem().setExtension(codeEmp1);
		req.getControlActProcess().setClassCode("CACT");
		req.getControlActProcess().setMoodCode("EVN");
		req.getControlActProcess().getSubject().setTypeCode("SUBJ");
		req.getControlActProcess().getSubject().getRegistrationRequest().setClassCode("REG");
		req.getControlActProcess().getSubject().getRegistrationRequest().setMoodCode("RQO");
		req.getControlActProcess().getSubject().getRegistrationRequest().getStatusCode();
		req.getControlActProcess().getSubject().getRegistrationRequest().getSubject1().setTypeCode("SBJ");
		
		List<BdDefdoc> bdDefdocs =(List<BdDefdoc>) defdocMap.get("bdDefdoc");
		//List<BdDefdoc> delPkDefdocs =(List<BdDefdoc>) defdocMap.get("delPkDefdocs");
		List<String> delPkDefdocs1 = (List<String>) defdocMap.get("delPkDeflist");		
		List<BdDefdoc> delPkDefdocs = null;		
		if(delPkDefdocs1!=null && delPkDefdocs1.size()>0 ) {			
		     delPkDefdocs = syxPlatFormSendBdMapper.selectDefdocsByList(delPkDefdocs1);
		}		
		String codeDefdoclist=MsgUtils.getPropValueStr(defdocMap, "pkDefdoclist");
		if(bdDefdocs!=null&&bdDefdocs.size()>0){
			codeDefdoclist = bdDefdocs.get(0).getCodeDefdoclist();
		}else if(delPkDefdocs != null && delPkDefdocs.size()>0){
			codeDefdoclist = delPkDefdocs.get(0).getCodeDefdoclist(); 
		}else {
			codeDefdoclist = MsgUtils.getPropValueStr(defdocMap, "pkDefdoclist");
		}
		Map<String, Object> defdocList = syxPlatFormSendBdMapper.getDdDefdocList(codeDefdoclist);
		if(defdocList == null){
			defdocList = syxPlatFormSendBdMapper.getDdDefdocListByPk(codeDefdoclist);
			codeDefdoclist = MsgUtils.getPropValueStr(defdocList, "codeDefdoclist");
		}
		
		req.getControlActProcess().getSubject().getRegistrationRequest().getSubject1().getValueSet().getId().setExtension(MsgUtils.getPropValueStr(defdocList, "code"));
		req.getControlActProcess().getSubject().getRegistrationRequest().getSubject1().getValueSet().getDesc().setValue(MsgUtils.getPropValueStr(defdocList,"name"));
		req.getControlActProcess().getSubject().getRegistrationRequest().getSubject1().getValueSet().getStatusCode().setCode("1");
		req.getControlActProcess().getSubject().getRegistrationRequest().getSubject1().getValueSet().getVersion().setCode("1.0");
		req.getControlActProcess().getSubject().getRegistrationRequest().getSubject1().getValueSet().getVersion().getDisplayName().setValue("");
		if(!isAdd) {
			if(bdDefdocs!=null&&bdDefdocs.size()>0){
				for (BdDefdoc bdDefdoc : bdDefdocs) {
					ValueSetItems valueSetItems = new ValueSetItems();
					valueSetItems.getCode().setCode(bdDefdoc.getCode());
					valueSetItems.getCode().getDisplayName().setValue(bdDefdoc.getName());
					valueSetItems.getStatusCode().setCode("0");
					req.getControlActProcess().getSubject().getRegistrationRequest().getSubject1().getValueSet().getValueSetItemss().add(valueSetItems);
				}				
			}			
		}else {
			ValueSetItems valueSetItems = new ValueSetItems();
			valueSetItems.getCode().setCode(bdDefdocs.get(bdDefdocs.size()-1).getCode());
			valueSetItems.getCode().getDisplayName().setValue(bdDefdocs.get(bdDefdocs.size()-1).getName());
			valueSetItems.getStatusCode().setCode("0");
			req.getControlActProcess().getSubject().getRegistrationRequest().getSubject1().getValueSet().getValueSetItemss().add(valueSetItems);
		}
		
		if(delPkDefdocs!=null&&delPkDefdocs.size()>0){
			for (BdDefdoc bdDefdoc : delPkDefdocs) {
				ValueSetItems valueSetItems = new ValueSetItems();
				valueSetItems.getCode().setCode(bdDefdoc.getCode());
				valueSetItems.getCode().getDisplayName().setValue(bdDefdoc.getName());
				valueSetItems.getStatusCode().setCode("1");
				req.getControlActProcess().getSubject().getRegistrationRequest().getSubject1().getValueSet().getValueSetItemss().add(valueSetItems);
			}
			
		}
		if(req.getControlActProcess().getSubject().getRegistrationRequest().getSubject1().getValueSet().getValueSetItemss()==null||req.getControlActProcess().getSubject().getRegistrationRequest().getSubject1().getValueSet().getValueSetItemss().size()<1){
			ValueSetItems valueSetItems = new ValueSetItems();
			valueSetItems.getCode().setCode("F");
			valueSetItems.getCode().getDisplayName().setValue("");
			valueSetItems.getStatusCode().setCode("0");
			req.getControlActProcess().getSubject().getRegistrationRequest().getSubject1().getValueSet().getValueSetItemss().add(valueSetItems);
		}
		req.getControlActProcess().getSubject().getRegistrationRequest().getAuthor().setTypeCode("AUT");
		req.getControlActProcess().getSubject().getRegistrationRequest().getAuthor().getAssignedEntity().setClassCode("ASSIGNED");
		req.getControlActProcess().getSubject().getRegistrationRequest().getAuthor().getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
		String codeEmp = UserContext.getUser().getCodeEmp();
		if(!StringUtils.isNotBlank(codeEmp)) {
			codeEmp = "";			
		}
		req.getControlActProcess().getSubject().getRegistrationRequest().getAuthor().getAssignedEntity().getId().getItem().setExtension(codeEmp);
		req.getControlActProcess().getSubject().getRegistrationRequest().getAuthor().getAssignedEntity().getAssignedPerson().setClassCode("PSN");
		req.getControlActProcess().getSubject().getRegistrationRequest().getAuthor().getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
		req.getControlActProcess().getSubject().getRegistrationRequest().getAuthor().getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("DSET_EN");
		req.getControlActProcess().getSubject().getRegistrationRequest().getAuthor().getAssignedEntity().getAssignedPerson().getName().getItem().getPart().setValue(UserContext.getUser().getNameEmp());
	}
	
	private void createBdReqQryMessage(Request req,Map<String,Object> defdocMap){
		req.getId().setRoot("2.16.156.10011.2.5.1.1");
		req.getId().setExtension(NHISUUID.getKeyId());
		req.getCreationTime().setValue(sdf.format(new Date()));
		req.getInteractionId().setRoot("2.16.156.10011.2.5.1.2");
		req.getInteractionId().setExtension("PRVS_IN000003UV01");
		
		req.getProcessingCode().setCode("P");
		req.getProcessingModeCode();
		req.getAcceptAckCode().setCode("AL");
		req.getReceiver().setTypeCode("RCV");
		req.getReceiver().getDevice().setClassCode("DEV");
		req.getReceiver().getDevice().setDeterminerCode("INSTANCE");
		req.getReceiver().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
		req.getReceiver().getDevice().getId().getItem().setExtension("EAI");
		req.getSender().setTypeCode("SND");
		req.getSender().getDevice().setClassCode("DEV");
		req.getSender().getDevice().setDeterminerCode("INSTANCE");
		req.getSender().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
		req.getSender().getDevice().getId().getItem().setExtension("NHIS");
		req.getControlActProcess().setClassCode("ACTN");
		req.getControlActProcess().setMoodCode("EVN");
		
		List<BdDefdoc> bdDefdocs =(List<BdDefdoc>) defdocMap.get("bdDefdoc");
		List<BdDefdoc> delPkDefdocs =(List<BdDefdoc>) defdocMap.get("delPkDefdocs");
		String codeDefdoclist="";
		
		if(bdDefdocs!=null&&bdDefdocs.size()>0){
			codeDefdoclist = bdDefdocs.get(0).getCodeDefdoclist();
		}else if(delPkDefdocs != null && delPkDefdocs.size()>0){
			codeDefdoclist = delPkDefdocs.get(0).getCodeDefdoclist();
		}else {
			codeDefdoclist = MsgUtils.getPropValueStr(defdocMap, "pkDefdoclist");
		}
		
		req.getControlActProcess().getQueryByParameter().getQueryId().setExtension("");
		req.getControlActProcess().getQueryByParameter().getValueSet().getId().setExtension(codeDefdoclist);
		req.getControlActProcess().getQueryByParameter().getValueSet().getVersion().setCode("");
		req.getControlActProcess().getQueryByParameter().getValueSet().getValueSetItems().getCode().setCode("F");;
		
	}
	
}
