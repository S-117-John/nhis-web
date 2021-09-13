package com.zebone.nhis.pv.pub.vo;

import java.util.Date;

public class PageQryPvParam {

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

	/** 床位 */
	private String bedNo;

	/** 姓名 */
	private String namePi;

	/** 就诊状态0 登记，1 就诊，2 结束，3 结算，9 退诊 */
	private String euStatus;
	/** 入院日期开始 */
	private Date dateBeginKs;
	/** 入院日期截止 */
	private Date dateBeginJz;
	/** 出院日期开始 */
	private Date dateEndKs;
	/** 出院日期结束 */
	private Date dateEndJz;
	/** 结算开始时间 */
	private Date dateBeginSt;
	/** 结算截止时间 */
	private Date dateEndSt;

	/** 就诊类型1门诊，2急诊，3住院，4体检，5家庭病床 */
	private String euPvtype;

	/** 患者编码 */
	private String codePi;

	/** 就诊编码 */
	private String codePv;
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

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public Date getDateBeginKs() {
		return dateBeginKs;
	}

	public void setDateBeginKs(Date dateBeginKs) {
		this.dateBeginKs = dateBeginKs;
	}

	public Date getDateBeginJz() {
		return dateBeginJz;
	}

	public void setDateBeginJz(Date dateBeginJz) {
		this.dateBeginJz = dateBeginJz;
	}

	public Date getDateEndKs() {
		return dateEndKs;
	}

	public void setDateEndKs(Date dateEndKs) {
		this.dateEndKs = dateEndKs;
	}

	public Date getDateEndJz() {
		return dateEndJz;
	}

	public void setDateEndJz(Date dateEndJz) {
		this.dateEndJz = dateEndJz;
	}

	public Date getDateBeginSt() {
		return dateBeginSt;
	}

	public void setDateBeginSt(Date dateBeginSt) {
		this.dateBeginSt = dateBeginSt;
	}

	public Date getDateEndSt() {
		return dateEndSt;
	}

	public void setDateEndSt(Date dateEndSt) {
		this.dateEndSt = dateEndSt;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
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
