package com.zebone.nhis.ma.pub.platform.pskq.repository;

import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class PvIpRepository {

    public static PvIp getOne(String id){
        String sql = "select * from pv_ip where PK_PV = ?";
        PvIp pvIp = DataBaseHelper.queryForBean(sql,PvIp.class,new Object[]{id});
        return pvIp;
    }
}
