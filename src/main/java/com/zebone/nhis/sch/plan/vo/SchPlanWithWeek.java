package com.zebone.nhis.sch.plan.vo;

public class SchPlanWithWeek {
	
	private String pkSchplan;
	private String pkSchsrv;
	private String pkSchres;
	private String pkWorkcalendar;
	private String pkTicketrules;
	private Integer minutePer;
	private String flagTicket;
	private String pkDept;
	private String euSchclass;
	private String pkOrg;
	
	private String pkPlanweek;
	private String weekNo;
	private String pkDateslot;
	private Integer cntTotal;
	private Integer cntAppt;
	private Integer cntAdd;
	private String  plName;
	private String nameDateslot;
	private String euAppttype;
	private String pkDeptunit;
	private String pkEmp;
	private String euNoon;
	
	public String getEuNoon() {
		return euNoon;
	}
	public void setEuNoon(String euNoon) {
		this.euNoon = euNoon;
	}
	public String getPkSchplan() {
		return pkSchplan;
	}
	public void setPkSchplan(String pkSchplan) {
		this.pkSchplan = pkSchplan;
	}
	public String getPkSchsrv() {
		return pkSchsrv;
	}
	public void setPkSchsrv(String pkSchsrv) {
		this.pkSchsrv = pkSchsrv;
	}
	public String getPkSchres() {
		return pkSchres;
	}
	public void setPkSchres(String pkSchres) {
		this.pkSchres = pkSchres;
	}
	public String getPkWorkcalendar() {
		return pkWorkcalendar;
	}
	public void setPkWorkcalendar(String pkWorkcalendar) {
		this.pkWorkcalendar = pkWorkcalendar;
	}
	public String getPkTicketrules() {
		return pkTicketrules;
	}
	public void setPkTicketrules(String pkTicketrules) {
		this.pkTicketrules = pkTicketrules;
	}
	public Integer getMinutePer() {
		return minutePer;
	}
	public void setMinutePer(Integer minutePer) {
		this.minutePer = minutePer;
	}
	public String getFlagTicket() {
		return flagTicket;
	}
	public void setFlagTicket(String flagTicket) {
		this.flagTicket = flagTicket;
	}
	public String getWeekNo() {
		return weekNo;
	}
	public void setWeekNo(String weekNo) {
		this.weekNo = weekNo;
	}
	public String getPkDateslot() {
		return pkDateslot;
	}
	public void setPkDateslot(String pkDateslot) {
		this.pkDateslot = pkDateslot;
	}
	public Integer getCntTotal() {
		return cntTotal;
	}
	public void setCntTotal(Integer cntTotal) {
		this.cntTotal = cntTotal;
	}
	public Integer getCntAppt() {
		return cntAppt;
	}
	public void setCntAppt(Integer cntAppt) {
		this.cntAppt = cntAppt;
	}
	public Integer getCntAdd() {
		return cntAdd;
	}
	public void setCntAdd(Integer cntAdd) {
		this.cntAdd = cntAdd;
	}
	public String getPkPlanweek() {
		return pkPlanweek;
	}
	public void setPkPlanweek(String pkPlanweek) {
		this.pkPlanweek = pkPlanweek;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getEuSchclass() {
		return euSchclass;
	}
	public void setEuSchclass(String euSchclass) {
		this.euSchclass = euSchclass;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getPlName() {
		return plName;
	}
	public void setPlName(String plName) {
		this.plName = plName;
	}
	public String getNameDateslot() {
		return nameDateslot;
	}
	public void setNameDateslot(String nameDateslot) {
		this.nameDateslot = nameDateslot;
	}
	public String getEuAppttype() {
		return euAppttype;
	}
	public void setEuAppttype(String euAppttype) {
		this.euAppttype = euAppttype;
	}
	public String getPkDeptunit() {
		return pkDeptunit;
	}
	public void setPkDeptunit(String pkDeptunit) {
		this.pkDeptunit = pkDeptunit;
	}
	public String getPkEmp() {
		return pkEmp;
	}
	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}
	
}
