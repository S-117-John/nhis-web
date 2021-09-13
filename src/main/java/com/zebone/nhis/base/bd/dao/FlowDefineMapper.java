package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface FlowDefineMapper {

	//审批流业务列表查询
	List<Map<String, Object>> qryFlow();
	
	//业务步骤查询
	List<Map<String, Object>> qryFlowStep(Map<String, Object> map);

}
