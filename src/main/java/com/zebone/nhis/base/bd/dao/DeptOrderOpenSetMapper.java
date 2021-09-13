package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface DeptOrderOpenSetMapper {
	
	//院区查询
	List<Map<String, Object>> qryCourt(@Param("pkOrg") String pkOrg);
	
	//院区可开立医嘱查询
	List<Map<String, Object>> qryCourtDetail(@Param("pkOrgarea") String pkOrgarea);
	
	//科室列表查询
	List<Map<String, Object>> qryDept(@Param("pkOrg") String pkOrg);
	
	//科室开立医嘱明细查询
	List<Map<String, Object>> qryDeptDetail(@Param("pkDept") String pkDept);
	
	//医嘱类型列表
	List<Map<String, Object>> qryOrdtype();
	
	//导入查询数据
	List<Map<String, Object>> qryImportList(Map<String, Object> map);

	//查询开立医嘱信息
	List<Map<String,Object>> findOrderList(Map<String, Object> map);

	//科室开立医嘱父节点
	List<Map<String, Object>> qryDeptFather(Map<String,Object> map);
}
