package com.zebone.nhis.ma.pub.zsba.vo;

import java.util.List;
import java.util.Set;

public class OrderExPrintVo {
	private String prtId;
	private String datePlan;//执行时间
	private String pkPv;//就诊主键
	private String sex;//性别
	private String namePi;//患者姓名
	private String bedNo;//床号
	private String nameDept;//就诊科室
	private String codeIp;//住院号
	private String pkPdDe;//要袋编码
	private String pkPi;
	private Set<String> pkExList;//执行单主键集合
	//执行单列表
    private List<OrderExVo> exlist;
    
	public String getPrtId() {
		return prtId;
	}
	public void setPrtId(String prtId) {
		this.prtId = prtId;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public Set<String> getPkExList() {
		return pkExList;
	}
	public void setPkExList(Set<String> pkExList) {
		this.pkExList = pkExList;
	}
	public String getPkPdDe() {
		return pkPdDe;
	}
	public void setPkPdDe(String pkPdDe) {
		this.pkPdDe = pkPdDe;
	}
	public String getDatePlan() {
		return datePlan;
	}
	public void setDatePlan(String datePlan) {
		this.datePlan = datePlan;
	}
	public List<OrderExVo> getExlist() {
		return exlist;
	}
	public void setExlist(List<OrderExVo> exlist) {
		this.exlist = exlist;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
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
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
    
    
}
