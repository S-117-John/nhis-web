package com.zebone.nhis.task.bl.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BlSyxTaskMapper {
	
	/**
	 * 查询患者信息集合
	 * @param codePiList
	 * @return
	 */
	public List<Map<String,Object>> qryPvList(@Param("pkList") List<String> codePiList);
	
	/**
	 * 根据执行科室id，住院科室ID获取科室信息
	 * @param pkList
	 * @return
	 */
	public List<Map<String,Object>> qryDeptList(@Param("pkList") List<String> pkList);
	
	/**
	 * 根据收费项目编码查询收费项目信息
	 * @param codeList
	 * @return
	 */
	public List<Map<String,Object>> qryItemList(@Param("pkList") List<String> codeList);
	
	/**
	 * 根据人员编码查询人员信息集合
	 * @param codeList
	 * @return
	 */
	public List<Map<String,Object>> qryEmpList(@Param("pkList") List<String> codeList);
	
	
	
	
}
