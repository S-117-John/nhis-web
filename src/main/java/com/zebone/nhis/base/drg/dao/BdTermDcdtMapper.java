package com.zebone.nhis.base.drg.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;
@Mapper
public interface BdTermDcdtMapper {
    
	public List<Map<String, Object>> queryTermDcdtList(Map<String, Object> paramMap);
}
