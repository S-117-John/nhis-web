package com.zebone.nhis.scm.ipdedrug.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface DrugAndDiagMapper {

	List<Map<String,Object>> qryByPatient(Map<String,Object> map);
	
	List<Map<String,Object>> qryByHospital(Map<String,Object> map);
}
