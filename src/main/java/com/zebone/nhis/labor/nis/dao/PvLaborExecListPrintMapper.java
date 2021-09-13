package com.zebone.nhis.labor.nis.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 执行单打印查询
 * @author yangxue
 *
 */
@Mapper
public interface PvLaborExecListPrintMapper {
	/**
	 * 查询变动医嘱
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryChangedOrderList(Map<String,Object> map);
	/**
	 * 查询护理类执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryNsExlistList(Map<String,Object> map);
	/**
	 * 查询口服类执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryKFExlistList(Map<String,Object> map);
	/**
	 * 查询注射类执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryZSExlistList(Map<String,Object> map);
	/**
	 * 查询输液类执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> querySYExlistList(Map<String,Object> map);
	/**
	 * 查询饮食类执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryYSExlistList(Map<String,Object> map);
	/**
	 * 查询治疗类执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryZLExlistList(Map<String,Object> map);
	/**
	 * 查询其他执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryQTExlistList(Map<String,Object> map);
}
