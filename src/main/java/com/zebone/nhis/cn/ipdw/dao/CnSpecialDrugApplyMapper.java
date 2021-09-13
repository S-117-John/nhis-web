package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnSpecialDrugApplyMapper {
	
	public List<Map<String, Object>> querySpecialDrugApply(Map<String,Object> map);
}
