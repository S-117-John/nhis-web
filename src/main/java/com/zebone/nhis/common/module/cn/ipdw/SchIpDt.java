package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="SCH_IP_DT")
public class SchIpDt extends BaseModule{

	@PK
	@Field(value="PK_SCHIPDT",id=KeyId.UUID)
    private String pkSchipdt;

	@Field(value="PK_SCHIP")
    private String pkSchip;
	
	@Field(value="PK_EMP")
	private String pkEmp;
	
	@Field(value="NAME_EMP")
	private String nameEmp;
	
	@Field(value="DATE_WORK")
	private Date dateWork;
	
	@Field(value="WEEKNO")
	private int weekno;
	
	@Field(value="PK_DATESOLT")
	private String pkDatesolt;
	
	@Field(value="DT_SCHTYPEDR")
	private String dtSchtypedr;
	
	@Field(value="DT_SCHTYPENS")
	private String dtSchtypens;
	
	@Field(value="NOTE")
	private String note;

	public String getPkSchipdt() {
		return pkSchipdt;
	}

	public void setPkSchipdt(String pkSchipdt) {
		this.pkSchipdt = pkSchipdt;
	}

	public String getPkSchip() {
		return pkSchip;
	}

	public void setPkSchip(String pkSchip) {
		this.pkSchip = pkSchip;
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

	public Date getDateWork() {
		return dateWork;
	}

	public void setDateWork(Date dateWork) {
		this.dateWork = dateWork;
	}

	public int getWeekno() {
		return weekno;
	}

	public void setWeekno(int weekno) {
		this.weekno = weekno;
	}

	public String getPkDatesolt() {
		return pkDatesolt;
	}

	public void setPkDatesolt(String pkDatesolt) {
		this.pkDatesolt = pkDatesolt;
	}

	public String getDtSchtypedr() {
		return dtSchtypedr;
	}

	public void setDtSchtypedr(String dtSchtypedr) {
		this.dtSchtypedr = dtSchtypedr;
	}

	public String getDtSchtypens() {
		return dtSchtypens;
	}

	public void setDtSchtypens(String dtSchtypens) {
		this.dtSchtypens = dtSchtypens;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
