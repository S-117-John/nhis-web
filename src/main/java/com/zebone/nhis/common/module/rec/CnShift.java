package com.zebone.nhis.common.module.rec;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: cn_shift 
 *
 * @since 2017-09-06 04:40:27
 */
@Table(value="cn_shift")
public class CnShift extends BaseModule  {

	@PK
	@Field(value="pk_shift",id=KeyId.UUID)
    private String pkShift;

	@Field(value="dt_shifttype")
    private String dtShifttype;

	@Field(value="eu_mntype")
    private String euMntype;

	@Field(value="pk_dept")
    private String pkDept;

	@Field(value="date_shift")
    private Date dateShift;

	@Field(value="pk_emp")
    private String pkEmp;

	@Field(value="name_emp")
    private String nameEmp;

	@Field(value="date_chk")
    private Date dateChk;

	@Field(value="pk_emp_chk")
    private String pkEmpChk;

	@Field(value="name_emp_chk")
    private String nameEmpChk;

	@Field(value="cnt_total")
    private BigDecimal cntTotal;

	@Field(value="cnt_adm")
    private BigDecimal cntAdm;

	@Field(value="cnt_ti")
    private BigDecimal cntTi;

	@Field(value="cnt_to")
    private BigDecimal cntTo;

	@Field(value="cnt_disc")
    private BigDecimal cntDisc;

	@Field(value="cnt_sev")
    private BigDecimal cntSev;

	@Field(value="cnt_crit")
    private BigDecimal cntCrit;

	@Field(value="cnt_death")
    private BigDecimal cntDeath;

	@Field(value="cnt_ns")
    private BigDecimal cntNs;

	@Field(value="cnt_opt")
    private BigDecimal cntOpt;

	@Field(value="cnt_deli")
    private BigDecimal cntDeli;

	@Field(value="note")
    private String note;

	@Field(value="def1")
    private String def1;

	@Field(value="def2")
    private String def2;

	@Field(value="def3")
    private String def3;

	@Field(value="def4")
    private String def4;

	@Field(value="def5")
    private String def5;

	@Field(value="def6")
    private String def6;

	@Field(value="def7")
    private String def7;

	@Field(value="def8")
    private String def8;

	@Field(value="modifier")
    private String modifier;

	@Field(value="modity_time")
    private Date modityTime;


    public String getPkShift(){
        return this.pkShift;
    }
    public void setPkShift(String pkShift){
        this.pkShift = pkShift;
    }

    public String getDtShifttype(){
        return this.dtShifttype;
    }
    public void setDtShifttype(String dtShifttype){
        this.dtShifttype = dtShifttype;
    }

    public String getEuMntype(){
        return this.euMntype;
    }
    public void setEuMntype(String euMntype){
        this.euMntype = euMntype;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public Date getDateShift(){
        return this.dateShift;
    }
    public void setDateShift(Date dateShift){
        this.dateShift = dateShift;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getNameEmp(){
        return this.nameEmp;
    }
    public void setNameEmp(String nameEmp){
        this.nameEmp = nameEmp;
    }

    public Date getDateChk(){
        return this.dateChk;
    }
    public void setDateChk(Date dateChk){
        this.dateChk = dateChk;
    }

    public String getPkEmpChk(){
        return this.pkEmpChk;
    }
    public void setPkEmpChk(String pkEmpChk){
        this.pkEmpChk = pkEmpChk;
    }

    public String getNameEmpChk(){
        return this.nameEmpChk;
    }
    public void setNameEmpChk(String nameEmpChk){
        this.nameEmpChk = nameEmpChk;
    }

    public BigDecimal getCntTotal(){
        return this.cntTotal;
    }
    public void setCntTotal(BigDecimal cntTotal){
        this.cntTotal = cntTotal;
    }

    public BigDecimal getCntAdm(){
        return this.cntAdm;
    }
    public void setCntAdm(BigDecimal cntAdm){
        this.cntAdm = cntAdm;
    }

    public BigDecimal getCntTi(){
        return this.cntTi;
    }
    public void setCntTi(BigDecimal cntTi){
        this.cntTi = cntTi;
    }

    public BigDecimal getCntTo(){
        return this.cntTo;
    }
    public void setCntTo(BigDecimal cntTo){
        this.cntTo = cntTo;
    }

    public BigDecimal getCntDisc(){
        return this.cntDisc;
    }
    public void setCntDisc(BigDecimal cntDisc){
        this.cntDisc = cntDisc;
    }

    public BigDecimal getCntSev(){
        return this.cntSev;
    }
    public void setCntSev(BigDecimal cntSev){
        this.cntSev = cntSev;
    }

    public BigDecimal getCntCrit(){
        return this.cntCrit;
    }
    public void setCntCrit(BigDecimal cntCrit){
        this.cntCrit = cntCrit;
    }

    public BigDecimal getCntDeath(){
        return this.cntDeath;
    }
    public void setCntDeath(BigDecimal cntDeath){
        this.cntDeath = cntDeath;
    }

    public BigDecimal getCntNs(){
        return this.cntNs;
    }
    public void setCntNs(BigDecimal cntNs){
        this.cntNs = cntNs;
    }

    public BigDecimal getCntOpt(){
        return this.cntOpt;
    }
    public void setCntOpt(BigDecimal cntOpt){
        this.cntOpt = cntOpt;
    }

    public BigDecimal getCntDeli(){
        return this.cntDeli;
    }
    public void setCntDeli(BigDecimal cntDeli){
        this.cntDeli = cntDeli;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getDef1(){
        return this.def1;
    }
    public void setDef1(String def1){
        this.def1 = def1;
    }

    public String getDef2(){
        return this.def2;
    }
    public void setDef2(String def2){
        this.def2 = def2;
    }

    public String getDef3(){
        return this.def3;
    }
    public void setDef3(String def3){
        this.def3 = def3;
    }

    public String getDef4(){
        return this.def4;
    }
    public void setDef4(String def4){
        this.def4 = def4;
    }

    public String getDef5(){
        return this.def5;
    }
    public void setDef5(String def5){
        this.def5 = def5;
    }

    public String getDef6(){
        return this.def6;
    }
    public void setDef6(String def6){
        this.def6 = def6;
    }

    public String getDef7(){
        return this.def7;
    }
    public void setDef7(String def7){
        this.def7 = def7;
    }

    public String getDef8(){
        return this.def8;
    }
    public void setDef8(String def8){
        this.def8 = def8;
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