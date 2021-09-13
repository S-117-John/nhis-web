package com.zebone.nhis.nd.temp.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.nd.temp.vo.NdDyncRangeVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface NdDyncMapper {
	/**
	 * 查询动态列
	 */
	public List<NdDyncRangeVo> queryRange(Map<String,Object> paramMap);
	
	
	
}
