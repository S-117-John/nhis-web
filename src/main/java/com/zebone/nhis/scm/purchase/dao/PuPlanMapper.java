package com.zebone.nhis.scm.purchase.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.purchase.vo.PdStoreParam;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PuPlanMapper {
	
	/** 查询物品 */
	List<PdStoreParam> getPdStoreVoList(PdStoreParam vo);

	/**
	 * 查询采购计划单列表
	 * @param paramMap{
						"pkStore":"仓库主键(必填)",
						"codePlan":"单据号码",
						"datePlanBegin":"计划日期-开始",
						"datePlanEnd":"计划日期-结束",
						"pkEmpMak":"制单人(不是系统登录人)",
						"euStatus":"单据状态"
						}
	 * @return
	 */
	List<Map<String,Object>> qryPlanList(Map<String,Object> paramMap);
	
	/***
	 * 查询历史采购计划
	 * @param paramMap
	 * @return
	 */
	List<Map<String,Object>> qryHistoryPlanList(Map<String,Object> paramMap);
	
	/**
	 * 查询历史采购订单
	 * @param paramMap
	 * @return
	 */
	List<Map<String,Object>> qryHistoryOrdList(Map<String,Object> paramMap);
	
	/**
	 * 按消耗計算
	 */
	public List<PdStoreParam> qryByConsumer(Map<String,Object> map);
	
	/**
	 * 按消耗計算
	 */
	public List<PdStoreParam> qryByConsumer3(Map<String,Object> map);
	
	/**
	 * 按採購計算
	 */
	public List<PdStoreParam> qryByPurchase(Map<String,Object> map);
	
	/**
	 * 查询历史采购计划明细
	 * @param paramMap{"pkPdplan":"采购计划主键"}
	 * @return
	 */
	public List<Map<String,Object>> qryHistoryPlanDts(Map<String,Object> paramMap);
	
	/**
	 * 查询历史采购订单明细
	 * @param paramMap{"pkPdpu":"采购订单主键"}
	 * @return
	 */
	public List<Map<String,Object>> qryHistoryOrdDts(Map<String,Object> paramMap);

	/**
	 * 根据消耗公式2计算
	 * @param map
	 * @return
	 */
	public List<PdStoreParam>  qryByConsumer2(Map<String, Object> map);
}
