package com.zebone.nhis.bl.ipcg.vo;

import java.util.Date;

public class MedicalAppVo {
	private String pkCnord;// 医嘱主键
	private String bedNo;// 床位号
	private String namePi;// 姓名
	private String codeIp;//住院号
	private String codeApply;// 申请单号
	private String flagEmer;// 加急标志
	private String flagDurg;//物品标志
	private String nameOrd;// 医嘱名称
	private Double quanOcc;// 执行数量
	private Date dateSign;// 申请日期
	private String nameEmpOrd;// 申请医生
	private String nameDept;// 申请科室
	private String nameDeptNs;// 申请病区
	private Integer infantNo; // 婴儿序号
	private Integer ordsnParent; // 医嘱组号
    private String euStatus;//状态
    private String pkPv;
    private String pkPi;
    
	public String getFlagDurg() {
		return flagDurg;
	}

	public void setFlagDurg(String flagDurg) {
		this.flagDurg = flagDurg;
	}

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

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
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
	
	public String getCodeApply() {
		return codeApply;
	}

	public void setCodeApply(String codeApply) {
		this.codeApply = codeApply;
	}

	public String getFlagEmer() {
		return flagEmer;
	}

	public void setFlagEmer(String flagEmer) {
		this.flagEmer = flagEmer;
	}

	public String getNameOrd() {
		return nameOrd;
	}

	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}

	public Double getQuanOcc() {
		return quanOcc;
	}

	public void setQuanOcc(Double quanOcc) {
		this.quanOcc = quanOcc;
	}

	public Date getDateSign() {
		return dateSign;
	}

	public void setDateSign(Date dateSign) {
		this.dateSign = dateSign;
	}

	public String getNameEmpOrd() {
		return nameEmpOrd;
	}

	public void setNameEmpOrd(String nameEmpOrd) {
		this.nameEmpOrd = nameEmpOrd;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getNameDeptNs() {
		return nameDeptNs;
	}

	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}

	public Integer getInfantNo() {
		return infantNo;
	}

	public void setInfantNo(Integer infantNo) {
		this.infantNo = infantNo;
	}

	public Integer getOrdsnParent() {
		return ordsnParent;
	}

	public void setOrdsnParent(Integer ordsnParent) {
		this.ordsnParent = ordsnParent;
	}

}
