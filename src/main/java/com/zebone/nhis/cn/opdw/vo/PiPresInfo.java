package com.zebone.nhis.cn.opdw.vo;

import java.util.Date;

public class PiPresInfo {
	
	private String pkPres;
    private Date dateBegin;
    private String euPvtype;
    private String nameDept;
    private String nameEmpOrd;
    private String  Prestype;
    private String presNo;
    
    private String dtPrestype;	//处方类型
    
    /**查询参数*/
    private Date dateEnd;
    private String pkPi;
    
    
    
	public String getDtPrestype() {
		return dtPrestype;
	}
	public void setDtPrestype(String dtPrestype) {
		this.dtPrestype = dtPrestype;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getPkPres() {
		return pkPres;
	}
	public void setPkPres(String pkPres) {
		this.pkPres = pkPres;
	}
	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	public String getEuPvtype() {
		return euPvtype;
	}
	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getNameEmpOrd() {
		return nameEmpOrd;
	}
	public void setNameEmpOrd(String nameEmpOrd) {
		this.nameEmpOrd = nameEmpOrd;
	}
	public String getPrestype() {
		return Prestype;
	}
	public void setPrestype(String prestype) {
		Prestype = prestype;
	}
	public String getPresNo() {
		return presNo;
	}
	public void setPresNo(String presNo) {
		this.presNo = presNo;
	}
 
}
