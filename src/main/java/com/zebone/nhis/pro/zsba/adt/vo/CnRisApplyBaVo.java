package com.zebone.nhis.pro.zsba.adt.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;

public class CnRisApplyBaVo extends CnRisApply {
    public String euOrdtype; //科研医嘱标志

    public String flagFit;//适应症标志

    /**
     * 住院号
     */
    private String codeIp;
    /**
     * 执行科室
     */
    private String pkDeptOcc;
    /**
     * 执行机构
     */
    private String pkOrgOcc;
    /**
     * 就诊主键
     */
    private String pkPv;
    /**
     * 患者主键
     */
    private String pkPi;

    /**
     * 申请单号
     */
    private String codeApply;

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

    public String getCodeIp() {
        return codeIp;
    }

    public void setCodeIp(String codeIp) {
        this.codeIp = codeIp;
    }

    public String getPkDeptOcc() {
        return pkDeptOcc;
    }

    public void setPkDeptOcc(String pkDeptOcc) {
        this.pkDeptOcc = pkDeptOcc;
    }

    public String getPkOrgOcc() {
        return pkOrgOcc;
    }

    public void setPkOrgOcc(String pkOrgOcc) {
        this.pkOrgOcc = pkOrgOcc;
    }

    @Override
    public String getPkPv() {
        return pkPv;
    }

    @Override
    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    @Override
    public String getPkPi() {
        return pkPi;
    }

    @Override
    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    @Override
    public String getCodeApply() {
        return codeApply;
    }

    @Override
    public void setCodeApply(String codeApply) {
        this.codeApply = codeApply;
    }
}
