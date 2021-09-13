package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CP_REC 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="CP_REC")
public class CpRec extends BaseModule  {

	@PK
	@Field(value="PK_CPREC",id=KeyId.UUID)
    private String pkCprec;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_CPTEMP")
    private String pkCptemp;

	@Field(value="CODE_ICD")
    private String codeIcd;

	@Field(value="PK_CPPHASE")
    private String pkCpphase;

	@Field(value="DAYS_USED")
    private Integer daysUsed;

	@Field(value="EU_RECTYPE")
    private String euRectype;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="DATE_REJECT")
    private Date dateReject;

	@Field(value="PK_EMP_REJECT")
    private String pkEmpReject;

	@Field(value="NAME_EMP_REJECT")
    private String nameEmpReject;

	@Field(value="DATE_START")
    private Date dateStart;

	@Field(value="PK_EMP_START")
    private String pkEmpStart;

	@Field(value="NAME_EMP_START")
    private String nameEmpStart;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="PK_EMP_END")
    private String pkEmpEnd;

	@Field(value="NAME_EMP_END")
    private String nameEmpEnd;

	@Field(value="DATE_EXIT")
    private Date dateExit;

	@Field(value="PK_EMP_EXIT")
    private String pkEmpExit;

	@Field(value="NAME_EMP_EXIT")
    private String nameEmpExit;

	@Field(value="FLAG_TRANSFER")
    private String flagTransfer;

	@Field(value="PK_CPREC_PRE")
    private String pkCprecPre;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkCprec(){
        return this.pkCprec;
    }
    public void setPkCprec(String pkCprec){
        this.pkCprec = pkCprec;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkCptemp(){
        return this.pkCptemp;
    }
    public void setPkCptemp(String pkCptemp){
        this.pkCptemp = pkCptemp;
    }

    public String getCodeIcd(){
        return this.codeIcd;
    }
    public void setCodeIcd(String codeIcd){
        this.codeIcd = codeIcd;
    }

    public String getPkCpphase(){
        return this.pkCpphase;
    }
    public void setPkCpphase(String pkCpphase){
        this.pkCpphase = pkCpphase;
    }

    public Integer getDaysUsed(){
        return this.daysUsed;
    }
    public void setDaysUsed(Integer daysUsed){
        this.daysUsed = daysUsed;
    }

    public String getEuRectype(){
        return this.euRectype;
    }
    public void setEuRectype(String euRectype){
        this.euRectype = euRectype;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public Date getDateReject(){
        return this.dateReject;
    }
    public void setDateReject(Date dateReject){
        this.dateReject = dateReject;
    }

    public String getPkEmpReject(){
        return this.pkEmpReject;
    }
    public void setPkEmpReject(String pkEmpReject){
        this.pkEmpReject = pkEmpReject;
    }

    public String getNameEmpReject(){
        return this.nameEmpReject;
    }
    public void setNameEmpReject(String nameEmpReject){
        this.nameEmpReject = nameEmpReject;
    }

    public Date getDateStart(){
        return this.dateStart;
    }
    public void setDateStart(Date dateStart){
        this.dateStart = dateStart;
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

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
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

    public Date getDateExit(){
        return this.dateExit;
    }
    public void setDateExit(Date dateExit){
        this.dateExit = dateExit;
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

    public String getFlagTransfer(){
        return this.flagTransfer;
    }
    public void setFlagTransfer(String flagTransfer){
        this.flagTransfer = flagTransfer;
    }

    public String getPkCprecPre(){
        return this.pkCprecPre;
    }
    public void setPkCprecPre(String pkCprecPre){
        this.pkCprecPre = pkCprecPre;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}