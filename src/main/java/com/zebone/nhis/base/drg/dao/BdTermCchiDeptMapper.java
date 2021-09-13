package com.zebone.nhis.base.drg.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;
@Mapper
public interface BdTermCchiDeptMapper {
    
	public List<Map<String, Object>> queryTermCchiDeptList(Map<String, Object> paramMap);
}
