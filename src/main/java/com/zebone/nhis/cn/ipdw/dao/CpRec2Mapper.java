package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CpRec2Mapper {
	
	//根据诊断查询可用的临床路径模板
	public List<Map<String,Object>> qryCpTempsByICD(Map<String,Object> paramMap);
	
	//查询路径模板的阶段
	public List<Map<String,Object>> qryCpTempPhase(Map<String,Object> paramMap);
	
	//查询路径医嘱
	public List<Map<String,Object>> qryCpOrd(Map<String,Object> paramMap);
	
	//查询变异记录
	public List<Map<String,Object>> qryCpExp(Map<String,Object> paramMap);
	
	//查询表单数据
	public List<Map<String,Object>> qryCpFormItem(Map<String,Object> paramMap);
}
