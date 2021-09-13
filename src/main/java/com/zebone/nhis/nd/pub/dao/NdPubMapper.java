package com.zebone.nhis.nd.pub.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.nhis.nd.pub.vo.EmrTemplateVo;
import com.zebone.nhis.nd.pub.vo.NdRecordRowVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface NdPubMapper {
	/**
	 * 查询患者病历列表
	 * @param pkPv
	 * @return
	 */
	public List<Map<String,Object>> queryRecordList(@Param("pkPv")String pkPv);
	
	/**
	 * 查询护理记录单对应的模板
	 * @param pkRecord
	 * @return
	 */
	public EmrTemplateVo queryRecordTemp(@Param("pkTmp")String pkTmp);
	/**
	 * 查询护理记录单对应的模板
	 * @param pkRecord
	 * @return
	 */
	public EmrTemplateVo queryRecordXml(Map<String,Object> param);
	
	/**
	 * 查询病历动态列表头
	 * @param pkRecord
	 * @return
	 */
	public List<Map<String,Object>> queryRecordDcList(@Param("pkRecord")String pkRecord);
	
	/**
	 * 查询病历行记录
	 * @param pkRecord
	 * @return
	 */
	public List<NdRecordRowVo> queryRecordRowList(Map<String,Object> param);
	
	/**
	 * 查询病历列记录
	 * @param pkRecord
	 * @return
	 */
	public List<NdRecordDt> queryRecordColDtList(Map<String,Object> paramMap);
	
}
