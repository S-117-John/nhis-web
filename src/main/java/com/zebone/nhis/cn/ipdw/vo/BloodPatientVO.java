package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnTransApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;

import java.util.List;

public class BloodPatientVO extends CnTransApply {
    /* 输血申请单对应的检查项目单 */
    private List<ExLabOcc> exOcc;

    public List<ExLabOcc> getExOcc() {
        return exOcc;
    }

    public void setExOcc(List<ExLabOcc> exOcc) {
        this.exOcc = exOcc;
    }
}
