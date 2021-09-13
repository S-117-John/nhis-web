package com.zebone.nhis.ma.pub.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MrdSystemMapper {
	
	public List<Map<String,Object>> qrySettlements(Map<String,Object> paramMap);

}
