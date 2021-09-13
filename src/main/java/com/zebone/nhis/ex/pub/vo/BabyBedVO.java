package com.zebone.nhis.ex.pub.vo;

import java.util.Date;

public class BabyBedVO{
	
	/**
	 * 入院科室
	 */
    private String pkDept ;

    /**
	 * 入院病区
	 */
    private String pkDeptNs ;

    /**
	 * 床位主键
	 */
    private String pkBed ;
    
    /**
   	 * 床位编码
   	 */
    private String codeBed ;

    /**
	 * 医疗组
	 */
    private String pkIpWg ;

    /**
	 * 管床医生
	 */
    private String pkIpPsnMain ;
    
    /**
	 * 管床医生名称
	 */
    private String nameIpPsnMain;

    /**
	 * 管床护士
	 */
    private String pkIpPsnNs ;
    
    /**
   	 * 管床护士名称
   	 */
    private String nameIpPsnNs;
    
    /**
	 * 入科日期
	 */
    private Date dateAdmit ;
    
    /**
   	 * 就诊主键
   	 */
    private String pkPv ;
    
	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkDeptNs() {
		return pkDeptNs;
	}

	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}

	public String getPkBed() {
		return pkBed;
	}

	public void setPkBed(String pkBed) {
		this.pkBed = pkBed;
	}

	public String getPkIpWg() {
		return pkIpWg;
	}

	public void setPkIpWg(String pkIpWg) {
		this.pkIpWg = pkIpWg;
	}

	public String getPkIpPsnMain() {
		return pkIpPsnMain;
	}

	public void setPkIpPsnMain(String pkIpPsnMain) {
		this.pkIpPsnMain = pkIpPsnMain;
	}

	public String getPkIpPsnNs() {
		return pkIpPsnNs;
	}

	public void setPkIpPsnNs(String pkIpPsnNs) {
		this.pkIpPsnNs = pkIpPsnNs;
	}

	public Date getDateAdmit() {
		return dateAdmit;
	}

	public void setDateAdmit(Date dateAdmit) {
		this.dateAdmit = dateAdmit;
	}

	public String getCodeBed() {
		return codeBed;
	}

	public void setCodeBed(String codeBed) {
		this.codeBed = codeBed;
	}

	public String getNameIpPsnMain() {
		return nameIpPsnMain;
	}

	public void setNameIpPsnMain(String nameIpPsnMain) {
		this.nameIpPsnMain = nameIpPsnMain;
	}

	public String getNameIpPsnNs() {
		return nameIpPsnNs;
	}

	public void setNameIpPsnNs(String nameIpPsnNs) {
		this.nameIpPsnNs = nameIpPsnNs;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	
}
