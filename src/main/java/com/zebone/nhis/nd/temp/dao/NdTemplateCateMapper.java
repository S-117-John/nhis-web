package com.zebone.nhis.nd.temp.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.nd.temp.NdTemplateCate;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface NdTemplateCateMapper {
	/**
	 * 根据机构主键，上级分类，主键查询模板分类信息
	 * @param paramMap{pkOrg,pkFather,pkTemplatecate}
	 * @return
	 */
	public List<NdTemplateCate> queryTemplateCate(Map<String,Object> paramMap);
	
	
	
}
