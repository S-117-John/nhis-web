package com.zebone.nhis.common.module.pro.lb;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import java.util.Date;

@Table(value="PH_DISEASE")
public class PhDisease extends BaseModule {
    @PK
    @Field(value="PK_PHDISE")
    private String pkPhdise;

    @Field(value="PK_PV")
    private String pkPv;

    @Field(value="PK_PVDIAG")
    private String pkPvdiag;

    @Field(value="CODE_ICD")
    private String codeIcd;

    @Field(value="NAME_ICD")
    private String nameIcd;

    @Field(value="DT_DISECARD")
    private String dtDisecard;

    @Field(value="FLAG_FIRST")
    private String flagFirst;

    @Field(value="DT_NATION")
    private String dtNation;

    @Field(value="CODE_CARD")
    private String codeCard;

    @Field(value="UNIT_WORK")
    private String unitWork;

    @Field(value="MOBILE")
    private String mobile;

    @Field(value="DT_PVSOURCE")
    private String dtPvsource;

    @Field(value="PROVINCE")
    private String province;

    @Field(value="CITY")
    private String city;

    @Field(value="COUNTY")
    private String county;

    @Field(value="TOWN")
    private String town;

    @Field(value="COUNTRY")
    private String country;

    @Field(value="HOUSENO")
    private String houseno;

    @Field(value="ADDR_DT")
    private String addrDt;

    @Field(value="DATE_ONSET")
    private Date dateOnset;

    @Field(value="DATE_DIAG")
    private Date dateDiag;

    @Field(value="DATE_DEAD")
    private Date dateDead;

    @Field(value="DATE_RPT")
    private Date dateRpt;

    @Field(value="ORG_RPT")
    private String orgRpt;

    @Field(value="PK_EMP_RPT")
    private String pkEmpRpt;

    @Field(value="NAME_EMP_RPT")
    private String nameEmpRpt;

    @Field(value="TEL_EMP")
    private String telEmp;

    @Field(value="NOTE")
    private String note;

    @Field(value="EU_STATUS")
    private String euStatus;

    @Field(value="DATE_SUBMIT")
    private Date dateSubmit;

    @Field(value="PK_EMP_SUBMIT")
    private String pkEmpSubmit;

    @Field(value="NAME_EMP_SUBMIT")
    private String nameEmpSubmit;

    @Field(value="DATE_CHK")
    private Date dateChk;

    @Field(value="PK_EMP_CHK")
    private String pkEmpChk;

    @Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

    @Field(value="DATE_CANCEL")
    private Date dateCancel;

    @Field(value="PK_EMP_CANCEL")
    private String pkEmpCancel;

    @Field(value="NAME_EMP_CANCEL")
    private String nameEmpCancel;

    public String getPkPhdise() {
        return pkPhdise;
    }

    public void setPkPhdise(String pkPhdise) {
        this.pkPhdise = pkPhdise == null ? null : pkPhdise.trim();
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv == null ? null : pkPv.trim();
    }

    public String getPkPvdiag() {
        return pkPvdiag;
    }

    public void setPkPvdiag(String pkPvdiag) {
        this.pkPvdiag = pkPvdiag == null ? null : pkPvdiag.trim();
    }

    public String getCodeIcd() {
        return codeIcd;
    }

    public void setCodeIcd(String codeIcd) {
        this.codeIcd = codeIcd == null ? null : codeIcd.trim();
    }

    public String getNameIcd() {
        return nameIcd;
    }

    public void setNameIcd(String nameIcd) {
        this.nameIcd = nameIcd == null ? null : nameIcd.trim();
    }

    public String getDtDisecard() {
        return dtDisecard;
    }

    public void setDtDisecard(String dtDisecard) {
        this.dtDisecard = dtDisecard == null ? null : dtDisecard.trim();
    }

    public String getFlagFirst() {
        return flagFirst;
    }

    public void setFlagFirst(String flagFirst) {
        this.flagFirst = flagFirst == null ? null : flagFirst.trim();
    }

    public String getDtNation() {
        return dtNation;
    }

    public void setDtNation(String dtNation) {
        this.dtNation = dtNation == null ? null : dtNation.trim();
    }

    public String getCodeCard() {
        return codeCard;
    }

    public void setCodeCard(String codeCard) {
        this.codeCard = codeCard == null ? null : codeCard.trim();
    }

    public String getUnitWork() {
        return unitWork;
    }

    public void setUnitWork(String unitWork) {
        this.unitWork = unitWork == null ? null : unitWork.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getDtPvsource() {
        return dtPvsource;
    }

    public void setDtPvsource(String dtPvsource) {
        this.dtPvsource = dtPvsource == null ? null : dtPvsource.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town == null ? null : town.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getHouseno() {
        return houseno;
    }

    public void setHouseno(String houseno) {
        this.houseno = houseno == null ? null : houseno.trim();
    }

    public String getAddrDt() {
        return addrDt;
    }

    public void setAddrDt(String addrDt) {
        this.addrDt = addrDt == null ? null : addrDt.trim();
    }

    public Date getDateOnset() {
        return dateOnset;
    }

    public void setDateOnset(Date dateOnset) {
        this.dateOnset = dateOnset;
    }

    public Date getDateDiag() {
        return dateDiag;
    }

    public void setDateDiag(Date dateDiag) {
        this.dateDiag = dateDiag;
    }

    public Date getDateDead() {
        return dateDead;
    }

    public void setDateDead(Date dateDead) {
        this.dateDead = dateDead;
    }

    public Date getDateRpt() {
        return dateRpt;
    }

    public void setDateRpt(Date dateRpt) {
        this.dateRpt = dateRpt;
    }

    public String getOrgRpt() {
        return orgRpt;
    }

    public void setOrgRpt(String orgRpt) {
        this.orgRpt = orgRpt == null ? null : orgRpt.trim();
    }

    public String getPkEmpRpt() {
        return pkEmpRpt;
    }

    public void setPkEmpRpt(String pkEmpRpt) {
        this.pkEmpRpt = pkEmpRpt == null ? null : pkEmpRpt.trim();
    }

    public String getNameEmpRpt() {
        return nameEmpRpt;
    }

    public void setNameEmpRpt(String nameEmpRpt) {
        this.nameEmpRpt = nameEmpRpt == null ? null : nameEmpRpt.trim();
    }

    public String getTelEmp() {
        return telEmp;
    }

    public void setTelEmp(String telEmp) {
        this.telEmp = telEmp == null ? null : telEmp.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public String getEuStatus() {
        return euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus == null ? null : euStatus.trim();
    }

    public Date getDateSubmit() {
        return dateSubmit;
    }

    public void setDateSubmit(Date dateSubmit) {
        this.dateSubmit = dateSubmit;
    }

    public String getPkEmpSubmit() {
        return pkEmpSubmit;
    }

    public void setPkEmpSubmit(String pkEmpSubmit) {
        this.pkEmpSubmit = pkEmpSubmit == null ? null : pkEmpSubmit.trim();
    }

    public String getNameEmpSubmit() {
        return nameEmpSubmit;
    }

    public void setNameEmpSubmit(String nameEmpSubmit) {
        this.nameEmpSubmit = nameEmpSubmit == null ? null : nameEmpSubmit.trim();
    }

    public Date getDateChk() {
        return dateChk;
    }

    public void setDateChk(Date dateChk) {
        this.dateChk = dateChk;
    }

    public String getPkEmpChk() {
        return pkEmpChk;
    }

    public void setPkEmpChk(String pkEmpChk) {
        this.pkEmpChk = pkEmpChk == null ? null : pkEmpChk.trim();
    }

    public String getNameEmpChk() {
        return nameEmpChk;
    }

    public void setNameEmpChk(String nameEmpChk) {
        this.nameEmpChk = nameEmpChk == null ? null : nameEmpChk.trim();
    }

    public Date getDateCancel() {
        return dateCancel;
    }

    public void setDateCancel(Date dateCancel) {
        this.dateCancel = dateCancel;
    }

    public String getPkEmpCancel() {
        return pkEmpCancel;
    }

    public void setPkEmpCancel(String pkEmpCancel) {
        this.pkEmpCancel = pkEmpCancel == null ? null : pkEmpCancel.trim();
    }

    public String getNameEmpCancel() {
        return nameEmpCancel;
    }

    public void setNameEmpCancel(String nameEmpCancel) {
        this.nameEmpCancel = nameEmpCancel == null ? null : nameEmpCancel.trim();
    }
}