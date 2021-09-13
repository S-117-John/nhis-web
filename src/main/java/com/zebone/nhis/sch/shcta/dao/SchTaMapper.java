package com.zebone.nhis.sch.shcta.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SchTaMapper {
	public List<Map<String,Object>> qrySchTaByParam(Map<String,Object> param);
	public List<Map<String,Object>> qrySchTaEmp(Map<String,Object> map);
	public List<Map<String,Object>> qrySchTaDept(String pkOrg);
	public List<Map<String,Object>> qryDeptSchTa(@Param("pkOrg")String pkOrg,@Param("dateBegin")String dateBegin,@Param("dateEnd")String dateEnd,@Param("pkDeptList")List<String>pkDeptList);
}
