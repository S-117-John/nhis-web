package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.material.vo.MtlPdPlanDtInfo;
import com.zebone.nhis.scm.material.vo.MtlPdPlanInfo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MtlPdPlanMapper {

	/**
	 * 查询科室请领记录
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryDeptAppList(Map<String,Object> map);
	
	/**
	 * 查询需求汇总
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> querySumNeedList(Map<String,Object> map);
	
	/**
	 * 查询物品在途数量
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPdOnUsedQuan(Map<String,Object> map);
	
	/**
	 * 查询物品在途明细
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPdOnUsedList(Map<String,Object> map);
	
	/**
	 * 按消耗计算-需要的物品
	 * @param map
	 * @return 
	 */
	public List<Map<String,Object>> queryNeedsByUsed(Map<String,Object> map);
	
	/**
	 * 按采购计算-需要的物品
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryNeedsByPu(Map<String,Object> map);
	
	/**
	 * 查询采购计划列表
	 * @param map
	 * @return
	 */
	public List<MtlPdPlanInfo> queryPdPlanList(Map<String,Object> map);
	
	/**
	 * 查询采购计划明细
	 * @param map
	 * @return
	 */
	public List<MtlPdPlanDtInfo> queryPdPlanDtList(Map<String,Object> map);

	/**
	 * 查新历史采购计划
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryHisPuPlanList(Map<String,Object> map);
	
	/**
	 * 查新历史采购计划 - 明细
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryHisPuPlanDtList(Map<String,Object> map);
	
	/**
	 * 查新历史采购订单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryHisPuOrdList(Map<String,Object> map);
	
	/**
	 * 查新历史采购订单 - 明细
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryHisPuOrdDtList(Map<String,Object> map);
	
	/**
	 * 按条件查询需要采购的物品
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPdPlanByCon(Map<String,Object> map);

	/**
	 * 库存已达下限的物品列表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPdPlanByMin(Map<String,Object> map);
	
	/**
	 * 查询有效物品
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryValidPdList(Map<String,Object> map);
	
	/**
	 * 查询有效的供应商
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryValidSupList(Map<String,Object> map);
}
