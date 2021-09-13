package com.zebone.nhis.ma.pub.platform.zb.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MsgRecBlMapper {
	/**
	 * 根据就诊主键查询，诊断信息，过敏信息，分娩记录
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryZMRDataByPkPv(Map<String,Object> paramMap);
	
	/**
	 * 根据医嘱查询门诊收费信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryOpBlInfo(Map<String,Object> paramMap);
	
	/**
	 * 根据医嘱查询住院收费信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryIpBlInfo(Map<String,Object> paramMap);
}
