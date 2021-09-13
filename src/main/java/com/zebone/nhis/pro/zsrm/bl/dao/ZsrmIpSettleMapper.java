package com.zebone.nhis.pro.zsrm.bl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.Mapper;
@Mapper
public interface ZsrmIpSettleMapper {
	/**
	 * 查询退费主键信息
	 * @param pkList
	 * @return
	 */
	public List<String> qryPkCgBack(@Param("pkList") List<String> pkList);

}
