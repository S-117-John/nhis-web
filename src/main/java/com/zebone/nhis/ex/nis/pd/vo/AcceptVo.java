package com.zebone.nhis.ex.nis.pd.vo;

import java.util.List;

public class AcceptVo {
	private List<String> pkPdDes;//待签收的发药单主键list
	private String pkEmp;//签收人主键
	private String nameEmp;//签收人名称
	private String dateSign;//签收时间
	
	public List<String> getPkPdDes() {
		return pkPdDes;
	}
	public void setPkPdPes(List<String> pkPdDes) {
		this.pkPdDes = pkPdDes;
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
	public String getDateSign() {
		return dateSign;
	}
	public void setDateSign(String dateSign) {
		this.dateSign = dateSign;
	}
//	private List<SummaryVo> list;
//	private EmpVo emp;
//	public List<SummaryVo> getList() {
//		return list;
//	}
//	public void setList(List<SummaryVo> list) {
//		this.list = list;
//	}
//	public EmpVo getEmp() {
//		return emp;
//	}
//	public void setEmp(EmpVo emp) {
//		this.emp = emp;
//	}
}
