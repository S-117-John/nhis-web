package com.zebone.nhis.ma.pub.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface JhEmrMapper {
	public List<Map<String,Object>> qryJhEmrList(Map<String,Object> paramMap);
}
