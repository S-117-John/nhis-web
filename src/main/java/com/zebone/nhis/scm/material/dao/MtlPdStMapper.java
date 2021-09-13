package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.pub.vo.PdPlanDtVo;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MtlPdStMapper {
	
	/**
	 * 采购入库 - 查询采购订单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPuOrdList(Map<String,Object> map);
	/**
	 * 采购入库 - 查询采购订单对应的明细
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPuOrdDtList(Map<String,Object> map);
	
	/**
     * 查询待入库单 - 库单pd_st
     * @param map
     * @return
     */
	public List<Map<String,Object>>  queryToInPdStList(Map<String,Object> map);
	
	/**
     * 查询待出库 - 申请单pd_plan
     * @param map
     * @return
     */
	public List<Map<String,Object>>  queryToOutPdStList(Map<String,Object> map);
	
	/**
	 * 查询待出库明细
	 * @param map
	 * @return
	 */
	public List<PdPlanDtVo> queryToOutPdStDtList(Map<String,Object> map);
	
	/**
     * 查询入库单
     * @param map
     * @return
     */
	public List<Map<String,Object>>  queryPdStByCon(Map<String,Object> map);
	
    /**
     * 查询入库单明细
     * @param map
     * @return
     */
	public List<PdStDtVo> queryPdStDtByPk(Map<String,Object> map);
	
	/**
     * 查询出库单明细
     * @param map
     * @return
     */
	public List<Map<String, Object>> queryPdStDtOutList(Map<String, Object> map);

	/**
     * 库存管理 - 查询库存管理
     * @param map
     * @return
     */
	public List<Map<String, Object>> queryPdStInfoList(Map<String, Object> map);
	
	/**
     * 库存管理 - 查询可用批次
     * @param map
     * @return
     */
	public List<Map<String, Object>> queryPdBatchList(Map<String, Object> map);
	
	/**
     * 库存管理 - 查询调价历史
     * @param map
     * @return
     */
	public List<Map<String, Object>> queryHisRePriceList(Map<String, Object> map);
	
	/**
     * 库存管理 - 查询单品
     * @param map
     * @return
     */
	public List<Map<String, Object>> queryPdSingleList(Map<String, Object> map);

	/**
	 * 根据条码扫描物品信息
	 * @param map
	 * @return
	 */
	public List<PdStDtVo> qryPkStoreByPkdept(Map<String,Object> map);
	
}
