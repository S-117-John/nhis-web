package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;
@Mapper
public interface BdTempPrtMapper {
    
	/**
	 * 查询自定义列表
	 * @param bdTempPrtDept
	 * @return
	 */
	public List<Map<String, Object>> queryTempPrtList(Map<String,Object> paramMap);
}
