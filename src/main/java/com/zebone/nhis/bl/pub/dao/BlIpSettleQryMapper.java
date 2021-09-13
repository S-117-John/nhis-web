package com.zebone.nhis.bl.pub.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.bl.pub.vo.BlDepositVo;
import com.zebone.nhis.bl.pub.vo.BlIpDtsVo;
import com.zebone.nhis.bl.pub.vo.BlSettleDetailVo;
import com.zebone.nhis.bl.pub.vo.BlSettleVo;
import com.zebone.nhis.bl.pub.vo.BlStInvVo;
import com.zebone.nhis.bl.pub.vo.InvInfoVo;
import com.zebone.nhis.bl.pub.vo.OpdtGroupVo;
import com.zebone.nhis.bl.pub.vo.PvDiagVo;
import com.zebone.nhis.bl.pub.vo.PvEncounterVo;
import com.zebone.nhis.bl.pub.vo.SettleRecord;
import com.zebone.nhis.bl.pub.vo.StQryCharge;
import com.zebone.nhis.bl.pub.vo.StQryDepoInfo;
import com.zebone.nhis.bl.pub.vo.StQryInsuSt;
import com.zebone.nhis.bl.pub.vo.StQryInvDt;
import com.zebone.nhis.common.module.bl.BlAmtVo;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.InvItemVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BlIpSettleQryMapper {
	
	List<SettleRecord> qryStRecords(Map<String, Object> mapParam) ;
	
	List<com.zebone.nhis.bl.pub.vo.StQryInvInfo> qryInvInfo(Map<String, Object> mapParam) ;
	
	List<StQryDepoInfo> qryDepoInfo(Map<String, Object> mapParam) ;
	
	List<StQryInsuSt> qryInsuSt(Map<String, Object> mapParam) ;
	
	List<StQryCharge> qryCgStIp(Map<String, Object> mapParam) ;
	
	List<StQryCharge> qryCgStOp(Map<String, Object> mapParam) ;
	
	List<StQryInvDt> qyrInvDt(Map<String, Object> mapParam) ;
	/**
	 * 根据就诊记录查询结算记录
	 * @param pkPv
	 * @return
	 */
	public List<Map<String,Object>> querySettleRecordByPv(Map<String,Object> paramMap);

	/**
	 * 门诊和住院的结算查询功能分开
	 * 门诊结算查询，查找所有
	 * @param params
	 * @return
	 */
	List<SettleRecord> qryStRecordAll(Map<String, Object> params);

	/**
	 * 门诊和住院的结算查询功能分开
	 * 住院结算查询，查找所有
	 * @param params
	 * @return
	 */
	List<SettleRecord> qryIpStRecordAll(Map<String, Object> params);
	
	public List<InvItemVo> QryInvItemInfo(@Param(value = "pkPv") String pkPv,@Param(value = "pkOrg") String pkOrg,
			@Param(value = "dateBegin") String dateBegin,
			@Param(value = "dateEnd") String dateEnd,
			@Param(value = "pkDepts")List<InvInfoVo>  pkDepts,
			@Param(value = "pkSettle")String pkSettle);	
	public BlAmtVo QryAmtAndPi(@Param(value = "pkPv") String pkPv,@Param(value = "dateBegin")String dateBegin, @Param(value = "dateEnd") String dateEnd,@Param(value = "pkSettle")String pkSettle);
	/**
	 * 查询患者预缴金
	 * @param paraMap
	 * @return
	 */
	public List<Map<String,Object>> queryChargePrePay(Map<String, Object> paraMap);
	/**
	 * 查询费用项目信息
	 * @param paraMap
	 * @return
	 */
	public List<Map<String,Object>> queryChargeItem(Map<String, Object> paraMap);
	/**
	 * 查询费用分类信息
	 * @param paraMap
	 * @return
	 */
	public List<Map<String,Object>> queryChargeClassify(Map<String, Object> paraMap);

	/**
	 * 查询患者未结算项目
	 * @param paraMap
	 * @return
	 */
	List<Map<String, Object>> getPatientChargeItem(Map<String, Object> paraMap);
	
	/**
	 * 根据患者就诊主键查询待结算项目
	 * @param paraMap
	 * @return
	 */
	List<Map<String, Object>> qryWaitChargeItem(Map<String, Object> paraMap);
	
	/**
	 * 特诊发票
	 * @param pkPv
	 * @param pkOrg
	 * @param dateBegin
	 * @param dateEnd
	 * @return
	 */
	public List<InvItemVo> qrySpecInvItem(@Param("pkSettle")String pkSettle);	
	
	/**
	 * 非特诊发票
	 * @param pkPv
	 * @param pkOrg
	 * @param dateBegin
	 * @param dateEnd
	 * @return
	 */
	public List<InvItemVo> qryNotSpecInvItem(@Param("pkSettle")String pkSettle);	
	
	/**查询费用分类信息*/
	public List<Map<String,Object>> qryAmtCate(Map<String,Object> paramMap);
	
	/**查询费用分类明细信息*/
	public List<Map<String,Object>> qryCateDtlsInfo(Map<String,Object> paramMap);
	
	/**查询费用分类明细信息*/
	public List<Map<String,Object>> qrySumCateDtlsInfo(Map<String,Object> paramMap);
	
	/**查询非特诊总费用*/
	public BlAmtVo QryAmtAndPiFT(@Param("pkSettle")String pkSettle);
	
	/**查询特诊总费用*/
	public BlAmtVo QryAmtAndPiT(@Param("pkSettle")String pkSettle);
	
	/**
	 * 查询患者就诊诊断信息
	 * @param pkPv
	 * @return
	 */
	List<PvDiagVo> qryPvDiags(String pkPv);
	
	/**
	 * Oracle
	 * 查询患者全部的就诊记录
	 * @param pkPv
	 * @return
	 */
	List<PvEncounterVo> qryPvEncounters(Map map);
	
	/**
	 * SqlServer
	 * 查询患者全部的就诊记录
	 * @param pkPv
	 * @return
	 */
	List<PvEncounterVo> qryPvInfoSqlServer(Map map);
	
	/**
	 * 查询患者费用分类
	 * @param pkPv
	 * @return
	 */
	List<BlSettleVo> qryBlSettles(String pkPv);
	
	/**
	 * 查询发票信息
	 * @param pkSettles
	 * @return
	 */
	List<BlStInvVo> qryBlStInv(String pkSettle);
	
	/**
	 * 查询费用明细
	 * @param map
	 * @return
	 */
	List<BlIpDtsVo> qryBlIpDtInfo(Map map);
	
	/**
	 * 查询费用分类
	 * @param map
	 * @return
	 */
	List<BlIpDtsVo> qryBlIpDts(Map map);
	
	/**
	 * 预交金
	 * @param map
	 * @return
	 */
	List<BlDepositVo> qryBlDeposits(Map map);
	
	/**
	 * 保险明细
	 * @param map
	 * @return
	 */
	List<BlSettleDetailVo> qryBlSettleDetail(String pkSettle);
	
	/**
	 * 结算支付
	 * @return
	 */
	List<BlDepositVo> qryBlDepositInfo(String pkSettle);
	
	Map<String, String> qryPkOrg(String pkPi);
	
	List<String> qryPkInvoices(String pkSettle);
	
	List<Map<String, Object>> qryInvoice(String pkInvoice);
	
	String qryPkOrgByPkPv(String pkPv);
	
	List<Map<String, Object>> qryMaleMedicine(String pkSettle);
	
	List<BlIpDtsVo> qryNotInvitem(List<String> list);
	
	/**查询待欠款结算信息*/
	public List<Map<String,Object>> qryDebtStInfo(Map<String,Object> paramMap);
	
	/**查询欠款结算患者信息*/
	public Map<String,Object> qryDebtPatiInfo(Map<String,Object> paramMap);
	
	/**查询欠款结算费用分类信息*/
	public List<Map<String,Object>> qryDebtCateAmtInfo(Map<String,Object> paramMap);
	
	/**查询欠款结算预交金信息*/
	public List<Map<String,Object>> qryDebtPerpAmtInfo(Map<String,Object> paramMap);
	
	/**根据结算主键查询收费项目总条目*/
	public Double qryOpCntByPkSt(Map<String,Object> paramMap);
	
	/**查询发票分类类型*/
	public String qryInvCateType(Map<String,Object> paramMap);
	
	/**查询发票对应的门诊结算明细集合*/
	public List<BlOpDt> qryOpListByPkinv(Map<String,Object> paramMap);
	
	/**根据票据主键查询该发票关联的患者就诊类型*/
	public String qryPvtypeByPkInvoice(Map<String,Object> paramMap);
	
	/**查询票据明细信息*/
	public List<StQryInvDt> qyrOpdtByPkInvoice(Map<String,Object> paramMap);
	
	/**查询预结算信息*/
	public Map<String,Object> qryPreStInfo(Map<String,Object> paramMap);
	
	/**根据执行科室分组查询记费明细信息*/
	public List<OpdtGroupVo> qryDtListByDeptEx(Map<String,Object> paramMap);
	
	/**查询结算关联发票信息*/
	public List<BlInvoice> qryStInvInfo(Map<String,Object> paramMap);
	
	/**查询患者现金缴款信息*/
	public List<BlDeposit> qryCashDepoInfo(Map<String,Object> paramMap);
	
	/**查询患者可退预交金集合*/
	public List<Map<String,Object>> qryStBackDepo(Map<String,Object> paramMap);

	/**查询费用分类汇总*/
	List<BlIpDtsVo> qryBlIpDtCount(Map<String, Object> map);

	/** 查询明细票据分类信息 */
	public List<Map<String, Object>> qyrOpdtItemcate(Map<String, Object> map);

	/**根据结算主键查询该发票关联的患者就诊类型*/
	String qryPvtypeByPkSettle(Map<String,Object> paramMap);

	/**根据结算主键查询票据明细信息*/
	List<StQryInvDt> qyrIpdtByPkSettle(Map<String,Object> paramMap);

	/**查询患者辅医保信息*/
	List<Map<String,Object>> qryMirHpInfoByPkPv(Map<String,Object> paramMap);
}
