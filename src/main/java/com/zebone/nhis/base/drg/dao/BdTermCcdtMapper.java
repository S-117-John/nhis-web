package com.zebone.nhis.base.drg.dao;

import java.util.List;
import java.util.Map;




import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BdTermCcdtMapper {
	public List<Map<String, Object>> queryTremCcdtList(Map<String, Object> paramMap);

}
