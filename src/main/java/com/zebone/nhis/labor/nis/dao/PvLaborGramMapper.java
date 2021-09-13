package com.zebone.nhis.labor.nis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.labor.nis.PvLaborGram;
import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.nhis.common.module.nd.record.NdRecordRow;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PvLaborGramMapper {

	/**
	 * 查询产程图记录
	 * @param paramMap{pkPv,pkLaborrec}
	 * @return
	 */
	public List<PvLaborGram> queryPvLaborGram(Map<String,Object> paramMap);
	
	
	/**
	 * 查询一般护理记录单行记录
	 * @param pkRecord
	 * @return
	 */
	public List<NdRecordRow> queryNdRecordRow(@Param("pkRecord")String pkRecord);
	
	/**
	 * 查询一般护理记录单列记录
	 * @param pkRecordRow
	 * @return
	 */
	public List<NdRecordDt> queryNdRecordDt(@Param("pkRecordRow")String pkRecordRow);
	/**
	 * 查询分娩方式
	 * @param pkLaborrec
	 * @return
	 */
	public List<Map<String,Object>> queryLaborMode(@Param("pkLaborrec")String pkLaborrec);
	
	
	
}
