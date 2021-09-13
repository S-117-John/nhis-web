package com.zebone.nhis.common.module.base.ou;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 人员工作关系
 * @author Xulj
 *
 */
@Table(value="bd_ou_empjob")
public class BdOuEmpjob extends BaseModule {

	/**
	 * 人员工作关系主键
	 */
	@PK
	@Field(value="pk_empjob",id=KeyId.UUID)
	private String pkEmpjob;
	
	/**
	 * 人员编码
	 */
	@Field(value="code_emp")
	private String codeEmp;
	
	/**
	 * 人员主键
	 */
	@Field(value="pk_emp")
	private String pkEmp;
	
	/**
	 * 人员类别
	 */
	@Field(value="dt_emptype")
	private String dtEmptype;
	
	/**
	 * 所在部门
	 */
	@Field(value="pk_dept")
	private String pkDept;
	
	/**
	 * 是否主职
	 */
	@Field(value="is_main")
	private String isMain;
	
	/**
	 * 到职日期
	 */
	@Field(value="duty_date")
	private Date dutyDate;
	
	/**
	 * 职务
	 */
	@Field(value="jobname")
	private String jobname;
	
	/**
	 * 岗位
	 */
	@Field(value="dt_empjob")
	private String dtEmpjob;
	
	//离职日期
	@Field(value="date_left")
	private Date dateLeft;


	public Date getDateLeft() {
		return dateLeft;
	}

	public void setDateLeft(Date dateLeft) {
		this.dateLeft = dateLeft;
	}

	public String getPkEmpjob() {
		return pkEmpjob;
	}

	public void setPkEmpjob(String pkEmpjob) {
		this.pkEmpjob = pkEmpjob;
	}

	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getDtEmptype() {
		return dtEmptype;
	}

	public void setDtEmptype(String dtEmptype) {
		this.dtEmptype = dtEmptype;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public Date getDutyDate() {
		return dutyDate;
	}

	public void setDutyDate(Date dutyDate) {
		this.dutyDate = dutyDate;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getDtEmpjob() {
		return dtEmpjob;
	}

	public void setDtEmpjob(String dtEmpjob) {
		this.dtEmpjob = dtEmpjob;
	}

	
}
