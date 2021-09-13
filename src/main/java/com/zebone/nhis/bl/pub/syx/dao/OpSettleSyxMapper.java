package com.zebone.nhis.bl.pub.syx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.bl.pub.syx.vo.BlOpDtRefundVo;
import com.zebone.nhis.bl.pub.syx.vo.BlSettleRefundVo;
import com.zebone.nhis.bl.pub.syx.vo.OpStInfoVo;
import com.zebone.nhis.bl.pub.syx.vo.PiParamVo;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OpSettleSyxMapper {
	
	/**
	 * 查询门诊收费患者banner信息
	 * @param paramMap{codePv,pkPv}
	 * @return
	 */
	public List<Map<String,Object>> queryPiBannerInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询患者有效就诊记录
	 * @param paramMap{euPvtype,namePi,mobile,idno,pkDept,codeOp,dateBegin,dateEnd}
	 * @return
	 */
	public List<Map<String,Object>> queryPvInfoByCon(Map<String,Object> paramMap);
	/**
	 * 查询可退结算数据
	 * @param paramMap
	 * @return
	 */
	public List<BlSettleRefundVo> querySettleInfoForRefund(Map<String,Object> paramMap);
	
	/**
	 * 查询结算记录对应收费明细
	 * @param paramMap{pkSettle}
	 * @return
	 */
	public List<BlOpDtRefundVo> querySettleRefundDetail(Map<String,Object> paramMap);
	/**
	 * 根据退费明细查询记费记录详细信息
	 * @param rtnlist
	 * @return
	 */
	public List<BlOpDt> queryCgDetailsByPk(List<BlOpDtRefundVo> list);
	
	/**
	 * 根据发票号，查询发票明细
	 * @param codeInv
	 * @return
	 */
	public List<Map<String,Object>> queryInvoiceDtByCode(@Param("codeInv")String codeInv);
	/**
	 * 更新患者基本信息,只更新界面录入的信息
	 * @param pivo
	 */
	public void updatePiMaster(PiParamVo pivo);
	/**
	 * 更新就诊信息
	 * @param pivo
	 */
	public void updatePvEncounter(PiParamVo pivo);
	/**
	 * 查询患者信息
	 * @param paramMap
	 * @return
	 */
	public PiParamVo queryPiInfo(Map<String,Object> paramMap);
	
	/**查询患者诊断是否包含自费诊断*/
	public List<String> qryZfDiagByPv(String pkPv);
	
	/**查询就诊患者的有效结算记录*/
	public List<OpStInfoVo> qryStInfoByPv(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> qryStDurgDeptEx(Map<String,Object> paramMap);
	
	/**根据票据号查询对应结算信息*/
	public BlSettle qryStInfoByCodeInv(Map<String,Object> paramMap);
}
