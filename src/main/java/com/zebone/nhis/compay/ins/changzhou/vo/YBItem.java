package com.zebone.nhis.compay.ins.changzhou.vo;

/**
 * 医保项目
 */
public class YBItem {
    /**
     * his项目id
     */
    private String hisId;
    /**
     * 医院收费项目编码
     */
    private String hisbm;
    /**
     * 医院收费项目名称
     */
    private String hismc;
    /**
     * 医保编码
     */
    private String ybbm;
    /**
     * 医保名称
     */
    private String ybmc;
    /**
     * 收费项目类别
     */
    private String xmlb;
    /**
     * 收费类别
     */
    private String sflb;
    /**
     * 医保项目等级
     */
    private String ybxmdj;
    /**
     * 是否医保项目
     */
    private String sfybxm;

    public String getHisId() {
        return hisId;
    }

    public void setHisId(String hisId) {
        this.hisId = hisId;
    }

    public String getYbbm() {
        return ybbm;
    }

    public void setYbbm(String ybbm) {
        this.ybbm = ybbm;
    }

    public String getYbmc() {
        return ybmc;
    }

    public void setYbmc(String ybmc) {
        this.ybmc = ybmc;
    }

    public String getXmlb() {
        return xmlb;
    }

    public void setXmlb(String xmlb) {
        this.xmlb = xmlb;
    }

    public String getSflb() {
        return sflb;
    }

    public void setSflb(String sflb) {
        this.sflb = sflb;
    }

    public String getSfybxm() {
        return sfybxm;
    }

    public void setSfybxm(String sfybxm) {
        this.sfybxm = sfybxm;
    }

    public String getHisbm() {
        return hisbm;
    }

    public void setHisbm(String hisbm) {
        this.hisbm = hisbm;
    }

    public String getHismc() {
        return hismc;
    }

    public void setHismc(String hismc) {
        this.hismc = hismc;
    }

    public String getYbxmdj() {
        return ybxmdj;
    }

    public void setYbxmdj(String ybxmdj) {
        this.ybxmdj = ybxmdj;
    }
}
