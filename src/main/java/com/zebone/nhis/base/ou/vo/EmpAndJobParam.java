package com.zebone.nhis.base.ou.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.base.ou.BdOuEmpjob;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;

public class EmpAndJobParam {

	
	/**
	 * 人员信息
	 */
	private BdOuEmployee emp = new BdOuEmployee();
	
	/**
	 * 关系
	 */
	private List<BdOuEmpjob> empJobs = new ArrayList<BdOuEmpjob>();

	/**
	 * 是否发送推送消息到平台
	 */
	private String flagSendMsg;

	public String getFlagSendMsg() {
		return flagSendMsg;
	}

	public void setFlagSendMsg(String flagSendMsg) {
		this.flagSendMsg = flagSendMsg;
	}

	public List<BdOuEmpjob> getEmpJobs() {
		return empJobs;
	}

	public void setEmpJobs(List<BdOuEmpjob> empJobs) {
		this.empJobs = empJobs;
	}
	
	public BdOuEmployee getEmp() {
		return emp;
	}

	public void setEmp(BdOuEmployee emp) {
		this.emp = emp;
	}

	
}
