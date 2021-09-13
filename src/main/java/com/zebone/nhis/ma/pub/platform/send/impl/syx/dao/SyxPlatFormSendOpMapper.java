package com.zebone.nhis.ma.pub.platform.send.impl.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ma.pub.platform.syx.vo.OpCall;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SyxPlatFormSendOpMapper {
	
	public Map<String, Object> qryPvAll(String pkPv);
	
	public String qryPkPv(String pkPi);

	/**
	 * 查询排班计划信息
	 * @param pkSchList
	 * @return
	 */
	public List<Map<String, Object>> qrySchSchAll(List<String> pkSchList);

	/**
	 * 查询科室相关信息
	 * @param pkDept
	 * @return
	 */
	public List<Map<String,Object>> qryDeptAll(String pkDept);
	/** 
	 * 
	 * 查询医生职务
	 * @param jobName
	 * @return
	 */
	public List<Map<String,Object>> qryDefDocAll(String jobName);

	/**
	 * 查询患者队列
	 * @param paramMap
	 * @return
	 */
	public List<OpCall> qryQueueList(Map<String, Object> paramMap);
	
}
