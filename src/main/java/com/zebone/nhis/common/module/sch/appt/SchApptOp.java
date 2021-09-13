package com.zebone.nhis.common.module.sch.appt;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SCH_APPT_OP 
 *
 * @since 2016-09-18 10:43:57
 */
@Table(value="SCH_APPT_OP")
public class SchApptOp extends BaseModule  {

	@PK
	@Field(value="PK_APPTOP",id=KeyId.UUID)
    private String pkApptop;

	@Field(value="PK_SCHAPPT")
    private String pkSchappt;

	@Field(value="PK_OPT")
    private String pkOpt;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="FLAG_NOTICE")
    private String flagNotice;

	@Field(value="FLAG_EXEC")
    private String flagExec;

	@Field(value="PK_OPOCC")
    private String pkOpocc;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkApptop(){
        return this.pkApptop;
    }
    public void setPkApptop(String pkApptop){
        this.pkApptop = pkApptop;
    }

    public String getPkSchappt(){
        return this.pkSchappt;
    }
    public void setPkSchappt(String pkSchappt){
        this.pkSchappt = pkSchappt;
    }

    public String getPkOpt(){
        return this.pkOpt;
    }
    public void setPkOpt(String pkOpt){
        this.pkOpt = pkOpt;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getFlagNotice(){
        return this.flagNotice;
    }
    public void setFlagNotice(String flagNotice){
        this.flagNotice = flagNotice;
    }

    public String getFlagExec(){
        return this.flagExec;
    }
    public void setFlagExec(String flagExec){
        this.flagExec = flagExec;
    }

    public String getPkOpocc(){
        return this.pkOpocc;
    }
    public void setPkOpocc(String pkOpocc){
        this.pkOpocc = pkOpocc;
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
}