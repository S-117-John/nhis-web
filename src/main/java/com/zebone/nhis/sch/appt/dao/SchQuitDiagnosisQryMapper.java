package com.zebone.nhis.sch.appt.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SchQuitDiagnosisQryMapper {
	
	public List<Map<String, Object>> getSchAppt(Map params);

}
