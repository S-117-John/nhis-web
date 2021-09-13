package com.zebone.nhis.webservice.syx.support;

import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

import java.util.List;

public class SetUserUtils {
    /**
     * 给UserContext赋值
     *
     * @param oldId
     */
    public static User setUserByOldId(String oldId) {
        List<User> users = DataBaseHelper.queryForList("select * from bd_ou_user bu inner join bd_ou_employee emp on emp.pk_emp=bu.pk_emp where emp.old_id=? and bu.is_lock='0' and bu.flag_active='1'", User.class, oldId);
        if (users == null || users.size() <= 0) {
            throw new BusException("当前用户没有访问权限，请在NHIS系统维护该用户的账号！");
        }
        UserContext.setUser(users.get(0));
        return users.get(0);
    }

    /**
     * 患者编码
     * @param empCode
     */
    public static User setUserByEmpCode(String empCode) {
        List<User> users = DataBaseHelper.queryForList("select * from bd_ou_user bu inner join bd_ou_employee emp on emp.pk_emp=bu.pk_emp where emp.code_emp=? and bu.is_lock='0' and bu.flag_active='1'", User.class, empCode);
        if (users == null || users.size() <= 0) {
            throw new BusException("当前用户没有访问权限，请在NHIS系统维护该用户的账号！");
        }
        UserContext.setUser(users.get(0));
        return users.get(0);
    }
}
