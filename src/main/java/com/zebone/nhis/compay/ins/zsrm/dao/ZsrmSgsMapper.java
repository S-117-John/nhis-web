package com.zebone.nhis.compay.ins.zsrm.dao;

import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.compay.ins.zsrm.vo.InsSgsybVisit;
import com.zebone.nhis.compay.ins.zsrm.vo.ZyChargeDetail;
import com.zebone.nhis.compay.ins.zsrm.vo.ZyHpDisParam;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmSgsMapper {
	
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

	/**
	 * 根据科室主键，查询科室的编码和名称
	 * @param pkDept
	 * @return
	 */
	BdOuDept qryDeptInfoByPkDept(@Param("pkDept") String pkDept);

	/**
	 * 更新ins_sgsyb_visit
	 * @param visit
	 */
	void updateInsSgsYbVisit(InsSgsybVisit visit);

	/**
	 * 查询住院费用信息
	 * @param paramMap
	 * @return
	 */
	List<ZyChargeDetail> qryZyChargeDetail(Map paramMap);

	/**
	 * 查询住院登记信息
	 * @param pkPv
	 * @return
	 */
    InsSgsybVisit qryInsSgsYbVisit(@Param("pkPv") String pkPv);

	/**
	 * 更新住院收费明细表中的医保上传标志
	 * @param updateList
	 */
	void updateFlagInsu(List<ZyChargeDetail> updateList);

	/**
	 * 查询出院登记参数
	 * @param pkPv
	 * @return
	 */
    ZyHpDisParam qryZyHpDisParam(@Param("pkPv") String pkPv);

	/**
	 * 查询患者的入院信息和诊断信息
	 * @param pkPv
	 * @return
	 */
	ZyHpDisParam qryIpInfoByPkPv(@Param("pkPv") String pkPv);

	/**
	 * 向医保结算信息表中添加his结算信息
	 * @param map
	 */
    void updateInsSgsYbSt(Map map);
}
