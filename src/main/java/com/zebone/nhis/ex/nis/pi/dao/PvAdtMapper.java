package com.zebone.nhis.ex.nis.pi.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PvAdtMapper {
    /**
     * 查询待入科患者列表
     * @param paramMap
     * @return
     */
	public List<Map<String,Object>> getPatiByIn(Map<String,Object> paramMap);
	
	/**
	 * 查询待出院患者列表
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getPatiByOut(Map<String,Object> paramMap);
	/**
	 * 查询取消出院患者列表
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryCancelPatiOut(Map<String,Object> paramMap);
	/**
	 * 查询取消出院转科患者列表
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryCancelPatiDeptOut(Map<String,Object> paramMap);
	
	/***
	 * 查询患者是否为转科患者
	 * @return
	 */
	public Integer countPvAdt(String pkPv);
	
	/***
	 * 检验当前选中科室是否可以转科不停嘱
	 * @return
	 */
	public Integer chkNoStopOrdByChg(Map<String, Object> paramMap);

	/**
	 * 查询待出院患者列表 （新增明日出院  ： 1102-出院 ； 110201-明日出院）
	 * @param paramMap
	 */
	public List<Map<String,Object>> getPatiByStayOut(Map<String, Object> paramMap);

	/**
	 * 查询待转科患者信息列表
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getWaitDeptChange(Map<String, Object> paramMap);
}
