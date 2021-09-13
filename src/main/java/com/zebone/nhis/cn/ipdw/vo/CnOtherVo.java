package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

import java.util.List;

public class CnOtherVo {

    //医嘱表数据
    private List<CnOrder> cnList;
    //医嘱备份表数据
    private List<CnOrder> cnListBack;

    public List<CnOrder> getCnList() {
        return cnList;
    }

    public void setCnList(List<CnOrder> cnList) {
        this.cnList = cnList;
    }

    public List<CnOrder> getCnListBack() {
        return cnListBack;
    }

    public void setCnListBack(List<CnOrder> cnListBack) {
        this.cnListBack = cnListBack;
    }
}
