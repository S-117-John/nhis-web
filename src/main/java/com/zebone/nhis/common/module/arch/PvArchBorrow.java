package com.zebone.nhis.common.module.arch;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PV_ARCH_BORROW 
 *
 * @since 2017-04-27 10:56:33
 */
@Table(value="PV_ARCH_BORROW")
public class PvArchBorrow   {

	@PK
	@Field(value="PK_BORROW",id=KeyId.UUID)
    private String pkBorrow;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_ARCHIVE")
    private String pkArchive;

	@Field(value="DATE_AP")
    private Date dateAp;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="DATE_CHK")
    private Date dateChk;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="FLAG_CHK")
    private String flagChk;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_EMP_AP")
    private String pkEmpAp;

	@Field(value="NAME_EMP_AP")
    private String nameEmpAp;

	@Field(value="DAYS")
    private String days;

	@Field(value="FLAG_CANC")
    private String flagCanc;

	@Field(value="DATE_CANC")
    private Date dateCanc;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	@Field(value="FLAG_DEL")
    private String flagDel;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkBorrow(){
        return this.pkBorrow;
    }
    public void setPkBorrow(String pkBorrow){
        this.pkBorrow = pkBorrow;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkArchive(){
        return this.pkArchive;
    }
    public void setPkArchive(String pkArchive){
        this.pkArchive = pkArchive;
    }

    public Date getDateAp(){
        return this.dateAp;
    }
    public void setDateAp(Date dateAp){
        this.dateAp = dateAp;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
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

    public String getFlagChk(){
        return this.flagChk;
    }
    public void setFlagChk(String flagChk){
        this.flagChk = flagChk;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkEmpAp(){
        return this.pkEmpAp;
    }
    public void setPkEmpAp(String pkEmpAp){
        this.pkEmpAp = pkEmpAp;
    }

    public String getNameEmpAp(){
        return this.nameEmpAp;
    }
    public void setNameEmpAp(String nameEmpAp){
        this.nameEmpAp = nameEmpAp;
    }

    public String getDays(){
        return this.days;
    }
    public void setDays(String days){
        this.days = days;
    }

    public String getFlagCanc(){
        return this.flagCanc;
    }
    public void setFlagCanc(String flagCanc){
        this.flagCanc = flagCanc;
    }

    public Date getDateCanc(){
        return this.dateCanc;
    }
    public void setDateCanc(Date dateCanc){
        this.dateCanc = dateCanc;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    public String getFlagDel(){
        return this.flagDel;
    }
    public void setFlagDel(String flagDel){
        this.flagDel = flagDel;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}