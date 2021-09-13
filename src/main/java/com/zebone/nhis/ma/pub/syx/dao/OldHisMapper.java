package com.zebone.nhis.ma.pub.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ma.pub.syx.vo.ExOpSchVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OldHisMapper {
	
	public List<ExOpSchVo> qryOpApplys(Map<String,Object> paramMap);


	public List<ExOpSchVo>  qryOpApplysHIS(Map<String, Object> paramMap);
	
	/**
	 * 查询中间表感染标志
	 * @param paramMap
	 * @return
	 */
	public Integer qryInfectedFlag(Map<String, Object> paramMap);
}
