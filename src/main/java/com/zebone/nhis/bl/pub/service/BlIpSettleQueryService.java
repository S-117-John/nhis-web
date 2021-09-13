package com.zebone.nhis.bl.pub.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.dao.BlIpSettleQryMapper;
import com.zebone.nhis.bl.pub.vo.PageSettleRecord;
import com.zebone.nhis.bl.pub.vo.SettleRecord;
import com.zebone.nhis.bl.pub.vo.StQryCharge;
import com.zebone.nhis.bl.pub.vo.StQryDepoInfo;
import com.zebone.nhis.bl.pub.vo.StQryInsuSt;
import com.zebone.nhis.bl.pub.vo.StQryInvDt;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 结算信息查询
 * @author IBM
 *
 */
@Service
public class BlIpSettleQueryService {
	
	@Autowired
	private BlIpSettleQryMapper mapper;
	
	private static String DATENULL = "0000";
	
	@SuppressWarnings("unchecked")
	//门诊结算查询
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
	
	//门诊结算查询（分页）
	public PageSettleRecord qryStRecordsPaging(String param,IUser user){
		 PageSettleRecord stListAndTotal = new PageSettleRecord();
		 List<SettleRecord> res = null;
		 Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		 String dateBegin = (String)params.get("dateBegin");
		 String dateEnd= (String)params.get("dateEnd");
		 int pageIndex = CommonUtils.getInteger(params.get("pageIndex"));
		 int pageSize = CommonUtils.getInteger(params.get("pageSize"));
		 params.put("pkOrg", UserContext.getUser().getPkOrg());
		 if(DATENULL.equals(dateBegin)){
			 params.put("dateBegin", null);
		 }
		 if(DATENULL.equals(dateEnd)){
			 params.put("dateEnd", null);
		 }
		// 分页操作
	    MyBatisPage.startPage(pageIndex, pageSize);
		 if(params.get("dtSttype")==null || params.get("dtSttype").equals("")){
			 res = mapper.qryStRecordAll(params);
		 }else {
			 res = mapper.qryStRecords(params);
		 }
		 Page<List<SettleRecord>> page = MyBatisPage.getPage();
		 stListAndTotal.setSettleRecordList(res);
		 stListAndTotal.setTotalCount(page.getTotalCount());
		 return stListAndTotal;
	}
	
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
	public List<com.zebone.nhis.bl.pub.vo.StQryInvInfo> qryInvInfo(String param,IUser user){
		 Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		 return mapper.qryInvInfo(params);
	}
	@SuppressWarnings("unchecked")
	public List<StQryDepoInfo> qryDepoInfo(String param,IUser user){
		 Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		 return mapper.qryDepoInfo(params);
	}
	@SuppressWarnings("unchecked")
	public List<StQryInsuSt> qryInsuSt(String param,IUser user){
		 Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		 return mapper.qryInsuSt(params);
	}
	@SuppressWarnings("unchecked")
	public List<StQryCharge> qryCgStIp(String param,IUser user){
		 Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		 return mapper.qryCgStIp(params);
	}
	@SuppressWarnings("unchecked")
	public List<StQryCharge> qryCgStOp(String param,IUser user){
		 Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		 params.put("pkOrg", UserContext.getUser().getPkOrg());
		List<StQryCharge> list = mapper.qryCgStOp(params);
		 return list;
	}
	@SuppressWarnings("unchecked")
	public List<StQryInvDt> qyrInvDt(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		params.put("pkOrg", UserContext.getUser().getPkOrg());

		List<StQryInvDt> invDtList = new ArrayList<>();

		if(!CommonUtils.isEmptyString(CommonUtils.getString(params.get("pkInvoice")))
				|| !CommonUtils.isEmptyString(CommonUtils.getString(params.get("pkSettle")))){
			//查询该发票关联的患者就诊类型
			if(!CommonUtils.isEmptyString(CommonUtils.getString(params.get("pkInvoice")))){
				String euPvtype = mapper.qryPvtypeByPkInvoice(params);
				if(!CommonUtils.isEmptyString(euPvtype)){
					Integer type = Integer.valueOf(euPvtype);
					if(type<3 || type==4){//门急诊
						invDtList = mapper.qyrOpdtByPkInvoice(params);
					}else{//住院
						invDtList = mapper.qyrInvDt(params);
					}
				}
			}else{
				//查询该发票关联的患者就诊类型
				String euPvtype = mapper.qryPvtypeByPkSettle(params);
				if(!CommonUtils.isEmptyString(euPvtype)){
					Integer type = Integer.valueOf(euPvtype);
					if(type<3 || type==4){//门急诊
						invDtList = mapper.qyrOpdtByPkInvoice(params);
					}else{//住院
						invDtList = mapper.qyrIpdtByPkSettle(params);
					}
				}
			}

		}
//		else if(!CommonUtils.isEmptyString(CommonUtils.getString(params.get("pkSettle")))){
//			//查询该发票关联的患者就诊类型
//			String euPvtype = mapper.qryPvtypeByPkSettle(params);
//			if(!CommonUtils.isEmptyString(euPvtype)){
//				Integer type = Integer.valueOf(euPvtype);
//				if(type<3){//门急诊
//					invDtList = mapper.qyrOpdtByPkInvoice(params);
//				}else{//住院
//					invDtList = mapper.qyrIpdtByPkSettle(params);
//				}
//			}
//		}
		
		return invDtList;
	}
	/**
	 * 查询某一患者的结算记录
	 * @param param{pkPv}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> querySettleRecordByPv(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null || CommonUtils.isNull(paramMap.get("pkPv"))){
			throw new BusException("未获取到患者就诊主键！");
		}
		return mapper.querySettleRecordByPv(paramMap);
	}
	
	public void updatePrintState(String param,IUser user) {
		Map<String, String> map = JSONObject.parseObject(param, Map.class);
		if (StringUtils.isBlank(map.get("pkSettle"))) {
			throw new BusException("未获取到结算主键！");
		}
		DataBaseHelper.execute(
				"update bl_settle st set st.cnt_printlist=NVL(st.cnt_printlist,0)+1 where st.PK_SETTLE in (" + map.get("pkSettle") + ")");
	}
	
}