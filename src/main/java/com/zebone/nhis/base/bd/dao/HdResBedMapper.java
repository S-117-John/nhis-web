package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 透析床位注册接口
 * @author leiminjian
 *
 */
@Mapper
public interface HdResBedMapper {
	
	/***
	 * 查询透析床位信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryDialysisBeds(Map<String,String> map);
	
	public List<Map<String,Object>> queryPvEncounterByBedNo(Map<String,String> map);
}
