package com.zebone.nhis.base.ou.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InternEmpMapper {

	public List<Map> seleInternEmpL(Map paraMap);
}
