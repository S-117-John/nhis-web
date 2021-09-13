package com.zebone.nhis.pi.hd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

/***
 * 透析患者
 * @author Administrator
 *
 */
@Mapper
public interface HdPiMapper {
	
	/**
	 * 查询透析患者信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryHdPiByCodePi(Map<String,String> map);
	
	//查询透析治疗患者
	public List<Map<String,Object>> queryHdPi(Map<String,Object> map);
}
