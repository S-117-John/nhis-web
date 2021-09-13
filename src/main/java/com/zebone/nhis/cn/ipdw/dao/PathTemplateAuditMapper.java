package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PathTemplateAuditMapper {

	//查询路径模板审核列表
	List<Map<String, Object>> qryAudit(Map<String, Object> map);

	//判断路径定义-重点医嘱下替代方式为药理的医嘱的药理分类是否为空
	Integer existNullPharm(@Param("pkCptemp")String pkCptemp);

}
