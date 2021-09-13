package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import java.util.Map;
import com.zebone.nhis.webservice.syx.vo.platForm.BlOpDtVo;
import com.zebone.nhis.webservice.syx.vo.platForm.HpVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybSt;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzybTrialstVo;
import com.zebone.nhis.webservice.syx.vo.platForm.OnlineBlopdtVo;
import com.zebone.nhis.webservice.syx.vo.platForm.OnlineSettlementVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PlatFormOnlineMapper {

	/**
	 * 查询待结算费用明细
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<OnlineBlopdtVo> queryNoSettleInfoForCgByPkPv(
			Map<String, Object> paramMap);

	/**
	 * 查询医保ins_gzyb_st、ins_gzyb_st_city结算信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public OnlineSettlementVo querysettlementvoByPkPv(
			Map<String, Object> paramMap);

	/**
	 * 查询医保结算ins_gzyb_st信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public GzybSt querygzybstvoByPkPv(Map<String, Object> paramMap);

	/**
	 * 获取医保预结算信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public InsGzybTrialstVo queryTrialstByPkPv(Map<String, Object> paramMap);

	/**
	 * 查询费用明细
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<BlOpDt> queryCgDetailsByPk(Map<String, Object> paramMap);

	/** 获取患者主医保是否是广州公医 */
	public Map<String, Object> qryPvInsuAttrVal(Map<String, Object> paramMap);

	/** 查询患者本次就诊待结算记费明细 */
	public List<BlOpDt> qryOpcgsByPv(Map<String, Object> paramMap);

	/** 获取本月已结算信息 */
	public Map<String, Object> qrySpdisStOpAmt(Map<String, Object> paramMap);

	/** 查询公医特病门诊待结算金额 */
	public Map<String, Object> qrySpdisOpAmt(Map<String, Object> paramMap);

	/** 查询患者医保类型下的记费明细 */
	public List<BlOpDtVo> qryOpListByPv(Map<String, Object> paramMap);

	/** 查询患者门诊特殊病信息 */
	public Map<String, Object> qrySpdisInfoByPv(Map<String, Object> paramMap);

	/** 查询公医慢病门诊待结算金额 */
	public Map<String, Object> qrygyChronicOpAmt(Map<String, Object> paramMap);

	/** 根据诊断查询是否属于慢病类型 */
	public Integer qryChronicTypeByDiag(Map<String, Object> paramMap);

	/** 查询非药品待结算金额 */
	public Map<String, Object> qrygyGeneralOpItemAmt(
			Map<String, Object> paramMap);

	/** 查询待结算诊查费主键 */
	public List<String> qryExamPkList(Map<String, Object> paramMap);

	/** 查询患者就诊诊查费集合 */
	public Map<String, Object> qryExamFeeInfo(Map<String, Object> paramMap);

	/** 查询患者有无结算信息(过滤取消结算) */
	public Integer qryStInfoByPv(Map<String, Object> paramMap);

	/** 查询医保计划扩展属性“0302”（广州公医普通门诊报销限制包含药费的诊次数）值 */
	public String qryOpVisitCount(Map<String, Object> paramMap);

	/** 获取患者当日诊次记录以及诊次药品费用 */
	public List<Map<String, Object>> qryTodayDrugAmtByPi(
			Map<String, Object> paramMap);

	/** 查询公医普通门诊待结算药费 */
	public Map<String, Object> qrygyGeneralOpDrugAmt(
			Map<String, Object> paramMap);

	/** 查询患者不同自付比例的总数量 */
	public Map<String, Object> qryRatioCountByPv(Map<String, Object> paramMap);
	 /**查询患者主诊断名称*/
    public String qryNamediagByPv(Map<String,Object> paramMap);
    /**查询患者医保就诊模式*/
    public String qryHpStatusByPv(Map<String,Object> paramMap);
    /**查询医保相关信息*/
    public HpVo queryHp(Map<String,Object> paramMap);
}
