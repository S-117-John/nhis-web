package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.Date;

public class OutPvPatiVo {
    private String pkPv ;//患者就诊主键                        
    private String pkPi ;//患者主键                       
    private String namePi ;//姓名                        
    private String codePv ;//就诊编码                        
    private String codeIp ;//住院号                        
    private String bedNo ;//床号                        
    private String dtSex ;//性别                      
    private String age ;//年龄                        
    private String nameHp ;//医保                        
    private String ipTimes ;//就诊次数                                             
    private String nameDept;//就诊科室
    private String pkDept;//就诊科室主键                      
    private String nameDeptNs;//就诊病区
    private String pkDeptNs;//就诊病区主键                        
    private String euStatus ;//就诊状态                        
    private String nameEmpPhy ;//主治医生
    private String pkEmpPhy ;//主治医生主键                  
    private String nameEmpNs ;//主管护士
    private String pkEmpNs ;//主管护士主键                       
    private Date dateBegin ;//入院日期                        
    private Date dateAdmit ;//入科日期                        
    private Date dateEnd ;//出院日期                        
    private Date dateCancel ;//退诊日期                       
    private Date archiveDate ;//归档日期                        
    private Date finalQcDate ;//终末置空日期                      
    private String flagCancel ;// 退诊标志                       
    private String flagIn ;//在院标志               
    private String flagSettle ;//结算标志
    public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
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
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getDtSex() {
		return dtSex;
	}
	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getNameHp() {
		return nameHp;
	}
	public void setNameHp(String nameHp) {
		this.nameHp = nameHp;
	}
	public String getIpTimes() {
		return ipTimes;
	}
	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getNameDeptNs() {
		return nameDeptNs;
	}
	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}
	public String getPkDeptNs() {
		return pkDeptNs;
	}
	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	public String getNameEmpPhy() {
		return nameEmpPhy;
	}
	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}
	public String getPkEmpPhy() {
		return pkEmpPhy;
	}
	public void setPkEmpPhy(String pkEmpPhy) {
		this.pkEmpPhy = pkEmpPhy;
	}
	public String getNameEmpNs() {
		return nameEmpNs;
	}
	public void setNameEmpNs(String nameEmpNs) {
		this.nameEmpNs = nameEmpNs;
	}
	public String getPkEmpNs() {
		return pkEmpNs;
	}
	public void setPkEmpNs(String pkEmpNs) {
		this.pkEmpNs = pkEmpNs;
	}
	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	public Date getDateAdmit() {
		return dateAdmit;
	}
	public void setDateAdmit(Date dateAdmit) {
		this.dateAdmit = dateAdmit;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public Date getDateCancel() {
		return dateCancel;
	}
	public void setDateCancel(Date dateCancel) {
		this.dateCancel = dateCancel;
	}
	public Date getArchiveDate() {
		return archiveDate;
	}
	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}
	public Date getFinalQcDate() {
		return finalQcDate;
	}
	public void setFinalQcDate(Date finalQcDate) {
		this.finalQcDate = finalQcDate;
	}
	public String getFlagCancel() {
		return flagCancel;
	}
	public void setFlagCancel(String flagCancel) {
		this.flagCancel = flagCancel;
	}
	public String getFlagIn() {
		return flagIn;
	}
	public void setFlagIn(String flagIn) {
		this.flagIn = flagIn;
	}
	public String getFlagSettle() {
		return flagSettle;
	}
	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}
	
    
}
