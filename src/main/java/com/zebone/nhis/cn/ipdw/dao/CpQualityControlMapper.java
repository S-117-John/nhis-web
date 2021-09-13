package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CpQualityControlMapper {
	//查询路径质控患者分析
	public List<Map<String,Object>> qryQCPatient(Map<String,Object> map) ;
	//查询路径使用状况分析
	public List<Map<String, Object>> qryQCUseState(Map<String, Object> map);
	//查询路径变异分析汇总
	public List<Map<String,Object>> qryQCVariationSum(Map<String,Object> map) ;
	//查询路径变异分析明显
	public List<Map<String, Object>> qryQCVariationDetail(Map<String, Object> map);	
	//查询路径退出分析
	public List<Map<String,Object>> qryQCQuit(Map<String,Object> map) ;
	//查询路径外医嘱列表
	public List<Map<String, Object>> qryQCOutCp(Map<String, Object> map);		
	
}
