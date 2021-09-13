package com.zebone.nhis.scm.opds.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ScmOpPresQryMapper {
	/**
	 * 查询处方执行明细（发药明细）
	 * @param map{pkPresocc}
	 * @return
	 */
	public List<Map<String,Object>> queryPresOccDetail(Map<String,Object> map);
	/**
	 * 退药处方执行明细
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryPresDt(Map<String,Object> map);
	
	/**
	 * 根据查询参数，获取处方信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPresInfoList(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> queryPresOccList(Map<String,Object> paramMap);
	
}
