package com.zebone.nhis.base.drg.vo;

import java.util.Date;

/**
 * drg信息平台用
 * @author dell
 *
 */
public class PvEncounterVo {
	/// <summary>
    /// 就诊主键
    /// </summary>
    public String pkPv ;
    /// <summary>
    /// 病案号
    /// </summary>
    public String codeIp ;
    /// <summary>
    /// 住院次数
    /// </summary>
    public String ipTimes ;
    /// <summary>
    /// 姓名
    /// </summary>
    public String namePi ;
    /// <summary>
    /// 性别
    /// </summary>
    public String dtSex ;
    /// <summary>
    /// 出生日期
    /// </summary>
    public Date birthDate ;
    /// <summary>
    /// 出院日期
    /// </summary>
    public Date dateEnd ;
    /// <summary>
    /// 科室pk
    /// </summary>
    public String pkDept ;
    /// <summary>
    /// 科室
    /// </summary>
    public String nameDept ;

    /// <summary>
    /// 医生PK
    /// </summary>
    public String pkEmpPhy ;
    /// <summary>
    /// 医生
    /// </summary>
    public String nameEmpPhy ;
    /// <summary>
    /// 状态
    /// </summary>
    public String euStatusDrg ;

    /// <summary>
    /// 出院开始日期
    /// </summary>
    public Date beginDateEnd ;
    /// <summary>
    /// 出院结束日期
    /// </summary>
    public Date endDateEnd ;

    /// <summary>
    /// 操作开始日期
    /// </summary>
    public Date beginDateDrg ;
    /// <summary>
    /// 操作结束日期
    /// </summary>
    public Date endDateDrg ;
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public String getIpTimes() {
		return ipTimes;
	}
	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getDtSex() {
		return dtSex;
	}
	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getPkEmpPhy() {
		return pkEmpPhy;
	}
	public void setPkEmpPhy(String pkEmpPhy) {
		this.pkEmpPhy = pkEmpPhy;
	}
	public String getNameEmpPhy() {
		return nameEmpPhy;
	}
	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}
	public String getEuStatusDrg() {
		return euStatusDrg;
	}
	public void setEuStatusDrg(String euStatusDrg) {
		this.euStatusDrg = euStatusDrg;
	}
	public Date getBeginDateEnd() {
		return beginDateEnd;
	}
	public void setBeginDateEnd(Date beginDateEnd) {
		this.beginDateEnd = beginDateEnd;
	}
	public Date getEndDateEnd() {
		return endDateEnd;
	}
	public void setEndDateEnd(Date endDateEnd) {
		this.endDateEnd = endDateEnd;
	}
	public Date getBeginDateDrg() {
		return beginDateDrg;
	}
	public void setBeginDateDrg(Date beginDateDrg) {
		this.beginDateDrg = beginDateDrg;
	}
	public Date getEndDateDrg() {
		return endDateDrg;
	}
	public void setEndDateDrg(Date endDateDrg) {
		this.endDateDrg = endDateDrg;
	}
	
    
}
