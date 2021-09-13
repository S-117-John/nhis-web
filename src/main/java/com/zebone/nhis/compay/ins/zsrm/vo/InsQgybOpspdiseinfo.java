package com.zebone.nhis.compay.ins.zsrm.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 门诊特慢病诊断信息
 */
public class InsQgybOpspdiseinfo {
    @JSONField(name = "diag_name")
    private String diagName;
    @JSONField(name = "diag_code")
    private String diagCode;
    /**
     * 手术操作名称
     */
    @JSONField(name = "oprn_oprt_name")
    private String oprnOprtName;
    /**
     * 手术操作代码
     */
    @JSONField(name = "oprn_oprt_code")
    private String oprnOprtCode;

    public String getDiagName() {
        return diagName;
    }

    public void setDiagName(String diagName) {
        this.diagName = diagName;
    }

    public String getDiagCode() {
        return diagCode;
    }

    public void setDiagCode(String diagCode) {
        this.diagCode = diagCode;
    }

    public String getOprnOprtName() {
        return oprnOprtName;
    }

    public void setOprnOprtName(String oprnOprtName) {
        this.oprnOprtName = oprnOprtName;
    }

    public String getOprnOprtCode() {
        return oprnOprtCode;
    }

    public void setOprnOprtCode(String oprnOprtCode) {
        this.oprnOprtCode = oprnOprtCode;
    }
}
