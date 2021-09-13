package com.zebone.nhis.bl.pub.vo;

import java.util.List;

/**
 * 描述诊间支付的输入数据
 * @author Administrator
 */
public class ClinicPayInputVo {

	private String pkPi;					//	患者 pk_pi
	private String pkPv;					//	患者就诊 pk_pv
	private String euPvtype;      			//  就诊类型 eu_pvtype
	private String pkEmp;			//	操作人 pk_emp
	private String nameEmp;			//	操作人姓名 name_emp
	private String pkOrgSt;			//	操作机构 pk_org_st
	private String pkDeptSt;		//	操作部门 pk_dept_st
	private List<String> pkPress;   	    // 处方主键
	private List<String> pkCnords;   	    // 医嘱主键
	
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getEuPvtype() {
		return euPvtype;
	}
	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}
	public String getPkEmp() {
		return pkEmp;
	}
	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}
	public String getNameEmp() {
		return nameEmp;
	}
	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}
	public String getPkOrgSt() {
		return pkOrgSt;
	}
	public void setPkOrgSt(String pkOrgSt) {
		this.pkOrgSt = pkOrgSt;
	}
	public String getPkDeptSt() {
		return pkDeptSt;
	}
	public void setPkDeptSt(String pkDeptSt) {
		this.pkDeptSt = pkDeptSt;
	}
	public List<String> getPkPress() {
		return pkPress;
	}
	public void setPkPress(List<String> pkPress) {
		this.pkPress = pkPress;
	}
	public List<String> getPkCnords() {
		return pkCnords;
	}
	public void setPkCnords(List<String> pkCnords) {
		this.pkCnords = pkCnords;
	}
	
}
