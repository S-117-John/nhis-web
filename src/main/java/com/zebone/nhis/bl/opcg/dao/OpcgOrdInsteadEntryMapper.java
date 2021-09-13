package com.zebone.nhis.bl.opcg.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OpcgOrdInsteadEntryMapper {
    List<Map<String, Object>> queryCurEnterPresInfo(Map paramMap);

    List<Map<String, Object>> queryCurEnterLisRisInfo(Map paramMap);

    List<Map<String, Object>> queryCurEnterHerbInfo(Map paramMap);

    Map<String, Object> MasterPlan(Map<String, Object> paramMap);

    List<Map<String,Object>> ExtendedAttributes(String pkInsu);

    List<Map<String,Object>> queryDictAttrByType(Map<String,Object> map);

}
