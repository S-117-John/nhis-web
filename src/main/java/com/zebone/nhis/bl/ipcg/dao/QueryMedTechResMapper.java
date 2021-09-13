package com.zebone.nhis.bl.ipcg.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.ipcg.vo.ApptOrdVo;
import com.zebone.platform.modules.mybatis.Mapper;



@Mapper
public interface QueryMedTechResMapper {
	/**
	 * 医技预约查询
	 */
	public List<Map<String,Object>> queryMedOrd(ApptOrdVo aov);
	
	/**
	 * 打印预约单
	 */
}
