package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface VenousLiquorRuleMapper {
	
	//查询规则列表
	List<Map<String, Object>> qryRule(@Param("pkOrg") String pkOrg);
	
	//查询使用科室列表
	List<Map<String, Object>> qryDept(@Param("pkPivasrule") String pkPivasrule);
	
	//查询当前机构所有护理单元列表
	List<Map<String, Object>> qryDeptNs(@Param("pkOrg") String pkOrg);

}
