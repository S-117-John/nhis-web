package com.zebone.nhis.ma.lb.vo;

import com.zebone.nhis.common.module.ma.sms.SmsTemp;
import com.zebone.nhis.common.module.ma.sms.SmsTempSt;

import java.util.List;

public class SmsTempVo extends SmsTemp {

    private List<SmsTempSt> stList;

    public List<SmsTempSt> getStList() {
        return stList;
    }

    public void setStList(List<SmsTempSt> stList) {
        this.stList = stList;
    }
}
