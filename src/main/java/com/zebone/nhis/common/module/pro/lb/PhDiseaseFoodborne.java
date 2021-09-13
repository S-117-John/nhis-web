package com.zebone.nhis.common.module.pro.lb;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.math.BigDecimal;

@Table(value="PH_DISEASE_FOODBORNE")
public class PhDiseaseFoodborne extends BaseModule {
    @PK
    @Field(value="PK_FOODBORNE")
    private String pkFoodborne;

    @Field(value="FLAG_IP")
    private String flagIp;

    @Field(value="PK_PHDISE")
    private String pkPhdise;

    @Field(value="EU_OCC")
    private String euOcc;

    @Field(value="EU_SYMPTOM")
    private String euSymptom;

    @Field(value="TEMPERATURE")
    private BigDecimal temperature;

    @Field(value="OTH_SYMPTOM")
    private String othSymptom;

    @Field(value="EU_DIGESTIVE")
    private String euDigestive;

    @Field(value="CNT_EMISIS")
    private Integer cntEmisis;

    @Field(value="CNT_DIARRHEA")
    private Integer cntDiarrhea;

    @Field(value="EU_DIARRHEA")
    private String euDiarrhea;

    @Field(value="OTH_DIGESTIVE")
    private String othDigestive;

    @Field(value="EU_RESPIRATORY")
    private String euRespiratory;

    @Field(value="OTH_RESPIRATORY")
    private String othRespiratory;

    @Field(value="EU_CARDIO")
    private String euCardio;

    @Field(value="OTH_CARDIO")
    private String othCardio;

    @Field(value="EU_URINARY")
    private String euUrinary;

    @Field(value="OTH_URINARY")
    private String othUrinary;

    @Field(value="EU_NEURAL")
    private String euNeural;

    @Field(value="EU_PUPIL")
    private String euPupil;

    @Field(value="OTH_NEURAL")
    private String othNeural;

    @Field(value="EU_SKIN")
    private String euSkin;

    @Field(value="OTH_SKIN")
    private String othSkin;

    @Field(value="EU_FIRSTDIAG")
    private String euFirstdiag;

    @Field(value="EU_USEANTI")
    private String euUseanti;

    @Field(value="DESC_ANTI")
    private String descAnti;

    @Field(value="EU_HIST")
    private String euHist;

    @Field(value="EU_UNHYGIENIC")
    private String euUnhygienic;

    @Field(value="CASE_ATTACHMENT")
    private String caseAttachment;

    @Field(value="NAME_EMP_PHY")
    private String nameEmpPhy;
    @Field(value="EU_STATUS")
    private String eUSTATUS;
    public String getPkFoodborne() {
        return pkFoodborne;
    }

    public void setPkFoodborne(String pkFoodborne) {
        this.pkFoodborne = pkFoodborne == null ? null : pkFoodborne.trim();
    }

    public String getFlagIp() {
        return flagIp;
    }

    public void setFlagIp(String flagIp) {
        this.flagIp = flagIp == null ? null : flagIp.trim();
    }

    public String getPkPhdise() {
        return pkPhdise;
    }

    public void setPkPhdise(String pkPhdise) {
        this.pkPhdise = pkPhdise == null ? null : pkPhdise.trim();
    }

    public String getEuOcc() {
        return euOcc;
    }

    public void setEuOcc(String euOcc) {
        this.euOcc = euOcc == null ? null : euOcc.trim();
    }

    public String getEuSymptom() {
        return euSymptom;
    }

    public void setEuSymptom(String euSymptom) {
        this.euSymptom = euSymptom == null ? null : euSymptom.trim();
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public String getOthSymptom() {
        return othSymptom;
    }

    public void setOthSymptom(String othSymptom) {
        this.othSymptom = othSymptom == null ? null : othSymptom.trim();
    }

    public String getEuDigestive() {
        return euDigestive;
    }

    public void setEuDigestive(String euDigestive) {
        this.euDigestive = euDigestive == null ? null : euDigestive.trim();
    }

    public Integer getCntEmisis() {
        return cntEmisis;
    }

    public void setCntEmisis(Integer cntEmisis) {
        this.cntEmisis = cntEmisis;
    }

    public Integer getCntDiarrhea() {
        return cntDiarrhea;
    }

    public void setCntDiarrhea(Integer cntDiarrhea) {
        this.cntDiarrhea = cntDiarrhea;
    }

    public String getEuDiarrhea() {
        return euDiarrhea;
    }

    public void setEuDiarrhea(String euDiarrhea) {
        this.euDiarrhea = euDiarrhea == null ? null : euDiarrhea.trim();
    }

    public String getOthDigestive() {
        return othDigestive;
    }

    public void setOthDigestive(String othDigestive) {
        this.othDigestive = othDigestive == null ? null : othDigestive.trim();
    }

    public String getEuRespiratory() {
        return euRespiratory;
    }

    public void setEuRespiratory(String euRespiratory) {
        this.euRespiratory = euRespiratory == null ? null : euRespiratory.trim();
    }

    public String getOthRespiratory() {
        return othRespiratory;
    }

    public void setOthRespiratory(String othRespiratory) {
        this.othRespiratory = othRespiratory == null ? null : othRespiratory.trim();
    }

    public String getEuCardio() {
        return euCardio;
    }

    public void setEuCardio(String euCardio) {
        this.euCardio = euCardio == null ? null : euCardio.trim();
    }

    public String getOthCardio() {
        return othCardio;
    }

    public void setOthCardio(String othCardio) {
        this.othCardio = othCardio == null ? null : othCardio.trim();
    }

    public String getEuUrinary() {
        return euUrinary;
    }

    public void setEuUrinary(String euUrinary) {
        this.euUrinary = euUrinary == null ? null : euUrinary.trim();
    }

    public String getOthUrinary() {
        return othUrinary;
    }

    public void setOthUrinary(String othUrinary) {
        this.othUrinary = othUrinary == null ? null : othUrinary.trim();
    }

    public String getEuNeural() {
        return euNeural;
    }

    public void setEuNeural(String euNeural) {
        this.euNeural = euNeural == null ? null : euNeural.trim();
    }

    public String getEuPupil() {
        return euPupil;
    }

    public void setEuPupil(String euPupil) {
        this.euPupil = euPupil == null ? null : euPupil.trim();
    }

    public String getOthNeural() {
        return othNeural;
    }

    public void setOthNeural(String othNeural) {
        this.othNeural = othNeural == null ? null : othNeural.trim();
    }

    public String getEuSkin() {
        return euSkin;
    }

    public void setEuSkin(String euSkin) {
        this.euSkin = euSkin == null ? null : euSkin.trim();
    }

    public String getOthSkin() {
        return othSkin;
    }

    public void setOthSkin(String othSkin) {
        this.othSkin = othSkin == null ? null : othSkin.trim();
    }

    public String getEuFirstdiag() {
        return euFirstdiag;
    }

    public void setEuFirstdiag(String euFirstdiag) {
        this.euFirstdiag = euFirstdiag == null ? null : euFirstdiag.trim();
    }

    public String getEuUseanti() {
        return euUseanti;
    }

    public void setEuUseanti(String euUseanti) {
        this.euUseanti = euUseanti == null ? null : euUseanti.trim();
    }

    public String getDescAnti() {
        return descAnti;
    }

    public void setDescAnti(String descAnti) {
        this.descAnti = descAnti == null ? null : descAnti.trim();
    }

    public String getEuHist() {
        return euHist;
    }

    public void setEuHist(String euHist) {
        this.euHist = euHist == null ? null : euHist.trim();
    }

    public String getEuUnhygienic() {
        return euUnhygienic;
    }

    public void setEuUnhygienic(String euUnhygienic) {
        this.euUnhygienic = euUnhygienic == null ? null : euUnhygienic.trim();
    }

    public String getCaseAttachment() {
        return caseAttachment;
    }

    public void setCaseAttachment(String caseAttachment) {
        this.caseAttachment = caseAttachment == null ? null : caseAttachment.trim();
    }

    public String getNameEmpPhy() {
        return nameEmpPhy;
    }

    public void setNameEmpPhy(String nameEmpPhy) {
        this.nameEmpPhy = nameEmpPhy == null ? null : nameEmpPhy.trim();
    }

    public String geteUSTATUS() {
        return eUSTATUS;
    }

    public void seteUSTATUS(String eUSTATUS) {
        this.eUSTATUS = eUSTATUS;
    }
}