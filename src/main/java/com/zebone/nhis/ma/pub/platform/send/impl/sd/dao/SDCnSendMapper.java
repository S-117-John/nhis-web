package com.zebone.nhis.ma.pub.platform.send.impl.sd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SDCnSendMapper {

	
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
	/**
	 * 门诊重推检查申请
	 * @param paramMap{pkPv:"就诊主键","type":"ip/op"}
	 * @return
	 */
	public List<Map<String,Object>> qryRisAgainInfo(Map<String,Object> paramMap);
	/**
	 * 门诊重推检验申请
	 * @param paramMap{pkPv:"就诊主键","type":"ip/op"}
	 * @return
	 */
	public List<Map<String,Object>> qryLisAgainInfo(Map<String,Object> paramMap);
	

}
