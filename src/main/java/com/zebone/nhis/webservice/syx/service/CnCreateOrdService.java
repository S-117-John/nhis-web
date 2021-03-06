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
				return setErrResponse(res,"?????????????????????");
			}
			res.setRetMsg(req.getNameOrd());
			if(!StringUtils.isNotBlank(req.getEuPvType())){
				return setErrResponse(res,"euPvType???????????????");
			}
			if(!StringUtils.isNotBlank(req.getCodePi())){
				return setErrResponse(res,"code_pi???????????????");
			}
			if(!StringUtils.isNotBlank(req.getCodePv())){
				return setErrResponse(res,"code_pv???????????????");
			}
			if(!StringUtils.isNotBlank(req.getCodeOrd())){
				return setErrResponse(res,"code_ord???????????????");
			}
			if(!StringUtils.isNotBlank(req.getDateStart())){
				return setErrResponse(res,"date_start???????????????");
			}
			if(!StringUtils.isNotBlank(req.getNameOrd())){
				return setErrResponse(res,"name_ord???????????????");
			}
			if(!StringUtils.isNotBlank(req.getQuan())){
				return setErrResponse(res,"quan???????????????");
			}
			if(!StringUtils.isNotBlank(req.getCodeFreq())){
				return setErrResponse(res,"code_freq???????????????");
			}
			if(!StringUtils.isNotBlank(req.getCodeDept())){
				return setErrResponse(res,"code_dept???????????????");
			}
			if(!StringUtils.isNotBlank(req.getCodeDr())){
				return setErrResponse(res,"code_dr???????????????");
			}
			if(!StringUtils.isNotBlank(req.getCodeDeptNs())){
				return setErrResponse(res,"code_dept_ns???????????????");
			}
			if(!StringUtils.isNotBlank(req.getCodeDeptEx())){
				return setErrResponse(res,"code_dept_ex???????????????");
			}
			if(!StringUtils.isNotBlank(req.getFlagNote())){
				return setErrResponse(res,"flag_note???????????????");
			}
			if(!StringUtils.isNotBlank(req.getFlagBl())){
				return setErrResponse(res,"flag_bl???????????????");
			}
			if(!StringUtils.isNotBlank(req.getCodeOrdtype())){
				return setErrResponse(res,"code_ordtype???????????????");
			}
			CnOrder cnOrder = new CnOrder();
			CnTransApply cnTransApply = new CnTransApply();
			
			if(!"3".equals(req.getEuPvType())&&!"1".equals(req.getEuPvType())&&!"2".equals(req.getEuPvType())){
				return setErrResponse(res,"euPvType??????!");
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
				return setErrResponse(res,"?????????????????????!");
			}
			cnOrder.setPkPi(pkPi);
			
			Map<String, Object> pv = cnCreateOrdMapper.qryPkPv(req.getCodePv(),req.getEuPvType());
			if(pv == null){
				return setErrResponse(res,"????????????????????????!");
			}
			cnOrder.setPkPv(getPropValueStr(pv, "pkPv"));
			
			if(!"DEF99999".equals(req.getCodeOrd())){
				Map<String, Object> ordMap = cnCreateOrdMapper.qryBdOrd(req.getCodeOrd());
				if(ordMap == null ){
					return setErrResponse(res,"???????????????????????????!");
				}
				cnOrder.setPkOrd(getPropValueStr(ordMap, "pkOrd"));
			}
			
//			cnOrder.setNameOrd(req.getNameOrd());
//			List<String> names = cnCreateOrdMapper.qryOrdByName(req.getNameOrd());
//			if(names == null||names.size()<1){
//				return setErrResponse(res, "??????????????????????????????????????????");
//			}else if(names.size()>1){
//				return setErrResponse(res, "??????????????????????????????????????????");
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
				return setErrResponse(res,"?????????????????????!");
			} 
			cnOrder.setDescOrd(req.getDescOrd());
			double quan = 0.0;
			try {
				quan = Double.parseDouble(req.getQuan());
			} catch (Exception e) {
				return setErrResponse(res,"quan???????????????!");
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
				return setErrResponse(res,"????????????????????????!");
			}
			cnOrder.setPkDept(pkDept);
			
			Map<String, Object> emp = cnCreateOrdMapper.qryEmp(req.getCodeDr());
			if(emp == null){
				return setErrResponse(res,"????????????????????????!");
			}
			cnOrder.setPkEmpOrd(getPropValueStr(emp, "pkEmp"));
			cnOrder.setNameEmpOrd(getPropValueStr(emp, "nameEmp"));
			
			String pkDeptNs = cnCreateOrdMapper.qryPkDept(req.getCodeDeptNs());
			if(!StringUtils.isNotBlank(pkDeptNs)){
				return setErrResponse(res,"????????????????????????!");
			}
			cnOrder.setPkDeptNs(pkDeptNs);
			
			String pkDeptEx = cnCreateOrdMapper.qryPkDept(req.getCodeDeptEx());
			if(!StringUtils.isNotBlank(pkDeptEx)){
				return setErrResponse(res,"????????????????????????!");
			}
			cnOrder.setPkDeptExec(pkDeptEx);
			if(!"0".equals(req.getFlagNote())&&!"1".equals(req.getFlagNote())){
				return setErrResponse(res,"flag_note??????!");
			}
			cnOrder.setFlagNote(req.getFlagNote());
			if(!"0".equals(req.getFlagBl())&&!"1".equals(req.getFlagBl())){
				return setErrResponse(res,"flag_bl??????!");
			}
			cnOrder.setFlagBl(req.getFlagBl());
			
			Integer ordTypeCount = cnCreateOrdMapper.qryOrdtype(req.getCodeOrdtype());
			if(ordTypeCount == null || ordTypeCount == 0){
				return setErrResponse(res,"????????????????????????!");
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
				return setErrResponse(res, "???????????????????????????");
			//cnCreateOrdMapper.qryDocCode("030208", req.getBtContent());
			cnTransApply.setDtBtAbo(req.getDtBtAbo());
			cnTransApply.setDtBtRh(req.getDtBtRh());
			cnTransApply.setPkOrg(cnCreateOrdMapper.getPkOrg());
			cnTransApply.setEuStatus("0");
			String btContent = req.getBtContent();
//			switch (req.getBtContent()) {
//				case "??????": btContent = "0001";
//				break;
//				case "???????????????": btContent = "0002";
//				break;
//				case "?????????????????????": btContent = "0007";
//				break;
//				case "?????????": btContent = "0011";
//				break;
//				case "???????????????": btContent = "0014";
//				break;
//				case "????????????????????????": btContent = "0016";
//				break;
//				case "??????????????????": btContent = "0019";
//				break;
//				case "????????????": btContent = "0025";
//				break;
//				case "??????????????????MAP???": btContent = "0030";
//				break;
//				case "???????????????????????????": btContent = "2001";
//				break;
//				case "?????????????????????": btContent = "9011";
//				break;
//				case "?????????????????????": btContent = "9012";
//				break;
//				case "?????????": btContent = "9013";
//				break;
//				default:return setErrResponse(res, "????????????????????????????????????" + req.getBtContent());
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
				return  setErrResponse(res, "???????????????????????????");
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
					res.setRetMsg(req.getNameOrd()+ "^" + "???????????????");
					}else {
						res.setRetMsg("???????????????");
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
			
 			return setErrResponse(res,"???????????????");
		}
	}

	//????????????????????????
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
	
	//???????????????
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
	    	String timeFormat = "yyyyMMddHHmmss";//????????????
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
	    	
	    	//???????????????
	    	Author aut=sub.getPlacerGroup().getAuthor();
	    	aut.setTypeCode("AUT");
	    	aut.setContextControlCode("OP");//TODO ???????????????/??????????????????
	    	String dateEnterStr = dateFormatString(timeFormat, getPropValueDate(ordMap, "dateEnter"));//TODO ??????????????????
	    	aut.getTime().setValue(dateEnterStr);//TODO ??????????????????
	    	aut.getSignatureCode().setCode("S");
	    	aut.getSignatureText().setValue(getPropValueStr(ordMap, "nameEmpInput"));//TODO ??????????????????????????????name_emp_input ??????????????????
	    	AssignedEntity entity=aut.getAssignedEntity();
	    	entity.setClassCode("ASSIGNED");
	    	entity.getId().getItem().setRoot("2.16.156.10011.1.4");
	    	entity.getId().getItem().setExtension(getPropValueStr(ordMap, "inCodeEmp"));//TODO ??????????????????
	    	entity.getAssignedPerson().setClassCode("PSN");
	    	entity.getAssignedPerson().setDeterminerCode("INSTANCE");
	    	entity.getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
	    	entity.getAssignedPerson().getName().getItem().getPart().setValue(getPropValueStr(ordMap, "nameEmpInput"));//???????????????
	    	RepresentedOrganization reporg=entity.getRepresentedOrganization();
	    	reporg.setClassCode("ORG");
	    	//MsgUtils
	    	reporg.setDeterminerCode("INSTANCE");
	    	reporg.getId().getItem().setRoot("2.16.156.10011.1.26");
	    	reporg.getId().getItem().setExtension(getPropValueStr(ordMap, "inCodeDept"));//TODO ??????????????????
	    	reporg.getName().setXSI_TYPE("BAG_EN");
	    	reporg.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "inNameDept"));//TODO ??????????????????
	    	
	    	//???????????????
	    	Verifier veri=sub.getPlacerGroup().getVerifier();
	    	veri.setTypeCode("VRF");
	    	veri.setContextControlCode("OP");//TODO ???????????????/??????????????????
	    	String dateChk = dateFormatString(timeFormat, getPropValueDate(ordMap, "dateChk"));
	    	veri.getTime().setValue(dateChk);//????????????
	    	veri.getSignatureCode().setCode("S");
	    	veri.getSignatureText().setValue(getPropValueStr(ordMap, "codeEmpChk"));//TODO ?????????????????????
	    	AssignedEntity veriEnt=veri.getAssignedEntity();
	    	veriEnt.setClassCode("ASSIGNED");
	    	veriEnt.getId().getItem().setRoot("2.16.156.10011.1.4");
	    	veriEnt.getId().getItem().setExtension(getPropValueStr(ordMap, "codeEmpChk"));//TODO ??????????????????
	    	veriEnt.getAssignedPerson().setClassCode("PSN");
	    	veriEnt.getAssignedPerson().setDeterminerCode("INSTANCE");
	    	veriEnt.getAssignedPerson().getName().setXSI_TYPE("BAG_EN");
	    	veriEnt.getAssignedPerson().getName().getItem().getPart().setValue(getPropValueStr(ordMap, "nameEmpChk"));//???????????????
	    	
	    	List<Component2> com2s=sub.getPlacerGroup().getComponent2s();//1..*
	    	for (int i = 0; i < orderList.size(); i++) {
	    		Component2 com2=new Component2();
	    		com2.getSequenceNumber().setValue(getPropValueStr(ordMap, "ordsn"));//????????????
	    		SubstanceAdministrationRequest subReq=com2.getSubstanceAdministrationRequest();
	    		subReq.setClassCode("SBADM");
	    		subReq.setModdCode("RQO");
	    		subReq.getId().setRoot("2.16.156.10011.1.28");
	    		subReq.getId().setExtension(getPropValueStr(ordMap, "ordsn"));//??????id
	    		subReq.getCode().setCode(getPropValueStr(ordMap, "codeOrdtype"));//????????????????????????
	    		subReq.getCode().setCodeSystem("2.16.156.10011.2.3.1.268");
	    		subReq.getCode().getDisplayName().setValue(getPropValueStr(ordMap, "nameOrdtype"));
	    		subReq.getText();
	    		subReq.getStatusCode().setCode("active");
	    		subReq.getEffectiveTime().setXSI_TYPE("QSC_TS");
	    		subReq.getEffectiveTime().setValidTimeLow(sdf.format(getPropValueDate(ordMap, "dateStart")));//????????????
	        	String dateEnd = dateFormatString(timeFormat, getPropValueDate(ordMap, "dateStop"));//??????????????????
	    		subReq.getEffectiveTime().setValidTimeHigh(dateEnd);//??????
	    		
	    		subReq.getEffectiveTime().getCode().setCode(getPropValueStr(ordMap, "codeFreq"));//??????
	    		subReq.getEffectiveTime().getCode().setCodeSystem("2.16.156.10011.2.5.1.13");
	    		subReq.getEffectiveTime().getCode().getDisplayName().setValue(getPropValueStr(ordMap, "freqName"));//????????????
	    		
	    		subReq.getRouteCode().setCode(getPropValueStr(ordMap, "codeSupply"));// 
	    		subReq.getRouteCode().setCodeSystem("2.16.156.10011.2.3.1.158");
	    		subReq.getRouteCode().setCodeSystemName("?????????????????????");//?????????????????????
	    		subReq.getRouteCode().getDisplayName().setValue(getPropValueStr(ordMap, "nameSupply"));//??????
	    		
	    		subReq.getDoseQuantity().setValue(getPropValueStr(ordMap, "dosage"));//????????????-??????
	    		subReq.getDoseQuantity().setUnit(getPropValueStr(ordMap, "nameDos"));//??????
	    		
	    		subReq.getDoseCheckQuantity().setXSI_TYPE("DSET_RTO");
	    		subReq.getDoseCheckQuantity().getItem().getNumerator().setXSI_TYPE("PQ");
	    		subReq.getDoseCheckQuantity().getItem().getNumerator().setUnit(getPropValueStr(ordMap, "nameQuan"));//??????
	    		subReq.getDoseCheckQuantity().getItem().getNumerator().setValue(getPropValueStr(ordMap, "quan"));//???
	    		subReq.getDoseCheckQuantity().getItem().getDenominator().setXSI_TYPE("PQ");
	    		subReq.getDoseCheckQuantity().getItem().getDenominator().setUnit("d");//??????
	    		subReq.getDoseCheckQuantity().getItem().getDenominator().setValue(getPropValueStr(ordMap, "days"));//???
				
	    		//???????????? 
	    		subReq.getAdministrationUnitCode().setCode(getPropValueStr(ordMap, "pddocCodeStd"));//???????????????????????????
	    		subReq.getAdministrationUnitCode().setCodeSystem("2.16.156.10011.2.3.1.211");
	    		subReq.getAdministrationUnitCode().setCodeSystemName("?????????????????????");
	    		subReq.getAdministrationUnitCode().getDisplayName().setValue(getPropValueStr(ordMap, "pddocNameStd"));//???????????????????????????
	    		
	    		//????????????
	    		Consumable2 con2=subReq.getConsumable2();
	    		con2.setTypeCode("CSM");
	    		ManufacturedProduct1 man1=con2.getManufacturedProduct1();
	    		man1.setClassCode("MANU");	
	    		man1.getId().setExtension(getPropValueStr(ordMap, "apprNo"));//TODO ???????????? ???????????????/?????????
	    		ManufacturedProduct man=man1.getManufacturedProduct();
	    		man.setClassCode("MMAT");
	    		man.setDeterminerCode("KIND");
	    		man.getCode().setCode(getPropValueStr(ordMap, "codePd"));//??????
	    		man.getCode().setCodeSystem("2.16.156.10011.2.5.1.14");
	    		man.getName().setXSI_TYPE("BAG_EN");
	    		man.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "namePd"));//??????
	    		man.getAsContent().setClassCode("CONT");
	    		man.getAsContent().getQuantity();
	    		ContainerPackagedProduct pro= man.getAsContent().getContainerPackagedProduct();
	    		pro.setClassCode("HOLD");
	    		pro.setDeterminerCode("KIND");
	    		pro.getCode();
	    		pro.getFormCode();
	    		//TODO ???????????? ??????????????????????????????????????????  
	    		pro.getCapacityQuantity().setUnit(getPropValueStr(ordMap, "nameDos"));
	    		pro.getCapacityQuantity().setValue(getPropValueStr(ordMap, "nameQuan"));
	    		
	    		//TODO ????????????????????????  ?????????????????????
	    		SubjectOf3 sub3=man1.getSubjectOf3();
	    		sub3.setTypeCode("SBJ");
	    		sub3.getPolicy().setClassCode("POLICY");
	    		sub3.getPolicy().setMoodCode("EVN");
	    		//!-- ????????????????????????/???????????????????????? -->
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
	    		sonlo.getId().getItem().setExtension(getPropValueStr(ordMap, "exCodeDept"));//??????????????????
	    		sonlo.getName().setXSI_TYPE("BAG_EN");
	    		sonlo.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "exNameDept"));//??????????????????
	    		subReq.getOccurrenceOf().getParentRequestReference().setClassCode("GROUPER");
	    		subReq.getOccurrenceOf().getParentRequestReference().getId().setExtension(getPropValueStr(ordMap, "ordsnParent"));//?????????id
	    		
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
	    		String euAlways=getPropValueStr(ordMap, "euAlwas");
	    		String euCode="";
	    		String euName="";
	    		if("0".equals(euAlways)){
	    			euCode="1";
	    			euName="????????????";
	    		}else if("1".equals(euAlways)){
	    			euCode="2";
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
	    		sonCom.getSupplyRequest().getQuantity().setValue(getPropValueStr(ordMap, "quanAp"));
	    		sonCom.getSupplyRequest().getQuantity().setUnit(getPropValueStr(ordMap, "nameUnitAp"));
	    		sonCom.getSupplyRequest().getExpectedUseTime().setValidTimeLow(sdm.format(getPropValueDate(ordMap, "dateStart")));//??????????????????
	        	String dateFormatString = dateFormatString(timeFormat, getPropValueDate(ordMap, "dateEnd"));//??????????????????
	    		sonCom.getSupplyRequest().getExpectedUseTime().setValidTimeHigh(dateFormatString);//??????????????????
	    		
	    		//??????????????????
	    		SubjectOf6 sub6=subReq.getSubjectOf6();
	    		sub6.setTypeCode("SUBJ");
	    		sub6.setContextConductionInd("false");
	    		sub6.getSeperatableInd().setValue("false");
	    		sub6.getAnnotation().getText().setValue(getPropValueStr(ordMap, "noteOrd"));
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
	    	itemIpt.setExtension(getPropValueStr(ordMap, "ipTimes"));
	    	itemIpt.setRoot("2.16.156.10011.2.5.1.8");
	    	Item itemCodeP = new Item();
	    	itemCodeP.setExtension(getPropValueStr(ordMap, "codePv"));//???????????????
	    	itemCodeP.setRoot("2.16.156.10011.2.5.1.9");
	    	itemList.add(itemIpt);
	    	itemList.add(itemCodeP);
//	    	enc.getId().getItem().setExtension(getPropValueStr(ordMap, "ipTimes"));
//	    	enc.getId().getItem().setRoot("2.16.156.10011.2.5.1.8");
//	    	enc.getId().getItem().setExtension(getPropValueStr(ordMap, "codePv"));//???????????????
//	    	enc.getId().getItem().setRoot("2.16.156.10011.2.5.1.9");
	    	String euPvtype=getPropValueStr(ordMap, "euPvType");
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
	    	item1.setExtension(getPropValueStr(ordMap, ""));//???id????????????
	    	Item item2=new Item();
	    	item2.setRoot("2.16.156.10011.2.5.1.4");
	    	item2.setExtension(getPropValueStr(ordMap, "pkPi"));//????????????
	    	Item item3=new Item();
	    	item3.setRoot("2.16.156.10011.1.10");
	    	item3.setExtension(getPropValueStr(ordMap, "codeOp"));//????????????
	    	Item item4=new Item();
	    	item4.setRoot("2.16.156.10011.1.12");
	    	item4.setExtension(getPropValueStr(ordMap, "codeIp"));//?????????
	    	items.add(item1);
	    	items.add(item2);
	    	items.add(item3);
	    	items.add(item4);
	    	pat.getTelecom().setXSI_TYPE("BAG_TEL");
	    	pat.getTelecom().getItem().setValue(getPropValueStr(ordMap, "telNo"));//????????????
	    	pat.getStatusCode().setCode("active");
	    	PatientPerson per=pat.getPatientPerson();
	    	per.setClassCode("PSN");
	    	per.setDeterminerCode("INSTANCE");
	    	per.getId().getItem().setRoot("2.16.156.10011.1.3");
	    	per.getId().getItem().setExtension(getPropValueStr(ordMap, "idNo"));//????????????
	    	per.getName().setXSI_TYPE("BAG_EN");
	    	per.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "namePi"));
	    	
	    	//??????
	    	
	    	per.getAdministrativeGenderCode().setCode(getPropValueStr(ordMap, "sexCodeStd"));
	    	per.getAdministrativeGenderCode().setCodeSystem("2.16.156.10011.2.3.3.4");
	    	per.getAdministrativeGenderCode().setCodeSystemName("????????????????????????GB/T 2261.1???");
	    	per.getAdministrativeGenderCode().getDisplayName().setValue(getPropValueStr(ordMap, "sexNameStd"));
	    	Date bDate = getPropValueDate(ordMap, "birthDate");
	    	String format = sd.format(bDate);
	        per.getBirthTime().setValue(format);//????????????
	    	//AsOtherIDs as = per.getAsOtherID();
	    	List<AsOtherIDs> asOtherIDsList = per.getAsOtherIDs();
	    	AsOtherIDs as = new AsOtherIDs();
	    	as.setClassCode("ROL");
	    	List<Item> asItems=as.getId().getItems();
	    	Item asitem1=new Item();
	    	asitem1.setRoot("2.16.156.10011.1.2");
	    	asitem1.setExtension(getPropValueStr(ordMap, ""));//??????????????????(??????)
	    	Item asitem2=new Item();
	    	asitem2.setRoot("2.16.156.10011.1.19");
	    	asitem2.setExtension(getPropValueStr(ordMap, "piHicNo"));//????????????
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
	    	serloc.getId().getItem().setExtension(getPropValueStr(ordMap, "bedCode"));//?????????
	    	serloc.getName().setXSI_TYPE("BAG_EN");
	    	serloc.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "bedName"));//????????????
	    	serloc.getAsLocatedEntityPartOf().setClassCode("LOCE");
	    	//?????????
	    	Location asloc= serloc.getAsLocatedEntityPartOf().getLocation();
	    	asloc.setClassCode("PLC");
	    	asloc.setDeterminerCode("INSTANCE");
	    	asloc.getId().getItem().setRoot("2.16.156.10011.1.21");
	    	asloc.getId().getItem().setExtension("");//????????????
	    	asloc.getName().setXSI_TYPE("BAG_EN");
	    	asloc.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "bedHouseno"));//????????????
	    	
	    	//????????????
	    	ServiceProviderOrganization org= enloc.getServiceDeliveryLocation().getServiceProviderOrganization();
	    	org.setClassCode("org");
	    	org.setDeterminerCode("INSTANCE");
	    	org.getId().getItem().setRoot("2.16.156.10011.1.26");
	    	org.getId().getItem().setExtension(getPropValueStr(ordMap, "pvCodeDept"));//????????????
	    	org.getName().setXSI_TYPE("BAG_EN");
	    	org.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "pvNameDept"));
	    	
	    	//??????
	    	AsOrganizationPartOf asorg= org.getAsOrganizationPartOf();
	    	asorg.setClassCode("PART");
	    	WholeOrganization whoorg= asorg.getWholeOrganization();
	    	whoorg.setClassCode("ORG");
	    	whoorg.setDeterminerCode("INSTANCE");
	    	whoorg.getId().getItem().setRoot("2.16.156.10011.1.27");
	    	whoorg.getId().getItem().setExtension(getPropValueStr(ordMap, "nsCodeDept"));//????????????
	    	whoorg.getName().setXSI_TYPE("BAG_EN");
	    	whoorg.getName().getItem().getPart().setValue(getPropValueStr(ordMap, "nsNameDept"));//????????????
	    	return req;
	    }
	 
	 private SysEsbmsg createSysEsbmsg(Request req, String action,String pkPv,String headType) {
			String reqXml=toRequestXml(req,req.getReqHead());
			String content = serverUtils.getSoapXML(action, reqXml);
			String id = req.getId().getExtension();
			SysEsbmsg sysEsbmsg = new SysEsbmsg(); 
			sysEsbmsg.setPkEsbmsg(NHISUUID.getKeyId());//????????????
			sysEsbmsg.setPkOrg("~");//??????
			sysEsbmsg.setIdMsg(id);//??????id
			sysEsbmsg.setPkPv(pkPv);//????????????
			sysEsbmsg.setDtEsbmsgtype(headType);//????????????
			sysEsbmsg.setContentMsg(content);//????????????
			sysEsbmsg.setEuStatus("0");//???????????????0????????????1.????????????2.???????????????
			sysEsbmsg.setDateSend(new Date());//????????????
			sysEsbmsg.setDescError("");//??????????????????
			sysEsbmsg.setCntHandle(0);//????????????
			sysEsbmsg.setIpSend(cnCreateOrdMapper.qryIpSend(action));//??????IP
			sysEsbmsg.setAddrSend("");//????????????
			sysEsbmsg.setNote("");//??????
			
			sysEsbmsg.setCreator("BLOOD");//?????????
			sysEsbmsg.setCreateTime(new Date());//????????????
			sysEsbmsg.setDelFlag("0");//
			sysEsbmsg.setTs(new Date());//?????????
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
     * ????????????????????????????????????""
     */
    private String dateFormatString(String format, Date propValueDate){
    	if (propValueDate == null||format == null) {
    		return "";
		}else {
			return sdf.format(propValueDate);
		}
    }
    
    /**
	 * ???????????????
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
				return (Date)obj;//?????????????????????Date???????????????
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
	 * ??????????????????/????????????
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
				return rtnErrMsg(res,"?????????????????????!");
			}
			ord.setPkPi(pkPi);
			Map<String, Object> pv = cnCreateOrdMapper.qryPkPv(ord.getCodePv(),ord.getEuPvtype());
			if(pv == null){
				return rtnErrMsg(res,"????????????????????????!");
			}
			ord.setPkPv(getPropValueStr(pv, "pkPv"));
			if(!"DEF99999".equals(ord.getCodeOrd())){
				Map<String, Object> ordMap = cnCreateOrdMapper.qryBdOrd(ord.getCodeOrd());
				if(ordMap == null ){
					return rtnErrMsg(res,"???????????????????????????!");
				}
				ord.setPkOrd(getPropValueStr(ordMap, "pkOrd"));
			}
			
			
			try {
				cnTransApply.setDatePlan(ord.getDateStart());
				ord.setDateEffe(ord.getDateStart());
				ord.setDateEnter(dateNow);
				ord.setDateSign(dateNow);
			} catch (Exception dataErr) {
				return rtnErrMsg(res,"?????????????????????!");
			} 
			
			String pkDept = cnCreateOrdMapper.qryPkDept(ord.getCodeDept());
			if(!StringUtils.isNotBlank(pkDept)){
				return rtnErrMsg(res,"????????????????????????!");
			}
			ord.setPkDept(pkDept);
			
			Map<String, Object> emp = cnCreateOrdMapper.qryEmp(ord.getCodeDr());
			if(emp == null){
				return rtnErrMsg(res,"????????????????????????!");
			}
			ord.setPkEmpOrd(getPropValueStr(emp, "pkEmp"));
			ord.setNameEmpOrd(getPropValueStr(emp, "nameEmp"));
			
			String pkDeptNs = cnCreateOrdMapper.qryPkDept(ord.getCodeDeptNs());
			if(!StringUtils.isNotBlank(pkDeptNs)){
				return rtnErrMsg(res,"????????????????????????!");
			}
			ord.setPkDeptNs(pkDeptNs);
			
			String pkDeptEx = cnCreateOrdMapper.qryPkDept(ord.getCodeDeptEx());
			if(!StringUtils.isNotBlank(pkDeptEx)){
				return rtnErrMsg(res,"????????????????????????!");
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
				return rtnErrMsg(res, "???????????????????????????");
			
			String btContent = ord.getBtContent();
			if(StringUtils.isNotBlank(btContent))
				cnTransApply.setBtContent(btContent);
			else {
				return  rtnErrMsg(res, "???????????????????????????");
			}
			
			//cnTransApply.setPkOrg(cnCreateOrdMapper.getPkOrg());
			cnTransApply.setPkOrg(pkOrg);
			cnTransApply.setFlagLab("0");
			cnTransApply.setFlagBthis("0");
			cnTransApply.setFlagPreg("0");
			cnTransApply.setFlagAl("0");
			cnTransApply.setCreator("????????????_CNBloodOrd");
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
			cnOrder.setCreator("????????????_CNBloodOrd");
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
