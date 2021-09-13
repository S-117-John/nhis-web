package com.zebone.nhis.ex.nis.ns.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.ex.nis.ns.vo.MedicineVo;
import com.zebone.nhis.ex.nis.ns.vo.OrderOccCgVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface DrugRetreatMapper2 {
	
	/**
	 * 查询待生成退药请领的执行单--[中山二院需求]
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryOccListBySyx(Map<String,Object> map);
	/**
	 * 查询执行单对应记费及发药记录--[中山二院需求]
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryCgListByOcc(Map<String,Object> map);
	
	/**
	 * 查询执行单对应记费及发药记录--[中山二院需求]
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryCgListByOccSqlServer(Map<String,Object> map);
	
	/**
	 * 根据计费主键查询记费信息
	 * @param map
	 * @return
	 */
	public List<OrderOccCgVo> queryCgListByPk(Map<String,Object> map);
	
	/**
	 * 查询待请退药品对应的批次--[中山二院需求]
	 * @param map
	 * @return
	 */
	public List<MedicineVo> queryMedicineListBySyx(Map<String,Object> map);
	
	/**
	 * 查询待生成退药请领的执行单--[中山二院需求]
	 * @param map
	 * @return
	 */
	public List<ExPdApplyDetail> querySumCntByOrd(Map<String,Object> map);
	
	/**
	 * 查询待生成退药请领的执行单--[中山二院需求]
	 * @param map
	 * @return
	 */
	public List<ExPdApplyDetail> querySumCntByOrdInOrcl(Map<String,Object> map);

	/**
	 * 查询待生成退药请领的药品发放批次--[中山二院需求]
	 * @param map
	 * @return
	 */
	public List<ExPdApplyDetail> queryPdBatchByOrd(Map<String,Object> map);

	/**
	 * 查询执行单对应记费及发药记录--[SqlServer数据库：安批取整：取消的申请单数量合计后进行向上取整；非安批取整：取消的申请单数量先向上取整再合计。]
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryCgListByOccForSqlServer(Map<String, Object> map);

	/**
	 * 查询执行单对应记费及发药记录--[Orcale数据库：安批取整：取消的申请单数量合计后进行向上取整；非安批取整：取消的申请单数量先向上取整再合计。]
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryCgListByOccForOrcl(Map<String, Object> map);
}
