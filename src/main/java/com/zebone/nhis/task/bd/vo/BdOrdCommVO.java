package com.zebone.nhis.task.bd.vo;

import com.zebone.nhis.common.module.cn.ipdw.BdOrdComm;

public class BdOrdCommVO  extends BdOrdComm {

    private String key;

    public String getKey() {
        return getPkEmp()+getPkOrd();
    }

    public void setKey(String key) {
        this.key = key;
    }
}
