package com.zebone.nhis.labor.nis.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PvLaborQueryMapper {
	/**
	 * 根据不同条件查询出产房患者记录
	 * @param paramMap{pkOrg,namePi,bedNo,pkDept,pkDeptNs,dateBegin,dateEnd,codePv}
	 * @return
	 */
	public List<Map<String,Object>> queryPvLabor(Map<String,Object> paramMap);
	
	
}
