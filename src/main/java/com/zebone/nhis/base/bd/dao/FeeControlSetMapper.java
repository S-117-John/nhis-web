package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface FeeControlSetMapper {

	//组查询
	List<Map<String, Object>> qryGroup();

	//明细查询
	List<Map<String, Object>> qryDetail(String sortNo);

	
}
