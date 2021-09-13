package com.zebone.nhis.common.module.ex.oi;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_INFUSION_REGISTER 
 *
 * @since 2017-10-31 10:00:23
 */
@Table(value="EX_INFUSION_REGISTER")
public class ExInfusionRegister extends BaseModule  {

	@PK
	@Field(value="PK_INFUREG",id=KeyId.UUID)
    private String pkInfureg;

	@Field(value="REGISTER_NO")
    private String registerNo;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PRES")
    private String pkPres;

	@Field(value="PK_SETTLE")
    private String pkSettle;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="DATE_REG")
    private Date dateReg;

	@Field(value="EMP_REG")
    private String empReg;

	@Field(value="PK_BED")
    private String pkBed;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
    public String getPkInfureg(){
        return this.pkInfureg;
    }
    public void setPkInfureg(String pkInfureg){
        this.pkInfureg = pkInfureg;
    }

    public String getRegisterNo(){
        return this.registerNo;
    }
    public void setRegisterNo(String registerNo){
        this.registerNo = registerNo;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkPres(){
        return this.pkPres;
    }
    public void setPkPres(String pkPres){
        this.pkPres = pkPres;
    }

    public String getPkSettle(){
        return this.pkSettle;
    }
    public void setPkSettle(String pkSettle){
        this.pkSettle = pkSettle;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public Date getDateReg(){
        return this.dateReg;
    }
    public void setDateReg(Date dateReg){
        this.dateReg = dateReg;
    }

    public String getEmpReg(){
        return this.empReg;
    }
    public void setEmpReg(String empReg){
        this.empReg = empReg;
    }

    public String getPkBed(){
        return this.pkBed;
    }
    public void setPkBed(String pkBed){
        this.pkBed = pkBed;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
         
}