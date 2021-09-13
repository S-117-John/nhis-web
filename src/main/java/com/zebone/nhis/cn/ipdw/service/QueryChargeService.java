package com.zebone.nhis.cn.ipdw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.QueryChargeMapper;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.mybatis.DynaBean;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class QueryChargeService {
	static org.apache.logging.log4j.Logger log = LogManager.getLogger(QueryChargeService.class);

	@Autowired      
	private QueryChargeMapper queryChargeMapper;
	
	public DynaBean getChargeSummary(String param, IUser user){
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		DynaBean ret = new DynaBean();
		String pkPv = paramMap.get("pkPv");
		//List<DynaBean> rs = queryChargeMapper.getChargeSummary(paramMap.get("pkPv"));
		ret.put("sumDeposit", queryChargeMapper.getChargeSumDeposit(pkPv));
		ret.put("sumGuarantee", queryChargeMapper.getChargeSumGuarantee(pkPv));
		ret.put("sumAmount", queryChargeMapper.getChargeSumAmount(pkPv));
		ret.put("sumAmount_pi", queryChargeMapper.getChargeSumAmountPi(pkPv));
//		if(rs.size()==1)
//			ret = rs.get(0);
		return ret;
	}

	public List<DynaBean> getCharge(String param, IUser user) throws Exception {
		Map<String, Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>(){});
		String queryType = (String) paramMap.get("queryType");
		if(StringUtils.isBlank((String) paramMap.get("pkDept"))) paramMap.remove("pkDept");
		List<DynaBean> ret = new ArrayList<DynaBean>();
		if("C".equals(queryType))
			ret = queryChargeMapper.getChargeByCategory(paramMap);
		else{
			String pkPv=(String) paramMap.get("pkPv");
			PvEncounter pvEncounter=DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where pk_pv=?", PvEncounter.class, pkPv);
			if(paramMap.get("dtStart")==null) paramMap.remove("dtStart");
			else {
				Date dtStart = DateUtils.getDefaultDateFormat().parse((String)paramMap.get("dtStart"));
				paramMap.put("dtStart",DateUtils.dateToStr("yyyy-MM-dd",dtStart)+" 00:00:00");
			}
			if(paramMap.get("dtEnd")==null) paramMap.remove("dtEnd");
			else {
				Date dtEnd = DateUtils.getDefaultDateFormat().parse((String)paramMap.get("dtEnd"));
				paramMap.put("dtEnd", DateUtils.dateToStr("yyyy-MM-dd", dtEnd)+" 23:59:59");
			}
			String cateCode = (String) paramMap.get("cateCode");
			if(StringUtils.isBlank(cateCode)){
				paramMap.remove("cateCode");
			}else{
				cateCode=cateCode+"%";
				paramMap.put("cateCode", cateCode);
			}
			String pkDeptExec = (String) paramMap.get("pkDeptExec");
			if(StringUtils.isBlank(pkDeptExec)) paramMap.remove("pkDeptExec");
			String nameCg = (String) paramMap.get("nameCg");
			if(StringUtils.isBlank(nameCg)) paramMap.remove("nameCg");
			if("D".equals(queryType)){
				if("0".equals(pvEncounter.getFlagIn())){
					ret = queryChargeMapper.getChargeDetailAndDump(paramMap);
				}else{
					ret = queryChargeMapper.getChargeDetail(paramMap);
				}
			}
			else if("T".equals(queryType))
				if("0".equals(pvEncounter.getFlagIn())){
					ret = queryChargeMapper.getChargeByItemAndDump(paramMap);
				}else{
					ret = queryChargeMapper.getChargeByItem(paramMap);
				}
		}
		return ret;
	}

	public List<DynaBean> queryEncHistory(String param, IUser user) throws Exception {
		Map<String, String> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,String>>(){});
		String pvType = paramMap.get("pvType");
		if(StringUtils.isBlank(pvType))paramMap.remove("pvType");
		String dtStart = paramMap.get("dtStart");
		String dtEnd = paramMap.get("dtEnd");
		if(StringUtils.isBlank(dtStart)) paramMap.remove("dtStart");
		else {
			Date dStart = DateUtils.getDefaultDateFormat().parse(dtStart);
			paramMap.put("dtStart",DateUtils.dateToStr("yyyy-MM-dd",dStart)+" 00:00:00");
		}
		if(StringUtils.isBlank(dtEnd)) paramMap.remove("dtEnd");
		else {
			Date dEnd = DateUtils.getDefaultDateFormat().parse(dtEnd);
			paramMap.put("dtEnd", DateUtils.dateToStr("yyyy-MM-dd", dEnd)+" 23:59:59");
		}
		List<DynaBean> ret = new ArrayList<DynaBean>();
		ret = queryChargeMapper.queryEncHistory(paramMap);
		return ret;
	}
}
