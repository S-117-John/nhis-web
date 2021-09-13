package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PathOrderMapper {

	//查询路径医嘱列表
	List<Map<String, Object>> qryPathOrder();
	
	//查询路径医嘱明细列表
	List<Map<String, Object>> qryPathOrderDetail(@Param("pkCptask")String pkCptask);

	
}
