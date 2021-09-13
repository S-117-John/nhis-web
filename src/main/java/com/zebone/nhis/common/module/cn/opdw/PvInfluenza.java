package com.zebone.nhis.common.module.cn.opdw;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/***
 * 流感上报实体
 */
@Table(value = "PV_INFLUENZA")
public class PvInfluenza {
    @PK
    @Field(value="PK_INFLU",id= Field.KeyId.UUID)
    private String pkInflu;

    @Field(value = "PK_PV")
    private String pkPv;

    @Field(value = "DATE_PV")
    private Date datePv;

    @Field(value = "NAME_PI")
    private String namePi;

    @Field(value = "DT_SEX")
    private String dtSex;

    @Field(value = "AGE_PV")
    private String agePv;

    @Field(value = "CODE_ICD")
    private String codeIcd;

    @Field(value = "NAME_ICD")
    private String nameIcd;

    @Field(value = "PK_DEPT")
    private String pkDept;

    @Field(value = "PK_EMP")
    private String pkEmp;

    @Field(value = "DATE_INFLU")
    private Date dateInflu;

    @Field(value = "CODE_PD")
    private String codePd;

    @Field(value = "NAME_PD")
    private String namePd;

    @Field(value = "SPEC")
    private String spec;

    @Field(value = "PRICE")
    private String price;

    @Field(value = "AMOUNT")
    private String amount;

    @Field(value = "CREATOR")
    private String creator;

    @Field(value = "CREATE_TIME")
    private Date createTime;

    @Field(value = "MODIFIER")
    private String modifier;

    @Field(value = "MODITY_TIME")
    private Date modityTime;

    @Field(value = "DEL_FLAG")
    private String delFlag;

    @Field(value = "TS")
    private Date ts;

    public String getPkInflu() {
        return pkInflu;
    }

    public void setPkInflu(String pkInflu) {
        this.pkInflu = pkInflu;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public Date getDatePv() {
        return datePv;
    }

    public void setDatePv(Date datePv) {
        this.datePv = datePv;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getDtSex() {
        return dtSex;
    }

    public void setDtSex(String dtSex) {
        this.dtSex = dtSex;
    }

    public String getAgePv() {
        return agePv;
    }

    public void setAgePv(String agePv) {
        this.agePv = agePv;
    }

    public String getCodeIcd() {
        return codeIcd;
    }

    public void setCodeIcd(String codeIcd) {
        this.codeIcd = codeIcd;
    }

    public String getNameIcd() {
        return nameIcd;
    }

    public void setNameIcd(String nameIcd) {
        this.nameIcd = nameIcd;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getPkEmp() {
        return pkEmp;
    }

    public void setPkEmp(String pkEmp) {
        this.pkEmp = pkEmp;
    }

    public Date getDateInflu() {
        return dateInflu;
    }

    public void setDateInflu(Date dateInflu) {
        this.dateInflu = dateInflu;
    }

    public String getCodePd() {
        return codePd;
    }

    public void setCodePd(String codePd) {
        this.codePd = codePd;
    }

    public String getNamePd() {
        return namePd;
    }

    public void setNamePd(String namePd) {
        this.namePd = namePd;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public Date getModityTime() {
        return modityTime;
    }

    public void setModityTime(Date modityTime) {
        this.modityTime = modityTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}
