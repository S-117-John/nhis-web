package com.zebone.nhis.ma.pub.sd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MedicareDZMapper {

	public List<Map<String, Object>> queryHisMedicalInsSt(Map<String, Object> paramMap);
	
}
