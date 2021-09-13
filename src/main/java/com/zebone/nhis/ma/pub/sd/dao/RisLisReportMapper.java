package com.zebone.nhis.ma.pub.sd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface RisLisReportMapper {

	//查询在院患者列表
	public List<Map<String, Object>> qryPatientList(Map<String, Object> map);

	//查询出院患者列表
	public List<Map<String, Object>> qryLeavePatientList(Map<String, Object> map);
	
	//查询转科患者列表
	public List<Map<String, Object>> qryChangePatientList(Map<String, Object> map);
	
	//查询普通检验报告
	public List<Map<String,Object>> qrySughLisReportCommon(Map<String,Object> map);
	
	//查询微生物检验报告
	public List<Map<String,Object>> qrySughLisReportWsw(Map<String,Object> map);
}
