package com.zebone.nhis.ex.nis.ns.dao;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.ex.nis.ns.vo.MedApplyVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MedApplyMapper {
	
	/**
	 * 查询申请单列表
	 * @param map{pkPvs,apptype,codeApply}
	 * @return
	 */
	public List<MedApplyVo> queryAppList(Map<String,Object> map);
	/**
	 * 查询申请单已执行列表
	 * @param map
	 * @return
	 */
	public String queryAppExList(Map<String,Object> map);
	/**
	 * 查询待退费的VO
	 * @param map{pkCnords}
	 * @return
	 */
	public List<RefundVo> queryAppBackCgList(Map<String,Object> map);
	
	/**
	 * 退费时更新检验申请单状态【eu_status '3' => eu_status】
	 * @param codeApplys
	 */
	public int updateLabApplyToBackCg(Map<String,Object> map);
	
	/**
	 * 退费时更新执行单状态【eu_status '1' => eu_status】
	 * @param pkCnords
	 */
	public int updateExStatus(Map<String,Object> map);
	
	/**
	 * 退费时更新医嘱的执行人相关信息【date_plan_ex,name_emp_ex,pk_emp_ex => null】
	 * @param pkCnords
	 */
	public int updateOrdExInfo(Map<String,Object> map);
	
	/**
	 * 取消执行【date_canc,name_emp_canc,pk_emp_canc,pk_dept_canc,flag_canc】
	 * @param pkCnords
	 */
	public int cancExStatus(Map<String,Object> map);
	/*
	 * 查询产房的医技申请列表
	 */
	public List<MedApplyVo> queryLabAppList(Map<String, Object> map);
}
