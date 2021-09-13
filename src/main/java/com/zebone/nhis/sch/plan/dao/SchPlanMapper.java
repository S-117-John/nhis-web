package com.zebone.nhis.sch.plan.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.sch.plan.SchSchstop;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SchPlanMapper {
	
	/**
	 * 查询排班计划
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getPlanList(Map<String,Object> paramMap);
	/**
	 * 查询排班计划及周计划信息
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> queryPlanAndWeek(Map<String,String> paramMap);
	
	/**
	 * 查询计划停诊的记录
	 * @param paramMap
	 * @return
	 */
	public List<SchSchstop> getSchSchstop(Map<String, Object> paramMap);

}
