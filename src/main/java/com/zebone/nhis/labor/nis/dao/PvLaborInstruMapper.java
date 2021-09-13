package com.zebone.nhis.labor.nis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.labor.nis.PvLaborInstruDt;
import com.zebone.nhis.labor.nis.vo.PvLaborInstruVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PvLaborInstruMapper {
	/**
	 * 查询器械清点单
	 * @param paramMap{pkOrg,pkPv,pkLaborrec}
	 * @return
	 */
	public PvLaborInstruVo queryPvLaborInstru(Map<String,Object> paramMap);
	/**
	 * 查询器械清点单明细
	 * @param pkInstru
	 * @return
	 */
	public List<PvLaborInstruDt> queryPvLaborInstruDt(@Param("pkInstru")String pkInstru);
	
	
}
