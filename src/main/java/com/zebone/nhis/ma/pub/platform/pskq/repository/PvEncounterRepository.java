package com.zebone.nhis.ma.pub.platform.pskq.repository;

import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class PvEncounterRepository {

    public static PvEncounter getOne(String id){
        String sql = "select * from PV_ENCOUNTER where PK_PV = ?";
        PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{id});
        return pvEncounter;
    }

    public static PvEncounter findByCodePv(String codePv){
        String sql = "select * from PV_ENCOUNTER where code_pv = ? and del_flag = '0' ";
        PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{codePv});
        return pvEncounter;
    }
}
