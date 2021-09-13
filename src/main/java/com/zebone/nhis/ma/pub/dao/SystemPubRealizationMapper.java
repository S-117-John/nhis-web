package com.zebone.nhis.ma.pub.dao;

import com.zebone.nhis.ma.pub.vo.AssistOccVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SystemPubRealizationMapper {

	public List<AssistOccVo> qryAssistOcc(List<String> pkCnords);

    List<Map<String, Object>>  querySendDrugAgainData(Map<String, Object> qryParam);
}
