package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;


@Mapper
public interface DeptFeeSetMapper {
	
    /**
     * 根据机构查询临床科室列表
     * @param paramMap
     * @return
     */
	public List<Map<String,Object>> findDeptByPkOrg(Map<String,Object> paramMap);
	/**
	 * 根据机构查询具有多属性的临床科室列表
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> findDeptAndTypeByPkOrg(Map<String,Object> paramMap);
}
