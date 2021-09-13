package com.zebone.nhis.compay.pub.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class InsQgybSetlDiseinfo {
    @JSONField(name = "diag_type")
    private String diagType;
    @JSONField(name = "diag_code")
    private String diagCode;
    @JSONField(name = "diag_name")
    private String diagName;
    @JSONField(name = "adm_cond_type")
    private String admCondType;
    @JSONField(name = "maindiag_flag")
    private String maindiagFlag;

    public String getMaindiagFlag() {
        return maindiagFlag;
    }

    public void setMaindiagFlag(String maindiagFlag) {
        this.maindiagFlag = maindiagFlag;
    }

    public String getDiagType() {
        return diagType;
    }

    public void setDiagType(String diagType) {
        this.diagType = diagType;
    }

    public String getDiagCode() {
        return diagCode;
    }

    public void setDiagCode(String diagCode) {
        this.diagCode = diagCode;
    }

    public String getDiagName() {
        return diagName;
    }

    public void setDiagName(String diagName) {
        this.diagName = diagName;
    }

    public String getAdmCondType() {
        return admCondType;
    }

    public void setAdmCondType(String admCondType) {
        this.admCondType = admCondType;
    }
}
