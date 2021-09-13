package com.zebone.nhis.webservice.zhongshan.vo;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname TkSettle
 * @Description 泰康人寿理赔金记录表
 * @Date 2020-12-04 16:36
 * @Created by wuqiang
 */
@Table(value = "TK_SETTLE")
public class TkSettle extends BaseModule {

    @PK
    @Field(value = "PK_TKSETTLE", id = Field.KeyId.UUID)
    private String pkTksettle;

    /**
     * PK_PV - 患者主键
     */
    @Field(value = "PK_PV")
    private String pkPv;

    /**
     * PK_SETTLE - HIS结算主键
     */
    @Field(value = "PK_SETTLE")
    private String pkSettle;

    /**
     * TK_ID - 泰康结算ID
     */
    @Field(value = "TK_ID")
    private String tkId;

    /**
     * TK_AMOUNT - 结算金额
     */
    @Field(value = "TK_AMOUNT")
    private BigDecimal tkAmount;

    /**
     * TK_DATE_SE - 结算日期
     */
    @Field(value = "TK_DATE_SE")
    private Date tkDateSe;

    /**
     * FLAG_CANC - 取消标志 0 未取消，1取消
     */
    @Field(value = "FLAG_CANC")
    private String flagCanc;

    /**
     * TK_DATE_CNAC - 取消时间
     */
    @Field(value = "TK_DATE_CNAC")
    private Date tkDateCnac;

    /**
     * PK_TKSETTLE_CNAC - 取消对应正记录主键
     */
    @Field(value = "PK_TKSETTLE_CNAC")
    private String pkTksettleCnac;

    /**
     * NOTE - 备注
     */
    @Field(value = "NOTE")
    private String note;

    /**
     * RESERVED1 - 预留字段
     */
    @Field(value = "RESERVED1")
    private String reserved1;

    /**
     * RESERVED2 - 预留字段
     */
    @Field(value = "RESERVED2")
    private String reserved2;

    /**
     * MODITY_TIME - 修改时间
     */
    @Field(value = "MODITY_TIME")
    private Date modityTime;


    /**
     * COMPANY_CODE - 公司编码
     */
    @Field(value = "COMPANY_CODE")
    private String companyCode;

    /**
     * COMPANY_NAME - 公司名称
     */
    @Field(value = "COMPANY_NAME")
    private String companyName;


    public String getPkTksettle() {
        return this.pkTksettle;
    }

    public void setPkTksettle(String pkTksettle) {
        this.pkTksettle = pkTksettle;
    }

    public String getPkPv() {
        return this.pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkSettle() {
        return this.pkSettle;
    }

    public void setPkSettle(String pkSettle) {
        this.pkSettle = pkSettle;
    }

    public String getTkId() {
        return this.tkId;
    }

    public void setTkId(String tkId) {
        this.tkId = tkId;
    }

    public BigDecimal getTkAmount() {
        return this.tkAmount;
    }

    public void setTkAmount(BigDecimal tkAmount) {
        this.tkAmount = tkAmount;
    }

    public Date getTkDateSe() {
        return this.tkDateSe;
    }

    public void setTkDateSe(Date tkDateSe) {
        this.tkDateSe = tkDateSe;
    }

    public String getFlagCanc() {
        return this.flagCanc;
    }

    public void setFlagCanc(String flagCanc) {
        this.flagCanc = flagCanc;
    }

    public Date getTkDateCnac() {
        return this.tkDateCnac;
    }

    public void setTkDateCnac(Date tkDateCnac) {
        this.tkDateCnac = tkDateCnac;
    }

    public String getPkTksettleCnac() {
        return this.pkTksettleCnac;
    }

    public void setPkTksettleCnac(String pkTksettleCnac) {
        this.pkTksettleCnac = pkTksettleCnac;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReserved1() {
        return this.reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return this.reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public Date getModityTime() {
        return this.modityTime;
    }

    public void setModityTime(Date modityTime) {
        this.modityTime = modityTime;
    }


    public String getCompanyCode() {
        return this.companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
