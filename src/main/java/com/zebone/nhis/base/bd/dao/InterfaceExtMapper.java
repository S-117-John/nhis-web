package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.wf.BdInterfaceExt;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InterfaceExtMapper {
	
	public List<BdInterfaceExt> qryInf(Map<String, String> map);
	
	public void delInf(String pkInterface);
}
