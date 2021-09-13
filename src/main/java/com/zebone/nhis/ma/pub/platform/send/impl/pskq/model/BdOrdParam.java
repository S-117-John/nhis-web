package com.zebone.nhis.ma.pub.platform.send.impl.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;

public class BdOrdParam {

    @JSONField(name = "STATUS")
    private String status;

    private BdOrd bdOrd;

    private String pkOrg;

    private String codeEmp;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BdOrd getBdOrd() {
        return bdOrd;
    }

    public void setBdOrd(BdOrd bdOrd) {
        this.bdOrd = bdOrd;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getCodeEmp() {
        return codeEmp;
    }

    public void setCodeEmp(String codeEmp) {
        this.codeEmp = codeEmp;
    }
}
