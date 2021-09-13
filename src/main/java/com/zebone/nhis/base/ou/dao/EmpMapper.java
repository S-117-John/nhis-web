package com.zebone.nhis.base.ou.dao;

import java.util.List;
import java.util.Map;


import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface EmpMapper {

	public List<Map> getEmpInfos(Map paramMap);
	
	public List<Map> getDeptEmpInfos(Map paramMap);

}
