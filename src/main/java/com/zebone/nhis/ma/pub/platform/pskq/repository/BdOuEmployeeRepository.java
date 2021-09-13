package com.zebone.nhis.ma.pub.platform.pskq.repository;

import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class BdOuEmployeeRepository {
    public static BdOuEmployee getOne(String id){
        String sql = "select * from bd_ou_employee where pk_emp = ? AND DEL_FLAG = '0' ";
        BdOuEmployee bdOuEmployee = DataBaseHelper.queryForBean(sql, BdOuEmployee.class,new Object[]{id});
        return bdOuEmployee;
    }


    public static BdOuEmployee findByCodeEmp(String codeEmp){
        String sql = "select * from bd_ou_employee where code_emp = ? AND DEL_FLAG = '0' ";
        BdOuEmployee bdOuEmployee = DataBaseHelper.queryForBean(sql,BdOuEmployee.class,new Object[]{codeEmp});
        return bdOuEmployee;
    }


}
