package com.zebone.nhis.common.dao;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BdOuPubMapper {
	
	/** 根据科室主键获取仓库主键 */
	String getPkStoreByPkDept(String pkDept);

}
