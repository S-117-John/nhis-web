package com.zebone.nhis.ma.pub.platform.zb.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;
@Mapper
public interface MsgRecBdMapper {
	
	/**
	 * Z06 - 查询药品基本信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryPdInfoList(Map<String,Object> paramMap);

}
