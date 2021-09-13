package com.zebone.nhis.pv.pub.vo;

import java.util.Date;

public class PageQryAuditParam {

	/** 所属机构 */
	private String pkOrg;
	/** 所属院区 */
	private String pkOrgArea;
	/** 入院科室 */
	private String pkDept;

	/** 入院病区 */
	private String pkDeptNs;

	/** 住院号 */
	private String codeIp;

	/** 姓名 */
	private String namePi;

	/** 床位 */
	private String bedNo;

	private String idNo;

	/** 审核类型0待审1已审 */
	private String flagCheckIn;

	/** 每页行数 */
	private int pageSize;
	/** 页码 */
	private int pageIndex;
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getPkOrgArea() {
		return pkOrgArea;
	}
	public void setPkOrgArea(String pkOrgArea) {
		this.pkOrgArea = pkOrgArea;
	}
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
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getFlagCheckIn() {
		return flagCheckIn;
	}
	public void setFlagCheckIn(String flagCheckIn) {
		this.flagCheckIn = flagCheckIn;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	
}
