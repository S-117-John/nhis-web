package com.zebone.nhis.ex.nis.ns.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.nis.ns.vo.OrdListVo;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 执行单打印查询
 * @author yangxue
 *
 */
@Mapper
public interface ExecListPrintMapper {
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
	
	/**
	 * 查询检验项目申请单
	 * @param map	
	 * @return
	 */
	public List<Map<String,Object>> queryJCExlistList(Map<String,Object> map);
	
	
	/**
	 * 查询检验项目申请单
	 * @param map	
	 * @return
	 */
	public List<Map<String,Object>> queryJYExlistList(Map<String,Object> map);
	
	/**
	 * 皮试执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPSExlistList(Map<String,Object> map);
	
	/**
	 * 自定义注射执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryZSZExlistList(Map<String,Object> map);
	
	/**
	 * 自定义临时药品执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryLSYPExlistList(Map<String,Object> map);
	
	/**
	 * 雾化执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryWHExlistList(Map<String,Object> map);
	
	/**
	 * 长期药品执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryCQYPExlistList(Map<String,Object> map);
	
	/**
	 * 汇总执行单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryHZExlistList(Map<String,Object> map);
	
	public void updateOrdList(OrdListVo vo);

	public Map<String, Object> queryPrintTemplate(Map<String, Object> map);
}
