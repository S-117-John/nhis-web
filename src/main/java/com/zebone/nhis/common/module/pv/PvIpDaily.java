package com.zebone.nhis.common.module.pv;

import java.util.Date;
import java.math.BigDecimal;

import org.springframework.format.annotation.DateTimeFormat;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PV_IP_DAILY 
 *
 * @since 2018-03-27 03:21:02
 */
@Table(value="PV_IP_DAILY")
public class PvIpDaily extends BaseModule  {

	@PK
	@Field(value="PK_IPDAILY",id=KeyId.UUID)
    private String pkIpdaily;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;
	
	@Field(value="DATE_SA")
    private Date dateSa;

	@Field(value="BEDNUM")
    private BigDecimal bednum;

	@Field(value="BEDNUM_OPEN")
    private BigDecimal bednumOpen;

	@Field(value="INHOSP_YD")
    private BigDecimal inhospYd;

	@Field(value="ADMIT")
    private BigDecimal admit;

	@Field(value="TRANS_IN")
    private BigDecimal transIn;

	@Field(value="TRANS_OUT")
    private BigDecimal transOut;

	@Field(value="DISCHARGE")
    private BigDecimal discharge;

	@Field(value="INHOSP")
    private BigDecimal inhosp;

	@Field(value="RISKYNUM")
    private BigDecimal riskynum;

	@Field(value="SEVERENUM")
    private BigDecimal severenum;

	@Field(value="DEATHNUM")
    private BigDecimal deathnum;
	
	@Field(value="NURSE_SPEC")
    private BigDecimal nurseSpec;

	@Field(value="NURSE_FIRST")
    private BigDecimal nurseFirst;

	@Field(value="ACCOMNUM")
    private BigDecimal accomnum;

	@Field(value="DAYS_TOTAL")
    private BigDecimal daysTotal;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkIpdaily(){
        return this.pkIpdaily;
    }
    public void setPkIpdaily(String pkIpdaily){
        this.pkIpdaily = pkIpdaily;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkDeptNs(){
        return this.pkDeptNs;
    }
    public void setPkDeptNs(String pkDeptNs){
        this.pkDeptNs = pkDeptNs;
    }

    public Date getDateSa(){
        return this.dateSa;
    }
    public void setDateSa(Date dateSa){
        this.dateSa = dateSa;
    }

    public BigDecimal getBednum(){
        return this.bednum;
    }
    public void setBednum(BigDecimal bednum){
        this.bednum = bednum;
    }

    public BigDecimal getBednumOpen(){
        return this.bednumOpen;
    }
    public void setBednumOpen(BigDecimal bednumOpen){
        this.bednumOpen = bednumOpen;
    }

    public BigDecimal getInhospYd(){
        return this.inhospYd;
    }
    public void setInhospYd(BigDecimal inhospYd){
        this.inhospYd = inhospYd;
    }

    public BigDecimal getAdmit(){
        return this.admit;
    }
    public void setAdmit(BigDecimal admit){
        this.admit = admit;
    }

    public BigDecimal getTransIn(){
        return this.transIn;
    }
    public void setTransIn(BigDecimal transIn){
        this.transIn = transIn;
    }

    public BigDecimal getTransOut(){
        return this.transOut;
    }
    public void setTransOut(BigDecimal transOut){
        this.transOut = transOut;
    }

    public BigDecimal getDischarge(){
        return this.discharge;
    }
    public void setDischarge(BigDecimal discharge){
        this.discharge = discharge;
    }

    public BigDecimal getInhosp(){
        return this.inhosp;
    }
    public void setInhosp(BigDecimal inhosp){
        this.inhosp = inhosp;
    }

    public BigDecimal getRiskynum(){
        return this.riskynum;
    }
    public void setRiskynum(BigDecimal riskynum){
        this.riskynum = riskynum;
    }

    public BigDecimal getSeverenum(){
        return this.severenum;
    }
    public void setSeverenum(BigDecimal severenum){
        this.severenum = severenum;
    }

    public BigDecimal getDeathnum(){
        return this.deathnum;
    }
    public void setDeathnum(BigDecimal deathnum){
        this.deathnum = deathnum;
    }

    public BigDecimal getNurseSpec(){
        return this.nurseSpec;
    }
    public void setNurseSpec(BigDecimal nurseSpec){
        this.nurseSpec = nurseSpec;
    }

    public BigDecimal getNurseFirst(){
        return this.nurseFirst;
    }
    public void setNurseFirst(BigDecimal nurseFirst){
        this.nurseFirst = nurseFirst;
    }

    public BigDecimal getAccomnum(){
        return this.accomnum;
    }
    public void setAccomnum(BigDecimal accomnum){
        this.accomnum = accomnum;
    }

    public BigDecimal getDaysTotal(){
        return this.daysTotal;
    }
    public void setDaysTotal(BigDecimal daysTotal){
        this.daysTotal = daysTotal;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}