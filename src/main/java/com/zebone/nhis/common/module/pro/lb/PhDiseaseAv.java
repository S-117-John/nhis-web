package com.zebone.nhis.common.module.pro.lb;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import java.util.Date;

@Table(value="PH_DISEASE_AV")
public class PhDiseaseAv extends BaseModule {
    @PK
    @Field(value="PK_AV")
    private String pkAv;

    @Field(value="PK_PHDISE")
    private String pkPhdise;

    @Field(value="DT_AVDISE")
    private String dtAvdise;

    @Field(value="EU_CONTACTHIST")
    private String euContacthist;

    @Field(value="CNT_SHARESYRANGE")
    private Integer cntSharesyrange;

    @Field(value="CNT_NOMARITALSEX")
    private Integer cntNomaritalsex;

    @Field(value="CNT_SSSB")
    private Integer cntSssb;

    @Field(value="OTH_CONTACTHIST")
    private String othContacthist;

    @Field(value="EU_VENEREALHIST")
    private String euVenerealhist;

    @Field(value="DT_INFECTWAY")
    private String dtInfectway;

    @Field(value="OTH_INFECTWAY")
    private String othInfectway;

    @Field(value="DT_SAMPSRC")
    private String dtSampsrc;

    @Field(value="OTH_SAMPSRC")
    private String othSampsrc;

    @Field(value="EU_LABRESULT")
    private String euLabresult;

    @Field(value="DATE_POSITIVE")
    private Date datePositive;

    @Field(value="ORG_TEST")
    private String orgTest;

    @Field(value="DATE_AIDSCONFIRM")
    private Date dateAidsconfirm;

    @Field(value="EU_MARI")
    private String euMari;

    @Field(value="EU_EDU")
    private String euEdu;

    public String getPkAv() {
        return pkAv;
    }

    public void setPkAv(String pkAv) {
        this.pkAv = pkAv == null ? null : pkAv.trim();
    }

    public String getPkPhdise() {
        return pkPhdise;
    }

    public void setPkPhdise(String pkPhdise) {
        this.pkPhdise = pkPhdise == null ? null : pkPhdise.trim();
    }

    public String getDtAvdise() {
        return dtAvdise;
    }

    public void setDtAvdise(String dtAvdise) {
        this.dtAvdise = dtAvdise == null ? null : dtAvdise.trim();
    }

    public String getEuContacthist() {
        return euContacthist;
    }

    public void setEuContacthist(String euContacthist) {
        this.euContacthist = euContacthist == null ? null : euContacthist.trim();
    }

    public Integer getCntSharesyrange() {
        return cntSharesyrange;
    }

    public void setCntSharesyrange(Integer cntSharesyrange) {
        this.cntSharesyrange = cntSharesyrange;
    }

    public Integer getCntNomaritalsex() {
        return cntNomaritalsex;
    }

    public void setCntNomaritalsex(Integer cntNomaritalsex) {
        this.cntNomaritalsex = cntNomaritalsex;
    }

    public Integer getCntSssb() {
        return cntSssb;
    }

    public void setCntSssb(Integer cntSssb) {
        this.cntSssb = cntSssb;
    }

    public String getOthContacthist() {
        return othContacthist;
    }

    public void setOthContacthist(String othContacthist) {
        this.othContacthist = othContacthist == null ? null : othContacthist.trim();
    }

    public String getEuVenerealhist() {
        return euVenerealhist;
    }

    public void setEuVenerealhist(String euVenerealhist) {
        this.euVenerealhist = euVenerealhist == null ? null : euVenerealhist.trim();
    }

    public String getDtInfectway() {
        return dtInfectway;
    }

    public void setDtInfectway(String dtInfectway) {
        this.dtInfectway = dtInfectway == null ? null : dtInfectway.trim();
    }

    public String getOthInfectway() {
        return othInfectway;
    }

    public void setOthInfectway(String othInfectway) {
        this.othInfectway = othInfectway == null ? null : othInfectway.trim();
    }

    public String getDtSampsrc() {
        return dtSampsrc;
    }

    public void setDtSampsrc(String dtSampsrc) {
        this.dtSampsrc = dtSampsrc == null ? null : dtSampsrc.trim();
    }

    public String getOthSampsrc() {
        return othSampsrc;
    }

    public void setOthSampsrc(String othSampsrc) {
        this.othSampsrc = othSampsrc == null ? null : othSampsrc.trim();
    }

    public String getEuLabresult() {
        return euLabresult;
    }

    public void setEuLabresult(String euLabresult) {
        this.euLabresult = euLabresult == null ? null : euLabresult.trim();
    }

    public Date getDatePositive() {
        return datePositive;
    }

    public void setDatePositive(Date datePositive) {
        this.datePositive = datePositive;
    }

    public String getOrgTest() {
        return orgTest;
    }

    public void setOrgTest(String orgTest) {
        this.orgTest = orgTest == null ? null : orgTest.trim();
    }

    public Date getDateAidsconfirm() {
        return dateAidsconfirm;
    }

    public void setDateAidsconfirm(Date dateAidsconfirm) {
        this.dateAidsconfirm = dateAidsconfirm;
    }

    public String getEuMari() {
        return euMari;
    }

    public void setEuMari(String euMari) {
        this.euMari = euMari == null ? null : euMari.trim();
    }

    public String getEuEdu() {
        return euEdu;
    }

    public void setEuEdu(String euEdu) {
        this.euEdu = euEdu == null ? null : euEdu.trim();
    }
}