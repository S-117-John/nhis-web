package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PV_DIAG_DT 
 *
 * @since 2018-12-28 05:07:52
 */
@Table(value="PV_DIAG_DT")
public class PvDiagDt extends BaseModule  {

	@PK
	@Field(value="PK_PVDIAGDT",id=KeyId.UUID)
    private String pkPvdiagdt;

	@Field(value="PK_PVDIAG")
    private String pkPvdiag;

	@Field(value="EU_CONTYPE")
    private String euContype;

	@Field(value="PK_CNDIAGCOMT")
    private String pkCndiagcomt;

	@Field(value="VAL")
    private String val;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	@Field(value="CNT_GRAV")
	private String cntGrav ;
	@Field(value="CNT_LABOR")
	private String cntLabor ;
	@Field(value="WEEK_GRAV")
	private String weekGrav ;
	@Field(value="DAY_GRAV")
	private String dayGrav ;
	@Field(value="DT_LABRESULT")
	private String dtLabresult ;
	@Field(value="DT_LABORTYPE")
	private String dtLabortype;
	@Field(value="FETAL_POS ")
	private String fetalPos ;

    public String getPkPvdiagdt(){
        return this.pkPvdiagdt;
    }
    public void setPkPvdiagdt(String pkPvdiagdt){
        this.pkPvdiagdt = pkPvdiagdt;
    }

    public String getPkPvdiag(){
        return this.pkPvdiag;
    }
    public void setPkPvdiag(String pkPvdiag){
        this.pkPvdiag = pkPvdiag;
    }

    public String getEuContype(){
        return this.euContype;
    }
    public void setEuContype(String euContype){
        this.euContype = euContype;
    }

    public String getPkCndiagcomt(){
        return this.pkCndiagcomt;
    }
    public void setPkCndiagcomt(String pkCndiagcomt){
        this.pkCndiagcomt = pkCndiagcomt;
    }

    public String getVal(){
        return this.val;
    }
    public void setVal(String val){
        this.val = val;
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
	public String getCntGrav() {
		return cntGrav;
	}
	public void setCntGrav(String cntGrav) {
		this.cntGrav = cntGrav;
	}
	public String getCntLabor() {
		return cntLabor;
	}
	public void setCntLabor(String cntLabor) {
		this.cntLabor = cntLabor;
	}
	public String getWeekGrav() {
		return weekGrav;
	}
	public void setWeekGrav(String weekGrav) {
		this.weekGrav = weekGrav;
	}
	public String getDayGrav() {
		return dayGrav;
	}
	public void setDayGrav(String dayGrav) {
		this.dayGrav = dayGrav;
	}
	public String getDtLabresult() {
		return dtLabresult;
	}
	public void setDtLabresult(String dtLabresult) {
		this.dtLabresult = dtLabresult;
	}
	public String getDtLabortype() {
		return dtLabortype;
	}
	public void setDtLabortype(String dtLabortype) {
		this.dtLabortype = dtLabortype;
	}
	public String getFetalPos() {
		return fetalPos;
	}
	public void setFetalPos(String fetalPos) {
		this.fetalPos = fetalPos;
	}
    
}