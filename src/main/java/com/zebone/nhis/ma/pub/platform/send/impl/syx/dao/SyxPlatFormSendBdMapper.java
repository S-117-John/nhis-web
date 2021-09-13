package com.zebone.nhis.ma.pub.platform.send.impl.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SyxPlatFormSendBdMapper {
	Map<String, Object> getDdDefdocList(String codeDefdoclist);
	
	/**
	 * 查询科室相关信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryDeptInfo(String pkDept);
	
	/**
	 * 查询科室关联人员相关信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryEmpInfoByDept(String pkEmp);
	
	
	 public Map<String,Object> getDdDefdocListByPk(String pkDefdoclist);
	
	 //根据主键集合返回BdDefdoc对象
	 public	List<BdDefdoc> selectDefdocsByList(List<String> list);
	
}
