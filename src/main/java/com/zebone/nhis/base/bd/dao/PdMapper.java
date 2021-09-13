package com.zebone.nhis.base.bd.dao;

import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 供应链药品Mapper
 * @author c
 *
 */
@Mapper
public interface PdMapper {
	
	/**校验单位是否在药品表中引用**/
	int queryUnitIsCite(String pkUnit);
	
}
