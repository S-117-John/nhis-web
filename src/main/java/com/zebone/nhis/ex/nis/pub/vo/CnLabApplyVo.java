package com.zebone.nhis.ex.nis.pub.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;

/**
 * 带 申请单号、 医嘱号、父医嘱号的检验申请单
 * @author 85102
 *
 */
public class CnLabApplyVo extends CnLabApply {
	
	/**
	 * 申请单号
	 */
	private String codeApp;
	
	/**
	 * 医嘱号
	 */
	private String ordsnNo;
	
	/**
	 * 父医嘱号
	 */
	private String ordsnParentNo;

	/**
	 * 医嘱状态
	 */
	private String euStatusOrd;

	/**
	 * 医嘱名称
	 */
	private String ordName;
	/**
	 * 住院号
	 */
	private String codeIp;
	/**
	 * 执行科室
	 */
	private String pkDeptOcc;
	/**
	 * 执行机构
	 */
	private String pkOrgOcc;
	/**
	 * 就诊主键
	 */
	private String pkPv;
	/**
	 * 患者主键
	 */
	private String pkPi;

	public String getOrdName() {
		return ordName;
	}

	public void setOrdName(String ordName) {
		this.ordName = ordName;
	}

	@Override
	public String getEuStatusOrd() {
		return euStatusOrd;
	}

	@Override
	public void setEuStatusOrd(String euStatusOrd) {
		this.euStatusOrd = euStatusOrd;
	}

	public String getCodeApp() {
		return codeApp;
	}

	public void setCodeApp(String codeApp) {
		this.codeApp = codeApp;
	}

	public String getOrdsnNo() {
		return ordsnNo;
	}

	public void setOrdsnNo(String ordsnNo) {
		this.ordsnNo = ordsnNo;
	}

	public String getOrdsnParentNo() {
		return ordsnParentNo;
	}

	public void setOrdsnParentNo(String ordsnParentNo) {
		this.ordsnParentNo = ordsnParentNo;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	@Override
	public String getPkPi() {
		return pkPi;
	}

	@Override
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	@Override
	public String getPkPv() {
		return pkPv;
	}

	@Override
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkOrgOcc() {
		return pkOrgOcc;
	}

	public void setPkOrgOcc(String pkOrgOcc) {
		this.pkOrgOcc = pkOrgOcc;
	}

	public String getPkDeptOcc() {
		return pkDeptOcc;
	}

	public void setPkDeptOcc(String pkDeptOcc) {
		this.pkDeptOcc = pkDeptOcc;
	}
}
