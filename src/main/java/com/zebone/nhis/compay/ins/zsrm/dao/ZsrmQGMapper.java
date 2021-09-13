package com.zebone.nhis.compay.ins.zsrm.dao;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybSt;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsurCata;
import com.zebone.nhis.common.module.compay.ins.qgyb.MedicalCharges;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.compay.ins.zsrm.vo.InsQgybDiseinfo;
import com.zebone.nhis.compay.ins.zsrm.vo.InsQgybIteminfo;
import com.zebone.nhis.compay.ins.zsrm.vo.InsQgybOpspdiseinfo;
import com.zebone.nhis.compay.ins.zsrm.vo.InsQgybPayInfo;
import com.zebone.nhis.compay.ins.zsrm.vo.InsQgybSetInfo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmQGMapper {
	
	/** 查询患者医保基本信息使用参数*/
	public Map<String,Object> qryInsBasePre(String pkPv);
	
	/** 查询医保挂号使用参数*/
	public Map<String,Object> qryInsRegPre(String pkPv);
	
	/** 查询医保登记信息 */
	public Map<String,Object> qryInsVisit(@Param(value="pkPv")String pkPv,@Param(value="insutype")String insutype);

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
	 * 查询INS_QGYB_ITEMMAP、INS_QGYB_ITEM、BD_pd
	 * @param praram
	 * @return
	 */
	List<MedicalCharges> qryInsQgybItemmapPd(Map<String, Object> praram);

	/**
	 * 查询INS_QGYB_ITEMMAP、INS_QGYB_ITEM、BD_ITEM
	 * @param praram
	 * @return
	 */
	List<MedicalCharges> qryInsQgybItemmapItemHC(Map<String, Object> praram);

	/**
	 * 查询INS_QGYB_ITEMMAP、INS_QGYB_ITEM、BD_ITEM
	 * @param praram
	 * @return
	 */
	List<MedicalCharges> qryInsQgybItemmapItemXM(Map<String, Object> praram);



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

	/**
	 * 查询结算清单信息
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> qrySettleList(Map paramMap);

	/**
	 * 查询基金支付信息,住院和门诊慢特病
	 * @param list
	 * @return
	 */
    List<Map<String, Object>> qryInsQgybPayInfo(ArrayList<String> list);


	/**
	 * 查询诊断信息,门诊慢特病和住院
	 * @param list
	 * @return
	 */
	List<Map<String, String>> qryInsQgybDiagInfo(ArrayList<String> list);

	/**
	 * 查询手术操作信息,门诊慢特病和住院
	 * @param list
	 * @return
	 */
	List<Map<String, Object>> qryInsQgybOprnInfo(ArrayList<String> list);

	/**
	 * 查询收费项目信息
	 * @param list
	 * @return
	 */
    List<Map<String, String>> qryInsQgybItemInfo(ArrayList<String> list);
    
    /**查询全国医保结算信息*/
    InsQgybSt qryInsuStInfo(Map<String,Object> paramMap);
    
    
	/**根据pkSettle查询就诊信息*/
	PvEncounter qryPvInfoByPkSettle(Map<String,Object> paramMap);
	/**根据pkSettle查询结算信息*/
	List<Map<String,Object>> qryStInfoByPkSettle(Map<String,Object> paramMap);
	/**查询医保结算信息*/
	List<InsQgybSetInfo> qryStInfo(Map<String,Object> paramMap);
	/**查询门诊医保结算信息*/
	List<InsQgybSetInfo> qryOpStInfo(Map<String,Object> paramMap);
	

	/**查询医保支付明细*/
	List<InsQgybPayInfo> qryInsuPayDtls(Map<String,Object> paramMap);

	/**查询费用明细信息*/
	List<Map<String,Object>> qryChargeDtls(Map<String,Object> paramMap);

	/**查询手术信息*/
	List<Map<String,Object>> qryIclDtls(Map<String,Object> paramMap);
    
	/**查询门特诊断信息*/
	List<InsQgybOpspdiseinfo> qryoOpspDiseinfoList(Map<String,Object> paramMap);
	/**查询住院诊断信息*/
	List<InsQgybDiseinfo> qryDiseinfoList(Map<String,Object> paramMap);

	/**查询门诊费用明细信息*/
	List<InsQgybIteminfo> qryOpChargeDtls(Map<String,Object> paramMap);

	/**
	 * 查询是否已匹配目录
	 * @param praram
	 */
	List<Map<String, Object>> queryHisContrast(Map<String,Object> paramMap);
}
