package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnCchi;

import java.util.List;

public class BdTermCchiSaveParamVo extends CnCchi {
    private String tlname;
    private List<CnCchi> cnCchiList;
    private List<CnCchi> delCnCchiList;

    public String getTlname() {
        return tlname;
    }

    public void setTlname(String tlname) {
        this.tlname = tlname;
    }

    public List<CnCchi> getCnCchiList() {
        return cnCchiList;
    }

    public void setCnCchiList(List<CnCchi> cnCchiList) {
        this.cnCchiList = cnCchiList;
    }

    public List<CnCchi> getDelCnCchiList() {
        return delCnCchiList;
    }

    public void setDelCnCchiList(List<CnCchi> delCnCchiList) {
        this.delCnCchiList = delCnCchiList;
    }
}
