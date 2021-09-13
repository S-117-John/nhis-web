package com.zebone.nhis.compay.ins.shenzhen.dao.city;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.pi.InsQgybPV;
import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybDise;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybItem;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybVisit;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybVisitCity;
import com.zebone.nhis.common.module.compay.ins.shenzhen.PageSzYbQryParam;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ShenzhenCityMapper {
	public List<Map<String,Object>> qryVisit(Map<String,Object> paramMap);

	public List<Map<String,Object>> qryVisitCity(Map<String,Object> paramMap);

	public List<Map<String,Object>> qryUploadDetail(Map<String,Object> paramMap);
	
	public List<BlIpDt> qryF00000000013(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> qryUploadPresDetail(Map<String,Object> paramMap);
	
	public List<String> qryUploadDetailZeroAmount(Map<String,Object> paramMap);

	public void updateFlagInsuByPk(Map<String,Object> paramMap);

	public List<Map<String,Object>> qryPvDiag(Map<String,Object> paramMap);

	public Map<String,Object> qryBlStatistical(Map<String,Object> paramMap);

	public List<Map<String,Object>> qryPresNo(Map<String,Object> paramMap);

	public List<BdPd> qryBdPdInfo(PageSzYbQryParam qryparam);

	public List<InsSzybItem> qryDrugInsSzybItemInfo(PageSzYbQryParam qryparam);

	public List<Map<String,Object>> qryBdItemInfo(PageSzYbQryParam qryparam);

	public List<InsSzybItem> qryNoDrugInsSzybItemInfo(PageSzYbQryParam qryparam);

	public List<Map<String,Object>> qryNoDrugMapInfo(PageSzYbQryParam qryparam);

	public List<Map<String,Object>> qryDrugMapInfo(PageSzYbQryParam paramMap);


	public InsSzybItem qrySzybItem(Map<String,Object> paramMap);

	public BdPd qryBdPd(Map<String,Object> paramMap);

	public BdItem qryBdItem(Map<String,Object> paramMap);

	public List<Map<String,Object>> qryInsSzybSt(Map<String,Object> paramMap);

	public Map<String,Object> qryInsDictmap(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> queryPreSettlePat(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> queryInsItem(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> queryInsOrd(Map<String,Object> paramMap);
	
	/**根据疾病编码查询疾病信息*/
	public List<InsSzybDise> qryDiseListByCode(@Param("codeList")List<String> codeList);
	
	/**查询医院医保疾病信息字典*/
	public List<Map<String,Object>> disePageHospitalQuery(PageSzYbQryParam qryparam);

	public Map<String,Object> queryPresInfo(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> queryFitInfo(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> queryPresClassInfo(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> queryPvHistory(Map<String,Object> paramMap);
	
	/**根据pk_item查询上传费用明细信息*/
	public List<Map<String,Object>> qryUploadInfoByPkItem(@Param("pkList")List<String> pkList);
	
	/**查询门诊挂号登记信息*/
	public List<Map<String,Object>> qryOpVisitInfo(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> qryOpStUploadDetail(Map<String,Object> paramMap);
	
	public List<String> qryOpDtUpDtlsZeroAmount(Map<String,Object> paramMap);
	
	public void updateOpdtFlagInsuByPk(Map<String,Object> paramMap);

	public List<Map<String,Object>> qryOpdtPresNo(Map<String,Object> paramMap);
	
	public Map<String,Object> qryOpdtBlStatistical(Map<String,Object> paramMap);
	
	public String qrySrvDictInfo(@Param("pkSchSrv")String pkSchSrv);
	
	/**根据结算主键查询结算明细信息*/
	public List<Map<String,Object>> qryOpListByPkSettle(@Param("pkSettle")String pkSettle);
	
	public List<Map<String,Object>> qryBackOpStUploadDetail(Map<String,Object> paramMap);

	List<Map<String,Object>> qryQuePvInfo(Map<String,Object> paramMap);
	
	public int updateOpVisitInfo(InsSzybVisit visitInfo);
	
	public int updateOpVisitCityInfo(InsSzybVisitCity visitCityInfo);

	/**根据记费主键查询医嘱0205扩展属性*/
	public List<String> qryOrdAttrByPkCgop(@Param("pkList")List<String> pkList);

	/**根据pkPv查询患者是否存在未结算的特检项目*/
	public List<String> qrySpecSuByPkPv(Map<String,Object> paramMap);

	List<Map<String,Object>> qryCodeOpByAaz500(@Param("insuNoList")List<String> insuNoList);

	List<Map<String,Object>>qryHisStInfoByBkc384(@Param("bkc384List")List<String> bkc384List);

	/**查询全国医保门诊挂号登记信息*/
	public List<Map<String,Object>> qryQgybOpVisitInfo(Map<String,Object> paramMap);

	List<InsSzybVisitCity> qryVisitCityByPkPi(Map<String,Object> paramMap);

	InsSzybVisit qryVisitInfoByPkPi(Map<String,Object> paramMap);

	/**根据pkVisit查询全国医保登记信息*/
	Map<String,Object> qryQgybVisitInfo(Map<String,Object> paramMap);

	/**查询医保就诊信息*/
	List<Map<String,Object>> qryInsuPvInfo(Map<String,Object> paramMap);

	List<InsQgybPV> qryInsuPvByPkPi(Map<String,Object> paramMap);

	/**全国医保查询限制性用药信息*/
	List<Map<String,Object>> queryQgFitInfo(Map<String,Object> paramMap);

	/**查询已保存医嘱限制性用药信息*/
	List<Map<String,Object>> querySavedQgFitInfo(Map<String,Object> paramMap);

	/**查询全国医保登记信息*/
	List<Map<String,Object>> qryQgybVisitRegInfo(Map<String,Object> paramMap);

}
