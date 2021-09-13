package com.zebone.nhis.common.module.cn.ipdw;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import java.util.Date;



@Table(value="ZSBA_OP_SPD")
public class ZsbaOpSpd extends BaseModule {

    @PK
    @Field(value="PK_OPSPD",id= Field.KeyId.UUID)
    private String pkOpspd;

    @Field(value = "PK_OPOCC")
    private String pkOpocc;

    @Field(value = "PK_ORG")
    private String pkOrg;

    @Field(value = "PK_PV")
    private String pkPv;

    @Field(value = "PK_CNORD")
    private String pkCnord;

    @Field(value = "PK_CGIP")
    private String pkCgip;

    @Field(value = "PK_ITEM")
    private String pkItem;

    @Field(value = "NAME_CG")
    private String nameCg;

    @Field(value = "PK_UNIT")
    private String pkUnit;

    @Field(value = "SPEC")
    private String spec;

    @Field(value = "QUAN")
    private Integer quan;

    @Field(value = "FLAG_DISAPPEAR")
    private String flagDisappear;

    @Field(value = "BARCODE")
    private String barcode;

    @Field(value = "NOTE")
    private String note;

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


    public String getPkOpspd() {
        return pkOpspd;
    }
    public void setPkOpspd(String pkOpspd) {
        this.pkOpspd = pkOpspd;
    }

    public String getPkOrg() {
        return pkOrg;
    }
    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkPv() {
        return pkPv;
    }
    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkCnord() {
        return pkCnord;
    }
    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }

    public String getPkCgip() {
        return pkCgip;
    }
    public void setPkCgip(String pkCgip) {
        this.pkCgip = pkCgip;
    }

    public String getPkItem() {
        return pkItem;
    }
    public void setPkItem(String pkItem) {
        this.pkItem = pkItem;
    }

    public String getNameCg() {
        return nameCg;
    }
    public void setNameCg(String nameCg) {
        this.nameCg = nameCg;
    }

    public String getPkUnit() {
        return pkUnit;
    }
    public void setPkUnit(String pkUnit) {
        this.pkUnit = pkUnit;
    }

    public String getSpec() {
        return spec;
    }
    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getQuan() {
        return quan;
    }
    public void setQuan(Integer quan) {
        this.quan = quan;
    }

    public String getFlagDisappear() {
        return flagDisappear;
    }
    public void setFlagDisappear(String flagDisappear) {
        this.flagDisappear = flagDisappear;
    }

    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
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

    public String getPkOpocc() { return pkOpocc; }
    public void setPkOpocc(String pkOpocc) { this.pkOpocc = pkOpocc; }
}
