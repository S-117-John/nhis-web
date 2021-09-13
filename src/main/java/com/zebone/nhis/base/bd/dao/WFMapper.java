package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.wf.BdWfcate;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface WFMapper {
	
	int BdWfcateCheckExist(Map<String,String> params);
	
	List<String> BdWfSelectPkWf(Map<String,Object> params);

	List<BdWfcate> BdWfCateSelectAll(Map<String, Object> params);

	int BdWfCheckExist(Map<String, String> params);

	int BdWfOrdArguCheckExist(Map<String, String> params);
	
}
