package com.zebone.nhis.ex.oi.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.oi.vo.BdPacleIvVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OiRegisterMapper {
	//插入门诊处方信息到输液登记临时表
	void InsertOiRegisterTempList(Map<String, Object> paramMap);
	//获得门诊输液患者列表（单一患者）
    public List<Map<String,Object>> getOiRegisterPatientList(Map<String, Object> paramMap);
	//获得患者列表
	public List<Map<String,Object>> getPatientList(Map<String, Object> paramMap);		
	//获得未治疗处方列表
	public List<Map<String,Object>> getOrderList(Map<String, Object> paramMap);		
	//获得处方收费列表
	public List<Map<String,Object>> getPresChargeList(Map<String, Object> paramMap);	
	//获得处方收费信息
	public Map<String,Object> getPresChargeVo(Map<String, Object> paramMap);
	//获得已登记输液患者列表
	public List<Map<String,Object>> getRegisteredPatientList(Map<String, Object> paramMap);
	//获得空闲的座位列表
	public List<Map<String,Object>> getRegPlaceIvList(Map<String, Object> map);
}
