package com.zebone.nhis.compay.ins.zsrm.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 住院诊断信息
 */
public class InsQgybDiseinfo {
    @JSONField(name = "diag_type")
    private String diagType;
    @JSONField(name = "diag_code")
    private String diagCode;
    @JSONField(name = "diag_name")
    private String diagName;
    @JSONField(name = "adm_cond_type")
    private String admCondType;

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
