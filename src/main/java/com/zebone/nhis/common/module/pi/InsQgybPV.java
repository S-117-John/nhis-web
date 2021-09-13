package com.zebone.nhis.common.module.pi;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

@Table(value = "INS_QGYB_PV")
public class InsQgybPV extends BaseModule {
    @PK
    @Field(value = "pk_inspv", id = Field.KeyId.UUID)
    private String pkInspv;

    @Field(value = "pk_pv")
    private String pkPv;
    @Field(value = "pk_pi")
    private String pkPi;
    @Field(value = "pk_hp")
    private String pkHp;
    @Field(value = "mdtrt_id")
    private String mdtrtId;
    @Field(value = "name_pi")
    private String namePi;
    @Field(value = "med_type")
    private String medType;
    @Field(value = "dt_exthp")
    private String dtExthp;
    @Field(value = "main_cond_dscr")
    private String mainCondDscr;
    @Field(value = "dise_codg")
    private String diseCodg;
    @Field(value = "dise_name")
    private String diseName;
    @Field(value = "birctrl_type")
    private String birctrlType;
    @Field(value = "birctrl_matn_date")
    private Date birctrlMatnDate;
    @Field(value = "note")
    private String note;
    @Field(value = "matn_type")
    private String matnType;
    @Field(value = "geso_val")
    private String gesoVal;



    public String getPkInspv() {
        return pkInspv;
    }

    public void setPkInspv(String pkInspv) {
        this.pkInspv = pkInspv;
    }

    @Override
    public String getPkOrg() {
        return pkOrg;
    }

    @Override
    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getPkHp() {
        return pkHp;
    }

    public void setPkHp(String pkHp) {
        this.pkHp = pkHp;
    }

    public String getMdtrtId() {
        return mdtrtId;
    }

    public void setMdtrtId(String mdtrtId) {
        this.mdtrtId = mdtrtId;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public String getDtExthp() {
        return dtExthp;
    }

    public void setDtExthp(String dtExthp) {
        this.dtExthp = dtExthp;
    }

    public String getMainCondDscr() {
        return mainCondDscr;
    }

    public void setMainCondDscr(String mainCondDscr) {
        this.mainCondDscr = mainCondDscr;
    }

    public String getDiseCodg() {
        return diseCodg;
    }

    public void setDiseCodg(String diseCodg) {
        this.diseCodg = diseCodg;
    }

    public String getDiseName() {
        return diseName;
    }

    public void setDiseName(String diseName) {
        this.diseName = diseName;
    }

    public String getBirctrlType() {
        return birctrlType;
    }

    public void setBirctrlType(String birctrlType) {
        this.birctrlType = birctrlType;
    }

    public Date getBirctrlMatnDate() {
        return birctrlMatnDate;
    }

    public void setBirctrlMatnDate(Date birctrlMatnDate) {
        this.birctrlMatnDate = birctrlMatnDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMatnType() {
        return matnType;
    }

    public void setMatnType(String matnType) {
        this.matnType = matnType;
    }

    public String getGesoVal() {
        return gesoVal;
    }

    public void setGesoVal(String gesoVal) {
        this.gesoVal = gesoVal;
    }
}
