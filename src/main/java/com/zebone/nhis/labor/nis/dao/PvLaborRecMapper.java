package com.zebone.nhis.labor.nis.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.labor.nis.PvLaborRecDt;
import com.zebone.nhis.ex.pub.vo.PvInfantVo;
import com.zebone.nhis.labor.nis.vo.PvLaborRecVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PvLaborRecMapper {

	/**
	 * 查询分娩记录
	 * @param paramMap{pkOrg,pkPv,pkPvlaborrec}
	 * @return
	 */
	public List<PvLaborRecVo> queryPvLaborRec(Map<String,Object> paramMap);
	/**
	 * 查询分娩记录
	 * @param paramMap{pkPvlaborrec,pkLaborrecdt}
	 * @return
	 */
	public List<PvLaborRecDt> queryPvLaborRecDt(Map<String,Object> paramMap);
	/**
	 * 查询婴儿信息
	 * @param paramMap{pkPvlaborrec,pkLaborrecdt}
	 * @return
	 */
	public List<PvInfantVo> queryPvInfant(Map<String,Object> paramMap);
	
}
