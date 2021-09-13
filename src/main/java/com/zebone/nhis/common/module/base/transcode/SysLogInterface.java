package com.zebone.nhis.common.module.base.transcode;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SYS_LOG_INTERFACE 
 *
 * @since 2020-08-07 05:55:50
 */
@Table(value="SYS_LOG_INTERFACE")
public class SysLogInterface   {

	@PK
	@Field(value="PK_LOG_INTF",id=KeyId.UUID)
    private String pkLogIntf;

	@Field(value="NAME_INTF")
    private String nameIntf;

	@Field(value="TYPE")
    private String type;

	@Field(value="LOG")
    private String log;

	@Field(value="IN_PARAM")
    private String inParam;

	@Field(value="OUT_PARAM")
    private String outParam;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(value="EXEC_TIME")
    private Long execTime;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkLogIntf(){
        return this.pkLogIntf;
    }
    public void setPkLogIntf(String pkLogIntf){
        this.pkLogIntf = pkLogIntf;
    }

    public String getNameIntf(){
        return this.nameIntf;
    }
    public void setNameIntf(String nameIntf){
        this.nameIntf = nameIntf;
    }

    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type = type;
    }

    public String getLog(){
        return this.log;
    }
    public void setLog(String log){
        this.log = log;
    }

    public String getInParam(){
        return this.inParam;
    }
    public void setInParam(String inParam){
        this.inParam = inParam;
    }

    public String getOutParam(){
        return this.outParam;
    }
    public void setOutParam(String outParam){
        this.outParam = outParam;
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

    public Long getExecTime(){
        return this.execTime;
    }
    public void setExecTime(Long execTime){
        this.execTime = execTime;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}