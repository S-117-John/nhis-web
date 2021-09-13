package com.zebone.nhis.bl.ipcg.vo;


import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;

public class ApptOrdVo extends BaseModule{
	//就诊类型
	@Field(value="eu_pvtype")
	private String euPvtype;
	
	//就诊号
	@Field(value="code_pv")
	private String codePv;
	
	//姓名
	@Field(value="NAME_PI")
	private String namePi;
	
	//手机
	@Field(value="MOBILE")
	private String mobile;
	
	//医嘱pk
	@Field(value="PK_CNORD")
	private String pkCnord;
	
	//预约项目
	@Field(value="NAME_ORD")
	private String nameOrd;
		
	//日期
	@Field(value="DATE_APPT")
	private Date dateAppt;
		
	//申请病区
	@Field(value="PK_DEPT_NS")
	private String pkDeptNs;
		
	//票号
	@Field(value="TICKET_NO")
	private String ticketNo;
		
	//申请单号
	@Field(value="CODE_APPLY")
	private String codeApply;
		
	//登记人
	@Field(value="NAME_EMP_REG")
	private String nameEmpReg;
		
	//登记日期
	@Field(value="DATE_REG")
	private Date dateReg;
		
	//取消人
	@Field(value="NAME_EMP_CANCEL")
	private String nameEmpCancel;
		
	//取消日期
	@Field(value="DATE_CANCEL")
	private Date dateCancel;
	
	//开始时间
	private Date dateBegin;
	
	//结束
	private Date dateEnd;
	
	private String strDateBegin;
	
	private String strDateEnd;
	
	//当前科室
	private String pkDeptEx;
	
	public String getPkDeptEx() {
		return pkDeptEx;
	}
	
	public void setPkDeptEx(String pkDeptEx) {
		this.pkDeptEx = pkDeptEx;
	}

	
	public String getStrDateBegin() {
		return strDateBegin;
	}

	public void setStrDateBegin(String strDateBegin) {
		this.strDateBegin = strDateBegin;
	}

	public String getStrDateEnd() {
		return strDateEnd;
	}

	public void setStrDateEnd(String strDateEnd) {
		this.strDateEnd = strDateEnd;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getNameOrd() {
		return nameOrd;
	}

	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}

	public Date getDateAppt() {
		return dateAppt;
	}

	public void setDateAppt(Date dateAppt) {
		this.dateAppt = dateAppt;
	}

	public String getPkDeptNs() {
		return pkDeptNs;
	}

	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getCodeApply() {
		return codeApply;
	}

	public void setCodeApply(String codeApply) {
		this.codeApply = codeApply;
	}

	public String getNameEmpReg() {
		return nameEmpReg;
	}

	public void setNameEmpReg(String nameEmpReg) {
		this.nameEmpReg = nameEmpReg;
	}

	public Date getDateReg() {
		return dateReg;
	}

	public void setDateReg(Date dateReg) {
		this.dateReg = dateReg;
	}

	public String getNameEmpCancel() {
		return nameEmpCancel;
	}

	public void setNameEmpCancel(String nameEmpCancel) {
		this.nameEmpCancel = nameEmpCancel;
	}

	public Date getDateCancel() {
		return dateCancel;
	}

	public void setDateCancel(Date dateCancel) {
		this.dateCancel = dateCancel;
	}

	//医技服务
	@Field(value="pk_schsrv")
	private String pkSchsrv;
	
	//医技资源
	@Field(value="pk_schres")
	private String pkSchres;
	
	//申请科室
	@Field(value="pk_dept")
	private String pkDept;
	
	//状态
	@Field(value="eu_status")
	private String euStatus;
	
	
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

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
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

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
