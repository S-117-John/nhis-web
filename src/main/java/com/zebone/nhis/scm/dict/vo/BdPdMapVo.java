package com.zebone.nhis.scm.dict.vo;


import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;



@Table(value="BD_PD_TRANS")
public class BdPdMapVo {


    /// <summary>
    /// BD_PD_TRANS主键
    /// </summary>
    @PK
    @Field(value="PK_PDTRANS",id= Field.KeyId.UUID)
    public String pkPdtrans;

    private String pkPd;

    @Field(value = "PK_ORG")
    private String pkOrg;

    @Field(value="PK_PD_Y")
    public String pkPdY ;

    /// <summary>
    /// 饮片编码
    /// </summary>
    public String pdyCode ;

    /// <summary>
    /// 饮片名称
    /// </summary>
    public String pdyName ;

    /// <summary>
    /// 饮片规格
    /// </summary>
    public String pdySpec ;

    /// <summary>
    /// 颗粒主键
    /// </summary>
    @Field(value="PK_PD_K")
    public String pkPdK ;

    /// <summary>
    /// 颗粒编码
    /// </summary>
    public String pdkCode ;

    /// <summary>
    /// 颗粒名称
    /// </summary>
    public String pdkName ;

    /// <summary>
    /// 颗粒规格
    /// </summary>
    public String pdkSpec ;

    /// <summary>
    ///  rate转换率
    /// </summary>

    @Field(value="RATE")
    public Double rates;


    @Field(value="CREATOR")
    private String creator;

    @Field(value="CREATE_TIME")
    private Date createTime;

    @Field(value="DEL_FLAG")
    private String delFlag;

    @Field(value="MODIFIER")
    private String modifier;

    @Field(value="MODITY_TIME")
    private Date modity_time;


    @Field(value="TS")
    private Date ts;

    public String getPkPdtrans() {
        return pkPdtrans;
    }

    public void setPkPdtrans(String pkPdtrans) {
        this.pkPdtrans = pkPdtrans;
    }

    public String getPkPdY() {
        return pkPdY;
    }

    public void setPkPdY(String pkPdY) {
        this.pkPdY = pkPdY;
    }

    public String getPdyCode() {
        return pdyCode;
    }

    public void setPdyCode(String pdyCode) {
        this.pdyCode = pdyCode;
    }

    public String getPdyName() {
        return pdyName;
    }

    public void setPdyName(String pdyName) {
        this.pdyName = pdyName;
    }

    public String getPdySpec() {
        return pdySpec;
    }

    public void setPdySpec(String pdySpec) {
        this.pdySpec = pdySpec;
    }

    public String getPkPdK() {
        return pkPdK;
    }

    public void setPkPdK(String pkPdK) {
        this.pkPdK = pkPdK;
    }

    public String getPdkCode() {
        return pdkCode;
    }

    public void setPdkCode(String pdkCode) {
        this.pdkCode = pdkCode;
    }

    public String getPdkName() {
        return pdkName;
    }

    public void setPdkName(String pdkName) {
        this.pdkName = pdkName;
    }

    public String getPdkSpec() {
        return pdkSpec;
    }

    public void setPdkSpec(String pdkSpec) {
        this.pdkSpec = pdkSpec;
    }

    public Double getRates() {
        return rates;
    }

    public void setRates(Double rates) {
        this.rates = rates;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getModity_time() {
        return modity_time;
    }

    public void setModity_time(Date modity_time) {
        this.modity_time = modity_time;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }


    public String getPkPd() {
        return pkPd;
    }

    public void setPkPd(String pkPd) {
        this.pkPd = pkPd;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }
}
