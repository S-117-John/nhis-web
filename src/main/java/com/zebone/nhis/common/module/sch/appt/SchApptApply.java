package com.zebone.nhis.common.module.sch.appt;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SCH_APPT_APPLY - sch_appt_apply 
 *
 * @since 2016-10-17 09:55:04
 */
@Table(value="SCH_APPT_APPLY")
public class SchApptApply extends BaseModule  {

	@PK
	@Field(value="pk_schapply",id=KeyId.UUID)
    private String pkSchapply;

    /** eu_schclass - 0 门诊出诊；1 医技排班；2 床位预约；3 手术排班 */
	@Field(value="eu_schclass")
    private String euSchclass;

	@Field(value="pk_pi")
    private String pkPi;

	@Field(value="date_begin")
    private Date dateBegin;

	@Field(value="date_end")
    private Date dateEnd;

	@Field(value="pk_dateslot")
    private String pkDateslot;

	@Field(value="pk_schsrv")
    private String pkSchsrv;

	@Field(value="pk_schres")
    private String pkSchres;

	@Field(value="date_reg")
    private Date dateReg;

	@Field(value="pk_emp_reg")
    private String pkEmpReg;

	@Field(value="name_emp_reg")
    private String nameEmpReg;

    /** eu_status - 0 申请，1 预约，9取消 */
	@Field(value="eu_status")
    private String euStatus;

	@Field(value="flag_cancel")
    private String flagCancel;

	@Field(value="pk_schappt")
    private String pkSchappt;

	@Field(value="date_cancel")
    private Date dateCancel;

	@Field(value="pk_emp_cancel")
    private String pkEmpCancel;

	@Field(value="name_emp_cancel")
    private String nameEmpCancel;

	@Field(value="note")
    private String note;

	@Field(value="modity_time")
    private Date modityTime;


    public String getPkSchapply(){
        return this.pkSchapply;
    }
    public void setPkSchapply(String pkSchapply){
        this.pkSchapply = pkSchapply;
    }

    public String getEuSchclass(){
        return this.euSchclass;
    }
    public void setEuSchclass(String euSchclass){
        this.euSchclass = euSchclass;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }

    public String getPkDateslot(){
        return this.pkDateslot;
    }
    public void setPkDateslot(String pkDateslot){
        this.pkDateslot = pkDateslot;
    }

    public String getPkSchsrv(){
        return this.pkSchsrv;
    }
    public void setPkSchsrv(String pkSchsrv){
        this.pkSchsrv = pkSchsrv;
    }

    public String getPkSchres(){
        return this.pkSchres;
    }
    public void setPkSchres(String pkSchres){
        this.pkSchres = pkSchres;
    }

    public Date getDateReg(){
        return this.dateReg;
    }
    public void setDateReg(Date dateReg){
        this.dateReg = dateReg;
    }

    public String getPkEmpReg(){
        return this.pkEmpReg;
    }
    public void setPkEmpReg(String pkEmpReg){
        this.pkEmpReg = pkEmpReg;
    }

    public String getNameEmpReg(){
        return this.nameEmpReg;
    }
    public void setNameEmpReg(String nameEmpReg){
        this.nameEmpReg = nameEmpReg;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getFlagCancel(){
        return this.flagCancel;
    }
    public void setFlagCancel(String flagCancel){
        this.flagCancel = flagCancel;
    }

    public String getPkSchappt(){
        return this.pkSchappt;
    }
    public void setPkSchappt(String pkSchappt){
        this.pkSchappt = pkSchappt;
    }

    public Date getDateCancel(){
        return this.dateCancel;
    }
    public void setDateCancel(Date dateCancel){
        this.dateCancel = dateCancel;
    }

    public String getPkEmpCancel(){
        return this.pkEmpCancel;
    }
    public void setPkEmpCancel(String pkEmpCancel){
        this.pkEmpCancel = pkEmpCancel;
    }

    public String getNameEmpCancel(){
        return this.nameEmpCancel;
    }
    public void setNameEmpCancel(String nameEmpCancel){
        this.nameEmpCancel = nameEmpCancel;
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