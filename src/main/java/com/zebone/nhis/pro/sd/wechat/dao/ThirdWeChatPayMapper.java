package com.zebone.nhis.pro.sd.wechat.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ThirdWeChatPayMapper {
	/**
	 * 根据pkPi 通过pkPi查询就诊记录
	 * @param pkPi
	 * @return
	 */
	public List<Map<String, Object>> getPatientPvList(String pkPi);
	
	/**
	 * 根据pkPv 根据pkPv查询结算记录
	 * @param pkPv
	 * @return
	 */
	public List<Map<String,Object>> getPvSettleInfo(String pkPv);
	
	/**
	 * 获取HIS结算信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryHisSettle(Map<String,Object> paramMap);
	
}
