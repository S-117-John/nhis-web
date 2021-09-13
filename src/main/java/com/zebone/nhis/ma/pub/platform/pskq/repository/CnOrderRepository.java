package com.zebone.nhis.ma.pub.platform.pskq.repository;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.springframework.stereotype.Component;



public class CnOrderRepository {

    public static CnOrder findByOrdsn(Integer ordsn){
        String sql = "select * from CN_ORDER where ORDSN = ?";
        CnOrder cnOrder = DataBaseHelper.queryForBean(sql,CnOrder.class,new Object[]{ordsn});
        return cnOrder;
    }

    public static CnOrder getOne(String id){
        String sql = "select * from CN_ORDER where PK_CNORD = ?";
        CnOrder cnOrder = DataBaseHelper.queryForBean(sql,CnOrder.class,new Object[]{id});
        return cnOrder;
    }
}
