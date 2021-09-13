package com.zebone.nhis.pro.zsba.compay.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.pub.vo.SettleRecord;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ZsBlIpSettleQryMapper {
	
	List<SettleRecord> qryStRecords(Map<String, Object> mapParam) ;
	
	/**
	 * 门诊和住院的结算查询功能分开
	 * 住院结算查询，查找所有
	 * @param params
	 * @return
	 */
	List<SettleRecord> qryIpStRecordAll(Map<String, Object> params);
	
	List<SettleRecord> qryStRecordAll(Map<String, Object> params);
	
	/**
	 * 查询患者预缴金
	 * @param paraMap
	 * @return
	 */
	public List<Map<String,Object>> queryChargePrePay(Map<String, Object> paraMap);
}
