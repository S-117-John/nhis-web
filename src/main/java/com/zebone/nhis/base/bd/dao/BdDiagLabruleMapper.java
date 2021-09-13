package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.Mapper;


@Mapper
public interface BdDiagLabruleMapper {
	
	/**
	 * 查询产科诊断规则列表
	 * @param value
	 * @return
	 */
	public List<Map<String, Object>> getBdDiagLabruleList(@Param("value") String value);

}
