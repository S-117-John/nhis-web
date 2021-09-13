package com.zebone.nhis.ma.pub.platform.send.impl.zb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnSendMapper {

	
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
	/**
	 * 医技查询
	 * @param paramMap{pkCnords:"'pk_cnord'","pkCgops":"'pkCgops'"}
	 * @return
	 */
	public List<Map<String,Object>> qryLisOrderRLInfo(Map<String, List<String>> paramMap);
	
	/**
	 * 住院医技查询
	 * @param paramMap{pkCnords:"'pk_cnord'","pkCgops":"'pkCgops'"}
	 * @return
	 */
	public List<Map<String,Object>> qryLisOrderIpInfo(Map<String,List<String>> paramMap);
}
