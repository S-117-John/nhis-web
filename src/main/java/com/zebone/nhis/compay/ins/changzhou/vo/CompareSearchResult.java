package com.zebone.nhis.compay.ins.changzhou.vo;

import java.util.Date;

/**
 * 查询匹配信息返回结果
 */
public class CompareSearchResult {
    /**
     * 匹配主键
     */
    private String id;
    /**
     * his项目id
     */
    private String sfxmid;
    /**
     * his项目编码
     */
    private String code;
    /**
     * 疾病icd编码
     */
    private String icdCode;
    /**
     * his项目名称
     */
    private String name;
    /**
     * 价格
     */
    private String price;
    /**
     * 规格
     */
    private String spec;
    /**
     * 单位
     */
    private String dw;
    /**
     * 生产厂家名称
     */
    private String factoryName;
    /**
     * 拼音码
     */
    private String pym;
    /**
     * 医保编码
     */
    private String ybbm;
    /**
     * 医保名称
     */
    private String ybmc;
    /**
     * 收费项目等级
     */
    private String sfxmdj;
    /**
     * 修改人
     */
    private String modifier;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * his项目类别
     */
    private String xmlb;
    /**
     * 医保收费类别
     */
    private String sflb;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSfxmid() {
        return sfxmid;
    }

    public void setSfxmid(String sfxmid) {
        this.sfxmid = sfxmid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getPym() {
        return pym;
    }

    public void setPym(String pym) {
        this.pym = pym;
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

    public String getSfxmdj() {
        return sfxmdj;
    }

    public void setSfxmdj(String sfxmdj) {
        this.sfxmdj = sfxmdj;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getIcdCode() {
        return icdCode;
    }

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }
}
