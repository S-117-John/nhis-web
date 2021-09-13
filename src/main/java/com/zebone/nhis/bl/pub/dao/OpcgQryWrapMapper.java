package com.zebone.nhis.bl.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.pub.vo.BlExtPayVo;
import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OpcgQryWrapMapper {

	/**
	 * 根据日期查询患者已结算的结算记录
	 * @param mapParam
	 * @return
	 */
	List<Map<String, Object>> querySettleRecords(Map<String, Object> mapParam);
	
	
	/**
	 * 查询结算下的处方
	 * @param mapParam
	 * @return
	 */
	List<Map<String, Object>> querySettlePressRecord(Map<String, String> mapParam);
	
	/**
	 * 查询已结算的处方明细
	 * @param pkSettle
	 * @return
	 */
	List<Map<String, Object>> querySettlePressItem(Map<String, String> mapParam);

	
	/**
	 * 查询结算下的检查检验
	 * @param mapParam
	 * @return
	 */
	List<Map<String, Object>> querySettleAssistRecord(Map<String, String> mapParam);
	
	/**
	 * 查询已结算的检查检验明细
	 * @param mapParam
	 * @return
	 */
	List<Map<String, Object>> querySettleAssistItem(Map<String, String> mapParam);
	
	
	/**
	 * 查询已经结算的费用执行单（处方和检查治疗）
	 * @param opCgTransforVo
	 * @return
	 * @throws BusException
	 */
	List<Map<String, Object>> queryPatiCgInfoSettled(Map<String, Object> mapParam) throws BusException;
	
	/**
	 * 通过患者主键主键查询第三方支付信息
	 * @param paramMap
	 * @return
	 */
	public List<BlExtPayVo> queryBlExtPayRefund(Map<String,Object> paramMap);

	/**
	 * 根据就诊主键查询收费表
	 * @param pkPvs
	 * @return
	 */
	public List<BlOpDt> qryBlOpdtByPkPvs(List<String> pkPvs);
	/**
	 * 查询门诊未结算费用明细(重新计算患者优惠比例使用)
	 * @param paramMap{pkPv,codePv}
	 * @return
	 */
	public List<ItemPriceVo> queryNoSettleInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询门诊未收费结算项目[门诊收费使用]--中二版重构
	 * @param paramMap{pkPv,curDate,pkPi}
	 * @return
	 */
	public List<BlPatiCgInfoNotSettleVO> queryNoSettleInfoForCg(Map<String,Object> paramMap);
	/**
	 * 查询门诊已收费结算挂号费[门诊收费使用]--中二版
	 * @param paramMap{pkPv,curDate,pkPi}
	 * @return
	 */
	public List<BlPatiCgInfoNotSettleVO> querySettlePvInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询患者本次就诊已结算的诊查费
	 * @param paramMap
	 * @return
	 */
	public List<BlOpDt> qryStIpcFeeByPv(Map<String,Object> paramMap);
	
	/**查询待结算发票明细信息*/
	public List<BlInvoiceDt> qryInvDtByCg(Map<String,Object> paramMap);
	
	/**查询医嘱附加费用信息*/
	public List<Map<String, Object>> qryEtceterasList(Map<String,String> paramMap);
	
	/**查询医嘱附加费用明细信息*/
	public List<Map<String, Object>> qryEtceterasItem(Map<String,String> paramMap);
	
	/**查询非医嘱费用明细信息*/
	public List<Map<String, Object>> qryCgItemByPkCgop(Map<String,String> paramMap);
}
