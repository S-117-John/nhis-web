package com.zebone.nhis.common.module.ma.self;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_OU_SSS_MACHINE 
 *
 * @since 2016-10-13 02:37:43
 */
@Table(value="BD_OU_SSS_MACHINE")
public class BdOuSssMachine   {

	@PK
	@Field(value="PK_MACHINE",id=KeyId.UUID)
    private String pkMachine;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="IP_ADDRESS")
    private String ipAddress;

	@Field(value="NAME_MACHINE")
    private String nameMachine;

	@Field(value="PWD")
    private String pwd;

    /** FLAG_ACTIVE - 1:是
0:否 */
	@Field(value="FLAG_ACTIVE")
    private String flagActive;

    /** IS_LOCK - 1:是
0:否 */
	@Field(value="IS_LOCK")
    private String isLock;

	@Field(value="CAID")
    private String caid;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkMachine(){
        return this.pkMachine;
    }
    public void setPkMachine(String pkMachine){
        this.pkMachine = pkMachine;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getIpAddress(){
        return this.ipAddress;
    }
    public void setIpAddress(String ipAddress){
        this.ipAddress = ipAddress;
    }

    public String getNameMachine(){
        return this.nameMachine;
    }
    public void setNameMachine(String nameMachine){
        this.nameMachine = nameMachine;
    }

    public String getPwd(){
        return this.pwd;
    }
    public void setPwd(String pwd){
        this.pwd = pwd;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
    }

    public String getIsLock(){
        return this.isLock;
    }
    public void setIsLock(String isLock){
        this.isLock = isLock;
    }

    public String getCaid(){
        return this.caid;
    }
    public void setCaid(String caid){
        this.caid = caid;
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

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}