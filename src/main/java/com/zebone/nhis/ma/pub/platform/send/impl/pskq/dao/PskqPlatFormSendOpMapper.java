package com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao;

import com.zebone.nhis.ma.pub.platform.pskq.model.*;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.Map;

/**
 *门诊就诊信息
 */
@Mapper
public interface PskqPlatFormSendOpMapper {
    /**
     * 获取挂号信息
     * @param map
     * @return
     */
    Outpatient getRegisterEncounterOp(Map<String, Object> map);
    /**
     * 获取退挂号信息
     * @param map
     * @return
     */
    Outpatient getRetreatEncounterOp(Map<String, Object> map);

    /**
     * 获取患者信息
     * @param map
     * @return
     */
    PatientInfo getPiMasterById(Map<String,Object> map);
}
