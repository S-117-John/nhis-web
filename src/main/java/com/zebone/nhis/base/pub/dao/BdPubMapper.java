package com.zebone.nhis.base.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BdPubMapper {

	public BdDefdoc queryBdDefDocByCodeAndDefDocList(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> qryUserOper(Map<String,Object> paramMap);
}
