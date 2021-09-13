package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PrnPresToxicHempMapper {

	public List<Map<String, Object>> getPresDt(Map<String, Object> qryParam);

	public List<Map<String, Object>> getPresSummary(List<String> pkPres);

}
