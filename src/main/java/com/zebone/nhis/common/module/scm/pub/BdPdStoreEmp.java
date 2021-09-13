package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: bd_pd_store_emp 
 *
 * @since 2017-09-23 03:39:51
 */
@Table(value="bd_pd_store_emp")
public class BdPdStoreEmp extends BaseModule  {

	@PK
	@Field(value="pk_pdstoreemp",id=KeyId.UUID)
    private String pkPdstoreemp;

	@Field(value="pk_pdstore")
    private String pkPdstore;

	@Field(value="pk_emp")
    private String pkEmp;

	@Field(value="name_emp")
    private String nameEmp;

	@Field(value="date_entry")
    private Date dateEntry;

	@Field(value="pk_emp_entry")
    private String pkEmpEntry;

	@Field(value="name_emp_entry")
    private String nameEmpEntry;

	@Field(value="modifier")
    private String modifier;

	@Field(value="modity_time")
    private Date modityTime;


    public String getPkPdstoreemp(){
        return this.pkPdstoreemp;
    }
    public void setPkPdstoreemp(String pkPdstoreemp){
        this.pkPdstoreemp = pkPdstoreemp;
    }

    public String getPkPdstore(){
        return this.pkPdstore;
    }
    public void setPkPdstore(String pkPdstore){
        this.pkPdstore = pkPdstore;
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

    public Date getDateEntry(){
        return this.dateEntry;
    }
    public void setDateEntry(Date dateEntry){
        this.dateEntry = dateEntry;
    }

    public String getPkEmpEntry(){
        return this.pkEmpEntry;
    }
    public void setPkEmpEntry(String pkEmpEntry){
        this.pkEmpEntry = pkEmpEntry;
    }

    public String getNameEmpEntry(){
        return this.nameEmpEntry;
    }
    public void setNameEmpEntry(String nameEmpEntry){
        this.nameEmpEntry = nameEmpEntry;
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