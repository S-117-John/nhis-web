package com.zebone.nhis.scm.st.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.st.vo.AllocationAppDtVo;
import com.zebone.nhis.scm.st.vo.AllocationAppVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface AllocationAppMapper {
	/**
	 * 查询调拨计划
	 * @param map
	 * @return
	 */
	public List<AllocationAppVo> queryAllocationPlanList(Map<String,Object> map);
	/**
	 * 查询调拨计划明细
	 * @param map
	 * @return
	 */
	public List<AllocationAppDtVo> queryAllocationPlanDt(Map<String,Object> map);
	
	/**
	 * 查询库存下限物品明细列表
	 * @param map
	 * @return
	 */
	public List<AllocationAppDtVo> queryStockMinList(Map<String,Object> map);
	
	/**
	 * 查询历史调拨计划
	 * @param map{dateBegin,dateEnd,pkStore}
	 * @return
	 */
	public List<AllocationAppVo> queryHisAllocationPlan(Map<String,Object> map);
	/**
	 * 查询历史调拨计划明细
	 * @param map
	 * @return
	 */
	public List<AllocationAppDtVo> queryHisAllocationPlanDt(Map<String,Object> map);
	
	/**
	 * 根据不同条件查询物品
	 * @param map
	 * @return
	 */
	public List<AllocationAppDtVo> queryPdByCon(Map<String,Object> map);
	/**
	 * 根据物品主键，仓库主键获取库存量
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryQuanStk(Map<String,Object> map);
	
	/**
	 * 按消耗计算
	 */
	public List<AllocationAppDtVo> qryByConsumer(Map<String,Object> map);
	
	/**
	 * 按入庫計算
	 */
	public List<AllocationAppDtVo> qryByInstore(Map<String,Object> map);
	/**
	 * 获取基数药品科室消耗记录
	 * @param map
	 * @return
	 */
	public List<AllocationAppDtVo> queryDeptAppByCg(Map<String,Object> map);
	/**
	 * 获取基数药品字典
	 * @param map
	 * @return
	 */
	public List<AllocationAppDtVo> queryDeptPdBase(Map<String,Object> map);
	
	/**
	 * 查询本仓库某药品的库存量
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryStoreQuanStk(Map<String,Object> paramMap);

	public List<AllocationAppDtVo> qryByConsumer2(Map<String, Object> map);
}
