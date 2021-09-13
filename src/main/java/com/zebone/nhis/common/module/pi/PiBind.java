package com.zebone.nhis.common.module.pi;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: PI_BIND 
 *
 * @since 2019-10-30 11:58:47
 */
@Table(value="PI_BIND")
public class PiBind   {

	@PK
	@Field(value="PK_BIND",id=KeyId.UUID)
    private String pkBind;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="LOGON")
    private String logon;

	@Field(value="PASSWORD")
    private String password;

	@Field(value="DATE_REGISTRE")
    private Date dateRegistre;

	@Field(value="DATE_STOP")
    private Date dateStop;

	@Field(value="DT_PLATFORM")
    private String dtPlatform;

	@Field(value="FLAG_VALID")
    private String flagValid;

	@Field(value="CREATOR")
    private String creator;

	@Field(value="CREATE_TIME")
    private Date createTime;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(value="TS")
    private Date ts;


    public String getPkBind(){
        return this.pkBind;
    }
    public void setPkBind(String pkBind){
        this.pkBind = pkBind;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getLogon(){
        return this.logon;
    }
    public void setLogon(String logon){
        this.logon = logon;
    }

    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public Date getDateRegistre(){
        return this.dateRegistre;
    }
    public void setDateRegistre(Date dateRegistre){
        this.dateRegistre = dateRegistre;
    }

    public Date getDateStop(){
        return this.dateStop;
    }
    public void setDateStop(Date dateStop){
        this.dateStop = dateStop;
    }

    public String getDtPlatform(){
        return this.dtPlatform;
    }
    public void setDtPlatform(String dtPlatform){
        this.dtPlatform = dtPlatform;
    }

    public String getFlagValid(){
        return this.flagValid;
    }
    public void setFlagValid(String flagValid){
        this.flagValid = flagValid;
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

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}