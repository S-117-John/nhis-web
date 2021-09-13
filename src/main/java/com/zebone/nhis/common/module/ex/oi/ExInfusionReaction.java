package com.zebone.nhis.common.module.ex.oi;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_INFUSION_REACTION 
 *
 * @since 2017-11-10 05:06:53
 */
@Table(value="EX_INFUSION_REACTION")
public class ExInfusionReaction extends BaseModule  {

	@PK
	@Field(value="PK_INFUREA",id=KeyId.UUID)
    private String pkInfurea;

	@Field(value="PK_INFUREG")
    private String pkInfureg;

	@Field(value="PK_INFUOCC")
    private String pkInfuocc;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="SPEC")
    private String spec;

	@Field(value="NOTE")
    private String note;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="DATE_RECORD")
    private Date dateRecord;

	@Field(value="EMP_RECORD")
    private String empRecord;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkInfurea(){
        return this.pkInfurea;
    }
    public void setPkInfurea(String pkInfurea){
        this.pkInfurea = pkInfurea;
    }

    public String getPkInfureg(){
        return this.pkInfureg;
    }
    public void setPkInfureg(String pkInfureg){
        this.pkInfureg = pkInfureg;
    }

    public String getPkInfuocc(){
        return this.pkInfuocc;
    }
    public void setPkInfuocc(String pkInfuocc){
        this.pkInfuocc = pkInfuocc;
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

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getSpec(){
        return this.spec;
    }
    public void setSpec(String spec){
        this.spec = spec;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public Date getDateRecord(){
        return this.dateRecord;
    }
    public void setDateRecord(Date dateRecord){
        this.dateRecord = dateRecord;
    }

    public String getEmpRecord(){
        return this.empRecord;
    }
    public void setEmpRecord(String empRecord){
        this.empRecord = empRecord;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}