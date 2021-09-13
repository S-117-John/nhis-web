package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 在用管理 - 查询mapper
 * @author wj
 */
@Mapper
public interface MtlInUsedMapper {
	/**
	 * 查询物品在用记录
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPdUsedList(Map<String,Object> map);
	
}
