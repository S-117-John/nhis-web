package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.cn.ipdw.vo.OrdCaVo;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.emr.rec.tmp.EmrTemplateVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnReqMapper {

	public List<Map<String,Object>> queryCnOrderRisByPv(String pk_pv) ;
	public List<Map<String,Object>> queryRisUhSrvtype(Map<String,Object> paramMap);
	public List<Map<String,Object>> queryItemBySrv(@Param("pk_org")String pk_org, @Param("pk_ord")String pk_ord);
	public List<Map<String,Object>> queryRisBdUhSrvPsn(@Param("pk_org")String pk_org,@Param("pk_emp")String pk_emp);
	public List<Map<String,Object>> queryCnOrderLisByPv(String pk_pv);
	public List<Map<String,Object>> queryLisUhSrvType(Map<String,Object> paramMap);
	public List<Map<String,Object>> queryLisBdUhSrvPsn(@Param("pk_org")String pk_org,@Param("pk_emp")String pk_emp);
	public List<Map<String,Object>> qryDtBody();
	public List<Map<String,Object>> qryRisLisTopUseOrd(Map<String,Object> paramMap);
	/**
	 * 检查申请
	 * @param paramMap{pkPv:"就诊主键","type":"ip/op"}
	 * @return
	 */
	public List<Map<String,Object>> qryRisInfo(Map<String,Object> paramMap);
	/**
	 * 检验申请
	 * @param paramMap{pkPv:"就诊主键","type":"ip/op"}
	 * @return
	 */
	public List<Map<String,Object>> qryLisInfo(Map<String,Object> paramMap);
	

	//查询检查申请单模板
	public List<EmrTemplateVo> qryRisForm(Map<String,Object> paramMap) ;
	
	//修改检查申请单
	public int modifyRisForm(CnRisApply cnRisApply) ;
	
	/** 根据pk_cnord查询用户CA电子签名*/
	public OrdCaVo qryOrdCaByPkcnord(Map<String,Object> paramMap);
	
	/** 根据pk_cnord查询用户CA电子签名*/
	public OrdCaVo qryImgCaByPkEmp(Map<String,Object> paramMap);
	
	
}
