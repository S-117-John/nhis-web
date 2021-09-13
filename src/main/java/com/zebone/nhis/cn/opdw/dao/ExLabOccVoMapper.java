package com.zebone.nhis.cn.opdw.dao;

import java.util.List;

import com.zebone.nhis.cn.opdw.vo.ExLabOccVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ExLabOccVoMapper {
	
	List<ExLabOccVo> getExLabOccVo(String pkpi);
	List<ExLabOccVo> getExLabOccVoOracle(String pkpi);
}
