package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CP_REC_PHASE 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="CP_REC_PHASE")
public class CpRecPhase extends BaseModule  {

	@PK
	@Field(value="PK_RECPHASE",id=KeyId.UUID)
    private String pkRecphase;

	@Field(value="PK_CPREC")
    private String pkCprec;

	@Field(value="PK_CPPHASE")
    private String pkCpphase;

	@Field(value="DATE_START")
    private Date dateStart;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="DATE_EXIT")
    private Date dateExit;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_EMP_START")
    private String pkEmpStart;

	@Field(value="NAME_EMP_START")
    private String nameEmpStart;

	@Field(value="PK_EMP_END")
    private String pkEmpEnd;

	@Field(value="NAME_EMP_END")
    private String nameEmpEnd;

	@Field(value="PK_EMP_EXIT")
    private String pkEmpExit;

	@Field(value="NAME_EMP_EXIT")
    private String nameEmpExit;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkRecphase(){
        return this.pkRecphase;
    }
    public void setPkRecphase(String pkRecphase){
        this.pkRecphase = pkRecphase;
    }

    public String getPkCprec(){
        return this.pkCprec;
    }
    public void setPkCprec(String pkCprec){
        this.pkCprec = pkCprec;
    }

    public Date getDateStart(){
        return this.dateStart;
    }
    public void setDateStart(Date dateStart){
        this.dateStart = dateStart;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }

    public Date getDateExit(){
        return this.dateExit;
    }
    public void setDateExit(Date dateExit){
        this.dateExit = dateExit;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkEmpStart(){
        return this.pkEmpStart;
    }
    public void setPkEmpStart(String pkEmpStart){
        this.pkEmpStart = pkEmpStart;
    }

    public String getNameEmpStart(){
        return this.nameEmpStart;
    }
    public void setNameEmpStart(String nameEmpStart){
        this.nameEmpStart = nameEmpStart;
    }

    public String getPkEmpEnd(){
        return this.pkEmpEnd;
    }
    public void setPkEmpEnd(String pkEmpEnd){
        this.pkEmpEnd = pkEmpEnd;
    }

    public String getNameEmpEnd(){
        return this.nameEmpEnd;
    }
    public void setNameEmpEnd(String nameEmpEnd){
        this.nameEmpEnd = nameEmpEnd;
    }

    public String getPkEmpExit(){
        return this.pkEmpExit;
    }
    public void setPkEmpExit(String pkEmpExit){
        this.pkEmpExit = pkEmpExit;
    }

    public String getNameEmpExit(){
        return this.nameEmpExit;
    }
    public void setNameEmpExit(String nameEmpExit){
        this.nameEmpExit = nameEmpExit;
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
	public String getPkCpphase() {
		return pkCpphase;
	}
	public void setPkCpphase(String pkCpphase) {
		this.pkCpphase = pkCpphase;
	}
}