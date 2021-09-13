package com.zebone.nhis.pro.zsba.compay.pub.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.vo.SettleRecord;
import com.zebone.nhis.pro.zsba.compay.pub.dao.ZsBlIpSettleQryMapper;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 结算信息查询
 * @author IBM
 *
 */
@Service
public class ZsBlIpSettleQueryService {
	
	@Autowired
	private ZsBlIpSettleQryMapper mapper;
	
	private static String DATENULL = "0000";
	
	@SuppressWarnings("unchecked")
	//住院结算查询
	public List<SettleRecord> qryIpStRecords(String param,IUser user){
		 List<SettleRecord> res = null;
		 Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		 String dateBegin = (String)params.get("dateBegin");
		 String dateEnd= (String)params.get("dateEnd");
		 params.put("pkOrg", UserContext.getUser().getPkOrg());
		 if(DATENULL.equals(dateBegin)){
			 params.put("dateBegin", null);
		 }
		 if(DATENULL.equals(dateEnd)){
			 params.put("dateEnd", null);
		 }
		 if(params.get("dtSttype")==null || params.get("dtSttype").equals("")){
			 res = mapper.qryIpStRecordAll(params);
			 return res;
		 }
		 res = mapper.qryStRecords(params);
		 return res;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 门诊结算查询
	 * 交易号：022003027125
	 */
	public List<SettleRecord> qryStRecords(String param,IUser user){
		 List<SettleRecord> res = null;
		 Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		 String dateBegin = (String)params.get("dateBegin");
		 String dateEnd= (String)params.get("dateEnd");
		 params.put("pkOrg", UserContext.getUser().getPkOrg());
		 if(DATENULL.equals(dateBegin)){
			 params.put("dateBegin", null);
		 }
		 if(DATENULL.equals(dateEnd)){
			 params.put("dateEnd", null);
		 }
		 if(params.get("dtSttype")==null || params.get("dtSttype").equals("")){
			 res = mapper.qryStRecordAll(params);
			 return res;
		 }
		 res = mapper.qryStRecords(params);
		 return res;
	}
}