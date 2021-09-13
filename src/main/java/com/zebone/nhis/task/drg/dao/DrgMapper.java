package com.zebone.nhis.task.drg.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

/**
 * DRG信息平台
 * @author dell
 */
@Mapper
public interface DrgMapper {
	/**
	 * 查询所有出院已完成病案首页，未上传DRG平台的患者
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryPvEncounterList(Map<String, Object> paramMap);
}
