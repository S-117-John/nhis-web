package com.zebone.nhis.pro.zsba.mz.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ZsbaBlOtherPayMapper {
	public List<Map<String,Object>> queryBlOtherPayList(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> queryBlOtherPayReport(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> queryBlOtherPayRefundDetail(Map<String,Object> paramMap);
	
	public void blOtherPayDayEnd(Map<String,Object> paramMap);
}