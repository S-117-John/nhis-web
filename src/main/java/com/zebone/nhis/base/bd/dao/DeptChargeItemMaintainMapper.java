package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.base.bd.vo.MaintainVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface DeptChargeItemMaintainMapper {
	
	//查询主页数据
	public List<MaintainVo> qryDeptChargeItemMaintain(Map<String, Object> map);

	//查询导入数据
	public List<Map<String, Object>> qryImportDeptChargeItemMaintain(
			Map<String, Object> map);
}
