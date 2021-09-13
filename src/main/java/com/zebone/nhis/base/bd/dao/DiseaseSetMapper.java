package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface DiseaseSetMapper {
	
	/**查询病种定义信息*/
	List<Map<String, Object>> qryDiseaseInfo(Map<String,Object> paramMap);
	
	/**根据病种信息主键查询关联的医保信息*/
	List<Map<String,Object>> qryHpByPkDiseae(@Param("pkGzgydisease") String pkGzgydisease);
	
	/**根据病种信息主键查询关联药品信息*/
	List<Map<String,Object>> qryOrdByPkDiseae(@Param("pkGzgydisease") String pkGzgydisease);
	
	/**查询待选公费医保信息*/
	List<Map<String,Object>> qryWaitChooseHp(Map<String,Object> paramMap);
	
	/**查询已选公费医保信息*/
	List<Map<String,Object>> qrySelectedHp(Map<String,Object> paramMap);
	
}
