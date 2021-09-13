package com.zebone.nhis.pv.adt.dao;

import com.zebone.nhis.pv.adt.vo.PatientPvVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 入院患者管理mapper
 * 
 * @author wangpeng
 * @date 2016年12月12日
 *
 */
@Mapper
public interface AdtPiSearhMapper {
	
	//查询患者就诊信息
	List<PatientPvVo> searchPv(Map<String, Object> paramap);

	//查询患者处方信息
	List<Map<String,Object>> searchPress(Map<String, Object> paramap);

	//门诊患者诊断查询
	List<Map<String,Object>> queryPvDiag(Map<String, Object> paramap);

	//住院患者诊断查询
	List<Map<String,Object>> queryCnDiag(Map<String, Object> paramap);

	//查询患者医嘱信息
	List<Map<String, Object>> queryPatiOrdList(Map<String, Object> newMap);

	//查询患者手术信息
	List<Map<String,Object>> queryPatiOpList(Map<String, Object> paramMap);
}
