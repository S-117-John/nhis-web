package com.zebone.nhis.base.drg.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

/**
 * DRG信息平台
 * @author dell
 */
@Mapper
public interface DrgPlatFormMapper {
	/**
	 * 查询患者就诊信息列表
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryPvEncounterList(Map<String, Object> paramMap);
}
