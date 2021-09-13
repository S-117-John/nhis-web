package com.zebone.nhis.ex.oi.dao;


import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ex.oi.ExInfusionOcc;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OinfuStatisticMapper {
	/**
	 * 根据处方查询治疗信息
	 * @param paramMap
	 * @return
	 */
	public List<ExInfusionOcc> getDealInfo(Map<String,Object> paramMap);
}
