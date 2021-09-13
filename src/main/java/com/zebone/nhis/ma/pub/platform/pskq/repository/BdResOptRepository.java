package com.zebone.nhis.ma.pub.platform.pskq.repository;

import com.zebone.nhis.common.module.base.bd.res.BdResOpt;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class BdResOptRepository {

    public static BdResOpt getOne(String id){
        String sql = "select * from BD_RES_OPT where PK_OPT = ?";
        BdResOpt bdResOpt = DataBaseHelper.queryForBean(sql,BdResOpt.class,new Object[]{id});
        return bdResOpt;
    }
}
