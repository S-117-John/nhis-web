package com.zebone.nhis.ma.rationaldrug.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.pi.PiAddress;
import com.zebone.nhis.common.module.pi.PiAllergic;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface RationalDrugPubMapper {
	/**
	 * 查询患者信息
	 * @param paramMap{pkPi}
	 * @return
	 */
	public PiMaster queryPiMaster(Map<String,Object> paramMap);
	/**
	 * 查询患者地址信息
	 * @param paramMap{pkPi}
	 * @return
	 */
	public List<PiAddress> queryPiAddress(Map<String,Object> paramMap);
	/**
	 * 查询患者过敏史
	 * @param paramMap{pkPi}
	 * @return
	 */
	public List<PiAllergic> queryPiAllergic(Map<String,Object> paramMap);
	/**
	 * 查询患者住院信息
	 * @param paramMap{pkPv}
	 * @return
	 */
	public Map<String,Object> queryPvIpInfo(Map<String,Object> paramMap);
	/**
	 * 查询患者就诊诊断信息
	 * @param paramMap{pkPv}
	 * @return
	 */
	public List<PvDiag> queryPvDiags(Map<String,Object> paramMap);
	/**
	 * 查询患者住院预交金信息
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal queryIpPreAmt(Map<String,Object> paramMap);
	/**
	 * 查询处方信息
	 * @param paramMap{pkPres}
	 * @return
	 */
	public CnPrescription  queryPresInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询处方医嘱信息
	 * @param paramMap{pkPres}
	 * @return
	 */
	public List<Map<String,Object>> queryOrderInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询处方草药医嘱信息
	 * @param paramMap{pkPres}
	 * @return
	 */
	public List<Map<String,Object>> queryHerbOrdInfo(Map<String,Object> paramMap);
/**
 * @Description 查询门
 * @auther wuqiang
 * @Date 2019/7/30
 * @Param [paramMap]
 * @return java.util.Map<java.lang.String,java.lang.Object>
 */
	 public Map<String, Object>   queryPvOpInfo(Map<String, Object> paramMap);
}
