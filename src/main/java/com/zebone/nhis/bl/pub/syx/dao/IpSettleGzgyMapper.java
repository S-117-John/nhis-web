package com.zebone.nhis.bl.pub.syx.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.bl.pub.vo.GyHpDivInfo;
import com.zebone.nhis.bl.pub.vo.GyStItemVo;
import com.zebone.nhis.common.module.bl.BlAmtVo;
import com.zebone.nhis.common.module.bl.InvItemVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface IpSettleGzgyMapper {
	
	/**查询医保单位支付比例*/
	public String qryHpRatioUnit(@Param("pkHp")String pkHp);
	
	/**查询就诊药品费用*/
	public List<Map<String,Object>> qryDrugCostByPv(Map<String,Object> paramMap);
	
	/**查询就诊非药品药品费用*/
	public Map<String,Object> qryItemCostByPv(Map<String,Object> paramMap);
	
	/**查询患者结算金额信息*/
	public Map<String,Object> qryStInfoByPv(Map<String,Object> paramMap);
	
	/**获取患者主医保是否是广州公医*/
	public Map<String,Object> qryPvInsuAttrVal(Map<String,Object> paramMap);
	
	/**查询患者主医保 已报计划类型*/
	public Map<String,Object> qryPvEuHptype(Map<String,Object> paramMap);
	
	/**查询患者0311本院职工标志*/
	public Map<String,Object> qryPvByzgFlag(Map<String,Object> paramMap);
	
	/**查询患者特诊加收费用明细*/
	public List<Map<String,Object>> queryBlCgIpDetailsT(Map<String,Object> map);
	
	/**查询患者特诊加收费用明细*/
	public List<Map<String,Object>> queryBlCgIpDetails(Map<String,Object> map);
	
	/**
	 * 查询预交金
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getYjFee(Map<String,Object> paramMap);
	
	/**
	 * 查询担保金
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getDbFee(Map<String,Object> paramMap);
	
	/**
	 * 查询总费用
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getTotalFee(Map<String,Object> paramMap);
	
	/**
	 * 查询在途费用非药品
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getZtNPdFee(Map<String,Object> paramMap);
	
	/**
	 * 查询在途费用药品
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getZtPdFee(Map<String,Object> paramMap);
	
	/**
	 * 查询固定费用
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getGdFee(Map<String,Object> paramMap);
	
	/**
	 * 查询总费用(特诊)
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getTotalFeeT(Map<String,Object> paramMap);
	
	/**
	 * 查询总费用(特诊)
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getTotalFeeFT(Map<String,Object> paramMap);
	
	/**查询患者非特诊加收费用明细*/
	public List<Map<String,Object>> queryBlCgIpDetailsFT(Map<String,Object> map);
	
	/**更新住院计费表*/
	public int updateBlInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询退费主键信息
	 * @param pkList
	 * @return
	 */
	public List<String> qryPkCgBack(@Param("pkList") List<String> pkList);
	
	/**
	 * 查询非特诊发票信息
	 * @param pkPv
	 * @param dateBegin
	 * @param dateEnd
	 * @param pkCgips
	 * @return
	 */
	public List<InvItemVo> qryPvNotSpecInvItem(@Param("pkPv") String pkPv,@Param("dateBegin") String dateBegin,@Param("dateEnd") String dateEnd,@Param("pkList") List<String> pkCgips);	
	
	/**
	 * 查询特诊发票信息
	 * @param pkPv
	 * @param dateBegin
	 * @param dateEnd
	 * @param pkCgips
	 * @return
	 */
	public List<InvItemVo> qryPvSpecInvItem(@Param("pkPv") String pkPv,@Param("dateBegin") String dateBegin,@Param("dateEnd") String dateEnd,@Param("pkList") List<String> pkCgips);	
	
	/**
	 * 查询非特诊总费用
	 * @param pkPv
	 * @param dateBegin
	 * @param dateEnd
	 * @param pkCgips
	 * @return
	 */
	public BlAmtVo qryPvAmtAndPiFT(@Param("pkPv") String pkPv,@Param("dateBegin") String dateBegin,@Param("dateEnd") String dateEnd,@Param("pkList") List<String> pkCgips);
	
	/**
	 * 查询特诊总费用
	 * @param pkPv
	 * @param dateBegin
	 * @param dateEnd
	 * @param pkCgips
	 * @return
	 */
	public BlAmtVo qryPvAmtAndPiT(@Param("pkPv") String pkPv,@Param("dateBegin") String dateBegin,@Param("dateEnd") String dateEnd,@Param("pkList") List<String> pkCgips);
	
	public List<InvItemVo> QryInvItemInfo(@Param(value = "pkPv") String pkPv,@Param(value = "pkOrg") String pkOrg,
			@Param("dateBegin") String dateBegin,@Param("dateEnd") String dateEnd,@Param("pkList") List<String> pkCgips);
	
	public BlAmtVo QryAmtAndPi(@Param(value = "pkPv") String pkPv,@Param(value = "dateBegin")String dateBegin, @Param(value = "dateEnd") String dateEnd,@Param("pkList") List<String> pkCgips);
	
	/**
	 * 查询患者本次就诊的药费
	 * @param pkPv
	 * @return
	 */
	public Map<String,Object> qryStDrugAmt(Map<String,Object> paramMap);
	
	/**
	 * 查询票据信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryInvInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询票据是否已经使用
	 * @param paramMap
	 * @return
	 */
	public List<String> qryInvIsUse(Map<String,Object> paramMap);
	
	/**查询患者最近一次结算时间*/
	public List<Date> qryDateStByPv(Map<String,Object> paramMap);
	
	/**查询就诊患者医保信息*/
	public Map<String,Object> qryHpInfoByPv(String pkPv);
	
	/**查询公医待结算非药品信息*/
	public List<GyStItemVo> qryGyItemStList(Map<String,Object> paramMap);
	
	/**查询材料费策略信息*/
	public List<GyHpDivInfo> qryMaterialsInfo(Map<String,Object> paramMap);
	
	/**查询非药品待结算信息*/
	public Map<String,Object> qryItemCgInfo(@Param(value = "pkPv")String pkPv,@Param("begin") Date begin,@Param("end") Date end,@Param(value = "pkSettle")String pkSettle,@Param("pkList") List<String>pkList,@Param("pkCgips") List<String> pkCgips);
	
	public List<InvItemVo> QryInvItemInfoByPkSettle(@Param(value = "pkSettle")String pkSettle);
    /**查询草药是否是限制性用药信息*/
    List<Map<String,String>> queryHerbalAttr(@Param("pkPds") List<String> pkPds);
}
