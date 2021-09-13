package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.wf.BdOrdAutoexec;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OrdAutorunMapper {
	
	/**查询医嘱自动执行设置*/
	public List<BdOrdAutoexec> qryOrdAutoexec(Map<String,Object> paramMap);
	
}
