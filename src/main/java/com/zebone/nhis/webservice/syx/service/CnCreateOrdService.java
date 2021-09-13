package com.zebone.nhis.webservice.syx.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.thoughtworks.xstream.XStream;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnTransApply;
import com.zebone.nhis.common.support.ApplicationUtils;

import org.apache.commons.beanutils.BeanUtils;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.syx.dao.BdSerialMapper;
import com.zebone.nhis.webservice.syx.dao.CnCreateOrdMapper;
import com.zebone.nhis.webservice.syx.utils.HIPMessageServerUtils;
import com.zebone.nhis.webservice.syx.vo.BdSerial;
import com.zebone.nhis.webservice.syx.vo.OrdRequest;
import com.zebone.nhis.webservice.syx.vo.OrdResponse;
import com.zebone.nhis.webservice.syx.vo.cn.CnOrdExt;
import com.zebone.nhis.webservice.syx.vo.send.AsOrganizationPartOf;
import com.zebone.nhis.webservice.syx.vo.send.AsOtherIDs;
import com.zebone.nhis.webservice.syx.vo.send.AssignedEntity;
import com.zebone.nhis.webservice.syx.vo.send.Author;
import com.zebone.nhis.webservice.syx.vo.send.Component2;
import com.zebone.nhis.webservice.syx.vo.send.ComponentOf1;
import com.zebone.nhis.webservice.syx.vo.send.Consumable2;
import com.zebone.nhis.webservice.syx.vo.send.ContainerPackagedProduct;
import com.zebone.nhis.webservice.syx.vo.send.ControlActProcess;
import com.zebone.nhis.webservice.syx.vo.send.Encounter;
import com.zebone.nhis.webservice.syx.vo.send.Item;
import com.zebone.nhis.webservice.syx.vo.send.Location;
import com.zebone.nhis.webservice.syx.vo.send.ManufacturedProduct;
import com.zebone.nhis.webservice.syx.vo.send.ManufacturedProduct1;
import com.zebone.nhis.webservice.syx.vo.send.Patient;
import com.zebone.nhis.webservice.syx.vo.send.PatientPerson;
import com.zebone.nhis.webservice.syx.vo.send.PertinentInformation;
import com.zebone.nhis.webservice.syx.vo.send.RepresentedOrganization;
import com.zebone.nhis.webservice.syx.vo.send.Request;
import com.zebone.nhis.webservice.syx.vo.send.ServiceDeliveryLocation;
import com.zebone.nhis.webservice.syx.vo.send.ServiceProviderOrganization;
import com.zebone.nhis.webservice.syx.vo.send.Subject;
import com.zebone.nhis.webservice.syx.vo.send.SubjectOf3;
import com.zebone.nhis.webservice.syx.vo.send.SubjectOf6;
import com.zebone.nhis.webservice.syx.vo.send.SubstanceAdministrationRequest;
import com.zebone.nhis.webservice.syx.vo.send.SysEsbmsg;
import com.zebone.nhis.webservice.syx.vo.send.Verifier;
import com.zebone.nhis.webservice.syx.vo.send.WholeOrganization;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnCreateOrdService {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat  sdm= new SimpleDateFormat("yyyyMMddHHmm");
	public static SimpleDateFormat  sd= new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat sdfl = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private HIPMessageServerUtils serverUtils = new HIPMessageServerUtils();
	private String pkOrg="a41476368e2943f48c32d0cb1179dab8";
	@Autowired
	private CnCreateOrdMapper cnCreateOrdMapper;
	
	@Autowired      
	private BdSerialMapper bdSnMapper;
	
	public OrdResponse createOrd(OrdRequest req) {
		OrdResponse res = new OrdResponse();
		
		try {
			if(req == null){
				return setErrResponse(res,"未获取到数据！");
			}
			res.setRetMsg(req.getNameOrd());
			if(!StringUtils.isNotBlank(req.getEuPvType())){
				return setErrResponse(res,"euPvType不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getCodePi())){
				return setErrResponse(res,"code_pi不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getCodePv())){
				return setErrResponse(res,"code_pv不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getCodeOrd())){
				return setErrResponse(res,"code_ord不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getDateStart())){
				return setErrResponse(res,"date_start不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getNameOrd())){
				return setErrResponse(res,"name_ord不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getQuan())){
				return setErrResponse(res,"quan不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getCodeFreq())){
				return setErrResponse(res,"code_freq不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getCodeDept())){
				return setErrResponse(res,"code_dept不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getCodeDr())){
				return setErrResponse(res,"code_dr不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getCodeDeptNs())){
				return setErrResponse(res,"code_dept_ns不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getCodeDeptEx())){
				return setErrResponse(res,"code_dept_ex不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getFlagNote())){
				return setErrResponse(res,"flag_note不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getFlagBl())){
				return setErrResponse(res,"flag_bl不能为空！");
			}
			if(!StringUtils.isNotBlank(req.getCodeOrdtype())){
				return setErrResponse(res,"code_ordtype不能为空！");
			}
			CnOrder cnOrder = new CnOrder();
			CnTransApply cnTransApply = new CnTransApply();
			
			if(!"3".equals(req.getEuPvType())&&!"1".equals(req.getEuPvType())&&!"2".equals(req.getEuPvType())){
				return setErrResponse(res,"euPvType非法!");
			}
			cnOrder.setEuPvtype(req.getEuPvType());
			String codePi = req.getCodePi();
			String pkPi = "";
			String[] split = codePi.split(",");
			if(split != null && split.length > 1 && StringUtils.isNotBlank(split[0])){
				pkPi = cnCreateOrdMapper.qryPkPi(split[1]);
			}else if(split != null && split.length > 0 && StringUtils.isNotBlank(split[0])){
				pkPi = cnCreateOrdMapper.qryPkPi(split[0]);
			}
			if(!StringUtils.isNotBlank(pkPi)){
				return setErrResponse(res,"未查询到该患者!");
			}
			cnOrder.setPkPi(pkPi);
			
			Map<String, Object> pv = cnCreateOrdMapper.qryPkPv(req.getCodePv(),req.getEuPvType());
			if(pv == null){
				return setErrResponse(res,"未查询到该次就诊!");
			}
			cnOrder.setPkPv(getPropValueStr(pv, "pkPv"));
			
			if(!"DEF99999".equals(req.getCodeOrd())){
				Map<String, Object> ordMap = cnCreateOrdMapper.qryBdOrd(req.getCodeOrd());
				if(ordMap == null ){
					return setErrResponse(res,"字典中不存在该医嘱!");
				}
				cnOrder.setPkOrd(getPropValueStr(ordMap, "pkOrd"));
			}
			
//			cnOrder.setNameOrd(req.getNameOrd());
//			List<String> names = cnCreateOrdMapper.qryOrdByName(req.getNameOrd());
//			if(names == null||names.size()<1){
//				return setErrResponse(res, "请求失败，没有找到医嘱项目！");
//			}else if(names.size()>1){
//				return setErrResponse(res, "请求失败，找到多个医嘱项目！");
//			}else {
//			}
			cnOrder.setNameOrd(req.getNameOrd());
			cnOrder.setCodeOrd(req.getCodeOrd());
			
			SimpleDateFormat sdfl = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			try {
				Date dateStart = sdfl.parse(req.getDateStart());
				cnOrder.setDateStart(dateStart);
				cnTransApply.setDatePlan(dateStart);
				cnOrder.setDateEnter(dateStart);
				cnOrder.setDateSign(dateStart);
			} catch (Exception dataErr) {
				return setErrResponse(res,"时间格式不正确!");
			} 
			cnOrder.setDescOrd(req.getDescOrd());
			double quan = 0.0;
			try {
				quan = Double.parseDouble(req.getQuan());
			} catch (Exception e) {
				return setErrResponse(res,"quan格式不正确!");
			} 
			cnOrder.setDosage(quan);
			cnTransApply.setQuanBt(quan);;
			cnOrder.setQuan(1.0);
			cnOrder.setFlagStop("0");
			cnOrder.setFlagStopChk("0");
			cnOrder.setFlagErase("0");
			cnOrder.setFlagEraseChk("0");
			cnOrder.setFlagDoctor("1");
			cnOrder.setFlagPrint("0");
			cnOrder.setFlagMedout("0");
			cnOrder.setCodeFreq(req.getCodeFreq());
			
			String pkDept = cnCreateOrdMapper.qryPkDept(req.getCodeDept());
			if(!StringUtils.isNotBlank(pkDept)){
				return setErrResponse(res,"未查询到开立科室!");
			}
			cnOrder.setPkDept(pkDept);
			
			Map<String, Object> emp = cnCreateOrdMapper.qryEmp(req.getCodeDr());
			if(emp == null){
				return setErrResponse(res,"未查询到开立医生!");
			}
			cnOrder.setPkEmpOrd(getPropValueStr(emp, "pkEmp"));
			cnOrder.setNameEmpOrd(getPropValueStr(emp, "nameEmp"));
			
			String pkDeptNs = cnCreateOrdMapper.qryPkDept(req.getCodeDeptNs());
			if(!StringUtils.isNotBlank(pkDeptNs)){
				return setErrResponse(res,"未查询到开立病区!");
			}
			cnOrder.setPkDeptNs(pkDeptNs);
			
			String pkDeptEx = cnCreateOrdMapper.qryPkDept(req.getCodeDeptEx());
			if(!StringUtils.isNotBlank(pkDeptEx)){
				return setErrResponse(res,"未查询到执行病区!");
			}
			cnOrder.setPkDeptExec(pkDeptEx);
			if(!"0".equals(req.getFlagNote())&&!"1".equals(req.getFlagNote())){
				return setErrResponse(res,"flag_note非法!");
			}
			cnOrder.setFlagNote(req.getFlagNote());
			if(!"0".equals(req.getFlagBl())&&!"1".equals(req.getFlagBl())){
				return setErrResponse(res,"flag_bl非法!");
			}
			cnOrder.setFlagBl(req.getFlagBl());
			
			Integer ordTypeCount = cnCreateOrdMapper.qryOrdtype(req.getCodeOrdtype());
			if(ordTypeCount == null || ordTypeCount == 0){
				return setErrResponse(res,"没有找到医嘱类型!");
			}
			
		
			cnOrder.setCodeOrdtype(req.getCodeOrdtype());
			cnOrder.setPkOrg(cnCreateOrdMapper.getPkOrg());
			int serialNo = getSerialNoPro("CN_ORDER","ORDSN",1,null);
			cnOrder.setOrdsn(serialNo);
			cnOrder.setEuAlways("1");
			cnOrder.setEuStatusOrd("1");
			cnOrder.setFlagSign("1");
			cnOrder.setPkEmpInput(getPropValueStr(emp, "pkEmp"));
			cnOrder.setNameEmpInput(getPropValueStr(emp, "nameEmp"));
			cnOrder.setCodeApply(req.getCodeApply());
			if ("p".equals(req.getNoteOrd())) {
				cnOrder.setCodeApply("p"+req.getCodeApply());
			}
			
			String dtBttype = req.getDtBttype(); //cnCreateOrdMapper.qryDocCode("030208", req.getDtBttype());
			if(StringUtils.isNotBlank(dtBttype))
				cnTransApply.setDtBttype(dtBttype);
			else 
				return setErrResponse(res, "没有找到输血性质！");
			//cnCreateOrdMapper.qryDocCode("030208", req.getBtContent());
			cnTransApply.setDtBtAbo(req.getDtBtAbo());
			cnTransApply.setDtBtRh(req.getDtBtRh());
			cnTransApply.setPkOrg(cnCreateOrdMapper.getPkOrg());
			cnTransApply.setEuStatus("0");
			String btContent = req.getBtContent();
//			switch (req.getBtContent()) {
//				case "全血": btContent = "0001";
//				break;
//				case "红细胞悬液": btContent = "0002";
//				break;
//				case "冰冻洗涤红细胞": btContent = "0007";
//				break;
//				case "冷沉淀": btContent = "0011";
//				break;
//				case "机采血小板": btContent = "0014";
//				break;
//				case "手工分浓缩血小板": btContent = "0016";
//				break;
//				case "新鲜冰冻血浆": btContent = "0019";
//				break;
//				case "冰冻血浆": btContent = "0025";
//				break;
//				case "洗涤红细胞（MAP）": btContent = "0030";
//				break;
//				case "去白细胞悬浮红细胞": btContent = "2001";
//				break;
//				case "去白红细胞悬液": btContent = "9011";
//				break;
//				case "去白机采血小板": btContent = "9012";
//				break;
//				case "自体血": btContent = "9013";
//				break;
//				default:return setErrResponse(res, "没有找到对应的输血成分：" + req.getBtContent());
//			}
			String pkUnit = cnCreateOrdMapper.qryUnit(req.getUnit());
			cnTransApply.setPkUnitBt(pkUnit);
			cnOrder.setPkUnitDos(pkUnit);
			cnOrder.setFlagDurg("0");
			cnOrder.setFlagBase("0");
			cnOrder.setFlagSelf("0");
			cnOrder.setPkOrgExec("a41476368e2943f48c32d0cb1179dab8");
			if(StringUtils.isNotBlank(btContent))
			cnTransApply.setBtContent(btContent);
			else {
				return  setErrResponse(res, "输血成分不能为空！");
			}
			if(!"12".equals(req.getCodeOrdtype())){
				cnTransApply = null;
			}
			cnOrder.setOrdsnParent(cnOrder.getOrdsn());
			DataBaseHelper.insertBean(cnOrder);
			if(cnTransApply!=null){
				cnTransApply.setPkCnord(cnOrder.getPkCnord());
				DataBaseHelper.insertBean(cnTransApply);
			}
			res.setPkCnord(cnOrder.getPkCnord()+"^"+cnOrder.getOrdsn());
			if("12".equals(req.getCodeOrdtype())){
					res.setRetMsg(req.getNameOrd()+ "^" + "请求成功！");
					}else {
						res.setRetMsg("请求成功！");
					}
			List<CnOrder> CnOrders = new ArrayList<>();
			HashMap<Object, Object> paramMap = new HashMap<>();
			String type =cnCreateOrdMapper.qrySysCode("PUB0001");
			try {
				if(StringUtils.isNotBlank(type) && type.indexOf("3")>-1){
					String headType="POOR_IN200901UV";
					String action="OrderInfoAdd";
					Request request = createOrderMessage(action, cnOrder.getPkCnord());
					SysEsbmsg sysEsbmsg = createSysEsbmsg(request,action,cnOrder.getPkPv(),headType);
					DataBaseHelper.insertBean(sysEsbmsg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return res;
		} catch (Exception e) {
			
 			return setErrResponse(res,"请求失败！");
		}
	}

	//设置错误响应对象
	private OrdResponse setErrResponse(OrdResponse res,String err){
		res.setRetCode("1");
		res.setRetMsg(res.getRetMsg() +"^"+err);
		return res;
	}
	
	private String rtnErrMsg(OrdResponse res,String err){
		res.setRetCode("1");
		res.setRetMsg(res.getRetMsg() +"^"+err);
		return JsonUtil.writeValueAsString(res);
	}
	
	private String getPropValueStr(Map<String, Object> map,String key) {
		if(map==null)return "";
		String value="" ;
		if(map.containsKey(key)){
			Object obj=map.get(key);
			value=obj==null?"":obj.toString();
		}
		return value;
	}

	public String analysisXml(String input_info) {
		OrdRequest req = (OrdRequest)XmlUtil.XmlToBean(input_info, OrdRequest.class);
		return XmlUtil.beanToXml(createOrd(req), OrdResponse.class);
	}
	
	//生成医嘱号
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	private int getSerialNoPro(String tableName, String fieldName, int count, IUser user){
		boolean isInit = false;
		if(tableName==null) return 0;
		if(!isInit){ 
			Double sn = bdSnMapper.selectSn(tableName.toUpperCase(), fieldName.toUpperCase()); 
			if(sn==null){
				BdSerial initSn = new BdSerial();
				initSn.setPkSerialno(NHISUUID.getKeyId());
				initSn.setPkOrg(CommonUtils.getGlobalOrg());
				initSn.setNameTb(tableName.toUpperCase());
				initSn.setNameFd(fieldName.toUpperCase());
				initSn.setValInit((short)1000);
				initSn.setVal((short)1000);
				bdSnMapper.initSn(initSn);
				isInit = true;
			}
		}
		int ret = ApplicationUtils.getSerialNo(tableName,fieldName,count);
		return ret;
	}
	
	 private Request createOrderMessage(String action,String pkCnord){
	    	List<Map<String,Object>> orderList=cnCreateOrdMapper.qryOrderInfo(pkCnord);
	    	if(orderList==null || orderList.size()<=0)return null;
	    	String timeFormat = "yyyyMMddHHmmss";//时间类型
	    	Map<String,Object> ordMap=orderList.get(0);
	    	Request req=new Request(action+".xsd");
	    	req = createPubReq(req, action);
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
	    	String dateEnterStr = dateFormatString(timeFormat, getPropValueDate(ordMap, "dateEnter"));//TODO 医嘱开立时间
	    	aut.getTime().setValue(dateEnterStr);//TODO 医嘱开立时间
	    	aut.getSignatureCode().setCode("S");
	    	aut.getSignatureText().setValue(getPropValueStr(ordMap, "nameEmpInput"));//TODO 医嘱开立者即录入者（name_emp_input 录入者名称）
	    	AssignedEntity entity=aut.getAssignedEntity();
	    	entity.setClassCode("ASSIGNED");
	    	entity.getId().getItem().setRoot("2.16.156.10011.1.4");
	    	entity.getId().getItem().setExtension(getPropValueStr(ordMap, "inCodeEmp"));//TODO 医务人员工号
	    	entity.getAssignedPerson().setClassCode("PSN");
	    	entity.getAssignedPerson().setDeterminerCode("INSTANCE");
	    	entity.getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
	    	entity.getAssignedPerson().getName().getItem().getPart().setValue(getPropValueStr(ordMap, "nameEmpInput"));//医嘱开立者
	    	RepresentedOrganization reporg=entity.getRepresentedOrganization();
	    	reporg.setClassCode("ORG");
	    	//MsgUtils
	    	reporg.setDeterminerCode("INSTANCE");
	    	reporg.getId().getItem().setRoot("2.16.156.10011.1.26");
	    	reporg.getId().getItem().setExtension(getPropValueStr(ordMap, "inCodeDept"));//TODO 开立科室编码
	    	reporg.getName().setXSI_TYPE("BAG_EN");
	    	reporg.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "inNameDept"));//TODO 开立科室名称
	    	
	    	//医嘱审核者
	    	Verifier veri=sub.getPlacerGroup().getVerifier();
	    	veri.setTypeCode("VRF");
	    	veri.setContextControlCode("OP");//TODO 是否是门诊/住院的标识符
	    	String dateChk = dateFormatString(timeFormat, getPropValueDate(ordMap, "dateChk"));
	    	veri.getTime().setValue(dateChk);//审核时间
	    	veri.getSignatureCode().setCode("S");
	    	veri.getSignatureText().setValue(getPropValueStr(ordMap, "codeEmpChk"));//TODO 医嘱审核者签名
	    	AssignedEntity veriEnt=veri.getAssignedEntity();
	    	veriEnt.setClassCode("ASSIGNED");
	    	veriEnt.getId().getItem().setRoot("2.16.156.10011.1.4");
	    	veriEnt.getId().getItem().setExtension(getPropValueStr(ordMap, "codeEmpChk"));//TODO 医务人员工号
	    	veriEnt.getAssignedPerson().setClassCode("PSN");
	    	veriEnt.getAssignedPerson().setDeterminerCode("INSTANCE");
	    	veriEnt.getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
	    	veriEnt.getAssignedPerson().getName().getItem().getPart().setValue(getPropValueStr(ordMap, "nameEmpChk"));//医嘱审核者
	    	
	    	List<Component2> com2s=sub.getPlacerGroup().getComponent2s();//1..*
	    	for (int i = 0; i < orderList.size(); i++) {
	    		Component2 com2=new Component2();
	    		com2.getSequenceNumber().setValue(getPropValueStr(ordMap, "ordsn"));//医嘱序号
	    		SubstanceAdministrationRequest subReq=com2.getSubstanceAdministrationRequest();
	    		subReq.setClassCode("SBADM");
	    		subReq.setModdCode("RQO");
	    		subReq.getId().setRoot("2.16.156.10011.1.28");
	    		subReq.getId().setExtension(getPropValueStr(ordMap, "ordsn"));//医嘱id
	    		subReq.getCode().setCode(getPropValueStr(ordMap, "codeOrdtype"));//医嘱项目类型代码
	    		subReq.getCode().setCodeSystem("2.16.156.10011.2.3.1.268");
	    		subReq.getCode().getDisplayName().setValue(getPropValueStr(ordMap, "nameOrdtype"));
	    		subReq.getText();
	    		subReq.getStatusCode().setCode("active");
	    		subReq.getEffectiveTime().setXSI_TYPE("QSC_TS");
	    		subReq.getEffectiveTime().setValidTimeLow(sdf.format(getPropValueDate(ordMap, "dateStart")));//开始时间
	        	String dateEnd = dateFormatString(timeFormat, getPropValueDate(ordMap, "dateStop"));//获取结束时间
	    		subReq.getEffectiveTime().setValidTimeHigh(dateEnd);//结束
	    		
	    		subReq.getEffectiveTime().getCode().setCode(getPropValueStr(ordMap, "codeFreq"));//频次
	    		subReq.getEffectiveTime().getCode().setCodeSystem("2.16.156.10011.2.5.1.13");
	    		subReq.getEffectiveTime().getCode().getDisplayName().setValue(getPropValueStr(ordMap, "freqName"));//频次名称
	    		
	    		subReq.getRouteCode().setCode(getPropValueStr(ordMap, "codeSupply"));// 
	    		subReq.getRouteCode().setCodeSystem("2.16.156.10011.2.3.1.158");
	    		subReq.getRouteCode().setCodeSystemName("用药途径代码表");//用药途径代码表
	    		subReq.getRouteCode().getDisplayName().setValue(getPropValueStr(ordMap, "nameSupply"));//名称
	    		
	    		subReq.getDoseQuantity().setValue(getPropValueStr(ordMap, "dosage"));//用药剂量-单次
	    		subReq.getDoseQuantity().setUnit(getPropValueStr(ordMap, "nameDos"));//单位
	    		
	    		subReq.getDoseCheckQuantity().setXSI_TYPE("DSET_RTO");
	    		subReq.getDoseCheckQuantity().getItem().getNumerator().setXSI_TYPE("PQ");
	    		subReq.getDoseCheckQuantity().getItem().getNumerator().setUnit(getPropValueStr(ordMap, "nameQuan"));//单位
	    		subReq.getDoseCheckQuantity().getItem().getNumerator().setValue(getPropValueStr(ordMap, "quan"));//值
	    		subReq.getDoseCheckQuantity().getItem().getDenominator().setXSI_TYPE("PQ");
	    		subReq.getDoseCheckQuantity().getItem().getDenominator().setUnit("d");//单位
	    		subReq.getDoseCheckQuantity().getItem().getDenominator().setValue(getPropValueStr(ordMap, "days"));//值
				
	    		//药物剂型 
	    		subReq.getAdministrationUnitCode().setCode(getPropValueStr(ordMap, "pddocCodeStd"));//获取码表对应的编码
	    		subReq.getAdministrationUnitCode().setCodeSystem("2.16.156.10011.2.3.1.211");
	    		subReq.getAdministrationUnitCode().setCodeSystemName("药物剂型代码表");
	    		subReq.getAdministrationUnitCode().getDisplayName().setValue(getPropValueStr(ordMap, "pddocNameStd"));//获取码表对应的名称
	    		
	    		//药物信息
	    		Consumable2 con2=subReq.getConsumable2();
	    		con2.setTypeCode("CSM");
	    		ManufacturedProduct1 man1=con2.getManufacturedProduct1();
	    		man1.setClassCode("MANU");	
	    		man1.getId().setExtension(getPropValueStr(ordMap, "apprNo"));//TODO 包装序号 （批准文号/注册）
	    		ManufacturedProduct man=man1.getManufacturedProduct();
	    		man.setClassCode("MMAT");
	    		man.setDeterminerCode("KIND");
	    		man.getCode().setCode(getPropValueStr(ordMap, "codePd"));//药码
	    		man.getCode().setCodeSystem("2.16.156.10011.2.5.1.14");
	    		man.getName().setXSI_TYPE("BAG_EN");
	    		man.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "namePd"));//药名
	    		man.getAsContent().setClassCode("CONT");
	    		man.getAsContent().getQuantity();
	    		ContainerPackagedProduct pro= man.getAsContent().getContainerPackagedProduct();
	    		pro.setClassCode("HOLD");
	    		pro.setDeterminerCode("KIND");
	    		pro.getCode();
	    		pro.getFormCode();
	    		//TODO 药物规格 需要确定医疗含量还是基本单位  
	    		pro.getCapacityQuantity().setUnit(getPropValueStr(ordMap, "nameDos"));
	    		pro.getCapacityQuantity().setValue(getPropValueStr(ordMap, "nameQuan"));
	    		
	    		//TODO 药物所属医保信息  医保数据待确定
	    		SubjectOf3 sub3=man1.getSubjectOf3();
	    		sub3.setTypeCode("SBJ");
	    		sub3.getPolicy().setClassCode("POLICY");
	    		sub3.getPolicy().setMoodCode("EVN");
	    		//!-- 药物医保类别编码/药物医保类别名称 -->
	    		sub3.getPolicy().getCode().setCode(getPropValueStr(ordMap, "poldocCodeStd"));
	    		sub3.getPolicy().getCode().getDisplayName().setValue(getPropValueStr(ordMap, "poldocNameStd"));
	    		
	    		Location loca=subReq.getLocation();
	    		loca.setTypeCode("LOC");
	    		ServiceDeliveryLocation serloca= loca.getServiceDeliveryLocation();
	    		serloca.setClassCode("DSDLOC");
	    		Location sonlo=serloca.getLocation();
	    		sonlo.setClassCode("PLC");
	    		sonlo.setDeterminerCode("INSTANCE");
	    		sonlo.getId().getItem().setRoot("2.16.156.10011.1.26");
	    		sonlo.getId().getItem().setExtension(getPropValueStr(ordMap, "exCodeDept"));//执行科室编码
	    		sonlo.getName().setXSI_TYPE("BAG_EN");
	    		sonlo.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "exNameDept"));//执行科室名称
	    		subReq.getOccurrenceOf().getParentRequestReference().setClassCode("GROUPER");
	    		subReq.getOccurrenceOf().getParentRequestReference().getId().setExtension(getPropValueStr(ordMap, "ordsnParent"));//父医嘱id
	    		
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
	    		String euAlways=getPropValueStr(ordMap, "euAlwas");
	    		String euCode="";
	    		String euName="";
	    		if("0".equals(euAlways)){
	    			euCode="1";
	    			euName="长期医嘱";
	    		}else if("1".equals(euAlways)){
	    			euCode="2";
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
	    		sonCom.getSupplyRequest().getQuantity().setValue(getPropValueStr(ordMap, "quanAp"));
	    		sonCom.getSupplyRequest().getQuantity().setUnit(getPropValueStr(ordMap, "nameUnitAp"));
	    		sonCom.getSupplyRequest().getExpectedUseTime().setValidTimeLow(sdm.format(getPropValueDate(ordMap, "dateStart")));//医嘱开始时间
	        	String dateFormatString = dateFormatString(timeFormat, getPropValueDate(ordMap, "dateEnd"));//获取结束时间
	    		sonCom.getSupplyRequest().getExpectedUseTime().setValidTimeHigh(dateFormatString);//医嘱结束时间
	    		
	    		//医嘱备注信息
	    		SubjectOf6 sub6=subReq.getSubjectOf6();
	    		sub6.setTypeCode("SUBJ");
	    		sub6.setContextConductionInd("false");
	    		sub6.getSeperatableInd().setValue("false");
	    		sub6.getAnnotation().getText().setValue(getPropValueStr(ordMap, "noteOrd"));
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
	    	itemIpt.setExtension(getPropValueStr(ordMap, "ipTimes"));
	    	itemIpt.setRoot("2.16.156.10011.2.5.1.8");
	    	Item itemCodeP = new Item();
	    	itemCodeP.setExtension(getPropValueStr(ordMap, "codePv"));//就诊流水号
	    	itemCodeP.setRoot("2.16.156.10011.2.5.1.9");
	    	itemList.add(itemIpt);
	    	itemList.add(itemCodeP);
//	    	enc.getId().getItem().setExtension(getPropValueStr(ordMap, "ipTimes"));
//	    	enc.getId().getItem().setRoot("2.16.156.10011.2.5.1.8");
//	    	enc.getId().getItem().setExtension(getPropValueStr(ordMap, "codePv"));//就诊流水号
//	    	enc.getId().getItem().setRoot("2.16.156.10011.2.5.1.9");
	    	String euPvtype=getPropValueStr(ordMap, "euPvType");
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
	    	item1.setExtension(getPropValueStr(ordMap, ""));//域id设置为空
	    	Item item2=new Item();
	    	item2.setRoot("2.16.156.10011.2.5.1.4");
	    	item2.setExtension(getPropValueStr(ordMap, "pkPi"));//患者主键
	    	Item item3=new Item();
	    	item3.setRoot("2.16.156.10011.1.10");
	    	item3.setExtension(getPropValueStr(ordMap, "codeOp"));//门急诊号
	    	Item item4=new Item();
	    	item4.setRoot("2.16.156.10011.1.12");
	    	item4.setExtension(getPropValueStr(ordMap, "codeIp"));//住院号
	    	items.add(item1);
	    	items.add(item2);
	    	items.add(item3);
	    	items.add(item4);
	    	pat.getTelecom().setXSI_TYPE("BAG_TEL");
	    	pat.getTelecom().getItem().setValue(getPropValueStr(ordMap, "telNo"));//联系电话
	    	pat.getStatusCode().setCode("active");
	    	PatientPerson per=pat.getPatientPerson();
	    	per.setClassCode("PSN");
	    	per.setDeterminerCode("INSTANCE");
	    	per.getId().getItem().setRoot("2.16.156.10011.1.3");
	    	per.getId().getItem().setExtension(getPropValueStr(ordMap, "idNo"));//身份证号
	    	per.getName().setXSI_TYPE("BAG_EN");
	    	per.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "namePi"));
	    	
	    	//性别
	    	
	    	per.getAdministrativeGenderCode().setCode(getPropValueStr(ordMap, "sexCodeStd"));
	    	per.getAdministrativeGenderCode().setCodeSystem("2.16.156.10011.2.3.3.4");
	    	per.getAdministrativeGenderCode().setCodeSystemName("生理性别代码表（GB/T 2261.1）");
	    	per.getAdministrativeGenderCode().getDisplayName().setValue(getPropValueStr(ordMap, "sexNameStd"));
	    	Date bDate = getPropValueDate(ordMap, "birthDate");
	    	String format = sd.format(bDate);
	        per.getBirthTime().setValue(format);//出生日期
	    	//AsOtherIDs as = per.getAsOtherID();
	    	List<AsOtherIDs> asOtherIDsList = per.getAsOtherIDs();
	    	AsOtherIDs as = new AsOtherIDs();
	    	as.setClassCode("ROL");
	    	List<Item> asItems=as.getId().getItems();
	    	Item asitem1=new Item();
	    	asitem1.setRoot("2.16.156.10011.1.2");
	    	asitem1.setExtension(getPropValueStr(ordMap, ""));//健康档案编码(没有)
	    	Item asitem2=new Item();
	    	asitem2.setRoot("2.16.156.10011.1.19");
	    	asitem2.setExtension(getPropValueStr(ordMap, "piHicNo"));//健康卡号
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
	    	serloc.getId().getItem().setExtension(getPropValueStr(ordMap, "bedCode"));//病床号
	    	serloc.getName().setXSI_TYPE("BAG_EN");
	    	serloc.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "bedName"));//病床名称
	    	serloc.getAsLocatedEntityPartOf().setClassCode("LOCE");
	    	//病房号
	    	Location asloc= serloc.getAsLocatedEntityPartOf().getLocation();
	    	asloc.setClassCode("PLC");
	    	asloc.setDeterminerCode("INSTANCE");
	    	asloc.getId().getItem().setRoot("2.16.156.10011.1.21");
	    	asloc.getId().getItem().setExtension("");//病房编码
	    	asloc.getName().setXSI_TYPE("BAG_EN");
	    	asloc.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "bedHouseno"));//所在房间
	    	
	    	//科室信息
	    	ServiceProviderOrganization org= enloc.getServiceDeliveryLocation().getServiceProviderOrganization();
	    	org.setClassCode("org");
	    	org.setDeterminerCode("INSTANCE");
	    	org.getId().getItem().setRoot("2.16.156.10011.1.26");
	    	org.getId().getItem().setExtension(getPropValueStr(ordMap, "pvCodeDept"));//科室编码
	    	org.getName().setXSI_TYPE("BAG_EN");
	    	org.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "pvNameDept"));
	    	
	    	//病区
	    	AsOrganizationPartOf asorg= org.getAsOrganizationPartOf();
	    	asorg.setClassCode("PART");
	    	WholeOrganization whoorg= asorg.getWholeOrganization();
	    	whoorg.setClassCode("ORG");
	    	whoorg.setDeterminerCode("INSTANCE");
	    	whoorg.getId().getItem().setRoot("2.16.156.10011.1.27");
	    	whoorg.getId().getItem().setExtension(getPropValueStr(ordMap, "nsCodeDept"));//病区编码
	    	whoorg.getName().setXSI_TYPE("BAG_EN");
	    	whoorg.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "nsNameDept"));//病区名称
	    	return req;
	    }
	 
	 private SysEsbmsg createSysEsbmsg(Request req, String action,String pkPv,String headType) {
			String reqXml=toRequestXml(req,req.getReqHead());
			String content = serverUtils.getSoapXML(action, reqXml);
			String id = req.getId().getExtension();
			SysEsbmsg sysEsbmsg = new SysEsbmsg(); 
			sysEsbmsg.setPkEsbmsg(NHISUUID.getKeyId());//消息主键
			sysEsbmsg.setPkOrg("~");//机构
			sysEsbmsg.setIdMsg(id);//消息id
			sysEsbmsg.setPkPv(pkPv);//患者主键
			sysEsbmsg.setDtEsbmsgtype(headType);//消息类型
			sysEsbmsg.setContentMsg(content);//消息内容
			sysEsbmsg.setEuStatus("0");//消息状态（0未处理，1.已处理，2.处理失败）
			sysEsbmsg.setDateSend(new Date());//发送时间
			sysEsbmsg.setDescError("");//操作失败描述
			sysEsbmsg.setCntHandle(0);//操作次数
			sysEsbmsg.setIpSend(cnCreateOrdMapper.qryIpSend(action));//发送IP
			sysEsbmsg.setAddrSend("");//发送地址
			sysEsbmsg.setNote("");//备注
			
			sysEsbmsg.setCreator("BLOOD");//创建人
			sysEsbmsg.setCreateTime(new Date());//创建时间
			sysEsbmsg.setDelFlag("0");//
			sysEsbmsg.setTs(new Date());//时间戳
			return sysEsbmsg;
		}
	 
	 private  Request createPubReq(Request req,String action){
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
			req.getReceiver().getDevice().getId().getItem().setExtension("192.168.8.234");
			req.getSender().setTypeCode("SND");
			req.getSender().getDevice().setClassCode("DEV");
			req.getSender().getDevice().setDeterminerCode("INSTANCE");
			req.getSender().getDevice().getId().getItem().setRoot("2.16.156.10011.2.5.1.3");
			req.getSender().getDevice().getId().getItem().setExtension("HIS");
			return req;
		}
	 
	 private  String toRequestXml(Request r,String reqHeader){
		  XStream xs = new XStream();
		  xs.setMode(XStream.NO_REFERENCES);
		  xs.processAnnotations(new Class[]{Request.class});
		  String str = xs.toXML(r);
		  str= str.replaceAll("PRPA__IN201311UV02","POOR_IN200901UV");
		  str = str.replaceAll(reqHeader,"POOR_IN200901UV" );
		  str = str.replaceAll("XMLNS__XSI", "xmlns:xsi");
		  str = str.replaceAll("XSI__SCHEMALOCATION", "xsi:schemaLocation");
		  str = str.replaceAll("XSI__TYPE", "xsi:type");
		  str = str.replaceAll("XSI__NIL", "xsi:nil");
		  System.out.println(str);
		  return str;
	  }
	 

	 /**
     *
     * 处理时间，如果时间为空发""
     */
    private String dateFormatString(String format, Date propValueDate){
    	if (propValueDate == null||format == null) {
    		return "";
		}else {
			return sdf.format(propValueDate);
		}
    }
    
    /**
	 * 取日期内容
	 * @param map
	 * @return
	 */
	public static Date getPropValueDate(Map<String, Object> map,String key) {
		Date value=null;
		boolean have=map.containsKey(key);
		if(have){
			Object obj=map.get(key);
			if(obj==null) return null;
			if(obj instanceof Date )
			{
				return (Date)obj;//如果传入的就是Date类型的数据
			}
			String dateStr=obj.toString();
			try {
				if(dateStr.indexOf("-")>=0){
					dateStr=dateStr.substring(0, 19);
					value = sdfl.parse(dateStr);
				}else{
					value = sdf.parse(dateStr);
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}			
		}
		return value;
	}
	
	
	/**
	 * 输血医嘱生成/作废接口
	 * @param param
	 * @return
	 */
	public String CnOrderBlood(String param) throws Exception{
		OrdResponse res = new OrdResponse();
		Date dateNow = new Date();
		List<Map<String, Object>> rtnList=new ArrayList<Map<String, Object>>();
		List<CnOrdExt> ordList = JsonUtil.readValue(
				JsonUtil.getJsonNode(param, "datalist"),
				new TypeReference<List<CnOrdExt>>() {
				});
		
		for(CnOrdExt ord :ordList){
			
			CnTransApply cnTransApply = new CnTransApply();
			String pkPi = cnCreateOrdMapper.qryPkPi(ord.getCodePi());
			if(!StringUtils.isNotBlank(pkPi)){
				return rtnErrMsg(res,"未查询到该患者!");
			}
			ord.setPkPi(pkPi);
			Map<String, Object> pv = cnCreateOrdMapper.qryPkPv(ord.getCodePv(),ord.getEuPvtype());
			if(pv == null){
				return rtnErrMsg(res,"未查询到该次就诊!");
			}
			ord.setPkPv(getPropValueStr(pv, "pkPv"));
			if(!"DEF99999".equals(ord.getCodeOrd())){
				Map<String, Object> ordMap = cnCreateOrdMapper.qryBdOrd(ord.getCodeOrd());
				if(ordMap == null ){
					return rtnErrMsg(res,"字典中不存在该医嘱!");
				}
				ord.setPkOrd(getPropValueStr(ordMap, "pkOrd"));
			}
			
			
			try {
				cnTransApply.setDatePlan(ord.getDateStart());
				ord.setDateEffe(ord.getDateStart());
				ord.setDateEnter(dateNow);
				ord.setDateSign(dateNow);
			} catch (Exception dataErr) {
				return rtnErrMsg(res,"时间格式不正确!");
			} 
			
			String pkDept = cnCreateOrdMapper.qryPkDept(ord.getCodeDept());
			if(!StringUtils.isNotBlank(pkDept)){
				return rtnErrMsg(res,"未查询到开立科室!");
			}
			ord.setPkDept(pkDept);
			
			Map<String, Object> emp = cnCreateOrdMapper.qryEmp(ord.getCodeDr());
			if(emp == null){
				return rtnErrMsg(res,"未查询到开立医生!");
			}
			ord.setPkEmpOrd(getPropValueStr(emp, "pkEmp"));
			ord.setNameEmpOrd(getPropValueStr(emp, "nameEmp"));
			
			String pkDeptNs = cnCreateOrdMapper.qryPkDept(ord.getCodeDeptNs());
			if(!StringUtils.isNotBlank(pkDeptNs)){
				return rtnErrMsg(res,"未查询到开立病区!");
			}
			ord.setPkDeptNs(pkDeptNs);
			
			String pkDeptEx = cnCreateOrdMapper.qryPkDept(ord.getCodeDeptEx());
			if(!StringUtils.isNotBlank(pkDeptEx)){
				return rtnErrMsg(res,"未查询到执行病区!");
			}
			ord.setPkDeptExec(pkDeptEx);
			
			String pkUnit = cnCreateOrdMapper.qryUnit(ord.getUnit());
			int serialNo = getSerialNoPro("CN_ORDER","ORDSN",1,null);
			ord.setOrdsn(serialNo);
			ord.setEuAlways("1");
			ord.setEuStatusOrd("1");
			ord.setFlagSign("1");
			ord.setPkEmpInput(getPropValueStr(emp, "pkEmp"));
			ord.setNameEmpInput(getPropValueStr(emp, "nameEmp"));
			ord.setDosage(ord.getQuan());
			ord.setQuan(1.0);
			ord.setFlagStop("0");
			ord.setFlagStopChk("0");
			ord.setFlagErase("0");
			ord.setFlagEraseChk("0");
			ord.setFlagDoctor("1");
			ord.setFlagPrint("0");
			ord.setFlagMedout("0");
			ord.setPkUnitDos(pkUnit);
			ord.setFlagDurg("0");
			ord.setFlagBase("0");
			ord.setFlagSelf("0");
			ord.setPkOrgExec(pkOrg);
			ord.setOrdsnParent(ord.getOrdsn());
			
			
			String dtBttype = ord.getDtBttype(); //cnCreateOrdMapper.qryDocCode("030208", req.getDtBttype());
			if(StringUtils.isNotBlank(dtBttype))
				cnTransApply.setDtBttype(dtBttype);
			else 
				return rtnErrMsg(res, "没有找到输血性质！");
			
			String btContent = ord.getBtContent();
			if(StringUtils.isNotBlank(btContent))
				cnTransApply.setBtContent(btContent);
			else {
				return  rtnErrMsg(res, "输血成分不能为空！");
			}
			
			//cnTransApply.setPkOrg(cnCreateOrdMapper.getPkOrg());
			cnTransApply.setPkOrg(pkOrg);
			cnTransApply.setFlagLab("0");
			cnTransApply.setFlagBthis("0");
			cnTransApply.setFlagPreg("0");
			cnTransApply.setFlagAl("0");
			cnTransApply.setCreator("输血接口_CNBloodOrd");
			cnTransApply.setDtBtAbo(ord.getDtBtAbo());
			cnTransApply.setDtBtRh(ord.getDtBtRh());
			cnTransApply.setEuStatus("0");
			cnTransApply.setQuanBt(ord.getQuan());
			cnTransApply.setPkUnitBt(pkUnit);
			
			if(!"12".equals(ord.getCodeOrdtype())){
				cnTransApply = null;
			}
			
			CnOrder cnOrder=new CnOrder();
			BeanUtils.copyProperties(cnOrder, ord);
			cnOrder.setCreator("输血接口_CNBloodOrd");
			cnOrder.setPkOrg(pkOrg);
			cnOrder.setFlagFirst("0");
			cnOrder.setFlagCp("0");
			cnOrder.setFlagEmer("0");
			cnOrder.setFlagThera("0");
			cnOrder.setFlagPrev("0");
			cnOrder.setFlagFit("0");
			cnOrder.setEuIntern("0");
			DataBaseHelper.insertBean(cnOrder);
			if(cnTransApply!=null){
				cnTransApply.setPkCnord(cnOrder.getPkCnord());
				DataBaseHelper.insertBean(cnTransApply);
			}
			
			Map<String,Object> rtnMap=new HashMap<String,Object>();
			rtnMap.put("id", ord.getId());
			rtnMap.put("pkCnord", cnOrder.getPkCnord());
			rtnList.add(rtnMap);
		}
		
		Map<String,Object> rtnMap=new HashMap<String,Object>();
		rtnMap.put("ret_code", "0");
		rtnMap.put("ret_msg", "");
		rtnMap.put("datalist", rtnList);
		String result = JsonUtil.writeValueAsString(rtnMap);
		return result;
	}
}
