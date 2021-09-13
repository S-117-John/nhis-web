package com.zebone.nhis.compay.ins.changzhou.vo;

/**
 *处方明细信息
 */
public class YBCfmx {
    /**
     * 项目类别
     */
    private String xmlb;
    /**
     * 费用类别
     */
    private String fylb;
    /**
     * 处方号
     */
    private String cfh;
    /**
     * 处方日期
     */
    private String cfrq;
    /**
     * 医院收费项目内码
     */
    private String yysfxmnm;
    /**
     * 收费项目中心编码
     */
    private String sfxmzxbm;
    /**
     * 医院收费项目名称
     */
    private String yysfxmmc;
    /**
     * 单价
     */
    private String dj;
    /**
     * 数量
     */
    private String sl;
    /**
     * 医生姓名
     */
    private String ysxm;
    /**
     * 处方医师编码
     */
    private String cfysbm;
    /**
     * 就诊ID
     */
    private String jzid;
    /**
     * 科室编号
     */
    private String ksbh;
    /**
     * 科室名称
     */
    private String ksmc;
    /**
     * HIS费用明细主键
     */
    private String pkCgOp;

    /**
     * HIS新的费用明细主键
     */
    private String pkCgNew;

    public String getPkCgNew() {
        return pkCgNew;
    }

    public void setPkCgNew(String pkCgNew) {
        this.pkCgNew = pkCgNew;
    }

    public String getPkCgOp() {
        return pkCgOp;
    }

    public void setPkCgOp(String pkCgOp) {
        this.pkCgOp = pkCgOp;
    }

    public String getXmlb() {
        return xmlb;
    }

    public void setXmlb(String xmlb) {
        this.xmlb = xmlb;
    }

    public String getFylb() {
        return fylb;
    }

    public void setFylb(String fylb) {
        this.fylb = fylb;
    }

    public String getCfh() {
        return cfh;
    }

    public void setCfh(String cfh) {
        this.cfh = cfh;
    }

    public String getCfrq() {
        return cfrq;
    }

    public void setCfrq(String cfrq) {
        this.cfrq = cfrq;
    }

    public String getYysfxmnm() {
        return yysfxmnm;
    }

    public void setYysfxmnm(String yysfxmnm) {
        this.yysfxmnm = yysfxmnm;
    }

    public String getSfxmzxbm() {
        return sfxmzxbm;
    }

    public void setSfxmzxbm(String sfxmzxbm) {
        this.sfxmzxbm = sfxmzxbm;
    }

    public String getYysfxmmc() {
        return yysfxmmc;
    }

    public void setYysfxmmc(String yysfxmmc) {
        this.yysfxmmc = yysfxmmc;
    }

    public String getDj() {
        return dj;
    }

    public void setDj(String dj) {
        this.dj = dj;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getYsxm() {
        return ysxm;
    }

    public void setYsxm(String ysxm) {
        this.ysxm = ysxm;
    }

    public String getCfysbm() {
        return cfysbm;
    }

    public void setCfysbm(String cfysbm) {
        this.cfysbm = cfysbm;
    }

    public String getJzid() {
        return jzid;
    }

    public void setJzid(String jzid) {
        this.jzid = jzid;
    }

    public String getKsbh() {
        return ksbh;
    }

    public void setKsbh(String ksbh) {
        this.ksbh = ksbh;
    }

    public String getKsmc() {
        return ksmc;
    }

    public void setKsmc(String ksmc) {
        this.ksmc = ksmc;
    }
}
