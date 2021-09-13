package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.base.bd.vo.BdOuDeptVo;
import com.zebone.nhis.common.module.base.bd.res.BdDefdept;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface DefdeptManageMapper {
	
	/**
	 * {dtDefdepttype:自定义科室类型,pkOrrg:当前机构}
	 * @param map
	 * @return
	 */
	public List<BdOuDeptVo> qryBdOuDept(Map<String, String> map);
	/**
	 * 
	 * @param dtDefdepttype:自定义科室类型
	 * @return
	 */
	
	public List<BdDefdept> qryDefdept(String dtDefdepttype);
	
	/**
	 * {"pkDefdept":自定义科室主键}
	 * @param pkDefdept
	 */
	public void delDefdept(String pkDefdept);
	
	/**
	 * {"pkDefdept":自定义科室主键}
	 * @param pkDefdept
	 */
	public void delDefdeptMap(String pkDefdept);
	
	public Integer qryCount(String pkDefdept);
	
	public Integer qryCountCode(Map map);
	
	public Integer qryCountName(Map map);
}
