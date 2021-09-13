package com.zebone.nhis.ma.pub.platform.pskq.repository;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class PiMasterRepository {

    public static PiMaster getOne(String id){
        String sql = "select * from PI_MASTER where PK_PI = ?";
        PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class,new Object[]{id});
        return piMaster;
    }
}
