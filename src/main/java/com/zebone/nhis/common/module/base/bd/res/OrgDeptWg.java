package com.zebone.nhis.common.module.base.bd.res;


import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 部门_医疗组
 * Table: ORG_DEPT_WG  - org_dept_wg 
 *
 * @since 2016-08-23 10:33:37
 */
@Table(value="ORG_DEPT_WG")
public class OrgDeptWg extends BaseModule  {

	@PK
	@Field(value="PK_WG",id=KeyId.UUID)
    private String pkWg;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="CODE_WG")
    private String codeWg;

	@Field(value="NAME_WG")
    private String nameWg;

	@Field(value="DESC_WG")
    private String descWg;
	
	
	@Field(value="PK_EMP_ENTRY")
    private String pkEmpEntry;
	
	@Field(value="NAME_EMP_ENTRY")
    private String nameEmpEntry;
	
	@Field(value="DATE_ENTRY")
    private Date dateEntry;
	
	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;
	
	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;
	
	@Field(value="DATE_CHK")
    private Date dateChk;
	
	@Field(value="PK_EMP_PUB")
    private String pkEmpPub;
	
	@Field(value="NAME_EMP_PUB")
    private String nameEmpPub;
	
	@Field(value="DATE_PUB")
    private Date datePub;
	
	@Field(value="EU_STATUS")
    private String euStatus;
	
	
	public Date getDateEntry() {
		return dateEntry;
	}
	public void setDateEntry(Date dateEntry) {
		this.dateEntry = dateEntry;
	}
	public Date getDateChk() {
		return dateChk;
	}
	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}
	public Date getDatePub() {
		return datePub;
	}
	public void setDatePub(Date datePub) {
		this.datePub = datePub;
	}

    public String getPkWg(){
        return this.pkWg;
    }
    public void setPkWg(String pkWg){
        this.pkWg = pkWg;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getCodeWg(){
        return this.codeWg;
    }
    public void setCodeWg(String codeWg){
        this.codeWg = codeWg;
    }

    public String getNameWg(){
        return this.nameWg;
    }
    public void setNameWg(String nameWg){
        this.nameWg = nameWg;
    }

    public String getDescWg(){
        return this.descWg;
    }
    public void setDescWg(String descWg){
        this.descWg = descWg;
    }
    
    public String getPkEmpEntry() {
		return pkEmpEntry;
	}
	public void setPkEmpEntry(String pkEmpEntry) {
		this.pkEmpEntry = pkEmpEntry;
	}
	public String getNameEmpEntry() {
		return nameEmpEntry;
	}
	public void setNameEmpEntry(String nameEmpEntry) {
		this.nameEmpEntry = nameEmpEntry;
	}
	 
	public String getPkEmpChk() {
		return pkEmpChk;
	}
	public void setPkEmpChk(String pkEmpChk) {
		this.pkEmpChk = pkEmpChk;
	}
	public String getNameEmpChk() {
		return nameEmpChk;
	}
	public void setNameEmpChk(String nameEmpChk) {
		this.nameEmpChk = nameEmpChk;
	}
	 
	public String getPkEmpPub() {
		return pkEmpPub;
	}
	public void setPkEmpPub(String pkEmpPub) {
		this.pkEmpPub = pkEmpPub;
	}
	public String getNameEmpPub() {
		return nameEmpPub;
	}
	public void setNameEmpPub(String nameEmpPub) {
		this.nameEmpPub = nameEmpPub;
	}
	 
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
}