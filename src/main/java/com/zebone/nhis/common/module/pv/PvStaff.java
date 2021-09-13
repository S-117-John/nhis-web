package com.zebone.nhis.common.module.pv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_STAFF  - 患者就诊_医护人员 
 *
 * @since 2016-09-23 11:29:57
 */
@Table(value="PV_STAFF")
public class PvStaff extends BaseModule  {

	@PK
	@Field(value="PK_STAFF",id=KeyId.UUID)
    private String pkStaff;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="NAME_EMP")
    private String nameEmp;

	@Field(value="DT_ROLE")
    private String dtRole;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="DATE_END")
    private Date dateEnd;

	@Field(value="DT_PATIEVA")
    private String dtPatieva;

	@Field(value="DESC_PATIEVA")
    private String descPatieva;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;


    public String getPkStaff(){
        return this.pkStaff;
    }
    public void setPkStaff(String pkStaff){
        this.pkStaff = pkStaff;
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

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getNameEmp(){
        return this.nameEmp;
    }
    public void setNameEmp(String nameEmp){
        this.nameEmp = nameEmp;
    }

    public String getDtRole(){
        return this.dtRole;
    }
    public void setDtRole(String dtRole){
        this.dtRole = dtRole;
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

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }
}