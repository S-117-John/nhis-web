package com.zebone.nhis.cn.opdw.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OpApplyMapper {
	//获得检查检验治疗医嘱列表cn_order
	public List<Map<String,Object>> getApplyRecord(String pkPv);	
	//获得检查医嘱列表cn_ris_apply
	public List<Map<String,Object>> getRisRecord(String pkPv);	
	//获得检查检验治疗医嘱列表cn_lab_apply
	public List<Map<String,Object>> getLabRecord(String pkPv);			
	//获得检查检验治疗医嘱列表cn_lab_apply(Oracle)
	public List<Map<String,Object>> getLabRecordOracle(String pkPv);			
	//获得检查模板医嘱列表
	public List<Map<String,Object>> getRisTemplates();	
	//获得检查模板项目医嘱列表
	public List<Map<String,Object>> getRisTemplatesItem(String PkOrg);		
	//获得检验模板医嘱列表
	public List<Map<String,Object>> getLabTemplates();	
	//获得检验模板项目医嘱列表
	public List<Map<String,Object>> getLabTemplatesItem(String PkOrg);			
	//获得个人常用检查项目医嘱列表
	public List<Map<String,Object>> getRisTemplatePerson(@Param("pk_org")String pk_org,@Param("pk_emp")String pk_emp);
	//获得个人常用检查项目医嘱列表
	public List<Map<String,Object>> getLisTemplatePerson(@Param("pk_org")String pk_org,@Param("pk_emp")String pk_emp);	
}
