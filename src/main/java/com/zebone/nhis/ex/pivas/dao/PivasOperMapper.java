package com.zebone.nhis.ex.pivas.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.pivas.vo.ExPdApplyDetailVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PivasOperMapper {

	List<Map<String, Object>> queryWaitToBeAudittedPdInfos(Map paramMap);

	List<Map<String, Object>> queryToBeConfirmedPdInfos(Map paramMap);
	
	List<ExPdApplyDetailVo> queryPdapDt(Map paramMap);

	List<Map<String, Object>> queryPivas(Map paramMap);

	int updatePivas(Map paramMap);
}
