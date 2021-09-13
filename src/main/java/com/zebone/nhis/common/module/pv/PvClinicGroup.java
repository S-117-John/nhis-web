package com.zebone.nhis.common.module.pv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_CLINIC_GROUP  - 患者就诊_医疗组 
 *
 * @since 2016-09-23 11:24:31
 */
@Table(value="PV_CLINIC_GROUP")
public class PvClinicGroup extends BaseModule  {

	@PK
	@Field(value="PK_CLINICGROUP",id=KeyId.UUID)
    private String pkClinicgroup;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;

	@Field(value="PK_WG")
    private String pkWg;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="DT_PATIEVA")
    private String dtPatieva;

	@Field(value="DESC_PATIEVA")
    private String descPatieva;

    /** EU_STATUS - 0历史 1当前 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;


    public String getPkClinicgroup(){
        return this.pkClinicgroup;
    }
    public void setPkClinicgroup(String pkClinicgroup){
        this.pkClinicgroup = pkClinicgroup;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkDeptNs(){
        return this.pkDeptNs;
    }
    public void setPkDeptNs(String pkDeptNs){
        this.pkDeptNs = pkDeptNs;
    }

    public String getPkWg(){
        return this.pkWg;
    }
    public void setPkWg(String pkWg){
        this.pkWg = pkWg;
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

    public String getDtPatieva(){
        return this.dtPatieva;
    }
    public void setDtPatieva(String dtPatieva){
        this.dtPatieva = dtPatieva;
    }

    public String getDescPatieva(){
        return this.descPatieva;
    }
    public void setDescPatieva(String descPatieva){
        this.descPatieva = descPatieva;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }
}