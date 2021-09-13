package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.material.vo.MtlPdPlanDtVo;
import com.zebone.nhis.scm.material.vo.MtlPdPlanVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 物资-付款处理Mapper
 * @author chengjia
 *
 */
@Mapper
public interface MtlDeptPdAppMapper {

	/**
	 * 查询科室申请记录
	 * @param param
	 * @return
	 */
	public List<MtlPdPlanVo> queryDeptPdAppList(Map<String,Object> param);
		
	/**
	 * 查询科室领用申请明细
	 * @param param
	 * @return
	 */
	public List<MtlPdPlanDtVo> queryDeptPdAppDtList(Map<String,Object> param);
	
	/**
	 * 查询科室物品消耗明细
	 * @param param
	 * @return
	 */
	public List<MtlPdPlanDtVo> queryPdDtUsedList(Map<String,Object> param);
	
	
	/**
	 * 查询科室历史申请记录
	 * @param param
	 * @return
	 */
	public List<MtlPdPlanVo> queryDeptHistAppList(Map<String,Object> param);
	
	/**
	 * 按条件过滤方式查询物品信息
	 * @param param
	 * @return
	 */
	public List<MtlPdPlanDtVo> queryPdListByConds(Map<String,Object> param);	
	
}
