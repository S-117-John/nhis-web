package com.zebone.nhis.base.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BdDumpMapper {
	
	
	/**
	 * 批量转存排班预约-排班————门诊
	 * @param pkPvList
	 * @return
	 */
	public int batInsertSchSchByDate(String endDate);
	/**
	 * 查询患者信息列表
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> queryPiMaster(Map<String,Object> param);
	
	/**
	 * 查询转存表可以恢复的患者信息
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> queryPiMasterByDump(Map<String,Object> param);
	
	/**
	 * 根据pkpv批量转存医嘱
	 * @param pkPvList
	 * @return
	 */
	public int batInsertCnOrderByPkPv(List<String> list);
	
	/**
	 * 根据pkpv批量转存检查申请
	 * @param pkPvList
	 * @return
	 */
	public int batInsertRisApplyByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量转存检验申请
	 * @param pkPvList
	 * @return
	 */
	public int batInsertLabApplyByPkPv(List<String> pkPvList);
	/**
	 * 根据pkpv批量转存医嘱-处方
	 * @param pkPvList
	 * @return
	 */
	public int batInsertPrescriptionByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量转存医嘱-打印
	 * @param pkPvList
	 * @return
	 */
	public int batInsertOrderPrintByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量转存医嘱-输血申请
	 * @param pkPvList
	 * @return
	 */
	public int batInsertTransApplyByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量转存医嘱-会诊申请
	 * @param pkPvList
	 * @return
	 */
	public int batInsertConsultApplyByPkPv(List<String> pkPvList);
	/**
	 * 根据pkpv批量转存医嘱-会诊应答
	 * @param pkPvList
	 * @return
	 */
	public int batInsertConsultResponseByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量转存医嘱-手术申请
	 * @param pkPvList
	 * @return
	 */
	public int batInsertOpApplyByPkPv(List<String> pkPvList);
	/**
	 * 根据pkpv批量转存执行单
	 * @param pkPvList
	 * @return
	 */
	public int batInsertOrderOccByPkPv(List<String> pkPvList);
	/**
	 * 根据pkpv批量转存执行单打印
	 * @param pkPvList
	 * @return
	 */
	public int batInsertOrderOccPrtByPkPv(List<String> pkPvList);
	/**
	 * 根据pkpv批量转存领药申请
	 * @param pkPvList
	 * @return
	 */
	public int batInsertPdApplyByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量转存发药记录
	 * @param pkPvList
	 * @return
	 */
	public int batInsertPdDeByPkPv(List<String> pkPvList);
	/**
	 * 根据pkpv批量转存记费明细
	 * @param pkPvList
	 * @return
	 */
	public int batInsertIpDtByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量转存记费明细--门诊
	 * @param pkPvList
	 * @return
	 */
	public int batInsertOpDtByPkPv(List<String> pkPvList);
	
	
	
	
	
	/**
	 * 根据pkpv批量删除已转存检查申请
	 * @param pkPvList
	 * @return
	 */
	public int batDelRisApplyByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已转存检验申请
	 * @param pkPvList
	 * @return
	 */
	public int batDelLabApplyByPkPv(List<String> pkPvList);
	/**
	 * 根据pkpv批量删除已转存医嘱-处方
	 * @param pkPvList
	 * @return
	 */
	public int batDelPrescriptionByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已转存医嘱-打印
	 * @param pkPvList
	 * @return
	 */
	public int batDelOrderPrintByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已转存医嘱-输血申请
	 * @param pkPvList
	 * @return
	 */
	public int batDelTransApplyByPkPv(List<String> pkPvList);
	/**
	 * 根据pkpv批量删除已转存医嘱-会诊应答
	 * @param pkPvList
	 * @return
	 */
	public int batDelConsultResponseByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已转存医嘱-会诊申请
	 * @param pkPvList
	 * @return
	 */
	public int batDelConsultApplyByPkPv(List<String> pkPvList);
	
	
	/**
	 * 根据pkpv批量删除已转存医嘱-手术申请
	 * @param pkPvList
	 * @return
	 */
	public int batDelOpApplyByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已转存执行单打印
	 * @param pkPvList
	 * @return
	 */
	public int batDelOrderOccPrtByPkPv(List<String> pkPvList);
	/**
	 * 根据pkpv批量删除已转存执行单
	 * @param pkPvList
	 * @return
	 */
	public int batDelOrderOccByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已转存领药申请
	 * @param pkPvList
	 * @return
	 */
	public int batDelPdApplyByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已转存发药记录
	 * @param pkPvList
	 * @return
	 */
	public int batDelPdDeByPkPv(List<String> pkPvList);
	/**
	 * 根据pkpv批量删除已转存记费明细
	 * @param pkPvList
	 * @return
	 */
	public int batDelIpDtByPkPv(List<String> pkPvList);
	/**
	 * 根据pkpv批量删除已转存记费明细--门诊
	 * @param pkPvList
	 * @return
	 */
	public int batDelOpDtByPkPv(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已转存医嘱
	 * @param pkPvList
	 * @return
	 */
	public int batDelCnOrderByPkPv(List<String> pkPvList);
	
	
	
	/****************************************************以下为恢复代码*******************************************************************/
	/**
	 * 根据pkpv批量恢复医嘱
	 * @param pkPvList
	 * @return
	 */
	public int batInsertCnOrderByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量恢复检查申请
	 * @param pkPvList
	 * @return
	 */
	public int batInsertRisApplyByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量恢复检验申请
	 * @param pkPvList
	 * @return
	 */
	public int batInsertLabApplyByPkPvR(List<String> pkPvList);
	/**
	 * 根据pkpv批量恢复医嘱-处方
	 * @param pkPvList
	 * @return
	 */
	public int batInsertPrescriptionByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量恢复医嘱-打印
	 * @param pkPvList
	 * @return
	 */
	public int batInsertOrderPrintByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量恢复医嘱-输血申请
	 * @param pkPvList
	 * @return
	 */
	public int batInsertTransApplyByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量恢复医嘱-会诊申请
	 * @param pkPvList
	 * @return
	 */
	public int batInsertConsultApplyByPkPvR(List<String> pkPvList);
	/**
	 * 根据pkpv批量恢复医嘱-会诊应答
	 * @param pkPvList
	 * @return
	 */
	public int batInsertConsultResponseByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量恢复医嘱-手术申请
	 * @param pkPvList
	 * @return
	 */
	public int batInsertOpApplyByPkPvR(List<String> pkPvList);
	/**
	 * 根据pkpv批量恢复执行单
	 * @param pkPvList
	 * @return
	 */
	public int batInsertOrderOccByPkPvR(List<String> pkPvList);
	/**
	 * 根据pkpv批量恢复执行单打印
	 * @param pkPvList
	 * @return
	 */
	public int batInsertOrderOccPrtByPkPvR(List<String> pkPvList);
	/**
	 * 根据pkpv批量恢复领药申请
	 * @param pkPvList
	 * @return
	 */
	public int batInsertPdApplyByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量恢复发药记录
	 * @param pkPvList
	 * @return
	 */
	public int batInsertPdDeByPkPvR(List<String> pkPvList);
	/**
	 * 根据pkpv批量恢复记费明细
	 * @param pkPvList
	 * @return
	 */
	public int batInsertIpDtByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量恢复记费明细--门诊
	 * @param pkPvList
	 * @return
	 */
	public int batInsertOpDtByPkPvR(List<String> pkPvList);
	
	
	
	/**
	 * 根据pkpv批量删除已恢复检查申请
	 * @param pkPvList
	 * @return
	 */
	public int batDelRisApplyByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已恢复检验申请
	 * @param pkPvList
	 * @return
	 */
	public int batDelLabApplyByPkPvR(List<String> pkPvList);
	/**
	 * 根据pkpv批量删除已恢复医嘱-处方
	 * @param pkPvList
	 * @return
	 */
	public int batDelPrescriptionByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已恢复医嘱-打印
	 * @param pkPvList
	 * @return
	 */
	public int batDelOrderPrintByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已恢复医嘱-输血申请
	 * @param pkPvList
	 * @return
	 */
	public int batDelTransApplyByPkPvR(List<String> pkPvList);
	/**
	 * 根据pkpv批量删除已恢复医嘱-会诊应答
	 * @param pkPvList
	 * @return
	 */
	public int batDelConsultResponseByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已恢复医嘱-会诊申请
	 * @param pkPvList
	 * @return
	 */
	public int batDelConsultApplyByPkPvR(List<String> pkPvList);
	
	
	/**
	 * 根据pkpv批量删除已恢复医嘱-手术申请
	 * @param pkPvList
	 * @return
	 */
	public int batDelOpApplyByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已恢复执行单打印
	 * @param pkPvList
	 * @return
	 */
	public int batDelOrderOccPrtByPkPvR(List<String> pkPvList);
	/**
	 * 根据pkpv批量删除已恢复执行单
	 * @param pkPvList
	 * @return
	 */
	public int batDelOrderOccByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已恢复领药申请
	 * @param pkPvList
	 * @return
	 */
	public int batDelPdApplyByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已恢复发药记录
	 * @param pkPvList
	 * @return
	 */
	public int batDelPdDeByPkPvR(List<String> pkPvList);
	/**
	 * 根据pkpv批量删除已恢复记费明细
	 * @param pkPvList
	 * @return
	 */
	public int batDelIpDtByPkPvR(List<String> pkPvList);
	
	/**
	 * 根据pkpv批量删除已恢复记费明细--门诊
	 * @param pkPvList
	 * @return
	 */
	public int batDelOpDtByPkPvR(List<String> pkPvList);
	/**
	 * 根据pkpv批量删除已恢复医嘱
	 * @param pkPvList
	 * @return
	 */
	public int batDelCnOrderByPkPvR(List<String> pkPvList);
	/**
	 * 查询病区或科室列表
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> queryDeptList(Map<String,Object> param);
}
