package com.zebone.nhis.webservice.cxf.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.cxf.INHISPskqSchWebService;
import com.zebone.nhis.webservice.dao.BdPubForWsMapper;
import com.zebone.nhis.webservice.dao.BlPubForWsMapper;
import com.zebone.nhis.webservice.dao.PvPubForWsMapper;
import com.zebone.nhis.webservice.dao.SchPubForWsMapper;
import com.zebone.nhis.webservice.pskq.model.entity.BdOuEmployee;
import com.zebone.nhis.webservice.service.BlPubForWsService;
import com.zebone.nhis.webservice.service.InvPubForWsService;
import com.zebone.nhis.webservice.service.LbPubForWsService;
import com.zebone.nhis.webservice.service.PskqPubForWsService;
import com.zebone.nhis.webservice.service.PvPubForWsService;
import com.zebone.nhis.webservice.service.SchPubForWsService;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.nhis.webservice.support.PskqSelfUtil;
import com.zebone.nhis.webservice.vo.BdOuDeptVo;
import com.zebone.nhis.webservice.vo.BdOuEmpPhoto;
import com.zebone.nhis.webservice.vo.BdOuOrgVo;
import com.zebone.nhis.webservice.vo.LBQueSchVo;
import com.zebone.nhis.webservice.vo.LbQueSchCount;
import com.zebone.nhis.webservice.vo.LbQuedocinfoResTemVo;
import com.zebone.nhis.webservice.vo.LbSHRequestVo;
import com.zebone.nhis.webservice.vo.LbSHResponseVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * @Classname NHISPskqSchWebServiceImpl
 * @Description ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
 * @Date 2020/11/4 10:12
 * @Created by zhangtao
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class NHISPskqSchWebServiceImpl implements INHISPskqSchWebService {

	@Resource
	private BlPubForWsService blPubForWsService;
	@Resource
	private BdPubForWsMapper bdPubForWsMapper;
	@Autowired
	private SchPubForWsMapper schPubForWsMapper;
	@Resource
	private SchPubForWsService schPubForWsService;
	@Resource
	private PvPubForWsMapper pvPubForWsMapper;
	@Resource
	private BlPubForWsMapper blPubForWsMapper;
	@Resource
	private PvPubForWsService pvPubForWsService;
	@Resource
	private InvPubForWsService invPubForWsService;
	@Resource
	private LbPubForWsService lbPubForWsService;

	@Resource
	private PskqPubForWsService pskqPubForWsService;
	
	/**
	 * ????????????-????????????
	 *
	 */	
	private Logger logger = LoggerFactory.getLogger("nhis.PskqSchWebServiceLog");
	ApplicationUtils apputil = new ApplicationUtils();

	@Override
	public String QueryHospitalInfo(String param) {
		// ????????????
		BdOuOrgVo bdOuOrg = new BdOuOrgVo();
		try {
			// ????????????
			List<Map<String, Object>> orgMap = bdPubForWsMapper.LbqueryOrgs();

			for (int i = 0; i < orgMap.size(); i++) {
				if (("0101").equals(orgMap.get(i).get("codeOrg"))) {
					bdOuOrg.setCodeOrg(orgMap.get(i).get("codeOrg").toString());
					bdOuOrg.setNameOrg(orgMap.get(i).get("nameOrg").toString());
					break;
				}
			}
			orgMap = null;
		} catch (Exception e) {
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????"),LbSHResponseVo.class, false);
		}

		bdOuOrg.setResVo(LbSelfUtil.commonVo(Constant.RESSUC, "??????"));

		return XmlUtil.beanToXml(bdOuOrg, bdOuOrg.getClass(), false);
	}

	@Override
	public String QueryDeptInfo(String param) {
		logger.info("QueryDeptInfo???????????????????????????"+param);
		LbSHRequestVo requ = (LbSHRequestVo) XmlUtil.XmlToBean(param,
				LbSHRequestVo.class);
		Map<String, Object> reqMap = new HashMap<String ,Object>();
		if (StringUtils.isNotBlank(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isNotNull(requ.getPyCode())){
				reqMap.put("pyCode", requ.getPyCode());
			}
			if(CommonUtils.isNotNull(requ.getDeptCode())){
				reqMap.put("codeDept", requ.getDeptCode());
			}
		}

		List<BdOuDeptVo> ListDept = new ArrayList<>();
		// ????????????
		LbSHResponseVo responseVo = new LbSHResponseVo();
		reqMap.put("flagOp", "1");
		try {
			List<Map<String, Object>> deptList = bdPubForWsMapper.queryDept(reqMap);

			for (int i = 0; i < deptList.size(); i++) {
				if (deptList.get(i).get("codeDept").toString().length() > 2) {
					// ????????????
					BdOuDeptVo dept = new BdOuDeptVo();
					dept.setCodeDept(LbSelfUtil.getPropValueStr(deptList.get(i),
							"codeDept"));
					dept.setNameDept(LbSelfUtil.getPropValueStr(deptList.get(i),
							"nameDept"));
					dept.setPyCode(LbSelfUtil.getPropValueStr(deptList.get(i),
							"pyCode"));
					dept.setDeptDesc(LbSelfUtil.getPropValueStr(deptList.get(i),
							"deptDesc"));
					ListDept.add(dept);
				}
			}
			// ??????????????????????????????
			responseVo.setDeptList(ListDept);
		} catch (Exception e) {
			logger.error("???????????????????????????{}",e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "??????"));
		logger.info("???????????????????????????"+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}

	@Override
	public String QueryDoctorInfos(String param) {
		logger.info("????????????????????????{}",param);
		LbSHRequestVo req = (LbSHRequestVo) XmlUtil.XmlToBean(param,LbSHRequestVo.class);
		Map<String, Object> paramMap = LbSelfUtil.toMap(req);	
		String resXml=null;
		// ????????????
	    LbSHResponseVo responseVo = new LbSHResponseVo(); 
		try {
			List<Map<String, Object>> queryEmployee = bdPubForWsMapper.queryEmployee(paramMap);
			if (queryEmployee != null) {
				List<LbQuedocinfoResTemVo> itemlist = new ArrayList<>();
				for (Map<String, Object> map : queryEmployee) {
					LbQuedocinfoResTemVo lbRestemVo = ApplicationUtils.mapToBean(map,LbQuedocinfoResTemVo.class);
					itemlist.add(lbRestemVo);
				}
				responseVo.setResTemList(itemlist);
			}
		} catch (Exception e) {
			logger.info("???????????????????????????"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "??????"));
		resXml=XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		logger.info("????????????????????????{}",resXml);
		return resXml;
	}

	@Override
	public String QuerySchDept(String param) {
		logger.info("?????????????????????"+param);
		LbSHRequestVo requ = null;
		DateTime dt = DateTime.now();
		// ????????????
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if (null != param && !("").equals(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????"), LbSHResponseVo.class, false);
			}
		} else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????"), LbSHResponseVo.class, false);
		}
		if("815003".equals(requ.getDeviceid())){
			requ.setDeviceid("zzj");
		}
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "?????????????????????????????????????????????"), LbSHResponseVo.class, false);
		}
		
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkOrg", user.getPkOrg());
			
			//????????????????????????????????????
			if(BeanUtils.isNotNull(requ.getEndDate()) && BeanUtils.isNotNull(requ.getStartDate())){
				paramMap.put("StartDate", requ.getStartDate());// ???????????????
				paramMap.put("EndDate", requ.getEndDate());// ??????????????????
			}else {
				//?????????????????????????????????
				if(BeanUtils.isNotNull(requ.getRegDate()) && requ.getRegDate().compareTo(dt.toString("YYYY-MM-dd"))>0){
					paramMap.put("nowDate", requ.getRegDate());
				}else{
					paramMap.put("nowDate", dt.toString("YYYY-MM-dd"));// ???????????????
				}
			}
			
			paramMap.put("codeDept", requ.getDeptCode());//??????????????????
			paramMap.put("doctCode", requ.getDoctCode());//??????????????????
			/** 1.???????????????????????? */
			List<Map<String, Object>> todaylist = schPubForWsMapper.PskqTodaySchInfos(paramMap);
			List<BdOuDeptVo> todaySet = new ArrayList<BdOuDeptVo>();
			for (Map<String, Object> today:todaylist) {
				boolean ifset = true;
				if(todaylist.size()>0){
					for(BdOuDeptVo tofmsp: todaySet){
						if(tofmsp.getCodeDept().equals(LbSelfUtil.getPropValueStr(today,"codeDept"))){
							ifset = false;
						}
					}
				}
				if(ifset){
					// ????????????
					BdOuDeptVo dept = new BdOuDeptVo();
					dept.setCodeDept(LbSelfUtil.getPropValueStr(today,"codeDept"));
					dept.setNameDept(LbSelfUtil.getPropValueStr(today,"nameDept"));
					dept.setDeptDesc(LbSelfUtil.getPropValueStr(today,"deptDesc"));
					todaySet.add(dept);//?????????????????????
				}
			}

			String sql = "select B.CODE_DEPT code from sch_resource a inner join bd_ou_dept b on a.pk_dept_belong=b.pk_dept where a.pk_dept is null and flag_stop='1'";
			List<Map<String,Object>> result = DataBaseHelper.queryForList(sql);
			for (Map<String, Object> map : result) {
				String code = MapUtils.getString(map,"code","");
				todaySet.removeIf(vo -> code.equals(vo.getCodeDept()));
			}


			// ??????????????????????????????
			responseVo.setDeptList(todaySet);

		} catch (Exception e) {
			logger.info("?????????????????????"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????"), LbSHResponseVo.class, false);
		}

		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "??????"));
		logger.info("?????????????????????"+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * ??????????????????????????????
	 * @param param
	 * @return
	 */
	@Override
	public String QuerySchDoctor(String param) {
		logger.info("?????????????????????"+param);
		LbSHRequestVo requ = null;
		DateTime dt = DateTime.now();
		// ????????????
		LbSHResponseVo responseVo = new LbSHResponseVo();
		if (null != param && !("").equals(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????"), LbSHResponseVo.class, false);
			}
		} else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????"), LbSHResponseVo.class, false);
		}
		
		User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "?????????????????????????????????????????????"), LbSHResponseVo.class, false);
		}
		
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkOrg", user.getPkOrg());
			//????????????????????????????????????
			if(BeanUtils.isNotNull(requ.getEndDate()) && BeanUtils.isNotNull(requ.getStartDate())){
				paramMap.put("StartDate", requ.getStartDate());// ???????????????
				paramMap.put("EndDate", requ.getEndDate());// ??????????????????
			}else {
				//?????????????????????????????????
				if(BeanUtils.isNotNull(requ.getRegDate()) && requ.getRegDate().compareTo(dt.toString("YYYY-MM-dd"))>0){
					paramMap.put("nowDate", requ.getRegDate());
				}else{
					paramMap.put("nowDate", dt.toString("YYYY-MM-dd"));// ???????????????
				}
			}
			paramMap.put("codeDept", requ.getDeptCode());//??????????????????
			paramMap.put("doctCode", requ.getDoctCode());//??????????????????
			/** 1.???????????????????????? */
			List<Map<String, Object>> todaylist = schPubForWsMapper.PskqTodaySchInfos(paramMap);
			List<LbQuedocinfoResTemVo> todaySet = new ArrayList<LbQuedocinfoResTemVo>();
			for (Map<String, Object> today:todaylist) {
				boolean ifset = true;
				if(todaylist.size()>0){
					for(LbQuedocinfoResTemVo tofmsp: todaySet){
						if(tofmsp.getDoctCode().equals(LbSelfUtil.getPropValueStr(today,"codeEmp"))){
							ifset = false;
						}
					}
				}
				if(ifset){
					//????????????
					LbQuedocinfoResTemVo doc = new LbQuedocinfoResTemVo();
					doc.setDoctCode(LbSelfUtil.getPropValueStr(today,"codeEmp"));
					doc.setDoctName(LbSelfUtil.getPropValueStr(today,"nameEmp"));
					doc.setCodeDept(LbSelfUtil.getPropValueStr(today,"codeDept"));
					doc.setDeptName(LbSelfUtil.getPropValueStr(today,"nameDept"));
					doc.setEmpsrvtype(LbSelfUtil.getPropValueStr(today,"name"));
					doc.setDoctDesc(LbSelfUtil.getPropValueStr(today,"spec"));

					todaySet.add(doc);
				}
			}
			// ??????????????????????????????
			responseVo.setResTemList(todaySet);
		} catch (Exception e) {
			logger.info("?????????????????????"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "??????"));
		logger.info("?????????????????????"+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	
	}
	@Override
	public String QuerySch(String param) {
		// TODO Auto-generated method stub
		logger.info("?????????????????????"+param);
		LbSHRequestVo requ = null;
		// ????????????
		LbSHResponseVo responseVo = new LbSHResponseVo();
		List<LBQueSchVo> queSchList = new ArrayList<>();
		
		if (null != param && !("").equals(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDeviceid())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????"), LbSHResponseVo.class, false);
			}
		} else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????????????????"), LbSHResponseVo.class, false);
		}
		String deviceid=null;
		if("2510".equals(requ.getDeviceid())) {
			deviceid = "wxgzh";
		}else if("2500".equals(requ.getDeviceid()) || requ.getDeviceid().toUpperCase().contains("ZZJ")) {
			deviceid = "zzj";
		}else{
			deviceid=requ.getDeviceid();
		}
		User user = PskqSelfUtil.getDefaultUser(deviceid);
		//User user = LbSelfUtil.getDefaultUser(requ.getDeviceid());
		if(!BeanUtils.isNotNull(user)){
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "?????????????????????????????????????????????"), LbSHResponseVo.class, false);
		}
		UserContext.setUser(user);

		try {
			//??????????????????????????????
			String ifFilter =ApplicationUtils.getSysparam("SCH0016", false);
			
	    	DateTime dt = DateTime.now();
	    	Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("pkOrg", user.getPkOrg());
			//?????????????????????????????????
			if(BeanUtils.isNotNull(requ.getEndDate()) && BeanUtils.isNotNull(requ.getStartDate())){
				paramMap.put("StartDate", requ.getStartDate());// ???????????????
				paramMap.put("EndDate", requ.getEndDate());// ??????????????????
			}else {
				//???????????????????????????????????????????????????????????????
				//??????????????????????????????????????????
				if(BeanUtils.isNotNull(requ.getRegDate()) && requ.getRegDate().compareTo(dt.toString("YYYY-MM-dd"))>0){
					paramMap.put("nowDate", requ.getRegDate());
				}else{
					paramMap.put("nowDate", dt.toString("YYYY-MM-dd"));// ???????????????		
				}
			}
			paramMap.put("codeDept", requ.getDeptCode());// ????????????
			paramMap.put("doctCode", requ.getDoctCode());// ????????????
	    	List<Map<String, Object>> schpubList = schPubForWsMapper.PskqTodaySchInfos(paramMap);
	    	if(schpubList != null && schpubList.size() > 0) {
	    		for (Map<String, Object> schpubMap:schpubList){
	    			//???????????????????????????????????????
		    		if("1".equals(ifFilter)) {
		    			schpubMap.put("dtApptype", "0");//??????????????????????????? 	
		    		}
		    		String dateWork = LbSelfUtil.getPropValueStr(schpubMap,"dateWork");
		    		//?????????????????????
		    		if(!CommonUtils.isEmptyString(dateWork) && dateWork.compareTo(dt.toString("yyyy-MM-dd")) < 0) {
		    			continue;
		    		} /*else if(!CommonUtils.isEmptyString(dateWork) && dateWork.compareTo(dt.toString("yyyy-MM-dd")) == 0){
		    			schpubMap.put("time", dt.toString("HH:mm:ss"));// ????????????
		    		}*/else {
		    			schpubMap.remove("time");
		    		}
		        	List<LbQueSchCount> queCountList = new ArrayList<>();
		        	List<Map<String, Object>> schSecList = schPubForWsMapper.querySchAppTicket(schpubMap);
		        	Integer cntTotal = 0;
		        	Integer apptCount = 0;
		        	Integer cntUsed = 0;
		    		for(Map<String, Object> schSecMap:schSecList){
		    			if(0<Integer.valueOf(schSecMap.get("cnt").toString())){
		    				Integer cnt = Integer.valueOf(LbSelfUtil.getPropValueStr(schSecMap,"cnt")) ;
		    				Integer appt = Integer.valueOf(LbSelfUtil.getPropValueStr(schSecMap,"cntAppt")) ;
		    				Integer used = Integer.valueOf(LbSelfUtil.getPropValueStr(schSecMap,"cntUsed")) ;
		    				LbQueSchCount schCount=new LbQueSchCount();
		    				//???????????????
		    				schCount.setHaveCount(String.valueOf(cnt - used));
		    				//????????????????????????
		    				schCount.setAvailableCount(String.valueOf(appt));
		    				schCount.setThanCount(schCount.getAvailableCount());
		    				if(!CommonUtils.isEmptyString(dateWork) && dateWork.compareTo(dt.toString("yyyy-MM-dd")) == 0){
		    					String nowdata=DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm");
		    					String dateWorks=LbSelfUtil.getPropValueStr(schpubMap,"dateWork");
		    					dateWorks=dateWorks+" "+LbSelfUtil.getPropValueStr(schSecMap,"timeEnd").substring(0, 5);
		    					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		    					Date sd1=df.parse(nowdata);
		    					Date sd2=df.parse(dateWorks);
		    					if(sd2.compareTo(sd1)==-1){
		    						schCount.setThanCount("0");
		    					}
				    		}
		    				
		    				//????????????
		    				schCount.setPhaseCode(LbSelfUtil.getPropValueStr(schSecMap,"pkDateslotsec"));
		    				String phase = LbSelfUtil.getPropValueStr(schpubMap,"nameDateslot")+","+LbSelfUtil.getPropValueStr(schSecMap,"timeBegin").substring(0, 5)+"-"+LbSelfUtil.getPropValueStr(schSecMap,"timeEnd").substring(0, 5);
		    				schCount.setPhaseDesc(phase);//????????????
		    				queCountList.add(schCount);
		    				cntTotal += cnt;
		    				apptCount += appt;
		    				cntUsed += used;
		    			}
		    		}
	    			LBQueSchVo queSchVo = new LBQueSchVo();
		    		queSchVo.setPhaseDescName(LbSelfUtil.getPropValueStr(schpubMap,"nameDateslot"));
		    		queSchVo.setSchCount(queCountList);
		    		queSchVo.setRegId(LbSelfUtil.getPropValueStr(schpubMap,"pkSch"));
		    		queSchVo.setRegDate(LbSelfUtil.getPropValueStr(schpubMap,"dateWork"));
		    		queSchVo.setBookType("1");
		    		queSchVo.setTypeCode(LbSelfUtil.getPropValueStr(schpubMap,"typecode"));
		    		queSchVo.setTypeName(LbSelfUtil.getPropValueStr(schpubMap,"typename"));
		    		queSchVo.setDeptCode(LbSelfUtil.getPropValueStr(schpubMap,"codeDept"));
		    		queSchVo.setDeptName(LbSelfUtil.getPropValueStr(schpubMap,"nameDept"));
		    		queSchVo.setDoctCode(LbSelfUtil.getPropValueStr(schpubMap,"codeEmp"));
		    		queSchVo.setDoctName(LbSelfUtil.getPropValueStr(schpubMap,"nameEmp"));
		    		queSchVo.setRankCode(LbSelfUtil.getPropValueStr(schpubMap,"code"));
		    		queSchVo.setRankName(LbSelfUtil.getPropValueStr(schpubMap,"name"));
		    		queSchVo.setSpec(LbSelfUtil.getPropValueStr(schpubMap,"spec"));
		    		queSchVo.setDeptDesc(LbSelfUtil.getPropValueStr(schpubMap,"deptDesc"));
		    		//????????????
		    		queSchVo.setAllCount(String.valueOf(cntTotal));
		    		//??????????????????
		    		queSchVo.setApptCount(String.valueOf(apptCount));
		    		//?????????????????????
		    		queSchVo.setOutCount(String.valueOf(cntUsed));
		    		//??????????????????
		    		queSchVo.setStatus(LbSelfUtil.getPropValueStr(schpubMap,"flagStop"));
		    		//????????????
		    		queSchVo.setTotalFee(LbSelfUtil.getPropValueStr(schpubMap,"price"));
		    		//?????????
		    		queSchVo.setRegFee(LbSelfUtil.getPropValueStr(schpubMap,"price"));
		    		//?????????
		    		queSchVo.setTreatFee("");
		    		//?????????
		    		queSchVo.setServiceFee("");
		    		//????????????
		    		queSchVo.setOtherFee("");
		    		queSchVo.setLocation(LbSelfUtil.getPropValueStr(schpubMap,"namePlace"));
		    		
		    		queSchVo.setUnitName(LbSelfUtil.getPropValueStr(schpubMap,"unitName"));
		    		queSchList.add(queSchVo);
		        	
				}
	    	}
			responseVo.setQueSchVo(queSchList);
		} catch (Exception e) {
			logger.info("?????????????????????"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "??????"));
		logger.info("?????????????????????"+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * ??????????????????
	 */
	@Override
	public String queryDoctorPhoto(String param) {
		logger.info("?????????????????????"+param);
		LbSHRequestVo requ = new LbSHRequestVo();
		if (null != param && !("").equals(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
		}
		// ????????????
		LbSHResponseVo responseVo = new LbSHResponseVo();
		List<BdOuEmpPhoto> docts = new ArrayList<>();
		List<BdOuEmployee> doctList=new ArrayList<>();
		try {
			String sql="select * from bd_ou_employee where del_flag='0' and photo is not null ";
			if(CommonUtils.isEmptyString(requ.getDoctCode())){
				doctList=DataBaseHelper.queryForList(sql,BdOuEmployee.class);
			}else{
				doctList=DataBaseHelper.queryForList(sql+" and CODE_EMP=?",BdOuEmployee.class,new Object[]{requ.getDoctCode()});
			}
			for (BdOuEmployee bdOuEmployee : doctList) {
				BdOuEmpPhoto bdOuEmpPhoto=new BdOuEmpPhoto();
				bdOuEmpPhoto.setCodeEmp(bdOuEmployee.getCodeEmp());
				bdOuEmpPhoto.setNameEmp(bdOuEmployee.getNameEmp());
				if(null!=bdOuEmployee.getPhoto()){
					bdOuEmpPhoto.setPhoto(Base64.getEncoder().encodeToString(bdOuEmployee.getPhoto()));
				}
				docts.add(bdOuEmpPhoto);
			}
			responseVo.setDoctors(docts);
		} catch (Exception e) {
			logger.info("?????????????????????"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "??????"));
		logger.info("?????????????????????"+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}
	/**
	 * ????????????????????????????????????
	 */
	@Override
	public String queryAppointRegList(String param) {
		logger.info("???????????????????????????????????????"+param);
		LbSHRequestVo requ = new LbSHRequestVo();
		if (null != param && !("").equals(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isEmptyString(requ.getDoctCode())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getStartDate())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
			}
			if(CommonUtils.isEmptyString(requ.getEndDate())){
				return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
			}
		} else{
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "????????????????????????"), LbSHResponseVo.class, false);
		}
		
		
		// ????????????
		LbSHResponseVo responseVo = new LbSHResponseVo();
		try {
			responseVo.setSchAppts(pskqPubForWsService.searchSchAppt(requ));
		} catch (Exception e) {
			logger.info("???????????????????????????????????????"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "??????"));
		logger.info("???????????????????????????????????????"+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}

	@Override
	public String QueryAllDept(String param) {
		logger.info("QueryDeptInfo???????????????????????????"+param);
		LbSHRequestVo requ = (LbSHRequestVo) XmlUtil.XmlToBean(param,
				LbSHRequestVo.class);
		Map<String, Object> reqMap = new HashMap<String ,Object>();
		if (StringUtils.isNotBlank(param)) {
			requ = (LbSHRequestVo) XmlUtil.XmlToBean(param, LbSHRequestVo.class);
			if(CommonUtils.isNotNull(requ.getPyCode())){
				reqMap.put("pyCode", requ.getPyCode());
			}
			if(CommonUtils.isNotNull(requ.getDeptCode())){
				reqMap.put("codeDept", requ.getDeptCode());
			}
		}

		List<BdOuDeptVo> ListDept = new ArrayList<>();
		// ????????????
		LbSHResponseVo responseVo = new LbSHResponseVo();
		reqMap.put("flagOp", "1");
		try {
			List<Map<String, Object>> deptList = bdPubForWsMapper.queryAllDept(reqMap);

			for (int i = 0; i < deptList.size(); i++) {
				// ????????????
				BdOuDeptVo dept = new BdOuDeptVo();
				dept.setCodeDept(LbSelfUtil.getPropValueStr(deptList.get(i),
						"deptCode"));
				dept.setNameDept(LbSelfUtil.getPropValueStr(deptList.get(i),
						"nameDept"));
				dept.setDeptDesc(LbSelfUtil.getPropValueStr(deptList.get(i),
						"deptDesc"));
				ListDept.add(dept);
			}
			// ??????????????????????????????
			responseVo.setDeptList(ListDept);
		} catch (Exception e) {
			logger.error("???????????????????????????{}",e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "??????"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "??????"));
		logger.info("???????????????????????????"+XmlUtil.beanToXml(responseVo, responseVo.getClass(), false));
		return XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
	}

	@Override
	public String QueryAllDoctor(String param) {
		logger.info("????????????????????????{}",param);
		LbSHRequestVo req = (LbSHRequestVo) XmlUtil.XmlToBean(param,LbSHRequestVo.class);
		Map<String, Object> paramMap = LbSelfUtil.toMap(req);
		String resXml=null;
		// ????????????
		LbSHResponseVo responseVo = new LbSHResponseVo();
		try {
			List<Map<String, Object>> queryEmployee = bdPubForWsMapper.queryAllDoctor(paramMap);
			if (queryEmployee != null) {
				List<LbQuedocinfoResTemVo> itemlist = new ArrayList<>();
				for (Map<String, Object> map : queryEmployee) {
					LbQuedocinfoResTemVo lbRestemVo = ApplicationUtils.mapToBean(map,LbQuedocinfoResTemVo.class);
					itemlist.add(lbRestemVo);
				}
				responseVo.setResTemList(itemlist);
			}
		} catch (Exception e) {
			logger.info("???????????????????????????"+e);
			return XmlUtil.beanToXml(LbSelfUtil.common(Constant.RESFAIL, "???????????????????????????"), LbSHResponseVo.class, false);
		}
		responseVo.setCommonVo(LbSelfUtil.commonVo(Constant.RESSUC, "??????"));
		resXml=XmlUtil.beanToXml(responseVo, responseVo.getClass(), false);
		logger.info("????????????????????????{}",resXml);
		return resXml;
	}


}
