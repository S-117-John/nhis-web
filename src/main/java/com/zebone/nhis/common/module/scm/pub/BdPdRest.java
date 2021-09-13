package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: BD_PD_REST
 *
 */
@Table(value="BD_PD_REST")
public class BdPdRest extends BaseModule {
	
	@PK
	@Field(value="PK_PDREST",id=KeyId.UUID)
    private String pkPdrest;
	
	@Field(value="PK_PD")
	private String pkPd;
	
	/**0医生，1科室，2诊断*/
	@Field(value="EU_CTRLTYPE")
	private String euCtrltype;
	
	@Field(value="PK_EMP")
	private String pkEmp;
	
	@Field(value="NAME_EMP")
	private String nameEmp;
	
	@Field(value="PK_DEPT")
	private String pkDept;
	
	@Field(value="PK_DIAG")
	private String pkDiag;

	@Field(value="PK_PI")
	private String pkPi;

	@Field(value="NAME_PI")
	private String namePi;

	@Field(value="AMOUNT")
	private Double amount;

	@Field(value="PK_UNIT")
	private String pkUnit;

	@Field(value="DATE_END")
	private Date dateEnd;
	
	@Field(value="PK_DEPT_PHARM")
	private String pkDeptPharm;
	
	@Field(value="MODITY_TIME")
	private Date modityTime;

	public String getPkPdrest() {
		return pkPdrest;
	}

	public void setPkPdrest(String pkPdrest) {
		this.pkPdrest = pkPdrest;
	}

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}

	public String getEuCtrltype() {
		return euCtrltype;
	}

	public void setEuCtrltype(String euCtrltype) {
		this.euCtrltype = euCtrltype;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkDiag() {
		return pkDiag;
	}

	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}

	public String getPkDeptPharm() {
		return pkDeptPharm;
	}

	public void setPkDeptPharm(String pkDeptPharm) {
		this.pkDeptPharm = pkDeptPharm;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPkUnit() {
		return pkUnit;
	}

	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
}
