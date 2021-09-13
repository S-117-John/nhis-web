package com.zebone.nhis.ma.lb.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CpMsgMapper {

	List<Map<String,Object>> getPiMasterInfo(Map<String, Object> paramMap);
}
