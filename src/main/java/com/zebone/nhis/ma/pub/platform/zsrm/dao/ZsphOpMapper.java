package com.zebone.nhis.ma.pub.platform.zsrm.dao;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.PvEncounterVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsphOpMapper {
    List<PvEncounterVo> getPvEncounter(Map<String, Object> paramMap);
    List<Map<String,Object>> qryDaigByCode(@Param("code") String code);
}
