package com.zebone.nhis.pro.zsba.mz.ins.zsba.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsurCata;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.MedicalCharges;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ZsbaQGMapper {
	
	/** 查询患者医保基本信息使用参数*/
	public Map<String,Object> qryInsBasePre(String pkPv);
	
	/** 查询医保挂号使用参数*/
	public Map<String,Object> qryInsRegPre(String pkPv);
	
	/** 查询医保登记信息 */
	public Map<String,Object> qryInsVisit(String pkPv);

	/** 查询患者主诊断信息 */
	public List<Map<String,Object>> qryPvDiag(String pkPv);
	
	/** 查询待上传费用明细 */
	public List<Map<String,Object>> qryChargeDetailNoUpload(Map<String, Object> praram);
	
	/** 查询非特需费用明细 */
	public List<BlOpDt> qryChargeDetailNoUploadSpec(Map<String, Object> praram);
	
	/** 查询所有明细 */
	public List<BlOpDt> qryChargeDts(Map<String, Object> praram);
	
	/** 查询患者医保基本信息使用参数*/
	public Map<String,Object> qryBlSt(Map<String, Object> praram);
	
	/** 查询退费结算主键*/
	public String qryPksettleCanc(@Param(value="pkPv") String pkPv,@Param(value="setlId") String setlId);
	
	/**更新医保登记信息*/
	//public void updateVisit(Map<String, Object> praram);
	/**更新医保计划*/
	 void updatePv(Map<String, Object> praram);

	/**
	 * 查询HIS药品字典对照信息
	 * @param praram
	 */
	List<MedicalCharges> qryHisPdIns(Map<String, Object> praram);
	/**
	 * 查询HIS收费项目字典对照信息
	 * @param praram
	 */
	List<MedicalCharges> qryHisItemIns(Map<String, Object> praram);

	/**
	 * 查询医保项目信息
	 * @param praram
	 * @return
	 */
	List<InsurCata> qryInsQgybItem(Map<String, Object> praram);
	
	
	

	/**
	 * 查询INS_QGYB_ITEMMAP、INS_QGYB_ITEM、BD_ITEM、BD_pd
	 * @param praram
	 * @return
	 */
	List<MedicalCharges> qryInsQgybItemmap(Map<String, Object> praram);
	Map<String,Object> qryItemMap(@Param(value="pkItemMap") String pkItemMap);
	/**
	 * 删除INS_QGYB_ITEMMAP
	 * @param praram
	 * @return
	 */
	int delInsQgybItemmap(Map<String, Object> praram);

	/**
	 * 查询全国医保结算总账
	 * @param praram
	 * @return
	 */
	List<Map<String, Object>> qryQGYBMedicalInsuranceSum(Map<String, Object> praram);
	/**
	 * 查询全国医保结算明细
	 * @param praram
	 * @return
	 */
	List<Map<String, Object>> qryQGYBMedicalInsuranceDetailed(Map<String, Object> praram);
	
	List<Map<String, Object>> qryRecordMtList(Map<String, Object> praram);
	
	/**根据处方主键查询其中中药饮片相关信息**/
	public List<Map<String, Object>> qryHerbalListInfoByPkPres(@Param("pkPresList") List<String> pkPresList);
}
