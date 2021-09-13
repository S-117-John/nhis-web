package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;

public class CnRisApplyVo extends CnRisApply {
    public String euOrdtype; //科研医嘱标志

    public String flagFit;//适应症标志

    public String getEuOrdtype() {
        return euOrdtype;
    }

    public void setEuOrdtype(String euOrdtype) {
        this.euOrdtype = euOrdtype;
    }

    public String getFlagFit() {
        return flagFit;
    }

    public void setFlagFit(String flagFit) {
        this.flagFit = flagFit;
    }
}
