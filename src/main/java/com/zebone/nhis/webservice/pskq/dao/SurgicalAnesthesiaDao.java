package com.zebone.nhis.webservice.pskq.dao;


import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author 卡卡西
 */
@Mapper
public interface SurgicalAnesthesiaDao {


    List<Map<String,Object>> refundableItems(Map<String,Object> paramMap);

    List<BlIpDt> findBlIpDt(List<String> pkCgip);

    List<Map<String,Object>> findBlIpDtCount(List<String> pkCgip);

    List<Map<String,Object>> findPvEncounter(String pkPv);

    String findValAttr(String pkPv);
}
