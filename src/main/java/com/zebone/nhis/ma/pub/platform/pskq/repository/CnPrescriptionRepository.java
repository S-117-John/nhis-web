package com.zebone.nhis.ma.pub.platform.pskq.repository;

import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class CnPrescriptionRepository {

    public static CnPrescription getOne(String id){
        String sql = "select * from CN_PRESCRIPTION where PK_PRES = ?";
        CnPrescription cnPrescription = DataBaseHelper.queryForBean(sql,CnPrescription.class,new Object[]{id});
        return cnPrescription;
    }
}
