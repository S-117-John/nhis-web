package com.zebone.nhis.compay.ins.changzhou.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

@Table(value = "INS_CZYB_COMPARE")
public class YBCompareInfo extends BaseModule {
    /**
     * 医保对照主键
     */
    @PK
    @Field(value = "ID", id = Field.KeyId.UUID)
    private String id;
    /**
     * his项目主键
     */
    @Field("SFXMID")
    private String sfxmid;
    /**
     * his项目编码
     */
    @Field("SFXMBM")
    private String sfxmbm;
    /**
     * his项目名称
     */
    @Field("SFXMMC")
    private String sfxmmc;
    /**
     * 医保项目编码
     */
    @Field("YBBM")
    private String ybbm;
    /**
     * 医保类型
     */
    @Field("YBLX")
    private String yblx;
    /**
     * 医保项目类别
     */
    @Field("XMLB")
    private String xmlb;
    /**
     * 医保收费类别
     */
    @Field("SFLB")
    private String sflb;
    /**
     * 医保项目等级
     */
    @Field("YBXMDJ")
    private String ybxmdj;
    /**
     * 医保名称
     */
    @Field("YBMC")
    private String ybmc;
    /**
     * 医院ICD编码
     */
    @Field("ICD_CODE")
    private String icdCode;

    /**
     * 最后操作时间
     */
    @Field("MODIFY_TIME")
    private Date modifyTime;

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

    public String getSfxmbm() {
        return sfxmbm;
    }

    public void setSfxmbm(String sfxmbm) {
        this.sfxmbm = sfxmbm;
    }

    public String getSfxmmc() {
        return sfxmmc;
    }

    public void setSfxmmc(String sfxmmc) {
        this.sfxmmc = sfxmmc;
    }

    public String getYbbm() {
        return ybbm;
    }

    public void setYbbm(String ybbm) {
        this.ybbm = ybbm;
    }

    public String getYblx() {
        return yblx;
    }

    public void setYblx(String yblx) {
        this.yblx = yblx;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getYbxmdj() {
        return ybxmdj;
    }

    public void setYbxmdj(String ybxmdj) {
        this.ybxmdj = ybxmdj;
    }

    public String getIcdCode() {
        return icdCode;
    }

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }
}
