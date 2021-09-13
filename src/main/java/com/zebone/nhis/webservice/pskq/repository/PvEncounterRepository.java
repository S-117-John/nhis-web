package com.zebone.nhis.webservice.pskq.repository;

import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class PvEncounterRepository {

    public static PvEncounter findByCode(String code){
        String sql = "select * from PV_ENCOUNTER where CODE_PV = ?";
        PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{code});
        return pvEncounter;
    }
}
