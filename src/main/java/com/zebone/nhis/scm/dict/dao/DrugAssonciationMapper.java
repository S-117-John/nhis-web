package com.zebone.nhis.scm.dict.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;


@Mapper
public interface DrugAssonciationMapper {
	
	
	List<Map<String,Object>> queryByPkPd(String pkPd);
	
	int delByPkPdrel(String PkPdrel);
}
