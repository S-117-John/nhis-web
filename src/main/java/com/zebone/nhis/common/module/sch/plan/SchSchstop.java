package com.zebone.nhis.common.module.sch.plan;


import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: SCH_SCHSTOP  - sch_schstop 
 *
 * @since 2021-03-25 10:37:49
 */
@Table(value="SCH_SCHSTOP")
public class SchSchstop extends BaseModule  {

	@PK
	@Field(value="PK_SCHSTOP",id=KeyId.UUID)
    private String pkSchstop;

	@Field(value="PK_EMP_STOP")
    private String pkEmpStop;

	@Field(value="CODE_EMP_STOP")
    private String codeEmpStop;
		
	@Field(value="NAME_EMP_STOP")
    private String nameEmpStop;

	@Field(value="DATE_STOP")
    private Date dateStop;

	@Field(value="NOTE")
    private String note;
	
	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	//午别标志
	@Field(value="EU_NOON")
    private String euNoon;
	
	//计划停诊结束时间
	@Field(value="DATE_END")
    private Date dateEnd;


	public String getPkSchstop() {
		return pkSchstop;
	}

	public String getPkEmpStop() {
		return pkEmpStop;
	}

	public String getCodeEmpStop() {
		return codeEmpStop;
	}

	public String getNameEmpStop() {
		return nameEmpStop;
	}

	public Date getDateStop() {
		return dateStop;
	}

	public String getNote() {
		return note;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setPkSchstop(String pkSchstop) {
		this.pkSchstop = pkSchstop;
	}

	public void setPkEmpStop(String pkEmpStop) {
		this.pkEmpStop = pkEmpStop;
	}

	public void setCodeEmpStop(String codeEmpStop) {
		this.codeEmpStop = codeEmpStop;
	}

	public void setNameEmpStop(String nameEmpStop) {
		this.nameEmpStop = nameEmpStop;
	}

	public void setDateStop(Date dateStop) {
		this.dateStop = dateStop;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	
	public String getEuNoon() {
		return euNoon;
	}

	public void setEuNoon(String euNoon) {
		this.euNoon = euNoon;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	
}