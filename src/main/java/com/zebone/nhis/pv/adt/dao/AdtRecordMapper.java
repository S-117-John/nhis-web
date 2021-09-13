package com.zebone.nhis.pv.adt.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface AdtRecordMapper {

	public List<Map<String,Object>> qryPvMessage(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> qryPvAdtRecord(String pkPv);
}
