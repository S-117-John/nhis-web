package com.zebone.nhis.common.module.rec;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: cn_shift_dt 
 *
 * @since 2017-09-06 04:40:48
 */
@Table(value="cn_shift_dt")
public class CnShiftDt extends BaseModule  {

	@PK
	@Field(value="pk_shiftdt",id=KeyId.UUID)
    private String pkShiftdt;

	@Field(value="pk_shift")
    private String pkShift;

	@Field(value="pk_pi")
    private String pkPi;

	@Field(value="pk_pv")
    private String pkPv;

	@Field(value="bedno")
    private String bedno;

	@Field(value="dt_level_ns")
    private String dtLevelNs;

	@Field(value="dt_level_dise")
    private String dtLevelDise;

	@Field(value="flag_opt")
    private String flagOpt;

	@Field(value="flag_deli")
    private String flagDeli;

	@Field(value="dt_level_nutr")
    private String dtLevelNutr;

	@Field(value="pk_emp_phy")
    private String pkEmpPhy;

	@Field(value="name_emp_phy")
    private String nameEmpPhy;

	@Field(value="desc_diag")
    private String descDiag;

	@Field(value="desc_al")
    private String descAl;

	@Field(value="temperature")
    private BigDecimal temperature;

	@Field(value="pulse")
    private BigDecimal pulse;

	@Field(value="resp")
    private BigDecimal resp;

	@Field(value="sbp")
    private BigDecimal sbp;

	@Field(value="dbp")
    private BigDecimal dbp;

	@Field(value="desc_dise")
    private String descDise;

	@Field(value="desc_treat")
    private String descTreat;

	@Field(value="desc_oth")
    private String descOth;

	@Field(value="note")
    private String note;

	@Field(value="modifier")
    private String modifier;

	@Field(value="modity_time")
    private Date modityTime;


    public String getPkShiftdt(){
        return this.pkShiftdt;
    }
    public void setPkShiftdt(String pkShiftdt){
        this.pkShiftdt = pkShiftdt;
    }

    public String getPkShift(){
        return this.pkShift;
    }
    public void setPkShift(String pkShift){
        this.pkShift = pkShift;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getBedno(){
        return this.bedno;
    }
    public void setBedno(String bedno){
        this.bedno = bedno;
    }

    public String getDtLevelNs(){
        return this.dtLevelNs;
    }
    public void setDtLevelNs(String dtLevelNs){
        this.dtLevelNs = dtLevelNs;
    }

    public String getDtLevelDise(){
        return this.dtLevelDise;
    }
    public void setDtLevelDise(String dtLevelDise){
        this.dtLevelDise = dtLevelDise;
    }

    public String getFlagOpt(){
        return this.flagOpt;
    }
    public void setFlagOpt(String flagOpt){
        this.flagOpt = flagOpt;
    }

    public String getFlagDeli(){
        return this.flagDeli;
    }
    public void setFlagDeli(String flagDeli){
        this.flagDeli = flagDeli;
    }

    public String getDtLevelNutr(){
        return this.dtLevelNutr;
    }
    public void setDtLevelNutr(String dtLevelNutr){
        this.dtLevelNutr = dtLevelNutr;
    }

    public String getPkEmpPhy(){
        return this.pkEmpPhy;
    }
    public void setPkEmpPhy(String pkEmpPhy){
        this.pkEmpPhy = pkEmpPhy;
    }

    public String getNameEmpPhy(){
        return this.nameEmpPhy;
    }
    public void setNameEmpPhy(String nameEmpPhy){
        this.nameEmpPhy = nameEmpPhy;
    }

    public String getDescDiag(){
        return this.descDiag;
    }
    public void setDescDiag(String descDiag){
        this.descDiag = descDiag;
    }

    public String getDescAl(){
        return this.descAl;
    }
    public void setDescAl(String descAl){
        this.descAl = descAl;
    }

    public BigDecimal getTemperature(){
        return this.temperature;
    }
    public void setTemperature(BigDecimal temperature){
        this.temperature = temperature;
    }

    public BigDecimal getPulse(){
        return this.pulse;
    }
    public void setPulse(BigDecimal pulse){
        this.pulse = pulse;
    }

    public BigDecimal getResp(){
        return this.resp;
    }
    public void setResp(BigDecimal resp){
        this.resp = resp;
    }

    public BigDecimal getSbp(){
        return this.sbp;
    }
    public void setSbp(BigDecimal sbp){
        this.sbp = sbp;
    }

    public BigDecimal getDbp(){
        return this.dbp;
    }
    public void setDbp(BigDecimal dbp){
        this.dbp = dbp;
    }

    public String getDescDise(){
        return this.descDise;
    }
    public void setDescDise(String descDise){
        this.descDise = descDise;
    }

    public String getDescTreat(){
        return this.descTreat;
    }
    public void setDescTreat(String descTreat){
        this.descTreat = descTreat;
    }

    public String getDescOth(){
        return this.descOth;
    }
    public void setDescOth(String descOth){
        this.descOth = descOth;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}