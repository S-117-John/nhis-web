package com.zebone.nhis.base.bd.dao;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.srv.BdQcRule;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface QcRuleMapper {

	/**
	 * 查询分诊规则
	 * @param pkOrg
	 * @return
	 */
	public List<BdQcRule> qryQcRule(String pkOrg);

}
