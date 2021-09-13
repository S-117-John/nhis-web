package com.zebone.nhis.common.module.labor.nis;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: PV_LABOR_REC
 *
 * @since 2017-09-15 07:25:14
 */
@Table(value = "PV_LABOR_REC")
public class PvLaborRec extends BaseModule {

    @PK
    @Field(value = "PK_LABORREC", id = KeyId.UUID)
    private String pkLaborrec;

    @Field(value = "PK_PV")
    private String pkPv;

    @Field(value = "PRE_WEEKS")
    private String preWeeks;

    @Field(value = "DATE_LABOR")
    private Date dateLabor;

    @Field(value = "FLAG_LC")
    private String flagLc;

    @Field(value = "BREAK_MODE")
    private String breakMode;

    @Field(value = "DATE_ALLBREAK")
    private Date dateAllbreak;

    @Field(value = "FLAG_DAB")
    private String flagDab;

    @Field(value = "AM_FLUID")
    private String amFluid;

    @Field(value = "OTHER_FLUID")
    private String otherFluid;

    @Field(value = "DATE_PLA")
    private Date datePla;

    @Field(value = "PLA_OUT_MODE2")
    private String plaOutMode2;

    @Field(value = "PLA_OUT_MODE")
    private String plaOutMode;

    @Field(value = "PLA_OUT_REASON")
    private String plaOutReason;

    @Field(value = "DUR_FIRST")
    private String durFirst;

    @Field(value = "DUR_SECOND")
    private String durSecond;

    @Field(value = "DUR_THRID")
    private String durThrid;

    @Field(value = "DUR_ALL")
    private String durAll;

    @Field(value = "PER_ABOUT")
    private String perAbout;

    @Field(value = "PER_OTHER")
    private String perOther;

    @Field(value = "PER_STIT")
    private String perStit;

    @Field(value = "PER_STIT_OTHER")
    private String perStitOther;

    @Field(value = "PER_STIT_NUM")
    private Integer perStitNum;

    @Field(value = "UPC_BREAK")
    private String upcBreak;

    @Field(value = "UPC_BREAK_PART")
    private String upcBreakPart;

    @Field(value = "UPC_BREAK_DEEP")
    private Integer upcBreakDeep;

    @Field(value = "UPC_OTHER")
    private String upcOther;

    @Field(value = "OTHER_FNUM")
    private String otherFnum;

    @Field(value = "NAME_OPER")
    private String nameOper;

    @Field(value = "SPEC_OPER")
    private String specOper;

    @Field(value = "NUM_VG")
    private Integer numVg;

    @Field(value = "NUM_OPER")
    private Integer numOper;

    @Field(value = "NUM_DAY")
    private Integer numDay;

    @Field(value = "PK_DIAG")
    private String pkDiag;

    @Field(value = "NOTE")
    private String note;

    @Field(value = "DT_EXCEP")
    private String dtExcep;

    @Field(value = "EU_HB")
    private String euHb;

    @Field(value = "PK_EMP_OPER")
    private String pkEmpOper;

    @Field(value = "NAME_EMP_OPER")
    private String nameEmpOper;

    @Field(value = "PK_EMP_JS")
    private String pkEmpJs;

    @Field(value = "NAME_EMP_JS")
    private String nameEmpJs;

    @Field(value = "PK_EMP_HY")
    private String pkEmpHy;

    @Field(value = "NAME_EMP_HY")
    private String nameEmpHy;

    @Field(value = "MODIFY_TIME")
    private Date modifyTime;

    /**
     * 获取或设置是否是高危妊娠
     */
    @Field(value = "FLAG_RISK")
    private String  flagRisk;

    /**
     * 获取或设置并发症
     */
    @Field(value = "FLAG_COMP")
    public String flagComp;
    
    /**
     * 并发症描述
     */
    @Field(value = "DESC_COMP")
    public String descComp;

    /**
     * 妇幼保健号
     */
    @Field(value = "PNO")
    public String pno;

    @Field(value = "FLAG_LIGATION")
    public String  flagLigation;

    /**
     * 母亲血型
     */
    @Field(value = "DT_BLOOD_MATHER")
    public String dtBloodMather;
    /**父亲姓名 */
    @Field(value = "NAME_FATHER")
    public String nameFather;
    /**父亲年龄 */
    @Field(value = "AGE_FATHER")
    public String ageFather;
    /**父亲血型 */
    @Field(value = "DT_BLOODTYPE")
    public String dtBloodtype;
    /**父亲职业 */
    @Field(value = "DT_JOB_FATHER")
    public String dtJobFather;
    /**父亲联系地址 */
    @Field(value = "ADDRESS_FATHER")
    public String addressFather;

    public String getDtBloodMather() {
        return dtBloodMather;
    }

    public void setDtBloodMather(String dtBloodMather) {
        this.dtBloodMather = dtBloodMather;
    }

    public String getNameFather() {
        return nameFather;
    }

    public void setNameFather(String nameFather) {
        this.nameFather = nameFather;
    }

    public String getAgeFather() {
        return ageFather;
    }

    public void setAgeFather(String ageFather) {
        this.ageFather = ageFather;
    }

    public String getDtBloodtype() {
        return dtBloodtype;
    }

    public void setDtBloodtype(String dtBloodtype) {
        this.dtBloodtype = dtBloodtype;
    }

    public String getDtJobFather() {
        return dtJobFather;
    }

    public void setDtJobFather(String dtJobFather) {
        this.dtJobFather = dtJobFather;
    }

    public String getAddressFather() {
        return addressFather;
    }

    public void setAddressFather(String addressFather) {
        this.addressFather = addressFather;
    }

    public String getFlagRisk() {
        return flagRisk;
    }

    public void setFlagRisk(String flagRisk) {
        this.flagRisk = flagRisk;
    }

    public String getFlagLigation() {
        return flagLigation;
    }
    public String getFlagComp() {
        return flagComp;
    }

    public void setFlagComp(String flagComp) {
        this.flagComp = flagComp;
    }

    public String getDescComp() {
        return descComp;
    }

    public void setDescComp(String descComp) {
        this.descComp = descComp;
    }

    public String  isFlagLigation() {
        return flagLigation;
    }

    public void setFlagLigation(String  flagLigation) {
        this.flagLigation = flagLigation;
    }

    public String getPkLaborrec() {
        return this.pkLaborrec;
    }

    public void setPkLaborrec(String pkLaborrec) {
        this.pkLaborrec = pkLaborrec;
    }

    public String getPkPv() {
        return this.pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPreWeeks() {
        return this.preWeeks;
    }

    public void setPreWeeks(String preWeeks) {
        this.preWeeks = preWeeks;
    }

    public Date getDateLabor() {
        return this.dateLabor;
    }

    public void setDateLabor(Date dateLabor) {
        this.dateLabor = dateLabor;
    }

    public String getFlagLc() {
        return this.flagLc;
    }

    public void setFlagLc(String flagLc) {
        this.flagLc = flagLc;
    }

    public String getBreakMode() {
        return this.breakMode;
    }

    public void setBreakMode(String breakMode) {
        this.breakMode = breakMode;
    }

    public Date getDateAllbreak() {
        return this.dateAllbreak;
    }

    public void setDateAllbreak(Date dateAllbreak) {
        this.dateAllbreak = dateAllbreak;
    }

    public String getAmFluid() {
        return this.amFluid;
    }

    public void setAmFluid(String amFluid) {
        this.amFluid = amFluid;
    }

    public String getOtherFluid() {
        return this.otherFluid;
    }

    public void setOtherFluid(String otherFluid) {
        this.otherFluid = otherFluid;
    }

    public Date getDatePla() {
        return this.datePla;
    }

    public void setDatePla(Date datePla) {
        this.datePla = datePla;
    }

    public String getPlaOutMode2() {
        return this.plaOutMode2;
    }

    public void setPlaOutMode2(String plaOutMode2) {
        this.plaOutMode2 = plaOutMode2;
    }

    public String getPlaOutMode() {
        return this.plaOutMode;
    }

    public void setPlaOutMode(String plaOutMode) {
        this.plaOutMode = plaOutMode;
    }

    public String getPlaOutReason() {
        return this.plaOutReason;
    }

    public void setPlaOutReason(String plaOutReason) {
        this.plaOutReason = plaOutReason;
    }

    public String getDurFirst() {
        return this.durFirst;
    }

    public void setDurFirst(String durFirst) {
        this.durFirst = durFirst;
    }

    public String getDurSecond() {
        return this.durSecond;
    }

    public void setDurSecond(String durSecond) {
        this.durSecond = durSecond;
    }

    public String getDurThrid() {
        return this.durThrid;
    }

    public void setDurThrid(String durThrid) {
        this.durThrid = durThrid;
    }

    public String getDurAll() {
        return this.durAll;
    }

    public void setDurAll(String durAll) {
        this.durAll = durAll;
    }

    public String getPerAbout() {
        return this.perAbout;
    }

    public void setPerAbout(String perAbout) {
        this.perAbout = perAbout;
    }

    public String getPerOther() {
        return this.perOther;
    }

    public void setPerOther(String perOther) {
        this.perOther = perOther;
    }

    public String getPerStit() {
        return this.perStit;
    }

    public void setPerStit(String perStit) {
        this.perStit = perStit;
    }

    public String getPerStitOther() {
        return this.perStitOther;
    }

    public void setPerStitOther(String perStitOther) {
        this.perStitOther = perStitOther;
    }

    public Integer getPerStitNum() {
        return this.perStitNum;
    }

    public void setPerStitNum(Integer perStitNum) {
        this.perStitNum = perStitNum;
    }

    public String getUpcBreak() {
        return this.upcBreak;
    }

    public void setUpcBreak(String upcBreak) {
        this.upcBreak = upcBreak;
    }

    public String getUpcBreakPart() {
        return this.upcBreakPart;
    }

    public void setUpcBreakPart(String upcBreakPart) {
        this.upcBreakPart = upcBreakPart;
    }

    public Integer getUpcBreakDeep() {
        return this.upcBreakDeep;
    }

    public void setUpcBreakDeep(Integer upcBreakDeep) {
        this.upcBreakDeep = upcBreakDeep;
    }

    public String getUpcOther() {
        return this.upcOther;
    }

    public void setUpcOther(String upcOther) {
        this.upcOther = upcOther;
    }

    public String getNameOper() {
        return this.nameOper;
    }

    public void setNameOper(String nameOper) {
        this.nameOper = nameOper;
    }

    public String getSpecOper() {
        return this.specOper;
    }

    public void setSpecOper(String specOper) {
        this.specOper = specOper;
    }

    public Integer getNumVg() {
        return this.numVg;
    }

    public void setNumVg(Integer numVg) {
        this.numVg = numVg;
    }

    public Integer getNumOper() {
        return this.numOper;
    }

    public void setNumOper(Integer numOper) {
        this.numOper = numOper;
    }

    public Integer getNumDay() {
        return this.numDay;
    }

    public void setNumDay(Integer numDay) {
        this.numDay = numDay;
    }

    public String getPkDiag() {
        return this.pkDiag;
    }

    public void setPkDiag(String pkDiag) {
        this.pkDiag = pkDiag;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDtExcep() {
        return this.dtExcep;
    }

    public void setDtExcep(String dtExcep) {
        this.dtExcep = dtExcep;
    }

    public String getEuHb() {
        return this.euHb;
    }

    public void setEuHb(String euHb) {
        this.euHb = euHb;
    }

    public String getPkEmpOper() {
        return this.pkEmpOper;
    }

    public void setPkEmpOper(String pkEmpOper) {
        this.pkEmpOper = pkEmpOper;
    }

    public String getNameEmpOper() {
        return this.nameEmpOper;
    }

    public void setNameEmpOper(String nameEmpOper) {
        this.nameEmpOper = nameEmpOper;
    }

    public String getPkEmpJs() {
        return this.pkEmpJs;
    }

    public void setPkEmpJs(String pkEmpJs) {
        this.pkEmpJs = pkEmpJs;
    }

    public String getNameEmpJs() {
        return this.nameEmpJs;
    }

    public void setNameEmpJs(String nameEmpJs) {
        this.nameEmpJs = nameEmpJs;
    }

    public String getPkEmpHy() {
        return this.pkEmpHy;
    }

    public void setPkEmpHy(String pkEmpHy) {
        this.pkEmpHy = pkEmpHy;
    }

    public String getNameEmpHy() {
        return this.nameEmpHy;
    }

    public void setNameEmpHy(String nameEmpHy) {
        this.nameEmpHy = nameEmpHy;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getOtherFnum() {
        return otherFnum;
    }

    public void setOtherFnum(String otherFnum) {
        this.otherFnum = otherFnum;
    }

    public String getFlagDab() {return flagDab;}

    public void setFlagDab(String flagDab) {this.flagDab = flagDab;}
}