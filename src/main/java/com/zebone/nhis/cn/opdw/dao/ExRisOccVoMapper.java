package com.zebone.nhis.cn.opdw.dao;

import java.util.List;

import com.zebone.nhis.cn.opdw.vo.ExRisOccVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ExRisOccVoMapper {
	
	List<ExRisOccVo> getExRisOccVo(String pkpi);
	List<ExRisOccVo> getExRisOccVoOracle(String pkpi);
}
