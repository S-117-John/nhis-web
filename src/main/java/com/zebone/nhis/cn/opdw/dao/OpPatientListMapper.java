package com.zebone.nhis.cn.opdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OpPatientListMapper {
    //获得门诊患者就诊列表待诊患者分诊模式
	public List<Map<String,Object>> getPatientListNormalTriage(Map<String,Object> map) ;
	//获得门诊患者就诊列表待诊患者未分诊模式
	public List<Map<String,Object>> getPatientListNormalNoTriage(Map<String,Object> map) ;
	//获得门诊患者就诊列表已接诊患者
	public List<Map<String,Object>> getPatientListNormalUsed(Map<String,Object> map) ;
	//获得门诊患者就诊列表科室患者
	public List<Map<String,Object>> getPatientListNormalDept(Map<String,Object> map) ;
	//获得门诊患者就诊列表历史就诊患者
	public List<Map<String,Object>> getPatientListHistory(Map<String,Object> map) ;
	public List<Map<String,Object>> getPatientListHistoryOracle(Map<String,Object> map) ;	
	//获得门诊患者资源列表
	public List<Map<String,Object>> getPatientListSource(Map<String,Object> map) ;		
}
