package com.zebone.nhis.common.module.ma.self;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_OU_SSS_MACHINE_OPER 
 *
 * @since 2016-10-13 02:38:09
 */
@Table(value="BD_OU_SSS_MACHINE_OPER")
public class BdOuSssMachineOper   {

	@PK
	@Field(value="PK_MACHINEOPER",id=KeyId.UUID)
    private String pkMachineoper;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_MACHINE")
    private String pkMachine;

	@Field(value="PK_OPER")
    private String pkOper;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkMachineoper(){
        return this.pkMachineoper;
    }
    public void setPkMachineoper(String pkMachineoper){
        this.pkMachineoper = pkMachineoper;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkMachine(){
        return this.pkMachine;
    }
    public void setPkMachine(String pkMachine){
        this.pkMachine = pkMachine;
    }

    public String getPkOper(){
        return this.pkOper;
    }
    public void setPkOper(String pkOper){
        this.pkOper = pkOper;
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