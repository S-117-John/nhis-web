package com.zebone.nhis.sch.shcta.vo;

import java.util.Date;

public class SchTaQryParam {

	   private String pkDept;
	   private String pkEmp;
       private String codeEmp;
       private String nameEmp;
       private Date dateBeginHead;
       private Date dateBeginTail;
       private Date dateEndHead;
       private Date dateEndTail;
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getCodeEmp() {
		return codeEmp;
	}
	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}
	public String getNameEmp() {
		return nameEmp;
	}
	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}
	public Date getDateBeginHead() {
		return dateBeginHead;
	}
	public void setDateBeginHead(Date dateBeginHead) {
		this.dateBeginHead = dateBeginHead;
	}
	public Date getDateBeginTail() {
		return dateBeginTail;
	}
	public void setDateBeginTail(Date dateBeginTail) {
		this.dateBeginTail = dateBeginTail;
	}
	public Date getDateEndHead() {
		return dateEndHead;
	}
	public void setDateEndHead(Date dateEndHead) {
		this.dateEndHead = dateEndHead;
	}
	public Date getDateEndTail() {
		return dateEndTail;
	}
	public void setDateEndTail(Date dateEndTail) {
		this.dateEndTail = dateEndTail;
	}
	public String getPkEmp() {
		return pkEmp;
	}
	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}     
}
