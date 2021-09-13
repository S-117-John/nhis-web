package com.zebone.nhis.pro.sd.bl.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SelfPayTrInsuMapper {
	
	/**查询患者门诊挂号结算信息*/
	public List<Map<String,Object>> qryOpRegStInfo(Map<String,Object> paramMap);
	
	/**根据结算主键查询关联的费用明细*/
	public List<BlOpDt> qryDtListByPkSettle(@Param("pkList") List<String> pkList);
	
	/**根据结算主键查询处方执行信息*/
	public Set<String> qryPresOccByPkSettle(Map<String,Object> paramMap);
	
	public Set<String> qryAssistOccByPkSettle(Map<String,Object> paramMap);
	
	/**查询处方执行明细关联的pkSettle信息*/
	public List<String> qryPresOccPkSettle(Map<String,Object> paramMap);
	
	/**查询医技执行明细关联的pkSettle信息*/
	public List<String> qryAssOccPkSettle(Map<String,Object> paramMap);
	
	/**根据结算主键查询结算信息*/
	public List<BlSettle> qryStInfoByPkSettle(@Param("pkList") List<String> pkList);
	
	/**查询结算下的处方*/
	List<Map<String, Object>> querySettlePressRecord(Map<String, String> mapParam);
	
	/**查询结算下的检查检验*/
	List<Map<String, Object>> querySettleAssistRecord(Map<String, String> mapParam);
	
	/**查询医嘱附加费用信息*/
	public List<Map<String, Object>> qryEtceterasList(Map<String,String> paramMap);

	/**查询处方执行记录*/
	public Integer qryPresOccCnt(Map<String,Object> paramMap);

	/**查询医技执行记录*/
	public Integer qryAssOccCnt(Map<String,Object> paramMap);
	
	/**
	 * 更新预约信息
	 * @param paramMap
	 */
	public void updateSchAppt(Map<String,Object> paramMap);
	
	/**
	 * 更新就诊信息
	 * @param paramMap
	 */
	public void updatePvEncounter(Map<String,Object> paramMap);
	
}
