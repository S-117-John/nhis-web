package com.zebone.nhis.ma.pub.platform.send.impl.syx.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.pub.service.BdPubService;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.constant.EuPvtype;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.constant.IsAdd;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.dao.SyxPlatFormSendExamMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.vo.SysEsbmsg;
import com.zebone.nhis.ma.pub.platform.syx.support.HIPMessageServerUtils;
import com.zebone.nhis.ma.pub.platform.syx.support.MsgUtils;
import com.zebone.nhis.ma.pub.platform.syx.support.XmlProcessUtils;
import com.zebone.nhis.ma.pub.platform.syx.vo.Component;
import com.zebone.nhis.ma.pub.platform.syx.vo.Item;
import com.zebone.nhis.ma.pub.platform.syx.vo.ObservationRequest;
import com.zebone.nhis.ma.pub.platform.syx.vo.PertinentInformation;
import com.zebone.nhis.ma.pub.platform.syx.vo.PertinentInformation1;
import com.zebone.nhis.ma.pub.platform.syx.vo.ProcedureRequest;
import com.zebone.nhis.ma.pub.platform.syx.vo.Request;
import com.zebone.nhis.ma.pub.service.SysLogService;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;

/**
 * 发送检查检验新增和更新申请单服务
 * @author ASUS
 *
 */
@Service
public class SyxPlatFormSendExamHandler{
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat  sd= new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat  sdm= new SimpleDateFormat("yyyyMMddHHmm");
	
	private HIPMessageServerUtils hipMessageServerUtils = new HIPMessageServerUtils();

	
	@Autowired
	private SyxPlatFormSendExamMapper syxPlatFormSendExamMapper;
	
	@Autowired
	private BdPubService bdPubService;
	
	/**
	 * 发送检查/检验/病理/输血消息
	 * @param paramMap
	 * @param isList
	 */
	public void sendMsg(Map<String,Object> paramMap,boolean isList) {
		try {
			String type = ApplicationUtils.getSysparam("PUB0001", false);
			Object risObject = paramMap.get("risList");
			Object lisObject = paramMap.get("lisList");
			List<SysEsbmsg> msgs = new ArrayList<>();
			
			List<CnOrder>  lisList = null;
			
			List<CnOrder>  risList = null;
			if(lisObject != null && type.indexOf("5")>-1){
				lisList = (List<CnOrder> )lisObject;
			}
			if(lisList != null && lisList.size()>0){
				List<Map<String, Object>> lis = syxPlatFormSendExamMapper.qryExamAndCheckAll(lisList);
				for (Map<String, Object> cnRisApply : lis) {
					String isAddStr = MsgUtils.getPropValueStr(paramMap, "isAdd");
					boolean isAdd = false;
					if(IsAdd.ADD.equals(isAddStr))
						isAdd = true;
					paramMap.put("pkCnord", cnRisApply.get("pkCnord"));
					Map<String, Object> param = setParam("","","",isAdd,paramMap);
					String xsd = MsgUtils.getPropValueStr(param, "xsd");
					String action = MsgUtils.getPropValueStr(param, "action");
					String qesultHead = MsgUtils.getPropValueStr(param, "qesultHead");
					try {
					
						SysEsbmsg esbmsg = pvOpRegMsg(xsd,action,qesultHead, paramMap,isAdd,cnRisApply);
						if(esbmsg != null)
							msgs.add(esbmsg);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			if(risObject != null){
				risList = (List<CnOrder> )risObject;
			}
			if(risList != null && risList.size()>0){
				List<CnOrder> pathologys = new ArrayList<CnOrder>();
				List<CnOrder> checkouts = new ArrayList<CnOrder>();
				for (CnOrder cnRisApply : risList) {
					paramMap.put("pkCnord", cnRisApply.getPkCnord());
					
					if("0204".equals(cnRisApply.getCodeOrdtype())){
						if (type.indexOf("A")<-1) {
							continue;
						}
						pathologys.add(cnRisApply);
					}else {
							checkouts.add(cnRisApply);
							
					}
				}
				List<String> pathologyDepts = syxPlatFormSendExamMapper.qryPathologyDept(checkouts);
				
				if(pathologyDepts != null && pathologyDepts.size()>0){
					for (String pkCnord : pathologyDepts) {
						if(type.indexOf("A")>-1){
							CnOrder cnOrder = new CnOrder();
							cnOrder.setPkCnord(pkCnord);
							pathologys.add(cnOrder);
						}
						for (int i =checkouts.size() -1 ; i >=0; i--) {
							if(checkouts.get(i).getPkCnord().equals(pkCnord))
								checkouts.remove(i);
						}
					}
				}
				if(pathologys.size()>0){
					List<Map<String,Object>> pathologyAll = syxPlatFormSendExamMapper.qryPathologyAll(pathologys);
					if (pathologyAll!=null && pathologyAll.size()>0) {
						paramMap.put("isPathology", "1");
						for (Map<String, Object> map : pathologyAll) {
						
							String isAddStr = MsgUtils.getPropValueStr(paramMap, "isAdd");
							boolean isAdd = false;
							if(IsAdd.ADD.equals(isAddStr))
								isAdd = true;
							Map<String, Object> param = setParam("","","",isAdd,paramMap);
							String xsd = MsgUtils.getPropValueStr(param, "xsd");
							String action = MsgUtils.getPropValueStr(param, "action");
							String qesultHead = MsgUtils.getPropValueStr(param, "qesultHead");
							try {
								
								SysEsbmsg esbmsg = pvOpRegMsg(xsd,action,qesultHead, paramMap,isAdd,map);
								if(esbmsg != null)
									msgs.add(esbmsg);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				if(checkouts.size()>0&&type.indexOf("6")>-1){
					List<Map<String,Object>> examAndCheckAll = syxPlatFormSendExamMapper.qryExamAndCheckAll(checkouts);
					paramMap.remove("isPathology");
					for (Map<String, Object> map : examAndCheckAll) {
						String isAddStr = MsgUtils.getPropValueStr(paramMap, "isAdd");
						boolean isAdd = false;
						if(IsAdd.ADD.equals(isAddStr))
							isAdd = true;
						Map<String, Object> param = setParam("","","",isAdd,paramMap);
						String xsd = MsgUtils.getPropValueStr(param, "xsd");
						String action = MsgUtils.getPropValueStr(param, "action");
						String qesultHead = MsgUtils.getPropValueStr(param, "qesultHead");
						try {
							
							SysEsbmsg esbmsg = pvOpRegMsg(xsd,action,qesultHead, paramMap,isAdd,map);
							if(esbmsg != null)
								msgs.add(esbmsg);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if (msgs.size()>0)
				SysLogService.saveSysEsbMsg(msgs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * 发送消息接受响应
	 * @param xsd
	 * @param action
	 * @param qesultHead
	 * @param paramMap
	 * @param isAdd
	 */
	protected SysEsbmsg pvOpRegMsg(String xsd,String action,String qesultHead,Map<String, Object> paramMap,boolean isAdd,Map<String, Object> examAll){
		Request req =new Request(xsd);
		req = jointXmlObj(req,paramMap,false,xsd.substring(0, xsd.indexOf(".")),examAll,action);
		
		return createSysEsbmsg(req,MsgUtils.getPropValueStr(paramMap, "action"),MsgUtils.getPropValueStr(paramMap, "pkPv"),MsgUtils.getPropValueStr(paramMap, "xsd").substring(0,MsgUtils.getPropValueStr(paramMap, "xsd").indexOf(".")),paramMap);
	}
	
	 /**
     * 组装 SysEsbmsg 表数据
     * @param req 报文对象
     * @param action 服务编码
     * @param checkvo 发送医嘱对象
     * @param headType 头文件类型
     * @param ipSend  发送者ip
     * @return
     */
   private SysEsbmsg createSysEsbmsg(Request req, String action,String pkPv, String headType,Map<String, Object> paramMap) {
		String reqXml=XmlProcessUtils.toRequestXml(req,req.getReqHead());
		String content = hipMessageServerUtils.getSoapXML(action, reqXml);
		System.out.println(content);
		String id = req.getId().getExtension();
		SysEsbmsg sysEsbmsg = new SysEsbmsg(); 
		sysEsbmsg.setPkEsbmsg(NHISUUID.getKeyId());//消息主键
		sysEsbmsg.setPkOrg(UserContext.getUser().getPkOrg());//机构
		sysEsbmsg.setIdMsg(id);//消息id
		sysEsbmsg.setPkPv(pkPv);//患者主键
		sysEsbmsg.setDtEsbmsgtype(headType);//消息类型
		sysEsbmsg.setContentMsg(content);//消息内容
		sysEsbmsg.setEuStatus("0");//消息状态（0未处理，1.已处理，2.处理失败）
		sysEsbmsg.setDateSend(new Date());//发送时间
		sysEsbmsg.setDescError("");//操作失败描述
		sysEsbmsg.setCntHandle(0);//操作次数
		
		String ipSend = MsgUtils.getPropValueStr(paramMap, action);
		if(!StringUtils.isNotBlank(ipSend)){
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
	
	protected Map<String, Object> setParam(String xsd, String action, String qesultHead,
			boolean isAdd, Map<String, Object> paramMap) {
		boolean isLabOrLib = true;	
		if("blood".equals(MsgUtils.getPropValueStr(paramMap, "isLabOrLib")))
			isLabOrLib = false;
		boolean isLab = true;
		if(!"lis".equals(MsgUtils.getPropValueStr(paramMap, "type"))){
			isLab = false;
		}
		boolean isPathology = false;
		if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(paramMap, "isPathology")))
			isPathology = true;
		xsd = "POOR_IN200901UV.xsd"; 
		action = "BloodTransAppInfoAdd";
		if(isLabOrLib){
			if (isLab) {
				action = "ExamAppInfoAdd";
			}else {
				action = "CheckAppInfoAdd";
				if(isPathology)
					action = "PathologyAppInfoAdd";
			}
		}
		
		qesultHead = "MCCI_IN000002UV01";
		
		if(!isAdd){
			xsd = "POOR_IN200902UV.xsd";
			action = "BloodTransAppInfoUpdate";
			if(isLabOrLib){
				if (isLab) {
					action = "ExamAppInfoUpdate";
				}else {
					action = "CheckAppInfoUpdate";
						if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(paramMap, "pkCnord")) && "0204".equals(syxPlatFormSendExamMapper.getOrdType(MsgUtils.getPropValueStr(paramMap, "pkCnord")))){
							isPathology = true;
							paramMap.put("isPathology", "1");
						}
					if(isPathology)
						action = "PathologyAppInfoUpdate";
				}
			}
		}
		
		paramMap.put("action", action);
		paramMap.put("xsd", xsd);
		paramMap.put("qesultHead", qesultHead);
		return paramMap;
	}
	
	/**
	 * 创建 检验/检查/病理/输血 请求对象
	 * @param req 请求对象
	 * @param paramMap 参数
	 * @param isAdd 新增/更新
	 * @param xsd xsd标识
	 * @param examAll
	 * @param action 区分消息分类
	 * @return
	 */
	protected Request jointXmlObj(Request req, Map<String, Object> paramMap,boolean isAdd,String xsd, Map<String, Object> examAll,String action) {
		//true 组建检查/检验消息构造；false 输血消息构造
		boolean isLabOrLib = true;	
		MsgUtils.createPubReq(req, MsgUtils.getPropValueStr(paramMap, "xsd").substring(0,MsgUtils.getPropValueStr(paramMap, "xsd").indexOf(".")));
		
		if("blood".equals(MsgUtils.getPropValueStr(paramMap, "isLabOrLib")))
			isLabOrLib = false;
			boolean isLab = true;//检验标志
			if("ris".equals(MsgUtils.getPropValueStr(paramMap, "type"))){
				isLab = false;
			}
			if(isLabOrLib){//检验/检查请求消息构造
				req.getReceiver().getDevice().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "pkCnord"));
				req.getControlActProcess().setClassCode("ACTN");
				req.getControlActProcess().setMoodCode("EVN");
				req.getControlActProcess().getSubject().setTypeCode("SUBJ");
				ObservationRequest pubNode = req.getControlActProcess().getSubject().getObservationRequest();
				
				pubNode.setClassCode("OBS");
				pubNode.setMoodCode("RQO");
				pubNode.getId().getItem().setRoot("2.16.156.10011.1.24");
				boolean isPathology = StringUtils.isNotBlank(MsgUtils.getPropValueStr(paramMap, "isPathology"));
						
				String euPvtype = MsgUtils.getPropValueStr(examAll, "euPvtype");
				
				//就诊类别代码
				String pvType = euPvtype;
				String pvTypeName = "住院";
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
				
				String applyPk = MsgUtils.getPropValueStr(examAll, "codeApply");
				pubNode.getId().getItem().setExtension(applyPk);
				pubNode.getCode();
				//申请单描述	
				pubNode.getText().setValue(MsgUtils.getPropValueStr(examAll, "descOrd"));
				pubNode.getStatusCode();
				if(isLab){
					pubNode.getStatusCode().setCode(MsgUtils.getPropValueStr(examAll, "euStatusLab"));
				}else{
					pubNode.getStatusCode().setCode(MsgUtils.getPropValueStr(examAll, "euStatusRis"));
				}
				//住院医嘱无申请单有效时间
				pubNode.getEffectiveTime().setXSI_TYPE("IVL_TS");
				pubNode.getEffectiveTime().getLow().setValue("");
				pubNode.getEffectiveTime().getHigh().setValue("");
				
				String flagEmer = MsgUtils.getPropValueStr(examAll, "flagEmer");
				String emerName = "不紧急";
				if(flagEmer.equals("1"))
					emerName = "紧急";
				if (!StringUtils.isNotBlank(flagEmer))
					flagEmer = "0";
				pubNode.getPriorityCode().setCode(flagEmer);
				pubNode.getPriorityCode().getDisplayName().setValue(emerName);
				if(isLab){
					pubNode.getSpecimen();
					pubNode.getSpecimen().getSpecimen().getId().setRoot("2.16.156.10011.1.14");
					pubNode.getSpecimen().getSpecimen().getId().setExtension(MsgUtils.getPropValueStr(examAll, "sampNo"));
					pubNode.getSpecimen().getSpecimen().setClassCode("SPEC");
					pubNode.getSpecimen().getSpecimen().getCode().setCode(MsgUtils.getPropValueStr(examAll, "sampcode"));
					pubNode.getSpecimen().getSpecimen().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(examAll, "sampname"));
					
				}
				
				pubNode.getAuthor().setTypeCode("AUT");
				String time = MsgUtils.getPropValueStr(examAll, "ristime");
				if(!StringUtils.isNotBlank(time))
					time = MsgUtils.getPropValueStr(examAll, "listime");
				if(StringUtils.isNotBlank(time)){
					time = time.replaceAll("-", "");
					time = time.replaceAll(" ", "");
					time = time.replaceAll(":", "");
					if(time.length()>12)
						time = time.substring(0, 12);
				}
				pubNode.getAuthor().getTime().setValue(time);
				if(isPathology){
					String lowTim = time.substring(0,8);
					pubNode.getEffectiveTime().getLow().setValue(lowTim);
				}
				//TODO 
				String nameEmp = MsgUtils.getPropValueStr(examAll, "inputname");
				String codeEmp = MsgUtils.getPropValueStr(examAll, "inputcode");
				if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(examAll, "nameEmp"))){
					nameEmp = MsgUtils.getPropValueStr(examAll, "nameEmp");
					codeEmp = MsgUtils.getPropValueStr(examAll, "codeEmp");
				}
				pubNode.getAuthor().getSignatureText().setValue(nameEmp);
				pubNode.getAuthor().getAssignedEntity().setClassCode("ASSIGNED");
				pubNode.getAuthor().getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
				pubNode.getAuthor().getAssignedEntity().getId().getItem().setExtension(codeEmp);
				pubNode.getAuthor().getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
				pubNode.getAuthor().getAssignedEntity().getAssignedPerson().setClassCode("PSN");
				pubNode.getAuthor().getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
				pubNode.getAuthor().getAssignedEntity().getAssignedPerson().getName().getItem().getPart().setValue(nameEmp);
				
				//申请科室
				pubNode.getAuthor().getAssignedEntity().getRepresentedOrganization().setClassCode("ORG");
				pubNode.getAuthor().getAssignedEntity().getRepresentedOrganization().setDeterminerCode("INSTANCE");
				pubNode.getAuthor().getAssignedEntity().getRepresentedOrganization().getId().getItem().setRoot("2.16.156.10011.1.26");
				pubNode.getAuthor().getAssignedEntity().getRepresentedOrganization().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "codeDept"));
				pubNode.getAuthor().getAssignedEntity().getRepresentedOrganization().getName().setXSI_TYPE("BAG_EN");
				
				pubNode.getAuthor().getAssignedEntity().getRepresentedOrganization().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "nameDept"));
				
				pubNode.getVerifier().setTypeCode("VRF");
				pubNode.getVerifier().getTime().setValue(MsgUtils.getPropValueStr(examAll, "dateChk"));
				pubNode.getVerifier().getAssignedEntity().setClassCode("ASSIGNED");
				pubNode.getVerifier().getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
				pubNode.getVerifier().getAssignedEntity().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "nsCode"));
				pubNode.getVerifier().getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
				pubNode.getVerifier().getAssignedEntity().getAssignedPerson().setClassCode("PSN");
				pubNode.getVerifier().getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
				pubNode.getVerifier().getAssignedEntity().getAssignedPerson().getName().getItem().setValue(MsgUtils.getPropValueStr(examAll, "nsName"));
				
				pubNode.getComponent2().getObservationRequest().setClassCode("OBS");
				pubNode.getComponent2().getObservationRequest().setMoodCode("RQO");
				pubNode.getComponent2().getObservationRequest().getId().getItem().setRoot("2.16.156.10011.1.28");;
				//pubNode.getComponent2().getObservationRequest().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "codeOrd"));
				pubNode.getComponent2().getObservationRequest().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "ordsn"));
				pubNode.getComponent2().getObservationRequest().getCode().setCode(MsgUtils.getPropValueStr(examAll, "bdordcode"));
				pubNode.getComponent2().getObservationRequest().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(examAll, "bdordname"));
				pubNode.getComponent2().getObservationRequest().getStatusCode();
				
				List<Item> methodItems = pubNode.getComponent2().getObservationRequest().getMethodCode().getItems();
				if(!isLab){
					Item risItem = new Item();
					risItem.setCode(MsgUtils.getPropValueStr(examAll, "bdordcode"));
					risItem.setCodeSystem("2.16.156.10011.2.3.2.47");
					risItem.setCodeSystemName("检查方式代码表");
					risItem.getDisplayName().setValue(MsgUtils.getPropValueStr(examAll, "bdordname"));
					methodItems.add(risItem);
				}
				String bdOrdCode = "";
				String bdOrdName = "";
				if(isLab){
					bdOrdCode = MsgUtils.getPropValueStr(examAll, "bdordcode");
					bdOrdName = MsgUtils.getPropValueStr(examAll, "bdordname");
				}
				Item item = new Item();
				item.setCode(bdOrdCode);
				item.getDisplayName().setValue(bdOrdName);
				methodItems.add(item);
				
				pubNode.getComponent2().getObservationRequest().getLocation().setTypeCode("LOC");
				//执行时间
				String dateStart = MsgUtils.getPropValueStr(examAll, "dateStart");
				if(StringUtils.isNotBlank(dateStart))
					dateStart = dateStart.replaceAll("[^0-9]", "");
				if(dateStart.length()>12)
					dateStart = dateStart.substring(0,12);
				pubNode.getComponent2().getObservationRequest().getLocation().getTime().getAny().setValue(dateStart);
				pubNode.getComponent2().getObservationRequest().getLocation().getServiceDeliveryLocation().setClassCode("SDLOC");
				pubNode.getComponent2().getObservationRequest().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().setDeterminerCode("INSTANCE");
				pubNode.getComponent2().getObservationRequest().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().setClassCode("ORG");
				pubNode.getComponent2().getObservationRequest().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId().getItem().setRoot("2.16.156.10011.1.26");
				pubNode.getComponent2().getObservationRequest().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "execcode"));
				pubNode.getComponent2().getObservationRequest().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().setXSI_TYPE("BAG_EN");
				if(!isLab)
					pubNode.getComponent2().getObservationRequest().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().setXSI_TYPE("DSET_EN");
				pubNode.getComponent2().getObservationRequest().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "execname"));
					
				
				if(!isLab){
					Item risItem = new Item();
					risItem.setCode(MsgUtils.getPropValueStr(examAll, "risDocCode"));
					risItem.getDisplayName().setValue(MsgUtils.getPropValueStr(examAll, "risDocName"));
					pubNode.getComponent2().getObservationRequest().getTargetSiteCode().getItem().setCode(MsgUtils.getPropValueStr(examAll, "bodydoccode"));
					pubNode.getComponent2().getObservationRequest().getTargetSiteCode().getItem().getDisplayName().setValue(MsgUtils.getPropValueStr(examAll, "bodydocname"));
				}
				
				//申请单注意事项
				pubNode.getSubjectOf6().setContextConductionInd("false");
				pubNode.getSubjectOf6().getSeperatableInd().setValue("false");
	
				String note = MsgUtils.getPropValueStr(examAll, "labnote");
				if(!StringUtils.isNotBlank(note))
					note = MsgUtils.getPropValueStr(examAll, "risnote");
				pubNode.getSubjectOf6().getAnnotation().getText().setValue(note);
				pubNode.getSubjectOf6().getAnnotation().getStatusCode().setCode("completed");
				pubNode.getSubjectOf6().getAnnotation().getAuthor().getAssignedEntity().setClassCode("ASSIGNED");
				
				//就诊
				pubNode.getComponentOf1().setContextConductionInd("false");
				pubNode.getComponentOf1().setTypeCode("COMP");
				pubNode.getComponentOf1().getEncounter().setClassCode("ENC");
				pubNode.getComponentOf1().getEncounter().setMoodCode("EVN");
				
					
				List<Item> entItems = pubNode.getComponentOf1().getEncounter().getId().getItems();
				if(isLab){
					Item itemOp = new Item();
					itemOp.setRoot("2.16.156.10011.1.11");
					itemOp.setExtension(MsgUtils.getPropValueStr(examAll, "codeOp"));
					entItems.add(itemOp);
					Item itemIp = new Item();
					itemIp.setRoot("2.16.156.10011.1.12");
					itemIp.setExtension(MsgUtils.getPropValueStr(examAll, "codeIp"));
					entItems.add(itemIp);
					Item itemPv = new Item();
					itemPv.setRoot("2.16.156.10011.2.5.1.9");
					itemPv.setExtension(MsgUtils.getPropValueStr(examAll, "codePv"));
					entItems.add(itemPv);
				}else {
					pubNode.getComponentOf1().setXSI_NIL("false");
					int ipTimes = 0;
					int opTimes = 0;
					if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(examAll, "ipTimes")))
						ipTimes = Integer.parseInt(MsgUtils.getPropValueStr(examAll, "ipTimes"));
					if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(examAll, "opTimes")))
						opTimes = Integer.parseInt(MsgUtils.getPropValueStr(examAll, "opTimes"));
					
					Item entNum = new Item();
					entNum.setRoot("2.16.156.10011.2.5.1.8");
					entNum.setExtension(ipTimes+opTimes+"");
					entItems.add(entNum);
					
					Item entNo = new Item();
					entNo.setRoot("2.16.156.10011.2.5.1.9");
					entNo.setExtension(MsgUtils.getPropValueStr(examAll, "codePv"));
					entItems.add(entNo);
				}
				
				pubNode.getComponentOf1().getEncounter().getCode().setCode(pvType);
				pubNode.getComponentOf1().getEncounter().getCode().setCodeSystem("2.16.156.10011.2.3.1.271");
				pubNode.getComponentOf1().getEncounter().getCode().setCodeSystemName("患者类型代码表");
				
				
				pubNode.getComponentOf1().getEncounter().getCode().getDisplayName().setValue(pvTypeName);
				
				pubNode.getComponentOf1().getEncounter().getStatusCode();
				pubNode.getComponentOf1().getEncounter().getSubject().setTypeCode("SBJ");
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().setClassCode("PAT");
				
				List<Item> piItems = pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getId().getItems();
				if(!isLab){
					Item regId = new Item();
					regId.setRoot("2.16.156.10011.2.5.1.5");
					regId.setExtension(pvType);
					piItems.add(regId);
				}
				
				Item piItem = new Item();
				piItem.setRoot("2.16.156.10011.2.5.1.4");
				//piItem.setExtension(MsgUtils.getPropValueStr(examAll, "pkPi"));
				piItem.setExtension(MsgUtils.getPropValueStr(examAll, "codePi"));
				piItems.add(piItem);
				
				if(!isLab){
					Item opIdent = new Item();
					opIdent.setRoot("2.16.156.10011.1.10");
					opIdent.setExtension(MsgUtils.getPropValueStr(examAll, "codeOp"));
					piItems.add(opIdent);
					Item ipIdent = new Item();
					ipIdent.setRoot("2.16.156.10011.1.12");
					ipIdent.setExtension(MsgUtils.getPropValueStr(examAll, "codeIp"));
					piItems.add(ipIdent);
				}
				
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().setClassCode("PSN");
				List<Item> items = pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getId().getItems();
				Item idItem = new Item();
				String id = "";
				if(MsgUtils.getPropValueStr(examAll, "dtIdtype").equals("01"))
					id = MsgUtils.getPropValueStr(examAll, "idNo");
				idItem.setRoot("2.16.156.10011.1.3");
				idItem.setExtension(id);
				items.add(idItem);
				
				Item insuItem = new Item();
				insuItem.setRoot("2.16.156.10011.1.15");
				insuItem.setExtension(MsgUtils.getPropValueStr(examAll, "insurNo"));
				items.add(insuItem);
				
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getName().setXSI_TYPE("DSET_EN");
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "namePi"));
				
				//联系电话
				String tel = MsgUtils.getPropValueStr(examAll, "mobile");
				if(!StringUtils.isNotBlank(tel))
					tel= MsgUtils.getPropValueStr(examAll, "telNo");
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getTelecom().setXSI_TYPE("BAG_TEL");
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getTelecom().getItem().setValue(tel);
				
				//性别
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAdministrativeGenderCode().setCode(MsgUtils.getPropValueStr(examAll, "codesex"));
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAdministrativeGenderCode().setCodeSystem("2.16.156.10011.2.3.3.4");
				if(isLab)
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAdministrativeGenderCode().getDisplayName().setValue(MsgUtils.getPropValueStr(examAll, "namesex"));
				
				
				//出生日期
				String birthDate = MsgUtils.getPropValueStr(examAll, "birthDate");
				int age = 0;
				if(StringUtils.isNotBlank(birthDate)&&birthDate.length()>9){
					birthDate = birthDate.replaceAll("-", "").substring(0, 8);
					//计算年龄
					String newDate = sdf.format(new Date()).replaceAll("-", "");
					String yearStr = newDate.substring(0, 4);
					int newYear = Integer.parseInt(yearStr);  
					int birthYear = Integer.parseInt(birthDate.substring(0, 4));
					age = newYear - birthYear;
					boolean addAge = false;
					
					int newMonth =Integer.parseInt(newDate.substring(4, 6));  
					int birthMonth =Integer.parseInt(birthDate.substring(4, 6));
					
					int newDay = Integer.parseInt(newDate.substring(6, 8));  
					int birthDay =Integer.parseInt(birthDate.substring(6, 8));
					
					if(newMonth<birthMonth){
						addAge = true;
					}
					if(newMonth==birthMonth&&newDay<birthDay){
						addAge = true;
					}
					if(addAge){
						age -- ;
					}
				}
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getBirthTime().setValue(birthDate);
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getBirthTime().getOriginalText().setValue(age+"");
				
				//住址
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAddr().setXSI_TYPE("BAG_AD");
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAddr().getItem().setUse("H");
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAddr().getItem().getPart().setType("AL");
				pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAddr().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "addrCur")+MsgUtils.getPropValueStr(examAll, "addrCurDt"));
				
				pubNode.getComponentOf1().getEncounter().getLocation().setTypeCode("LOC");
				pubNode.getComponentOf1().getEncounter().getLocation().getTime();
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().setClassCode("SDLOC");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().setClassCode("PLC");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().setDeterminerCode("INSTANCE");
				
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "bedcode"));
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getName().setXSI_TYPE("BAG_EN");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getName().getItem().setUse("IDE");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "bedname"));
				
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().setClassCode("LOCE");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().getLocation().setClassCode("PLC");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().getLocation().setDeterminerCode("INSTANCE");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().getLocation().getId().getItem().setExtension("");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().getLocation().getName().setXSI_TYPE("BAG_EN");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().getLocation().getName().getItem().setUse("IDE");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().getLocation().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "houseno"));
				
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().setClassCode("ORG");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().setDeterminerCode("INSTANCE");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "pvdeptcode"));
				
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().setXSI_TYPE("BAG_EN");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().getItem().setUse("IDE");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "pvdeptname"));
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().setClassCode("PART");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().getWholeOrganization().setClassCode("ORG");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().getWholeOrganization().setDeterminerCode("INSTANCE");
				//病区名称
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().getWholeOrganization().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "deptnscode"));
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().getWholeOrganization().getName().setXSI_TYPE("BAN_EN");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().getWholeOrganization().getName().getItem().setUse("IDE");
				pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().getWholeOrganization().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "deptnsname"));
				
			
			PertinentInformation1 pertinentInformation1 = pubNode.getComponentOf1().getEncounter().getPertinentInformation1();
			pertinentInformation1.setTypeCode("PERT");
			if(!isLab)
				pertinentInformation1.setXSI_NIL("false");
			pertinentInformation1.getObservationDx().setClassCode("OBS");
			pertinentInformation1.getObservationDx().setMoodCode("EVN");
			pertinentInformation1.getObservationDx().getCode().setCode("");
			pertinentInformation1.getObservationDx().getCode().setCodeSystem("2.16.156.10011.2.5.1.10");
			pertinentInformation1.getObservationDx().getCode().getDisplayName().setValue("");
			pertinentInformation1.getObservationDx().getStatusCode().setCode("active");
			pertinentInformation1.getObservationDx().getEffectiveTime().getAny().setValue("");
			pertinentInformation1.getObservationDx().getValue().setCode("");
			pertinentInformation1.getObservationDx().getValue().setCodeSystem("2.16.156.10011.2.3.3.11");
			pertinentInformation1.getObservationDx().getValue().getDisplayName().setValue("");
			if(!isLab){
				if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(paramMap, "isPathology")))
					req = setPathologyReq(req, paramMap,examAll);
			}
			
		}else {
			//TODO
			req.getControlActProcess().setClassCode("CACT");
			req.getControlActProcess().setMoodCode("EVN");

			req.getControlActProcess().getSubject().setTypeCode("SUBJ");
			ProcedureRequest pubNode = req.getControlActProcess().getSubject().getProcedureRequest();
			
			pubNode.setClassCode("PROC");
			pubNode.setMoodCode("RQO");
			pubNode.getId().getItem().setRoot("2.16.156.10011.1.24");
			
			String euPvtype = MsgUtils.getPropValueStr(examAll, "euPvtype");
			
			//就诊类别代码
			String pvType = euPvtype;
			String pvTypeName = "住院";
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
			
			String applyPk = MsgUtils.getPropValueStr(examAll, "pkOrdlis");
			if(!StringUtils.isNotBlank(applyPk))
				applyPk=MsgUtils.getPropValueStr(examAll, "pkOrdris");
			pubNode.getId().getItem().setExtension(applyPk);
			pubNode.getCode();
			//申请单描述	
			pubNode.getText().setValue(MsgUtils.getPropValueStr(examAll, "descOrd"));
			pubNode.getStatusCode();
			if(isLab)
				pubNode.getStatusCode().setCode("active");
			
			//住院医嘱无申请单有效时间
			pubNode.getEffectiveTime().setXSI_TYPE("IVL_TS");
			pubNode.getEffectiveTime().getLow().setValue("");
			pubNode.getEffectiveTime().getHigh().setValue("");
			
			//TODO 优先级别无
			pubNode.getPriorityCode().setCode("N");
			pubNode.getPriorityCode().getDisplayName().setValue("常规");
			
			pubNode.getAuthor().setTypeCode("AUT");
			String time = MsgUtils.getPropValueStr(examAll, "ristime");
			if(!StringUtils.isNotBlank(time))
				time = MsgUtils.getPropValueStr(examAll, "listime");
			if(StringUtils.isNotBlank(time)){
				time = time.replaceAll("-", "");
				time = time.replaceAll(" ", "");
				time = time.replaceAll(":", "");
				if(time.length()>12)
					time = time.substring(0, 12);
			}
			pubNode.getAuthor().getTime().setValue(time);
			
			//TODO 
			String nameEmp = MsgUtils.getPropValueStr(examAll, "inputname");
			String codeEmp = MsgUtils.getPropValueStr(examAll, "inputcode");
			if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(examAll, "nameEmp"))){
				nameEmp = MsgUtils.getPropValueStr(examAll, "nameEmp");
				codeEmp = MsgUtils.getPropValueStr(examAll, "codeEmp");
			}
			
			pubNode.getAuthor().getAssignedEntity().setClassCode("ASSIGNED");
			pubNode.getAuthor().getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
			pubNode.getAuthor().getAssignedEntity().getId().getItem().setExtension(codeEmp);
			pubNode.getAuthor().getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
			pubNode.getAuthor().getAssignedEntity().getAssignedPerson().setClassCode("PSN");
			pubNode.getAuthor().getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
			pubNode.getAuthor().getAssignedEntity().getAssignedPerson().getName().getItem().getPart().setValue(nameEmp);
			
			//申请科室
			pubNode.getAuthor().getAssignedEntity().getRepresentedOrganization().setClassCode("ORG");
			pubNode.getAuthor().getAssignedEntity().getRepresentedOrganization().setDeterminerCode("INSTANCE");
			pubNode.getAuthor().getAssignedEntity().getRepresentedOrganization().getId().getItem().setRoot("2.16.156.10011.1.26");
			pubNode.getAuthor().getAssignedEntity().getRepresentedOrganization().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "codeDept"));
			pubNode.getAuthor().getAssignedEntity().getRepresentedOrganization().getName().setXSI_TYPE("BAG_EN");
			pubNode.getAuthor().getAssignedEntity().getRepresentedOrganization().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "nameDept"));
			
			pubNode.getVerifier().setTypeCode("VRF");
			pubNode.getVerifier().getTime().setValue(MsgUtils.getPropValueStr(examAll, "dateChk"));
			pubNode.getVerifier().getAssignedEntity().setClassCode("ASSIGNED");
			pubNode.getVerifier().getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");
			pubNode.getVerifier().getAssignedEntity().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "nsCode"));
			pubNode.getVerifier().getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
			pubNode.getVerifier().getAssignedEntity().getAssignedPerson().setClassCode("PSN");
			pubNode.getVerifier().getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
			pubNode.getVerifier().getAssignedEntity().getAssignedPerson().getName().getItem().setValue(MsgUtils.getPropValueStr(examAll, "nsName"));
			
			PertinentInformation pif = pubNode.getPertinentInformation();	
			pif.setTypeCode("SUBJs");
			pif.setContextConductionInd("true");
			pif.getOrganizer().setClassCode("CONTAINR");
			pif.getOrganizer().setMoodCode("EVN");
			
			List<Component> components = pif.getOrganizer().getComponents();
			
			//血型
			Component bloodType = new Component();
			bloodType.setTypeCode("COMP");
			bloodType.getObservation().setClassCode("OBS");
			bloodType.getObservation().setMoodCode("EVN");
			bloodType.getObservation().getCode().setCode("");
			bloodType.getObservation().getCode().getOriginalText().setValue("患者ABO血型");
			bloodType.getObservation().getValue().setXSI_TYPE("CD");
			bloodType.getObservation().getValue().setCode("");
			bloodType.getObservation().getValue().setCodeSystem("2.16.156.10011.2.3.1.85");
			bloodType.getObservation().getValue().setCodeSystemName("ABO血型代码表");
			bloodType.getObservation().getValue().getDisplayName().setValue("");
			components.add(bloodType); 
			
			Component rhType = new Component();
			rhType.setTypeCode("COMP");
			rhType.getObservation().setClassCode("OBS");
			rhType.getObservation().setMoodCode("EVN");
			rhType.getObservation().getCode().setCode("");
			rhType.getObservation().getCode().getOriginalText().setValue("Rh血型");
			rhType.getObservation().getValue().setXSI_TYPE("CD");
			rhType.getObservation().getValue().setCode("");
			rhType.getObservation().getValue().setCodeSystem("2.16.156.10011.2.3.1.85");
			rhType.getObservation().getValue().setCodeSystemName("Rh（D）血型代码表");
			rhType.getObservation().getValue().getDisplayName().setValue("");
			components.add(rhType);
			
			Component stature = new Component();
			stature.setTypeCode("COMP");
			stature.getObservation().setClassCode("GEN");
			stature.getObservation().setMoodCode("EVN");
			stature.getObservation().getCode().setCode("");
			stature.getObservation().getCode().getOriginalText().setValue("身高");
			stature.getObservation().getValue().setXSI_TYPE("PQ");
			stature.getObservation().getValue().setValue("");
			stature.getObservation().getValue().setUnit("cm");
			
			components.add(stature);
			
			Component weight = new Component();
			weight.setTypeCode("COMP");
			weight.getObservation().setClassCode("GEN");
			weight.getObservation().setMoodCode("EVN");
			weight.getObservation().getCode().setCode("");
			weight.getObservation().getCode().getOriginalText().setValue("体重");
			weight.getObservation().getValue().setXSI_TYPE("PQ");
			weight.getObservation().getValue().setValue("");
			weight.getObservation().getValue().setUnit("kg");
			
			components.add(weight);
			
			Component systolicPressure = new Component();
			systolicPressure.setTypeCode("COMP");
			systolicPressure.getObservation().setClassCode("GEN");
			systolicPressure.getObservation().setMoodCode("EVN");
			systolicPressure.getObservation().getCode().setCode("");
			systolicPressure.getObservation().getCode().getOriginalText().setValue("收缩压");
			systolicPressure.getObservation().getValue().setXSI_TYPE("PQ");
			systolicPressure.getObservation().getValue().setValue("");
			systolicPressure.getObservation().getValue().setUnit("mmHg");
			
			components.add(systolicPressure);
			
			Component dbp = new Component();
			dbp.setTypeCode("COMP");
			dbp.getObservation().setClassCode("GEN");
			dbp.getObservation().setMoodCode("EVN");
			dbp.getObservation().getCode().setCode("");
			dbp.getObservation().getCode().getOriginalText().setValue("舒张压");
			dbp.getObservation().getValue().setXSI_TYPE("PQ");
			dbp.getObservation().getValue().setValue("");
			dbp.getObservation().getValue().setUnit("mmHg");
			
			components.add(dbp);
			
			Component animalHeat  = new Component();
			animalHeat.setTypeCode("COMP");
			animalHeat.getObservation().setClassCode("GEN");
			animalHeat.getObservation().setMoodCode("EVN");
			animalHeat.getObservation().getCode().setCode("");
			animalHeat.getObservation().getCode().getOriginalText().setValue("体温");
			animalHeat.getObservation().getValue().setXSI_TYPE("PQ");
			animalHeat.getObservation().getValue().setValue("");
			animalHeat.getObservation().getValue().setUnit("℃");
			
			components.add(animalHeat);
			
			Component pulse  = new Component();
			pulse.setTypeCode("COMP");
			pulse.getObservation().setClassCode("GEN");
			pulse.getObservation().setMoodCode("EVN");
			pulse.getObservation().getCode().setCode("");
			pulse.getObservation().getCode().getOriginalText().setValue("脉搏");
			pulse.getObservation().getValue().setXSI_TYPE("PQ");
			pulse.getObservation().getValue().setValue("");
			pulse.getObservation().getValue().setUnit("次/分");
			
			components.add(pulse);
			
			
			//血型
			Component appBloodType = new Component();
			appBloodType.setTypeCode("COMP");
			appBloodType.getObservation().setClassCode("OBS");
			appBloodType.getObservation().setMoodCode("EVN");
			appBloodType.getObservation().getCode().setCode("");
			appBloodType.getObservation().getCode().getOriginalText().setValue("申请ABO血型");
			appBloodType.getObservation().getValue().setXSI_TYPE("CD");
			appBloodType.getObservation().getValue().setCode("");
			appBloodType.getObservation().getValue().setCodeSystem("2.16.156.10011.2.3.1.85");
			appBloodType.getObservation().getValue().setCodeSystemName("ABO血型代码表");
			appBloodType.getObservation().getValue().getDisplayName().setValue("");
			components.add(appBloodType); 
			
			Component appRhType = new Component();
			appRhType.setTypeCode("COMP");
			appRhType.getObservation().setClassCode("OBS");
			appRhType.getObservation().setMoodCode("EVN");
			appRhType.getObservation().getCode().setCode("");
			appRhType.getObservation().getCode().getOriginalText().setValue("申请Rh血型");
			appRhType.getObservation().getValue().setXSI_TYPE("CD");
			appRhType.getObservation().getValue().setCode("");
			appRhType.getObservation().getValue().setCodeSystem("2.16.156.10011.2.3.1.85");
			appRhType.getObservation().getValue().setCodeSystemName("Rh（D）血型代码表");
			appRhType.getObservation().getValue().getDisplayName().setValue("");
			components.add(appRhType);
			
			Component  pursuanceTag  = new Component();
			pursuanceTag.setTypeCode("COMP");
			pursuanceTag.getObservation().setClassCode("OBS");
			pursuanceTag.getObservation().setMoodCode("EVN");
			pursuanceTag.getObservation().getCode().setCode("");
			pursuanceTag.getObservation().getCode().getOriginalText().setValue("采血标记");
			pursuanceTag.getObservation().getValue().setXSI_TYPE("BL");
			pursuanceTag.getObservation().getValue().setValue("");
			
			components.add(pursuanceTag);
			
			Component  bts  = new Component();
			bts.setTypeCode("COMP");
			bts.getObservation().setClassCode("OBS");
			bts.getObservation().setMoodCode("EVN");
			bts.getObservation().getCode().setCode("");
			bts.getObservation().getCode().getOriginalText().setValue("输血地点");
			bts.getObservation().getValue().setXSI_TYPE("ST");
			bts.getObservation().getValue().setValue("");
			
			components.add(bts);
			
			Component  btp  = new Component();
			btp.setTypeCode("COMP");
			btp.getObservation().setClassCode("OBS");
			btp.getObservation().setMoodCode("EVN");
			btp.getObservation().getCode().setCode("");
			btp.getObservation().getCode().getOriginalText().setValue("输血目的");
			btp.getObservation().getValue().setXSI_TYPE("ST");
			btp.getObservation().getValue().setValue("");
			
			components.add(btp);
			
			Component  nbt  = new Component();
			nbt.setTypeCode("COMP");
			nbt.getObservation().setClassCode("OBS");
			nbt.getObservation().setMoodCode("EVN");
			nbt.getObservation().getCode().setCode("");
			nbt.getObservation().getCode().getOriginalText().setValue("输血性质");
			nbt.getObservation().getValue().setXSI_TYPE("ST");
			nbt.getObservation().getValue().setValue("");
			
			components.add(nbt);
			
			Component  tem  = new Component();
			tem.setTypeCode("COMP");
			tem.getObservation().setClassCode("OBS");
			tem.getObservation().setMoodCode("EVN");
			tem.getObservation().getCode().setCode("");
			tem.getObservation().getCode().getOriginalText().setValue("输血紧急标志");
			tem.getObservation().getValue().setXSI_TYPE("BL");
			tem.getObservation().getValue().setValue("");
			
			components.add(tem);
			
			Component  thi  = new Component();
			thi.setTypeCode("COMP");
			thi.getObservation().setClassCode("OBS");
			thi.getObservation().setMoodCode("EVN");
			thi.getObservation().getCode().setCode("");
			thi.getObservation().getCode().getOriginalText().setValue("病史信息");
			thi.getObservation().getValue().setXSI_TYPE("ST");
			thi.getObservation().getValue().setValue("");
			
			components.add(thi);
			
			Component  HistoryBloodTran  = new Component();
			HistoryBloodTran.setTypeCode("COMP");
			HistoryBloodTran.getObservation().setClassCode("OBS");
			HistoryBloodTran.getObservation().setMoodCode("EVN");
			HistoryBloodTran.getObservation().getCode().setCode("");
			HistoryBloodTran.getObservation().getCode().getOriginalText().setValue("输血史");
			HistoryBloodTran.getObservation().getValue().setXSI_TYPE("ST");
			HistoryBloodTran.getObservation().getValue().setValue("");
			
			components.add(HistoryBloodTran);
			
			Component  HistoryTransRes  = new Component();
			HistoryTransRes.setTypeCode("COMP");
			HistoryTransRes.getObservation().setClassCode("OBS");
			HistoryTransRes.getObservation().setMoodCode("EVN");
			HistoryTransRes.getObservation().getCode().setCode("");
			HistoryTransRes.getObservation().getCode().getOriginalText().setValue("输血反应史");
			HistoryTransRes.getObservation().getValue().setXSI_TYPE("ST");
			HistoryTransRes.getObservation().getValue().setValue("");
			
			components.add(HistoryTransRes);
			
			Component  drugAllergyHis   = new Component();
			drugAllergyHis.setTypeCode("COMP");
			drugAllergyHis.getObservation().setClassCode("OBS");
			drugAllergyHis.getObservation().setMoodCode("EVN");
			drugAllergyHis.getObservation().getCode().setCode("");
			drugAllergyHis.getObservation().getCode().getOriginalText().setValue("药物过敏史");
			drugAllergyHis.getObservation().getValue().setXSI_TYPE("ST");
			drugAllergyHis.getObservation().getValue().setValue("");
			
			components.add(drugAllergyHis);
			
			Component  gravidity   = new Component();
			gravidity.setTypeCode("COMP");
			gravidity.getObservation().setClassCode("OBS");
			gravidity.getObservation().setMoodCode("EVN");
			gravidity.getObservation().getCode().setCode("");
			gravidity.getObservation().getCode().getOriginalText().setValue("孕次");
			gravidity.getObservation().getValue().setXSI_TYPE("PQ");
			gravidity.getObservation().getValue().setValue("");
			
			components.add(gravidity);
			
			Component  ProductionTimeTree   = new Component();
			ProductionTimeTree.setTypeCode("COMP");
			ProductionTimeTree.getObservation().setClassCode("OBS");
			ProductionTimeTree.getObservation().setMoodCode("EVN");
			ProductionTimeTree.getObservation().getCode().setCode("");
			ProductionTimeTree.getObservation().getCode().getOriginalText().setValue("产次");
			ProductionTimeTree.getObservation().getValue().setXSI_TYPE("PQ");
			ProductionTimeTree.getObservation().getValue().setValue("");
			
			components.add(ProductionTimeTree);
			
			
			Component  OtherImportMediHis   = new Component();
			OtherImportMediHis.setTypeCode("COMP");
			OtherImportMediHis.getObservation().setClassCode("OBS");
			OtherImportMediHis.getObservation().setMoodCode("EVN");
			OtherImportMediHis.getObservation().getCode().setCode("");
			OtherImportMediHis.getObservation().getCode().getOriginalText().setValue("其它重要病史");
			OtherImportMediHis.getObservation().getValue().setXSI_TYPE("ST");
			OtherImportMediHis.getObservation().getValue().setValue("");
			
			components.add(OtherImportMediHis);
			
			Component  remark   = new Component();
			remark.setTypeCode("COMP");
			remark.getObservation().setClassCode("OBS");
			remark.getObservation().setMoodCode("EVN");
			remark.getObservation().getCode().setCode("");
			remark.getObservation().getCode().getOriginalText().setValue("备注");
			remark.getObservation().getValue().setXSI_TYPE("ST");
			remark.getObservation().getValue().setValue("");
			
			components.add(remark);
			
			Component  bloodVolume   = new Component();
			bloodVolume.setTypeCode("COMP");
			bloodVolume.getObservation().setClassCode("OBS");
			bloodVolume.getObservation().setMoodCode("EVN");
			bloodVolume.getObservation().getCode().setCode("");
			bloodVolume.getObservation().getCode().getOriginalText().setValue("血量");
			bloodVolume.getObservation().getValue().setXSI_TYPE("PQ");
			bloodVolume.getObservation().getValue().setValue("");
			
			components.add(bloodVolume);
			
			//就诊
			pubNode.getComponentOf1().setContextConductionInd("false");
			pubNode.getComponentOf1().setXSI_NIL("false");
			pubNode.getComponentOf1().setTypeCode("COMP");
			pubNode.getComponentOf1().getEncounter().setClassCode("ENC");
			pubNode.getComponentOf1().getEncounter().setMoodCode("EVN");
			
			
				
			List<Item> entItems = pubNode.getComponentOf1().getEncounter().getId().getItems();
				
				//就诊次数
				Item itemOp = new Item();
				itemOp.setRoot("2.16.156.10011.2.5.1.8");
				itemOp.setExtension(MsgUtils.getPropValueStr(examAll, "codeOp"));
				entItems.add(itemOp);
				//就诊流水号
				Item itemIp = new Item();
				itemIp.setRoot("2.16.156.10011.2.5.1.9");
				itemIp.setExtension(MsgUtils.getPropValueStr(examAll, "codeIp"));
				entItems.add(itemIp);
			
			
			pubNode.getComponentOf1().getEncounter().getCode().setCode(pvType);
			pubNode.getComponentOf1().getEncounter().getCode().setCodeSystem("2.16.156.10011.2.3.1.271");
			pubNode.getComponentOf1().getEncounter().getCode().setCodeSystemName("患者类型代码表");
			
			
			pubNode.getComponentOf1().getEncounter().getCode().getDisplayName().setValue(pvTypeName);
			
			pubNode.getComponentOf1().getEncounter().getStatusCode();
			pubNode.getComponentOf1().getEncounter().getSubject().setTypeCode("SBJ");
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().setClassCode("PAT");
			
			List<Item> piItems = pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getId().getItems();
			
			Item id = new Item();
			id.setRoot("2.16.156.10011.2.5.1.5");
			id.setExtension(MsgUtils.getPropValueStr(examAll, "pkPi"));
			piItems.add(id);
			
			Item piItem = new Item();
			piItem.setRoot("2.16.156.10011.2.5.1.4");
			//piItem.setExtension(MsgUtils.getPropValueStr(examAll, "pkPi"));
			piItem.setExtension(MsgUtils.getPropValueStr(examAll, "codePi"));
			piItems.add(piItem);
			
			//门诊号标志
			Item ipCode = new Item();
			ipCode.setRoot("2.16.156.10011.1.10");
			ipCode.setExtension(MsgUtils.getPropValueStr(examAll, "pkPi"));
			piItems.add(ipCode);
			
			Item opCode = new Item();
			opCode.setRoot("2.16.156.10011.1.12");
			opCode.setExtension(MsgUtils.getPropValueStr(examAll, "pkPi"));
			piItems.add(opCode);
			
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().setClassCode("PSN");
			List<Item> items = pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getId().getItems();
			Item idItem = new Item();
			idItem.setRoot("2.16.156.10011.1.3");
			idItem.setExtension(MsgUtils.getPropValueStr(examAll, "idNo"));
			items.add(idItem);
			
			Item insuItem = new Item();
			insuItem.setRoot("2.16.156.10011.1.15");
			insuItem.setExtension(MsgUtils.getPropValueStr(examAll, "insurNo"));
			items.add(insuItem);
			
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getName().setXSI_TYPE("DSET_EN");
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "namePi"));
			
			//联系电话
			String tel = MsgUtils.getPropValueStr(examAll, "mobile");
			if(!StringUtils.isNotBlank(tel))
				tel= MsgUtils.getPropValueStr(examAll, "telNo");
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getTelecom().setXSI_TYPE("BAG_TEL");
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getTelecom().getItem().setValue(tel);
			
			//性别
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAdministrativeGenderCode().setCode(MsgUtils.getPropValueStr(examAll, "codesex"));
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAdministrativeGenderCode().setCodeSystem("2.16.156.10011.2.3.3.4");
			
			//出生日期
			String birthDate = MsgUtils.getPropValueStr(examAll, "birthDate");
			int age = 0;
			if(StringUtils.isNotBlank(birthDate)&&birthDate.length()>9){
				birthDate = birthDate.replaceAll("-", "").substring(0, 8);
				//计算年龄
				String newDate = sdf.format(new Date()).replaceAll("-", "");
				String yearStr = newDate.substring(0, 4);
				int newYear = Integer.parseInt(yearStr);  
				int birthYear = Integer.parseInt(birthDate.substring(0, 4));
				age = newYear - birthYear;
				boolean addAge = false;
				
				int newMonth =Integer.parseInt(newDate.substring(4, 6));  
				int birthMonth =Integer.parseInt(birthDate.substring(4, 6));
				
				int newDay = Integer.parseInt(newDate.substring(6, 8));  
				int birthDay =Integer.parseInt(birthDate.substring(6, 8));
				
				if(newMonth<birthMonth){
					addAge = true;
				}
				if(newMonth==birthMonth&&newDay<birthDay){
					addAge = true;
				}
				if(addAge){
					age -- ;
				}
			}
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getBirthTime().setValue(birthDate);
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getBirthTime().getOriginalText().setValue(age+"");
			
			//住址
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAddr().setXSI_TYPE("BAG_AD");
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAddr().getItem().setUse("H");
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAddr().getItem().getPart().setType("AL");
			pubNode.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAddr().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "addrCur")+MsgUtils.getPropValueStr(examAll, "addrCurDt"));
			
			pubNode.getComponentOf1().getEncounter().getLocation().setTypeCode("LOC");
			pubNode.getComponentOf1().getEncounter().getLocation().getTime();
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().setClassCode("SDLOC");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().setClassCode("PLC");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().setDeterminerCode("INSTANCE");
			
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "bedcode"));
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getName().setXSI_TYPE("BAG_EN");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getName().getItem().setUse("IDE");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "bedname"));
			
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().setClassCode("LOCE");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().getLocation().setClassCode("PLC");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().getLocation().setDeterminerCode("INSTANCE");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().getLocation().getId().getItem().setExtension("");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().getLocation().getName().setXSI_TYPE("BAG_EN");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().getLocation().getName().getItem().setUse("IDE");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getLocation().getAsLocatedEntityPartOf().getLocation().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "houseno"));
			
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().setClassCode("ORG");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().setDeterminerCode("INSTANCE");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "pvdeptcode"));
			
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().setXSI_TYPE("BAG_EN");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().getItem().setUse("IDE");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "pvdeptname"));
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().setClassCode("PART");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().getWholeOrganization().setClassCode("ORG");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().getWholeOrganization().setDeterminerCode("INSTANCE");
			//病区名称
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().getWholeOrganization().getId().getItem().setExtension(MsgUtils.getPropValueStr(examAll, "deptnscode"));
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().getWholeOrganization().getName().setXSI_TYPE("BAN_EN");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().getWholeOrganization().getName().getItem().setUse("IDE");
			pubNode.getComponentOf1().getEncounter().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getAsOrganizationPartOf().getWholeOrganization().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(examAll, "deptnsname"));
			
		}
			return req;
	}
	
	//拼接病理xml
	private Request setPathologyReq(Request req,Map<String, Object> paramMap,Map<String, Object> dataAll) {
		req.getControlActProcess().setClassCode("CACT");
		ObservationRequest observationRequest = req.getControlActProcess().getSubject().getObservationRequest();
		observationRequest.setCode(null);
		observationRequest.getEffectiveTime().setHigh(null);
		observationRequest.setPriorityCode(null);
		observationRequest.getMethodCode().getItem().setCodeSystem("2.16.156.10011.2.5.1.16");
		//检查类型编码
		observationRequest.getMethodCode().getItem().setCode("");
		observationRequest.getMethodCode().getItem().getDisplayName().setValue("");
		observationRequest.getSpecimen().setTypeCode("SPC");
		observationRequest.getSpecimen().getSpecimen().setClassCode("SPEC");
		observationRequest.getSpecimen().getSpecimen().getId();
		observationRequest.getSpecimen().getSpecimen().getCode();
		observationRequest.getSpecimen().getSpecimen().getSpecimenNatural().setClassCode("ENT");
		observationRequest.getSpecimen().getSpecimen().getSpecimenNatural().setDeterminerCode("INSTANCE");
		//送检组织
		observationRequest.getSpecimen().getSpecimen().getSpecimenNatural().getCode().getDisplayName().setValue("");
		observationRequest.getSpecimen().getSpecimen().getSpecimenNatural().getQuantity().setValue("");
		observationRequest.getSpecimen().getSpecimen().getSpecimenNatural().getQuantity().setUnit("");
		//取材部位 多个可循环
		observationRequest.getSpecimen().getSpecimen().getSpecimenNatural().getDerivedSpecimen().setClassCode("SPEC");
		//条码号
		observationRequest.getSpecimen().getSpecimen().getSpecimenNatural().getDerivedSpecimen().getId().setExtension("");
		observationRequest.getSpecimen().getSpecimen().getSpecimenNatural().getDerivedSpecimen().getCode();
		observationRequest.getSpecimen().getSpecimen().getSpecimenNatural().getDerivedSpecimen().getSpecimenNatural().setClassCode("ENT");
		observationRequest.getSpecimen().getSpecimen().getSpecimenNatural().getDerivedSpecimen().getSpecimenNatural().setDeterminerCode("INSTANCE");
		observationRequest.getSpecimen().getSpecimen().getSpecimenNatural().getDerivedSpecimen().getSpecimenNatural().getCode().getDisplayName().setValue("");
		observationRequest.getSpecimen().getSpecimen().getSpecimenNatural().getDerivedSpecimen().getSpecimenNatural().getQuantity().setValue("");
		observationRequest.getSpecimen().getSpecimen().getSpecimenNatural().getDerivedSpecimen().getSpecimenNatural().getQuantity().setUnit("");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().setTypeCode("SBJ");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().setClassCode("SPECCOLLECT");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().setMoodCode("EVN");
		//采集日期
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getEffectiveTime().setXSI_TYPE("IVL_TS");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getEffectiveTime().getLow().setValue("");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getSubject().setTypeCode("SBJ");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getSubject().setContextConductionInd("OP");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getSubject().getSpecimenInContainer().setClassCode("CONT");
		//固定液
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getSubject().getSpecimenInContainer().getContainerAdditiveMaterial().setClassCode("MAT");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getSubject().getSpecimenInContainer().getContainerAdditiveMaterial().setDeterminerCode("INSTANCE");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getSubject().getSpecimenInContainer().getContainerAdditiveMaterial().getCode().getDisplayName().setValue("");
		//observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getSubject().getSpecimenInContainer().getContainerAdditiveMaterial().getCode().get
		
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().setTypeCode("PRF");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().setClassCode("ASSIGNED");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getId().getItem().setExtension("");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getId().getItem().setRoot("2.16.156.10011.1.4");

		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().setDeterminerCode("INSTANCE");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().setClassCode("PSN");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
		observationRequest.getSpecimen().getSpecimen().getSubjectOf1().getSpecimenProcessStep().getPerformer().getAssignedEntity().getAssignedPerson().getName().getItem().getPart().setValue("");
		
		observationRequest.getAuthor().setSignatureText(null);
		observationRequest.getVerifier().getAssignedEntity().getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
		
		observationRequest.getReason().setTypeCode("RSON");
		observationRequest.getReason().setContextConductionInd("true");
		observationRequest.getReason().setContextControlCode("OP");
		//病历摘要及手术所见
		observationRequest.getReason().getObservation().setClassCode("OBS");
		observationRequest.getReason().getObservation().setMoodCode("EVN");
		observationRequest.getReason().getObservation().getCode().setCode("DE06.00.182.00");
		observationRequest.getReason().getObservation().getCode().setCodeSystem("2.16.156.10011.2.2.1");
		observationRequest.getReason().getObservation().getCode().setCodeSystemName("卫生信息数据元目录");
		observationRequest.getReason().getObservation().getValue().setXSI_TYPE("ST");
		//病历摘要及手术所见
		observationRequest.getReason().getObservation().getValue().setValue("");
		
		observationRequest.getComponent2().getObservationRequest().setMoodCode(null);
		observationRequest.getComponent2().getObservationRequest().getId().setItem(null);
		observationRequest.getComponent2().getObservationRequest().getCode().setCodeSystem("2.16.156.10011.2.5.1.17");
		observationRequest.getComponent2().getObservationRequest().getMethodCode().setItems(null);
		//检查方式编码
		observationRequest.getComponent2().getObservationRequest().getMethodCode().getItem().setCode(MsgUtils.getPropValueStr(dataAll, "codeOrd"));
		observationRequest.getComponent2().getObservationRequest().getMethodCode().getItem().setCodeSystem("2.16.156.10011.2.3.2.47");
		observationRequest.getComponent2().getObservationRequest().getMethodCode().getItem().getDisplayName().setValue(MsgUtils.getPropValueStr(dataAll, "nameOrd"));
		
		observationRequest.getComponent2().getObservationRequest().getTargetSiteCode().getItem().setCodeSystem("2.16.156.10011.2.5.1.18");
		observationRequest.getComponent2().getObservationRequest().getLocation().getTime().setAny(null);
		//执行时间
		observationRequest.getComponent2().getObservationRequest().getLocation().getTime().getLow().setValue("");
		observationRequest.getComponent2().getObservationRequest().getLocation().getServiceDeliveryLocation().getServiceProviderOrganization().getName().setXSI_TYPE("BAG_EN");
		
		observationRequest.getSubjectOf6().setXSI_NIL("false");
		
		observationRequest.getComponentOf1().setXSI_NIL("false");
		observationRequest.getComponentOf1().getEncounter().getCode().setCodeSystemName(null);
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getName().setXSI_TYPE("BAG_EN");
		//婚姻状况
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getMaritalStatusCode().setCode(MsgUtils.getPropValueStr(dataAll, "marcode"));
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getMaritalStatusCode().setCodeSystem("2.16.156.10011.2.3.3.5");
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getEthnicGroupCode().getItem().setCode(MsgUtils.getPropValueStr(dataAll, "nationCode"));
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getEthnicGroupCode().getItem().setCodeSystem("2.16.156.10011.2.3.3.3");

		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAsEmployee().setClassCode("EMP");
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAsEmployee().getOccupationCode().setCode(MsgUtils.getPropValueStr(dataAll, "occcode"));
		//职业
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAsEmployee().getOccupationCode().setCodeSystem("2.16.156.10011.2.3.3.13");
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAsEmployee().getOccupationCode().getDisplayName().setValue(MsgUtils.getPropValueStr(dataAll, "occname"));
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAsEmployee().getEmployerOrganization().setDeterminerCode("INSTANCE");
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAsEmployee().getEmployerOrganization().setClassCode("ORG");
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAsEmployee().getEmployerOrganization().getName().setXSI_TYPE("BAG_EN");
		//工作单位名称
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAsEmployee().getEmployerOrganization().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(dataAll, "unitWork"));
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAsEmployee().getEmployerOrganization().getContactParty().setClassCode("CON");
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAsEmployee().getEmployerOrganization().getContactParty().setXSI_NIL("true");
		//国家编码
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAsCitizen().getPoliticalNation().getCode().setCode(MsgUtils.getPropValueStr(dataAll, "natcode"));
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAsCitizen().getPoliticalNation().getCode().setCodeSystem("2.16.156.10011.2.3.3.1");
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getAsCitizen().getPoliticalNation().getCode().getDisplayName().setValue(MsgUtils.getPropValueStr(dataAll, "natname"));
		//联系人
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getContactParty().setClassCode("CON");
		//联系人电话
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getContactParty().getTelecom().setXSI_TYPE("BAG_TEL");
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getContactParty().getTelecom().getItem().setUse("MC");
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getContactParty().getTelecom().getItem().setValue(MsgUtils.getPropValueStr(dataAll, "telRel"));
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getContactParty().getTelecom().getItem().setCapabilities("voice");
		//联系人亲属/姓名
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getContactParty().getContactPerson().getName().setXSI_TYPE("BAG_EN");
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getContactParty().getContactPerson().getName().getItem().setUse("IDE");
		observationRequest.getComponentOf1().getEncounter().getSubject().getPatient().getPatientPerson().getContactParty().getContactPerson().getName().getItem().getPart().setValue(MsgUtils.getPropValueStr(dataAll, "nameRel"));
		observationRequest.getComponent2().getObservationRequest().setOrderno(MsgUtils.getPropValueStr(dataAll, "ordsn"));
		return req;
	}
	
	 /**
		 * 查询医嘱信息发送者IP
		 * @return
		 */
		protected String qryIpSend(String action){
			switch (action) {
			case "OperationAppInfoAdd":
				action="OpAppInfoAdd";
				break;
			case "OperationAppInfoUpdate":
				action="OpAppInfoUpdate";
				break;
			case "ExamAppInfoUpdate":
				action="ExamInfoUpdate";
				break;
			case "CheckAppInfoUpdate":
				action="CheckInfoUpdate";
				break;
			case "PathologyAppInfoAdd":
				action="PathologyAdd";
				break;
			case "PathologyAppInfoUpdate":
				action="PathologyUpdate";
				break;
			default:
				break;
			}
			return syxPlatFormSendExamMapper.qryIpSend(action);
		}
	
}
