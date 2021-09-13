package com.zebone.nhis.ma.pub.platform.send.impl.sd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 基础数据相关查询
 * @author JesusM
 *
 */
@Mapper
public interface SDBdSendMapper {
	/**
	 * 查询科室和病区关系（业务线）
	 * @param paramMap
	 */
	List<Map<String,Object>> queryBdDeptBus(Map<String,Object> paramMap);
	
	/**
	 * 查询收费项目数据
	 * @param paramMap
	 */
	List<Map<String,Object>> queryBdItem(Map<String,Object> paramMap);
	
	/**
	 * 查询收费项目总价格
	 * @param paramMap
	 * @return
	 */
	List<Map<String,Object>> queryBdItemPrice(Map<String,Object> paramMap);
	
	/**
	 * 查询医嘱项目数据
	 * @param paramMap
	 */
	Map<String,Object> queryBdOrd(Map<String,Object> paramMap);
	
	
	
}
