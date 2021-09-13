package com.zebone.nhis.nd.temp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.nd.temp.NdTemplateDept;
import com.zebone.nhis.nd.temp.vo.NdTemplateVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface NdTemplateMapper {
	/**
	 * 根据模板分类查询模板
	 * @param paramMap{pkTemplatecate}
	 * @return
	 */
	public List<NdTemplateVo> queryTemplate(@Param("pkTemplatecate")String pkTemplatecate);
	
	/**
	 * 根据模板查询模板使用科室
	 * @param paramMap{pkTemplate}
	 * @return
	 */
	public List<NdTemplateDept> queryTemplateDept(@Param("pkTemplate")String pkTemplate);
	
	
	
}
