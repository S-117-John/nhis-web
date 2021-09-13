package com.zebone.nhis.bl.pub.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.bl.pub.vo.BlCgSetDtVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BlCgSetMapper {

	/**
	 * 查询所有收费组套和明细
	 * @param pkCgsets
	 * @return
	 */
	public List<BlCgSetDtVo> qryCgSetDts(@Param("pkCgsets")Set<String> pkCgsets);

}
