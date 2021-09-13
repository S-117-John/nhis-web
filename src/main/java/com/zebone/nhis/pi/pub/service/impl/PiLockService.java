package com.zebone.nhis.pi.pub.service.impl;

import com.zebone.nhis.common.module.pi.PiLock;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PiLockService {

    /**
     * 就诊前查询欠费
     * @param param
     * @param user
     * @return
     */
    public List<PiLock> checkArrearsBeforeSeeDoctor(String param, IUser user){
        String pkPi = JsonUtil.getFieldValue(param,"pkPi");
        String sql = "select lk.* from pi_lock lk where lk.pk_pi = ? and lk.eu_locktype = '3' and lk.del_flag = '0'";
        List<PiLock> piLockList = DataBaseHelper.queryForList(sql,PiLock.class,new Object[]{pkPi});
        if(piLockList.size()>0){
            throw new BusException("患者在黑名单："+piLockList.get(0).getNote());
        }
        return piLockList;
    }
}
