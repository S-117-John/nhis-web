package com.zebone.nhis.sch.shcta.vo;

import java.util.Date;

public class SchTaEmp {

	private String pkOrg;//所属机构
	private String pkEmp; //人员
	private String nameEmp;//人员姓名
    private Date dateBegin; //实习开始日期
    private Date dateEnd; //实习结束日期
    private String euWorktype; //0 固定科室，1 科室轮转
    private Integer cycle; //轮转周期(天)
    private Integer deptCount;//分配次数，可待科室数
	public String getPkEmp() {
		return pkEmp;
	}
	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}
	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getEuWorktype() {
		return euWorktype;
	}
	public void setEuWorktype(String euWorktype) {
		this.euWorktype = euWorktype;
	}
	public Integer getCycle() {
		return cycle;
	}
	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}
	public String getNameEmp() {
		return nameEmp;
	}
	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}
	public Integer getDeptCount() {
		return deptCount;
	}
	public void setDeptCount(Integer deptCount) {
		this.deptCount = deptCount;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	
}
