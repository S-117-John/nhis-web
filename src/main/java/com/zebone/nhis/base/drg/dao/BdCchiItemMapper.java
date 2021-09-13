package com.zebone.nhis.base.drg.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BdCchiItemMapper {
	/**
	 * 查询关联列表
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryCchiItemList(Map<String, Object> paramMap);
}
