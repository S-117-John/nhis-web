package com.zebone.nhis.pro.zsba.mz.ins.zsba.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ZsbaSgsMapper {
	
	/** 查询患者医保信息*/
	public BdHp qryPvHp (String pkPv);
	
	/** 查询患者医保基本信息使用参数*/
	public Map<String,Object> qryInsBasePre(String pkPv);
	
	/** 查询医保挂号使用参数*/
	public Map<String,Object> qryInsRegPre(String pkPv);
	
	/** 查询门诊费用信息*/
	public List<Map<String,Object>> qryChargeDetailNoUpload(Map<String,Object> param);
	
	/** 查询门诊费用信息-特需费用*/
	public List<BlOpDt> qryChargeDetailNoUploadSpec(Map<String,Object> param);
	
	/** 查询所有明细 */
	public List<BlOpDt> qryChargeDts(Map<String, Object> praram);
	
	/** 查询预结算退费信息*/
	public Map<String,Object> qryBlSt(Map<String, Object> praram);
	
	/** 查询退费结算主键*/
	public String qryPksettleCanc(@Param(value="pkPv") String pkPv,@Param(value="ybPksettle") String setlId);
	/**
	 * 查询省工伤医保结算总账
	 * @param praram
	 * @return
	 */
	List<Map<String, Object>> qrySgsMedicalInsuranceSum(Map<String, Object> praram);
}
