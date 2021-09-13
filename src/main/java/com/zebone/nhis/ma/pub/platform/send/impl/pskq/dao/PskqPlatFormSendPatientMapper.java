package com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao;

import com.zebone.nhis.ma.pub.platform.pskq.model.Patient;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.Map;

@Mapper
public interface PskqPlatFormSendPatientMapper {

    /**
     * 获取患者信息
     * @param map
     * @return
     */
    Patient getPiMasterById(Map<String,Object> map);

    int upatePiMasterByCodePi(Patient patient);
}
