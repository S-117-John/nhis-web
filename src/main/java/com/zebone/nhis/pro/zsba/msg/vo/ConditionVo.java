package com.zebone.nhis.pro.zsba.msg.vo;

import java.util.List;

public class ConditionVo {
    //起始日期
    private String dateS;
    //截止日期
    private String dateE;
    public String getDateS() {
		return dateS;
	}
	public void setDateS(String dateS) {
		this.dateS = dateS;
	}
	public String getDateE() {
		return dateE;
	}
	public void setDateE(String dateE) {
		this.dateE = dateE;
	}
	public String getPiNo() {
		return piNo;
	}
	public void setPiNo(String piNo) {
		this.piNo = piNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPvNo() {
		return pvNo;
	}
	public void setPvNo(String pvNo) {
		this.pvNo = pvNo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	//患者编号
    private String piNo;
    //患者姓名
    private String name;
    //就诊编号
    private String pvNo;
    //消息主题
    private String subject;
    //消息源头
    private String root;
    //消息级别
    private String level;
    //消息状态
    private String state;
    //就诊类型
    private String type;
    //发送部门
    private String dept;
    //机构id
    private String organizationId;
    //用户Code
    private List<String> userCode;
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public List<String> getUserCode() {
		return userCode;
	}
	public void setUserCode(List<String> userCode) {
		this.userCode = userCode;
	}

}
