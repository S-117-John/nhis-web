package com.zebone.nhis.ma.pub.platform.pskq.repository;

import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class BdOuDeptRepository {

    public static BdOuDept getOne(String id){
        String sql = "select * from BD_OU_DEPT where PK_DEPT = ? AND DEL_FLAG = '0' ";
        BdOuDept bdOuDept = DataBaseHelper.queryForBean(sql,BdOuDept.class,new Object[]{id});
        return bdOuDept;
    }

    public static BdOuDept findByCodeDept(String codeDept){
        String sql = "select * from BD_OU_DEPT where CODE_DEPT = ? AND DEL_FLAG = '0' ";
        BdOuDept bdOuDept = DataBaseHelper.queryForBean(sql,BdOuDept.class,new Object[]{codeDept});
        return bdOuDept;
    }
}
