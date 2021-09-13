package com.zebone.nhis.ex.oi.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OiOccMapper {
	//获得输液处方汇总表
	public List<Map<String,Object>> getOrderSumList(Map<String, Object> paramMap);
    //获得输液处方列表（按类型获得）
	public List<Map<String,Object>> getOrderList(Map<String, Object> paramMap);
	//获得执行明细列表
	public List<Map<String,Object>> getExecList(Map<String, Object> paramMap);
	//获得执行明细操作列表
	public List<Map<String,Object>> getExecDetailList(Map<String, Object> paramMap);
	//获得不良反应列表
	public List<Map<String,Object>> getReactionList(Map<String, Object> paramMap);	
	//获得打印输液卡信息列表
	public List<Map<String,Object>> getPrintCardList(Map<String, Object> paramMap);		
	//获得打印贴瓶单信息列表
	public List<Map<String,Object>> getPrintLabelList(Map<String, Object> paramMap);		
	//更新执行表
	public void UpdateOcc(Map<String, Object> paramMap);
}
