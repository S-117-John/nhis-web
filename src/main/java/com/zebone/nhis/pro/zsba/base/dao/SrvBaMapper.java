package com.zebone.nhis.pro.zsba.base.dao;

import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SrvBaMapper {

	/**
	 * 更新
	 * @param map
	 */
	public void updateOrdBypk(Map<String, String> map);
}
