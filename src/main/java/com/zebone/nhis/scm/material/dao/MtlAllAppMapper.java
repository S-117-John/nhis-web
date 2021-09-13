package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.material.vo.MtlAllAppDtVo;
import com.zebone.nhis.scm.material.vo.MtlAllAppVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MtlAllAppMapper {
	/**
	 * 查询调拨计划
	 * @param map
	 * @return
	 */
	public List<MtlAllAppVo> queryAllocationPlanList(Map<String,Object> map);
	/**
	 * 查询调拨计划明细
	 * @param map
	 * @return
	 */
	public List<MtlAllAppDtVo> queryAllocationPlanDt(Map<String,Object> map);
	
	/**
	 * 查询库存下限物品明细列表
	 * @param map
	 * @return
	 */
	public List<MtlAllAppDtVo> queryStockMinList(Map<String,Object> map);
	
	/**
	 * 查询历史调拨计划
	 * @param map{dateBegin,dateEnd,pkStore}
	 * @return
	 */
	public List<MtlAllAppVo> queryHisAllocationPlan(Map<String,Object> map);
	/**
	 * 查询历史调拨计划明细
	 * @param map
	 * @return
	 */
	public List<MtlAllAppDtVo> queryHisAllocationPlanDt(Map<String,Object> map);
	
	/**
	 * 根据不同条件查询物品
	 * @param map
	 * @return
	 */
	public List<MtlAllAppDtVo> queryPdByCon(Map<String,Object> map);
	
	/**
	 * 根据物品主键，仓库主键获取库存量
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryQuanStk(Map<String,Object> map);


	/**
	 * 根据采购计算物品所需量
	 * @param map
	 * @return
	 */
	public List<MtlAllAppDtVo> queryQuanNeedByPu(Map<String,Object> map);
}
